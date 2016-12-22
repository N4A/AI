package me.knapsack;

import java.util.Random;

import me.genetic.Individual;

/**
 * @author Duocai Wu
 * @date 2016年12月22日
 * @time 下午3:33:36
 *
 */
public class SequenceCrossMutate extends BasicKnapSack {

	/**
	 * 
	 */
	public SequenceCrossMutate() {
		// TODO Auto-generated constructor stub
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

}
