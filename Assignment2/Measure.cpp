#include <algorithm>
#include <iostream>
#include "Measure.hpp"

Measure::Measure(const Measure &M)
  : progressValues(M.getSize(), 0),
    top(false),
    max(&M) {
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
  if(other.isTop()) {
    this->makeTop();
    return;
  }
  for(int i = 0; i <= upTo; i++) {
    this->progressValues[i] = other.progressValues[i];
  }
}

bool Measure::tryIncrement(int upTo) {
  for(int i = upTo; i >= 0; i--) {
    if(this->progressValues[i] < this->max->progressValues[i]) {
      this->progressValues[i]++;
      for(int j = i+1; j < this->progressValues.size(); j++) {
	this->progressValues[j] = 0;
      }
      return true;
    }
  }
  return false;
}

Measure &Measure::operator=(const Measure &other) {
  this->progressValues = other.progressValues;
  this->top = other.top;
  this->max = other.max;
  return *this;
}

bool Measure::operator==(const Measure &other) const {
  return ((this==&other) ||
	  (this->isTop() && other.isTop()) ||
	  (this->progressValues == other.progressValues));
}

bool Measure::operator!=(const Measure &other) const {
  return !(*this == other);
}

bool Measure::operator<(const Measure &other) const {
  if(other.isTop()) return true;
  else if(this->isTop()) return false; //unsure; we need to define something so that Top can be used as complement to
                                       //the lexicographic ordering, but this way Top < Top is valid
  return std::lexicographical_compare(this->progressValues.begin(),
				      this->progressValues.end(),
				      other.progressValues.begin(),
				      other.progressValues.end());
}

bool Measure::operator>(const Measure &other) const {
  if(this->isTop()) return true;
  else if(other.isTop()) return false;
  return !((*this == other) || //compare by value so operator== is called
	   (*this < other)); //if neither == and < are true, then > must be true
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
