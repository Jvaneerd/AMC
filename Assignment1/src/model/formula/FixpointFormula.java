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
    private final FixpointType operator;
    private final FixpointType binder;
    private final boolean isOpen;

    public FixpointFormula(Variable variable, Formula formula, FixpointType operator, FixpointType binder) {
        super(FormulaType.FIXPOINT);
        this.variable = variable;
        this.formula = formula;
        this.operator = operator;
        this.binder = binder;
        this.isOpen = !getFreeVariables().isEmpty();
    }

    public Variable getVariable() {
        return variable;
    }

    public Formula getFormula() {
        return formula;
    }

    public FixpointType getOperator() {
        return operator;
    }

    public FixpointType getBinder() {
        return binder;
    }

    @Override
    protected Set<Variable> getFreeVariables() {
        Set<Variable> vars = formula.getFreeVariables();
        vars.remove(this.variable);
        return vars;
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public Set<Variable> getVariables() {
        Set<Variable> set = new HashSet<>();
        set.add(this.variable);
        return Stream.concat(set.stream(), formula.getVariables().stream()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        if (operator == FixpointType.MU) {
            return "mu " + variable.toString() + "." + formula.toString();
        } else {
            return "nu " + variable.toString() + "." + formula.toString();
        }
    }
}
