package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jcmd.JCmdEnum;
import beifengtz.vmconsole.entity.jcmd.JCmdProcess;
import beifengtz.vmconsole.entity.jcmd.JCmdResult;
import beifengtz.vmconsole.exception.NotAvailableException;
import beifengtz.vmconsole.tools.jcmd.ArgumentsTool;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import sun.jvmstat.monitor.*;
import sun.tools.attach.HotSpotVirtualMachine;
import sun.tools.jstat.JStatLogger;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.VmConsole-Api</p>
 * Created in 19:55 2019/5/30
 *
 * <p>jcmd命令执行类，可对对于的虚拟机执行相应命令并返回结果</p>
 *
 * <p>支持执行的命令如下：</p>
 *
 * <p>  - JFR.stop</p>
 * <p>  - JFR.start</p>
 * <p>  - JFR.dump</p>
 * <p>  - JFR.check</p>
 * <p>  - VM.native_memory</p>
 * <p>  - VM.check_commercial_features</p>
 * <p>  - VM.unlock_commercial_features</p>
 * <p>  - ManagementAgent.stop</p>
 * <p>  - ManagementAgent.start_local</p>
 * <p>  - ManagementAgent.start</p>
 * <p>  - GC.rotate_log</p>
 * <p>  - Thread.print</p>
 * <p>  - GC.class_stats</p>
 * <p>  - GC.class_histogram</p>
 * <p>  - GC.heap_dump</p>
 * <p>  - GC.run_finalization</p>
 * <p>  - GC.run</p>
 * <p>  - VM.uptime</p>
 * <p>  - VM.flags</p>
 * <p>  - VM.system_properties</p>
 * <p>  - VM.command_line</p>
 * <p>  - VM.version</p>
 */
public class JCmd {

    /**
     * 列出虚拟机进程列表
     *
     * @return {@link JCmdResult}对象
     * @throws Exception 异常
     */
    public static JCmdResult listProcess() throws Exception{
        return run(new String[]{});
    }

    /**
     * 列出虚拟机支持的命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return {@link JCmdResult}对象
     * @throws Exception 异常
     */
    public static JCmdResult listCommands(int vmId) throws Exception{
        return run(new String[]{String.valueOf(vmId),"help"});
    }

    /**
     *
     * @param vmId 虚拟机唯一识别id
     * @param jCmdEnum {@link JCmdEnum}命令枚举类
     * @return {@link JCmdResult}对象
     * @throws Exception 异常
     */
    public static JCmdResult executeCommand(int vmId, JCmdEnum jCmdEnum) throws Exception{
        return run(new String[]{String.valueOf(vmId),jCmdEnum.getCommand()});
    }

    /**
     * 批量执行命令（此方法暂无法使用）
     *
     * @param vmId 虚拟机唯一识别id
     * @param jCmdEnum {@link JCmdEnum}命令枚举类，可以传多个命令执行
     * @return {@link JCmdResult}对象
     * @throws Exception 异常
     */
    @Deprecated
    private static JCmdResult executeCommand(int vmId, JCmdEnum... jCmdEnum) throws Exception{
        String[] args = new String[jCmdEnum.length + 1];
        args[0] = String.valueOf(vmId);
        for (int i = 0;i<jCmdEnum.length;++i)
            args[i+1] = jCmdEnum[i].getCommand();
        return run(args);
    }

    /**
     * 为每一个虚拟机实例都执行此命令
     *
     * @param jCmdEnum {@link JCmdEnum}命令枚举类，可以传多个命令执行
     * @return {@link JCmdResult}对象
     * @throws Exception 异常
     */
    public static JCmdResult executeCommandForAllProcess(JCmdEnum jCmdEnum) throws Exception{
        return executeCommand(0,jCmdEnum);
    }

    /**
     * jcmd命令执行核心函数，可直接传入命令参数返回结果
     *
     * @param var0 命令参数
     * @return {@link JCmdResult}对象
     * @throws Exception 异常
     */
    private static JCmdResult run(String[] var0) throws Exception{
        ArgumentsTool var1 = null;

        try {
            var1 = new ArgumentsTool(var0);
        } catch (IllegalArgumentException var10) {
            throw new IllegalArgumentException("Error parsing arguments: " + var10.getMessage());
        }

        if (var1.isShowUsage()) {
            throw new IllegalArgumentException();
        }

        if (var1.isListProcesses()) {
            JCmdResult jCmdResult = new JCmdResult();
            List var2 = VirtualMachine.list();
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
                VirtualMachineDescriptor var4 = (VirtualMachineDescriptor)var3.next();
                jCmdResult.getProcesses().add(new JCmdProcess(Integer.valueOf(var4.id()),var4.displayName()));
            }
            return jCmdResult;
        }

        ArrayList var12 = new ArrayList();
        VirtualMachineDescriptor var5;
        List var13;
        Iterator var15;

        JCmdResult jCmdResult = new JCmdResult();

        if (var1.getPid() == 0) {
            var13 = VirtualMachine.list();
            var15 = var13.iterator();

            while(var15.hasNext()) {
                var5 = (VirtualMachineDescriptor)var15.next();
                if (!isJCmdProcess(var5)) {
                    var12.add(var5.id());
                }
            }
        } else if (var1.getProcessSubstring() != null) {
            var13 = VirtualMachine.list();
            var15 = var13.iterator();

            label94:
            while(true) {
                do {
                    if (!var15.hasNext()) {
                        if (var12.isEmpty()) {
                            throw new NotAvailableException("Could not find any processes matching : '" + var1.getProcessSubstring() + "'");
                        }
                        break label94;
                    }

                    var5 = (VirtualMachineDescriptor)var15.next();
                } while(isJCmdProcess(var5));

                try {
                    String var6 = getMainClass(var5);
                    if (var6 != null && var6.indexOf(var1.getProcessSubstring()) != -1) {
                        var12.add(var5.id());
                    }
                } catch (URISyntaxException | MonitorException var11) {
                    if (var11.getMessage() != null) {
                        throw new Exception(var11.getMessage());
                    } else {
                        throw var11;
                    }
                }
            }
        } else if (var1.getPid() == -1) {
            throw new NotAvailableException("Invalid pid specified");
        } else {
            var12.add(var1.getPid() + "");
        }

        var15 = var12.iterator();

        StringBuilder result = new StringBuilder();

        while(var15.hasNext()) {
            String var16 = (String)var15.next();
            if (var1.isListCounters()) {
                result.append(listCounters(var16));
            } else {
                try {
                    result.append(executeCommandForPid(var16, var1.getCommand()));
                } catch (Exception var9) {
                    throw var9;
                }
            }
        }
        jCmdResult.setResult(result.toString());
        return jCmdResult;
    }

    /**
     * 提交命令
     *
     * @param var0 虚拟机vmId
     * @param var1 命令
     * @return String结果
     * @throws AttachNotSupportedException 虚拟机连接异常
     * @throws IOException IO异常
     * @throws UnsupportedEncodingException 编码异常
     */
    private static String executeCommandForPid(String var0, String var1) throws AttachNotSupportedException, IOException, UnsupportedEncodingException {
        VirtualMachine var2 = VirtualMachine.attach(var0);
        HotSpotVirtualMachine var3 = (HotSpotVirtualMachine)var2;
        String[] var4 = var1.split("\\n");
        String[] var5 = var4;
        int var6 = var4.length;
        StringBuilder result = new StringBuilder();

        for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            if (var8.trim().equals("stop")) {
                break;
            }

            InputStream var9 = var3.executeJCmd(var8);
            Throwable var10 = null;

            try {
                byte[] var11 = new byte[256];
                boolean var13 = false;

                int var12;
                do {
                    var12 = var9.read(var11);
                    if (var12 > 0) {
                        String var14 = new String(var11, 0, var12, "UTF-8");
                        result.append(var14);
                        var13 = true;
                    }
                } while(var12 > 0);

                if (!var13) {
                    result.append("Command executed successfully");
                }
            } catch (Throwable var22) {
                var10 = var22;
                throw var22;
            } finally {
                if (var9 != null) {
                    if (var10 != null) {
                        try {
                            var9.close();
                        } catch (Throwable var21) {
                            var10.addSuppressed(var21);
                        }
                    } else {
                        var9.close();
                    }
                }
            }
        }

        var2.detach();
        //  这里返回的时候消除help的提示，因本API已经阉割了help的功能，为了避免造成使用者误解，所以需对提示信息进行筛查
        return result.toString().split("\\n{2}")[0];
    }

    /**
     * 列出计数器
     *
     * @param var0 命令参数
     * @return String
     * @throws URISyntaxException URI语法异常
     * @throws MonitorException 监管异常
     * @throws IOException IO异常
     */
    private static String listCounters(String var0) throws URISyntaxException,MonitorException,IOException{
        VmIdentifier var1 = null;

        var1 = new VmIdentifier(var0);
        StringBuilder result = new StringBuilder();

        MonitoredHost var2 = MonitoredHost.getMonitoredHost(var1);
        MonitoredVm var3 = var2.getMonitoredVm(var1, -1);
        JStatLogger var4 = new JStatLogger(var3);

        //  先推入内存，再读取成字符串
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        PrintStream ps = new PrintStream(outputStream);
        var4.printSnapShot("\\w*", new AscendingMonitorComparator(), false, true, ps);
        result.append(outputStream.toString());

        var2.detach(var3);
        outputStream.close();
        ps.close();

        return result.toString();
    }

    /**
     * 检测是否是jcmd的处理命令
     * @param var0 虚拟机描述对象
     * @return boolean
     */
    private static boolean isJCmdProcess(VirtualMachineDescriptor var0) {
        try {
            String var1 = getMainClass(var0);
            return var1 != null && var1.equals(sun.tools.jcmd.JCmd.class.getName());
        } catch (MonitorException | URISyntaxException var2) {
            return false;
        }
    }

    /**
     * 获取主类
     *
     * @param var0 虚拟机描述对象
     * @return  String
     * @throws URISyntaxException URI语法异常
     * @throws MonitorException 监管异常
     */
    private static String getMainClass(VirtualMachineDescriptor var0) throws URISyntaxException, MonitorException {
        try {
            String var1 = null;
            VmIdentifier var2 = new VmIdentifier(var0.id());
            MonitoredHost var3 = MonitoredHost.getMonitoredHost(var2);
            MonitoredVm var4 = var3.getMonitoredVm(var2, -1);
            var1 = MonitoredVmUtil.mainClass(var4, true);
            var3.detach(var4);
            return var1;
        } catch (NullPointerException var5) {
            return null;
        }
    }

    static class AscendingMonitorComparator implements Comparator<Monitor> {
        AscendingMonitorComparator() {
        }

        public int compare(Monitor var1, Monitor var2) {
            String var3 = var1.getName();
            String var4 = var2.getName();
            return var3.compareTo(var4);
        }
    }
}
