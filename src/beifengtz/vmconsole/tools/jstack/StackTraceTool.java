package beifengtz.vmconsole.tools.jstack;

import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.AddressException;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.oops.Method;
import sun.jvm.hotspot.runtime.*;
import sun.jvm.hotspot.tools.StackTrace;

import java.io.PrintStream;

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

    public static void main(String[] args) {
        StackTraceTool st = new StackTraceTool();
        st.execute(args);
    }

}
