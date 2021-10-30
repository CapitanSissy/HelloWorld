package com.github.capitansissy.security;

import java.io.Serializable;

public class Constants implements Serializable {
  public static class Crypto {
    public static final String MESSAGE_DIGEST_INSTANCE = "SHA-1";
    public static final int MESSAGE_DIGEST_LENGTH = 16;
    public static final String SECRET_KEY_SPEC = "AES";
    public static final String CIPHER_INSTANCE = "AES/ECB/PKCS5PADDING";
    public static final int BUFFER_SIZE = 1024;
  }
}
