package beifengtz.vmconsole.tools.jstack;

import sun.jvm.hotspot.debugger.JVMDebugger;

import java.io.PrintStream;


/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jstack.VmConsole-Api</p>
 * Created in 22:34 2019/5/28
 */
public class JStackTool extends MyTool {
    private boolean mixedMode;
    private boolean concurrentLocks;
    private PrintStream ps;

    public JStackTool(boolean mixedMode, boolean concurrentLocks, PrintStream ps) {
        this.mixedMode = mixedMode;
        this.concurrentLocks = concurrentLocks;
        this.ps = ps;
    }

    public JStackTool() {
        this(true, true, System.out);
    }

    public JStackTool(JVMDebugger d) {
        super(d);
    }

    protected boolean needsJavaPrefix() {
        return false;
    }

    public String getName() {
        return "jstack";
    }

    protected void printFlagsUsage() {
        System.out.println("    -l\tto print java.util.concurrent locks");
        System.out.println("    -m\tto print both java and native frames (mixed mode)");
        super.printFlagsUsage();
    }

    public void run() {
        MyTool tool = null;
        if (this.mixedMode) {
            tool = new PStackTool(false, this.concurrentLocks);
            initTool(tool);
            ((PStackTool) tool).run(ps);
        } else {
            tool = new StackTraceTool(false, this.concurrentLocks);
            initTool(tool);
            ((StackTraceTool) tool).run(ps);
        }
    }

    public void initTool(MyTool tool){
        tool.setAgent(this.getAgent());
        tool.setDebugeeType(this.getDebugeeType());
    }

    /**
     * 初始化方法，用于jstack调用
     *
     * @param args 命令参数
     * @param ps   输出流
     */
    public static void init(String[] args, PrintStream ps) {
        boolean mixedMode = false;
        boolean concurrentLocks = false;
        int used = 0;

        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-m")) {
                mixedMode = true;
                ++used;
            } else if (args[i].equals("-l")) {
                concurrentLocks = true;
                ++used;
            }
        }

        if (used != 0) {
            String[] newArgs = new String[args.length - used];

            for (int i = 0; i < newArgs.length; ++i) {
                newArgs[i] = args[i + used];
            }

            args = newArgs;
        }

        JStackTool jstack = new JStackTool(mixedMode, concurrentLocks, ps);
        jstack.execute(args);
    }
}
