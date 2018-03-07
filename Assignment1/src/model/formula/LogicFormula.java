/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.formula;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author s163360
 */
public class LogicFormula extends Formula {

    private final Formula lhs;
    private final Formula rhs;
    private final LogicOperator operator;

    public LogicFormula(LogicOperator operator, Formula lhs, Formula rhs) {
        super(FormulaType.LOGIC);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Formula getLhs() {
        return lhs;
    }

    public Formula getRhs() {
        return rhs;
    }

    public LogicOperator getOperator() {
        return operator;
    }

    @Override
    public Set<String> getRecursionVariables() {
        return Stream.concat(lhs.getRecursionVariables().stream(), rhs.getRecursionVariables().stream()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        if (operator == LogicOperator.AND) {
            return "(" + lhs.toString() + " && " + rhs.toString() + ")";
        } else {
            return "(" + lhs.toString() + " || " + rhs.toString() + ")";
        }
    }

}
