package me.tsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.genetic.IGeneticAssistant;
import me.genetic.Individual;

/**
 * @author Duocai Wu
 * @date 2016年12月22日
 * @time 下午3:47:23
 *
 */
public class BasicTSP implements IGeneticAssistant {
	protected int codeLen;
	protected Position[] positions;
	
	public void init(File file) {
		try {
			@SuppressWarnings("resource")
			BufferedReader bReader = new BufferedReader(
					new FileReader(file));
			String first = bReader.readLine().trim();
			codeLen = Integer.parseInt(first);
			
			//read weight and value
			positions = new Position[codeLen];
			for (int i = 0; i < codeLen; i++) {
				String[] ixy = bReader.readLine().trim().split(" ");
				int index = Integer.parseInt(ixy[0]);
				double x = Double.parseDouble(ixy[1]);
				double y = Double.parseDouble(ixy[2]);
				positions[index-1] = new Position(x, y);
			}
			
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#randomInitIndividual()
	 */
	@Override
	public Individual<?> initIndividual() {
		Random random = new Random();
		Integer[] code = new Integer[codeLen];
		List<Integer> iList = new ArrayList<>();
		for (int i = 0; i < codeLen; i++) {
			iList.add(i);
		}
		for (int i = 0; i < code.length; i++) {
			int rm = random.nextInt(codeLen - i);
			code[i] = iList.get(rm);
			iList.remove(rm);
		}
		Individual<Integer> individual = new Individual<>(code);
		return individual;
	}

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#fitness(me.genetic.Individual)
	 */
	@Override
	public double fitness(Individual<?> ivd) {
		Integer[] code = (Integer[]) ivd.getCode();
		
		double totalDistance = 0;
		
		for (int i = 0; i < code.length; i++) {
			totalDistance += calculateDistance(
					positions[code[i]],
					positions[code[(i+1)%codeLen]]);
		}
		
		return 1/totalDistance;
	}

	//calculate distance between pos1 and pos2
	private double calculateDistance(Position pos1, Position pos2) {
		return Math.sqrt(
				Math.pow(pos1.getX()-pos2.getX(),2)
				+ Math.pow(pos1.getY()-pos2.getY(), 2)
				);
	}

	/**
	 * Start from the second cut point of one parent, the cities from the
	 * other parent are copied in the same order, omitting cities already
	 * present. When the end of the string is reached, continue on from
	 * the beginning. 
	 *
	 * @param ivd1
	 * @param ivd2
	 * @date 2016年12月22日
	 * @time 下午4:53:06
	 */
	@Override
	public void cross(Individual<?> ivd1, Individual<?> ivd2) {
		Integer[] code1 = (Integer[]) ivd1.getCode();
		Integer[] code2 = (Integer[]) ivd2.getCode();
//		p1 = ( 1 9 2 | 4 6 5 7 | 8 3 )
//		p2 = ( 4 5 9 | 1 8 7 6 | 2 3 )
//		tmp1 = 2 3 4 5 9 1 8 7 6
//		c1 = ( 2 3 9 | 4 6 5 7 | 1 8 )
//		c2 = ( 3 9 2 | 1 8 7 6 | 4 5 )
		Random random = new Random();
		int cutStart = random.nextInt(codeLen);//likely pos of 4
		int cutEnd = random.nextInt(codeLen);//likely pos of 8
		if (cutStart > cutEnd) {//ensure end < start
			int tmp = cutEnd;
			cutEnd = cutStart;
			cutStart = tmp;
		}
		int[] tmp1 = newSequence(code1,cutEnd);
		int[] static1 = getPiece(code1,cutStart,cutEnd);
		int[] tmp2 = newSequence(code2, cutEnd);
		int[] static2 = getPiece(code2, cutStart, cutEnd);
		
		//cross code1
		cross(code1,cutStart,cutEnd,static1,tmp2);
		//cross code2
		cross(code2, cutStart, cutEnd, static2, tmp1);
	}

	//cross code except for the static area
	//using gene in newSequence
	private void cross(Integer[] code, 
			int staticStart, int staticEnd, 
			int[] staticArea, int[] newSequence) {
		int j = 0;
		for (int i = 0; i < code.length; i++) {
			if (i < staticEnd && i >= staticStart) {
				continue;//this area should not be changed
			}
			for (; j < newSequence.length; j++) {
				if (!isMember(newSequence[j],staticArea)) {
					code[i] = newSequence[j];
					j++;//set j to next
					break;
				}
			}
		}
	}

	//check if the num is a member of array
	private boolean isMember(int num, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (num == array[i]) {
				return true;
			}
		}
		return false;
	}

	//return the piece of the array from start to end
	//not including the end
	private int[] getPiece(Integer[] code, int start, int end) {
		int[] newCode = new int[end - start];
		for (int i = 0; i < newCode.length; i++) {
			newCode[i] = code[start + i];
		}
		return newCode;
	}

	//get newCode start from the position
	//start in code
	private int[] newSequence(Integer[] code, int start) {
		int[] newCode = new int[code.length];
		for (int i = 0; i < newCode.length; i++) {
			newCode[i] = code[(start+i)%code.length];
		}
		return newCode;
	}

	/**
	 * from:
	 * p1 = ( 1 9 2 | 4 6 5 7 | 8 3 )
	 * to:
	 * c1 = ( 1 9 2 | 7 5 6 4 | 8 3 )
	 *
	 * @param ivd
	 * @date 2016年12月22日
	 * @time 下午7:26:53
	 */
	@Override
	public void mutate(Individual<?> ivd) {
		Integer[] code = (Integer[]) ivd.getCode();
//		p1 = ( 1 9 2 | 4 6 5 7 | 8 3 )
//		c1 = ( 1 9 2 | 7 5 6 4 | 8 3 )
		Random random = new Random();
		int cutStart = random.nextInt(codeLen);//likely pos of 4
		int cutEnd = random.nextInt(codeLen);//likely pos of 8
		if (cutStart > cutEnd) {//ensure end < start
			int tmp = cutEnd;
			cutEnd = cutStart;
			cutStart = tmp;
		}
		
		int[] inverse = getPiece(code,cutStart,cutEnd);
		int len = cutEnd - cutStart;
		//inverse for the area from start to and
		for (int i = 0; i < len; i++) {
			code[cutStart + i] = inverse[len-i-1];
		}
	}

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#copy(me.genetic.Individual)
	 */
	@Override
	public Individual<?> copy(Individual<?> individual) {
		Integer[] code = (Integer[]) individual.getCode();
		Integer[] newCode = new Integer[code.length];
		for (int i = 0; i < newCode.length; i++) {
			newCode[i] = code[i];
		}
		return new Individual<>(newCode);
	}

}
