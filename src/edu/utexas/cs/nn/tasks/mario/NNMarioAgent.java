package edu.utexas.cs.nn.tasks.mario;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import edu.utexas.cs.nn.evolution.genotypes.Genotype;
import edu.utexas.cs.nn.networks.Network;
import edu.utexas.cs.nn.parameters.CommonConstants;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.util.MiscUtil;

public class NNMarioAgent<T extends Network> extends MarioAgent {

	private Network network;

	public NNMarioAgent(Genotype<T> genotype) {
		super(genotype);
		network = genotype.getPhenotype();
	}

	@Override
	public void reset() {
		network.flush();
	}


	@Override
	protected double[] evaluateInputs(double[] inputs) {
		return network.process(inputs);
	}
}
