package beifengtz.vmconsole;

import beifengtz.vmconsole.tools.jstack.PStackTool;
import beifengtz.vmconsole.tools.jstack.StackTraceTool;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.VmConsole-Api</p>
 * Created in 19:50 2019/5/28
 * <p>
 * jstack命令介绍：
 * <p>
 * jstack用于打印出给定的java进程ID或core file或远程调试服务的Java
 * 堆栈信息，如果是在64位机器上，需要指定选项"-J-d64"，Windows的jstack
 * 使用方式只支持以下的这种方式：
 * <p>
 * jstack [-l] pid
 * <p>
 * 如果java程序崩溃生成core文件，jstack工具可以用来获得core文件的java stack
 * 和native stack的信息，从而可以轻松地知道java程序是如何崩溃和在程序何
 * 处发生问题。另外，jstack工具还可以附属到正在运行的java程序中，看到当时
 * 运行的java程序的java stack和native stack的信息, 如果现在运行的java
 * 程序呈现hung的状态，jstack是非常有用的。
 */
public class JStackCmd {
    public static void main(String[] args) throws Exception {
        run(new String[]{"-F", "7160"});
    }

    public static void run(String[] var0) throws IllegalArgumentException, Exception {
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

            runJStackTool(var2, var3, var6);
        } else {
            //  生成dump文件暂不支持
            throw new Exception("This command parameter is not supported at this time.");
        }
    }

    private static void runJStackTool(boolean var0, boolean var1, String[] var2) throws Exception {
        boolean mixedMode = false;
        boolean concurrentLocks = false;

        //  反射获取JStack类
        Class var3 = loadSAToolClass();
        if (var3 == null) {
            usage();
        }

        if (var0) {
            //  显示Java和C/C++的堆栈
            var2 = prepend("-m", var2);
            mixedMode = true;

        }

        if (var1) {
            //  除堆栈外，显示关于锁的附加信息
            var2 = prepend("-l", var2);
            concurrentLocks = true;
        }

        //  获取HotSpot内部Tool对象
        //  内存流（针对程序是输出，针对内存是输入）
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);

        //  使用打印流，将结果打印至内存
        PrintStream ps = new PrintStream(outputStream);


        Class[] var4 = new Class[]{String[].class, PrintStream.class};
        //  获取init方法
        Method var5 = var3.getDeclaredMethod("init", var4);
        //  反射的方式调用函数
        var5.invoke((Object) null, var2, ps);

        System.out.println("------------end-------------");

        System.out.println(outputStream);

        ps.close();
        outputStream.close();
    }

    private static Class<?> loadSAToolClass() {
        try {
            return Class.forName("beifengtz.vmconsole.tools.jstack.JStackTool", true, ClassLoader.getSystemClassLoader());
        } catch (Exception var1) {
            return null;
        }
    }

    private static Class<?> loadSAClass() {
        try {
            return Class.forName("sun.jvm.hotspot.tools.JStack", true, ClassLoader.getSystemClassLoader());
        } catch (Exception var1) {
            return null;
        }
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
