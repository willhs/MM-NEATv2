package edu.utexas.cs.nn.evolution.mutation.tweann;

import edu.utexas.cs.nn.evolution.genotypes.Genotype;
import edu.utexas.cs.nn.evolution.genotypes.TWEANNGenotype;
import edu.utexas.cs.nn.networks.TWEANN;

/**
 * Created by hardwiwill on 17/01/17.
 */
public class DeleteLinkMutationSandpile extends TWEANNMutation {

    public DeleteLinkMutationSandpile() {
        super("deleteLinkSandpileRate");
    }

    @Override
    /**
     * Mutates TWEANNGenotype
     * @param genotype TWEANNGenotype to mutate
     */
    public void mutate(Genotype<TWEANN> genotype) {
        ((TWEANNGenotype) genotype).deleteLinkSandpileMutation();
    }
}
