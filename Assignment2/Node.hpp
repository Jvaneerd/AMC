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
  Node(int id, int priority, bool isEven, std::string name);

  Node &operator=(const Node &other) = default; /*{
    this->id = other.id;
    this->isEven = other.isEven;
    this->name = other.name;
    this->successors = other.successors;
    this->predecessors = other.predecessors;
    }*/
  
  inline int getPriority() { return priority; }
  inline int getId() { return id; }
  inline bool IsEven() { return isEven; }
  inline std::vector<unsigned> getSuccessors() { return successors; }
  inline void addSuccessor(unsigned suc) { successors.emplace_back(suc); }
  inline void addPredecessor(unsigned pre) { predecessors.emplace_back(pre); }
  std::string toString();
};

