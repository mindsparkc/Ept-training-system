package com.sinosoft.license.tools;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密解密
 *
 *
 **/
public class RsaUtils
{

    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAm" +
            "AwggJcAgEAAoGBAMrpIK1YTAbVIm/1tPIZEd3KztRvucMUaj4O6vvxRH" +
            "2Ef/rZT0aqhOoslL6poOlexMUeIaEXkwPybhkd+yhlTZD5AEZGnYHK5Jce" +
            "3QzWNRLcILvcR6RnvWRt5rz2VCKimhCcJIKJAAnoWUHP03JdeGyXQRlv/S" +
            "Jii169gnlvYTq5AgMBAAECgYAsnkTUOUVGf1l6OxJV5hNarPz8E7FKZ1PR" +
            "X6BmVDBWVWt68owPIvHlOHZg0pYSmwczvSNx8+gdBxMUrNYMlPIQGWHdL" +
            "dADTxKrL+GTgTd1m3MK+8WYvlhcis0/gzOgaiZqdLFKV0nZ9+jADYvL2QD" +
            "Jj8nGvuGpNgJmF0wtHeI7AQJBAPT/YJdSoH2rasWvaBKkqoJ8I9rg3co4q" +
            "6viH0A5A7QwRhMpHxJK8nNNQMPPsj5EpOmGdA30UrWGI9txLwXOk1kCQQD" +
            "UBeWQ7hhwsugfwDWQTBbwPIFuMmpic1muUj109k1smG3gA2rX5FcBCKpDdZ" +
            "yvlBZmPWqpGg8C2KPbb86Bh9ZhAkBgDVdzPGPLoU8FHhXxaTmoWr876vlJdR" +
            "ppEwgph7DaDisLDA4wf826Qslne6BoJUV6gMozRly+6k5YlHFZF3pRAkBSPo" +
            "9UbueQH/YnCto8RSfIvM/APVJDKlepZUFts3VJIL/OW8UARhTx07ioPmEvQpS" +
            "FyGEtg2w7QGofXvmOCXFBAkEA6bZQSpk4r18VGq9GZeomkmaiKTnRShwd441H" +
            "GUvG0ZZjOqDAL0LsmRQwL9jk2RNMLg31GyjAv8SEccAnhW84LA==";

    //RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    //RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text 待解密的文本
     * @return 解密后的文本
     */
//    public static String decryptByPrivateKey(String text) throws Exception
//    {
//        return decryptByPrivateKey(privateKey, text);
//    }

    /**
     * 公钥解密
     *
     * @param publicKeyString 公钥
     * @param text 待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyString,String text) throws Exception
    {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param text 待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String text) throws Exception
    {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey( String text) throws Exception
    {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString 公钥
     * @param text 待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyString, String text) throws Exception
    {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }


    /**
     * 构建RSA密钥对
     *
     * @return 生成后的公私钥信息
     */
    public static RsaKeyPair generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        System.out.println(publicKeyString);
        System.out.println(privateKeyString);
        return new RsaKeyPair(publicKeyString, privateKeyString);
    }

    /**
     * RSA密钥对对象
     */
    public static class RsaKeyPair
    {
        private final String publicKey;
        private final String privateKey;

        public RsaKeyPair(String publicKey, String privateKey)
        {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey()
        {
            return publicKey;
        }

        public String getPrivateKey()
        {
            return privateKey;
        }
    }
}
