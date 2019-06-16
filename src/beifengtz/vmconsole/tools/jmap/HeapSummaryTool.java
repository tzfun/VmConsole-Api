package beifengtz.vmconsole.tools.jmap;

import beifengtz.vmconsole.entity.jmap.*;
import beifengtz.vmconsole.exception.NotAvailableException;
import beifengtz.vmconsole.tools.MyTool;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.gc_implementation.g1.G1CollectedHeap;
import sun.jvm.hotspot.gc_implementation.g1.G1MonitoringSupport;
import sun.jvm.hotspot.gc_implementation.g1.HeapRegion;
import sun.jvm.hotspot.gc_implementation.g1.HeapRegionSetBase;
import sun.jvm.hotspot.gc_implementation.parallelScavenge.PSOldGen;
import sun.jvm.hotspot.gc_implementation.parallelScavenge.PSYoungGen;
import sun.jvm.hotspot.gc_implementation.parallelScavenge.ParallelScavengeHeap;
import sun.jvm.hotspot.gc_implementation.shared.MutableSpace;
import sun.jvm.hotspot.gc_interface.CollectedHeap;
import sun.jvm.hotspot.memory.*;
import sun.jvm.hotspot.oops.Instance;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.OopField;
import sun.jvm.hotspot.runtime.VM;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jmap.VmConsole-Api</p>
 * Created in 13:17 2019/6/16
 */
public class HeapSummaryTool extends MyTool {
    private static String alignment = "   ";
    private static final double FACTOR = 1048576.0D;
    private static boolean hasInit;
    private PrintStream ps = System.out;
    private JMapForHeapResult jMapForHeapResult;
    private boolean resultWithObject;  //  是否以对象形式接收，true用对象，false用打印流

    public HeapSummaryTool() {
    }

    public HeapSummaryTool(JVMDebugger d) {
        super(d);
        resultWithObject = false;
    }

    public HeapSummaryTool(JMapForHeapResult jMapForHeapResult) {
        this.jMapForHeapResult = jMapForHeapResult;
        hasInit = true;
        resultWithObject = true;
    }
    public HeapSummaryTool(PrintStream ps) {
        this.ps = ps;
        hasInit = true;
        resultWithObject = false;
    }

    public static void init(String[] args, JMapForHeapResult jMapForHeapResult) throws Exception{
        hasInit = false;
        HeapSummaryTool hs = new HeapSummaryTool(jMapForHeapResult);
        hs.execute(args, jMapForHeapResult);
    }

    public void run() {
        CollectedHeap heap = VM.getVM().getUniverse().heap();
        VM.Flag[] flags = VM.getVM().getCommandLineFlags();
        Map flagMap = new HashMap();
        if (flags == null) {
            try{
                throw new NotAvailableException("command line flags are not available");
            }catch (NotAvailableException e){
                e.printStackTrace();
            }
        } else {
            for(int f = 0; f < flags.length; ++f) {
                flagMap.put(flags[f].getName(), flags[f]);
            }
        }
        if (resultWithObject){
            //  对象接收
            this.run(heap,flagMap, jMapForHeapResult);
        }else {
            //  打印流接收
            this.run(heap,flagMap,ps);
        }
    }

    /**
     * 用打印流处理结果
     */
    private void run(CollectedHeap heap,Map flagMap,PrintStream ps){
        ps.println();
        this.printGCAlgorithm(flagMap);
        ps.println();
        ps.println("Heap Configuration:");
        this.printValue("MinHeapFreeRatio         = ", this.getFlagValue("MinHeapFreeRatio", flagMap));
        this.printValue("MaxHeapFreeRatio         = ", this.getFlagValue("MaxHeapFreeRatio", flagMap));
        this.printValMB("MaxHeapSize              = ", this.getFlagValue("MaxHeapSize", flagMap));
        this.printValMB("NewSize                  = ", this.getFlagValue("NewSize", flagMap));
        this.printValMB("MaxNewSize               = ", this.getFlagValue("MaxNewSize", flagMap));
        this.printValMB("OldSize                  = ", this.getFlagValue("OldSize", flagMap));
        this.printValue("NewRatio                 = ", this.getFlagValue("NewRatio", flagMap));
        this.printValue("SurvivorRatio            = ", this.getFlagValue("SurvivorRatio", flagMap));
        this.printValMB("MetaspaceSize            = ", this.getFlagValue("MetaspaceSize", flagMap));
        this.printValMB("CompressedClassSpaceSize = ", this.getFlagValue("CompressedClassSpaceSize", flagMap));
        this.printValMB("MaxMetaspaceSize         = ", this.getFlagValue("MaxMetaspaceSize", flagMap));
        this.printValMB("G1HeapRegionSize         = ", HeapRegion.grainBytes());
        ps.println();
        ps.println("Heap Usage:");
        long edenRegionNum;
        if (heap instanceof SharedHeap) {
            SharedHeap sharedHeap = (SharedHeap)heap;
            if (sharedHeap instanceof GenCollectedHeap) {
                GenCollectedHeap genHeap = (GenCollectedHeap)sharedHeap;

                for(int n = 0; n < genHeap.nGens(); ++n) {
                    Generation gen = genHeap.getGen(n);
                    if (gen instanceof DefNewGeneration) {
                        ps.println("New Generation (Eden + 1 Survivor Space):");
                        this.printGen(gen);
                        ContiguousSpace eden = ((DefNewGeneration)gen).eden();
                        ps.println("Eden Space:");
                        this.printSpace(eden);
                        ContiguousSpace from = ((DefNewGeneration)gen).from();
                        ps.println("From Space:");
                        this.printSpace(from);
                        ContiguousSpace to = ((DefNewGeneration)gen).to();
                        ps.println("To Space:");
                        this.printSpace(to);
                    } else {
                        ps.println(gen.name() + ":");
                        this.printGen(gen);
                    }
                }
            } else {
                if (!(sharedHeap instanceof G1CollectedHeap)) {
                    throw new RuntimeException("unknown SharedHeap type : " + heap.getClass());
                }

                G1CollectedHeap g1h = (G1CollectedHeap)sharedHeap;
                G1MonitoringSupport g1mm = g1h.g1mm();
                edenRegionNum = g1mm.edenRegionNum();
                long survivorRegionNum = g1mm.survivorRegionNum();
                HeapRegionSetBase oldSet = g1h.oldSet();
                HeapRegionSetBase humongousSet = g1h.humongousSet();
                long oldRegionNum = oldSet.count().length() + humongousSet.count().capacity() / HeapRegion.grainBytes();
                this.printG1Space("G1 Heap:", g1h.n_regions(), g1h.used(), g1h.capacity());
                ps.println("G1 Young Generation:");
                this.printG1Space("Eden Space:", edenRegionNum, g1mm.edenUsed(), g1mm.edenCommitted());
                this.printG1Space("Survivor Space:", survivorRegionNum, g1mm.survivorUsed(), g1mm.survivorCommitted());
                this.printG1Space("G1 Old Generation:", oldRegionNum, g1mm.oldUsed(), g1mm.oldCommitted());
            }
        } else {
            if (!(heap instanceof ParallelScavengeHeap)) {
                throw new RuntimeException("unknown CollectedHeap type : " + heap.getClass());
            }

            ParallelScavengeHeap psh = (ParallelScavengeHeap)heap;
            PSYoungGen youngGen = psh.youngGen();
            this.printPSYoungGen(youngGen);
            PSOldGen oldGen = psh.oldGen();
            edenRegionNum = oldGen.capacity() - oldGen.used();
            ps.println("PS Old Generation");
            this.printValMB("capacity = ", oldGen.capacity());
            this.printValMB("used     = ", oldGen.used());
            this.printValMB("free     = ", edenRegionNum);
            ps.println(alignment + (double)oldGen.used() * 100.0D / (double)oldGen.capacity() + "% used");
        }
        this.printInternStringStatistics();
    }

    /**
     * 用对象处理结果
     */
    private void run(CollectedHeap heap, Map flagMap, JMapForHeapResult jMapForHeapResult){
        jMapForHeapResult.getHeapConf().put("MinHeapFreeRatio",this.getFlagValue("MinHeapFreeRatio", flagMap));
        jMapForHeapResult.getHeapConf().put("MaxHeapFreeRatio",this.getFlagValue("MaxHeapFreeRatio", flagMap));
        jMapForHeapResult.getHeapConf().put("MaxHeapSize",this.getFlagValue("MaxHeapSize", flagMap));
        jMapForHeapResult.getHeapConf().put("NewSize",this.getFlagValue("NewSize", flagMap));
        jMapForHeapResult.getHeapConf().put("MaxNewSize",this.getFlagValue("MaxNewSize", flagMap));
        jMapForHeapResult.getHeapConf().put("OldSize",this.getFlagValue("OldSize", flagMap));
        jMapForHeapResult.getHeapConf().put("NewRatio",this.getFlagValue("NewRatio", flagMap));
        jMapForHeapResult.getHeapConf().put("SurvivorRatio",this.getFlagValue("SurvivorRatio", flagMap));
        jMapForHeapResult.getHeapConf().put("MetaspaceSize",this.getFlagValue("MetaspaceSize", flagMap));
        jMapForHeapResult.getHeapConf().put("CompressedClassSpaceSize",this.getFlagValue("CompressedClassSpaceSize", flagMap));
        jMapForHeapResult.getHeapConf().put("MaxMetaspaceSize",this.getFlagValue("MaxMetaspaceSize", flagMap));
        jMapForHeapResult.getHeapConf().put("G1HeapRegionSize",this.getFlagValue("G1HeapRegionSize", flagMap));

        long edenRegionNum;
        //  这里根据不同垃圾收集器类型来进行处理
        if (heap instanceof SharedHeap) {
            SharedHeap sharedHeap = (SharedHeap)heap;
            if (sharedHeap instanceof GenCollectedHeap) {
                GenCollectedHeap genHeap = (GenCollectedHeap)sharedHeap;
                HeapForGen heapForGen = jMapForHeapResult.getHeapUsage().getHeapForGen() == null ? new HeapForGen() : jMapForHeapResult.getHeapUsage().getHeapForGen();
                for(int n = 0; n < genHeap.nGens(); ++n) {
                    Generation gen = genHeap.getGen(n);
                    if (gen instanceof DefNewGeneration) {
                        heapForGen.setNewGen(this.dealMutableSpace("Eden + 1 Survivor Space",gen));
                        ContiguousSpace eden = ((DefNewGeneration)gen).eden();
                        heapForGen.setYoungEden(this.dealMutableSpace("Eden Space",eden));
                        ContiguousSpace from = ((DefNewGeneration)gen).from();
                        heapForGen.setYoungFrom(this.dealMutableSpace("From Survivor Space",from));
                        ContiguousSpace to = ((DefNewGeneration)gen).to();
                        heapForGen.setYoungTo(this.dealMutableSpace("To Survivor Space",to));
                    } else {
                        heapForGen.setOldGen(this.dealMutableSpace(gen.name(),gen));
                    }
                    jMapForHeapResult.getHeapUsage().setHeapForGen(heapForGen);
                }
            } else {
                if (!(sharedHeap instanceof G1CollectedHeap)) {
                    throw new RuntimeException("unknown SharedHeap type : " + heap.getClass());
                }

                //  如果是G1垃圾收集器，内存分布处理如下
                G1CollectedHeap g1h = (G1CollectedHeap)sharedHeap;
                G1MonitoringSupport g1mm = g1h.g1mm();
                edenRegionNum = g1mm.edenRegionNum();
                long survivorRegionNum = g1mm.survivorRegionNum();
                HeapRegionSetBase oldSet = g1h.oldSet();
                HeapRegionSetBase humongousSet = g1h.humongousSet();
                long oldRegionNum = oldSet.count().length() + humongousSet.count().capacity() / HeapRegion.grainBytes();
                HeapForG1 heapForG1 = new HeapForG1();

                heapForG1.setHeap(this.instanceG1Space("G1 Heap:", g1h.n_regions(), g1h.used(), g1h.capacity()));
                heapForG1.setEden(this.instanceG1Space("Eden Space:", edenRegionNum, g1mm.edenUsed(), g1mm.edenCommitted()));
                heapForG1.setSurvivor(this.instanceG1Space("Survivor Space:", survivorRegionNum, g1mm.survivorUsed(), g1mm.survivorCommitted()));
                heapForG1.setOld(this.instanceG1Space("G1 Old Generation:", oldRegionNum, g1mm.oldUsed(), g1mm.oldCommitted()));
                jMapForHeapResult.getHeapUsage().setHeapForG1(heapForG1);
            }
        } else {
            if (!(heap instanceof ParallelScavengeHeap)) {
                throw new RuntimeException("unknown CollectedHeap type : " + heap.getClass());
            }
            //  如果为null则创建新的HeapForGen
            HeapForGen heapForGen = jMapForHeapResult.getHeapUsage().getHeapForGen() == null ? new HeapForGen() : jMapForHeapResult.getHeapUsage().getHeapForGen();

            ParallelScavengeHeap psh = (ParallelScavengeHeap)heap;
            PSYoungGen youngGen = psh.youngGen();
            this.dealYoungGenForObj(youngGen,heapForGen);
            PSOldGen oldGen = psh.oldGen();
            edenRegionNum = oldGen.capacity() - oldGen.used();

            heapForGen.setOldGen(new HeapSpace(
                    "Old Generation",
                    String.valueOf(oldGen.capacity()),
                    String.valueOf(oldGen.used()),
                    String.valueOf(edenRegionNum),
                    (double)oldGen.used() * 100.0D / (double)oldGen.capacity() + "%"
            ));
            jMapForHeapResult.getHeapUsage().setHeapForGen(heapForGen);
        }
//        this.printInternStringStatistics();
    }

    private void printGCAlgorithm(Map flagMap) {
        long l = this.getFlagValue("UseParNewGC", flagMap);
        if (l == 1L) {
            ps.println("using parallel threads in the new generation.");
        }

        l = this.getFlagValue("UseTLAB", flagMap);
        if (l == 1L) {
            ps.println("using thread-local object allocation.");
        }

        l = this.getFlagValue("UseConcMarkSweepGC", flagMap);
        if (l == 1L) {
            ps.println("Concurrent Mark-Sweep GC");
        } else {
            l = this.getFlagValue("UseParallelGC", flagMap);
            if (l == 1L) {
                ps.print("Parallel GC ");
                l = this.getFlagValue("ParallelGCThreads", flagMap);
                ps.println("with " + l + " thread(s)");
            } else {
                l = this.getFlagValue("UseG1GC", flagMap);
                if (l == 1L) {
                    ps.print("Garbage-First (G1) GC ");
                    l = this.getFlagValue("ParallelGCThreads", flagMap);
                    ps.println("with " + l + " thread(s)");
                } else {
                    ps.println("Mark Sweep Compact GC");
                }
            }
        }
    }

    private void printPSYoungGen(PSYoungGen youngGen) {
        ps.println("PS Young Generation");
        MutableSpace eden = youngGen.edenSpace();
        ps.println("Eden Space:");
        this.printMutableSpace(eden);
        MutableSpace from = youngGen.fromSpace();
        ps.println("From Space:");
        this.printMutableSpace(from);
        MutableSpace to = youngGen.toSpace();
        ps.println("To Space:");
        this.printMutableSpace(to);
    }

    private void dealYoungGenForObj(PSYoungGen youngGen,HeapForGen heapForGen) {
        MutableSpace eden = youngGen.edenSpace();
        heapForGen.setYoungEden(this.dealMutableSpace("Eden Space",eden));
        MutableSpace from = youngGen.fromSpace();
        heapForGen.setYoungFrom(this.dealMutableSpace("From Space",from));
        MutableSpace to = youngGen.toSpace();
        heapForGen.setYoungTo(this.dealMutableSpace("To Space",to));
    }

    private void printMutableSpace(MutableSpace space) {
        this.printValMB("capacity = ", space.capacity());
        this.printValMB("used     = ", space.used());
        long free = space.capacity() - space.used();
        this.printValMB("free     = ", free);
        ps.println(alignment + (double)space.used() * 100.0D / (double)space.capacity() + "% used");
    }

    private HeapSpace dealMutableSpace(String name,MutableSpace space) {
        long free = space.capacity() - space.used();
        return new HeapSpace(name,
                countValMB(space.capacity()),
                countValMB(space.used()),
                countValMB(free),
                (double)space.used() * 100.0D / (double)space.capacity() + "%");
    }

    private HeapSpace dealMutableSpace(String name,ContiguousSpace space) {
        long free = space.capacity() - space.used();
        return new HeapSpace(name,
                countValMB(space.capacity()),
                countValMB(space.used()),
                countValMB(free),
                (double)space.used() * 100.0D / (double)space.capacity() + "%");
    }

    private HeapSpace dealMutableSpace(String name,Generation space) {
        long free = space.capacity() - space.used();
        return new HeapSpace(name,
                countValMB(space.capacity()),
                countValMB(space.used()),
                countValMB(free),
                (double)space.used() * 100.0D / (double)space.capacity() + "%");
    }

    private void printGen(Generation gen) {
        this.printValMB("capacity = ", gen.capacity());
        this.printValMB("used     = ", gen.used());
        this.printValMB("free     = ", gen.free());
        ps.println(alignment + (double)gen.used() * 100.0D / (double)gen.capacity() + "% used");
    }

    private void printSpace(ContiguousSpace space) {
        this.printValMB("capacity = ", space.capacity());
        this.printValMB("used     = ", space.used());
        this.printValMB("free     = ", space.free());
        ps.println(alignment + (double)space.used() * 100.0D / (double)space.capacity() + "% used");
    }

    private void printG1Space(String spaceName, long regionNum, long used, long capacity) {
        long free = capacity - used;
        ps.println(spaceName);
        this.printValue("regions  = ", regionNum);
        this.printValMB("capacity = ", capacity);
        this.printValMB("used     = ", used);
        this.printValMB("free     = ", free);
        double occPerc = capacity > 0L ? (double)used * 100.0D / (double)capacity : 0.0D;
        ps.println(alignment + occPerc + "% used");
    }

    private HeapSpace instanceG1Space(String spaceName, long regionNum, long used, long capacity){
        long free = capacity - used;
        double occPerc = capacity > 0L ? (double)used * 100.0D / (double)capacity : 0.0D;
        return new HeapSpace(spaceName,countValMB(regionNum),countValMB(capacity),countValMB(used),countValMB(free),occPerc + "%");
    }

    private void printValMB(String title, long value) {
        if (value < 0L) {
            ps.println(alignment + title + (value >>> 20) + " MB");
        } else {
            double mb = (double)value / 1048576.0D;
            ps.println(alignment + title + value + " (" + mb + "MB)");
        }
    }

    private String countValMB(long value) {
        if (value < 0L) {
            return (value >>> 20) + " MB";
        } else {
            double mb = (double)value / 1048576.0D;
            return  value + " (" + mb + "MB)";
        }
    }

    private void printValue(String title, long value) {
        ps.println(alignment + title + value);
    }

    private long getFlagValue(String name, Map flagMap) {
        VM.Flag f = (VM.Flag)flagMap.get(name);
        if (f != null) {
            if (f.isBool()) {
                return f.getBool() ? 1L : 0L;
            } else {
                return Long.parseLong(f.getValue());
            }
        } else {
            return -1L;
        }
    }

    private void printInternStringStatistics() {
        class StringStat implements StringTable.StringVisitor {
            private int count;
            private long size;
            private OopField stringValueField;

            StringStat() {
                VM vm = VM.getVM();
                SystemDictionary sysDict = vm.getSystemDictionary();
                InstanceKlass strKlass = SystemDictionary.getStringKlass();
                this.stringValueField = (OopField)strKlass.findField("value", "[C");
            }

            private long stringSize(Instance instance) {
                return instance.getObjectSize() + this.stringValueField.getValue(instance).getObjectSize();
            }

            public void visit(Instance str) {
                ++this.count;
                this.size += this.stringSize(str);
            }

            public void print() {
                ps.println(this.count + " interned Strings occupying " + this.size + " bytes.");
            }
        }

        StringStat stat = new StringStat();
        StringTable strTable = VM.getVM().getStringTable();
        strTable.stringsDo(stat);
        stat.print();
    }
}
