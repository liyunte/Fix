package com.lyt.fix.library.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static void copyFile(File sourceFile, File targetFile) throws Exception{
            FileInputStream inputStream = new FileInputStream(sourceFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            FileOutputStream outputStream = new FileOutputStream(targetFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            byte[] b = new byte[1024*5];
            int len;
            while ((len = bufferedInputStream.read(b))!=-1){
                bufferedOutputStream.write(b,0,len);
            }
            bufferedOutputStream.flush();
            bufferedInputStream.close();
            bufferedOutputStream.close();
            outputStream.close();
            inputStream.close();
    }
}
