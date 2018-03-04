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
public class edge {
    private node source;
    private node dest;
    private String label;
    
    public edge(node src, node dest, String lbl) {
        this.source = src;
        this.dest = dest;
        this.label = lbl;
    }
    
    public node getSource() { return this.source; }
    public node getDest() { return this.dest; }
    public String getLabel() { return this.label; }
    
    @Override
    public String toString() {
        return String.format("\t%d --%s--> %d", source.getState(), label, dest.getState());
    }
}
