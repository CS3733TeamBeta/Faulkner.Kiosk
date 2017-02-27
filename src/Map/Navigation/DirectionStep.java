package Map.Navigation;

import Map.Entity.*;


import java.util.LinkedList;

/**
 * Created by IanCJ on 2/23/2017.
 */
public class DirectionStep {
    LinkedList<NodeEdge> stepEdges;
    String instruction;

    public DirectionStep(String instruction){
        this.instruction = instruction;
        this.stepEdges = new LinkedList<>();
    }

    public DirectionStep(String instruction, LinkedList<NodeEdge> stepEdges) {
        this.instruction = instruction;
        this.stepEdges = stepEdges;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
    public String getInstruction() {
        return this.instruction;
    }
    public void setStepEdges(LinkedList<NodeEdge> stepEdges) {
        this.stepEdges = stepEdges;
    }
    public LinkedList<NodeEdge> getStepEdges() {
        return this.stepEdges;
    }

    @Override
    public String toString() {
        return this.instruction;
    }
}