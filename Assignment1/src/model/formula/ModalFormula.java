/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.formula;

import java.util.Set;

/**
 *
 * @author s163360
 */
public class ModalFormula extends Formula {

    protected final String action;
    protected final Formula formula;
    protected final ModalOperator operator;

    public ModalFormula(String action, Formula formula, ModalOperator operator) {
        super(FormulaType.MODAL);
        this.action = action;
        this.formula = formula;
        this.operator = operator;
    }

    public String getAction() {
        return action;
    }

    public Formula getFormula() {
        return formula;
    }

    public ModalOperator getOperator() {
        return operator;
    }

    @Override
    public Set<Variable> getVariables() {
        return formula.getVariables();
    }

    @Override
    protected Set<Variable> getFreeVariables() {
        return formula.getFreeVariables();
    }

    @Override
    public String toString() {
        if (operator == ModalOperator.BOX) {
            return "[" + action + "]" + formula.toString();
        } else {
            return "<" + action + ">" + formula.toString();
        }
    }
}
