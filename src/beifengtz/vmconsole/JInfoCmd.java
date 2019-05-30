package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jinfo.JInfoResult;
import beifengtz.vmconsole.tools.jinfo.JInfoTool;
import com.sun.tools.attach.VirtualMachine;
import sun.tools.attach.HotSpotVirtualMachine;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.VmConsole-Api</p>
 * Created in 16:41 2019/5/30
 *
 * <p>jinfo可以输出并修改运行时的java 进程的options。</p>
 */
public class JInfoCmd {

    public static JInfoResult run(String[] var0) throws Exception{
        if (var0.length == 0) {
            throw new IllegalArgumentException("Parameter can not be empty.");
        }

        boolean var1 = true;
        String var2 = var0[0];
        byte var3;
        if (var2.startsWith("-")) {
            if (!var2.equals("-flags") && !var2.equals("-sysprops")) {
                if (var2.equals("-flag")) {
                    var1 = false;
                } else {
                    throw new IllegalArgumentException();
                }
            } else if (var0.length != 2 && var0.length != 3) {
                throw new IllegalArgumentException();
            }
        }

        if (var1) {
            return runTool(var0);
        } else if (var0.length == 3) {
            String var5 = var0[2];
            String var4 = var0[1];
            return flag(var5, var4);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 调用JInfo查询工具
     * @param var0 命令参数
     * @return {@link JInfoResult}对象
     * @throws Exception 异常
     */
    private static JInfoResult runTool(String[] var0) throws Exception {
        JInfoResult jInfoResult = new JInfoResult();
        JInfoTool.init(var0,jInfoResult);
        jInfoResult.setCommandType(JInfoResult.COMMANDTYPE_QUERY);
        return jInfoResult;
    }

    /**
     * 反射加载类
     *
     * @param var0 类全路径
     * @return Class对象
     */
    private static Class<?> loadClass(String var0) {
        try {
            return Class.forName(var0, true, ClassLoader.getSystemClassLoader());
        } catch (Exception var2) {
            return null;
        }
    }

    private static JInfoResult flag(String var0, String var1) throws IOException {
        VirtualMachine var2 = attach(var0);
        int var5 = var1.indexOf(61);
        String var3;
        InputStream var4;
        if (var5 != -1) {
            var3 = var1.substring(0, var5);
            String var6 = var1.substring(var5 + 1);
            var4 = ((HotSpotVirtualMachine)var2).setFlag(var3, var6);
        } else {
            char var7 = var1.charAt(0);
            switch(var7) {
                case '+':
                    var3 = var1.substring(1);
                    var4 = ((HotSpotVirtualMachine)var2).setFlag(var3, "1");
                    break;
                case '-':
                    var3 = var1.substring(1);
                    var4 = ((HotSpotVirtualMachine)var2).setFlag(var3, "0");
                    break;
                default:
                    var4 = ((HotSpotVirtualMachine)var2).printFlag(var1);
            }
        }

        drain(var2, var4);
        return null;
    }

    private static VirtualMachine attach(String var0) {
        try {
            return VirtualMachine.attach(var0);
        } catch (Exception var3) {
            String var2 = var3.getMessage();
            if (var2 != null) {
                System.err.println(var0 + ": " + var2);
            } else {
                var3.printStackTrace();
            }

            System.exit(1);
            return null;
        }
    }

    private static void drain(VirtualMachine var0, InputStream var1) throws IOException {
        byte[] var2 = new byte[256];

        int var3;
        do {
            var3 = var1.read(var2);
            if (var3 > 0) {
                String var4 = new String(var2, 0, var3, "UTF-8");
                System.out.print(var4);
            }
        } while(var3 > 0);

        var1.close();
        var0.detach();
    }
}
