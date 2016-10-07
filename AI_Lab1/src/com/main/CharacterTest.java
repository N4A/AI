/**
 * 
 */
package com.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.bp.BackPropagation;
import com.bp.BpInterface;

/**
 * @author duocai
 *
 */
public class CharacterTest implements BpInterface{
	final static int outputSize = 8;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		BpInterface bpCtrl = new CharacterTest();
		int inputSize = 784;
		
		int trainNumber = 7780;
		
		//get input
		double[][] trainExamplesInput = new double[trainNumber][inputSize];
		double[][] trainExamplesOutput = new double[trainNumber][outputSize];
		File file = new File("dataset_image/train/handledTrain.txt");
		BufferedReader fReader = new BufferedReader(new FileReader(file));
		if (fReader.ready()) {
			for (int i = 0; i < trainNumber; i++) {
				String string = fReader.readLine();
				String[] tokens = string.split(" ");
				for (int j = 0; j < inputSize; j++) {
					trainExamplesInput[i][j] = Double.parseDouble(tokens[j])/255;
				}
				//get output and change to vector
				int j = 7-('H'-tokens[inputSize].charAt(0));
				trainExamplesOutput[i][j] = 1;
			}
		}
		fReader.close();
		BackPropagation bp = new BackPropagation(inputSize, 25, outputSize, 0.3,
				50, 0.01, bpCtrl);
		bp.setTrainExamples(trainExamplesInput, trainExamplesOutput);
		bp.startTrain();
		
		//test
		File test = new File("dataset_image/train/handledTrain.txt");
		BufferedReader tReader = new BufferedReader(new FileReader(test));
		int i = 0;
		int right = 0;
		while (tReader.ready()) {
			i++;
			String string = tReader.readLine();
			String[] tokens = string.split(" ");
			double[] testExamples = new double[inputSize];
			for (int j = 0; j < inputSize; j++) {
				testExamples[j] = Double.parseDouble(tokens[j]);
			}
			//get output and change to vector
			char result = (char) ('A' + getMax(bp.test(testExamples)));
			char ans = tokens[tokens.length-1].charAt(0);
			System.out.println("output:"+result+"|answer:"+ans);
			if (ans == result) {
				right += 1;
			}
		}
		tReader.close();
		double trainAccuracy = (right*1.0/i)*100;
		
		test = new File("dataset_image/validation/validation.txt");
		tReader = new BufferedReader(new FileReader(test));
		i = 0;
		right = 0;
		while (tReader.ready()) {
			i++;
			String string = tReader.readLine();
			String[] tokens = string.split(" ");
			double[] testExamples = new double[inputSize];
			for (int j = 0; j < inputSize; j++) {
				testExamples[j] = Double.parseDouble(tokens[j]);
			}
			//get output and change to vector
			char result = (char) ('A' + getMax(bp.test(testExamples)));
			char ans = tokens[tokens.length-1].charAt(0);
			System.out.println("output:"+result+"|answer:"+ans);
			if (ans == result) {
				right += 1;
			}
		}
		tReader.close();
		System.out.println("train set accuracy: " + trainAccuracy);
		System.out.println("validation set accuracy: " + (right*1.0/i)*100);
	}
	
	@Override
	public double changeRate(double cost, double oldRate) {
		if (cost < 2.5){//origin:
			return -1;
		}
		else if (cost < 3) {
			return 0.1;
		}
		else if (cost < 4.4) {
			return 0.1;
		}
		else if (cost < 0.3193362) {
			return 0.001;
		}
		return oldRate;//+(Math.random()-0.5)*oldRate*0.0001;
	}
	
	/**
	 * 自决定初始参数,选择训练好的参数
	 * @param Theta1
	 * @param Theta2
	 * @throws IOException 
	 */
	@Override
	public void decideWeights(double[][] Theta1, double[][] Theta2) throws IOException {
		File file = new File("testbp/weightOfCharacter.txt");
		BufferedReader fReader = new BufferedReader(new FileReader(file));
		if (fReader.ready()) {
			//init Theta1
			for (int i = 0; i < Theta1.length; i++) {
				String string = fReader.readLine();
				String[] tokens = string.split(" ");
				for (int j = 0; j < Theta1[0].length; j++) {
					Theta1[i][j] = Double.parseDouble(tokens[j]);
				}
			}
			//init Theta2
			for (int i = 0; i < Theta2.length; i++) {
				String string = fReader.readLine();
				String[] tokens = string.split(" ");
				for (int j = 0; j < Theta2[0].length; j++) {
					Theta2[i][j] = Double.parseDouble(tokens[j]);
				}
			}
			
		}
	}
	
	
	@Override
	public String getPath() {
		return "testbp/weightOfCharacter.txt";
	}
	
	private static int getMax(double[] output) {
		double max = output[0];
		int maxId = 0;
		for (int i = 1; i < output.length; i++) {
			if (output[i] > max) {
				max = output[i];
				maxId = i;
			}
		}
		int ret = (maxId) % outputSize;
		//System.out.println("the answer is: " + (maxId + 1) % outputLayerSize);
		return ret;
	}
}
