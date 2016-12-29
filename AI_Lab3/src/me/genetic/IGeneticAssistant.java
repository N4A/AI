package me.genetic;

/**
 * @author Duocai Wu
 * @date 2016年12月21日
 * @time 下午2:09:38
 *
 */
public interface IGeneticAssistant {
	
	/**
	 * make a individual randmly
	 * @return
	 * @date 2016年12月21日
	 * @time 下午3:10:26
	 */
	public Individual<?> initIndividual();
	
	/**
	 * calculate the fitness of the 
	 * parameter individual(ivd).
	 * @param ivd
	 * @return the fitness
	 * @date 2016年12月21日
	 * @time 下午2:33:56
	 */
	public double fitness(Individual<?> ivd);
	
	/**
	 * decide how to let ivd1 and lvd2 cross
	 * @param ivd1
	 * @param ivd2
	 * @date 2016年12月21日
	 * @time 下午2:44:44
	 */
	public void cross(Individual<?> ivd1, Individual<?> ivd2);
	
	/**
	 * decide how to mutate ivd
	 * @param ivd
	 * @date 2016年12月21日
	 * @time 下午2:44:48
	 */
	public void mutate(Individual<?> ivd);

	/**
	 * copy the individual to the new address
	 * @param individual
	 * @return
	 * @date 2016年12月21日
	 * @time 下午8:00:30
	 */
	public Individual<?> copy(Individual<?> individual);
}
