package com.security.files;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Crypto {

    public static void encrypt(SecretKey secretKey, File file) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        doCrypto(cipher,secretKey,file);
    }
    public static void decrypt(SecretKey secretKey, File file) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        doCrypto(cipher,secretKey,file);
    }

    private static void doCrypto(Cipher cipher,SecretKey secretKey, File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File("./Homepage.png"));


        byte[] b = new byte[(int) file.length()];
        fileInputStream.read(b);
        fileInputStream.close();

        byte[] op = cipher.doFinal(b);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(op);
        fos.close();
    }
}
