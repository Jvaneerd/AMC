#pragma once
#include "Node.h"
#include <vector>

class ParityGame
{
private:
	std::vector<Node*> nodes;
public:
	ParityGame(std::vector<Node*> nodes);
	~ParityGame();
	Node* getNode(int id);
	std::string toString();
};

