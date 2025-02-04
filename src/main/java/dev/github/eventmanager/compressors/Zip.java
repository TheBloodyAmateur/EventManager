package dev.github.eventmanager.compressors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip extends Compressors {
    public static void compress(String filePath) {
        // TODO Compress the file using Zip
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + ".zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            byte[] buffer = new byte[1024];
            int len;
            ZipEntry zipEntry = new ZipEntry(filePath);
            zipOutputStream.putNextEntry(zipEntry);

            while((len=fileInputStream.read(buffer)) != -1){
                zipOutputStream.write(buffer, 0, len);
            }

            //close resources
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
