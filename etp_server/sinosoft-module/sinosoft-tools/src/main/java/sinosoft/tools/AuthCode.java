package sinosoft.tools;

import java.io.*;
import java.net.InetAddress;

/**
 * 生成授权码
 */
public class AuthCode {


    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdzN7RoGAhZGSNZArcvsrcNggNLk8BoyHuLSAHd4B2DbLMNiYDgtKkMcjZ20kHf8i0FXpoPw0R9oiN8cdDPoLR9jnVSBK0EG9CAVsz+jqEdtGllz2lxRDY+SVKfgeNJWI1sHZsKJrX19ZQLex0Wn2UAvoUNBc+NJdR4g4BYGsEIwIDAQAB";

    public static void main(String[] args) throws Exception {
        //获得当前操作系统的名称
        String name=System.getProperty("os.name");
        //物理地址
        InetAddress inetAddr = InetAddress.getLocalHost();
        String ip = inetAddr.getHostAddress();
        //CPU序号
        String cpu = "";
        if ("LINUX".equals(name.toUpperCase())) {
//            System.out.println("=============>for linux");
            cpu = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":");
            System.out.println("cpuid : " + cpu);

        } else {
//            System.out.println("=============>for windows");
            cpu = getCPUSerial();
        }
        String text = name + "," + ip + "," + cpu;
        String text_encry = RsaUtils.encryptByPrivateKey(text);
        System.out.println("机器码："+publicKey+"#"+text_encry);

    }



    /**
     * 获取CPU序列号
     *
     * @return
     */
    public static String getCPUSerial() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + " (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n"
                    + " Wscript.Echo objItem.ProcessorId \n"
                    + " exit for ' do the first cpu only! \n" + "Next \n";
            // + " exit for \r\n" + "Next";
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }
        return result.trim();
    }

    /**
     * @param cmd    命令语句
     * @param record 要查看的字段
     * @param symbol 分隔符
     * @return
     */

    public static String getSerialNumber(String cmd, String record, String symbol) {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split("\n");
        for (String info : infos) {
            info = info.trim();
            if (info.indexOf(record) != -1) {
                info.replace(" ", "");
                String[] sn = info.split(symbol);
                return sn[1];
            }
        }
        return null;
    }
    /***************************linux*********************************/
    public static String executeLinuxCmd(String cmd) {
        try {
//            System.out.println("got cmd job : " + cmd);
            Runtime run = Runtime.getRuntime();
            Process process;
            process = run.exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[8192];
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
            in.close();
            process.destroy();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
