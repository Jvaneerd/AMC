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
public class Node implements Comparable {

    private final List<Edge> successors;
    private final int state;

    public Node(int state) {
        this.state = state;
        this.successors = new ArrayList<>();
    }

    public void addSuccessor(Edge e) {
        successors.add(e);
    }

    public List<Edge> getSuccessors() {
        return successors;
    }

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        String s = String.format("Node %d, transitions: %d", state, successors.size());
        for (int e = 0; e < successors.size(); e++) {
            s = String.format("%s\n%s", s, successors.get(e).toString());
        }
        return s;
    }

    @Override
    public int compareTo(Object t) {
        return this.state - ((Node) t).getState();
    }
}
