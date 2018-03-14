/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelChecker;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.formula.BoxFormula;
import model.formula.DiamondFormula;
import model.formula.Formula;
import model.formula.LogicFormula;
import model.formula.LogicOperator;
import model.formula.ModalFormula;
import model.formula.MuFormula;
import model.formula.NuFormula;
import model.formula.Variable;
import model.lts.Edge;
import model.lts.LTS;
import model.lts.Node;

/**
 *
 * @author s163360
 */
public abstract class BasicAlgorithm {

    protected Map<String, Set<Node>> variableAssignments;

    public Set<Node> checkFormula(LTS lts, Formula formula) {
        initializeVariables(lts, formula);
        return recursiveCheckFormula(lts, formula);
    }

    protected Set<Node> recursiveCheckFormula(LTS lts, Formula formula) {
        switch (formula.getType()) {
            case TRUE:
                return new HashSet<>(lts.getNodes());
            case FALSE:
                return new HashSet<>();
            case VARIABLE:
                return variableAssignments.get(((Variable) formula).getName());
            case LOGIC:
                return checkLogicFormula(lts, ((LogicFormula) formula));
            case DIAMOND:
                return checkDiamondFormula(lts, (DiamondFormula) formula);
            case BOX:
                return checkBoxFormula(lts, (BoxFormula) formula);
            case MU:
                return checkMuFormula(lts, (MuFormula) formula);
            case NU:
                return checkNuFormula(lts, (NuFormula) formula);
            default:
                // Throw exception?
                return null;
        }
    }

    protected abstract void initializeVariables(LTS lts, Formula formula);

    protected abstract Set<Node> checkMuFormula(LTS lts, MuFormula formula);

    protected abstract Set<Node> checkNuFormula(LTS lts, NuFormula formula);

    private Set<Node> checkLogicFormula(LTS lts, LogicFormula formula) {
        Set<Node> lhs = recursiveCheckFormula(lts, formula.getLhs());
        Set<Node> rhs = recursiveCheckFormula(lts, formula.getRhs());
        if (formula.getOperator() == LogicOperator.AND) {
            lhs.retainAll(rhs);
        } else {
            lhs.addAll(rhs);
        }
        return lhs;
    }

    private Set<Node> checkBoxFormula(LTS lts, ModalFormula formula) {
        Set<Node> nodesFormulaHoldsFor = recursiveCheckFormula(lts, formula.getFormula());
        Set<Node> nodes = new HashSet<>();

        lts.getNodes().forEach((n) -> {
            boolean addNode = true;
            for (Edge e : n.getSuccessors()) {
                if (e.getLabel().equals(formula.getAction()) && addNode) {
                    addNode = addNode && nodesFormulaHoldsFor.contains(e.getDest());
                }
            }
            if (addNode) {
                nodes.add(n);
            }
        });
        return nodes;
    }

    private Set<Node> checkDiamondFormula(LTS lts, ModalFormula formula) {
        Set<Node> nodesFormulaHoldsFor = recursiveCheckFormula(lts, formula.getFormula());
        Set<Node> nodes = new HashSet<>();

        lts.getNodes().forEach((n) -> {
            for (Edge e : n.getSuccessors()) {
                if (nodesFormulaHoldsFor.contains(e.getDest()) && e.getLabel().equals(formula.getAction())) {
                    nodes.add(n);
                    break;
                }
            }
        });
        return nodes;
    }

}
