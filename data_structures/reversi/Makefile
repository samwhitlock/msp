# This a Makefile, an input file for the 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     gmake 
# (or just 'make' if you are on a system that uses GNU make by default,
# such as Linux.) You can also clean up junk files and .class files with
#     gmake clean
# Finally, you can run tests with
#     gmake check

# This is not an especially efficient Makefile, because it's not easy to
# figure out the minimal set of Java files that need to be recompiled.  
# So if any .class file does not exist or is older than its .java file,
# we just remove all the .class files, compile the main class, and 
# then compile everything in the plugin directory.  

# Default time limit on tests (CPU time)
CPULIMIT = 15

# Default file-size limit on tests (in 512-byte blocks)
FILELIMIT = 500

# All source files
SRCS = $(wildcard *.java) $(wildcard reversigame/*.java)

# Sources for the public entry points to the system.
MAIN_SRCS = reversi.java 

# Unit test files.
TESTING_SRCS = reversigame/Testing.java 

# Tests of one program
SINGLE_TESTS = $(wildcard testing/test1_*.in)

# Tests of two programs playing each other
DOUBLE_TESTS = $(filter-out test%-vis.in, $(wildcard testing/test2_*.in))

# All other Java sources (see also Project 1 Makefile).
OTHER_SRCS := $(filter-out $(MAIN_SRCS) $(TESTING_SRCS), $(SRCS))

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked

MAIN_CLASSES = $(MAIN_SRCS:.java=.class)
TESTING_CLASSES = $(TESTING_SRCS:.java=.class)
OTHER_CLASSES = $(OTHER_SRCS:.java=.class)

# Tell make that these are not really files.
.PHONY: clean default check regression-test unit-test \
	regression-test-one-program regression-test-two-programs

# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: $(MAIN_CLASSES) $(TESTING_CLASSES)

# If any class is missing, remove all class files and recompile.
$(MAIN_CLASSES): $(MAIN_SRCS) $(OTHER_SRCS)
	echo $$CLASSPATH
	find . -name '*.class' -exec rm -f {} \;
	javac $(JFLAGS) $(MAIN_SRCS)

$(TESTING_CLASSES): $(MAIN_CLASSES) $(TESTING_SRCS)
	javac $(JFLAGS) $(TESTING_SRCS)

# Run Tests.
check: unit-test regression-test

# Run Junit tests.
unit-test: $(TESTING_CLASSES)
	java reversigame.Testing

# Run all regression tests
regression-test: regression-test-one-program regression-test-two-programs

regression-test-one-program: $(MAIN_CLASSES)
	@echo 
	@echo Running tests of a single program...
	./test-single --time=$(CPULIMIT) --filesize=$(FILELIMIT) $(SINGLE_TESTS)

regression-test-two-programs: $(MAIN_CLASSES)
	@echo
	@echo Running tests of two opposing programs...
	./test-double --time=$(CPULIMIT) --filesize=$(FILELIMIT) $(DOUBLE_TESTS)

# Find and remove all *~, *.class, and testing output files.
# Do not touch .svn directories.
clean :
	find . -name .svn -prune -o \( -name '*.class' -o -name '*~' \) -exec rm -f {} \;
	rm -f *.pyc
