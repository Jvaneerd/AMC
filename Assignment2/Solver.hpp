#include "ParityGame.hpp"
#include "Measure.hpp"

class PGSolver {
  struct InputCompare
  {
    InputCompare(const PGSolver& c) : mySolver(c) {}
    inline bool operator() (unsigned n1, unsigned n2) const
      {
	return mySolver.nodes[n1].getId() < mySolver.nodes[n2].getId();
      }
    const PGSolver& mySolver;
  };
  
  struct RandomCompare
  {
    RandomCompare(const PGSolver& c) : mySolver(c) {}
    inline bool operator() (unsigned n1, unsigned n2) const
      {
	return mySolver.randomOrder[n1] < mySolver.randomOrder[n2];
      }
    const PGSolver& mySolver;
  };
  
  struct SmartCompare
  {
    SmartCompare(const PGSolver& c) : mySolver(c) {}
	inline bool operator() (unsigned i1, unsigned i2) const
	{
		const Node &n1 = mySolver.nodes[i1];
		const Node &n2 = mySolver.nodes[i2];
		if (n1.getPriority() % 2 != n2.getPriority() % 2) return n1.getPriority() % 2;
		if (n1.getNrPredec() != n2.getNrPredec()) return n1.getNrPredec() > n2.getNrPredec();
		if (n1.IsEven() != n2.IsEven()) return !n1.IsEven();
		if (n1.getPriority() != n2.getPriority()) return n1.getPriority() < n2.getPriority();
		return n1.getId() < n2.getId();
	}
    const PGSolver& mySolver;
  };
  
  struct Comparator {
    inline bool operator() (const Node& n1, const Node& n2) const {};
  };
  

private:
  bool isSolved;
  unsigned numberOfLifts;
  const Measure max;
  std::vector<Node> nodes;
  std::vector<unsigned> parents;
  std::vector<unsigned> numberOfPreds;
  std::vector<unsigned> randomOrder;

  Measure progMeasure;
  Measure liftMeasure;
  Measure findMaxMeasure(ParityGame &pg);
  void Prog(unsigned v, unsigned w);
  bool Lift(unsigned v);

  void InitRandomOrder();

  template <typename Comparator>
  void SolvePGIterative(); 
  template <typename Comparator>
  void SolvePGQueue();
  void SolvePGCycles();
  bool SolveCycle(unsigned loc);
public:
  PGSolver(ParityGame &pg);
  std::vector<Measure> measures;
  void SolvePG(std::string strategy, std::string order);
  unsigned GetNumberOfLifts();
  std::string GetPGResult(bool all);
};
