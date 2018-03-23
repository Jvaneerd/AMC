#include "ParityGameParser.h"


ParityGameParser::ParityGameParser()
{
}


ParityGameParser::~ParityGameParser()
{
}

ParityGame* ParityGameParser::parse(std::string fileName) {
	std::ifstream infile;
	infile.open(fileName);

	int id, priority, owner;
	std::string successors;
	std::string name;

	//Ignore parity keyword
	infile.ignore(7, ' ');

	std::string nrOfNodesString;
	infile >> nrOfNodesString;
	int maxNodeId = std::stoi(nrOfNodesString.substr(0, nrOfNodesString.length()-1));

	std::vector<Node*> nodes(maxNodeId + 1);
	std::vector<std::string> successorList(maxNodeId + 1);

	// Parse nodes
	while (infile >> id >> priority >> owner >> successors >> name)
	{
		// Create node object
		Node* node = new Node(id, priority, owner == 0, name.substr(1, name.length() - 3));

		// Delete node from memory in case a node with this id already existed to prevent memory leak?
		// Idk what I'm doing with the pointers
		delete nodes[id];

		nodes[id] = node;

		// Store successor list
		successorList[id] = successors;
	}

	// Create parity game
	ParityGame* parityGame = new ParityGame(nodes);

	// Add successors
	for (int i = 0; i <= maxNodeId; i++) {
		Node* src = parityGame->getNode(i);
		std::istringstream ss(successorList[i]);
		std::string suc;
		while (std::getline(ss, suc, ',')) {
			Node* dest = parityGame->getNode(std::stoi(suc));
			src->addSuccessor(dest);
		}
	}

	return parityGame;
}
