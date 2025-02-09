package com.github.eventmanager.compressors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip extends Compressors {
    public static void compress(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(setNewFileExtension(filePath, "gz"));
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            byte[] buffer = new byte[1024];
            int len;
            while((len=fileInputStream.read(buffer)) != -1){
                gzipOutputStream.write(buffer, 0, len);
            }
            //close resources
            gzipOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
