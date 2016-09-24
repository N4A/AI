/**
 * 
 */
package com.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.bp.BpInterface;

/**
 * @author duocai
 *
 */
public class SinTest implements BpInterface {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		int inputSize = 7;
		int outputSize = 10;
		int trainNumber = 10;
		
		//get input
		double[][] trainExamplesInput = new double[trainNumber][inputSize];
		double[][] trainExamplesOutput = new double[trainNumber][outputSize];
		File file = new File("testbp/lcdd.txt");
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
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
