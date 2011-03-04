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

# On the instructional machines, we've defined the environment variable
# $STAFFLIBDIR to be ~cs61b/lib, where we keep .jar (Java Archive) files
# containig staff classes.  At home, you can define STAFFLIBDIR to point at
# wherever you keep copies of these files.

STAFF_TRACKER = $(STAFFLIBDIR)/proj2-tracker.jar
STAFF_UTIL = $(STAFFLIBDIR)/proj2-util.jar

OTHER_JARS = $(STAFFLIBDIR)/ucb.jar:$(STAFFLIBDIR)/ucb-f2007.jar:$(STAFFLIBDIR)/junit.jar

# All source files
SRCS = $(wildcard *.java) $(wildcard tracker/*.java) $(wildcard util/*.java)

ALL_TESTS = $(wildcard testing/*.trk)

# Sources for the public entry points to the system.
MAIN_SRCS = track.java  \
	    util/Set2D.java util/QuadTree.java  \
	    util/Debugging.java \
	    util/SimpleSet2D.java

# Unit test files.
TESTING_SRCS = tracker/Testing.java util/Testing.java

# All other Java sources (see also Project 1 Makefile).
OTHER_SRCS := $(filter-out $(MAIN_SRCS) $(TESTING_SRCS), $(SRCS))

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked

MAIN_CLASSES = $(MAIN_SRCS:.java=.class)
TESTING_CLASSES = $(TESTING_SRCS:.java=.class)
OTHER_CLASSES = $(OTHER_SRCS:.java=.class)

# Tell make that these are not really files.
.PHONY: clean default check check-util check-tracker regression-test \
        regress-both regress-util regress-tracker


# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: $(MAIN_CLASSES) 

# If any class is missing, remove all class files and recompile.
$(MAIN_CLASSES): $(MAIN_SRCS) $(OTHER_SRCS)
	echo $$CLASSPATH
	find . -name '*.class' -exec rm -f {} \;
	javac $(JFLAGS) $(MAIN_SRCS)

$(TESTING_CLASSES): $(MAIN_CLASSES) $(TESTING_SRCS)
	javac $(JFLAGS) $(TESTING_SRCS)

# Create a JAR file out of my tracker classes
tracker.jar: $(MAIN_CLASSES)
	jar cf $@ tracker

# Create a JAR file out of my util classes
util.jar: $(MAIN_CLASSES)
	jar cf $@ tracker

# Run Tests.
check: check-util check-tracker regression-test

# Run util Junit tests.
check-util: util/Testing.class
	java util.Testing

# Run tracker Junit tests.
check-tracker: tracker/Testing.class
	java tracker.Testing

# Run all regression tests
regression-test: regress-util regress-tracker regress-both

# Run all regression tests using my tracker and util packages.
regress-both: default
	@echo 
	@echo "Running tests of both packages..."
	python test-track $(ALL_TESTS)

# Run all regression tests using my util package and the staff tracker package. 
regress-util: util.jar
	@echo 
	@echo "Running tests of util package against staff tracker package..."
	CLASSPATH=util.jar:$(STAFF_TRACKER):$(OTHER_JARS) python test-track $(ALL_TESTS)

# Run all regression tests using my tracker package and the staff util package. 
regress-tracker: tracker.jar
	@echo 
	@echo "Running tests of tracker package against staff util package..."
	CLASSPATH=tracker.jar:$(STAFF_UTIL):$(OTHER_JARS) python test-track $(ALL_TESTS)

# Find and remove all *~, *.class, and *.out files, and the generated jar
# files.  Do not touch .svn directories.
clean :
	rm -f tracker.jar util.jar ERRS
	find . -name .svn -prune -o \( -name '*.out' -o -name '*.class' -o -name '*~' \) -exec rm -f {} \;
