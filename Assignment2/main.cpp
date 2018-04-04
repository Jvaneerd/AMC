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
  std::cout << "Parity game solved in <interesting metric>, results:\n" << sv.GetPGResult() << std::endl;
  return 0;
}
