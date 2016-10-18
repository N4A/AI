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
 * @time ����8:25:52
 * @date 2016��10��16��
 */
public class CharacterTest implements BpInterface {
	final static int outputSize = 8;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		int inputSize = 784;
		BpInterface bpCtrl = new CharacterTest();
		BackPropagation bp = new BackPropagation(inputSize, 25, outputSize, 0.3,
				100, 0.01, bpCtrl);
		
		//test
		File test = new File("testbp/letterBp.txt");
		File output = new File("LetterBP14302010040.txt");
		BufferedReader tReader = new BufferedReader(new FileReader(test));
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(output));
		while (tReader.ready()) {
			String string = tReader.readLine();
			String[] tokens = string.split(" ");
			double[] testExamples = new double[inputSize];
			for (int j = 0; j < inputSize; j++) {
				testExamples[j] = Double.parseDouble(tokens[j]);
			}
			//get output and change to vector
			char result = (char) ('A' + getMax(bp.test(testExamples)));
			bWriter.write(result + "\r\n");
		}
		tReader.close();
		bWriter.close();
		System.out.println("finished");
	}
	
	/**
	 * �Ծ�����ʼ����,ѡ��ѵ���õĲ���
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
