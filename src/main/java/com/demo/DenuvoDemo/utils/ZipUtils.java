package com.demo.DenuvoDemo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.core.io.InputStreamResource;

/**
 *
 * @author Andrii Filimonov
 */
public class ZipUtils {
    
    public static InputStreamResource createZipFromString(String input) throws IOException {

        File tempFile = File.createTempFile("temp", ".json");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(input.getBytes());
        }
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            ZipEntry zipEntry = new ZipEntry(tempFile.getName());
            zipOutputStream.putNextEntry(zipEntry);
            
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }
            zipOutputStream.closeEntry();
        }
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        
        return new InputStreamResource(byteArrayInputStream);        
    }
    
}
