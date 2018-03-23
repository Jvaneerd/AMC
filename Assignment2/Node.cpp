#include "Node.h"

Node::Node() {

}

Node::Node(int id, int priority, bool isEven, std::string name)
{
	this->id = id;
	this->priority = priority;
	this->isEven = isEven;
	this->name = name;
}


Node::~Node()
{
}

void Node::addSuccessor(Node* suc) {
	successors.push_back(suc);
}

std::string Node::toString() {
	std::string s;
	if (isEven) {
		s = "<Node " + std::to_string(id) + ": " + name + "> owner: even, ";
	}
	else {
		s = "[Node " + std::to_string(id) + ": " + name + "] owner: odd, ";
	}

	s += "priority: " + std::to_string(priority) + ", successors: ";

	for (Node* n : successors) {
		s += std::to_string(n->id) + ", ";
	}
	return s;
}
