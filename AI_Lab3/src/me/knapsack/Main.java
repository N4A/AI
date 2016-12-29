package me.knapsack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	static double[] mutatePs = {0.05,0.1,0.15,0.2,0.3,0.5,0.8};
	static double[] crossPs = {0.3,0.5,0.7,0.85};
	static int[] scales = {500,1000,1500};
	public static void main(String[] args) {	
		train();
		//test();
	}
	
	//训练报告
	static void train() {
		for (int i = 0; i < crossPs.length; i++) {
			for (int j = 0; j < mutatePs.length; j++) {
				for (int j2 = 0; j2 < scales.length; j2++) {
					doTrain(crossPs[i], mutatePs[j],
							scales[j2], 10000);
				}
			}
		}
	}
	//do train
	static void doTrain(double crossP, double mutateP,
			int scale, int iterationMax) {
		File dir = new File("testknapsack");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			BufferedWriter bWriter = null;
			try {
				bWriter = new BufferedWriter(
						new FileWriter(
								new File("outputs/knapsack/srcm/"
									+"test"+i
									+"cp"+crossP
									+"mp"+mutateP
									+"s"+scale+".txt")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//IGeneticAssistant assistant = new KnapSack1();
			IGeneticAssistant assistant = new BasicKnapSack();
			assistant = new RandomCrossMutate();
			((BasicKnapSack)assistant).init(files[i]);
			
			GeneralGeneticAlgorithm algorithm = 
					new SequenceCross(iterationMax, scale,
							assistant, crossP, mutateP,bWriter);
//			//random cross
//			algorithm = 
//					new RandomCross(iterationMax, scale,
//							assistant, crossP, mutateP,bWriter);
			
			algorithm.start(true);
			
			System.out.print(algorithm.getBestGeneration()+":");
			System.out.print(algorithm.getBestFitness()+",");
			System.out.println(algorithm.getBestIndividual().toString());
			
			try {
				bWriter.write(algorithm.getBestGeneration()+":");
				bWriter.write(algorithm.getBestFitness()+",");
				bWriter.write(algorithm.getBestIndividual().toString()+"\n");
				bWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//测试
	static void test() {
		double mutateP = 0.15;
		double crossP = 0.75;
		int scale = 1500;
		int iterationMax = 10000;
		
		File dir = new File("testknapsack");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			//IGeneticAssistant assistant = new KnapSack1();
			IGeneticAssistant assistant = new BasicKnapSack();
			((BasicKnapSack)assistant).init(files[i]);
			
			GeneralGeneticAlgorithm algorithm = 
					new SequenceCross(iterationMax, scale,
							assistant, crossP, mutateP);
			//random cross
			algorithm = 
					new RandomCross(iterationMax, scale,
							assistant, crossP, mutateP);
			
			algorithm.start(true);
			
			System.out.print(algorithm.getBestGeneration()+":");
			System.out.print(algorithm.getBestFitness()+",");
			System.out.println(algorithm.getBestIndividual().toString());
		}
	}
}
