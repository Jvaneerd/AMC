#include <iostream>
#include <fstream>
#include <sstream>
#include "ParityGame.h"

class ParityGameParser
{
public:
	ParityGameParser();
	~ParityGameParser();
	ParityGame* parse(std::string fileName);
};

