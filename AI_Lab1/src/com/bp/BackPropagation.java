/**
 * 
 */
package com.bp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	
	final private double lambda = 0.01;

	// train examples
	private double[][] trainExamplesInput;
	private double[][] trainExamplesOutput;

	// weights between layers
	private double[][] Theta1;
	private double[][] Theta2;

	private BpInterface bpCtrl;

	/**
	 * 
	 * @param inputLayerSize
	 * @param hiddenLayerSize
	 * @param outputLayerSize
	 * @param rate
	 *            -- learning rate
	 * @param iterationMax
	 */
	public BackPropagation(int inputLayerSize, int hiddenLayerSize, int outputLayerSize, double rate, int iterationMax,
			BpInterface bpCtrl) {
		this.inputLayerSize = inputLayerSize;
		this.hiddenLayerSize = hiddenLayerSize;
		this.outputLayerSize = outputLayerSize;
		this.rate = rate;
		this.iterationMax = iterationMax;

		Theta1 = new double[hiddenLayerSize][inputLayerSize + 1];
		Theta2 = new double[outputLayerSize][hiddenLayerSize + 1];
		this.bpCtrl = bpCtrl;
		try {
			bpCtrl.decideWeights(Theta1, Theta2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		double[] output = new double[outputLayerSize];
		double[] hidden = new double[hiddenLayerSize + 1];
		hidden[0] = 1;// add bias
		// forward propagation to get hidden and output
		// get hidden
		double[] hiddenRaw = matrixMultiVec(Theta1, input);// input added bias
															// before, no need
															// here
		for (int j = 0; j < hiddenLayerSize; j++) {
			hidden[j + 1] = sigmoid(hiddenRaw[j]);
		}
		// get output
		double[] outputRaw = matrixMultiVec(Theta2, hidden);
		for (int j = 0; j < outputLayerSize; j++) {
			output[j] = sigmoid(outputRaw[j]);
		}

		return showMax(output);
	}

	private int showMax(double[] output) {
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
		if (trainExamplesInput[0].length != inputLayerSize) {
			showError("the number of input-features should be equal to input layer size.");
		}
		if (trainExamplesOutput[0].length != outputLayerSize) {
			showError("the number of output-labels should be equal to output layer size.");
		}
		if (trainExamplesInput.length != trainExamplesOutput.length) {
			showError("the input and output number should be equal.");
		}
		this.trainExamplesInput = new double[trainExamplesInput.length][trainExamplesInput[0].length + 1];
		for (int i = 0; i < trainExamplesInput.length; i++) {
			this.trainExamplesInput[i][0] = 1;
		}
		for (int i = 0; i < trainExamplesInput.length; i++) {
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
//		int update = 800;// 更新频率
		double cost = costFunction();
		showIterationCost(iteration, cost, rate);//展示训练前cost初值
		while (iteration < iterationMax) {
			double rateTemp = 0;
			rateTemp = bpCtrl.changeRate(cost, rate);
			if (rateTemp == -1)
				break;
			else
				rate = rateTemp;
			// get delta_weights
			double[][] totalDeltaTheta1 = new double[Theta1.length][Theta1[0].length];
			double[][] totalDeltaTheta2 = new double[Theta2.length][Theta2[0].length];
			int i = 0;
			for (; i < trainExamplesInput.length; i++) {
				double[] output = new double[outputLayerSize];
				double[] hidden = new double[hiddenLayerSize + 1];
				hidden[0] = 1;// add bias
				// forward propagation to get hidden and output
				// get hidden
				// input added bias before, no need here
				double[] hiddenRaw = matrixMultiVec(Theta1, trainExamplesInput[i]);
				for (int j = 0; j < hiddenLayerSize; j++) {
					hidden[j + 1] = sigmoid(hiddenRaw[j]);
				}
				// get output
				double[] outputRaw = matrixMultiVec(Theta2, hidden);
				for (int j = 0; j < outputLayerSize; j++) {
					output[j] = sigmoid(outputRaw[j]);
				}

				// back propagation
				double[][] deltaTheta1 = new double[Theta1.length][Theta1[0].length];
				double[][] deltaTheta2 = new double[Theta2.length][Theta2[0].length];
				for (int j = 0; j < deltaTheta2.length; j++) {
					// get deltaTheta2
					for (int j2 = 0; j2 < deltaTheta2[0].length; j2++) {
						double d = trainExamplesOutput[i][j];// desired value;
						double o = output[j];// real output value
						deltaTheta2[j][j2] = rate * (d - o) * o * (1 - o) * hidden[j2];
					}
				}
				// get deltaTheta2;
				for (int j = 0; j < deltaTheta1.length; j++) {
					for (int j2 = 0; j2 < deltaTheta1[0].length; j2++) {
						double tempj = 0;// all influence on hidden layer unit j
						for (int k = 0; k < deltaTheta2.length; k++) {
							double d = trainExamplesOutput[i][k];// desired
																	// value;
							double o = output[k];// real output value
							tempj += Theta2[k][j] * o * (1 - o) * (d - o);
						}
						double input = trainExamplesInput[i][j2];
						deltaTheta1[j][j2] = rate * hidden[j] * (1 - hidden[j]) * input * tempj;
					}
				}

				// add all examples influences
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

//				if (i % update == update-1) {// 每 update个单位更新weights
//					// update Theta1 and Theta2
//					for (int k = 0; k < totalDeltaTheta2.length; k++) {
//						for (int j = 0; j < totalDeltaTheta2[0].length; j++) {
//							Theta2[k][j] += totalDeltaTheta2[k][j];
//						}
//					}
//					for (int k = 0; k < totalDeltaTheta1.length; k++) {
//						for (int j = 0; j < totalDeltaTheta1[0].length; j++) {
//							Theta1[k][j] += totalDeltaTheta1[k][j];
//						}
//					}
//					totalDeltaTheta1 = new double[Theta1.length][Theta1[0].length];
//					totalDeltaTheta2 = new double[Theta2.length][Theta2[0].length];
//
//					iteration++;
//					cost = costFunction();
//					showIterationCost(iteration, cost, rate);
//					// change cost and decide whether to stop iterations
//					rateTemp = bpCtrl.changeRate(cost, rate);
//					if (rateTemp == -1)
//						break;
//					else
//						rate = rateTemp;
//				}
			}
			//循环里还有未更新的delta weights
//			if ((i-1)%update<update-1){
				// update Theta1 and Theta2
				for (int k = 0; k < totalDeltaTheta2.length; k++) {
					for (int j = 0; j < totalDeltaTheta2[0].length; j++) {
						Theta2[k][j] += totalDeltaTheta2[k][j];
					}
				}
				for (int k = 0; k < totalDeltaTheta1.length; k++) {
					for (int j = 0; j < totalDeltaTheta1[0].length; j++) {
						Theta1[k][j] += totalDeltaTheta1[k][j];
					}
				}
				totalDeltaTheta1 = new double[Theta1.length][Theta1[0].length];
				totalDeltaTheta2 = new double[Theta2.length][Theta2[0].length];

				iteration++;
				cost = costFunction();
				showIterationCost(iteration, cost, rate);
				// change cost and decide whether to stop iterations
				rate = rateTemp = bpCtrl.changeRate(cost, rate);
//			}
			//达到要求时，则停止调整参数
			if (rateTemp == -1) {
				printWeight(bpCtrl.getPath());
				break;
			}
		}
	}

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
				row += round6( mat[i][j] * vec[j]);
			}
			ret[i] = round6(row);
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
		for (int i = 0; i < trainExamplesInput.length; i++) {
			double[] output = new double[outputLayerSize];
			double[] hidden = new double[hiddenLayerSize + 1];
			hidden[0] = 1;// add bias
			// forward propagation
			// get hidden
			double[] hiddenRaw = matrixMultiVec(Theta1, trainExamplesInput[i]);// input
																				// added
																				// bias
																				// before,
																				// no
																				// need
																				// here
			for (int j = 0; j < hiddenLayerSize; j++) {
				hidden[j + 1] = sigmoid(hiddenRaw[j]);
			}
			// get output
			double[] outputRaw = matrixMultiVec(Theta2, hidden);
			for (int j = 0; j < outputLayerSize; j++) {
				output[j] = sigmoid(outputRaw[j]);
				// get error
				error += Math.pow(trainExamplesOutput[i][j] - output[j], 2);
			}
		}
		error = error / trainExamplesOutput.length / 2;
		return error;
	}

	/**
	 * sigmoid function
	 * 
	 * @param x
	 * @return
	 */
	public double sigmoid(double x) {
		return round6((1 / (1 + Math.exp(-x))));
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
	
	/**
	 * 保留六位小数
	 * @param num
	 * @return
	 */
	private double round6(double num){
		//return num;
		return Math.round(num*100000)/100000.0;
	}


}
