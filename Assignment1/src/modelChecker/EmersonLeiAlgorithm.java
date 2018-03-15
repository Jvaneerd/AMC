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
import model.formula.LogicFormula;
import model.formula.ModalFormula;
import model.formula.Variable;
import model.lts.LTS;
import model.lts.Node;

/**
 *
 * @author s163360
 */
public class EmersonLeiAlgorithm extends BasicAlgorithm {

    @Override
    protected void initializeVariables(LTS lts, Formula formula) {
        variableAssignments = new HashMap<>();
        formula.getVariables().forEach((var) -> {
            resetVariable(var, lts);
        });
    }

    private void resetVariable(Variable var, LTS lts) {
        if (var.getScope() == FixpointType.MU) {
            variableAssignments.put(var.getName(), new HashSet<>());
        } else {
            variableAssignments.put(var.getName(), new HashSet<>(lts.getNodes()));
        }
    }

    private void resetOpenSubFormulae(Formula formula, LTS lts, FixpointType criterion) {
        switch (formula.getType()) {
            case LOGIC:
                LogicFormula logicFormula = (LogicFormula) formula;
                resetOpenSubFormulae(logicFormula.getLhs(), lts, criterion);
                resetOpenSubFormulae(logicFormula.getRhs(), lts, criterion);
                break;
            case MODAL:
                resetOpenSubFormulae(((ModalFormula) formula).getFormula(), lts, criterion);
                break;
            case FIXPOINT:
                FixpointFormula fixpointFormula = (FixpointFormula) formula;
                if (fixpointFormula.isOpen() && criterion == fixpointFormula.getOperator()) {
                    resetVariable(fixpointFormula.getVariable(), lts);
                }
                resetOpenSubFormulae(fixpointFormula.getFormula(), lts, criterion);
                break;
            default:
                break;
        }
    }

    @Override
    protected Set<Node> checkFixpointFormula(LTS lts, FixpointFormula formula) {
        Variable var = formula.getVariable();

        if (formula.getBinder() != formula.getOperator()) {
            resetOpenSubFormulae(formula, lts, formula.getOperator());
        }

        Set<Node> oldSolution = variableAssignments.get(var.getName());
        variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));

        while (!oldSolution.equals(variableAssignments.get(var.getName()))) {
            oldSolution = variableAssignments.get(var.getName());
            variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var.getName());
    }

}
