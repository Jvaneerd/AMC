#include "stdafx.h"
#include <iostream>


void FuncWithPtr(int &ptr) {
	ptr = 4;
}

int main() {
	ParityGameParser parser;
	ParityGame* pg = parser.parse("tests/parityGame.gm");
	std::cout << pg->toString() << std::endl;

	int i;
	auto foo = [&](int i2) { std::cout << "test" << std::endl; std::cin >> i; i += i2; };
	foo(3);
	std::cout << "value of i: " << i << std::endl;
	FuncWithPtr(i);
	std::cout << "value of i: " << i << std::endl;
	std::cin >> i;
	return 0;
}