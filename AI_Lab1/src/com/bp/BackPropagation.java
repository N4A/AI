/**
 * 
 */
package com.bp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

/**
 * @author duocai
 *
 */
public class BackPropagation {
	// super parameters
	private int inputLayerSize;
	private int hiddenLayerSize;
	private int outputLayerSize;
	private double rate;// learning rate
	private int iterationMax;
	
	private double lambda;

	// train examples
	private double[][] trainExamplesInput;
	private double[][] trainExamplesOutput;

	// weights between layers
	private double[][] Theta1;
	private double[][] Theta2;

	private BpInterface bpCtrl;
	
	//参数
	private double[] hidden;
	private double[] output;
	
	private int numOfExam;

	/**
	 * 
	 * @param inputLayerSize
	 * @param hiddenLayerSize
	 * @param outputLayerSize
	 * @param rate
	 *            -- learning rate
	 * @param iterationMax
	 * @param lambda
	 */
	public BackPropagation(int inputLayerSize, int hiddenLayerSize, int outputLayerSize, double rate, int iterationMax,
			double lambda, BpInterface bpCtrl) {
		this.inputLayerSize = inputLayerSize;
		this.hiddenLayerSize = hiddenLayerSize;
		this.outputLayerSize = outputLayerSize;
		this.rate = rate;
		this.iterationMax = iterationMax;
		this.lambda = lambda;

		Theta1 = new double[hiddenLayerSize][inputLayerSize + 1];
		Theta2 = new double[outputLayerSize][hiddenLayerSize + 1];
		this.bpCtrl = bpCtrl;
		try {
			bpCtrl.decideWeights(Theta1, Theta2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hidden = new double[hiddenLayerSize+1];
		hidden[0] = 1;//add bias
		output = new double[outputLayerSize];
	}
	
	/**
	 * 向前传播，算出结果
	 * @input - 某个测试样咧
	 */
	private void forwardPropagation(double[] input){
		// forward propagation
		// get hidden
		double[] hiddenRaw = matrixMultiVec(Theta1, input);
		for (int j = 0; j < hiddenLayerSize; j++) {
			hidden[j + 1] = sigmoid(hiddenRaw[j]);
		}
		// get output
		double[] outputRaw = matrixMultiVec(Theta2, hidden);
		for (int j = 0; j < outputLayerSize; j++) {
			output[j] = sigmoid(outputRaw[j]);
		}
	}
	
	/**
	 * 反向传播，算出参数误差
	 * @param deltaTheta1
	 * @param deltaTheta2
	 * @param desiredOutput
	 * @param input
	 */
	private void backPropagation(double[][] deltaTheta1, double[][] deltaTheta2, double[] desiredOutput, double[] input) {
		for (int j = 0; j < deltaTheta2.length; j++) {
			// get deltaTheta2
			double d = desiredOutput[j];// desired value;
			double o = output[j];// real output value
			for (int j2 = 0; j2 < deltaTheta2[0].length; j2++) {
				deltaTheta2[j][j2] = rate * (d - o) * o * (1 - o) * hidden[j2];
			}
		}
		// get deltaTheta2;
		for (int j = 0; j < deltaTheta1.length; j++) { 
			double h = hidden[j];
			for (int j2 = 0; j2 < deltaTheta1[0].length; j2++) {
				double totalj = 0;// all influence on hidden layer unit j
				for (int k = 0; k < deltaTheta2.length; k++) {
					double d = desiredOutput[k];// desired value;
					double o = output[k];// real output value
					totalj += Theta2[k][j] * o * (1 - o) * (d - o);//积累到hidden layer的误差
				}
				double i = input[j2];
				deltaTheta1[j][j2] = rate * h * (1 - h) * i * totalj;
			}
		}

	}

	/**
	 * test
	 * 
	 * @param inputraw
	 */
	public int test(double[] inputraw) {
		if (inputraw.length != inputLayerSize) {
			showError("test input not valid, the features number should be equal to training examples");
		}

		double[] input = new double[inputLayerSize + 1];
		input[0] = 1;
		for (int j = 0; j < inputraw.length; j++) {
			input[j + 1] = inputraw[j];
		}
		
		//get uouput
		forwardPropagation(input);

		return getMax();
	}

	private int getMax() {
		double max = output[0];
		int maxId = 0;
		for (int i = 1; i < output.length; i++) {
			if (output[i] > max) {
				max = output[i];
				maxId = i;
			}
		}
		int ret = (maxId) % outputLayerSize;
		//System.out.println("the answer is: " + (maxId + 1) % outputLayerSize);
		return ret;
	}

	/**
	 * set training examples
	 * 
	 * @param trainExamplesInput
	 * @param trainExamplesOutput
	 */
	public void setTrainExamples(double[][] trainExamplesInput, double[][] trainExamplesOutput) {
		numOfExam = trainExamplesInput.length;
		if (trainExamplesInput[0].length != inputLayerSize) {
			showError("the number of input-features should be equal to input layer size.");
		}
		if (trainExamplesOutput[0].length != outputLayerSize) {
			showError("the number of output-labels should be equal to output layer size.");
		}
		if (numOfExam != trainExamplesOutput.length) {
			showError("the input and output number should be equal.");
		}
		this.trainExamplesInput = new double[numOfExam][trainExamplesInput[0].length + 1];
		for (int i = 0; i < numOfExam; i++) {
			this.trainExamplesInput[i][0] = 1;
		}
		for (int i = 0; i < numOfExam; i++) {
			for (int j = 0; j < trainExamplesInput[0].length; j++) {
				this.trainExamplesInput[i][j + 1] = trainExamplesInput[i][j];
			}
		}
		this.trainExamplesOutput = trainExamplesOutput;
	}

	/**
	 * start training
	 */
	public void startTrain() {
		int iteration = 0;
		while (iteration < iterationMax) {	
			double cost = 0;//每次迭代重新计算cost
			// get delta_weights
			double[][] totalDeltaTheta1 = new double[Theta1.length][Theta1[0].length];
			double[][] totalDeltaTheta2 = new double[Theta2.length][Theta2[0].length];
			int i = 0;
			for (; i < numOfExam; i++) {
				//forward propagation
				forwardPropagation(trainExamplesInput[i]);
				cost+= getOneExampleCost(trainExamplesOutput[i]);

				// back propagation
				double[][] deltaTheta1 = new double[Theta1.length][Theta1[0].length];
				double[][] deltaTheta2 = new double[Theta2.length][Theta2[0].length];
				backPropagation(deltaTheta1, deltaTheta2, trainExamplesOutput[i],
						trainExamplesInput[i]);
				
				// add this example's influences
				for (int j = 0; j < deltaTheta2.length; j++) {
					for (int j2 = 0; j2 < deltaTheta2[0].length; j2++) {
						totalDeltaTheta2[j][j2] += deltaTheta2[j][j2];
					}
				}
				for (int j = 0; j < deltaTheta1.length; j++) {
					for (int j2 = 0; j2 < deltaTheta1[0].length; j2++) {
						totalDeltaTheta1[j][j2] += deltaTheta1[j][j2];
					}
				}
			}
			//正则化,并计算cost正则项
			double cost1 = 0;
			double cost2 = 0;
			for (int j = 0; j < Theta2.length; j++) {
				for (int j2 = 0; j2 < Theta2[0].length; j2++) {
					if (j2 != 0) {
						totalDeltaTheta2[j][j2] -= lambda*Theta2[j][j2];
					}
					cost2 += lambda*Theta2[j][j2]*Theta2[j][j2];
				}
			}
			for (int j = 0; j < Theta1.length; j++) {
				for (int j2 = 0; j2 < Theta1[0].length; j2++) {
					if (j2 != 0) {
						totalDeltaTheta1[j][j2] -= lambda*Theta1[j][j2];
					}
					cost1 += lambda*Theta1[j][j2]*Theta1[j][j2];
				}
			}
			cost = (cost + cost1 + cost2) / numOfExam / 2;
			showIterationCost(iteration, cost, rate);
			rate = bpCtrl.changeRate(cost, rate);
			//达到要求时，则停止调整参数
			if (rate == -1) {
				printWeight(bpCtrl.getPath());
				break;
			}
			//update Theta
			updateTheta(totalDeltaTheta1, totalDeltaTheta2);			
			iteration++;
		}
	}
	
	/**
	 * 更新参数
	 * @param totalDeltaTheta1
	 * @param totalDeltaTheta2
	 */
	private void updateTheta(double[][] totalDeltaTheta1, double[][] totalDeltaTheta2) {
		for (int k = 0; k < totalDeltaTheta2.length; k++) {
			for (int j = 0; j < totalDeltaTheta2[0].length; j++) {
				Theta2[k][j] += totalDeltaTheta2[k][j]/numOfExam
						- lambda*Theta2[k][j];
			}
		}
		for (int k = 0; k < totalDeltaTheta1.length; k++) {
			for (int j = 0; j < totalDeltaTheta1[0].length; j++) {
				Theta1[k][j] += totalDeltaTheta1[k][j]/numOfExam
						- lambda*Theta1[k][j];
			}
		}
		totalDeltaTheta1 = new double[Theta1.length][Theta1[0].length];
		totalDeltaTheta2 = new double[Theta2.length][Theta2[0].length];
	}

	/**
	 * 输出训练好的权重
	 * @param path
	 */
	private void printWeight(String path) {
		System.out.println("The weights of Bp networks are outputed to " + path);
		File file = new File(path);
		try {
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < Theta1.length; i++) {
				for (int j = 0; j < Theta1[0].length; j++) {
					bWriter.write(Theta1[i][j] + " ");
				}
				bWriter.write("\r\n");
			}
			for (int i = 0; i < Theta2.length; i++) {
				for (int j = 0; j < Theta2[0].length; j++) {
					bWriter.write(Theta2[i][j] + " ");
				}
				bWriter.write("\r\n");
			}
			bWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Matrix * vector
	 * 
	 * @param mat
	 * @param vec
	 * @return
	 */
	private double[] matrixMultiVec(double[][] mat, double[] vec) {
		double[] ret = new double[mat.length];
		for (int i = 0; i < mat.length; i++) {
			int row = 0;
			for (int j = 0; j < vec.length; j++) {
				row += mat[i][j] * vec[j];
			}
			ret[i] = row;
		}
		return ret;
	}

	/**
	 * calculate error cost
	 * 
	 * @return
	 */
	private double costFunction() {
		double error = 0;
		for (int i = 0; i < numOfExam; i++) {
			forwardPropagation(trainExamplesInput[i]);		
			//get error
			error+= getOneExampleCost(trainExamplesOutput[i]);
		}
		double cost1 = 0;
		double cost2 = 0;
		for (int j = 0; j < Theta2.length; j++) {
			for (int j2 = 0; j2 < Theta2[0].length; j2++) {
				cost2 += lambda*Theta2[j][j2]*Theta2[j][j2];
			}
		}
		for (int j = 0; j < Theta1.length; j++) {
			for (int j2 = 0; j2 < Theta1[0].length; j2++) {
				cost1 += lambda*Theta1[j][j2]*Theta1[j][j2];
			}
		}
		error = (error + cost1 + cost2) / numOfExam / 2;
		return error;
	}
	/**
	 * 得到一个样本的误差
	 * @param desiredOutput
	 * @return
	 */
	private double getOneExampleCost(double[] desiredOutput) {
		double err = 0;
		for (int j = 0; j < outputLayerSize; j++) {
			// get error
			double error = desiredOutput[j] - output[j];
			err += error*error;
		}
		return err;
	}

	/**
	 * sigmoid function
	 * 
	 * @param x
	 * @return
	 */
	public double sigmoid(double x) {
		return (1 / (1 + Math.exp(-x)));
	}

	/**
	 * show error message
	 * 
	 * @param error
	 */
	private void showError(String error) {
		System.err.println(error);
		System.exit(0);
	}

	/**
	 * show iteration and cost
	 * 
	 * @param iteration
	 * @param cost
	 */
	private void showIterationCost(int iteration, double cost, double rate) {
		System.out.println("Iteration:\t" + iteration + "|Cost:\t" + cost + "|rate:\t" + rate);
	}
}
//if (i % update == update-1) {// 每 update个单位更新weights
//// update Theta1 and Theta2
//for (int k = 0; k < totalDeltaTheta2.length; k++) {
//	for (int j = 0; j < totalDeltaTheta2[0].length; j++) {
//		Theta2[k][j] += totalDeltaTheta2[k][j];
//	}
//}
//for (int k = 0; k < totalDeltaTheta1.length; k++) {
//	for (int j = 0; j < totalDeltaTheta1[0].length; j++) {
//		Theta1[k][j] += totalDeltaTheta1[k][j];
//	}
//}
//totalDeltaTheta1 = new double[Theta1.length][Theta1[0].length];
//totalDeltaTheta2 = new double[Theta2.length][Theta2[0].length];
//
//iteration++;
//cost = costFunction();
//showIterationCost(iteration, cost, rate);
//// change cost and decide whether to stop iterations
//rateTemp = bpCtrl.changeRate(cost, rate);
//if (rateTemp == -1)
//	break;
//else
//	rate = rateTemp;
//}
