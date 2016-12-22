package me.knapsack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import me.genetic.IGeneticAssistant;
import me.genetic.Individual;

/**
 * @author Duocai Wu
 * @date 2016年12月21日
 * @time 下午5:33:24
 *
 */
public class BasicKnapSack implements IGeneticAssistant {
	protected int codeLen;
	protected int capacity;
	protected double[] weight;
	protected double[] value;

	/**
	 *init the messages from the file
	 * @param file
	 * @date 2016年12月21日
	 * @time 下午6:17:51
	 */
	public void init(File file) {
		try {
			@SuppressWarnings("resource")
			BufferedReader bReader = new BufferedReader(
					new FileReader(file));
			String[] first = bReader.readLine().trim().split(" ");
			capacity = Integer.parseInt(first[0]);
			codeLen = Integer.parseInt(first[1]);
			
			//read weight and value
			weight = new double[codeLen];
			value = new double[codeLen];
			for (int i = 0; i < codeLen; i++) {
				String[] wv = bReader.readLine().trim().split(" ");
				weight[i] = Double.parseDouble(wv[0]);
				value[i] = Double.parseDouble(wv[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#randomInitIndividual()
	 */
	@Override
	public Individual<?> randomInitIndividual() {
		Byte[] code = new Byte[codeLen];
		Random random = new Random();
		for (int i = 0; i < code.length; i++) {
			code[i] = (byte) (random.nextInt(1000)%2);//0 or 1
		}
		Individual<Byte> individual = new Individual<Byte>(code);
		return individual;
	}

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#fitness(me.genetic.Individual)
	 */
	@Override
	public double fitness(Individual<?> ivd) {
		Byte[] code = (Byte[])ivd.getCode();
		double totalWeight = 0;
		double totalValue = 0;
		
		//calculate weight and value
		for (int i = 0; i < codeLen; i++) {
			if (code[i] == 1) {
				totalWeight += weight[i];
				totalValue += value[i];
			}
		}
		
		if (totalWeight > capacity) {
			totalValue = 1e-10;
		}

		return totalValue;
	}

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#cross(me.genetic.Individual, me.genetic.Individual)
	 */
	@Override
	public void cross(Individual<?> ivd1, Individual<?> ivd2) {
		Byte[] code1 = (Byte[])ivd1.getCode();
		Byte[] code2 = (Byte[])ivd2.getCode();
		
		Random random = new Random();
		int crossStart = random.nextInt(codeLen);
		int crossEnd = random.nextInt(codeLen);
		if (crossStart > crossEnd) {
			int tmp = crossEnd;
			crossEnd = crossStart;
			crossStart = tmp;
		}
		int crossLen = (crossEnd - crossStart)/10;
		crossLen = crossLen >= 1 ? crossLen : 1;
		for (int i = 0; i < crossLen; i++) {
			byte tmp = code1[crossStart + i];
			code1[crossStart + i] = code2[crossStart + i];
			code2[crossStart + i] = tmp;
		}
	}

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#mutate(me.genetic.Individual)
	 */
	@Override
	public void mutate(Individual<?> ivd) {
		Byte[] code = (Byte[])ivd.getCode(); 
		
		Random random = new Random();
		int crossStart = random.nextInt(codeLen);
		int crossEnd = random.nextInt(codeLen);
		if (crossStart > crossEnd) {
			int tmp = crossEnd;
			crossEnd = crossStart;
			crossStart = tmp;
		}
		int crossLen = (crossEnd - crossStart)/10;
		crossLen = crossLen >= 1 ? crossLen : 1;
		for (int i = 0; i < crossLen; i++) {
			byte tmp = code[crossStart + i];
			code[crossStart + i] = (byte) (1-tmp);
		}
	}

	@Override
	public Individual<?> copy(Individual<?> individual) {
		Byte[] code = (Byte[]) individual.getCode();
		Byte[] newCode = new Byte[code.length];
		for (int i = 0; i < newCode.length; i++) {
			newCode[i] = code[i];
		}
		return new Individual<>(newCode);
	}
}
