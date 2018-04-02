#include "ParityGame.hpp"
#include "Measure.hpp"
#include <unordered_map>

class PGSolver {
private:
  bool isSolved;
  const Measure max;
  ParityGame *pg;
  std::unordered_map<Node *, Measure *> progressMeasures;
  Measure findMaxMeasure(ParityGame *pg) const;
  Measure *Prog(Node &v, Node &w);
  bool Lift(Node &v);
public:
  PGSolver(ParityGame *pg);
  void SolvePG();
  std::string GetPGResult();
};
