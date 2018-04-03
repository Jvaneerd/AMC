#pragma once
#include <string>
#include <vector>
#include <memory>

class Node
{
private:
  int id, priority;
  bool isEven;
  std::string name;
  std::vector<std::shared_ptr<Node>> successors;
  //TODO: predecessors to update after a lift
public:
  Node(int id, int priority, bool isEven, std::string name);
  inline int getPriority() { return priority; }
  inline int getId() { return id; }
  inline bool IsEven() { return isEven; }
  inline std::vector<std::shared_ptr<Node>> getSuccessors() { return successors; }
  void addSuccessor(std::shared_ptr<Node> suc);
  std::string toString();
};

