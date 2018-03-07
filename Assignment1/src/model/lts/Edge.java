/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.lts;

/**
 *
 * @author johri
 */
public class Edge {
    private final Node source;
    private final Node dest;
    private final String label;
    
    public Edge(Node src, Node dest, String lbl) {
        this.source = src;
        this.dest = dest;
        this.label = lbl;
    }
    
    public Node getSource() { return this.source; }
    public Node getDest() { return this.dest; }
    public String getLabel() { return this.label; }
    
    @Override
    public String toString() {
        return String.format("\t%d --%s--> %d", source.getState(), label, dest.getState());
    }
}
