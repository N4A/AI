/**
 * 
 */
package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.bp.BackPropagation;
import com.bp.BpInterface;

/**
 * @author duocai
 * @time ����8:43:27
 * @date 2016��10��16��
 */
public class LCDDTest implements BpInterface {
	final static int outputSize = 10;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int inputSize = 7;
		LCDDTest bpCtrl = new LCDDTest();
		BackPropagation bp = new BackPropagation(inputSize, 18, 
				outputSize, 0.3, 50000, 0, bpCtrl);
		
		File file = new File("testbp/lcdd.txt");
		File output = new File("LcdD14302010040.txt");
		BufferedReader fReader = new BufferedReader(new FileReader(file));
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(output));
		while (fReader.ready()) {
			String string = fReader.readLine();
			String[] tokens = string.split(" ");
			double[] test = new double[inputSize];
			for (int j = 0; j < inputSize; j++) {
				test[j] = Double.parseDouble(tokens[j]);
			}
			int result = getMax(bp.test(test));
			bWriter.write(result + "\r\n");
		}
		fReader.close();
		bWriter.close();
		System.out.println("finished.");

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
		// TODO Auto-generated method stub
		return null;
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
		return ret;
	}
}
