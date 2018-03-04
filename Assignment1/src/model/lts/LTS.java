/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.lts;

import java.util.ArrayList;

/**
 *
 * @author johri
 */
public class LTS {
    private final ArrayList<node> nodes;
    private final node initial;
    
    public LTS(int n_nodes, int initial) {
        nodes = new ArrayList<>();
        for(int i = 0; i < n_nodes; i++) {
            nodes.add(new node(i));
        }
        this.initial = nodes.get(initial);
    }
    
    public node getNode(int state) { return nodes.get(state); };
    public node getInitial() { return initial; };
    
    @Override
    public String toString() {
        String s = String.format("LTS with %d states. Initial state: %d\n", nodes.size(), initial.getState());
        for(int n = 0; n < nodes.size(); n++) {
            s = String.format("%s%s\n", s, nodes.get(n).toString());
        }
        return s;
    }
}
