#include <algorithm>
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

bool Measure::operator==(const Measure &other) const {
  return ((this==&other) ||
	  (this->isTop() && other.isTop()) ||
	  (this->progressValues == other.progressValues));
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

bool Measure::boundedLesser(int upTo, const Measure &other) const {
  if(other.isTop()) return true;
  else if(this->isTop()) return false;

  return std::lexicographical_compare(this->progressValues.begin(),
				      this->progressValues.begin() + upTo,
				      other.progressValues.begin(),
				      other.progressValues.begin() + upTo);
}

bool Measure::boundedGreater(int upTo, const Measure &other) const {
  if(this->isTop()) return true;
  else if(other.isTop()) return false;
  return !(this->boundedEq(upTo, other) ||
	   this->boundedLesser(upTo, other));
}

bool Measure::boundedEq(int upTo, const Measure &other) const {
  if(this == &other || (this->isTop() && other.isTop())) return true;
  for(int i = 0; i < this->progressValues.size(); i++)
    if(this->progressValues[i] != other.progressValues[i]) return false;
  return true;
}

bool Measure::boundedLessEq(int upTo, const Measure &other) const {
  return !(this->boundedGreater(upTo, other));
}

bool Measure::boundedGreaterEq(int upTo, const Measure &other) const {
  return !(this->boundedLesser(upTo, other));
}

std::string Measure::toString() const {
  std::string s = "(";
  for(auto it : progressValues) {
    s += std::to_string(it) + ", ";
  }
  s += "Top: ";
  s += this->top ? "true" : "false";
  return s + ")";
}
