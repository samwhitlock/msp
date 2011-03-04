# This a Makefile, an input file for the 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     gmake 
# (or just 'make' if you are on a system that uses GNU make by default,
# such as Linux.) You can also clean up junk files and .class files with
#     gmake clean
# Finally, you can run JUnit tests with
#     gmake check

# This is not an especially efficient Makefile, because it's not easy to
# figure out the minimal set of Java files that need to be recompiled.  
# So if any .class file does not exist or is older than its .java file,
# we just remove all the .class files, and compile the main classes.

# Sources for the public entry points to the system.
MAIN_SRCS = draw.java drawing/Testing.java

# All Java sources in the drawing package.  The $(function...) notation
# is specific to gmake.  wildcard does file-name substitution as in the 
# shell, and filter-out removes things that match any of a set of patterns
# from a list.
OTHER_SRCS := $(filter-out $(MAIN_SRCS), $(wildcard drawing/*.java))

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.
JFLAGS = -g -Xlint:unchecked

# The $(NAME:.java=.class) notation substitutes .java for trailing .class
# extensions.
MAIN_CLASSES = $(MAIN_SRCS:.java=.class)
OTHER_CLASSES = $(OTHER_SRCS:.java=.class)

ALL_TESTS = $(wildcard drawing-tests/*.drw)

# Tell make that these targets are not really files.
.PHONY: clean default check compile unit-test regression-test

# By default, recompile if any sources have changed since the last build.
# This will fail to recompile properly if you compile, then delete 
# a .class file in the drawing package, and then compile again.  You
# shouldn't do that, of course, but if it happens, just do a 'make clean'
# first and then 'gmake'
default: $(MAIN_CLASSES)

$(MAIN_CLASSES): $(MAIN_SRCS) $(OTHER_SRCS)
	rm -f $(OTHER_CLASSES)
	javac $(JFLAGS) $(MAIN_SRCS)

# Compile all Java files in sight, just to see that they compile.  (Before you
# actually use a new class Foo in your program, 'gmake default' or 'gmake'
# will never compile it.  This target is here to check out such classes
# before you actually start using them).
compile:
	javac $(JFLAGS) $(MAIN_SRCS) $(OTHER_SRCS)

# Run JUnit tests.
check : unit-test regression-test

unit-test: drawing/Testing.class
	java drawing.Testing

regression-test: draw.class
	python test-draw $(ALL_TESTS)

clean :
	rm -f *~ drawing/*~ 
	rm -f *.class drawing/*.class
	rm -f drawing-tests/*.ps
