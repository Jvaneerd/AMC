#include <iostream>
#include <sstream>
#include <iterator>
#include <algorithm>
#include <map>
#include "Solver.hpp"

Measure PGSolver::findMaxMeasure(ParityGame *pg) const {
  std::vector<unsigned> prioOccs(pg->getMaxPriority() + 1, 0); // +1 because priority 0 needs to be included
  for( auto it : pg->getNodes()) {
    auto p = it->getPriority();
    if(p & 1) prioOccs[p]++; //p & 1 means least significant bit is set, so value is odd
  }
  return Measure(prioOccs);
}

PGSolver::PGSolver(ParityGame *pg)
  : max(findMaxMeasure(pg)),
    pg(pg),
    isSolved(false) {
  for(auto it : pg->getNodes()) progressMeasures[it] = std::shared_ptr<Measure>(new Measure(this->max));
}

std::shared_ptr<Measure> PGSolver::Prog(std::shared_ptr<Node> v, std::shared_ptr<Node> w) {
  auto prio = v->getPriority();
  std::shared_ptr<Measure> res(new Measure(this->max));
  res->makeEqUpTo(prio, *(this->progressMeasures[w])); //At this point, res ==prio w, so least res >=prio w is fulfilled
  if((prio & 1) && !res->isTop()) { //prio & 1 means least significant bit is set, so prio is odd
    if(!res->tryIncrement(prio)) res->makeTop(); //If res can't be incremented in the bounded range, then it must become top
  }
  return res;
}

bool PGSolver::Lift(std::shared_ptr<Node> v) {
  std::shared_ptr<Measure> res(new Measure(this->max));
  if(v->IsEven()) {
    res->makeTop(); //otherwise nothing will be lower than a fresh Measure
    for(auto it : v->getSuccessors()) {
      auto temp = Prog(v, it);
      if(*temp < *res) res = temp; // < to denote min of all progs of successors
    }
  }
  else {
    for(auto it : v->getSuccessors()) {
      auto temp = Prog(v, it);
      if(*temp > *res) res = temp; // > to denote max of all progs of successors
    }
  }
  if(*progressMeasures[v] != *res) { //the result of all the progs is different, update v in progressMeasures
    progressMeasures[v] = res;
    return false; //Something's changed
  }

  return true; //Nothing's changed
}

void PGSolver::SolvePG() {
//  std::cout << "Max measure: " << this->max.toString() << std::endl;
  std::map<std::shared_ptr<Node>, bool> nodeFullyLifted;
  for(auto &it : this->progressMeasures) nodeFullyLifted[it.first] = false;
  while(std::count_if(nodeFullyLifted.begin(), nodeFullyLifted.end(),
		      [&](std::pair<std::shared_ptr<Node>, bool> p){ return !p.second; })) {
    for(auto &it : nodeFullyLifted) it.second = false; //TODO: implement predecessors of node to more easily update.
    for(auto &it : this->progressMeasures) nodeFullyLifted[it.first] = (it.second->isTop() || Lift(it.first));

/*#pragma omp parallel for
    for(auto it = this->progressMeasures.begin(); it != this->progressMeasures.end(); it++) {
      nodeFullyLifted[it->first] = (it->second->isTop() || Lift(it->first));
      }*/
  }
  this->isSolved = true;
}

std::string PGSolver::GetPGResult() {
  if(!this->isSolved) return "PG not solved yet...\n";
  else {
//    std::vector<int> even;
//    std::vector<int> odd;
    std::ostringstream ss;
    for(auto &it : this->progressMeasures) {
      if(it.first->getId() == 0) {
	ss << it.first->toString() << " was won by player: ";
	if(it.second->isTop()) ss << ">ODD<";
	else ss << ">EVEN<";
	ss << std::endl;
	break;
      }
      // if(it.second->isTop()) odd.emplace_back(it.first->getId());
      // else even.emplace_back(it.first->getId());
    }
    // ss << "Nodes won by player even:\n";
    // if(even.size() > 0) {
    //   std::sort(even.begin(), even.end());
    //   std::copy(even.begin(), even.end() - 1, std::ostream_iterator<int>(ss, ", "));
    //   ss << even.back();
    // }
    // ss << "\nNodes won by player odd:\n";
    // if(odd.size() > 0) {
    //   std::sort(odd.begin(), odd.end());
    //   std::copy(odd.begin(), odd.end() - 1, std::ostream_iterator<int>(ss, ", "));
    //   ss << odd.back();
    // }
    return ss.str();
  }
}
