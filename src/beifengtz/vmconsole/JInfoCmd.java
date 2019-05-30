package beifengtz.vmconsole;

import com.sun.tools.attach.VirtualMachine;
import sun.tools.attach.HotSpotVirtualMachine;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.VmConsole-Api</p>
 * Created in 16:41 2019/5/30
 *
 * <p>jinfo可以输出并修改运行时的java 进程的options。</p>
 */
public class JInfoCmd {

    public static void run(String[] var0) throws Exception{
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
            runTool(var0);
        } else if (var0.length == 3) {
            String var5 = var0[2];
            String var4 = var0[1];
            flag(var5, var4);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static void runTool(String[] var0) throws Exception {
        String var1 = "sun.JvmResult.hotspot.tools.JInfo";
        Class var2 = loadClass(var1);
        if (var2 == null) {
            usage(1);
        }

        Class[] var3 = new Class[]{String[].class};
        Method var4 = var2.getDeclaredMethod("main", var3);
        Object[] var5 = new Object[]{var0};
        var4.invoke((Object)null, var5);
    }

    private static Class<?> loadClass(String var0) {
        try {
            return Class.forName(var0, true, ClassLoader.getSystemClassLoader());
        } catch (Exception var2) {
            return null;
        }
    }

    private static void flag(String var0, String var1) throws IOException {
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

    private static void usage(int var0) {
        Class var1 = loadClass("sun.JvmResult.hotspot.tools.JInfo");
        boolean var2 = var1 != null;
        System.err.println("Usage:");
        if (var2) {
            System.err.println("    jinfo [option] <pid>");
            System.err.println("        (to connect to running process)");
            System.err.println("    jinfo [option] <executable <core>");
            System.err.println("        (to connect to a core file)");
            System.err.println("    jinfo [option] [server_id@]<remote server IP or hostname>");
            System.err.println("        (to connect to remote debug server)");
            System.err.println("");
            System.err.println("where <option> is one of:");
            System.err.println("    -flag <name>         to print the value of the named VM flag");
            System.err.println("    -flag [+|-]<name>    to enable or disable the named VM flag");
            System.err.println("    -flag <name>=<value> to set the named VM flag to the given value");
            System.err.println("    -flags               to print VM flags");
            System.err.println("    -sysprops            to print Java system properties");
            System.err.println("    <no option>          to print both of the above");
            System.err.println("    -h | -help           to print this help message");
        } else {
            System.err.println("    jinfo <option> <pid>");
            System.err.println("       (to connect to a running process)");
            System.err.println("");
            System.err.println("where <option> is one of:");
            System.err.println("    -flag <name>         to print the value of the named VM flag");
            System.err.println("    -flag [+|-]<name>    to enable or disable the named VM flag");
            System.err.println("    -flag <name>=<value> to set the named VM flag to the given value");
            System.err.println("    -h | -help           to print this help message");
        }

        System.exit(var0);
    }
}
