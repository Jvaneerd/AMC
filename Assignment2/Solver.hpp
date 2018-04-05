#include "ParityGame.hpp"
#include "Measure.hpp"

class PGSolver {
private:
  bool isSolved;
  unsigned numberOfLifts;
  const Measure max;
  std::vector<Node> nodes;
  std::vector<Measure> measures;
  Measure progMeasure;
  Measure liftMeasure;
  Measure findMaxMeasure(ParityGame &pg);
  void Prog(unsigned v, unsigned w);
  bool Lift(unsigned v);
public:
  PGSolver(ParityGame &pg);
  void SolvePG();
  void SolvePGWithSmartQueue();
  void SolvePGWithSelfLoops();
  unsigned GetNumberOfLifts();
  std::string GetPGResult();
};
