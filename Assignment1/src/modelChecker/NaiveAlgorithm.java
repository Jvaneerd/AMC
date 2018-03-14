/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelChecker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import model.formula.Formula;
import model.formula.MuFormula;
import model.formula.NuFormula;
import model.formula.Variable;
import model.lts.LTS;
import model.lts.Node;

/**
 *
 * @author s163360
 */
public class NaiveAlgorithm extends BasicAlgorithm {

    @Override
    protected void initializeVariables(LTS lts, Formula formula) {
        variableAssignments = new HashMap<>();
    }

    @Override
    protected Set<Node> checkMuFormula(LTS lts, MuFormula formula) {
        Variable var = formula.getVariable();
        variableAssignments.put(var.getName(), new HashSet<>());

        Set<Node> previousSolution = null;
        while (previousSolution == null || !previousSolution.equals(variableAssignments.get(var.getName()))) {
            previousSolution = variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var.getName());
    }

    @Override
    protected Set<Node> checkNuFormula(LTS lts, NuFormula formula) {
        Variable var = formula.getVariable();
        variableAssignments.put(var.getName(), new HashSet<>(lts.getNodes()));

        Set<Node> previousSolution = null;
        while (previousSolution == null || !previousSolution.equals(variableAssignments.get(var.getName()))) {
            previousSolution = variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var.getName());
    }

}
