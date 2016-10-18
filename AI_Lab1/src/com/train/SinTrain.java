/**
 * 
 */
package com.train;

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
public class SinTrain implements BpInterface {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		int inputSize = 1;
		int outputSize = 1;
		int trainNumber = 1500;

		// get input
		double[][] trainExamplesInput = new double[trainNumber][inputSize];
		double[][] trainExamplesOutput = new double[trainNumber][outputSize];
		double base = -Math.PI / 2;
		double step = Math.PI / trainNumber;
		for (int i = 0; i < trainNumber; i++) {
			trainExamplesInput[i] = new double[] { base>=0?base:-base };
			trainExamplesOutput[i] = new double[] {base>=0?sin(base):-sin(base)};
			base += step;
		}
		BpInterface bpCtrl = new SinTrain();
		BackPropagation bp = new BackPropagation(inputSize, 20, outputSize, 0.01, 
				10000, 0.00000000001, bpCtrl);
		bp.setTrainExamples(trainExamplesInput, trainExamplesOutput);
		bp.startTrain();

		// test
		int testSize = 200;
		double error = 0;
		for (int i = 0; i < testSize; i++) {
			double test = Math.random()*Math.PI-Math.PI/2;
			double[] input = new double[] { test >= 0 ? test : -test };
			double result = bp.test(input)[0];
			if (test < 0) {
				result = -result;
			}
			double desired = sin(test);
			System.out.println(result + "/" + desired);
			double err = result - desired;
			error += err * err;
		}
		System.out.println("Average cost: " + (error / testSize / 2));
	}
	
	/**
	 * �ƶ��ı�cost�ĺ���
	 * 
	 * @param origin
	 * @return
	 */
	@Override
	public double changeRate(double cost, double oldRate) {
		if (cost < 0.0000719503) {
			return -1;
		}
		else if (cost < 0.00007195033) {
			return 0.00001;
		}
		else if (cost < 0.000072) {
			return 0.001;
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
		File file = new File("testbp/weightOfSin.txt");
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
		return "testbp/weightOfSin.txt";
	}

	private static float sin(double a) {
		return (float) Math.sin(a);
	}
}
