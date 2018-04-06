#pragma once
#include <string>
#include <vector>

class Node
{
private:
  unsigned id, priority;
  bool isEven;
  std::string name;
  std::vector<unsigned> successors;
  std::vector<unsigned> predecessors;
  
public:
  Node(Node &&N) = default;
  Node(const Node &N) = default;
  Node() = default;
  Node(int id, int priority, bool isEven, std::string name);

  Node &operator=(const Node &other) = default;
  
  inline int getPriority() { return priority; }
  inline int getId() { return id; }
  inline bool IsEven() { return isEven; }
  inline std::vector<unsigned> getSuccessors() { return successors; }
  inline std::vector<unsigned> getPredecessors() { return predecessors; }
  inline void addSuccessor(unsigned suc) { successors.emplace_back(suc); }
  inline void addPredecessor(unsigned pre) { predecessors.emplace_back(pre); }
  std::string toString();

  bool operator<(const Node &other) const;
  bool operator>(const Node &other) const;
};
