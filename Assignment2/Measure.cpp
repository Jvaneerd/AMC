#include <algorithm>
#include <iostream>
#include "Measure.hpp"

Measure::Measure(const Measure *M, size_t s)
  : progressValues(s, 0),
    top(false),
    max(M) {
}

Measure::Measure(std::vector<unsigned> maxM)
  : progressValues(maxM.size(), 0),
    top(false),
    max(this) {
  for(int i = 0; i < maxM.size(); i++) {
    this->progressValues[i] = maxM[i];
  }
}

void Measure::makeEqUpTo(int upTo, const Measure &other) {
  this->top = other.isTop();
  auto i = 1;
  for(; i <= upTo && !this->top; i += 2) this->progressValues[i] = other.progressValues[i];
  for(; i < progressValues.size() && !this->top; i+=2) this->progressValues[i] = 0;
}

bool Measure::tryIncrement(int upTo) {
  bool possible = false;
  int i = upTo - 1 + ( upTo % 2);
  for(int i = upTo; i >= 1; i-=2) {
    if(this->progressValues[i] < this->max->progressValues[i]) {
      this->progressValues[i]++;
      possible = true;
      break;
    }
  }
  if(possible) for(int j = i+2; j < this->progressValues.size(); j += 2) this->progressValues[j] = 0;
  return possible;
}

bool Measure::operator==(const Measure &other) const {
  return ((this==&other) ||
	  (this->isTop() && other.isTop()) ||
	  (!this->isTop() && !other.isTop() &&
	   (this->progressValues == other.progressValues)));
}

bool Measure::operator!=(const Measure &other) const {
  return !(*this == other);
}

bool Measure::operator<(const Measure &other) const {
  if(other.isTop()) return true;
  else if(this->isTop()) return false; //unsure; we need to define something so that Top can be used as complement to
                                       //the lexicographic ordering, but this way Top < Top is valid
  auto ret = false;
  for (int i = 1; i < this->progressValues.size() && !ret; i += 2) ret = ret || this->progressValues[i] < other.progressValues[i];

  return ret;
}

bool Measure::operator>(const Measure &other) const {
  if(this->isTop()) return true;
  else if(other.isTop()) return false;
  return !(*this < other); // For the algorithm's purposes, !< is good enough.
                           // Saves a LOT of time if operator== doesn't have to be called
  // return !((*this == other) || //compare by value so operator== is called
  // 	   (*this < other)); //if neither == and < are true, then > must be true
}

bool Measure::operator<=(const Measure &other) const {
  return !(*this > other);
}

bool Measure::operator>=(const Measure &other) const {
  return !(*this < other);
}

std::string Measure::toString() const {
  std::string s = "(";
  for(auto it : this->progressValues) {
    s += std::to_string(it) + ", ";
  }
  s += "Top: ";
  s += this->top ? "true" : "false";
  return s + ")";
}
