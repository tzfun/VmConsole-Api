package beifengtz.vmconsole.tools.jinfo;

import beifengtz.vmconsole.entity.jinfo.JInfoFlag;
import beifengtz.vmconsole.entity.jinfo.JInfoResult;
import beifengtz.vmconsole.exception.NotAvailableException;
import beifengtz.vmconsole.exception.UnInitException;
import beifengtz.vmconsole.tools.MyTool;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.runtime.Arguments;
import sun.jvm.hotspot.runtime.VM;

import java.io.PrintStream;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jinfo.VmConsole-Api</p>
 * Created in 16:52 2019/5/30
 */
public class JInfoTool extends MyTool {
    public static final int MODE_FLAGS = 0;
    public static final int MODE_SYSPROPS = 1;
    public static final int MODE_BOTH = 2;
    private int mode;
    private static boolean hasInit;
    private PrintStream ps;
    private JInfoResult jInfoResult;

    public JInfoTool() {
    }

    public JInfoTool(int m) {
        this(m,System.out);
    }

    public JInfoTool(int m,PrintStream ps) {
        this.mode = m;
        this.ps = ps;
        hasInit = true;
    }

    public JInfoTool(int m,JInfoResult jInfoResult) {
        this.mode = m;
        this.jInfoResult = jInfoResult;
        hasInit = true;
    }

    public JInfoTool(JVMDebugger d) {
        super(d);
    }

    public boolean needsJavaPrefix() {
        return false;
    }

    public String getName() {
        return "jinfo";
    }

    protected void printFlagsUsage() {
        System.out.println("    -flags\tto print VM flags");
        System.out.println("    -sysprops\tto print Java System properties");
        System.out.println("    <no option>\tto print both of the above");
        super.printFlagsUsage();
    }

    public void run() {
        if (hasInit){
            MyTool tool = null;
            try{
                switch(this.mode) {
                    case 0:
                        this.printVMFlags(jInfoResult);
                        return;
                    case 1:
                        tool = new SysPropsDumperTool();
                        tool.setAgent(this.getAgent());
                        ((SysPropsDumperTool)tool).run(jInfoResult);
                        break;
                    case 2:
                        tool = new MyTool() {
                            public void run() {
                                MyTool sysProps = new SysPropsDumperTool();
                                sysProps.setAgent(this.getAgent());
                                try {
                                    ((SysPropsDumperTool)sysProps).run(jInfoResult);
                                }catch (NotAvailableException e){
                                    e.printStackTrace();
                                }
                                JInfoTool.this.printVMFlags(jInfoResult);
                            }
                        };
                        tool.run();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }catch (NotAvailableException e){
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
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
    public static void throwInitException() throws UnInitException{
        throw new UnInitException("JStackTool have not initialized. The caller should call the init method first.");
    }

    /**
     * <p>初始化方法，在使用{@link JInfoTool}实例方法的时候必须调用init方法</p>
     *
     * <p>默认将输出打印到控制台，即System.out</p>
     *
     * @param args 命令参数
     * @throws Exception 非法参数异常
     */
    public static void init(String[] args) throws Exception {
        init(args,System.out);
    }

    /**
     * <p>初始化方法，在使用{@link JInfoTool}实例方法的时候必须调用init方法</p>
     *
     * <p>传入命令参数和{@link PrintStream}打印流，其中命令参数用于
     * 解析命令参数，打印流用于接收数据</p>
     *
     * @param args 命令参数
     * @param ps 输出流，命令结果将直接输入到其中
     * @throws Exception 非法参数异常
     */
    public static void init(String[] args, PrintStream ps) throws Exception{
        ToolArg toolArg = initDoFirst(args);
        JInfoTool jinfo = new JInfoTool(toolArg.mode,ps);
        jinfo.execute(toolArg.args,ps);
    }

    /**
     * <p>初始化方法，在使用{@link JInfoTool}实例方法的时候必须调用init方法</p>
     *
     * <p>传入命令参数和{@link JInfoResult}结果集对象，其中命令参数用于
     * 解析命令参数，结果集对象用于接收并封装数据</p>
     *
     * @param args 命令参数
     * @param jInfoResult   jStack结果集对象
     * @throws Exception 非法参数异常
     */
    public static void init(String[] args, JInfoResult jInfoResult) throws Exception{
        ToolArg toolArg = initDoFirst(args);
        JInfoTool jinfo = new JInfoTool(toolArg.mode,jInfoResult);
        jinfo.execute(toolArg.args,jInfoResult);
    }

    /**
     * <p>命令参数解析，解析成功之后只剩下vmId，其他参数转换为标志数据</p>
     *
     * @param args 命令参数
     * @return ToolArg数组对象
     */
    private static ToolArg initDoFirst(String[] args){
        int mode = -1;
        switch(args.length) {
            case 1:
                if (args[0].charAt(0) == '-') {
                    throw new IllegalArgumentException();
                } else {
                    mode = 2;
                }
                break;
            case 2:
            case 3:
                String modeFlag = args[0];
                if (modeFlag.equals("-flags")) {
                    mode = 0;
                } else if (modeFlag.equals("-sysprops")) {
                    mode = 1;
                } else if (modeFlag.charAt(0) == '-') {
                    throw new IllegalArgumentException();
                } else {
                    mode = 2;
                }

                if (mode != 2) {
                    String[] newArgs = new String[args.length - 1];

                    for(int i = 0; i < newArgs.length; ++i) {
                        newArgs[i] = args[i + 1];
                    }

                    args = newArgs;
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return new ToolArg(mode,args);
    }

    /**
     * <p>Tool对象的参数及标志位封装类，用于封装解析后的参数及标志字段</p>
     */
    static class ToolArg{
        int mode;
        String[] args;

        public ToolArg(int mode, String[] args) {
            this.mode = mode;
            this.args = args;
        }
    }

    /**
     * <p>将flags信息打印至流中</p>
     *
     * @param out {@link PrintStream}打印流
     */
    private void printVMFlags(PrintStream out) {
        VM.Flag[] flags = VM.getVM().getCommandLineFlags();
        out.print("Non-default VM flags: ");
        VM.Flag[] arr$ = flags;
        int len$ = flags.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            VM.Flag flag = arr$[i$];
            if (flag.getOrigin() != 0) {
                if (flag.isBool()) {
                    String onoff = flag.getBool() ? "+" : "-";
                    out.print("-XX:" + onoff + flag.getName() + " ");
                } else {
                    out.print("-XX:" + flag.getName() + "=" + flag.getValue() + " ");
                }
            }
        }

        out.println();
        out.print("Command line: ");
        String str = Arguments.getJVMFlags();
        if (str != null) {
            System.out.print(str + " ");
        }

        str = Arguments.getJVMArgs();
        if (str != null) {
            System.out.print(str);
        }

        out.println();
    }

    /**
     * <p>将flags信息打印至JInfoResult对象中</p>
     *
     * @param jInfoResult {@link JInfoResult}对象
     */
    private void printVMFlags(JInfoResult jInfoResult) {
        VM.Flag[] flags = VM.getVM().getCommandLineFlags();
        VM.Flag[] arr$ = flags;
        int len$ = flags.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            VM.Flag flag = arr$[i$];
            if (flag.getOrigin() != 0) {
                JInfoFlag jInfoFlag = null;
                if (flag.isBool()) {
                    String onoff = flag.getBool() ? "+" : "-";
                    jInfoFlag = new JInfoFlag("-XX:" + onoff + flag.getName(),null);
                } else {
                    jInfoFlag = new JInfoFlag("-XX:" + flag.getName(),flag.getValue());
                }
                jInfoResult.getFlags().add(jInfoFlag);
            }
        }

        //  Command line
        String str = Arguments.getJVMFlags();
        StringBuilder commandLine = new StringBuilder();
        if (str != null) {
            commandLine.append(str);
            commandLine.append(" ");

        }

        str = Arguments.getJVMArgs();
        if (str != null) {
            commandLine.append(str);
        }
        jInfoResult.setCommandLine(commandLine.toString());
        commandLine = null;
    }
}
