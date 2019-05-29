package beifengtz.vmconsole.tools.jstack;

import beifengtz.vmconsole.entity.JStackResult;
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
    private static boolean hasInit;
    private PrintStream ps;
    private JStackResult jStackResult;
    private int calledType; //  调用时执行类型，0为打印流输出，1为JStackResult对象接收

    public JStackTool(boolean mixedMode, boolean concurrentLocks, PrintStream ps) {
        this.mixedMode = mixedMode;
        this.concurrentLocks = concurrentLocks;
        this.ps = ps;
        hasInit = true;
        calledType = 0;
    }

    public JStackTool(JVMDebugger d) {
        super(d);
    }

    public JStackTool(boolean mixedMode, boolean concurrentLocks, JStackResult jStackResult) {
        this.mixedMode = mixedMode;
        this.concurrentLocks = concurrentLocks;
        this.jStackResult = jStackResult;
        hasInit = true;
        calledType = 1;
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
        if (hasInit){
            MyTool tool = null;
            if (this.mixedMode) {
                tool = new PStackTool(false, this.concurrentLocks);
                initTool(tool);
                try{
                    if (calledType == 0)
                        ((PStackTool) tool).run(ps);
                    else
                        ((PStackTool) tool).run(jStackResult);

                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                tool = new StackTraceTool(false, this.concurrentLocks);
                initTool(tool);
                try{
                    if (calledType == 0)
                        ((StackTraceTool) tool).run(ps);
                    else
                        ((StackTraceTool) tool).run(jStackResult);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }else {
            try {
                throwInitException();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 抛出未初始化异常
     * @throws Exception
     */
    public static void throwInitException() throws Exception{
        throw new Exception("JStackTool have not initialized. The caller should call the init method first.");
    }

    /**
     * 设置Tool对象的默认参数
     *
     * @param tool {@link MyTool}
     */
    public void initTool(MyTool tool){
        tool.setAgent(this.getAgent());
        tool.setDebugeeType(this.getDebugeeType());
    }

    /**
     * 初始化方法，用于jstack调用，实例化对象并检测命令参数
     *
     * 默认将输出打印到控制台
     *
     * @param args 命令参数
     */
    public static void init(String[] args) {
        init(args,System.out);
    }

    /**
     * 初始化方法，用于jStack调用，实例化对象并检测命令参数
     *
     * 传入{@link JStackResult}对象接收数据
     *
     * @param args 命令参数
     * @param jStackResult   jStack结果集对象
     */
    public static void init(String[] args, JStackResult jStackResult) throws Exception{
        hasInit = false;
        ToolArg toolArg = initDoFirst(args);
        JStackTool jstack = new JStackTool(toolArg.mixedMode, toolArg.concurrentLocks,jStackResult);
        jstack.execute(args,jStackResult);
    }

    /**
     * 初始化方法，用于jStack调用，实例化对象并检测命令参数
     *
     * 传入{@link PrintStream}打印流接收数据
     *
     * @param args 命令参数
     * @param ps 输出流
     */
    public static void init(String[] args, PrintStream ps) {
        hasInit = false;
        ToolArg toolArg = initDoFirst(args);
        JStackTool jstack = new JStackTool(toolArg.mixedMode, toolArg.concurrentLocks,ps);
        jstack.execute(args,ps);
    }

    /**
     * jStack参数解析
     *
     * @param args 命令参数
     * @return 数组对象
     */
    public static ToolArg initDoFirst(String[] args) {
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
        return new ToolArg(mixedMode,concurrentLocks,args);
    }

    /**
     * Tool对象的参数及标志位封装类
     */
    static class ToolArg{
        boolean mixedMode;
        boolean concurrentLocks;
        String[] args;

        public ToolArg(boolean mixedMode, boolean concurrentLocks, String[] args) {
            this.mixedMode = mixedMode;
            this.concurrentLocks = concurrentLocks;
            this.args = args;
        }
    }
}
