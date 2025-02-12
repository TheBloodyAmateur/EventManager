package com.github.eventmanager.compressors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The Gzip class provides methods for compressing files using the GZIP format.
 */
public class Gzip extends Compressors {

    /**
     * Compresses the file at the specified file path using the GZIP format.
     *
     * @param filePath the path to the file to be compressed.
     */
    public static void compress(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(setNewFileExtension(filePath, "gz"));
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                gzipOutputStream.write(buffer, 0, len);
            }
            // Close resources
            gzipOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}