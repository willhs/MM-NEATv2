package edu.utexas.cs.nn.evolution.mutation.tweann;

import edu.utexas.cs.nn.evolution.genotypes.Genotype;
import edu.utexas.cs.nn.evolution.genotypes.TWEANNGenotype;
import edu.utexas.cs.nn.networks.TWEANN;

/**
 * Created by hardwiwill on 12/12/16.
 */
public class DeleteNodeMutation extends TWEANNMutation {

    public DeleteNodeMutation() {
        super("deleteNodeRate");
    }

    @Override
    public void mutate(Genotype<TWEANN> genotype) {
        ((TWEANNGenotype)genotype).deleteNodeMutation();
    }
}
