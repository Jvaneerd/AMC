/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.formula.Formula;
import model.lts.LTS;
import model.lts.Node;
import modelChecker.EmersonLeiAlgorithm;
import modelChecker.NaiveAlgorithm;
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
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws ParseException, IOException {
        //TODO: proper arguments handling
        //String ltsPath = "tests/demanding/demanding_children_2.aut";
        //String formulaPath = "tests/demanding/johri2.mcf";

        String ltsPath = "tests/boardgame/robots_400.aut";
        String formulaPath = "tests/boardgame/inf_winning_strategy.mcf";

        List<String> ltsList = fileToList(ltsPath);
        String formula = fileToString(formulaPath);

        FormulaParser fp = new FormulaParser();
        LTSParser lp = new LTSParser();

        Formula f = fp.parse(formula);
        LTS l = lp.parse(ltsList);
        System.out.println("Formula:  " + f.toString());
        //System.out.println(l.toString());

        NaiveAlgorithm naiveAlgorithm = new NaiveAlgorithm();
        long startTime = System.currentTimeMillis();
        Set<Node> nodes = naiveAlgorithm.checkFormula(l, f);
        long endTime = System.currentTimeMillis();
        System.out.println("Naive algorithm took " + (endTime - startTime) + " milliseconds");

//        System.out.println("The formula is valid in the following states (naive):");
//        new TreeSet<>(nodes).forEach((n) -> {
//            System.out.print(n.getState() + ", ");
//        });
        EmersonLeiAlgorithm elAlgo = new EmersonLeiAlgorithm();
        startTime = System.currentTimeMillis();
        Set<Node> elNodes = elAlgo.checkFormula(l, f);
        endTime = System.currentTimeMillis();

        System.out.println("\nEmerson-Lei algorithm took " + (endTime - startTime) + " milliseconds");
        if (elNodes.contains(l.getInitial())) {
            System.out.println("Verdict: formula is >TRUE< for initial state");
        } else {
            System.out.println("Verdict: formula is >FALSE< for initial state");
        }
        if (elNodes.equals(nodes)) {
            System.out.println("Sets are equal!");
        } else {
            System.out.println("Sets are not equal!");
        }
//        System.out.println("The formula is valid in the following states (Emerson-Lei):");
//        new TreeSet<>(elNodes).forEach((n) -> {
//            System.out.print(n.getState() + ", ");
//        });
    }

    private static String fileToString(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            throw new IOException("Error while trying to read: " + filePath, e);
        }
        return contentBuilder.toString();
    }

    private static List<String> fileToList(String filePath) throws IOException {
        List<String> fileList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            fileList = stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("Error while trying to read: " + filePath, e);
        }
        return fileList;
    }

}
