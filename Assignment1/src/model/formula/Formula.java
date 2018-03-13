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
public abstract class Formula {

    private final FormulaType type;

    public Formula(FormulaType type) {
        this.type = type;
    }

    public FormulaType getType() {
        return this.type;
    }

    public abstract Set<Variable> getVariables();

    protected abstract Set<Variable> getOpenVariables();
}
