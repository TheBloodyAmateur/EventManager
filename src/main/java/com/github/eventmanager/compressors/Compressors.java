package com.github.eventmanager.compressors;

import java.util.regex.Pattern;

abstract class Compressors {
    static void compress(String filePath) {

    }

    static void decompress(String filePath) {

    }

    static String setNewFileExtension(String filePath, String compressionType) {
        return filePath.substring(0, filePath.lastIndexOf('.')) + "." + compressionType;
    }
}
