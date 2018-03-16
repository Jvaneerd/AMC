/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelChecker;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.formula.FixpointFormula;
import model.formula.Formula;
import model.formula.LogicFormula;
import model.formula.LogicOperator;
import model.formula.ModalFormula;
import model.formula.ModalOperator;
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
    private int iterations;

    public Set<Node> checkFormula(LTS lts, Formula formula) {
        this.iterations = 0;
        initializeVariables(lts, formula);
        return recursiveCheckFormula(lts, formula);
    }

    public int getIterations() {
        return iterations;
    }

    protected Set<Node> recursiveCheckFormula(LTS lts, Formula formula) {
        iterations++;
        switch (formula.getType()) {
            case TRUE:
                return new HashSet<>(lts.getNodes());
            case FALSE:
                return new HashSet<>();
            case VARIABLE:
                return variableAssignments.get(((Variable) formula).getName());
            case LOGIC:
                return checkLogicFormula(lts, ((LogicFormula) formula));
            case MODAL:
                return checkModalFormula(lts, (ModalFormula) formula);
            case FIXPOINT:
                return checkFixpointFormula(lts, (FixpointFormula) formula);
            default:
                // Throw exception?
                return null;
        }
    }

    protected abstract void initializeVariables(LTS lts, Formula formula);

    protected abstract Set<Node> checkFixpointFormula(LTS lts, FixpointFormula formula);

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

    private Set<Node> checkModalFormula(LTS lts, ModalFormula formula) {
        Set<Node> nodesFormulaHoldsFor = recursiveCheckFormula(lts, formula.getFormula());
        ModalOperator operator = formula.getOperator();

        Set<Node> nodes = operator == ModalOperator.BOX ? new HashSet<>(lts.getNodes()) : new HashSet<>();

        lts.getNodes().forEach((n) -> {
            for (Edge e : n.getSuccessors()) {
                if (e.getLabel().equals(formula.getAction())) {
                    if (operator == ModalOperator.BOX && !nodesFormulaHoldsFor.contains(e.getDest())) {
                        nodes.remove(n);
                        break;
                    } else if (operator == ModalOperator.DIAMOND && nodesFormulaHoldsFor.contains(e.getDest())) {
                        nodes.add(n);
                        break;
                    }
                }
            }
        });
        return nodes;
    }

    private Set<Node> checkBoxFormula(LTS lts, ModalFormula formula) {
        Set<Node> nodesFormulaHoldsFor = recursiveCheckFormula(lts, formula.getFormula());
        Set<Node> nodes = new HashSet<>(lts.getNodes());

        lts.getNodes().forEach((n) -> {
            for (Edge e : n.getSuccessors()) {
                if (!nodesFormulaHoldsFor.contains(e.getDest()) && e.getLabel().equals(formula.getAction())) {
                    nodes.remove(n);
                    break;
                }
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
