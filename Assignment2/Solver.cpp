#include <iostream>
#include <sstream>
#include <iterator>
#include <algorithm>
#include "Solver.hpp"

Measure PGSolver::findMaxMeasure(ParityGame &pg) {
  std::vector<unsigned> prioOccs(pg.getMaxPriority() + 1, 0); // +1 because priority 0 needs to be included
  for(auto &it : pg.getNodes()) {
    auto p = it.getPriority();
    if(p & 1) prioOccs[p]++; //p & 1 means least significant bit is set, so value is odd
  }
  return Measure(prioOccs);
}

PGSolver::PGSolver(ParityGame &pg)
  : max(findMaxMeasure(pg)),
    isSolved(false),
    nodes(pg.getNodes()),
    progMeasure(&max, max.getSize()),
    liftMeasure(&max, max.getSize()) {
  for(auto &it : pg.getNodes()) measures.emplace_back(Measure(&this->max, this->max.getSize()));
}

void PGSolver::Prog(unsigned v, unsigned w) {
  auto prio = nodes[v].getPriority();
  progMeasure.makeEqUpTo(prio, measures[w]);
//  Measure res(&this->max, this->max.getSize());
//  res.makeEqUpTo(prio, measures[w]); //At this point, res ==prio w, so least res >=prio w is fulfilled
  if((prio & 1) && !progMeasure.isTop()) { //prio & 1 means least significant bit is set, so prio is odd
    if(!progMeasure.tryIncrement(prio)) progMeasure.makeTop(); //If res can't be incremented in the bounded range, then it must become top
  }
}

bool PGSolver::Lift(unsigned v) {
  auto &vnode = nodes[v];
  auto &vmeasure = measures[v];
//  Measure res(&max, max.getSize());
  if(vnode.IsEven()) {
    liftMeasure.makeTop(); //otherwise nothing will be lower than a fresh Measure
    for(auto &it : vnode.getSuccessors()) {
      Prog(v, it);
      if(progMeasure < liftMeasure) liftMeasure = std::move(progMeasure); // < to denote min of all progs of successors
    }
  }
  else {
    for(auto &it : vnode.getSuccessors()) {
      Prog(v, it);
      if(progMeasure > liftMeasure) liftMeasure = std::move(progMeasure); // > to denote max of all progs of successors
    }
  }
  if(measures[v] != liftMeasure) { //the result of all the progs is different, update v in progressMeasures
    measures[v] = std::move(liftMeasure);
    return false; //Something's changed
  }

  return true; //Nothing's changed
}

void PGSolver::SolvePG() {
  std::cout << "Max measure: " << max.toString() << std::endl;
  std::vector<bool> nodeFullyLifted(nodes.size(), false);
  while(std::count(nodeFullyLifted.begin(), nodeFullyLifted.end(), false)) {
    for(auto it : nodeFullyLifted) it = false; //TODO: implement predecessors of node to more easily update. -> new strategy

    for(auto &it : this->nodes) {
      auto id = it.getId();
      nodeFullyLifted[id] = measures[id].isTop() || Lift(id);
    }
  }
  
  this->isSolved = true;
}

std::string PGSolver::GetPGResult() {
  if(!this->isSolved) return "PG not solved yet...\n";
  else {
//    std::vector<int> even;
//    std::vector<int> odd;
    std::ostringstream ss;
    ss << nodes[0].toString() << " was won by player: ";
    if(measures[0].isTop()) ss << ">ODD<";
    else ss << ">EVEN<";
    // for(auto &it : this->nodes) {
    //   if(measures[it.getId()].isTop()) odd.emplace_back(it.getId());
    //   else even.emplace_back(it.getId());
    // }
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
