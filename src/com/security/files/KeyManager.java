package com.security.files;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

public class KeyManager {

    private static KeyManager keyManager=new KeyManager();
    private static Scanner s;
    private static KeyStore ks;
    private KeyManager(){
        init();
    }

    public static KeyManager getInstance(){
        return keyManager;
    }

    private void init() {
        s=new Scanner(System.in);

        try {
            ks = KeyStore.getInstance("pkcs12");
            File keyFile=new File("./keyStore.jks");
            if(keyFile.exists()){
                return;
            }

            System.out.println("====================== Creating Keystore ========================");
            System.out.println("====================== Enter the keystore password ===================");
            String passwrd=s.next();

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            ks.load(null, passwrd.toCharArray());

            SecretKey originalKey = keyGenerator.generateKey();
            KeyStore.SecretKeyEntry key= new KeyStore.SecretKeyEntry(originalKey);
            KeyStore.ProtectionParameter password
                    = new KeyStore.PasswordProtection(passwrd.toCharArray());

            ks.setEntry("main-key",key,password);
            try (FileOutputStream fos = new FileOutputStream("keyStore.jks")) {
                ks.store(fos, passwrd.toCharArray());
            }
            File f=new File("keyStore.jks");
            Files.setPosixFilePermissions(f.toPath(), PosixFilePermissions.fromString("r--r--r--"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Key getKey(){
        Key key=null;
        try {
            System.out.println("====================== Enter the keystore password ===================");
            String passwrd=s.next();
            ks.load(new FileInputStream("keyStore.jks"), passwrd.toCharArray());
             key = ks.getKey("main-key", passwrd.toCharArray());
             if(key==null){
                 System.out.println("======== either key doesnt exist or password wrong ========");
             }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    return key;
    }

    public static Key getKeyWithSalt(byte[] keys,String salt){
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(Base64.getEncoder().encodeToString(keys).toCharArray(), salt.getBytes(), 65536, 256);
            Key secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return secretKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }

}
