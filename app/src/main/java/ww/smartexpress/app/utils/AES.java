package ww.smartexpress.app.utils;

import android.os.Build;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AES {
    private SecretKey secretKey;
    private int KEY_SIZE = 128;
    private int T_LEN = 128;
    private Cipher encryptionCipher;

    public void init() throws Exception{
        KeyGenerator keyGenerator  = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        secretKey = keyGenerator.generateKey();
    }

    public String encrypt(String message) throws Exception{
        byte[] messageInBytes = message.getBytes();
        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception{
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, encryptionCipher.getIV());
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] decryptedBytes = decryptCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    private String encode(byte[] data){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(data);
        }
        return null;
    }

    private byte[] decode(String data){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getDecoder().decode(data);
        }
        return new byte[0];
    }

//    public static void main(){
//        try {
//            AES aes = new AES();
//            aes.init();
//            String
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
