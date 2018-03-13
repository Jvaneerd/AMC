/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.formula;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author s163360
 */
public class FixpointFormula extends Formula {

    protected final Variable variable;
    protected final Formula formula;
    private final FormulaType binder;

    public FixpointFormula(FormulaType type, Variable variable, Formula formula, FormulaType binder) {
        super(type);
        this.variable = variable;
        this.formula = formula;
        this.binder = binder;
    }

    public Variable getVariable() {
        return variable;
    }

    public Formula getFormula() {
        return formula;
    }

    public FormulaType getBinder() {
        return binder;
    }

    @Override
    protected Set<Variable> getOpenVariables() {
        Set<Variable> vars = formula.getOpenVariables();
        vars.remove(this.variable);
        return vars;
    }

    public boolean isOpen() {
        return !getOpenVariables().isEmpty();
    }

    @Override
    public Set<Variable> getVariables() {
        Set<Variable> set = new HashSet<>();
        set.add(this.variable);
        return Stream.concat(set.stream(), formula.getVariables().stream()).collect(Collectors.toSet());
    }
}
