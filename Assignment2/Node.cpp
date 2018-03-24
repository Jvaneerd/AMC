#include "Node.h"

Node::Node(int id, int priority, bool isEven, std::string name) : id(id), priority(priority), isEven(isEven), name(name) {}

void Node::addSuccessor(Node* suc) {
	successors.push_back(suc);
}

std::string Node::toString() {
  std::string s = isEven ? "<Node " + std::to_string(id) + ": " + name + "> owner: even, " :
    "[Node " + std::to_string(id) + ": " + name + "] owner: odd, ";
  s += "priority: " + std::to_string(priority) + ", successors: ";
  for (Node* n : successors) s += std::to_string(n->id) + ", ";
  return s;
}
