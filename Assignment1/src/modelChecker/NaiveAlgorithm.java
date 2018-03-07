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
import model.lts.LTS;
import model.lts.Node;

/**
 *
 * @author s163360
 */
public class NaiveAlgorithm extends BasicAlgorithm {

    @Override
    protected Set<Node> recursiveCheckFormula(LTS lts, Formula formula) {
        switch (formula.getType()) {
            case MU:
                return checkMuFormula(lts, (MuFormula) formula);
            case NU:
                return checkNuFormula(lts, (NuFormula) formula);
            default:
                return super.recursiveCheckFormula(lts, formula);
        }
    }

    @Override
    protected void initializeVariables(Formula formula) {
        variableAssignments = new HashMap<>();
        formula.getRecursionVariables().forEach((var) -> {
            variableAssignments.put(var, null);
        });
    }

    @Override
    protected Set<Node> checkMuFormula(LTS lts, MuFormula formula) {
        String var = formula.getVariable().getName();
        variableAssignments.put(var, new HashSet<>());

        Set<Node> previousSolution = null;
        while (previousSolution == null || !previousSolution.equals(variableAssignments.get(var))) {
            previousSolution = variableAssignments.put(var, recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var);
    }

    @Override
    protected Set<Node> checkNuFormula(LTS lts, NuFormula formula) {
        String var = formula.getVariable().getName();
        variableAssignments.put(var, new HashSet<>(lts.getNodes()));

        Set<Node> previousSolution = null;
        while (previousSolution == null || !previousSolution.equals(variableAssignments.get(var))) {
            previousSolution = variableAssignments.put(var, recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var);
    }

}
