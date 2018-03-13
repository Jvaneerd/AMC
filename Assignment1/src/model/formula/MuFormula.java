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
public class MuFormula extends FixpointFormula {

    public MuFormula(Variable variable, Formula formula, FormulaType binder) {
        super(FormulaType.MU, variable, formula, binder);
    }

    @Override
    public String toString() {
        return "mu " + variable.toString() + "." + formula.toString();
    }

}
