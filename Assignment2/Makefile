CXX=g++
#CXX=clang++
RM=rm -f
CPPFLAGS=-std=c++11 -O3
LDFLAGS=
LDLIBS=

SRCS=main.cpp Node.cpp ParityGame.cpp Measure.cpp Solver.cpp
OBJS=$(subst .cpp,.o,$(SRCS))
TARGET=PGP

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) $(LDFLAGS) -o $(TARGET) $(OBJS) $(LDLIBS) 

depend: .depend

.depend: $(SRCS)
	$(RM) ./.depend
	$(CXX) $(CPPFLAGS) -MM $^>>./.depend;

clean:
	$(RM) $(OBJS)

distclean: clean
	$(RM) *~ .depend

include .depend
