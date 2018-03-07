/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.lts;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author johri
 */
public class node {
    private List<edge> successors;
    private List<edge> predecessors;
    private int state;
    
    public node(int state) { 
        this.state = state;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    }
    
    public void addSuccessor(edge e) { successors.add(e); };
    public void addPredecessor(edge e) { predecessors.add(e); };
    public int getState() { return state; };
    @Override
    public String toString() {
        String s = String.format("Node %d, transitions: %d", state, successors.size());
        for(int e = 0; e < successors.size(); e++) {
            s = String.format("%s\n%s", s, successors.get(e).toString());
        }
        return s;
    }
}
