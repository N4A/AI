/**
 * 
 */
package com.train;

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
public class LCDDTrain implements BpInterface {
	final static int outputSize = 10;
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		LCDDTrain lcddTest = new LCDDTrain();
		int inputSize = 7;

		int trainNumber = 10;

		// get input
		double[][] trainExamplesInput = new double[trainNumber][inputSize];
		double[][] trainExamplesOutput = new double[trainNumber][outputSize];
		File file = new File("testbp/lcddTrain.txt");
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
					trainExamplesOutput[i][j] = Double.parseDouble(tokens[j + inputSize]);
				}
			}
		}

		BackPropagation bp = new BackPropagation(inputSize, 18, outputSize, 0.3,
				1, 0, lcddTest);
		bp.setTrainExamples(trainExamplesInput, trainExamplesOutput);
		bp.startTrain();

		System.out.println("test:0123456789");
		System.out.print((getMax(bp.test(new double[] { 1, 1, 1, 1, 1, 1, 0 })) + 1) % 10);
		System.out.print(getMax(bp.test(new double[] { 0, 0, 0, 1, 1, 0, 0 })) + 1);
		System.out.print(getMax(bp.test(new double[] { 1, 0, 1, 1, 0, 1, 1 })) + 1);
		System.out.print(getMax(bp.test(new double[] { 0, 0, 1, 1, 1, 1, 1 })) + 1);
		System.out.print(getMax(bp.test(new double[] { 0, 1, 0, 1, 1, 0, 1 })) + 1);
		System.out.print(getMax(bp.test(new double[] { 0, 1, 1, 0, 1, 1, 1 })) + 1);
		System.out.print(getMax(bp.test(new double[] { 1, 1, 1, 0, 1, 1, 1 })) + 1);
		System.out.print(getMax(bp.test(new double[] { 0, 0, 1, 1, 1, 0, 0 })) + 1);
		System.out.print(getMax(bp.test(new double[] { 1, 1, 1, 1, 1, 1, 1 })) + 1);
		System.out.println(getMax(bp.test(new double[] { 0, 1, 1, 1, 1, 1, 1 })) + 1);

		while (true) {
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			double[] test = new double[inputSize];
			System.out.println("enter your test cases:7 number in {0,1}");
			for (int i = 0; i < test.length; i++) {
				test[i] = input.nextDouble();
			}
			System.out.println((getMax(bp.test(test)) + 1) % 10);
		}
	}

	/**
	 * �ƶ��ı�cost�ĺ���
	 * 
	 * @param origin
	 * @return
	 */
	@Override
	public double changeRate(double cost, double oldRate) {
		if (cost < 0.0001) {// origin:0.06729
			return -1;//表示结束训练
		}
		if (cost < 0.08) {
			return 0.0001;
		} else if (cost < 0.09) {
			return 0.001;
		} else if (cost < 0.1) {
			return 0.01;
		} else if (cost < 0.15) {
			return 0.1;
		}
		return oldRate;
	}

	/**
	 * �Ծ�����ʼ����,ѡ��ѵ���õĲ���
	 * 
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
			// init Theta1
			for (int i = 0; i < Theta1.length; i++) {
				String string = fReader.readLine();
				String[] tokens = string.split(" ");
				for (int j = 0; j < Theta1[0].length; j++) {
					Theta1[i][j] = Double.parseDouble(tokens[j]);
				}
			}
			// init Theta2
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
