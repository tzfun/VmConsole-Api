package beifengtz.vmconsole.tools.jstack;

import java.io.PrintStream;

import beifengtz.vmconsole.entity.jstack.JStackResult;
import sun.jvm.hotspot.HotSpotAgent;
import sun.jvm.hotspot.debugger.DebuggerException;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.runtime.VM;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jstack</p>
 * Created in 23:24 2019/5/28
 *
 * <p>虚拟机注册工具类，用于注册虚拟机信息、调试环境、获取虚拟机版本等</p>
 */
public abstract class MyTool implements Runnable {
    private HotSpotAgent agent;
    private JVMDebugger jvmDebugger;
    private int debugeeType;
    protected static final int DEBUGEE_PID = 0;
    protected static final int DEBUGEE_CORE = 1;
    protected static final int DEBUGEE_REMOTE = 2;

    public MyTool() {
    }

    public MyTool(JVMDebugger d) {
        this.jvmDebugger = d;
    }

    public String getName() {
        return this.getClass().getName();
    }

    protected boolean needsJavaPrefix() {
        return true;
    }

    protected void setAgent(HotSpotAgent a) {
        this.agent = a;
    }

    protected void setDebugeeType(int dt) {
        this.debugeeType = dt;
    }

    protected HotSpotAgent getAgent() {
        return this.agent;
    }

    protected int getDebugeeType() {
        return this.debugeeType;
    }

    protected void printUsage() {
        String name = null;
        if (this.needsJavaPrefix()) {
            name = "java " + this.getName();
        } else {
            name = this.getName();
        }

        System.out.println("Usage: " + name + " [option] <pid>");
        System.out.println("\t\t(to connect to a live java process)");
        System.out.println("   or " + name + " [option] <executable> <core>");
        System.out.println("\t\t(to connect to a core file)");
        System.out.println("   or " + name + " [option] [server_id@]<remote server IP or hostname>");
        System.out.println("\t\t(to connect to a remote debug server)");
        System.out.println();
        System.out.println("where option must be one of:");
        this.printFlagsUsage();
    }

    protected void printFlagsUsage() {
        System.out.println("    -h | -help\tto print this help message");
    }

    protected void usage() {
        this.printUsage();
    }

    public void execute(String[] args, PrintStream ps) {
        try {
            this.start(args, ps, ps);
        } finally {
            this.stop();
        }
    }

    public void execute(String[] args, JStackResult jStackResult) throws Exception{
        try {
            this.start(args, jStackResult);
        } finally {
            this.stop();
        }
    }

    public void stop() {
        if (this.agent != null) {
            this.agent.detach();
        }

    }

    private int start(String[] args, PrintStream out, PrintStream err) {
        if (args.length >= 1 && args.length <= 2) {
            if (args[0].startsWith("-h")) {
                this.usage();
                return 0;
            } else if (args[0].startsWith("-")) {
                this.usage();
                return 1;
            } else {
                int pid = 0;
                String coreFileName = null;
                String executableName = null;
                String remoteServer = null;
                switch (args.length) {
                    case 1:
                        try {
                            pid = Integer.parseInt(args[0]);
                            this.debugeeType = 0;
                        } catch (NumberFormatException var9) {
                            remoteServer = args[0];
                            this.debugeeType = 2;
                        }
                        break;
                    case 2:
                        executableName = args[0];
                        coreFileName = args[1];
                        this.debugeeType = 1;
                        break;
                    default:
                        this.usage();
                        return 1;
                }

                this.agent = new HotSpotAgent();

                try {
                    switch (this.debugeeType) {
                        case 0:
                            out.println("Attaching to process ID " + pid + ", please wait...");
                            this.agent.attach(pid);
                            break;
                        case 1:
                            out.println("Attaching to core " + coreFileName + " from executable " + executableName + ", please wait...");
                            this.agent.attach(executableName, coreFileName);
                            break;
                        case 2:
                            out.println("Attaching to remote server " + remoteServer + ", please wait...");
                            this.agent.attach(remoteServer);
                    }
                } catch (DebuggerException var10) {
                    switch (this.debugeeType) {
                        case 0:
                            err.print("Error attaching to process: ");
                            break;
                        case 1:
                            err.print("Error attaching to core file: ");
                            break;
                        case 2:
                            err.print("Error attaching to remote server: ");
                    }

                    if (var10.getMessage() != null) {
                        err.println(var10.getMessage());
                        var10.printStackTrace();
                    }

                    err.println();
                    return 1;
                }

                out.println("Debugger attached successfully.");
                this.startInternal();
                return 0;
            }
        } else {
            this.usage();
            return 1;
        }
    }

    private int start(String[] args, JStackResult jStackResult) throws Exception{
        if (args.length >= 1 && args.length <= 2) {
            if (args[0].startsWith("-h")) {
                return 0;
            } else if (args[0].startsWith("-")) {
                return 1;
            } else {
                PrintStream err = System.err;
//                PrintStream out = System.out;
                StringBuilder errStr = new StringBuilder();
                int pid = 0;
                String coreFileName = null;
                String executableName = null;
                String remoteServer = null;
                switch (args.length) {
                    case 1:
                        try {
                            pid = Integer.parseInt(args[0]);
                            this.debugeeType = 0;
                        } catch (NumberFormatException var9) {
                            remoteServer = args[0];
                            this.debugeeType = 2;
                        }
                        break;
                    case 2:
                        executableName = args[0];
                        coreFileName = args[1];
                        this.debugeeType = 1;
                        break;
                    default:
                        this.usage();
                        return 1;
                }

                this.agent = new HotSpotAgent();

                try {
                    switch (this.debugeeType) {
                        case 0:
                            jStackResult.setVmId(pid);
//                            out.println("Attaching to process ID " + pid + ", please wait...");
                            this.agent.attach(pid);
                            break;
                        case 1:
//                            out.println("Attaching to core " + coreFileName + " from executable " + executableName + ", please wait...");
                            this.agent.attach(executableName, coreFileName);
                            break;
                        case 2:
//                            out.println("Attaching to remote server " + remoteServer + ", please wait...");
                            this.agent.attach(remoteServer);
                    }
                } catch (DebuggerException var10) {
                    switch (this.debugeeType) {
                        case 0:
                            errStr.append("Error attaching to process: ");
                            break;
                        case 1:
                            errStr.append("Error attaching to core file: ");
                            break;
                        case 2:
                            errStr.append("Error attaching to remote server: ");
                    }

                    if (var10.getMessage() != null) {
                        errStr.append(var10.getMessage());
                        errStr.append("\n");
                        throw new Exception(errStr.toString());
                    }

                    err.println();
                    return 1;
                }
                this.startInternal(jStackResult);
                return 0;
            }
        } else {
            this.usage();
            return 1;
        }
    }

    public void start() {
        if (this.jvmDebugger == null) {
            throw new RuntimeException("Tool.start() called with no JVMDebugger set.");
        } else {
            this.agent = new HotSpotAgent();
            this.agent.attach(this.jvmDebugger);
            this.startInternal();
        }
    }

    private void startInternal() {
        PrintStream out = System.out;
        VM vm = VM.getVM();
        if (vm.isCore()) {
            out.println("Core build detected.");
        } else if (vm.isClientCompiler()) {
            out.println("Client compiler detected.");
        } else {
            if (!vm.isServerCompiler()) {
                throw new RuntimeException("Fatal error: should have been able to detect core/C1/C2 build");
            }

            out.println("Server compiler detected.");
        }

        String version = vm.getVMRelease();
        if (version != null) {
            out.print("JVM version is ");
            out.println(version);
        }

        this.run();
    }

    private void startInternal(JStackResult jStackResult) {
//        PrintStream out = System.out;
        VM vm = VM.getVM();
        if (vm.isCore()) {
//            out.println("Core build detected.");
        } else if (vm.isClientCompiler()) {
//            out.println("Client compiler detected.");
        } else {
            if (!vm.isServerCompiler()) {
                throw new RuntimeException("Fatal error: should have been able to detect core/C1/C2 build");
            }
//            out.println("Server compiler detected.");
        }

        String version = vm.getVMRelease();
        if (version != null) {
            jStackResult.setVmVersion(version);
        }

        this.run();
    }
}

