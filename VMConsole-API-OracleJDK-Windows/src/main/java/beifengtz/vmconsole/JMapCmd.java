package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jmap.JMapForHeapResult;
import beifengtz.vmconsole.exception.NotSupportedEnvironmentException;
import beifengtz.vmconsole.security.SystemEnvironment;
import beifengtz.vmconsole.tools.jmap.HeapSummaryTool;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import sun.tools.attach.HotSpotVirtualMachine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.FileAlreadyExistsException;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.VmConsole-Api</p>
 * Created in 11:20 2019/6/16
 */
public class JMapCmd {
    private static String HISTO_OPTION = "-histo";
    private static String LIVE_HISTO_OPTION = "-histo:live";
    private static String DUMP_OPTION_PREFIX = "-dump:";
    private static String SA_TOOL_OPTIONS = "-heap|-heap:format=b|-clstats|-finalizerinfo";
    private static String FORCE_SA_OPTION = "-F";
    private static String DEFAULT_OPTION = "-pmap";
    private static final String LIVE_OBJECTS_OPTION = "-live";
    private static final String ALL_OBJECTS_OPTION = "-all";

    public JMapCmd() {
    }

    /**
     * 保存dump文件，内容包含所有的对象
     *
     * @param vmId  虚拟机id
     * @param filePath  文件路径
     * @return  boolean 失败为false，成功为true
     * @throws IOException  IO异常
     */
    public static boolean dumpAll(int vmId, String filePath) throws IOException, NotSupportedEnvironmentException {
        SystemEnvironment.checkWindowsAndOracleJdk();
        try{
            dump(String.valueOf(vmId),"–dump:format=b,file="+filePath);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存dump文件，只包含存活的对象
     *
     * @param vmId  虚拟机id
     * @param filePath  文件路径
     * @return  boolean 失败为false，成功为true
     * @throws IOException  IO异常
     */
    public static boolean dumpLive(int vmId,  String filePath) throws IOException,NotSupportedEnvironmentException{
        SystemEnvironment.checkWindowsAndOracleJdk();
        try{
            dump(String.valueOf(vmId),"–dump:live,format=b,file="+filePath);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 每个class的实例数目,内存占用,类全名信息.
     * @param vmId 虚拟机ID
     * @return InputStream 输出流
     * @throws IOException IO异常
     */
    public static InputStream histoAll(int vmId) throws IOException, NotSupportedEnvironmentException {
        SystemEnvironment.checkWindowsAndOracleJdk();
        VirtualMachine var2 = attach(String.valueOf(vmId));
        return ((HotSpotVirtualMachine)var2).heapHisto(new Object[]{"-all"});
    }

    /**
     * 每个class的实例数目,内存占用,类全名信息.只统计活的对象数量.
     * @param vmId 虚拟机ID
     * @return InputStream 输出流
     * @throws IOException IO异常
     */
    public static InputStream histoLive(int vmId) throws IOException, NotSupportedEnvironmentException {
        SystemEnvironment.checkWindowsAndOracleJdk();
        VirtualMachine var2 = attach(String.valueOf(vmId));
        return ((HotSpotVirtualMachine)var2).heapHisto(new Object[]{"-live"});
    }

    /**
     * 获取虚拟机heap信息，包含配置信息、年轻代占用比例、老年代占用比例等
     *
     * @param vmId  虚拟机ID
     * @return  JMapForHeapResult
     * @throws Exception 执行错误会抛出异常
     */
    public static JMapForHeapResult heapInfo(int vmId) throws Exception{
        SystemEnvironment.checkWindowsAndOracleJdk();
        JMapForHeapResult jMapForHeapResult = new JMapForHeapResult();
        HeapSummaryTool.init(new String[]{String.valueOf(vmId)}, jMapForHeapResult);
        return jMapForHeapResult;
    }

    private static void run(String[] var0) throws Exception {
        SystemEnvironment.checkWindowsAndOracleJdk();
        if (var0.length == 0) {
            throw new IllegalArgumentException("Input parameters cannot be empty");
        }

        boolean var1 = false;
        String var2 = null;

        int var3;
        //  拼装命令
        for(var3 = 0; var3 < var0.length; ++var3) {
            String var4 = var0[var3];
            if (!var4.startsWith("-")) {
                break;
            }

            if (!var4.equals("-help") && !var4.equals("-h")) {
                //  -F命令
                if (var4.equals(FORCE_SA_OPTION)) {
                    var1 = true;
                } else {
                    //  缺少vmId
                    if (var2 != null) {
                        throw new IllegalArgumentException("Missing required parameter vmId");
                    }

                    var2 = var4;
                }
            } else {
                throw new IllegalArgumentException("Help command is not supported yet");
            }
        }

        //  默认参数
        if (var2 == null) {
            var2 = DEFAULT_OPTION;
        }

        //  SA工具参数，如果包含就调用sa-jdi.jar包中的工具类
        if (var2.matches(SA_TOOL_OPTIONS)) {
            var1 = true;
        }

        int var7 = var0.length - var3;
        if (var7 == 0 || var7 > 2) {
            throw new IllegalArgumentException("Please pass in the correct parameters");
        }

        //  最后一个参数表示vmId，这里检查是否是正确的vmId，当然也包含远程VMID的检查
        if (var3 != 0 && var7 == 1) {
            if (!var0[var3].matches("[0-9]+")) {
                var1 = true;
            }
        } else {
            var1 = true;
        }

        //  var1为true则需要调用SA包的工具类
        if (var1) {
            //  重新拼接参数
            String[] var5 = new String[var7];

            for(int var6 = var3; var6 < var0.length; ++var6) {
                var5[var6 - var3] = var0[var6];
            }

            runTool(var2, var5);
        } else {
            String var8 = var0[1];
            if (var2.equals(HISTO_OPTION)) {
                histo(var8, false);
            } else if (var2.equals(LIVE_HISTO_OPTION)) {
                histo(var8, true);
            } else if (var2.startsWith(DUMP_OPTION_PREFIX)) {
                dump(var8, var2);
            } else {
                throw new IllegalArgumentException("Please pass in the correct parameters");
            }
        }

    }

    private static void runTool(String var0, String[] var1) throws Exception {
        String[][] var2 = new String[][]{{"-pmap", "sun.jvm.hotspot.tools.PMap"}, {"-heap", "sun.jvm.hotspot.tools.HeapSummary"}, {"-heap:format=b", "sun.jvm.hotspot.tools.HeapDumper"}, {"-histo", "sun.jvm.hotspot.tools.ObjectHistogram"}, {"-clstats", "sun.jvm.hotspot.tools.ClassLoaderStats"}, {"-finalizerinfo", "sun.jvm.hotspot.tools.FinalizerInfo"}};
        String var3 = null;
        if (var0.startsWith(DUMP_OPTION_PREFIX)) {
            String var4 = parseDumpOptions(var0);
            if (var4 == null) {
                usage(1);
            }

            var3 = "sun.jvm.hotspot.tools.HeapDumper";
            var1 = prepend(var4, var1);
            var1 = prepend("-f", var1);
        } else {
            for(int var8 = 0; var8 < var2.length; ++var8) {
                if (var0.equals(var2[var8][0])) {
                    var3 = var2[var8][1];
                    break;
                }
            }
        }

        if (var3 == null) {
            throw new IllegalArgumentException();
        }

        Class var9 = loadClass(var3);
        if (var9 == null) {
            throw new ClassNotFoundException(var3);
        }

        Class[] var5 = new Class[]{String[].class};
        Method var6 = var9.getDeclaredMethod("main", var5);
        Object[] var7 = new Object[]{var1};
        var6.invoke((Object)null, var7);
    }

    private static Class<?> loadClass(String var0) {
        try {
            return Class.forName(var0, true, ClassLoader.getSystemClassLoader());
        } catch (Exception var2) {
            return null;
        }
    }

    private static void histo(String var0, boolean var1) throws IOException {
        VirtualMachine var2 = attach(var0);
        InputStream var3 = ((HotSpotVirtualMachine)var2).heapHisto(new Object[]{var1 ? "-live" : "-all"});
        drain(var2, var3);
    }

    private static void dump(String var0, String var1) throws IOException,IllegalArgumentException {
        String var2 = parseDumpOptions(var1);
        if (var2 == null) {
            throw new IllegalArgumentException();
        }
        File file = new File(var2);
        if (file.exists()){
            throw new FileAlreadyExistsException("File exists");
        }
        var2 = file.getCanonicalPath();
        boolean var3 = isDumpLiveObjects(var1);
        VirtualMachine var4 = attach(var0);
        //  保存dump文件，返回的var5为执行结果
        InputStream var5 = ((HotSpotVirtualMachine)var4).dumpHeap(new Object[]{var2, var3 ? "-live" : "-all"});
        //  处理返回结果，这一步可要可不要
        //  drain(var4, var5);
    }

    private static String parseDumpOptions(String var0) {
        assert var0.startsWith(DUMP_OPTION_PREFIX);

        String var1 = null;
        String[] var2 = var0.substring(DUMP_OPTION_PREFIX.length()).split(",");

        for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3];
            if (!var4.equals("format=b") && !var4.equals("live")) {
                if (!var4.startsWith("file=")) {
                    return null;
                }

                var1 = var4.substring(5);
                if (var1.length() == 0) {
                    return null;
                }
            }
        }

        return var1;
    }

    private static boolean isDumpLiveObjects(String var0) {
        String[] var1 = var0.substring(DUMP_OPTION_PREFIX.length()).split(",");
        String[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (var5.equals("live")) {
                return true;
            }
        }

        return false;
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

            if (var3 instanceof AttachNotSupportedException && haveSA()) {
                System.err.println("The -F option can be used when the target process is not responding");
            }

            System.exit(1);
            return null;
        }
    }

    /**
     * 从输出流中获得结果
     * @param var0  虚拟机
     * @param var1  输入流
     * @throws IOException  IO异常
     */
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

    private static String[] prepend(String var0, String[] var1) {
        String[] var2 = new String[var1.length + 1];
        var2[0] = var0;
        System.arraycopy(var1, 0, var2, 1, var1.length);
        return var2;
    }

    private static boolean haveSA() {
        Class var0 = loadClass("sun.jvm.hotspot.tools.HeapSummary");
        return var0 != null;
    }

    private static void usage(int var0) {
        System.err.println("Usage:");
        if (haveSA()) {
            System.err.println("    jmap [option] <pid>");
            System.err.println("        (to connect to running process)");
            System.err.println("    jmap [option] <executable <core>");
            System.err.println("        (to connect to a core file)");
            System.err.println("    jmap [option] [server_id@]<remote server IP or hostname>");
            System.err.println("        (to connect to remote debug server)");
            System.err.println("");
            System.err.println("where <option> is one of:");
            System.err.println("    <none>               to print same info as Solaris pmap");
            System.err.println("    -heap                to print java heap summary");
            System.err.println("    -histo[:live]        to print histogram of java object heap; if the \"live\"");
            System.err.println("                         suboption is specified, only count live objects");
            System.err.println("    -clstats             to print class loader statistics");
            System.err.println("    -finalizerinfo       to print information on objects awaiting finalization");
            System.err.println("    -dump:<dump-options> to dump java heap in hprof binary format");
            System.err.println("                         dump-options:");
            System.err.println("                           live         dump only live objects; if not specified,");
            System.err.println("                                        all objects in the heap are dumped.");
            System.err.println("                           format=b     binary format");
            System.err.println("                           file=<file>  dump heap to <file>");
            System.err.println("                         Example: jmap -dump:live,format=b,file=heap.bin <pid>");
            System.err.println("    -F                   force. Use with -dump:<dump-options> <pid> or -histo");
            System.err.println("                         to force a heap dump or histogram when <pid> does not");
            System.err.println("                         respond. The \"live\" suboption is not supported");
            System.err.println("                         in this mode.");
            System.err.println("    -h | -help           to print this help message");
            System.err.println("    -J<flag>             to pass <flag> directly to the runtime system");
        } else {
            System.err.println("    jmap -histo <pid>");
            System.err.println("      (to connect to running process and print histogram of java object heap");
            System.err.println("    jmap -dump:<dump-options> <pid>");
            System.err.println("      (to connect to running process and dump java heap)");
            System.err.println("");
            System.err.println("    dump-options:");
            System.err.println("      format=b     binary default");
            System.err.println("      file=<file>  dump heap to <file>");
            System.err.println("");
            System.err.println("    Example:       jmap -dump:format=b,file=heap.bin <pid>");
        }

        System.exit(var0);
    }
}
