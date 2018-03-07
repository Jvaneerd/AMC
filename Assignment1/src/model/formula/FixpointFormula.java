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

}
