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
    private List<edge> edges;
    private int state;
    
    public node(int state) { 
        this.state = state;
        this.edges = new ArrayList<>();
    }
    
    public void addEdge(edge e) { edges.add(e); };
    public int getState() { return state; };
    @Override
    public String toString() {
        String s = String.format("Node %d, transitions: %d", state, edges.size());
        for(int e = 0; e < edges.size(); e++) {
            s = String.format("%s\n%s", s, edges.get(e).toString());
        }
        return s;
    }
}
