/**
 * 
 */
package com.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.bp.BackPropagation;
import com.bp.BpInterface;

/**
 * @author duocai
 *
 */
public class LCDDTest implements BpInterface{
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		LCDDTest lcddTest = new LCDDTest();
		int inputSize = 7;
		int outputSize = 10;
		int trainNumber = 10;
		
		//get input
		double[][] trainExamplesInput = new double[trainNumber][inputSize];
		double[][] trainExamplesOutput = new double[trainNumber][outputSize];
		File file = new File("testbp/lcdd.txt");
		@SuppressWarnings("resource")
		BufferedReader fReader = new BufferedReader(new FileReader(file));
		if (fReader.ready()) {
			for (int i = 0; i < trainNumber; i++) {
				String string = fReader.readLine();
				String[] tokens = string.split(" ");
				for (int j = 0; j < inputSize; j++) {
					trainExamplesInput[i][j] = Double.parseDouble(tokens[j]);
				}
				for (int j = 0; j < outputSize; j++) {
					trainExamplesOutput[i][j] = Double.parseDouble(tokens[j+inputSize]);
				}
			}
		}
		
		BackPropagation bp = new BackPropagation(inputSize, 18, outputSize, 0.3, 10,lcddTest);
		bp.setTrainExamples(trainExamplesInput, trainExamplesOutput);
		bp.startTrain();
		
		System.out.println("test:0123456789");
		System.out.println(bp.test(new double[]{1,1,1,1,1,1,0})+1);
		System.out.println(bp.test(new double[]{0,0,0,1,1,0,0})+1);
		System.out.println(bp.test(new double[]{1,0,1,1,0,1,1})+1);
		System.out.println(bp.test(new double[]{0,0,1,1,1,1,1})+1);
		System.out.println(bp.test(new double[]{0,1,0,1,1,0,1})+1);
		System.out.println(bp.test(new double[]{0,1,1,0,1,1,1})+1);
		System.out.println(bp.test(new double[]{1,1,1,0,1,1,1})+1);
		System.out.println(bp.test(new double[]{0,0,1,1,1,0,0})+1);
		System.out.println(bp.test(new double[]{1,1,1,1,1,1,1})+1);
		System.out.println(bp.test(new double[]{0,1,1,1,1,1,1})+1);
		
		while (true){
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			double[] test = new double[inputSize];
			System.out.println("enter your test cases:7 number in {0,1}");
			for (int i = 0; i < test.length; i++) {
				test[i] = input.nextDouble();
			}
			System.out.println(bp.test(test)+1);
		}
	}
	
	/**
	 * 制定改变cost的函数
	 * @param origin
	 * @return
	 */
	@Override
	public double changeRate(double cost, double oldRate) {
		if (cost < 0.0673){//origin:0.0674
			return -1;
		}
		if (cost < 0.08) {
			return 0.0001;
		}
		else if (cost < 0.09) {
			return 0.001;
		}
		else if (cost < 0.1) {
			return 0.01;
		}
		else if (cost<0.15) {
			return 0.1;
		}
		return oldRate;
	}
	
	/**
	 * 自决定初始参数,选择训练好的参数
	 * @param Theta1
	 * @param Theta2
	 * @throws IOException 
	 */
	@Override
	public void decideWeights(double[][] Theta1, double[][] Theta2) throws IOException {
		File file = new File("testbp/weightOfLCDD.txt");
		@SuppressWarnings("resource")
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
		return "testbp/weightOfLCDD.txt";
	}
}
