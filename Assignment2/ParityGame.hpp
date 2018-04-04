#pragma once
#include "Node.hpp"
#include <vector>

class ParityGame
{
private:
  std::vector<Node> nodes;
  int maxPriority;
public:
  ParityGame(std::vector<Node> nodes, int maxPriority);
  ~ParityGame();
  inline std::vector<Node> getNodes() { return nodes; }
  inline int getMaxPriority() { return maxPriority; }
  std::string toString();
};

