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
 * @time 下午8:56:25
 * @date 2016年10月16日
 */
public class SinTest implements BpInterface {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		int inputSize = 1;
		LCDDTest bpCtrl = new LCDDTest();
		BackPropagation bp = new BackPropagation(inputSize, 20, 
				1, 0.3, 50000, 0, bpCtrl);
		File file = new File("testbp/sine.txt");
		File output = new File("Sine14302010040.txt");
		BufferedReader fReader = new BufferedReader(new FileReader(file));
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(output));
		while (fReader.ready()) {
			String string = fReader.readLine();
			double[] test = new double[inputSize];
			double num = Double.parseDouble(string);
			test[0] = num >= 0 ? num : -num;
			double result = bp.test(test)[0];
			result = num >= 0 ? result : -result;
			bWriter.write(result + "\r\n");
		}
		fReader.close();
		bWriter.close();
		System.out.println("finished.");
	}

	/**
	 * 自决定初始参数,选择训练好的参数
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
		return null;
	}
}
