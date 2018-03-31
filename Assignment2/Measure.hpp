#include <vector>
#include <string>

class Measure {
private:
  std::vector<unsigned> progressValues;
  bool top;
  const Measure *max;
public:
  Measure(const Measure &M);
  Measure(std::vector<unsigned> maxM);
  inline bool isTop() const { return top; }
  inline void makeTop() { top = true; }
  inline size_t getSize() const { return progressValues.size(); }

  bool operator<(const Measure &other) const;
  bool operator>(const Measure &other) const;
  bool operator==(const Measure &other) const;
  bool operator<=(const Measure &other) const;
  bool operator>=(const Measure &other) const;

  bool boundedLesser(int upTo, const Measure &other) const;
  bool boundedGreater(int upTo, const Measure &other) const;
  bool boundedEq(int upTo, const Measure &other) const;
  bool boundedLessEq(int upTo, const Measure &other) const;
  bool boundedGreaterEq(int upTo, const Measure &other) const;

  std::string toString() const;
};
