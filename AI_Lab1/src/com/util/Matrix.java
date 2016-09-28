/**
 * 
 */
package com.util;

/**
 * @author admin
 *
 */
public class Matrix {

	/**
	 * 
	 */
	public Matrix() {
		// TODO Auto-generated constructor stub
	}
	
	public static double[][] Multiply(double[][] a, double[][] b) {
		double[][] c = new double[a.length][b[0].length];
		
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				double total = 0;
				for (int j2 = 0; j2 < a.length; j2++) {
					total += a[i][j2]*b[j2][j];
				}
				c[i][j] = total;
			}
		}
		
		return c;
	}
	
	/**
	 * 将b加到a上
	 * @param a
	 * @param b
	 */
	public static void Add(double[][] a, double[][] b) {		
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				a[i][j] = a[i][j]+b[i][j];
			}
		}
	}
	
	public static double[] matrixMultiVec(double[][] mat, double[] vec) {
		double[] ret = new double[mat.length];
		for (int i = 0; i < mat.length; i++) {
			double row = 0;//what a bug: int row = 0;
			for (int j = 0; j < vec.length; j++) {
				double a = mat[i][j] * vec[j];
				row += a;
			}
			ret[i] = row;
		}
		return ret;
	}
	
	public static double[][] transpose(double[][] mat) {
		double[][] c = new double[mat[0].length][mat.length];
		
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				c[i][j] = mat[j][i];
			}
		}
		
		return c;
	}
	
	/**
	 * 向量a乘以向量b，返回矩阵
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] vecMulVec(double[] a, double[] b, double rate) {
		double[][] c = new double[a.length][b.length];
		
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				c[i][j] = rate*a[i]*b[j];
			}
		}
		
		return c;
	}
	
	public static double[] vecSubVec(double[] a, double[] b) {
		double[] c = new double[a.length];
		
		for (int i = 0; i < c.length; i++) {
			c[i] = a[i]-b[i];
		}
		
		return c;
	}
	
	public static double[] numSubVec(double a, double[] b) {
		double[] c = new double[b.length];
		
		for (int i = 0; i < c.length; i++) {
			c[i] = a-b[i];
		}
		
		return c;
	}
	
	/**
	 * 返回每个元素相乘得到的向量
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[] dotMulti(double[] a, double[] b) {
		double[] c = new double[a.length];
		
		for (int i = 0; i < c.length; i++) {
			c[i] = a[i]*b[i];
		}
		
		return c;
	}
	
	public static double[][] rmFirstColumn(double[][] a) {
		double[][] c = new double[a.length][a[0].length-1];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				c[i][j] = a[i][j+1];
			}
		}
		return c;
	}
}
