#pragma once
#include "Node.hpp"
#include <vector>

class ParityGame
{
private:
  std::vector<Node*> nodes;
  int maxPriority;
public:
  ParityGame(std::vector<Node*> nodes, int maxPriority);
  ~ParityGame();
  inline Node* getNode(int id) { return nodes[id]; }
  inline std::vector<Node*> getNodes() { return nodes; }
  inline int getMaxPriority() { return maxPriority; }
  std::string toString();
};

