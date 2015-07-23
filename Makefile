JAVA_HOME = /usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt
#JAVA_HOME =  /usr/lib/jvm/java-8-oracle/
EMPTY :=
SPACE := $(EMPTY) $(EMPTY)

CLASSPATH += target/dependency/*
CLASSPATH += target/classes
CLASSPATH += target/test-classes
CLASSPATH += src/test/resource
CLASSPATH += src/main/resource

JAVA_FLAG += -Djava.library.path=./target/jni

clean:
	rm -rf target/*classes target/jni/*

environment_RPi:
	mvn test-compile dependency:copy-dependencies
	#make -C src/main/jni libdevice.so

TestOpenOnRpiLight: 
	java -cp $(subst $(SPACE),:,$(CLASSPATH)) \
		$(JAVA_FLAG) \
		com.diplab.activiti.test.process.TestOpenOnRpiLight
