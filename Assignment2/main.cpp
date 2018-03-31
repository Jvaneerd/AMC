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
  ParityGame* pg = PGParser::pgParse(infile);
  std::cout << pg->toString();
  PGSolver* sv = new PGSolver(pg);
  delete(pg);
  return 0;
}
