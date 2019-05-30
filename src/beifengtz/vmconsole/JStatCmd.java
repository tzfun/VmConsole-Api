package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jstat.*;
import beifengtz.vmconsole.tools.jstat.JStatTool;
import sun.jvmstat.monitor.*;
import sun.jvmstat.monitor.event.HostEvent;
import sun.jvmstat.monitor.event.HostListener;
import sun.jvmstat.monitor.event.VmStatusChangeEvent;
import sun.tools.jstat.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole</p>
 * Created in 16:41 2019/5/27
 *
 * <p>jstat命令介绍：</p>
 *
 * <p>Jstat用于监控基于HotSpot的JVM，对其堆的使用情况进行实时的命令行的统计，
 * <p>使用jstat我们可以对指定的JVM做如下监控：</p>
 *
 * <p>- 类的加载及卸载情况</p>
 * <p>- 查看新生代、老生代及持久代的容量及使用情况</p>
 * <p>- 查看新生代、老生代及持久代的垃圾收集情况，包括垃圾回收的次数及垃圾回收所占用的时间</p>
 * <p>- 查看新生代中Eden区及Survior区中容量及分配情况等</p>
 *
 * <p>jstat工具特别强大，它有众多的可选项，通过提供多种不同的监控维度，使我们
 * 可以从不同的维度来了解到当前JVM堆的使用情况。详细查看堆内各个部分的使用量，
 * 使用的时候必须加上待统计的Java进程号，可选的不同维度参数以及可选的统计频率参数。</p>
 *
 */
public class JStatCmd {
    private static Arguments arguments;

    /**
     * <p>封装<code>jstat -list</code>命令</p>
     *
     * @return JStatResult
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResult list() throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-list"});
        if (r == null) {
            throw new NullPointerException();
        } else {
            return r.get(0);
        }
    }

    /**
     * 封装jstat -snap [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResult
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResult snap(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-snap", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            return r.get(0);
        }
    }

    /**
     * <p>封装<code>jstat -class [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForClass
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForClass clazz(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-class", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForClass) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -class [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForClass集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForClass> clazz(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-class",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForClass> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForClass) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gc [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGc
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGc gc(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gc", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGc) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gc [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGc集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGc> gc(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gc",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGc> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGc) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -compiler [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForCompiler
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForCompiler compiler(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-compiler", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForCompiler) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -compiler [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForCompiler集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForCompiler> compiler(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-compiler",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForCompiler> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForCompiler) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gccapacity [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcCapacity
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGcCapacity gcCapacity(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gccapacity", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGcCapacity) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gccapacity [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGcCapacity集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGcCapacity> gcCapacity(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gccapacity",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGcCapacity> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGcCapacity) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcnew [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcNew
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGcNew gcNew(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcnew", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGcNew) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcnew [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGcNew集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGcNew> gcNew(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcnew",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGcNew> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGcNew) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcnewcapacity [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcNewCapacity
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGcNewCapacity gcNewCapacity(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcnewcapacity", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGcNewCapacity) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcnewcapacity [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGcNewCapacity集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGcNewCapacity> gcNewCapacity(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcnewcapacity",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGcNewCapacity> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGcNewCapacity) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcold [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcOld
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGcOld gcOld(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcold", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGcOld) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcold [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGcOld集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGcOld> gcOld(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcold",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGcOld> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGcOld) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcoldcapacity [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcOldCapacity
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGcOldCapacity gcOldCapacity(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcoldcapacity", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGcOldCapacity) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcoldcapacity [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGcOldCapacity集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGcOldCapacity> gcOldCapacitiy(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcoldcapacity",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGcOldCapacity> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGcOldCapacity) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcmetacapacity [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcMetaCapacity
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGcMetaCapacity gcMetaCapacity(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcmetacapacity", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGcMetaCapacity) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcmetacapacity [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGcMetaCapacity集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGcMetaCapacity> gcMetaCapacitiy(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcmetacapacity",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGcMetaCapacity> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGcMetaCapacity) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcutil [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcUtil
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForGcUtil gcUtil(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcutil", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForGcUtil) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -gcutil [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForGcMetaCapacity集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForGcUtil> gcUtil(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-gcutil",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForGcUtil> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForGcUtil) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -printcompilation [vmid]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForCompilation
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static JStatResultForCompilation printCompilation(int vmId) throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-printcompilation", String.valueOf(vmId)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                return (JStatResultForCompilation) r.get(0);
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>封装<code>jstat -printcompilation [vmid] [interval] [count]</code>命令</p>
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return JStatResultForCompilation集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws NullPointerException 空指针异常
     */
    public static ArrayList<JStatResultForCompilation> printCompilation(int vmId, long interval, int count)
            throws IllegalArgumentException, NullPointerException {
        ArrayList<JStatResult> r = run(new String[]{"-printcompilation",
                String.valueOf(vmId),
                String.valueOf(interval),
                String.valueOf(count)});
        if (r == null) {
            throw new NullPointerException();
        } else {
            try {
                ArrayList<JStatResultForCompilation> res = new ArrayList<>(count);
                for (JStatResult j : r) {
                    res.add((JStatResultForCompilation) j);
                }
                return res;
            } catch (ClassFormatError e) {
                e.printStackTrace();
                throw new NullPointerException();
            }
        }
    }

    /**
     * <p>jstat命令执行函数</p>
     *
     * @param var0 命令参数
     * @return {@link JStatResult}的集合
     * @throws IllegalArgumentException 参数非法异常
     */
    public static ArrayList<JStatResult> run(String[] var0) throws IllegalArgumentException {
        //  转义jStat命令参数
        arguments = new Arguments(var0);

        if (arguments.isHelp()) {
            Arguments.printUsage(System.out);
            arguments = null;
            return null;
        }

        //  是否是获取命令参数列表
        if (arguments.isOptions()) {
            OptionLister var1 = new OptionLister(arguments.optionsSources());
            var1.print(System.out);
            arguments = null;
            return null;
        }
        ArrayList<JStatResult> jStatResults = null;
        try {

            if (arguments.isList()) {
                //  获取虚拟机状态信息名字列表
                jStatResults = names();
            } else if (arguments.isSnap()) {
                //  获取虚拟机状态信息名字及值列表
                jStatResults = snapShot();
            } else {
                //  其他命令（class、compiler、gc等）
                jStatResults = samples();
            }
        } catch (MonitorException var4) {
            if (var4.getMessage() != null) {
                System.err.println(var4.getMessage());
            } else {
                Throwable var2 = var4.getCause();
                if (var2 != null && var2.getMessage() != null) {
                    System.err.println(var2.getMessage());
                } else {
                    var4.printStackTrace();
                }
            }
        }

        return jStatResults;
    }

    /**
     * <p>有返回结果的获取虚拟机状态信息，将所有信息存入{@link JStatResult}的集合</p>
     * <p>
     * <p>集合中只有<strong>一个</strong>JStatResult对象</p>
     *
     * @return ArrayList<JStatResult> {@link JStatResult}的集合
     * @throws MonitorException 监控异常
     */
    static ArrayList<JStatResult> names() throws MonitorException {
        VmIdentifier var0 = arguments.vmId();
        int var1 = arguments.sampleInterval();
        MonitoredHost var2 = MonitoredHost.getMonitoredHost(var0);
        MonitoredVm var3 = null;
        try {
            var3 = var2.getMonitoredVm(var0, var1);
        } catch (MonitorException e) {
            e.printStackTrace();
        }
        JStatTool var4 = new JStatTool(var3);

        //  仅返回一个结果列表
        ArrayList<JStatResult> resList = new ArrayList<>(1);
        //  初始化结果集
        JStatResult jStatResult = new JStatResult();
        jStatResult.setVmId(var0.getLocalVmId());
        resList.add(jStatResult);

        var4.printNames(arguments.counterNames(),
                arguments.comparator(),
                arguments.showUnsupported(),
                System.out,
                resList);
        var2.detach(var3);
        return resList;
    }

    /**
     * <p>有返回结果的获取虚拟机状态和相应值信息，将所有信息和值存入{@link JStatResult}的集合</p>
     * <p>
     * <p>集合中只有<strong>一个</strong>JStatResult对象</p>
     *
     * @return ArrayList<JStatResult> {@link JStatResult}的集合
     * @throws MonitorException 监控异常
     */
    static ArrayList<JStatResult> snapShot() throws MonitorException {
        VmIdentifier var0 = arguments.vmId();
        int var1 = arguments.sampleInterval();
        MonitoredHost var2 = MonitoredHost.getMonitoredHost(var0);
        MonitoredVm var3 = var2.getMonitoredVm(var0, var1);
        JStatTool var4 = new JStatTool(var3);

        //  仅返回一个结果列表
        ArrayList<JStatResult> resList = new ArrayList<>(1);
        //  初始化结果集
        JStatResult jStatResult = new JStatResult();
        jStatResult.setVmId(var0.getLocalVmId());
        resList.add(jStatResult);

        var4.printSnapShot(arguments.counterNames(),
                arguments.comparator(),
                arguments.isVerbose(),
                arguments.showUnsupported(),
                System.out,
                resList);
        var2.detach(var3);
        return resList;
    }

    /**
     * <p>有返回结果的获取虚拟机信息，返回的实际对象根据命令类型进行不同的封装，
     * 但是返回的集合全部由其父类{@link JStatResult}代替，如果要获取相应对象需要进行向下转型</p>
     * <p>
     * <p>-class: {@link JStatResultForClass}</p>
     * <p>-gc: {@link JStatResultForGc}</p>
     * <p>-compiler: {@link JStatResultForCompiler}</p>
     * <p>-compilation: {@link JStatResultForCompilation}</p>
     * <p>-gccapacity: {@link JStatResultForGcCapacity}</p>
     * <p>-gcmetacapacity: {@link JStatResultForGcMetaCapacity}</p>
     * <p>-gcnew: {@link JStatResultForGcNew}</p>
     * <p>-gcnewcapacity: {@link JStatResultForGcNewCapacity}</p>
     * <p>-gcold: {@link JStatResultForGcOld}</p>
     * <p>-gcoldcapacity: {@link JStatResultForGcOldCapacity}</p>
     * <p>-gcutil: {@link JStatResultForGcUtil}</p>
     * <p>
     *
     * <p>集合中{@link JStatResult}对象的个数根据命令参数count决定，
     * 如果没有传入count命令，则默认返回集合的容量为一</p>
     *
     * @return ArrayList<JStatResult> {@link JStatResult}的集合
     * @throws MonitorException 监控异常
     */
    static ArrayList<JStatResult> samples() throws MonitorException {
        //  获取虚拟机认证码
        final VmIdentifier var0 = arguments.vmId();
        //  命令执行间隔时间，单位m或者ms
        int var1 = arguments.sampleInterval();

        final MonitoredHost var2 = MonitoredHost.getMonitoredHost(var0);
        //  获取虚拟机实例对象
        MonitoredVm var3 = var2.getMonitoredVm(var0, var1);

        //  jStat工具
        final JStatTool var4 = new JStatTool(var3);

        Object var5 = null;

        //  是否是特殊参数(-class、-gc等)
        if (arguments.isSpecialOption()) {
            OptionFormat var6 = arguments.optionFormat();
            var5 = new OptionOutputFormatter(var3, var6);
        } else {
            List var10 = var3.findByPattern(arguments.counterNames());
            Collections.sort(var10, arguments.comparator());
            ArrayList var7 = new ArrayList();
            Iterator var8 = var10.iterator();

            while (true) {
                while (var8.hasNext()) {
                    Monitor var9 = (Monitor) var8.next();
                    if (!var9.isSupported() && !arguments.showUnsupported()) {
                        var8.remove();
                    } else if (var9.getVariability() == Variability.CONSTANT) {
                        var8.remove();
                        if (arguments.printConstants()) {
                            var7.add(var9);
                        }
                    } else if (var9.getUnits() == Units.STRING && !arguments.printStrings()) {
                        var8.remove();
                    }
                }

                if (!var7.isEmpty()) {
                    var4.printList(var7, arguments.isVerbose(), arguments.showUnsupported(), System.out);
                    if (!var10.isEmpty()) {
                        System.out.println();
                    }
                }

                if (var10.isEmpty()) {
                    var2.detach(var3);
                    return null;
                }

                var5 = new RawOutputFormatter(var10, arguments.printStrings());
                break;
            }
        }

        //  注册新的进程去关闭Hook程序
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                var4.stopLogging();
            }
        });

        //  虚拟机监听器，检查异常情况
        HostListener var11 = new HostListener() {
            //  状态更改，停止打印
            public void vmStatusChanged(VmStatusChangeEvent var1) {
                Integer var2x = new Integer(var0.getLocalVmId());
                if (var1.getTerminated().contains(var2x)) {
                    var4.stopLogging();
                } else if (!var1.getActive().contains(var2x)) {
                    var4.stopLogging();
                }

            }

            // 关闭连接，停止打印
            public void disconnected(HostEvent var1) {
                if (var2 == var1.getMonitoredHost()) {
                    var4.stopLogging();
                }

            }
        };
        if (var0.getLocalVmId() != 0) {
            // 注入监听器
            var2.addHostListener(var11);
        }
        ArrayList<JStatResult> resList = new ArrayList<>(arguments.sampleCount());
        //  循环处理结果
        var4.logSamples((OutputFormatter) var5,
                arguments.headerRate(),
                arguments.sampleInterval(),
                arguments.sampleCount(),
                System.out,
                var0.getLocalVmId(),
                arguments.specialOption(),
                resList);
        if (var11 != null) {
            //  结果处理成功，移除监听器
            var2.removeHostListener(var11);
        }
        //  从Host中分离该虚拟机实例
        var2.detach(var3);
        return resList;
    }
}
