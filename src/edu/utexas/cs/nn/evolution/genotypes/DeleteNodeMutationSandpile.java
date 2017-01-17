package edu.utexas.cs.nn.evolution.genotypes;

import edu.utexas.cs.nn.evolution.mutation.Mutation;
import edu.utexas.cs.nn.evolution.mutation.tweann.TWEANNMutation;
import edu.utexas.cs.nn.networks.TWEANN;

/**
 * Created by hardwiwill on 17/01/17.
 */
public class DeleteNodeMutationSandpile extends TWEANNMutation {

    public DeleteNodeMutationSandpile() {
        super("deleteNodeSandpile");
    }

    @Override
    public void mutate(Genotype<TWEANN> genotype) {
        ((TWEANNGenotype) genotype).deleteNodeSandpileMutation();
    }
}
