/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import model.formula.Formula;
import parser.FormulaParser;
import parser.ParseException;

/**
 *
 * @author johri
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws parser.ParseException
     */
    public static void main(String[] args) throws ParseException {
        FormulaParser fp = new FormulaParser();

        String s = "nu X. (([i]X && ([plato]X && [others]X )) && mu Y. ([i]Y && (<plato>true || <others>true)) )";

        Formula f = fp.parse(s);
        System.out.println(f.toString());
    }

}
