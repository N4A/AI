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
	 * 返回训练好的数据输出路径
	 * @param path
	 */
	public String getPath();
	/**
	 * 制定改变cost的函数
	 * @param cost
	 * @return cost- -1:停止训练,输出训练参数，默认返回原来的cost
	 */
	public default double changeRate(double cost, double oldRate) {
		return oldRate;
	}
	
	/**
	 * 自决定初始参数，默认随机生成
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
