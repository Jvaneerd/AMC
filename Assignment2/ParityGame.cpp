#include "ParityGame.hpp"

ParityGame::ParityGame(std::vector<Node*> nodes, int maxPriority)
  : nodes(nodes),
    maxPriority(maxPriority) {
}

ParityGame::~ParityGame() {
  for (Node* n : nodes) delete(n);
}

std::string ParityGame::toString() {
  std::string s;
  for (Node* n : nodes) s+= n->toString() + "\n";
  return s;
}
