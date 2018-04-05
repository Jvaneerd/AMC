#include "Node.hpp"

Node::Node(int id, int priority, bool isEven, std::string name) : id(id), priority(priority), isEven(isEven), name(name) {}

std::string Node::toString() {
  std::string s = isEven ? "<Node " + std::to_string(id) + ": " + name + "> owner: even, " :
    "[Node " + std::to_string(id) + ": " + name + "] owner: odd, ";
  s += "priority: " + std::to_string(priority) + ", successors: ";
  for (auto &it : successors) s += std::to_string(it) + ", ";
  s += "predecessors: ";
  for (auto &it : predecessors) s += std::to_string(it) + ", ";
  return s;
}

bool Node::operator<(const Node &other) const {
	if (this->priority % 2 != other.priority % 2) return this->priority % 2;
	if (this->isEven != other.isEven) return !this->isEven;
	if (this->priority != other.priority) return this->priority < other.priority;
	return this->id < other.id;
}

bool Node::operator>(const Node &other) const {
	if (this->priority % 2 != other.priority % 2) return !this->priority % 2;
	if (this->isEven != other.isEven) return this->isEven;
	if (this->priority != other.priority) return this->priority > other.priority;
	return this->id > other.id;
}
