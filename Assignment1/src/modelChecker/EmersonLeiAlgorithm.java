/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelChecker;

import java.util.HashMap;
import java.util.Set;
import model.formula.Formula;
import model.formula.MuFormula;
import model.formula.NuFormula;
import model.lts.LTS;
import model.lts.Node;

/**
 *
 * @author s163360
 */
public class EmersonLeiAlgorithm extends BasicAlgorithm {

    @Override
    public Set<Node> checkFormula(LTS lts, Formula formula) {
        switch (formula.getType()) {
            case MU:
                return checkMuFormula(lts, (MuFormula) formula);
            case NU:
                return checkNuFormula(lts, (NuFormula) formula);
            default:
                return super.checkFormula(lts, formula);
        }
    }

    @Override
    protected void initializeVariables() {
        variableAssignments = new HashMap<>();
        // Assign initial values to all variables.
        // We need a method first to retrieve all variables from the formula
    }

    @Override
    protected Set<Node> checkMuFormula(LTS lts, MuFormula formula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Set<Node> checkNuFormula(LTS lts, NuFormula formula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
