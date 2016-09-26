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

import junit.framework.Test;

/**
 * @author duocai
 *
 */
public class SinTest implements BpInterface {
	private static final int BinaryLen = 32;

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		float a = (float) Math.sin(Math.PI / 2);
		float b = (float) Math.sin(-Math.PI / 2);
		int a1 = Float.floatToIntBits(a);
		String astring = Integer.toBinaryString(a1);
		System.out.println(astring);
		int inputSize = BinaryLen;
		int outputSize = BinaryLen;
		int trainNumber = 100;

		// get input
		double[][] trainExamplesInput = new double[trainNumber][inputSize];
		double[][] trainExamplesOutput = new double[trainNumber][outputSize];
		double base = -Math.PI / 2;
		double step = Math.PI / trainNumber;
		for (int i = 0; i < trainNumber; i++) {
			trainExamplesInput[i] = getBinary(base);
			trainExamplesOutput[i] = getBinary(sin(base));
			base += step;
		}
		BpInterface bpCtrl = new SinTest();
		BackPropagation bp = new BackPropagation(inputSize, 48,
				outputSize, 0.3, 10000, 0.01, bpCtrl);
		bp.setTrainExamples(trainExamplesInput, trainExamplesOutput);
		bp.startTrain();
				

		// test
		int testSize = 100;
		double error = 0;
		for (int i = 0; i < testSize; i++) {
			double test = Math.random() * Math.PI - Math.PI / 2;
			double[] input =  getBinary(test);
			double result = getDouble(bp.test(input));
			double desired = sin(test);
			System.out.println(result + "/" + desired);
			double err = result-desired;
			error += err*err;
		}
		System.out.println("Average cost: " + (error/testSize/2));
	}

	private static double getDouble(double[] test) {
		double ret =0;
		for (int i = 0; i < BinaryLen; i++) {
			ret += Math.round(test[i])*Math.pow(2, i);
		}
		return ret*1.0/Math.pow(2, BinaryLen);
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 将double转换成二进制
	 * @param ad
	 * @return
	 */
	private static double[] getBinary(double ad) {
		int ai = (int)(ad*Math.pow(2, BinaryLen));
		double[] binary = new double[BinaryLen];
		for (int i = 0; i < BinaryLen; i++) {
			int bi = ai%2;
			binary[i] = bi;
			bi /= 2;
		}
		return binary;
	}

	private static float sin(double a) {
		return (float) Math.sin(a);
	}
}
