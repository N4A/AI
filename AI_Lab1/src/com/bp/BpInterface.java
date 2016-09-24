/**
 * 
 */
package com.bp;

import java.io.IOException;

/**
 * @author duocai
 *
 */
public interface BpInterface {
	
	/**
	 * ����ѵ���õ��������·��
	 * @param path
	 */
	public String getPath();
	/**
	 * �ƶ��ı�cost�ĺ���
	 * @param cost
	 * @return cost- -1:ֹͣѵ��,���ѵ��������Ĭ�Ϸ���ԭ����cost
	 */
	public default double changeRate(double cost, double oldRate) {
		return oldRate;
	}
	
	/**
	 * �Ծ�����ʼ������Ĭ���������
	 * @param Theta1
	 * @param Theta2
	 * @throws IOException 
	 */
	public default void decideWeights(double[][] Theta1, double[][] Theta2) throws IOException {
		// Randomly initialize the weights between layers
		randomInitWeights(Theta1);
		randomInitWeights(Theta2);
	}
	
	/**
	 * Randomly initialize the weights
	 * @param weights
	 */
	default void randomInitWeights(double[][] weights) {
		int rows = weights.length;
		int columns = weights[0].length;
		// A good choice of eposilon_init is
		// Math.sqrt(6)/Math.sqrt((rows+columns));
		double epsilon = Math.sqrt(3) / Math.sqrt((rows + columns));

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				weights[i][j] = Math.random() * 2 * epsilon - epsilon;
			}
		}
	}
}
