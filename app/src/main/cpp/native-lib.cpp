#include <jni.h>
#include <string>
#include <zlib.h>
#include <iostream>
#include <fstream>

uLong calculateCrc32(const std::string& filePath);

extern "C"
JNIEXPORT jlong JNICALL
Java_com_kirillmokhnatkin_appinfochecker_domain_interactor_GetApplicationInfoInteractor_calculateApkCheckSum(
        JNIEnv *env, jobject, jstring apk_file_path) {
    const char* path = env->GetStringUTFChars(apk_file_path, nullptr);
    uLong crc = calculateCrc32(path);
    env->ReleaseStringUTFChars(apk_file_path, path);
    return static_cast<jlong>(crc);
}

uLong calculateCrc32(const std::string& filePath) {
    std::ifstream file(filePath, std::ios::binary);
    if (!file.is_open()) {
        std::cerr << "Failed to open file: " << filePath << std::endl;
        return 0;
    }

    const size_t bufferSize = 1024; // 1KB buffer
    char buffer[bufferSize];
    uLong crc = crc32(0L, Z_NULL, 0); // Initialize CRC-32

    while (file.good()) {
        file.read(buffer, bufferSize);
        std::streamsize bytesRead = file.gcount();
        if (bytesRead > 0) {
            crc = crc32(crc, reinterpret_cast<const Bytef*>(buffer), bytesRead);
        }
    }

    file.close();
    return crc;
}