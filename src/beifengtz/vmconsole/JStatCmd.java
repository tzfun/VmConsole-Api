package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.*;
import beifengtz.vmconsole.util.JStatUtil;
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
 * <p>location: beifengtz.vmconsole.javase_learning</p>
 * Created in 16:41 2019/5/27
 */
public class JStatCmd {
    private static Arguments arguments;

    public static void main(String[] args) {
        System.out.println(list());
        System.out.println(snap(8208));
        System.out.println(clazz(8208));
        System.out.println(gc(8208));
        System.out.println(gcNew(8208));
        System.out.println(gcOld(8208));
        System.out.println(gcNewCapacity(8208));
        System.out.println(gcOldCapacity(8208));
        System.out.println(gcMetaCapacity(8208));
        System.out.println(compiler(8208));
        System.out.println(printCompilation(8208));
    }

    /**
     * 封装jstat -list命令
     *
     * @return JStatResult
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -class [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForClass
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -class [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForClass>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gc [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGc
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gc [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGc>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -compiler [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForCompiler
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -compiler [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForCompiler>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gccapacity [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcCapacity
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gccapacity [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGcCapacity>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcnew [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcNew
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcnew [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGcNew>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcnewcapacity [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcNewCapacity
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcnewcapacity [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGcNewCapacity>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcold [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcOld
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcold [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGcOld>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcoldcapacity [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcOldCapacity
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcoldcapacity [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGcOldCapacity>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcmetacapacity [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcMetaCapacity
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcmetacapacity [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGcMetaCapacity>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcutil [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForGcUtil
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -gcutil [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForGcMetaCapacity>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -printcompilation [vmid]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @return JStatResultForCompilation
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * 封装jstat -printcompilation [vmid] [interval] [count]命令
     *
     * @param vmId 虚拟机唯一识别id
     * @param interval 时间间隔，单位ms
     * @param count 返回条数
     * @return ArrayList<JStatResultForCompilation>
     * @throws IllegalArgumentException
     * @throws NullPointerException
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
     * jstat命令执行函数
     *
     * @param var0 命令参数
     * @return ArrayList<JStatResult> {@link JStatResult}的集合
     * @throws {@link IllegalArgumentException}
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
     * 有返回结果的获取虚拟机状态信息，
     * 将所有信息存入{@link JStatResult}的集合
     * <p>
     * 集合中只有<strong>一个</strong>JStatResult对象
     *
     * @return ArrayList<JStatResult> {@link JStatResult}的集合
     * @throws {@link MonitorException}
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
        JStatUtil var4 = new JStatUtil(var3);

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
     * 有返回结果的获取虚拟机状态和相应值信息，
     * 将所有信息和值存入{@link JStatResult}的集合
     * <p>
     * 集合中只有<strong>一个</strong>JStatResult对象
     *
     * @return ArrayList<JStatResult> {@link JStatResult}的集合
     * @throws {@link MonitorException}
     */
    static ArrayList<JStatResult> snapShot() throws MonitorException {
        VmIdentifier var0 = arguments.vmId();
        int var1 = arguments.sampleInterval();
        MonitoredHost var2 = MonitoredHost.getMonitoredHost(var0);
        MonitoredVm var3 = var2.getMonitoredVm(var0, var1);
        JStatUtil var4 = new JStatUtil(var3);

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
     * 有返回结果的获取虚拟机信息，返回的实际对象根据命令类型进行不同的封装，
     * 但是返回的集合全部由其父类{@link JStatResult}代替，如果要获取相应对象需要进行向下转型
     * <p>
     * -class: {@link JStatResultForClass}
     * -gc: {@link JStatResultForGc}
     * -compiler: {@link JStatResultForCompiler}
     * -compilation: {@link JStatResultForCompilation}
     * -gccapacity: {@link JStatResultForGcCapacity}
     * -gcmetacapacity: {@link JStatResultForGcMetaCapacity}
     * -gcnew: {@link JStatResultForGcNew}
     * -gcnewcapacity: {@link JStatResultForGcNewCapacity}
     * -gcold: {@link JStatResultForGcOld}
     * -gcoldcapacity: {@link JStatResultForGcOldCapacity}
     * -gcutil: {@link JStatResultForGcUtil}
     * <p>
     * 集合中{@link JStatResult}对象的个数根据命令参数count决定，
     * 如果没有传入count命令，则默认返回集合的容量为一
     *
     * @return ArrayList<JStatResult> {@link JStatResult}的集合
     * @throws {@link MonitorException}
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
        final JStatUtil var4 = new JStatUtil(var3);

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
