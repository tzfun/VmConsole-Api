package beifengtz.vmconsole.tools.jstack;

import beifengtz.vmconsole.entity.jstack.JStackResult;
import beifengtz.vmconsole.exception.*;
import beifengtz.vmconsole.tools.MyTool;
import sun.jvm.hotspot.debugger.AddressException;
import sun.jvm.hotspot.debugger.JVMDebugger;

import java.io.IOException;
import java.io.PrintStream;


/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jstack</p>
 * Created in 22:34 2019/5/28
 *
 * <p>jstack命令工具封装，其中包含模拟命令执行体、参数解析、逻辑判断等</p>
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

    public boolean needsJavaPrefix() {
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
                }catch (NotAvailableException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }catch (UnKnowException e){
                    e.printStackTrace();
                }catch (ConfigurationException e){
                    e.printStackTrace();
                }catch (NotImplementedException e){
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
                }catch (IOException e){
                    e.printStackTrace();
                }catch (UnKnowException e){
                    e.printStackTrace();
                }catch (AddressException e){
                    e.printStackTrace();
                }

            }
        }else {
            try {
                throwInitException();
            }catch (UnInitException e){
                e.printStackTrace();
            }

        }
    }

    /**
     * <p>抛出未初始化异常类</p>
     *
     * @throws UnInitException 未初始化异常
     */
    public static void throwInitException() throws UnInitException {
        throw new UnInitException("JStackTool have not initialized. The caller should call the init method first.");
    }

    /**
     * <p>设置Tool对象的默认参数</p>
     *
     * @param tool {@link MyTool}
     */
    public void initTool(MyTool tool){
        tool.setAgent(this.getAgent());
        tool.setDebugeeType(this.getDebugeeType());
    }

    /**
     * <p>初始化方法，在使用{@link JStackTool}实例方法的时候必须调用init方法</p>
     *
     * <p>默认将输出打印到控制台，即System.out</p>
     *
     * @param args 命令参数
     */
    public static void init(String[] args) {
        init(args,System.out);
    }

    /**
     * <p>初始化方法，在使用{@link JStackTool}实例方法的时候必须调用init方法</p>
     *
     * <p>传入命令参数和{@link PrintStream}打印流，其中命令参数用于
     * 解析命令参数，打印流用于接收数据</p>
     *
     * @param args 命令参数
     * @param ps 输出流，命令结果将直接输入到其中
     */
    public static void init(String[] args, PrintStream ps) {
        hasInit = false;
        ToolArg toolArg = initDoFirst(args);
        JStackTool jstack = new JStackTool(toolArg.mixedMode, toolArg.concurrentLocks,ps);
        jstack.execute(toolArg.args,ps);
    }


    /**
     * <p>初始化方法，在使用{@link JStackTool}实例方法的时候必须调用init方法</p>
     *
     * <p>传入命令参数和{@link JStackResult}结果集对象，其中命令参数用于
     * 解析命令参数，结果集对象用于接收并封装数据</p>
     *
     * @param args 命令参数
     * @param jStackResult   jStack结果集对象
     * @throws Exception 异常
     */
    public static void init(String[] args, JStackResult jStackResult) throws Exception{
        hasInit = false;
        ToolArg toolArg = initDoFirst(args);
        JStackTool jstack = new JStackTool(toolArg.mixedMode, toolArg.concurrentLocks,jStackResult);
        jstack.execute(toolArg.args,jStackResult);
    }

    /**
     * <p>jStack命令参数解析，解析成功之后只剩下vmId，其他-m、-l等参数转换为boolean型的标志数据</p>
     *
     * @param args 命令参数
     * @return ToolArg数组对象
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
     * <p>Tool对象的参数及标志位封装类，用于封装解析后的参数及标志字段</p>
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
