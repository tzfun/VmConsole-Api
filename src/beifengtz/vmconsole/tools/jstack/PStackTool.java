package beifengtz.vmconsole.tools.jstack;

import sun.jvm.hotspot.code.CodeBlob;
import sun.jvm.hotspot.code.CodeCache;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.Debugger;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.debugger.ThreadProxy;
import sun.jvm.hotspot.debugger.cdbg.CDebugger;
import sun.jvm.hotspot.debugger.cdbg.CFrame;
import sun.jvm.hotspot.debugger.cdbg.ClosestSymbol;
import sun.jvm.hotspot.interpreter.Interpreter;
import sun.jvm.hotspot.interpreter.InterpreterCodelet;
import sun.jvm.hotspot.oops.Method;
import sun.jvm.hotspot.runtime.*;
import sun.jvm.hotspot.tools.PStack;
import sun.jvm.hotspot.utilities.PlatformInfo;

import java.io.PrintStream;
import java.util.*;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.VmConsole-Api</p>
 * Created in 22:31 2019/5/28
 */
public class PStackTool extends MyTool {

    private Map jframeCache;
    private Map proxyToThread;
    private PrintStream out;
    private boolean verbose;
    private boolean concurrentLocks;

    public PStackTool(boolean v, boolean concurrentLocks) {
        this.verbose = v;
        this.concurrentLocks = concurrentLocks;
    }

    public PStackTool() {
        this(true, true);
    }

    public PStackTool(JVMDebugger d) {
        super(d);
    }

    public void run() {
        this.run(System.out);
    }

    public void run(PrintStream out) {
        Debugger dbg = this.getAgent().getDebugger();
        this.run(out, dbg);
    }

    public void run(PrintStream out, Debugger dbg) {
        if (PlatformInfo.getOS().equals("darwin")) {
            out.println("Not available on Darwin");
        } else {
            CDebugger cdbg = dbg.getCDebugger();
            if (cdbg != null) {
                ConcurrentLocksPrinter concLocksPrinter = null;
                this.initJFrameCache();
                if (this.concurrentLocks) {
                    concLocksPrinter = new ConcurrentLocksPrinter();
                }

                try {
                    DeadlockDetector.print(out);
                } catch (Exception var16) {
                    out.println("can't print deadlock information: " + var16.getMessage());
                }

                List l = cdbg.getThreadList();
                boolean cdbgCanDemangle = cdbg.canDemangle();
                Iterator itr = l.iterator();

                while(itr.hasNext()) {
                    ThreadProxy th = (ThreadProxy)itr.next();

                    try {
                        CFrame f = cdbg.topFrameForThread(th);
                        out.print("----------------- ");
                        out.print(th);
                        out.println(" -----------------");

                        for(; f != null; f = f.sender(th)) {
                            ClosestSymbol sym = f.closestSymbolToPC();
                            Address pc = f.pc();
                            out.print(pc + "\t");
                            if (sym != null) {
                                String name = sym.getName();
                                if (cdbgCanDemangle) {
                                    name = cdbg.demangle(name);
                                }

                                out.print(name);
                                long diff = sym.getOffset();
                                if (diff != 0L) {
                                    out.print(" + 0x" + Long.toHexString(diff));
                                }

                                out.println();
                            } else {
                                String[] names = null;
                                Interpreter interp = VM.getVM().getInterpreter();
                                if (interp.contains(pc)) {
                                    names = this.getJavaNames(th, f.localVariableBase());
                                    if (names == null || names.length == 0) {
                                        out.print("<interpreter> ");
                                        InterpreterCodelet ic = interp.getCodeletContaining(pc);
                                        if (ic != null) {
                                            String desc = ic.getDescription();
                                            if (desc != null) {
                                                out.print(desc);
                                            }
                                        }

                                        out.println();
                                    }
                                } else {
                                    CodeCache c = VM.getVM().getCodeCache();
                                    if (c.contains(pc)) {
                                        CodeBlob cb = c.findBlobUnsafe(pc);
                                        if (cb.isNMethod()) {
                                            names = this.getJavaNames(th, f.localVariableBase());
                                            if (names == null || names.length == 0) {
                                                out.println("<Unknown compiled code>");
                                            }
                                        } else if (cb.isBufferBlob()) {
                                            out.println("<StubRoutines>");
                                        } else if (cb.isRuntimeStub()) {
                                            out.println("<RuntimeStub>");
                                        } else if (cb.isDeoptimizationStub()) {
                                            out.println("<DeoptimizationStub>");
                                        } else if (cb.isUncommonTrapStub()) {
                                            out.println("<UncommonTrap>");
                                        } else if (cb.isExceptionStub()) {
                                            out.println("<ExceptionStub>");
                                        } else if (cb.isSafepointStub()) {
                                            out.println("<SafepointStub>");
                                        } else {
                                            out.println("<Unknown code blob>");
                                        }
                                    } else {
                                        this.printUnknown(out);
                                    }
                                }

                                if (names != null && names.length != 0) {
                                    for(int i = 0; i < names.length; ++i) {
                                        out.println(names[i]);
                                    }
                                }
                            }
                        }
                    } catch (Exception var17) {
                        var17.printStackTrace();
                    }

                    if (this.concurrentLocks) {
                        JavaThread jthread = (JavaThread)this.proxyToThread.get(th);
                        if (jthread != null) {
                            concLocksPrinter.print(jthread, out);
                        }
                    }
                }
            } else if (this.getDebugeeType() == 2) {
                out.println("remote configuration is not yet implemented");
            } else {
                out.println("not yet implemented (debugger does not support CDebugger)!");
            }

        }
    }

    public static void main(String[] args) throws Exception {
        PStackTool t = new PStackTool();
        t.execute(args);
    }

    private void initJFrameCache() {
        this.jframeCache = new HashMap();
        this.proxyToThread = new HashMap();
        Threads threads = VM.getVM().getThreads();

        for(JavaThread cur = threads.first(); cur != null; cur = cur.next()) {
            ArrayList tmp = new ArrayList(10);

            try {
                for(JavaVFrame vf = cur.getLastJavaVFrameDbg(); vf != null; vf = vf.javaSender()) {
                    tmp.add(vf);
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            JavaVFrame[] jvframes = new JavaVFrame[tmp.size()];
            System.arraycopy(tmp.toArray(), 0, jvframes, 0, jvframes.length);
            this.jframeCache.put(cur.getThreadProxy(), jvframes);
            this.proxyToThread.put(cur.getThreadProxy(), cur);
        }

    }

    private void printUnknown(PrintStream out) {
        out.println("\t????????");
    }

    private String[] getJavaNames(ThreadProxy th, Address fp) {
        if (fp == null) {
            return null;
        } else {
            JavaVFrame[] jvframes = (JavaVFrame[])((JavaVFrame[])this.jframeCache.get(th));
            if (jvframes == null) {
                return null;
            } else {
                List names = new ArrayList(10);

                for(int fCount = 0; fCount < jvframes.length; ++fCount) {
                    JavaVFrame vf = jvframes[fCount];
                    Frame f = vf.getFrame();
                    if (fp.equals(f.getFP())) {
                        StringBuffer sb = new StringBuffer();
                        Method method = vf.getMethod();
                        sb.append("* ");
                        sb.append(method.externalNameAndSignature());
                        sb.append(" bci:" + vf.getBCI());
                        int lineNumber = method.getLineNumberFromBCI(vf.getBCI());
                        if (lineNumber != -1) {
                            sb.append(" line:" + lineNumber);
                        }

                        if (this.verbose) {
                            sb.append(" Method*:" + method.getAddress());
                        }

                        if (vf.isCompiledFrame()) {
                            sb.append(" (Compiled frame");
                            if (vf.isDeoptimized()) {
                                sb.append(" [deoptimized]");
                            }
                        } else if (vf.isInterpretedFrame()) {
                            sb.append(" (Interpreted frame");
                        }

                        if (vf.mayBeImpreciseDbg()) {
                            sb.append("; information may be imprecise");
                        }

                        sb.append(")");
                        names.add(sb.toString());
                    }
                }

                String[] res = new String[names.size()];
                System.arraycopy(names.toArray(), 0, res, 0, res.length);
                return res;
            }
        }
    }
}
