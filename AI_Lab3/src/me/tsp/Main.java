package me.tsp;

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
	static double[] mutatePs = {0.02,0.025,0.35};
	static double[] crossPs = {0.065,0.075,0.85};
	static int[] scales = {4000,5000,6000};
	public static void main(String[] args) { 
		train();
	}
	
	//训练报告
	static void train() {
		for (int i = 0; i < crossPs.length; i++) {
			for (int j = 0; j < mutatePs.length; j++) {
				for (int j2 = 0; j2 < scales.length; j2++) {
					doTrain(crossPs[i], mutatePs[j],
							scales[j2], 18000);
				}
			}
		}
	}
	//do train
	static void doTrain(double crossP, double mutateP,
			int scale, int iterationMax) {
		File dir = new File("testtsp");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			BufferedWriter bWriter = null;
			try {
				bWriter = new BufferedWriter(
						new FileWriter(
								new File("outputs/tsp/sequence/"
									+"test"+i
									+"cp"+crossP
									+"mp"+mutateP
									+"s"+scale+".txt")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			IGeneticAssistant assistant = new BasicTSP();
			((BasicTSP)assistant).init(files[i]);
			
			GeneralGeneticAlgorithm algorithm = 
					new SequenceCross(iterationMax, scale,
							assistant, crossP, mutateP,bWriter);
			//random cross
//			algorithm = 
//					new RandomCross(iterationMax, scale,
//							assistant, crossP, mutateP,bWriter);
			
			algorithm.start(true);
			
			System.out.print(algorithm.getBestGeneration()+":");
			System.out.print(1/algorithm.getBestFitness()+",");
			System.out.println(algorithm.getBestIndividual().toString());
			
			try {
				bWriter.write(algorithm.getBestGeneration()+":");
				bWriter.write(1/algorithm.getBestFitness()+",");
				bWriter.write(algorithm.getBestIndividual().toString()+"\n");
				bWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//测试
	static void test() {
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
