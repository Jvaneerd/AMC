#include <algorithm>
#include <iostream>
#include <fstream>
#include <sstream>
#include <string.h>
#include "ParityGame.hpp"

namespace PGParser {
  inline ParityGame* pgParse(std::ifstream &infile) {
    int id, priority, owner;
    std::string successors, name;
    int maxPrio = 0;

    //Ignore parity keyword
    infile.ignore(7, ' ');

    std::string nrOfNodesString;
    infile >> nrOfNodesString;
    auto maxNodeId = std::stoi(nrOfNodesString.substr(0, nrOfNodesString.length()-1));

    std::vector<Node*> nodes(maxNodeId + 1, NULL);
    std::vector<std::string> successorList(maxNodeId + 1);

    std::string line;
    const char* lineDelim = " ;";
    char * token;

    std::getline(infile, line); //Finalize reading first line, ugly but works

    // Parse nodes
    while (std::getline(infile, line)) //infile >> id >> priority >> owner >> successors >> name)
    {
      auto dup = strdup(line.c_str());
      id = std::stoi(strtok(dup, lineDelim));
      priority = std::stoi(strtok(NULL, lineDelim));
      owner = std::stoi(strtok(NULL, lineDelim));
      successors = strtok(NULL, lineDelim);
      token = strtok(NULL, lineDelim);
      name = (token == NULL) ? "" : std::string(token).substr(1, std::string(token).length() - 2);
      // Create node object
      auto node = new Node(id, priority, owner == 0, name);

      maxPrio = std::max(priority, maxPrio);
      //Nodes must have unique identifiers; delete nodes previously created under this ID
      if(nodes[id] != NULL) delete(nodes[id]);

      nodes[id] = node;
      // Store successor list
      successorList[id] = successors;
      delete(dup);
    }
    // Create parity game
    auto parityGame = new ParityGame(nodes, maxPrio);

    auto succDelim = ",";
    // Add successors
    for (int i = 0; i <= maxNodeId; i++) {
      auto src = parityGame->getNode(i);
      auto dup = strdup(successorList[i].c_str());
      token = strtok(dup, succDelim);
      while (token != NULL) {
	auto dest = parityGame->getNode(std::stoi(token));
	src->addSuccessor(dest);

	token = strtok(NULL, succDelim);
      }
      delete(dup);
    }

    return parityGame;
  }
}

