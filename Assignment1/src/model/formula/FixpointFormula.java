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

    protected final RecursionVariable variable;
    protected final Formula formula;

    public FixpointFormula(FormulaType type, RecursionVariable variable, Formula formula) {
        super(type);
        this.variable = variable;
        this.formula = formula;
    }

    public RecursionVariable getVariable() {
        return variable;
    }

    public Formula getFormula() {
        return formula;
    }

    @Override
    public Set<RecursionVariable> getRecursionVariables() {
        Set<RecursionVariable> set = new HashSet<>();
        set.add(this.variable);
        return Stream.concat(set.stream(), formula.getRecursionVariables().stream()).collect(Collectors.toSet());
    }
}
