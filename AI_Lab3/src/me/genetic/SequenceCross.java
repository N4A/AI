package me.genetic;

/**
 * @author Duocai Wu
 * @date 2016年12月22日
 * @time 下午3:02:37
 *
 */
public class SequenceCross extends GeneralGeneticAlgorithm {

	/**
	 * 
	 */
	public SequenceCross(int iterationMax, int scale, IGeneticAssistant assistant, double crossP, double mutateP) {
		super(iterationMax, scale, assistant, crossP, mutateP);
	}

	@Override
	public void cross() {
		for (int i = 1; i < species.length; i+=2) {
			double random = Math.random();
			//cross
			if (random < crossP) {
				assistant.cross(species[i-1], species[i]);
			}
		}
	}
}
