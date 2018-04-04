#include "Node.hpp"

Node::Node(int id, int priority, bool isEven, std::string name) : id(id), priority(priority), isEven(isEven), name(name) {}

std::string Node::toString() {
  std::string s = isEven ? "<Node " + std::to_string(id) + ": " + name + "> owner: even, " :
    "[Node " + std::to_string(id) + ": " + name + "] owner: odd, ";
  s += "priority: " + std::to_string(priority) + ", successors: ";
  for (auto &it : successors) s += std::to_string(it) + ", ";
  return s;
}
