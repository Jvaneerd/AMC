#include "ParityGame.hpp"
#include "Measure.hpp"
#include <unordered_map>

class PGSolver {
private:
  const Measure max;
  ParityGame *pg;
  std::unordered_map<Node *, Measure *> progressMeasures;
  Measure findMaxMeasure(ParityGame *pg) const;
public:
  PGSolver(ParityGame *pg);
};
