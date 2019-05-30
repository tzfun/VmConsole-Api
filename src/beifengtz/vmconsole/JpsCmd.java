package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jps.JpsResult;
import sun.jvmstat.monitor.*;
import sun.tools.jps.Arguments;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;


/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.javase_learning</p>
 * Created in 12:20 2019/5/27
 *
 * <p>虚拟机进程信息查询工具类，该类中提供直接执行命令的方法，但是建议您使用已经封装好的方法。</p>
 *
 * <p>jps命令介绍：</p>
 *
 * <p>用来查看基于HotSpot的JVM里面中，所有具有访问权限的Java进程的具体状态,
 *  包括进程ID，进程启动的路径及启动参数等等，与unix上的ps类似，只不过jps
 * 是用来显示java进程，可以把jps理解为ps的一个子集。</p>
 *
 * <p>使用jps时，如果没有指定hostid，它只会显示本地环境中所有的Java进程；
 * 如果指定了hostid，它就会显示指定hostid上面的java进程，不过这需要远程服
 * 务上开启了jstatd服务，可使用JStatCmd相关API。</p>
 *
 */
public class JpsCmd {
    private static Arguments arguments;

    /**
     * <p>封装命令：<code>jps -q</code></p>
     * <p>仅获取虚拟机的vmId</p>
     *
     * @return JpsResult的集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws MonitorException 监控异常
     */
    public static List<JpsResult> quit() throws IllegalArgumentException, MonitorException{
        return run(new String[]{"-q"});
    }

    /**
     * <p>封装命令：<code>jps -m</code></p>
     * <p>获取虚拟机的vmId和main函数的参数</p>
     *
     * @return JpsResult的集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws MonitorException 监控异常
     */
    public static List<JpsResult> withMainClassArgs() throws IllegalArgumentException, MonitorException{
        return run(new String[]{"-m"});
    }

    /**
     * <p>封装命令：<code>jps -l</code></p>
     * <p>获取虚拟机的vmId和主类或Jar的全名</p>
     *
     * @return JpsResult的集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws MonitorException 监控异常
     */
    public static List<JpsResult> withFullName() throws IllegalArgumentException, MonitorException{
        return run(new String[]{"-l"});
    }

    /**
     * <p>封装命令：<code>jps -v</code></p>
     * <p>获取虚拟机的vmId和虚拟机参数</p>
     *
     * @return JpsResult的集合
     * @throws IllegalArgumentException 非法参数异常
     * @throws MonitorException 监控异常
     */
    public static List<JpsResult> withVmArgs() throws IllegalArgumentException, MonitorException{
        return run(new String[]{"-v"});
    }

    /**
     * jps命令执行函数
     *
     * @param var0 String类型，传入jps命令的参数：
     *      <p>-q 输出虚拟机唯一ID，不输出类名、Jar名和传入main方法的参数</p>
     *      <p>-m 输出传入main方法的参数</p>
     *      <p>-l 输出main类或Jar的全名</p>
     *      <p>-v 输出虚拟机进程启动时JVM的参数</p>
     *      <p>-V 输出虚拟机标志信息</p>
     *
     * @return {@link JpsResult}对象集合
     *
     * @throws IllegalArgumentException 非法参数异常
     * @throws MonitorException 监控异常
     */
    public static List<JpsResult> run(String[] var0) throws IllegalArgumentException, MonitorException {

        //  参数转义
        arguments = new Arguments(var0);
        if (arguments.isHelp()) {
            Arguments.printUsage(System.err);
            arguments = null;
            return null;
        }

        ArrayList<JpsResult> jpsResultList = new ArrayList<>();
        //  获取Host Id
        HostIdentifier var1 = arguments.hostId();

        //  获取所有被监控的jvm实例Host信息
        MonitoredHost var25 = MonitoredHost.getMonitoredHost(var1);

        //  迭代遍历每个jvm实例
        Set var3 = var25.activeVms();
        Iterator var4 = var3.iterator();

        while (true) {
            while (var4.hasNext()) {
                JpsResult jpsResult = new JpsResult();
                ArrayList<Object> errStacks = new ArrayList<>();
                //  获取实例的id
                Integer var5 = (Integer) var4.next();
                jpsResult.setVmId(var5);

                //  定义一个数据动态字符串，拼装返回结果
                StringBuilder var6 = new StringBuilder();

                //  用于接收错误栈信息
                Object var7 = null;

                //  var8为jvm的唯一标识id，并拼装入var6
                int var8 = var5;
                var6.append(String.valueOf(var8));

                //  判断是否是“-p”参数，如果有就到此结束
                if (arguments.isQuiet()) {
                    jpsResult.setStrResult(String.valueOf(var6));
                } else {

                    //  创建临时被监控的虚拟机变量
                    MonitoredVm var9 = null;

                    //  拼接虚拟机参数，其中需传入虚拟机唯一识别id，这里就是var8
                    String var10 = "//" + var8 + "?mode=r";

                    //  var11为错误信息，如果在接下来操作的某一步出现错误，就会打印它
                    //  在每个可能发生错误的处理步骤前后都需要对var11进行操作
                    //  在处理前先把错误信息赋给var11，结束后则设置为null
                    //  最后判断是否为空来决定是否打印该信息
                    String var11 = null;

                    try {
                        var11 = " -- process information unavailable";

                        //  根据传入的参数获取一个虚拟机识别码对象
                        VmIdentifier var12 = new VmIdentifier(var10);

                        //  接收此虚拟机对象，其中第二个参数为间隔量
                        var9 = var25.getMonitoredVm(var12, 0);
                        var11 = " -- main class information unavailable";

                        //  拼接结果，添加虚拟机运行的主类方法详情信息
                        var6.append(" ");
                        var6.append(MonitoredVmUtil.mainClass(var9, arguments.showLongPaths()));
                        jpsResult.setMainClass(MonitoredVmUtil.mainClass(var9, arguments.showLongPaths()));

                        //  用于接收主类参数、虚拟机参数以及虚拟机标志信息
                        String var13;

                        //  是否显示主类参数，该命令参数为“-m”
                        if (arguments.showMainArgs()) {
                            var11 = " -- main args information unavailable";

                            //  获取主类参数，需要传入该虚拟机对象
                            var13 = MonitoredVmUtil.mainArgs(var9);

                            //  拼接结果，添加主类参数
                            if (var13 != null && var13.length() > 0) {
                                var6.append(" ");
                                var6.append(var13);
                                jpsResult.setMainArgs(var13);
                            }
                        }

                        //  是否显示虚拟机参数，该命令参数为“-v”
                        if (arguments.showVmArgs()) {
                            var11 = " -- beifengtz args information unavailable";

                            //  获取虚拟机参数，需要传入该虚拟机对象
                            var13 = MonitoredVmUtil.jvmArgs(var9);

                            //  拼接结果，添加虚拟机参数
                            if (var13 != null && var13.length() > 0) {
                                var6.append(" ");
                                var6.append(var13);
                                jpsResult.setVmArgs(var13);
                            }
                        }

                        //  是否显示虚拟机标志信息，该命令参数为“-V”
                        if (arguments.showVmFlags()) {
                            var11 = " -- beifengtz flags information unavailable";

                            //  获取虚虚拟机标志信息，需要传入该虚拟机对象
                            var13 = MonitoredVmUtil.jvmFlags(var9);

                            //  拼接结果，添加虚拟机标志信息
                            if (var13 != null && var13.length() > 0) {
                                var6.append(" ");
                                var6.append(var13);
                                jpsResult.setVmFlags(var13);
                            }
                        }

                        var11 = " -- detach failed";

                        //  到此就已经获取完需要的信息，从所有Host中分隔该虚拟机实例
                        var25.detach(var9);

                        //  完整的结果信息
                        jpsResult.setStrResult(String.valueOf(var6));
                        var11 = null;
                    } catch (URISyntaxException var21) {
                        var7 = var21;
                        errStacks.add(var21);
                        assert false;
                    } catch (Exception var22) {
                        var7 = var22;
                        errStacks.add(var22);
                    } finally {

                        //  追加错误信息
                        if (var11 != null) {
                            var6.append(var11);
                            jpsResult.setErrMessage(var11);
                            jpsResult.setErrStacks(errStacks);
                            errStacks = null;

                            //  如果未调试状态，则打印完整信息
                            if (arguments.isDebug() && var7 != null && ((Throwable) var7).getMessage() != null) {
                                var6.append("\n\t");
                                var6.append(((Throwable) var7).getMessage());
                            }

                            jpsResult.setStrResult(String.valueOf(var6));

                            //  打印出错误栈信息
                            if (arguments.printStackTrace()) {
                                ((Throwable) var7).printStackTrace();
                            }
                            continue;
                        }
                    }
                }
                jpsResultList.add(jpsResult);
                jpsResult = null;
            }
            arguments = null;
            return jpsResultList;
        }
    }
}
