JFLAGS = -g -cp ../bin/java/sml.jar Demo.java Gui.java
RFLAGS = -Djava.library.path=../bin/ -cp ../bin/java/sml.jar:. 
JC = javac
JVM= java 
FILE=

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Demo.java \
		Gui.java \


TWENTYFILE = Demo
MAIN = Gui

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(RFLAGS) $(MAIN)

twenty: $(TWENTYFILE).class
	$(JVM) $(RFLAGS) $(TWENTYFILE)

clean:
		$(RM) *.class