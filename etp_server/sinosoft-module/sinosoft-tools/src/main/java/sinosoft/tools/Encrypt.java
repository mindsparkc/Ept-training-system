package sinosoft.tools;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * 用以加密数据库密码
 *
 * @author wanglijie
 */
public class Encrypt {
    public static void main(String[] args) throws Exception {
        String password = "pajy@303";
        //String password="pajy@303";
        System.out.println("需要加密的字符串：【" + password + "】");
        String[] arr = ConfigTools.genKeyPair(512);
        System.out.println("privateKey:" + arr[0]);
        System.out.println("publicKey:" + arr[1]);
        String encStr = ConfigTools.encrypt(arr[0], password);
        System.out.println("password:" + encStr);
        System.out.println("尝试解密字符串：【" + ConfigTools.decrypt(arr[1], encStr) + "】");
    }
}
