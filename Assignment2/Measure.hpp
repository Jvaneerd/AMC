#include <vector>
#include <string>
#include <iostream>

class Measure {
private:
  std::vector<unsigned> progressValues;
  bool top;
  const Measure *max;
public:
  Measure(Measure &&M) = default;
  Measure(const Measure *M, size_t s);
  Measure(std::vector<unsigned> maxM);
  inline bool isTop() const { return this->top; }
  inline void makeTop() { this->top = true; }
  inline size_t getSize() const { return this->progressValues.size(); }

  void makeEqUpTo(int upTo, const Measure &other);
  bool tryIncrement(int upTo);

  Measure &operator=(const Measure &other) = default;
  Measure &operator=(Measure &&other) {
    this->top = other.isTop();
    std::swap(this->progressValues, other.progressValues);
    return *this;
  }

  bool operator!=(const Measure &other) const;
  bool operator<(const Measure &other) const;
  bool operator>(const Measure &other) const;
  bool operator==(const Measure &other) const;
  bool operator<=(const Measure &other) const;
  bool operator>=(const Measure &other) const;

  std::string toString() const;
};
