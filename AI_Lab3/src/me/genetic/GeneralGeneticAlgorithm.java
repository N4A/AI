package me.genetic;

/**
 * @author Duocai Wu
 * @date 2016��12��21��
 * @time ����2:46:15
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
	
	public GeneralGeneticAlgorithm(int iterationMax, int scale,
			IGeneticAssistant assistant, double crossP,
			double mutateP) {
		this.scale = scale;
		this.iterationMax = iterationMax;
		this.assistant = assistant;
		this.crossP = crossP;
		this.mutateP = mutateP;
	}
	
	/**
	 * start to run the algorithm
	 * @param output - decide whether to print 
	 * 				best individual during iteration
	 * @date 2016��12��21��
	 * @time ����3:25:45
	 */
	public void start(boolean output) {
		fitnesses = new double[scale];
		bestFitness = Double.MIN_VALUE;//set fitness to minimal
		curGeneration = 0;//first generation
		//init the first species randomly
		randomInitGeneration();
		
		//iterate until reach the limit
		for (int i = 0; i < iterationMax; i++) {
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
				System.out.print("g: " + curGeneration);
				System.out.print(",best: " + curGBestFitness);
				System.out.print(",average: " + curGAverageFitness);
				System.out.println(",best in total: "+ bestFitness);
				
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
		//evaluate fitness of each member of the species;
		calculateFitness();
		
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
	 * @date 2016��12��22��
	 * @time ����3:07:04
	 */
	public void select() {
		//calculate the rate of being selected of each
		//individual according to fitness
		double[] rates = calculateRates();
		
		Individual<?>[] newGeneration = new Individual[scale];
		//Russian roulette strategy
		for (int i = 0; i < newGeneration.length; i++) {
			//rates���Կ�������Ϊ1�ľ��ȷֲ��������һ����С��
			//rate[i]��ֵ������i��С�εĳ��ȡ�
			//Ȼ���������һ��(0,1)֮��������������ڵ�i������ĸ��ʾ���rate[i]��
			//����൱��һ�����̲��ԡ�
			//������ķ���ȡ�������ڵ��Ǹ�����
			double accum = 0;//�ۻ�����
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
	 * @date 2016��12��22��
	 * @time ����3:04:40
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
	 * @date 2016��12��22��
	 * @time ����3:05:03
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
	
	//init the species randomly
	private void randomInitGeneration() {
		species = new Individual[scale];
		for (int i = 0; i < species.length; i++) {
			species[i] = assistant.randomInitIndividual();
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