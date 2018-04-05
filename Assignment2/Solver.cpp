#include <iostream>
#include <sstream>
#include <iterator>
#include <algorithm>
#include <set>
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

void PGSolver::SolvePG() {
	std::cout << "Max measure: " << max.toString() << std::endl;
	std::vector<bool> nodeFullyLifted(nodes.size(), false);

	while (std::count(nodeFullyLifted.begin(), nodeFullyLifted.end(), false)) {
		// std::cout << "Iteration " << i++ << std::endl;
		// std::cout << "count: " << std::count(nodeFullyLifted.begin(), nodeFullyLifted.end(), false) << std::endl;
		for (auto it : nodeFullyLifted) it = false; //TODO: implement predecessors of node to more easily update. -> new strategy

		for (auto &it : this->nodes) {
			auto id = it.getId();
			nodeFullyLifted[id] = measures[id].isTop() || Lift(id);
		}
	}

	this->isSolved = true;
}

/*
Solve parity game by utilizing a queue that is ordered from low to high according to the following rules:
1) Node with odd priority < Node with even priority
2) Node owned by odd < Node owned by even
3) Node.priority
4) Node.Id

At the start all nodes are added to the queue.
When a node is lifted, remove it from the queue.
When a node is lifted with success (the measure has changed) add its predecessors to the queue (if they are not TOP already)
*/
void PGSolver::SolvePGWithSmartQueue() {
	std::set<Node> queue;
	for (auto &it : this->nodes) queue.insert(it);

	while (!queue.empty()) {
		auto node = *queue.begin();
		auto id = node.getId();
		queue.erase(node);

		if (!measures[id].isTop() && !Lift(id)) {
			for (auto it : node.getPredecessors()) {
				if (!measures[it].isTop()) queue.insert(nodes[it]);
			}
		}
	}

	this->isSolved = true;
}

/*
Same algorithm as previous, except that at the start all nodes with self-loops are found that can immediately be set to TOP.
No difference in lifts on dining/cache test cases compared to other algorithm.
Small difference in lifts on the example from the SPM lecture.
*/
void PGSolver::SolvePGWithSelfLoops() {
	std::set<Node> queue;
	for (auto &it : this->nodes) queue.insert(it);

	// Find all nodes owned by odd with an odd priority and a self loop.
	// They can immediately be set to TOP.
	for (auto &it : this->nodes) {
		if (!it.IsEven() && it.getPriority() & 1) {
			auto pred = it.getPredecessors();
			auto find = std::find(pred.begin(), pred.end(), it.getId());
			if (find != pred.end()) measures[it.getId()].makeTop();
		}
	}

	while (!queue.empty()) {
		auto node = *queue.begin();
		auto id = node.getId();
		queue.erase(node);

		if (!measures[id].isTop() && !Lift(id)) {
			for (auto it : node.getPredecessors()) {
				if (!measures[it].isTop()) queue.insert(nodes[it]);
			}
		}
	}

	this->isSolved = true;
}


std::vector<unsigned> parents;

/*
Recursively solve, by starting from a node and then solving its predecessors
Also looks for cycles and solves those first when found
When no useful cycles are found, algorithm uses more lifts than other algorithms
However, when good cycles are found, #lifts may drastically decrease
For example: dining_5.invariantly_inevitably_eat.gm
*/
void PGSolver::SolveRecursive() {
	for (auto it : this->nodes) {
		SolveNode(it.getId());
	}
	this->isSolved = true;
}

bool PGSolver::SolveCycle() {
	auto i = 0;
	bool changed = false;
	while (!measures[parents[i]].isTop() && !Lift(parents[i])) {
		i++;
		i %= parents.size();
		changed = true;
	}
	return changed;
}

void PGSolver::SolveNode(unsigned id) {
	bool cycleSolved = false;
	if (!parents.empty() && id == parents[0]) {
		// We have made a cycle.
		// Solve cycle first, might lead to easy TOP.
		cycleSolved = SolveCycle();
	}

	parents.push_back(id);
	if (cycleSolved || (!measures[id].isTop() && !Lift(id))) {
		Node node = nodes[id];
		for (auto it : node.getPredecessors()) {
			SolveNode(it);
		}
	}
	parents.pop_back();

}



unsigned PGSolver::GetNumberOfLifts()
{
	return numberOfLifts;
}

std::string PGSolver::GetPGResult() {
  if(!this->isSolved) return "PG not solved yet...\n";
  else {
	  //    std::vector<int> even;
	  //    std::vector<int> odd;
	  std::ostringstream ss;

	  /*
	  for (auto &it : this->nodes) {
		  ss << it.toString() << " was won by player: ";
		  if (measures[it.getId()].isTop()) ss << ">ODD<";
		  else ss << ">EVEN<";
		  ss << std::endl;
	  }
	  */

	  ss << nodes[0].toString() << " was won by player: ";
	  if (measures[0].isTop()) ss << ">ODD<";
	  else ss << ">EVEN<";
	  ss << std::endl;

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
