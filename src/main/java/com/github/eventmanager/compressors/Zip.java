package com.github.eventmanager.compressors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The Zip class provides methods for compressing files using the ZIP format.
 */
public class Zip extends Compressors {

    /**
     * Compresses the file at the specified file path using the ZIP format.
     *
     * @param filePath the path to the file to be compressed.
     */
    public static void compress(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(setNewFileExtension(filePath, "zip"));
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            zipOutputStream.putNextEntry(new ZipEntry(new File(filePath).getName()));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, len);
            }
            // Close resources
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}