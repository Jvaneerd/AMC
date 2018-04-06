#include <iostream>
#include <fstream>
#include "ParityGameParser.hpp"
#include "Solver.hpp"

int main(int argc, char *argv[]) {
  std::ifstream infile;
  if(argc < 2) {
    std::cout << "Please support a parity game to solve: ./PGP <path_to_parity_game>" << std::endl;
    return -1;
  } else {
    std::string fileName = argv[1]; //File name is "2nd" argument, as argv[0] always contains executable file name
	  infile.open(fileName);
    if(infile.fail()) {
      std::cout << "Could not open file: " << fileName << std::endl;
      return -1;
    }
  }
  auto pg = PGParser::pgParse(infile);

  
  PGSolver sv(pg);
  sv.SolvePG(); // Blocking until PG has been solved
  std::cout << "Parity game solved in " << sv.GetNumberOfLifts() << " lifts, results:\n" << sv.GetPGResult(false) << std::endl;

  PGSolver svRand(pg);
  svRand.SolveRandom();
  std::cout << "Parity game solved randomly in " << svRand.GetNumberOfLifts() << " lifts, results:\n" << svRand.GetPGResult(false) << std::endl;
  
  PGSolver svSmartQueue(pg);
  svSmartQueue.SolvePGWithSmartQueue(); // Blocking until PG has been solved
  std::cout << "Parity game solved smart in " << svSmartQueue.GetNumberOfLifts() << " lifts, results:\n" << svSmartQueue.GetPGResult(false) << std::endl;

  PGSolver svSelfLoops(pg);
  svSelfLoops.SolvePGWithSelfLoops(); // Blocking until PG has been solved
  std::cout << "Parity game solved self loops in " << svSelfLoops.GetNumberOfLifts() << " lifts, results:\n" << svSelfLoops.GetPGResult(false) << std::endl;
  
  PGSolver svRecursive(pg);
  svRecursive.SolveRecursive(); // Blocking until PG has been solved
  std::cout << "Parity game solved recursive in " << svRecursive.GetNumberOfLifts() << " lifts, results:\n" << svRecursive.GetPGResult(true) << std::endl;

  
  bool equalResults = true;
  for (auto &it : pg.getNodes()) {
    if (sv.measures[it.getId()] != svRecursive.measures[it.getId()]) {
      equalResults = false;
      break;
    }
  }

  if (equalResults) {
    std::cout << "Recursive algorithm results EQUAL to naive algorithm results\n";
  }
  else {
    std::cout << "Recursive algorithm results DIFFERENT from naive algorithm results\n";
    }
  
  return 0;
}
