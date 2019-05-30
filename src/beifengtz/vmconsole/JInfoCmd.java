package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jinfo.JInfoFlag;
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
 * <p>虚拟机参数查询以及设置工具类，该类中提供直接执行命令的方法，但是建议您使用已经封装好的方法。</p>
 *
 * <p>jinfo可以输出并修改运行时的java进程的options。</p>
 *
 * <p>命令调用方法：</p>
 *
 * <p>    -flag <name>         to print the value of the named VM flag</p>
 * <p>    -flag [+|-]<name>    to enable or disable the named VM flag</p>
 * <p>    -flag <name>=<value> to set the named VM flag to the given value</p>
 * <p>    -flags               to print VM flags</p>
 * <p>    -sysprops            to print Java system properties</p>
 * <p>    <no option>          to print both of the above</p>
 */
public class JInfoCmd {

    public static JInfoResult queryFlagsAndSysInfo(int vmId)throws Exception{
        return run(new String[]{String.valueOf(vmId)});
    }

    public static JInfoResult queryFlags(int vmId)throws Exception{
        return run(new String[]{"-flags",String.valueOf(vmId)});
    }

    public static JInfoResult querySysInfo(int vmId)throws Exception{
        return run(new String[]{"-sysprops",String.valueOf(vmId)});
    }

    public static JInfoResult queryFlag(int vmId,String flagName)throws Exception{
        return run(new String[]{"-flag",flagName,String.valueOf(vmId)});
    }

    public static JInfoResult addFlag(int vmId,String flagName)throws Exception{
        return run(new String[]{"-flag","+"+flagName,String.valueOf(vmId)});
    }

    public static JInfoResult removeFlag(int vmId,String flagName)throws Exception{
        return run(new String[]{"-flag","-"+flagName,String.valueOf(vmId)});
    }

    public static JInfoResult setFlag(int vmId,String flagName,String value)throws Exception{
        return run(new String[]{"-flag",flagName+"="+value,String.valueOf(vmId)});
    }

    /**
     * 命令执行核心函数，可直接执行命令
     * @param var0 命令参数
     * @return {@link JInfoResult}对象用于存储结果
     * @throws Exception 异常
     */
    public static JInfoResult run(String[] var0) throws Exception{
        if (var0.length == 0) {
            throw new IllegalArgumentException("Parameter can not be empty.");
        }

        boolean var1 = true;
        String var2 = var0[0];
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

    /**
     * -flag命令处理函数，分为设置和查询
     *
     * @param var0 参数0
     * @param var1 参数1
     * @return {@link JInfoResult}对象
     */
    private static JInfoResult flag(String var0, String var1) {
        VirtualMachine var2 = attach(var0);
        int var5 = var1.indexOf(61);
        String var3;
        InputStream var4;
        JInfoResult jInfoResult = new JInfoResult();
        try{
            if (var5 != -1) {
                //  修改参数的值
                jInfoResult.setCommandType(JInfoResult.COMMANDTYPE_SET);
                var3 = var1.substring(0, var5);
                String var6 = var1.substring(var5 + 1);
                var4 = ((HotSpotVirtualMachine)var2).setFlag(var3, var6);
            } else {
                char var7 = var1.charAt(0);
                switch(var7) {
                    case '+':
                        //  新增一个参数
                        jInfoResult.setCommandType(JInfoResult.COMMANDTYPE_SET);
                        var3 = var1.substring(1);
                        var4 = ((HotSpotVirtualMachine)var2).setFlag(var3, "1");
                        break;
                    case '-':
                        //  减少一个参数
                        jInfoResult.setCommandType(JInfoResult.COMMANDTYPE_SET);
                        var3 = var1.substring(1);
                        var4 = ((HotSpotVirtualMachine)var2).setFlag(var3, "0");
                        break;
                    default:
                        //  查询一个参数
                        jInfoResult.setCommandType(JInfoResult.COMMANDTYPE_QUERY);
                        var4 = ((HotSpotVirtualMachine)var2).printFlag(var1);
                }
            }
            drain(var2, var4,jInfoResult);
            jInfoResult.setSetSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            jInfoResult.setSetSuccess(false);
        }
        return jInfoResult;
    }

    /**
     * 虚拟机连接函数
     *
     * @param var0 虚拟机vmId
     * @return {@link VirtualMachine}实例
     */
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

    /**
     * 处理结果函数，从输入流中读取数据，并存入对象
     * @param var0 虚拟机实例
     * @param var1 输入流
     * @param jInfoResult {@link JInfoResult}对象
     * @throws IOException IO异常
     */
    private static void drain(VirtualMachine var0, InputStream var1,JInfoResult jInfoResult) throws IOException {
        byte[] var2 = new byte[256];

        int var3;
        StringBuilder res = new StringBuilder();
        do {
            var3 = var1.read(var2);
            if (var3 > 0) {
                String var4 = new String(var2, 0, var3, "UTF-8");
                res.append(var4);
            }
        } while(var3 > 0);
        String[] content = res.toString().trim().split("\\=");
        if (content.length >= 2){
            jInfoResult.getFlags().add(new JInfoFlag(content[0],content[1]));
        }
        var1.close();
        var0.detach();
    }
}
