#pragma once
#include <string>
#include <vector>

class Node
{
private:
  int id, priority;
  bool isEven;
  std::string name;
  std::vector<Node*> successors;
public:
  Node(int id, int priority, bool isEven, std::string name);
  void addSuccessor(Node* suc);
  std::string toString();
};

