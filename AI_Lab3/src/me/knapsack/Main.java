package me.knapsack;

import java.io.File;

import me.genetic.GeneralGeneticAlgorithm;
import me.genetic.RandomCross;
import me.genetic.SequenceCross;
import me.genetic.IGeneticAssistant;

/**
 * @author Duocai Wu
 * @date 2016年12月22日
 * @time 下午2:31:58
 *
 */
public class Main {
	public static void main(String[] args) {
		double mutateP = 0.15;
		double crossP = 0.8;
		int scale = 1000;
		int iterationMax = 10000;
		
		File dir = new File("testknapsack");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			//IGeneticAssistant assistant = new KnapSack1();
			IGeneticAssistant assistant = new BasicKnapSack();
			((BasicKnapSack)assistant).init(files[i]);
			
//			GeneralGeneticAlgorithm algorithm = 
//					new SequenceCross(iterationMax, scale,
//							assistant, crossP, mutateP);
			GeneralGeneticAlgorithm algorithm = 
					new RandomCross(iterationMax, scale,
							assistant, crossP, mutateP);
			
			algorithm.start(true);
			
			System.out.print(algorithm.getBestGeneration()+":");
			System.out.print(algorithm.getBestFitness()+",");
			System.out.println(algorithm.getBestIndividual().toString());
		}
	}
}
