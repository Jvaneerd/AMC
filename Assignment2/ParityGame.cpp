#include "ParityGame.hpp"

ParityGame::ParityGame(std::vector<Node> nodes, int maxPriority)
  : nodes(nodes),
    maxPriority(maxPriority) {
}

ParityGame::~ParityGame() {
}

std::string ParityGame::toString() {
  std::string s;
  for (auto &it : nodes) s+= it.toString() + "\n";
  return s;
}
