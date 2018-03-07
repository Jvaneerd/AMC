/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.List;
import model.lts.LTS;
import model.lts.Edge;
import model.lts.Node;

/**
 *
 * @author johri
 */
public class LTSParser {
    private List<String> ltsAsStrings;
    private int edges;
    
    public LTS parse(List<String> ltsAsStrings) throws ParseException {
        this.ltsAsStrings = ltsAsStrings;
        return parseLts();
    }
    
    private LTS parseLts() throws ParseException {
        String ltsDescription = ltsAsStrings.get(0);
        String[] descTokens = ltsDescription.substring(ltsDescription.indexOf('(') + 1, ltsDescription.indexOf(')')).split(",");
        if(descTokens.length != 3) throw new ParseException("Incorrect LTS description: " + ltsDescription);
        LTS l = new LTS(Integer.parseInt(descTokens[2]), Integer.parseInt(descTokens[0]));
        edges = Integer.parseInt(descTokens[1]);
        
        for(int e = 1; e <= edges; e++) {
            String edge = ltsAsStrings.get(e);
            String[] edgeInfo = edge.substring(edge.indexOf('(') + 1, edge.indexOf(')')).split(",");
            int edgeSrc = Integer.parseInt(edgeInfo[0]);
            String edgeLbl = edgeInfo[1].replaceAll("\"", "");
            int edgeDest = Integer.parseInt(edgeInfo[2]);
            Node src = l.getNode(edgeSrc);
            Node dest = l.getNode(edgeDest);
            src.addEdge(new Edge(src, dest, edgeLbl));
        }
        
        return l;
    }
}
