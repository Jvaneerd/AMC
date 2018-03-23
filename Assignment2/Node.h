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
	Node();
	Node(int id, int priority, bool isEven, std::string name);
	~Node();
	void addSuccessor(Node* suc);
	std::string toString();
};

