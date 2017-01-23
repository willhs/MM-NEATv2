package edu.utexas.cs.nn.tasks.mario;

import edu.utexas.cs.nn.util.datastructures.ArrayUtil;
import edu.utexas.cs.nn.util.stats.StatisticsUtilities;

/**
 * Created by hardwiwill on 16/12/16.
 */
public class AverageArbitrator implements MarioEnsembleArbitrator {

    public double[] arbitrate(double[][] outputs) {
        double[] averages = new double[outputs[0].length];
        for (int i = 0; i < outputs[0].length; i++) {
            averages[i] = StatisticsUtilities.average(ArrayUtil.column(outputs, i));
        }
        return averages;
    }
}
