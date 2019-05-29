package beifengtz.vmconsole.tools.jstack;

import beifengtz.vmconsole.entity.JStackResult;
import beifengtz.vmconsole.entity.ThreadStack;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.AddressException;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.oops.Method;
import sun.jvm.hotspot.runtime.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jstack.VmConsole-Api</p>
 * Created in 22:51 2019/5/28
 */
public class StackTraceTool extends MyTool {

    private boolean verbose;
    private boolean concurrentLocks;

    public StackTraceTool(boolean v, boolean concurrentLocks) {
        this.verbose = v;
        this.concurrentLocks = concurrentLocks;
    }

    public StackTraceTool() {
        this(true, true);
    }

    public void run() {
        this.run(System.out);
    }

    public StackTraceTool(JVMDebugger d) {
        super(d);
    }

    public StackTraceTool(JVMDebugger d, boolean v, boolean concurrentLocks) {
        super(d);
        this.verbose = v;
        this.concurrentLocks = concurrentLocks;
    }

    /**
     * 打印流输出结果
     *
     * @param tty {@link PrintStream}
     */
    public void run(PrintStream tty) {
        try {
            DeadlockDetector.print(tty);
        } catch (Exception var11) {
            var11.printStackTrace();
            tty.println("Can't print deadlocks:" + var11.getMessage());
        }

        try {
            ConcurrentLocksPrinter concLocksPrinter = null;
            if (this.concurrentLocks) {
                concLocksPrinter = new ConcurrentLocksPrinter();
            }

            Threads threads = VM.getVM().getThreads();
            int i = 1;

            for(JavaThread cur = threads.first(); cur != null; ++i) {
                if (cur.isJavaThread()) {
                    Address sp = cur.getLastJavaSP();
                    tty.print("Thread ");
                    cur.printThreadIDOn(tty);
                    tty.print(": (state = " + cur.getThreadState());
                    if (this.verbose) {
                        tty.println(", current Java SP = " + sp);
                    }

                    tty.println(')');

                    try {
                        for(JavaVFrame vf = cur.getLastJavaVFrameDbg(); vf != null; vf = vf.javaSender()) {
                            Method method = vf.getMethod();
                            tty.print(" - " + method.externalNameAndSignature() + " @bci=" + vf.getBCI());
                            int lineNumber = method.getLineNumberFromBCI(vf.getBCI());
                            if (lineNumber != -1) {
                                tty.print(", line=" + lineNumber);
                            }

                            if (this.verbose) {
                                Address pc = vf.getFrame().getPC();
                                if (pc != null) {
                                    tty.print(", pc=" + pc);
                                }

                                tty.print(", Method*=" + method.getAddress());
                            }

                            if (vf.isCompiledFrame()) {
                                tty.print(" (Compiled frame");
                                if (vf.isDeoptimized()) {
                                    tty.print(" [deoptimized]");
                                }
                            }

                            if (vf.isInterpretedFrame()) {
                                tty.print(" (Interpreted frame");
                            }

                            if (vf.mayBeImpreciseDbg()) {
                                tty.print("; information may be imprecise");
                            }

                            tty.println(")");
                        }
                    } catch (Exception var12) {
                        tty.println("Error occurred during stack walking:");
                        var12.printStackTrace();
                    }

                    tty.println();
                    if (this.concurrentLocks) {
                        concLocksPrinter.print(cur, tty);
                    }

                    tty.println();
                }

                cur = cur.next();
            }
        } catch (AddressException var13) {
            System.err.println("Error accessing address 0x" + Long.toHexString(var13.getAddress()));
            var13.printStackTrace();
        }

    }

    /**
     * jStackResult对象接收结果，对结果进行封装
     *
     * @param jStackResult {@link JStackResult}
     */
    public void run(JStackResult jStackResult) throws Exception{
        //  内存流（针对程序是输出，针对内存是输入）
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);

        //  使用打印流，将结果打印至内存
        PrintStream tty = new PrintStream(outputStream);

        try {
            DeadlockDetector.print(tty);
        } catch (Exception var11) {
            jStackResult.setDeadlocks("ERROR! Can't print deadlocks:" + var11.getMessage());
            closeStream(tty,outputStream);
            throw var11;

        }

        try {
            ConcurrentLocksPrinter concLocksPrinter = null;
            if (this.concurrentLocks) {
                concLocksPrinter = new ConcurrentLocksPrinter();
            }

            //  依次遍历线程
            Threads threads = VM.getVM().getThreads();
            int i = 1;

            LinkedList<ThreadStack> threadStacks = new LinkedList<>();

            for(JavaThread cur = threads.first(); cur != null; ++i) {

                ThreadStack threadStack = new ThreadStack();

                if (cur.isJavaThread()) {
                    Address sp = cur.getLastJavaSP();
                    //  将线程Id写入打印流
                    cur.printThreadIDOn(tty);

                    String idInfo = outputStream.toString().trim();
                    //  处理首个线程，首个线程信息之前会输出死锁情况，所以这里需要进行处理
                    if (i == 1){
                        String[] strs = idInfo.split("\\n");
                        String threadId = strs[strs.length-1];

                        threadStack.setDeadLocks(idInfo.substring(0,idInfo.length()-threadId.length()-1));

                        threadStack.setId(threadId);
                    }else {
                        threadStack.setId(idInfo);
                    }
                    outputStream.reset();// 内存重用

                    threadStack.setState(cur.getThreadState().toString());
                    if (this.verbose) {
                        threadStack.setCurrentJavaSP(sp.toString());
                    }

                    StringBuilder tempStacks = new StringBuilder();
                    try {
                        for(JavaVFrame vf = cur.getLastJavaVFrameDbg(); vf != null; vf = vf.javaSender()) {
                            Method method = vf.getMethod();

                            tempStacks.append(" - ");
                            tempStacks.append(method.externalNameAndSignature());
                            tempStacks.append(" @bci=");
                            tempStacks.append(vf.getBCI());

                            int lineNumber = method.getLineNumberFromBCI(vf.getBCI());
                            if (lineNumber != -1) {
                                tempStacks.append(", line=");
                                tempStacks.append(lineNumber);
                            }

                            if (this.verbose) {
                                Address pc = vf.getFrame().getPC();
                                if (pc != null) {
                                    tempStacks.append(", pc=");
                                    tempStacks.append(pc);
                                }
                                tempStacks.append(", Method*=");
                                tempStacks.append(method.getAddress());
                            }

                            if (vf.isCompiledFrame()) {
                                tempStacks.append(" (Compiled frame");
                                if (vf.isDeoptimized()) {
                                    tempStacks.append(" [deoptimized]");
                                }
                            }

                            if (vf.isInterpretedFrame()) {
                                tempStacks.append(" (Interpreted frame");
                            }

                            if (vf.mayBeImpreciseDbg()) {
                                tempStacks.append("; information may be imprecise");
                            }

                            tempStacks.append(")");
                            tempStacks.append("\n");
                        }
                    } catch (Exception var12) {
                        tempStacks.append("Error occurred during stack walking:");
                        tempStacks.append(var12.getMessage());
                    }finally {
                        threadStack.setStacks(tempStacks);
                    }

                    if (this.concurrentLocks) {
                        concLocksPrinter.print(cur, tty);

                        threadStack.setConcurrentLocks(outputStream.toString());
                        outputStream.reset();
                    }
                    threadStacks.add(threadStack);
                }

                cur = cur.next();
            }
            jStackResult.setThreadStacks(threadStacks);
        } catch (AddressException var13) {
            System.err.println("Error accessing address 0x" + Long.toHexString(var13.getAddress()));
            throw var13;
        }


    }
    /**
     * 关闭相应流
     * @param ps {@link PrintStream}
     * @param o {@link OutputStream}
     * @throws IOException
     */
    private void closeStream(PrintStream ps, OutputStream o) throws IOException {
        ps.close();
        o.close();
    }
}
