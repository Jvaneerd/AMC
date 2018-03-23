#include "ParityGame.h"

ParityGame::ParityGame(std::vector<Node*> nodes)
{
	this->nodes = nodes;
}


ParityGame::~ParityGame()
{
}

Node* ParityGame::getNode(int id) {
	return nodes[id];
}

std::string ParityGame::toString() {
	std::string s;
	for (Node* n : nodes) {
		s += n->toString() + "\n";
	}
	return s;
}