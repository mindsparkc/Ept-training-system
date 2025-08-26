package com.sinosoft.license.tools;

import java.io.*;

public class CreateAuthCode {


    public static void main(String[] args) throws Exception {
        String[] authCode = getUserAuthCode();
        String newCode = getNewAuthCode(authCode[0],authCode[1]);
        createAuthFile(newCode);
    }
    //获取授权码
    public static String[] getUserAuthCode(){
        String[] info = new String[2];
        try {
            String path = System.getProperty("user.dir");
            System.out.println(path);
            File file = new File(path,"userAuth.txt");


            if (file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                int i = 0;
                while ((lineTxt = br.readLine()) != null) {
                    info[i] = lineTxt;
                    i++;
                }
                br.close();
            } else {
                System.out.println("文件不存在!");
            }

        } catch (Exception e) {
            System.out.println("文件读取错误!");

        }
        return info;

    }
    /**
     * 拼接过期时间，加密
     * @param authCode
     * @return
     */
    public static String getNewAuthCode(String authCode,String date){
        //解密,截取用户传过来的公钥
        String[] codeArr = authCode.split("#");

        String code = "";
        try {
            //公钥解密
            String info = RsaUtils.decryptByPublicKey(codeArr[0],codeArr[1]);
            // 拼接过期时间
            String new_info = info+","+ date;
            // 重新 私钥加密
            System.out.println(new_info);
            code = RsaUtils.encryptByPrivateKey(new_info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return code;
    }

    /**
     * 生成授权码文件
     */
    public static void createAuthFile(String authCode){

        String filePath = "authCode.txt";
        String content = authCode;
        FileWriter fw = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(filePath);
            fw.write(content);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fw.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
