package com.github.eventmanager.compressors;

/**
 * The Compressors class provides abstract methods for compressing and decompressing files.
 * It also includes a utility method for setting a new file extension based on the compression type.
 */
abstract class Compressors {

    /**
     * Compresses the file at the specified file path.
     *
     * @param filePath the path to the file to be compressed.
     */
    static void compress(String filePath) {
        // Implementation for compressing the file
    }

    /**
     * Decompresses the file at the specified file path.
     *
     * @param filePath the path to the file to be decompressed.
     */
    static void decompress(String filePath) {
        // Implementation for decompressing the file
    }

    /**
     * Sets a new file extension based on the compression type.
     *
     * @param filePath the original file path.
     * @param compressionType the type of compression (e.g., "gzip", "zip").
     * @return the new file path with the updated extension.
     */
    static String setNewFileExtension(String filePath, String compressionType) {
        return filePath.substring(0, filePath.lastIndexOf('.')) + "." + compressionType;
    }
}