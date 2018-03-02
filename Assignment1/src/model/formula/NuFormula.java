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
public class NuFormula extends FixpointFormula {

    public NuFormula(Formula variable, Formula formula) {
        super(FormulaType.NU, variable, formula);
    }

    @Override
    public String toString() {
        return "nu " + variable.toString() + "." + formula.toString();
    }

}
