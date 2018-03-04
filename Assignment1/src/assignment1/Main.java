/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import model.formula.Formula;
import model.lts.LTS;
import parser.FormulaParser;
import parser.LTSParser;
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
    public static void main(String[] args) throws ParseException, IOException {
        //TODO: proper arguments handling
        List<String> ltsList;
        ltsList = Files.lines(new File(args[0]).toPath()).collect(Collectors.toList());
        FormulaParser fp = new FormulaParser();
        LTSParser lp = new LTSParser();

        String s = "nu X. (([i]X && ([plato]X && [others]X )) && mu Y. ([i]Y && (<plato>true || <others>true)) )";

        Formula f = fp.parse(s);
        LTS l = lp.parse(ltsList);
        System.out.println(f.toString());
        System.out.println(l.toString());
    }

}
