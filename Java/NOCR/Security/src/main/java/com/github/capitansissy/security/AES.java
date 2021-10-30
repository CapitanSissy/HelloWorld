package com.github.capitansissy.security;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES implements Serializable {
  private static SecretKeySpec secretKey;

  private static void setKey(@NotNull String myKey) {
    MessageDigest sha;
    try {
      byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
      sha = MessageDigest.getInstance(Constants.Crypto.MESSAGE_DIGEST_INSTANCE);
      key = sha.digest(key);
      key = Arrays.copyOf(key, Constants.Crypto.MESSAGE_DIGEST_LENGTH);
      secretKey = new SecretKeySpec(key, Constants.Crypto.SECRET_KEY_SPEC);
    } catch (NoSuchAlgorithmException e) {
//      logger.setLog(e.getMessage(), 3);
    }
  }

  @Nullable
  public static String encrypt(String strToEncrypt, String secret) {
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance(Constants.Crypto.CIPHER_INSTANCE);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
//      logger.setLog("Error while encrypting: " + e.toString(), 3);
      return null;
    }
  }

  @Nullable
  public static String decrypt(String strToDecrypt, String secret) {
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance(Constants.Crypto.CIPHER_INSTANCE);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
//      logger.setLog("Error while decrypting: " + e.toString(), 3);
      return null;
    }
  }

}
