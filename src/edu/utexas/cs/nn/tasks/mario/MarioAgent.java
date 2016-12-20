package edu.utexas.cs.nn.tasks.mario;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import edu.utexas.cs.nn.evolution.Organism;
import edu.utexas.cs.nn.evolution.genotypes.Genotype;
import edu.utexas.cs.nn.parameters.CommonConstants;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.util.MiscUtil;

/**
 * Created by hardwiwill on 16/12/16.
 */
public abstract class MarioAgent <T> extends Organism implements Agent {
    private String name = "NNMarioAgent";
    static final int SUB_LEFT = 0;
    static final int SUB_RIGHT = 2;
    static final int SUB_DOWN = 4;
    static final int SUB_JUMP = 7;
    static final int SUB_SPEED = 6;
    static int jumpCount = 0;
    static int stuckCount = 0;
    static int xPrev = 0;
    static int xStart;
    static int yStart;
    static int width;
    static int height;
    static int xEnd;
    static int yEnd;

    public MarioAgent(Genotype<T> genotype) {
        super(genotype);
        xStart = Parameters.parameters.integerParameter("marioInputStartX");
        yStart = Parameters.parameters.integerParameter("marioInputStartY");
        width = Parameters.parameters.integerParameter("marioInputWidth");
        height = Parameters.parameters.integerParameter("marioInputHeight");
        xEnd = height + xStart;
        yEnd = width + yStart;
    }

    /**
     * Getter for the Agent type
     */
    @Override
    public AGENT_TYPE getType() {
        return AGENT_TYPE.AI;
    }

    /**
     * Getter for the Agent name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Setter for Agent name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Using the scene (byte[][]) determines if the (x, y) is 1 or 0
     * @param x
     * @param y
     * @param scene
     * @return
     */
    protected double probe(int x, int y, byte[][] scene) {
        int realX = x + 11; // unsure about these magic numbers -Gab
        int realY = y + 11;
        return (scene[realX][realY] != 0) ? 1 : 0;
    }

    @Override
    public boolean[] getAction(Environment observation) {
        byte[][] worldScene = observation.getLevelSceneObservation(/*1*/);
        byte[][] enemiesScene = observation.getEnemiesObservation(/*1*/);


        int xPos = (int) observation.getMarioFloatPos()[0];

        if(xPos <= xPrev){
            stuckCount++;
            if(stuckCount > Parameters.parameters.integerParameter("marioStuckTimeout")){
                stuckCount = 0;
                return null; // kill mario
            }
            xPrev = Math.max(xPrev, xPos);
        } else {
            stuckCount = 0;
            xPrev = xPos;
        }
        //System.out.println("Current x position " + xPos);
        //System.out.println("Previous x position " + xPrev);
        //System.out.println("Stuck Count " + stuckCount);
        //System.out.println("Stuck Timeout " + Parameters.parameters.integerParameter("marioStuckTimeout"));

        int worldBuffer = 0;
        int enemiesBuffer = (width * height);
        double[] inputs;
        if(!CommonConstants.hyperNEAT){
            inputs = new double[((width * height) * 2) + 1];
        } else {
            inputs = new double[((width * height) * 2)];
        }
        for(int x = xStart; x < xEnd; x++){
            for(int y = yStart; y < yEnd; y++){
                inputs[worldBuffer++] = probe(x, y, worldScene);
                inputs[enemiesBuffer++] = probe(x, y, enemiesScene);
                //System.out.println("	(" + x + ", " + y + ") world(" + (worldBuffer-1) + "): " + inputs[worldBuffer-1] + ", enemies(" + (enemiesBuffer-1) + "): " + inputs[enemiesBuffer-1]);
            }
        }
        if(!CommonConstants.hyperNEAT){
            inputs[enemiesBuffer++] = 1; // HyperNEAT does not need a bias
        }
        if(Parameters.parameters.booleanParameter("showMarioInputs")){
            printMarioWorld(inputs);
            MiscUtil.waitForReadStringAndEnterKeyPress();
        }

        double[] outputs = evaluateInputs(inputs);

        boolean[] action = new boolean[outputs.length];
        if(!CommonConstants.hyperNEAT){
            for (int i = 0; i < action.length; i++) {
                action[i] = outputs[i] > 0;
            }
        } else {
            action[Mario.KEY_LEFT] = outputs[SUB_LEFT] > 0;
            action[Mario.KEY_RIGHT] = outputs[SUB_RIGHT] > 0;
            action[Mario.KEY_DOWN] = outputs[SUB_DOWN] > 0;
            action[Mario.KEY_JUMP] = outputs[SUB_JUMP] > 0;
            action[Mario.KEY_SPEED] = outputs[SUB_SPEED] > 0;
        }

        if(action[Mario.KEY_JUMP]){
            jumpCount++;
            if(jumpCount == Parameters.parameters.integerParameter("marioJumpTimeout")){
                action[Mario.KEY_JUMP] = false;
                jumpCount = 0;
            }
        } else {
            jumpCount = 0;
        }

        return action;
    }

    /**
     * Compute action given outputs
     * @param inputs
     * @return
     */
    protected abstract double[] evaluateInputs(double[] inputs);

    public static void printMarioWorld(double[] inputs){
        System.out.println("World: (# for objects, X for enemies)");
        System.out.println("(0 is top left, goes right then down, etc.)");
        int width = Parameters.parameters.integerParameter("marioInputWidth");
        int height = Parameters.parameters.integerParameter("marioInputHeight");
        int worldBuffer = 0;
        int enemiesBuffer = (width * height);
        for(int y = 0; y < height; y++){
            System.out.print("	");
            for(int x = 0; x < width; x++){
                String bit;
                if(inputs[worldBuffer++] == 1.0){
                    bit = "#";
                } else if (inputs[enemiesBuffer++] == 1.0){
                    bit = "X";
                } else {
                    bit = "_";
                }
                System.out.print("[" + bit + "]");
            }
            System.out.println();
        }
    }
}
