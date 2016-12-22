package me.tsp;

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
		double mutateP = 0.04;
		double crossP = 0.12;
		int scale = 5000;
		int iterationMax = 18000;
		
		File dir = new File("testtsp");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			//IGeneticAssistant assistant = new KnapSack1();
			IGeneticAssistant assistant = new BasicTSP();
			((BasicTSP)assistant).init(files[i]);
			
			GeneralGeneticAlgorithm algorithm = 
					new SequenceCross(iterationMax, scale,
							assistant, crossP, mutateP);
//			algorithm = 
//					new RandomCross(iterationMax, scale,
//							assistant, crossP, mutateP);
			
			algorithm.start(true);
			
			System.out.print(algorithm.getBestGeneration()+":");
			System.out.print(1/algorithm.getBestFitness()+",");
			System.out.println(algorithm.getBestIndividual().toString());
		}
	}
}
