#include <algorithm>
#include <iostream>
#include <fstream>
#include <sstream>
#include <string.h>
#include "ParityGame.hpp"

namespace PGParser {
  inline ParityGame pgParse(std::ifstream &infile) {
    int id, priority, owner;
    std::string successors, name;
    int maxPrio = 0;

    //Ignore parity keyword
    infile.ignore(7, ' ');

    std::string nrOfNodesString;
    infile >> nrOfNodesString;
    auto maxNodeId = std::stoi(nrOfNodesString.substr(0, nrOfNodesString.length()-1));

    std::vector<Node> nodes;
//    nodes.reserve(maxNodeId + 1);

    std::string line;
    auto lineDelim = " ;";
    auto succDelim = ",";
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
      Node node(id, priority, owner == 0, name);
      maxPrio = std::max(priority, maxPrio);
      // Store successor list
      dup = strdup(successors.c_str());
      token = strtok(dup, succDelim);
      while (token != NULL) {
	auto dest = std::stoi(token);
	node.addSuccessor(dest);
	token = strtok(NULL, succDelim);
      }
      nodes.emplace_back(std::move(node));
//      nodes[id] = std::move(node);
      delete(dup);
    }

    //Set predecessors
    for(auto &it : nodes) 
      for(auto &sit : it.getSuccessors()) 
	nodes[sit].addPredecessor(it.getId());
    
    // Create parity game
    ParityGame parityGame(nodes, maxPrio);

    return parityGame;
  }
}

