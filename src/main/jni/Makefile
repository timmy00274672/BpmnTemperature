# Define a virtual path for .class in the bin directory
vpath %.class CLASS_PATH = ../../../target/classes ../../../target/test-classes

JAVA_HOME=/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt
#JAVA_HOME =  /usr/lib/jvm/java-8-oracle/

CLASS += com.diplab.device.swtich.RpiTrunLightController


# $* matches the target filename without the extension
device.h: com/diplab/device/swtich/RpiTrunLightController.class
	javah -cp ../../../target/classes:../../../target/test-classes \
		-o device.h \
		$(CLASS)
# linux

libdevice.so:	device.h device.c
	mkdir -p ../../../target/jni
	gcc -o ../../../target/jni/libdevice.so -lc -shared -fPIC \
		-I $(JAVA_HOME)/include \
		-I $(JAVA_HOME)/include/linux/ \
		device.c


