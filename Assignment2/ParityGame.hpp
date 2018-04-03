#pragma once
#include "Node.hpp"
#include <vector>
#include <memory>

class ParityGame
{
private:
  std::vector<std::shared_ptr<Node>> nodes;
  int maxPriority;
public:
  ParityGame(std::vector<std::shared_ptr<Node>> nodes, int maxPriority);
  ~ParityGame();
  inline std::shared_ptr<Node> getNode(int id) { return nodes[id]; }
  inline std::vector<std::shared_ptr<Node>> getNodes() { return nodes; }
  inline int getMaxPriority() { return maxPriority; }
  std::string toString();
};

