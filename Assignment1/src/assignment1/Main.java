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
import java.util.Scanner;
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

    static Scanner scanner;

    /**
     * @param args the command line arguments
     * @throws parser.ParseException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws ParseException, IOException {
        if (args.length >= 2) {
            String ltsPath = args[0];
            String formulaPath = args[1];
            boolean emersonLei = true;
            boolean naive = true;
            if (args.length == 3) {
                naive = !args[2].equals("-e");
                emersonLei = !args[2].equals("-n");
            }
            evaluate(ltsPath, formulaPath, emersonLei, naive);
        } else {
            while (true) {
                scanner = new Scanner(System.in);

                System.out.println("Which test case would you like to run?");
                System.out.println("1. Dining philosophers");
                System.out.println("2. Demanding children");
                System.out.println("3. Cache coherence");
                System.out.println("4. Boardgame");
                System.out.println("5. Custom (give own input files)");

                String testString = scanner.next();
                int test;
                if (testString.matches("[0-9]")) {
                    test = Integer.parseInt(testString);

                    switch (test) {
                        case 1:
                            evaluatePhilosophers();
                            break;
                        case 2:
                            evaluateChildren();
                            break;
                        case 3:
                            evaluateCache();
                            break;
                        case 4:
                            evaluateBoardgame();
                            break;
                        case 5:
                            evaluateCustom();
                            break;
                        default:
                            System.out.println("Error: that is not a valid test case.");
                            break;
                    }
                } else {
                    System.out.println("Error: that is not a valid test case.");
                }
                System.out.println("------------------------------------------------");
            }
        }
        System.out.println("------------------------------------------------");
    }

    private static void evaluatePhilosophers() throws IOException, ParseException {
        System.out.print("How many philosophers (which LTS) do you want to use? Pick an int from [2..11]:");
        String nrPhilosophers = scanner.next();
        if (nrPhilosophers.matches("(2|3|4|5|6|7|8|9|10|11)")) {
            String ltsPath = "tests/philosophers/dining_" + nrPhilosophers + ".aut";

            System.out.println("Which formula would you like to evaluate?");
            System.out.println("1. Invariantly possibly eat");
            System.out.println("2. Invariantly inevitably eat");
            System.out.println("3. Invariantly Plato starves");
            System.out.println("4. Plato infinitely often can eat");
            String formulaString = scanner.next();
            if (formulaString.matches("(1|2|3|4)")) {
                int formula = Integer.parseInt(formulaString);
                String formulaPath = "tests/philosophers/";

                switch (formula) {
                    case 1:
                        formulaPath += "invariantly_possibly_eat.mcf";
                        break;
                    case 2:
                        formulaPath += "invariantly_inevitably_eat.mcf";
                        break;
                    case 3:
                        formulaPath += "invariantly_plato_starves.mcf";
                        break;
                    case 4:
                        formulaPath += "plato_infinitely_often_can_eat.mcf";
                        break;
                }
                System.out.println("------------------------------------------------");
                evaluate(ltsPath, formulaPath, true, true);
            } else {
                System.out.println("Error: that is not a valid formula.");
            }
        } else {
            System.out.println("Error: nr of philosophers must be in the range [2-11].");
        }
    }

    private static void evaluateChildren() throws IOException, ParseException {
        System.out.print("How many children (which LTS) do you want to use? Pick an int from [2..10]:");
        String nrChildren = scanner.next();
        if (nrChildren.matches("(2|3|4|5|6|7|8|9|10)")) {
            String ltsPath = "tests/demanding/demanding_children_" + nrChildren + ".aut";

            System.out.println("Which formula would you like to evaluate?");
            System.out.println("1. Child can play infinitely often");
            System.out.println("2. Invariantly eventually attention");
            System.out.println("3. Invariantly child alive");
            System.out.println("4. Invariantly children alternating");
            String formulaString = scanner.next();
            if (formulaString.matches("(1|2|3|4)")) {
                int formula = Integer.parseInt(formulaString);
                String formulaPath = "tests/demanding/";

                switch (formula) {
                    case 1:
                        formulaPath += "child_can_play_infinitely_often.mcf";
                        break;
                    case 2:
                        formulaPath += "invariantly_eventually_attention.mcf";
                        break;
                    case 3:
                        formulaPath += "invariantly_child_alive.mcf";
                        break;
                    case 4:
                        formulaPath += "invariantly_children_alternating.mcf";
                        break;
                }
                System.out.println("------------------------------------------------");
                evaluate(ltsPath, formulaPath, true, true);
            } else {
                System.out.println("Error: that is not a valid formula.");
            }
        } else {
            System.out.println("Error: nr of children must be in the range [2-10].");
        }
    }

    private static void evaluateCache() throws IOException, ParseException {
        System.out.print("How many clients (which LTS) do you want to use? Pick an int from [2..5]:");
        String nrPhilosophers = scanner.next();
        if (nrPhilosophers.matches("(2|3|4|5)")) {
            String ltsPath = "tests/ccp/german_linear_" + nrPhilosophers + ".1.aut";

            System.out.println("Which formula would you like to evaluate?");
            System.out.println("1. Invariantly possibly exclusive access");
            System.out.println("2. Invariantly inevitably exclusive access");
            System.out.println("3. Invariantly eventually fair shared access");
            System.out.println("4. Infinite run no access");
            System.out.println("5. Infinitely often exclusive");

            String formulaString = scanner.next();
            if (formulaString.matches("(1|2|3|4|5)")) {
                int formula = Integer.parseInt(formulaString);
                String formulaPath = "tests/ccp/";

                switch (formula) {
                    case 1:
                        formulaPath += "invariantly_possibly_exclusive_access.mcf";
                        break;
                    case 2:
                        formulaPath += "invariantly_inevitably_exclusive_access.mcf";
                        break;
                    case 3:
                        formulaPath += "invariantly_eventually_fair_shared_access.mcf";
                        break;
                    case 4:
                        formulaPath += "infinite_run_no_access.mcf";
                        break;
                    case 5:
                        formulaPath += "infinitely_often_exclusive.mcf";
                        break;
                }
                System.out.println("------------------------------------------------");
                evaluate(ltsPath, formulaPath, true, true);
            } else {
                System.out.println("Error: that is not a valid formula.");
            }
        } else {
            System.out.println("Error: nr of clients must be in the range [2-5].");
        }
    }

    private static void evaluateBoardgame() throws IOException, ParseException {
        System.out.print("How many large would you like the boardgame to be (which LTS do you want to use)? Pick an int from {50,100,150,200,250,300,350,400,450,500}:");
        String boardSize = scanner.next();
        if (boardSize.matches("(50|100|150|200|250|300|350|400|450|500)")) {
            String ltsPath = "tests/boardgame/robots_" + boardSize + ".aut";

            System.out.println("Which formula would you like to evaluate?");
            System.out.println("1. Exists a winning play");
            System.out.println("2. Exists a winning strategy");
            System.out.println("3. Exists an infinite winning play");
            System.out.println("4. Exists an infinite winning strategy");

            String formulaString = scanner.next();
            if (formulaString.matches("(1|2|3|4)")) {
                int formula = Integer.parseInt(formulaString);
                String formulaPath = "tests/boardgame/";

                switch (formula) {
                    case 1:
                        formulaPath += "winning_play.mcf";
                        break;
                    case 2:
                        formulaPath += "winning_strategy.mcf";
                        break;
                    case 3:
                        formulaPath += "inf_winning_play.mcf";
                        break;
                    case 4:
                        formulaPath += "inf_winning_strategy.mcf";
                        break;
                    case 5:
                        formulaPath += "infinitely_often_exclusive.mcf";
                        break;
                }
                System.out.println("------------------------------------------------");
                evaluate(ltsPath, formulaPath, true, true);
            } else {
                System.out.println("Error: that is not a valid formula.");
            }
        } else {
            System.out.println("Error: boardgame size must be in {50,100,150,200,250,300,350,400,450,500}.");
        }
    }

    private static void evaluateCustom() throws IOException, ParseException {
        System.out.println("Give the filepath of the LTS you want to use.");
        String ltsPath = scanner.next();

        System.out.println("Give the filepath of the formula you want to evaluate.");
        String formulaPath = scanner.next();

        evaluate(ltsPath, formulaPath, true, true);
    }

    private static void evaluate(String ltsPath, String formulaPath, boolean emersonLei, boolean naive) throws IOException, ParseException {
        List<String> ltsList = fileToList(ltsPath);
        String formula = fileToString(formulaPath);

        FormulaParser fp = new FormulaParser();
        LTSParser lp = new LTSParser();

        Formula f = fp.parse(formula);
        LTS l = lp.parse(ltsList);
        System.out.println("Formula:  " + f.toString());
        System.out.println("LTS: " + ltsPath + " consisting of " + l.getNodes().size() + " states\n");

        long startTime;
        long endTime;
        Set<Node> naiveNodes = null;
        Set<Node> elNodes = null;

        if (naive) {
            NaiveAlgorithm naiveAlgorithm = new NaiveAlgorithm();
            startTime = System.currentTimeMillis();
            naiveNodes = naiveAlgorithm.checkFormula(l, f);
            endTime = System.currentTimeMillis();
            System.out.println("Naive algorithm took " + (endTime - startTime) + " milliseconds and " + naiveAlgorithm.getIterations() + " iterations");
        }
//        System.out.println("The formula is valid in the following states (naive):");
//        new TreeSet<>(nodes).forEach((n) -> {
//            System.out.print(n.getState() + ", ");
//        });
        if (emersonLei) {
            EmersonLeiAlgorithm elAlgo = new EmersonLeiAlgorithm();
            startTime = System.currentTimeMillis();
            elNodes = elAlgo.checkFormula(l, f);
            endTime = System.currentTimeMillis();
            System.out.println("Emerson-Lei algorithm took " + (endTime - startTime) + " milliseconds and " + elAlgo.getIterations() + " iterations");

        }

        Set<Node> nodes = naive ? naiveNodes : elNodes;
        if (nodes.contains(l.getInitial())) {
            System.out.println("\nVerdict: formula is >TRUE< for initial state");
        } else {
            System.out.println("\nVerdict: formula is >FALSE< for initial state");
        }

        if (naive && emersonLei) {
            if (elNodes.equals(naiveNodes)) {
                System.out.println("The algorithms computed the same result!");
            } else {
                System.out.println("The algorithms computed a different result");
            }
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
