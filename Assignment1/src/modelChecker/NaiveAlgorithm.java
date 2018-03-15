/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelChecker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import model.formula.FixpointFormula;
import model.formula.FixpointType;
import model.formula.Formula;
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
    protected Set<Node> checkFixpointFormula(LTS lts, FixpointFormula formula) {
        Variable var = formula.getVariable();

        if (formula.getOperator() == FixpointType.MU) {
            variableAssignments.put(var.getName(), new HashSet<>());
        } else {
            variableAssignments.put(var.getName(), new HashSet<>(lts.getNodes()));
        }

        Set<Node> previousSolution = null;
        while (previousSolution == null || !previousSolution.equals(variableAssignments.get(var.getName()))) {
            previousSolution = variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var.getName());
    }

}
