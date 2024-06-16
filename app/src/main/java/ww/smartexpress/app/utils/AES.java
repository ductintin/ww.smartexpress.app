package ww.smartexpress.app.utils;

import android.security.keystore.KeyProperties;
import android.security.keystore.KeyProtection;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AES {
    private SecretKey secretKey;
    private static final String KEY_ALIAS = "my_key_alias";

    public void init() throws Exception{

        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // Kiểm tra xem khóa đã tồn tại trong KeyStore hay chưa
        if (keyStore.containsAlias(KEY_ALIAS)) {
            // Lấy khóa từ KeyStore bằng alias
            KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            secretKey = keyEntry.getSecretKey();

        } else {
            // Không tìm thấy khóa trong KeyStore, tạo khóa mới
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES);
            keyGenerator.init(256);

            secretKey = keyGenerator.generateKey();

            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
            KeyProtection protectionParameter = new KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build();

            keyStore.setEntry(KEY_ALIAS, entry, protectionParameter);

        }

    }

    public String encrypt(String plainText) throws Exception{
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);

        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String cipherText = Base64.encodeToString(cipher.doFinal(plainText.getBytes()), android.util.Base64.DEFAULT);
            String iv = Base64.encodeToString(cipher.getIV(), android.util.Base64.DEFAULT);
            return cipherText + "." + iv;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String decrypt(String cipherText) throws Exception{
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
        String[] arr = cipherText.split("\\.");
        Log.d("TAG", "de: " + arr[0]);
        Log.d("TAG", "de: " + arr[1]);
        byte[] cipherData = Base64.decode(arr[0], android.util.Base64.DEFAULT);
        byte[] iv = Base64.decode(arr[1], android.util.Base64.DEFAULT);

        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            byte[] clearText = cipher.doFinal(cipherData);
            return new String(clearText, 0, clearText.length, StandardCharsets.UTF_8);

        }catch (Exception e){
            e.printStackTrace();

        }
        return "";
    }

//    public String encrypt(String message) throws Exception{
//        byte[] messageInBytes = message.getBytes();
//        encryptionCipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
//        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] encryptBytes = encryptionCipher.doFinal(messageInBytes);
//        return encode(encryptBytes);
//    }
//
//    public String decrypt(String encryptedMessage) throws Exception{
//        byte[] messageInBytes = decode(encryptedMessage);
//        decryptCipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
//        spec = new GCMParameterSpec(T_LEN, encryptionCipher.getIV());
//        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
//        byte[] decryptedBytes = decryptCipher.doFinal(messageInBytes);
//        return new String(decryptedBytes);
//    }
//
//    private String encode(byte[] data){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return Base64.getEncoder().encodeToString(data);
//        }
//        return null;
//    }
//
//    private byte[] decode(String data){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return Base64.getDecoder().decode(data);
//        }
//        return new byte[0];
//    }

}
