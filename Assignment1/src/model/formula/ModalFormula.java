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

    public ModalFormula(FormulaType type, String action, Formula formula) {
        super(type);
        this.action = action;
        this.formula = formula;
    }

    public String getAction() {
        return action;
    }

    public Formula getFormula() {
        return formula;
    }

    @Override
    public Set<RecursionVariable> getRecursionVariables() {
        return formula.getRecursionVariables();
    }
}
