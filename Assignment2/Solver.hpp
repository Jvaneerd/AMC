#include "ParityGame.hpp"
#include "Measure.hpp"

class PGSolver {
private:
  bool isSolved;
  const Measure max;
  std::vector<Node> nodes;
  std::vector<Measure> measures;
  Measure findMaxMeasure(ParityGame &pg);
  Measure Prog(unsigned v, unsigned w);
  bool Lift(unsigned v);
public:
  PGSolver(ParityGame &pg);
  void SolvePG();
  std::string GetPGResult();
};
