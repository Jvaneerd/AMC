#include <iostream>
#include "Solver.hpp"

Measure PGSolver::findMaxMeasure(ParityGame *pg) const {
  std::vector<unsigned> prioOccs(pg->getMaxPriority() + 1, 0); // +1 because priority 0 needs to be included
  for( Node *n : pg->getNodes()) {
    auto p = n->getPriority();
    if(p & 1) prioOccs[p]++; //p & 1 means least significant bit is set, so value is odd
  }
  return Measure(prioOccs);
}

PGSolver::PGSolver(ParityGame *pg) : max(findMaxMeasure(pg)), pg(pg) {
  for(Node *n : pg->getNodes()) {
    progressMeasures[n] = new Measure(this->max);
  }

  for(auto it : progressMeasures) {
    std::cout << " " << it.first->toString() << " : " << it.second->toString() << std::endl;
  }

  std::cout << "Solver created" << std::endl;
  std::cout << "Max measure: " << this->max.toString() << std::endl;
}
