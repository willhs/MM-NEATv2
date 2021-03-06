package edu.utexas.cs.nn.tasks.gridTorus.sensors;

import edu.utexas.cs.nn.gridTorus.TorusAgent;
import edu.utexas.cs.nn.gridTorus.TorusWorld;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.tasks.gridTorus.NNTorusPredPreyController;

/**
 * finds the sensor inputs for the predators by proximity. The inputs will be
 * the X and Y offsets to each predator. The inputs will be in order of closest
 * predators to most distance predators So, each input index can hold different
 * agents at different times depending upon relative distance, whereas with the
 * index sensors each index of the sensorValue array would have held the same
 * agent per each index constantly throughout the task
 * 
 * @author rollinsa
 *
 */
public class TorusPredatorsByProximitySensorBlock implements TorusPredPreySensorBlock {

	private int numPredators;

	public TorusPredatorsByProximitySensorBlock() {
		numPredators = Parameters.parameters.integerParameter("torusPredators");
	}

	@Override
	/**
	 * @return the sensor inputs for the predators by proximity. The inputs will
	 *         be the X and Y offsets to each predator from this agent. Index 0
	 *         will hold the closest predator and the last index in the array
	 *         will hold the furthest predator (array in ascending order by
	 *         distance) Distance here is defined as the sum of the absolute
	 *         value of the X and Y offsets
	 */
	public double[] sensorValues(TorusAgent me, TorusWorld world, TorusAgent[] preds, TorusAgent[] prey) {
		double[] predOffsets = NNTorusPredPreyController.getAgentOffsets(me, world, preds);
		//Is the agent sensing himself? If so, take that into account
		boolean self = false;
		if(predOffsets.length == numPredators * 2 - 2){
			self = true;
		}
		
		double[] proximityPreds = new double[self ? (2 * numPredators - 2) : (2 * numPredators)];

		// holds sum of absolute values of X and Y offsets and the first index
		// of the original offsets
		double[][] overallDists = new double[self ? (numPredators - 1) : numPredators][2];
		// finds the sum of each absolute value X and Y offset pair and stores
		// them
		for (int i = 0; i < (self ? (numPredators - 1) : numPredators); i++) {
			overallDists[i][0] = Math.abs(predOffsets[2 * i]) + Math.abs(predOffsets[2 * i + 1]);
			overallDists[i][1] = 2 * i;
		}
		// sort overallDists array so that the min index is the min distanced
		// agent (ascending order by distance)
		for (int i = 0; i < (self ? (numPredators - 1) : numPredators); i++) {
			int indexOfMin = 0;
			double minValue = Double.POSITIVE_INFINITY;
			// find the next lowest summation value in the double array
			for (int j = 0; j < (self ? (numPredators - 1) : numPredators); j++) {
				if (overallDists[j][0] < minValue) {
					indexOfMin = j;
					minValue = overallDists[j][0];
				}
			}
			proximityPreds[2 * i] = predOffsets[(int) overallDists[indexOfMin][1]];
			proximityPreds[2 * i + 1] = predOffsets[((int) overallDists[indexOfMin][1]) + 1];
			// this is done so that the agent just put into the array by
			// proximity will not be put in again
			overallDists[indexOfMin][0] = Double.POSITIVE_INFINITY;
		}
		
		//cut off array proximityPreds so that it only senses the closest specified number of agents
		if(me.getAgentType()==0 && !Parameters.parameters.booleanParameter("predsSenseAllPreds")){
			double[] adjustedOffsets = new double[Parameters.parameters.integerParameter("numberPredsSensedByPreds") * 2];
			System.arraycopy(proximityPreds, 0, adjustedOffsets, 0, Parameters.parameters.integerParameter("numberPredsSensedByPreds") * 2);
			return adjustedOffsets;
		}
		if(me.getAgentType()==1 && !Parameters.parameters.booleanParameter("preySenseAllPreds")){
			double[] adjustedOffsets = new double[Parameters.parameters.integerParameter("numberPredsSensedByPrey") * 2];
			System.arraycopy(proximityPreds, 0, adjustedOffsets, 0, Parameters.parameters.integerParameter("numberPredsSensedByPrey") * 2);
			return adjustedOffsets;
		}
		
		return proximityPreds;
	}

	@Override
	/**
	 * @return the total number of sensors for the predators (X and Y offsets to
	 *         each pred)
	 */
	public int numSensors(boolean isPredator) {
		
		if(isPredator){
			if(Parameters.parameters.booleanParameter("predsSenseAllPrey")){
				return numPredators * 2 - 2;
			}else{
				return Parameters.parameters.integerParameter("numberPredsSensedByPreds") * 2;
			}
		}else{
			if(Parameters.parameters.booleanParameter("preySenseAllPrey")){
				return numPredators * 2;
			}else{
				return Parameters.parameters.integerParameter("numberPredsSensedByPrey") * 2;
			}
		}
	}

	@Override
	/**
	 * Finds the sensor labels for this agent and returns them in an array of
	 * Strings The sensor labels for this class will be the list of Predators
	 * being sensed by proximity
	 * 
	 * @return the sensorLabels for the predators by proximity
	 */
	public String[] sensorLabels(boolean isPredator) {	
		if(isPredator){
			if(Parameters.parameters.booleanParameter("predsSenseAllPrey")){
				return NNTorusPredPreyController.sensorLabels(numPredators-1, "Closest Pred");
			}else{
				return NNTorusPredPreyController.sensorLabels(Parameters.parameters.integerParameter("numberPredsSensedByPreds"), "Closest Pred");
			}
		}else{
			if(Parameters.parameters.booleanParameter("preySenseAllPrey")){
				return NNTorusPredPreyController.sensorLabels(numPredators, "Closest Pred");
			}else{
				return NNTorusPredPreyController.sensorLabels(Parameters.parameters.integerParameter("numberPredsSensedByPrey"), "Closest Pred");
			}
		}
	}

}
