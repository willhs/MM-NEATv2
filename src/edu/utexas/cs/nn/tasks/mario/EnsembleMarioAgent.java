package edu.utexas.cs.nn.tasks.mario;

import edu.utexas.cs.nn.evolution.genotypes.Genotype;
import edu.utexas.cs.nn.networks.Network;
import edu.utexas.cs.nn.scores.Score;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hardwiwill on 16/12/16.
 */
public class EnsembleMarioAgent <T extends List<Network>> extends MarioAgent {

    private List<Network> ensemble;
    private MarioEnsembleArbitrator arbitrator;

    public EnsembleMarioAgent(Genotype<T> genotype) {
        super(genotype);
        this.ensemble = genotype.getPhenotype();
    }

    @Override
    protected double[] evaluateInputs(double[] inputs) {
        double[][] outputs = ensemble.stream()
                .map(n -> n.process(inputs))
                .toArray(double[][]::new);

        return arbitrator.arbitrate(outputs);
    }

    @Override
    public void reset() {

    }
}
