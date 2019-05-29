package beifengtz.vmconsole.tools.jstack;

import beifengtz.vmconsole.entity.JStackResult;
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
import sun.jvm.hotspot.utilities.PlatformInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    /**
     * 打印流接收输出结果
     *
     * @param out 打印流
     */
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

    /**
     * 用JStackResult对象接收
     * @param jStackResult {@link JStackResult}
     */
    public void run(JStackResult jStackResult) throws Exception {
        Debugger dbg = this.getAgent().getDebugger();
        this.run(jStackResult,dbg);
    }

    public void run(JStackResult jStackResult, Debugger dbg) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        PrintStream out = new PrintStream(outputStream);

        if (PlatformInfo.getOS().equals("darwin")) {
            closeStream(out,outputStream);
            throw new Exception("Not available on Darwin");
        } else {
            CDebugger cdbg = dbg.getCDebugger();
            if (cdbg != null) {
                ConcurrentLocksPrinter concLocksPrinter = null;
                this.initJFrameCache();
                if (this.concurrentLocks) {
                    concLocksPrinter = new ConcurrentLocksPrinter();
                }

                //  打印死锁堆栈信息
                try {
                    DeadlockDetector.print(out);
                    jStackResult.setDeadlocks(out.toString());
                    //  重用缓存区
                    outputStream.reset();
                } catch (Exception var16) {
                    closeStream(out,outputStream);
                    throw new Exception("can't print deadlock information: " + var16.getMessage());
                }

                //  获取线程列表，迭代遍历
                List l = cdbg.getThreadList();
                boolean cdbgCanDemangle = cdbg.canDemangle();
                Iterator itr = l.iterator();

                ArrayList<StringBuilder> jniStack = new ArrayList<>(l.size());

                while(itr.hasNext()) {
                    ThreadProxy th = (ThreadProxy)itr.next();
                    StringBuilder tempInfo = new StringBuilder();
                    try {
                        CFrame f = cdbg.topFrameForThread(th);


                        //  循环遍历并保存信息
                        for(; f != null; f = f.sender(th)) {
                            ClosestSymbol sym = f.closestSymbolToPC();
                            Address pc = f.pc();
                            tempInfo.append(pc);
                            tempInfo.append("\t");
                            if (sym != null) {
                                String name = sym.getName();
                                if (cdbgCanDemangle) {
                                    name = cdbg.demangle(name);
                                }
                                tempInfo.append(name);
                                long diff = sym.getOffset();
                                if (diff != 0L) {
                                    tempInfo.append(" + 0x");
                                    tempInfo.append(Long.toHexString(diff));
                                }
                                tempInfo.append("\n");
                            } else {
                                String[] names = null;
                                Interpreter interp = VM.getVM().getInterpreter();
                                if (interp.contains(pc)) {
                                    names = this.getJavaNames(th, f.localVariableBase());
                                    if (names == null || names.length == 0) {
                                        tempInfo.append("<interpreter> ");
                                        InterpreterCodelet ic = interp.getCodeletContaining(pc);
                                        if (ic != null) {
                                            String desc = ic.getDescription();
                                            if (desc != null) {
                                                tempInfo.append(desc);
                                            }
                                        }
                                        tempInfo.append("\n");
                                    }
                                } else {
                                    CodeCache c = VM.getVM().getCodeCache();
                                    if (c.contains(pc)) {
                                        CodeBlob cb = c.findBlobUnsafe(pc);
                                        if (cb.isNMethod()) {
                                            names = this.getJavaNames(th, f.localVariableBase());
                                            if (names == null || names.length == 0) {
                                                tempInfo.append("<Unknown compiled code>");
                                            }
                                        } else if (cb.isBufferBlob()) {
                                            tempInfo.append("<StubRoutines>");
                                        } else if (cb.isRuntimeStub()) {
                                            tempInfo.append("<RuntimeStub>");
                                        } else if (cb.isDeoptimizationStub()) {
                                            tempInfo.append("<DeoptimizationStub>");
                                        } else if (cb.isUncommonTrapStub()) {
                                            tempInfo.append("<UncommonTrap>");
                                        } else if (cb.isExceptionStub()) {
                                            tempInfo.append("<ExceptionStub>");
                                        } else if (cb.isSafepointStub()) {
                                            tempInfo.append("<SafepointStub>");
                                        } else {
                                            tempInfo.append("<Unknown code blob>");
                                        }
                                    } else {
                                        tempInfo.append("\t????????");
                                    }
                                }

                                if (names != null && names.length != 0) {
                                    for(int i = 0; i < names.length; ++i) {
                                        tempInfo.append(names[i]);
                                    }
                                }
                            }
                        }
                        jniStack.add(tempInfo);
                    } catch (Exception var17) {
                        var17.printStackTrace();
                    }

                    //  并发锁
                    if (this.concurrentLocks) {
                        JavaThread jthread = (JavaThread)this.proxyToThread.get(th);
                        if (jthread != null) {
                            concLocksPrinter.print(jthread, out);
                            jStackResult.setConcurrentLocks(outputStream.toString());
                            outputStream.reset();
                        }
                    }
                }
                jStackResult.setJniStack(jniStack);
            } else if (this.getDebugeeType() == 2) {
                closeStream(out,outputStream);
                throw new Exception("remote configuration is not yet implemented");
            } else {
                closeStream(out,outputStream);
                throw new Exception("not yet implemented (debugger does not support CDebugger!");
            }
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
