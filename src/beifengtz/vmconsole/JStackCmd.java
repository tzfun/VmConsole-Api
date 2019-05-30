package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jstack.JStackResult;
import beifengtz.vmconsole.exception.AttachingException;
import beifengtz.vmconsole.tools.jstack.JStackTool;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import sun.tools.attach.HotSpotVirtualMachine;

import java.io.InputStream;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.VmConsole-Api</p>
 * Created in 19:50 2019/5/28
 *
 * <p>jstack命令介绍：</p>
 *
 * <p>
 * jstack用于打印出给定的java进程ID或core file或远程调试服务的Java
 * 堆栈信息，如果是在64位机器上，需要指定选项"-J-d64"，Windows的jstack
 * 使用方式只支持以下的这种方式：<code>jstack [-l]pid</code></p>
 *
 * <p>
 * 如果java程序崩溃生成core文件，jstack工具可以用来获得core文件的java stack
 * 和native stack的信息，从而可以轻松地知道java程序是如何崩溃和在程序何
 * 处发生问题。另外，jstack工具还可以附属到正在运行的java程序中，看到当时
 * 运行的java程序的java stack和native stack的信息, 如果现在运行的java
 * 程序呈现hung的状态，jstack是非常有用的。</p>
 */
public class JStackCmd {
    /**
     * <p>获取线程堆栈详情，封装<code>jstack -F [vmid]</code>命令</p>
     * @param vmId 虚拟机唯一识别Id
     * @return JStackResult
     * @throws IllegalArgumentException 非法参数异常
     * @throws Exception 异常
     */
    public static JStackResult threadStack(int vmId)throws IllegalArgumentException,Exception{
        return run(new String[]{"-F",String.valueOf(vmId)});
    }

    /**
     * <p>如果调用到本地方法的话，可以获取C/C++的堆栈，封装<code>jstack -m [vmid]</code>命令</p>
     * @param vmId 虚拟机唯一识别Id
     * @return JStackResult
     * @throws IllegalArgumentException 非法参数异常
     * @throws Exception 异常
     */
    public static JStackResult jniStack(int vmId)throws IllegalArgumentException,Exception{
        return run(new String[]{"-m",String.valueOf(vmId)});
    }

    /**
     * <p>除堆栈外，获取关于锁的附加信息，封装<code>jstack -l [vmid]</code>命令</p>
     * @param vmId 虚拟机唯一识别Id
     * @return JStackResult
     * @throws IllegalArgumentException 非法参数异常
     * @throws Exception 异常
     */
    public static JStackResult threadDump(int vmId)throws IllegalArgumentException,Exception{
        return run(new String[]{"-l",String.valueOf(vmId)});
    }

    /**
     * jstack命令核心执行函数，所有命令都可以在此执行
     *
     * @param var0 命令参数
     * @return JStackResult
     * @throws IllegalArgumentException 非法参数异常
     * @throws Exception 异常
     */
    public static JStackResult run(String[] var0) throws IllegalArgumentException,Exception {
        if (var0.length == 0) {
            usage();
            throw new IllegalArgumentException("Parameter can not be empty.");
        }

        boolean var1 = false;
        boolean var2 = false;
        boolean var3 = false;

        int var4;
        for (var4 = 0; var4 < var0.length; ++var4) {
            String var5 = var0[var4];
            if (!var5.startsWith("-")) {
                break;
            }

            if (!var5.equals("-help") && !var5.equals("-h")) {
                if (var5.equals("-F")) {    //  输出线程堆栈
                    var1 = true;
                } else if (var5.equals("-m")) { //  如果调用到本地方法的话，可以显示C/C++的堆栈
                    var2 = true;
                } else if (var5.equals("-l")) { //  除堆栈外，显示关于锁的附加信息
                    var3 = true;
                } else {
                    //  参数错误
                    usage();
                    throw new IllegalArgumentException("The parameter is invalid.");
                }
            } else {
                //  帮助命令，显示帮助方法
                usage();
            }
        }

        if (var2) {
            var1 = true;
        }

        //  检测有没有传入vmId
        int var8 = var0.length - var4;
        if (var8 == 0 || var8 > 2) {
            usage();
            throw new IllegalArgumentException("The parameter is invalid.");
        }

        if (var8 == 2) {    //  远程连接参数为2
            var1 = true;
        } else if (!var0[var4].matches("[0-9]+")) { //  本地连接，检测vmId必须由数字组成
            var1 = true;
        }

        if (var1) {
            String[] var6 = new String[var8];

            for (int var7 = var4; var7 < var0.length; ++var7) {
                var6[var7 - var4] = var0[var7];
            }

            return runJStackTool(var2, var3, var6);
        } else {
            String var9 = var0[var4];
            String[] var10;
            if (var3) {
                var10 = new String[]{"-l"};
            } else {
                var10 = new String[0];
            }

            return runThreadDump(var9, var10);
        }
    }

    private static JStackResult runJStackTool(boolean var0, boolean var1, String[] var2) throws Exception {

        if (var0) {
            //  显示Java和C/C++的堆栈
            var2 = prepend("-m", var2);

        }

        if (var1) {
            //  除堆栈外，显示关于锁的附加信息
            var2 = prepend("-l", var2);
        }

        JStackResult jStackResult = new JStackResult();
        JStackTool.init(var2,jStackResult);
        return jStackResult;
    }

    private static Class<?> loadSAClass() {
        try {
            return Class.forName("sun.jvm.hotspot.tools.JStack", true, ClassLoader.getSystemClassLoader());
        } catch (Exception var1) {
            return null;
        }
    }

    private static JStackResult runThreadDump(String var0, String[] var1) throws Exception {
        VirtualMachine var2 = null;

        try {
            var2 = VirtualMachine.attach(var0);
        } catch (Exception var7) {
            String var4 = var7.getMessage();
            if (var4 != null) {
                throw new AttachingException(var0 + ": " + var4);
            } else {
                if (var7 instanceof AttachNotSupportedException) {
                    throw new AttachNotSupportedException("The -F option can be used when the target process is not responding");
                }
                throw var7;
            }
        }

        //  输入流接收快照
        InputStream var3 = ((HotSpotVirtualMachine)var2).remoteDataDump((Object[])var1);
        byte[] var8 = new byte[256];
        StringBuilder threadDump = new StringBuilder();

        int var5;
        do {
            var5 = var3.read(var8);
            if (var5 > 0) {
                String var6 = new String(var8, 0, var5, "UTF-8");
                threadDump.append(var6);
            }
        } while(var5 > 0);
        JStackResult jStackResult = new JStackResult();
        jStackResult.setThreadDump(threadDump.toString());
        threadDump = null;
        var8 = null;
        var3.close();
        var2.detach();
        return jStackResult;
    }

    /**
     * 命令拼接
     * 将var0拼接到var1中
     *
     * @param var0 添加的字符串
     * @param var1 字符串数组
     * @return 新的字符串数组
     */
    private static String[] prepend(String var0, String[] var1) {
        String[] var2 = new String[var1.length + 1];
        var2[0] = var0;
        System.arraycopy(var1, 0, var2, 1, var1.length);
        return var2;
    }

    private static void usage() {
        System.err.println("Usage:");
        System.err.println("    jstack [-l] <pid>");
        System.err.println("        (to connect to running process)");
        if (loadSAClass() != null) {
            System.err.println("    jstack -F [-m] [-l] <pid>");
            System.err.println("        (to connect to a hung process)");
            System.err.println("    jstack [-m] [-l] <executable> <core>");
            System.err.println("        (to connect to a core file)");
            System.err.println("    jstack [-m] [-l] [server_id@]<remote server IP or hostname>");
            System.err.println("        (to connect to a remote debug server)");
        }

        System.err.println("");
        System.err.println("Options:");
        if (loadSAClass() != null) {
            System.err.println("    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)");
            System.err.println("    -m  to print both java and native frames (mixed mode)");
        }

        System.err.println("    -l  long listing. Prints additional information about locks");
        System.err.println("    -h or -help to print this help message");
    }
}
