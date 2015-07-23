#include "device.h"

JNIEXPORT void JNICALL Java_com_diplab_device_swtich_RpiTrunLightController_off (JNIEnv *env, jobject obj)
{
	printf("off\n");
}

/*
 * Class:     com_diplab_device_swtich_RpiTrunLightController
 * Method:    on
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_diplab_device_swtich_RpiTrunLightController_on (JNIEnv *env, jobject obj)
{
	printf("on\n");
}

