package com.security.files;

import javax.crypto.SecretKey;

public class KeyRotation {

    public static byte[] rotateKey(SecretKey secretKey){
        byte[] keys=secretKey.getEncoded();
        System.out.println("Before rotation - "+new String(keys));
        byte last=keys[0];
        for(int i=0;i<keys.length-1;i++){
            keys[i]=keys[i+1];
        }
        keys[keys.length-1]=last;
        System.out.println("After rotation - " + new String(keys));
        return keys;
    }
}
