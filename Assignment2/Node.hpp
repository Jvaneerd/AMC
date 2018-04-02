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
  //TODO: predecessors to update after a lift
public:
  Node(int id, int priority, bool isEven, std::string name);
  inline int getPriority() { return priority; }
  inline int getId() { return id; }
  inline bool IsEven() { return isEven; }
  inline std::vector<Node*> getSuccessors() { return successors; }
  void addSuccessor(Node* suc);
  std::string toString();
};

