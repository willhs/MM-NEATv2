package edu.utexas.cs.nn.tasks.mario;

import edu.utexas.cs.nn.MMNEAT.MMNEAT;
import edu.utexas.cs.nn.evolution.genotypes.Genotype;
import edu.utexas.cs.nn.evolution.genotypes.TWEANNGenotype;
import edu.utexas.cs.nn.networks.Network;
import edu.utexas.cs.nn.networks.NetworkTask;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.scores.Score;
import edu.utexas.cs.nn.tasks.GroupTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by hardwiwill on 16/12/16.
 */
public class CooperativeMarioTask <T extends Network> extends GroupTask implements NetworkTask {

    private MarioTask<Network> task;
    private int populations;

    // only so that MM-NEAT can load this class
    public CooperativeMarioTask() {
        this(new MarioTask(), Parameters.parameters.integerParameter("numCoevolutionSubpops"));
    }

    public CooperativeMarioTask(MarioTask task, int populations) {
        this.task = task;
        this.populations = populations;

        MMNEAT.genotypeExamples = new ArrayList<Genotype>(2);
        // Multitask
        MMNEAT.genotypeExamples.add(new TWEANNGenotype(task.numInputs(), task.MARIO_OUTPUTS, 0));
    }

    @Override
    public String[] sensorLabels() {
        return task.sensorLabels();
    }

    @Override
    public String[] outputLabels() {
        return task.outputLabels();
    }

    @Override
    public ArrayList<Score> evaluate(Genotype[] team) {
        Stream<Score> scoreStream = Arrays.stream(team)
                .map(g -> {
                    Score<Network> score = task.evaluate(g);
                    return score;
                });

        ArrayList<Score> scoreList = new ArrayList<>(scoreStream.collect(Collectors.toList()));
        return scoreList;
    }

    @Override
    public int numberOfPopulations() {
        // delegate to subclass?
        return 0;
    }

    @Override
    public int[] objectivesPerPopulation() {
        return IntStream.range(0, populations)
                .map(p -> 1)
                .toArray();
    }

    @Override
    public int[] otherStatsPerPopulation() {
        return new int[0];
    }

    @Override
    public double getTimeStamp() {
        return 0;
    }

    @Override
    public void finalCleanup() {

    }
}
