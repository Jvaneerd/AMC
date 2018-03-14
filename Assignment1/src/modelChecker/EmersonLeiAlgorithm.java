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
import model.formula.Formula;
import model.formula.FormulaType;
import model.formula.LogicFormula;
import model.formula.ModalFormula;
import model.formula.MuFormula;
import model.formula.NuFormula;
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
        switch (var.getScope()) {
            case MU:
                variableAssignments.put(var.getName(), new HashSet<>());
                break;
            case NU:
                variableAssignments.put(var.getName(), new HashSet<>(lts.getNodes()));
                break;
            case FREE:
                throw new UnsupportedOperationException("Free variables not supported yet.");
            default:
                break;
        }
    }

    private void resetOpenSubFormulae(Formula formula, LTS lts, FormulaType criterion) {
        switch (formula.getType()) {
            case LOGIC:
                LogicFormula logicFormula = (LogicFormula) formula;
                resetOpenSubFormulae(logicFormula.getLhs(), lts, criterion);
                resetOpenSubFormulae(logicFormula.getRhs(), lts, criterion);
                break;
            case DIAMOND:
                resetOpenSubFormulae(((ModalFormula) formula).getFormula(), lts, criterion);
                break;
            case BOX:
                resetOpenSubFormulae(((ModalFormula) formula).getFormula(), lts, criterion);
                break;
            case MU:
                MuFormula muFormula = (MuFormula) formula;
                if (muFormula.isOpen() && criterion == FormulaType.MU) {
                    resetVariable(muFormula.getVariable(), lts);
                }
                resetOpenSubFormulae(muFormula.getFormula(), lts, criterion);
                break;
            case NU:
                NuFormula nuFormula = (NuFormula) formula;
                if (nuFormula.isOpen() && criterion == FormulaType.NU) {
                    resetVariable(nuFormula.getVariable(), lts);
                }
                resetOpenSubFormulae(nuFormula.getFormula(), lts, criterion);
                break;
            default:
                break;
        }
    }

    @Override
    protected Set<Node> checkMuFormula(LTS lts, MuFormula formula) {
        if (formula.getBinder() == FormulaType.NU) {
            resetOpenSubFormulae(formula, lts, FormulaType.MU);
        }

        return checkFixpointFormula(lts, formula);
    }

    @Override
    protected Set<Node> checkNuFormula(LTS lts, NuFormula formula) {
        if (formula.getBinder() == FormulaType.MU) {
            resetOpenSubFormulae(formula, lts, FormulaType.NU);
        }

        return checkFixpointFormula(lts, formula);
    }

    private Set<Node> checkFixpointFormula(LTS lts, FixpointFormula formula) {
        Variable var = formula.getVariable();

        Set<Node> oldSolution = variableAssignments.get(var.getName());
        variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));

        while (!oldSolution.equals(variableAssignments.get(var.getName()))) {
            oldSolution = variableAssignments.get(var.getName());
            variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var.getName());
    }

}
