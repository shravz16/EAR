import com.security.files.Crypto;
import com.security.files.KeyManager;
import com.security.files.KeyRotation;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        KeyManager.getInstance();

        Thread.sleep(1000L);
        Scanner s=new Scanner(System.in);
        System.out.println("Enter password for salt");
        String password = s.next();
        SecretKey secretKey = (SecretKey) KeyManager.getKey();

        byte[] keys = KeyRotation.rotateKey(secretKey);
        secretKey = (SecretKey) KeyManager.getKeyWithSalt(keys,password);
        if(args[0].equals("encrypt")){
            Crypto.encrypt(secretKey,new File(args[1]));
        }
        else if(args[0].equals("decrypt")){
            Crypto.decrypt(secretKey,new File(args[1]));
        }
        System.out.println("Completed!");
    }
}