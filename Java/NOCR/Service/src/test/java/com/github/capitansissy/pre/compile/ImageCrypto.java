package com.github.capitansissy.pre.compile;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.security.Constants;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

class ImageCrypto implements Serializable {
  static void encryptImage(@NotNull FileInputStream originalImage, FileOutputStream encryptedImage) throws Exception {
    byte[] secretKey = Defaults.PUBLIC_DATABASE_KEY.getBytes(StandardCharsets.UTF_8);
    /*=-=-=-=-=-[]-=-=-=-=-=*/
    MessageDigest sha = MessageDigest.getInstance(Constants.Crypto.MESSAGE_DIGEST_INSTANCE);
    secretKey = sha.digest(secretKey);
    secretKey = Arrays.copyOf(secretKey, Constants.Crypto.MESSAGE_DIGEST_LENGTH);
    /*=-=-=-=-=-[]-=-=-=-=-=*/
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, Constants.Crypto.SECRET_KEY_SPEC);
    Cipher cipher = Cipher.getInstance(Constants.Crypto.CIPHER_INSTANCE);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    CipherOutputStream cipherOutputStream = new CipherOutputStream(encryptedImage, cipher);
    byte[] bytes = new byte[Constants.Crypto.BUFFER_SIZE];
    int reader;
    while ((reader = originalImage.read(bytes)) != -1) {
      cipherOutputStream.write(bytes, 0, reader);
    }
    originalImage.close();
    encryptedImage.flush();
    cipherOutputStream.close();
  }

  static void decryptImage(@NotNull FileInputStream encryptedImage, FileOutputStream normalImage) throws Exception {
    byte[] secretKey = Defaults.PUBLIC_DATABASE_KEY.getBytes(StandardCharsets.UTF_8);
    /*=-=-=-=-=-[]-=-=-=-=-=*/
    MessageDigest sha = MessageDigest.getInstance(Constants.Crypto.MESSAGE_DIGEST_INSTANCE);
    secretKey = sha.digest(secretKey);
    secretKey = Arrays.copyOf(secretKey, Constants.Crypto.MESSAGE_DIGEST_LENGTH);
    /*=-=-=-=-=-[]-=-=-=-=-=*/
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, Constants.Crypto.SECRET_KEY_SPEC);
    Cipher cipher = Cipher.getInstance(Constants.Crypto.CIPHER_INSTANCE);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    CipherOutputStream cipherOutputStream = new CipherOutputStream(normalImage, cipher);
    byte[] bytes = new byte[Constants.Crypto.BUFFER_SIZE];
    int reader;
    while ((reader = encryptedImage.read(bytes)) != -1) {
      cipherOutputStream.write(bytes, 0, reader);
    }
    encryptedImage.close();
    normalImage.flush();
    cipherOutputStream.close();
  }

}
