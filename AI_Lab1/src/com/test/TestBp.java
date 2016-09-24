/**
 * 
 */
package com.test;

import org.junit.Test;

import com.bp.BackPropagation;
import com.bp.BpInterface;

/**
 * @author duocai
 *
 */
public class TestBp {
	
	BackPropagation bp = new BackPropagation(1, 1, 1, 1, 1,4, new BpInterface() {
		
		@Override
		public String getPath() {
			// TODO Auto-generated method stub
			return null;
		}
	});
	
	@Test
	/**
	 * fprintf('Sigmoid gradient evaluated at [1 -0.5 0 0.5 1]:\n  ');
	 */
	public void testSigmoid() {
		double[] test = {-1,-0.5,0,0.5,1};
		for (int i = 0; i < test.length; i++) {
			System.out.println(bp.sigmoid(test[i]));
		}
	}

}
