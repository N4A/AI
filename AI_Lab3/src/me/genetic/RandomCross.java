package me.genetic;

/**
 * @author Duocai Wu
 * @date 2016年12月21日
 * @time 下午9:18:10
 *
 */
public class RandomCross extends GeneralGeneticAlgorithm {

	/**
	 * @param iterationMax
	 * @param scale
	 * @param assistant
	 * @param crossP
	 * @param mutateP
	 */
	public RandomCross(int iterationMax, int scale, IGeneticAssistant assistant, double crossP, double mutateP) {
		super(iterationMax, scale, assistant, crossP, mutateP);
	}

	//select
	@Override
	public void cross() {
		int first = -1;  
        for(int i=0;i<species.length;i++){  
            double random = Math.random();  
            if(random<this.crossP){  
                if(first<0){  
                    first = i;  
                }
                else{  
                    assistant.cross(species[first], species[i]);
                    first = -1;  
                }  
            }  
        } 
	}
}
