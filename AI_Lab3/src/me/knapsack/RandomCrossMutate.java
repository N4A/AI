package me.knapsack;

import java.util.Random;

import me.genetic.Individual;

/**
 * @author Duocai Wu
 * @date 2016年12月22日
 * @time 下午2:34:56
 *
 */
public class RandomCrossMutate extends BasicKnapSack {

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#cross(me.genetic.Individual, me.genetic.Individual)
	 */
	@Override
	public void cross(Individual<?> ivd1, Individual<?> ivd2) {
		Byte[] code1 = (Byte[])ivd1.getCode();
		Byte[] code2 = (Byte[])ivd2.getCode();
		
		Random random = new Random();
		int len = random.nextInt(codeLen);
		for (int i = 0; i < len; i++) {
			int pos = random.nextInt(codeLen);
			byte tmp = code1[pos];
			code1[pos] = code2[pos];
			code2[pos] = tmp;
		}
	}

	/* (non-Javadoc)
	 * @see me.genetic.IGeneticAssistant#mutate(me.genetic.Individual)
	 */
	@Override
	public void mutate(Individual<?> ivd) {
		Byte[] code = (Byte[])ivd.getCode(); 
		
		Random random = new Random();
		int len = random.nextInt(codeLen);
		for (int i = 0; i < len; i++) {
			int pos = random.nextInt(codeLen);
			code[pos] = (byte) (1-code[pos]);
		}
	}
}
