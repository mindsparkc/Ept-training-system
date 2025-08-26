package sinosoft.tools;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * RSA加密解密
 *
 *
 **/
public class RsaUtils
{

    public static String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJ3M3tGgYCFkZI1kCt" +
            "y+ytw2CA0uTwGjIe4tIAd3gHYNssw2JgOC0qQxyNnbSQd/yLQVemg/DRH2iI3xx0M+gtH2OdVIErQQb0IBWzP6OoR20aWXPaX" +
            "FENj5JUp+B40lYjWwdmwomtfX1lAt7HRafZQC+hQ0Fz40l1HiDgFgawQjAgMBAAECgYEAkIiEkia9xfgJLYQzesr6Tcbjiwcec" +
            "FZ2DoygaJZpQDf9y5PRL6gUvTWoTPrQIiFFuMRvcKpHk2tm2fRZWdsYwDyehIcclC7N9xU01JacmtjsAb+L6gRMtBK1CpeE" +
            "y7Fq0wUCnyW7xYwzpv124Ji8KeHs2DCuDANIfkKAoZIruVkCQQDSa06IjQNV4ZEyeGhQbGss2OEAjMBuDxaF4q1x1CFjh" +
            "0exIr1bneAoLBSN1SN4ooKLDGdZsyjsd5qRZP98GLAfAkEAv/ubvryv8T+MVL5xmB5Ln58NRTblZg7wuJMdEq+WkN0kJ" +
            "t8EBYwuSbGqYV64aSBC2X+cMECJq2extPr1Xf5bfQJAJdaIC8QXynTCkGnYkEW+zBS/+UJWRDRQX4kQmXyE+InWGY" +
            "Neu7/bFD5A//RI6aOHiAuV9ipifItRbz3TN3SVDwJBAJ/5NzRe4nUysOrErr9FL1rZHCO6tLiwZmJHJAi4C8y2mTN" +
            "SznUY9FEwHHt6hEwFN+8o6KtEe/M6OthF7kPeebkCQQCgjG" +
            "ECwQ+Py5jTZRCcjnhZ8TEFNzoFkW0ZgUwmm/B29e9kDStSfzp/ZtxhQ76Z92aTgNPYqeXWM26PPw5V7KTS";


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




}
