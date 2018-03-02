/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.formula;

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

    @Override
    public String toString() {
        if (operator == LogicOperator.AND) {
            return "(" + lhs.toString() + " && " + rhs.toString() + ")";
        } else {
            return "(" + lhs.toString() + " || " + rhs.toString() + ")";
        }
    }

}
