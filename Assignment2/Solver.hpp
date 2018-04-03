#include "ParityGame.hpp"
#include "Measure.hpp"
#include <unordered_map>
#include <memory>

class PGSolver {
private:
  bool isSolved;
  const Measure max;
  ParityGame *pg;
  //unordered_map is a hash table, so access times are amortized constant!
  std::unordered_map<std::shared_ptr<Node>, std::shared_ptr<Measure>> progressMeasures;
  Measure findMaxMeasure(ParityGame *pg) const;
  std::shared_ptr<Measure> Prog(std::shared_ptr<Node> v, std::shared_ptr<Node> w);
  bool Lift(std::shared_ptr<Node> v);
public:
  PGSolver(ParityGame *pg);
  void SolvePG();
  std::string GetPGResult();
};
