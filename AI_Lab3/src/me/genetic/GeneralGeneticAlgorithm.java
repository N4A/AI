package me.genetic;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author Duocai Wu
 * @date 2016年12月21日
 * @time 下午2:46:15
 *
 */
public abstract class GeneralGeneticAlgorithm {
	private int scale;//the scale of the species
	private int iterationMax;

	private double[] fitnesses;
	private Individual<?> bestIndividual;

	private double bestFitness;//best in total
	private int bestGeneration;
	private int curGeneration;
	private double curGBestFitness;//best in cur generation
	private double curGAverageFitness;// average fitness of cur generation
	
	protected IGeneticAssistant assistant;
	protected Individual<?>[] species;
	protected double crossP;
	protected double mutateP;
	
	private BufferedWriter bWriter = null;
	
	public GeneralGeneticAlgorithm(int iterationMax, int scale,
			IGeneticAssistant assistant, double crossP,
			double mutateP, BufferedWriter bWriter) {
		this.scale = scale;
		this.iterationMax = iterationMax;
		this.assistant = assistant;
		this.crossP = crossP;
		this.mutateP = mutateP;
		this.bWriter = bWriter;
	}
	
	public GeneralGeneticAlgorithm(int iterationMax, int scale,
			IGeneticAssistant assistant, double crossP,
			double mutateP) {
		this(iterationMax, scale,
				assistant, crossP, mutateP,null);
	}
	
	/**
	 * start to run the algorithm
	 * @param output - decide whether to print 
	 * 				best individual during iteration
	 * @date 2016年12月21日
	 * @time 下午3:25:45
	 */
	public void start(boolean output) {
		fitnesses = new double[scale];
		bestFitness = Double.MIN_VALUE;//set fitness to minimal
		curGeneration = 0;//first generation
		//init the first species randomly
		initGeneration();
		
		//iterate until reach the limit
		for (int i = 0; i < iterationMax; i++) {
			//evaluate fitness of each member of the species;
			calculateFitness();
			//select members from the species based on fitness
			//produce the offspring of these pairs using genetic operators;
			//and replace candidates of the species, with these offspring;
			select();
			//cross
			cross();
			//mutate
			mutate();
			//set to next generation
			curGeneration++;
			
			//print best individual
			if (output) {
				if (i%300 == 0) {
					System.out.print("g: " + curGeneration);
					System.out.print(",best: " + curGBestFitness);
					System.out.print(",average: " + curGAverageFitness);
					System.out.println(",best in total: "+ bestFitness);
					
					if (bWriter != null) {
						try {
							bWriter.write("g: " + curGeneration);
							bWriter.write(",best: " + curGBestFitness);
							bWriter.write(",average: " + curGAverageFitness);
							bWriter.write(",best in total: "+ bestFitness+"\n");
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		
	}

	//calculate the fitness of current generation
	//and set the best individual
	private void calculateFitness() {
		curGBestFitness = 0;
		double totalFitness = 0;
		for (int i = 0; i < scale; i++) {
			fitnesses[i] = assistant.fitness(species[i]);
			totalFitness += fitnesses[i];
			//check whether get better individual or not
			if (fitnesses[i] > bestFitness) {
				bestFitness = fitnesses[i];
				bestIndividual = assistant.copy(species[i]);
				bestGeneration = curGeneration;
			}
			if (fitnesses[i] > curGBestFitness) {
				curGBestFitness = fitnesses[i];
			}
		}
		curGAverageFitness = totalFitness/scale;
	}
	
	//calculate chosen rate
	private double[] calculateRates() {		
		double[] rates = new double[scale];
		double totalFitness = sum(fitnesses);
		//calculate chosen rate
		for (int i = 0; i < rates.length; i++) {
			rates[i] = fitnesses[i]/totalFitness;
		}
		return rates;
	}
	
	private double sum(double[] array) {
		double total = 0;
		for (int i = 0; i < array.length; i++) {
			total += array[i];
		}
		return total;
	}

	/**
	 * select the better individuals
	 * and produce the next generation
	 * default one is Russian roulette strategy
	 * @date 2016年12月22日
	 * @time 下午3:07:04
	 */
	public void select() {
		//calculate the rate of being selected of each
		//individual according to fitness
		double[] rates = calculateRates();
		
		Individual<?>[] newGeneration = new Individual[scale];
		//Russian roulette strategy
		for (int i = 0; i < newGeneration.length; i++) {
			//rates可以看作长度为1的均匀分布中有序的一个个小段
			//rate[i]的值代表第i个小段的长度。
			//然后随机生成一个(0,1)之间的数，则它落于第i个区间的概率就是rate[i]。
			//这就相当于一个轮盘策略。
			//用下面的方法取出它落于的那个区间
			double accum = 0;//累积概率
			double random = Math.random();
			for (int j = 0; j < species.length; j++) {
				accum += rates[j];
				if (accum >= random) {
					newGeneration[i] = assistant.copy(species[j]);
					break;
				}
			}
		}
		
		species = newGeneration;
	}
	
	/**
	 * decide the cross algorithm
	 * default one is sequence cross
	 * @date 2016年12月22日
	 * @time 下午3:04:40
	 */
	public void cross() {
		for (int i = 1; i < species.length; i+=2) {
			double random = Math.random();
			//cross
			if (random < crossP) {
				assistant.cross(species[i-1], species[i]);
			}
		}
	}
	
	/**
	 * decide the cross algorithm
	 * default one is random mutate
	 * @date 2016年12月22日
	 * @time 下午3:05:03
	 */
	public void mutate() {
		for (int i = 0; i < species.length; i++) {
			double random = Math.random();
			//mutate
			if (random < mutateP) {
				assistant.mutate(species[i] );
			}
		}
	}
	
	/**
	 * decide how to initialize the first generation
	 * default one is initialize it randomly
	 * @date 2016年12月22日
	 * @time 下午9:58:14
	 */
	public void initGeneration() {
		species = new Individual[scale];
		for (int i = 0; i < species.length; i++) {
			species[i] = assistant.initIndividual();
		}
	}

	public int getBestGeneration() {
		return bestGeneration;
	}
	
	public Individual<?> getBestIndividual() {
		return bestIndividual;
	}
	
	public double getBestFitness() {
		return bestFitness;
	}
}
