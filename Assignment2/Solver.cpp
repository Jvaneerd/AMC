#include <iostream>
#include <sstream>
#include <iterator>
#include <algorithm>
#include <ctime>
#include <cstdlib>
#include <set>
#include <stack>
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
    numberOfLifts(0),
    isSolved(false),
    nodes(pg.getNodes()),
    progMeasure(&max, max.getSize()),
    liftMeasure(&max, max.getSize()) {
  for(auto &it : pg.getNodes()) measures.emplace_back(Measure(&this->max, this->max.getSize()));
}

void PGSolver::Prog(unsigned v, unsigned w) {
  auto prio = nodes[v].getPriority();
  progMeasure.makeEqUpTo(prio, measures[w]);
  if((prio & 1) && !progMeasure.isTop()) { //prio & 1 means least significant bit is set, so prio is odd
    if(!progMeasure.tryIncrement(prio)) progMeasure.makeTop(); //If res can't be incremented in the bounded range, then it must become top
  }
}

bool PGSolver::Lift(unsigned v) {
  numberOfLifts++;
  auto &vnode = nodes[v];
  auto &vmeasure = measures[v];
  if(vnode.IsEven()) {
    liftMeasure.makeTop(); //otherwise nothing will be lower than a fresh Measure
    for(auto &it : vnode.getSuccessors()) {
      Prog(v, it);
      if(progMeasure < liftMeasure) liftMeasure = std::move(progMeasure); // < to denote min of all progs of successors
    }
  }
  else {
    liftMeasure.makeEqUpTo(max.getSize()-1, vmeasure);
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


void PGSolver::InitRandomOrder() {
  std::srand(std::time(NULL));
  for (auto it : nodes) {
    randomOrder.push_back(std::rand());
  }
}

void PGSolver::SolvePG(std::string strategy, std::string order) {
  std::cout << "Max measure: " << max.toString() << std::endl;
  std::cout << "Strategy: " << strategy << " Order: " << order << std::endl;
  
  if(strategy == "-s2"){
    std::cout << "Solving parity game using queue strategy." << std::endl;
    if (order == "-o2") {
      InitRandomOrder();
      SolvePGQueue<RandomCompare>();
    } else if (order == "-o3") {
      SolvePGQueue<SmartCompare>();
    }
    else {
      SolvePGQueue<InputCompare>();
    }
  }
  else if (strategy == "-s3") {
    std::cout << "Solving parity game using cycle strategy." << std::endl;
    SolvePGCycles();
  }
  else {
    std::cout << "Solving parity game using iterative strategy." << std::endl;
    if (order == "-o2") {
      InitRandomOrder();
      SolvePGIterative<RandomCompare>();
    }
    else if (order == "-o3") {
      SolvePGIterative<SmartCompare>();
    }
    else {
      SolvePGIterative<InputCompare>();
    }
  }
  this->isSolved = true;
}

// Iterative strategy
template <typename Comparator>
void PGSolver::SolvePGIterative() {
  bool stable = false;
  
  std::vector<unsigned> queue;
  for (auto it : nodes) queue.push_back(it.getId());
  std::sort(queue.begin(), queue.end(), Comparator(*this));
  
  while (!stable) {
    stable = true;
    for (auto &it : queue) {
      stable &= measures[it].isTop() || Lift(it);
    }
  }
}


// Queue strategy
template <typename Comparator>
void PGSolver::SolvePGQueue() {
  std::set<unsigned, Comparator> queue(Comparator(*this));
  
  for (auto &it : this->nodes) queue.insert(it.getId());
  
  while (!queue.empty()) {
    auto id = *queue.begin();
    queue.erase(id);

    if (!measures[id].isTop() && !Lift(id)) {
      for (auto &it : nodes[id].getPredecessors()) {
	if (!measures[it].isTop()) queue.insert(it);
      }
    }
  }
}


// Cycle strategy
void PGSolver::SolvePGCycles() {
  std::stack<unsigned> stack;

  // Push all nodes on the stack
  for (auto &it : this->nodes)  stack.push(it.getId());

  while (!stack.empty()) {
    // Pop top node
    auto id = stack.top();
    stack.pop();

    // Decrease number of preds
    if (!parents.empty()) {
      unsigned last = numberOfPreds.back();
      numberOfPreds.pop_back();
      numberOfPreds.emplace_back(last - 1);
    }
    
    auto preds = 0;
    if (!measures[id].isTop()) {
      // Check for cycles
      bool startChanged = false;
      unsigned startIndex = std::distance(parents.begin(), std::find(parents.begin(), parents.end(), id));
      if (startIndex < parents.size()) {
	startChanged = SolveCycle(startIndex);
      }
      
      // Try to lift
      if (startChanged || !Lift(id)) {
	// Add predecessors to stack if lift was successful
	for (auto &it : nodes[id].getPredecessors()) {
	  stack.push(it);
	  preds++;
	}
      }
    }
    // If predecessors where added to stack, update parents and predecessor count lists
    if (preds > 0) {
      parents.emplace_back(id);
      numberOfPreds.emplace_back(preds);
    }
    // If no predecessors where added, remove all parents of which all predecessors are handled
    else {
      while (!parents.empty() && numberOfPreds.back() == 0) {
	numberOfPreds.pop_back();
	parents.pop_back();
      }
    }
  }
}

bool PGSolver::SolveCycle(unsigned startIndex) {
  bool startChanged = !Lift(parents[startIndex]);
  bool allChanged = startChanged;
  auto i = startIndex + 1;
  auto round = 0;
  while (allChanged && i < parents.size() && round < 2) {
    allChanged &= !Lift(parents[i]);
    i++;
    if (i == parents.size()) {
      round++;
      i = startIndex;
    }
  }
  // If the measure of all nodes in the cycle changed, it will eventually be lifted to top, hence make everything in cycle TOP
  if (allChanged) {
    for (int i = startIndex; i < parents.size(); i++) measures[parents[i]].makeTop();
  }
  return startChanged;
}

unsigned PGSolver::GetNumberOfLifts()
{
  return numberOfLifts;
}

std::string PGSolver::GetPGResult(bool all) {
	if (!this->isSolved) return "PG not solved yet...\n";
	else {
		std::ostringstream ss;

		if (all) {
			for (auto &it : this->nodes) {
				ss << it.toString() << " was won by player: ";
				if (measures[it.getId()].isTop()) ss << ">ODD<";
				else ss << ">EVEN<";
				ss << std::endl;
			}
		}
		else {
			ss << nodes[0].toString() << " was won by player: ";
			if (measures[0].isTop()) ss << ">ODD<";
			else ss << ">EVEN<";
			ss << std::endl;
		}
		return ss.str();
	}

}
