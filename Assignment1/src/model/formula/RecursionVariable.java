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
public class RecursionVariable extends Formula {

    private final String name;
          
    public RecursionVariable(String name) {
        super(FormulaType.VARIABLE);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
