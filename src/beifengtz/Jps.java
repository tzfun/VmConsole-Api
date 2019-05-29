package beifengtz;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.VmConsole-Api</p>
 * Created in 9:32 2019/5/29
 */
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;
import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;
import sun.tools.jps.Arguments;

public class Jps {
    //  jps命令独有的参数解析类
    private static Arguments arguments;

    public Jps() {
    }
    //  所有的工具类的执行函数体都是main中，便于方便生成可执行文件
    //  但是也因为此使得这些命令不方便拓展，只有靠重构
    public static void main(String[] var0) {
        try {
            //  解析传入的命令，可能会出现非法命令，因此需要处理该异常
            arguments = new Arguments(var0);
        } catch (IllegalArgumentException var20) {
            //  打印异常栈
            System.err.println(var20.getMessage());
            //  打印使用方法
            Arguments.printUsage(System.err);
            //  虚拟机异常退出
            System.exit(1);
        }

        //  判断是否是-h或-?命令，是就打印使用方法，然后程序正常退出
        if (arguments.isHelp()) {
            Arguments.printUsage(System.err);
            System.exit(0);
        }

        try {
            //  获取hostId，此处从参数获取
            //  如果参数未传入hostId则默认为所有，
            //  如果传入了参数则从远程或其他hostId获取jvm实例
            HostIdentifier var1 = arguments.hostId();
            //  根据hostId获取被监控的jvm实例集合，后续可依次遍历
            //  此处可能会获取失败，抛出MonitorException异常
            MonitoredHost var25 = MonitoredHost.getMonitoredHost(var1);
            //  迭代器遍历
            Set var3 = var25.activeVms();
            Iterator var4 = var3.iterator();

            while(true) {
                while(var4.hasNext()) {
                    //  获取虚拟机唯一识别码vmId
                    Integer var5 = (Integer)var4.next();
                    //  定义结果字符串，设为动态字符串，方便后续结果拼接
                    StringBuilder var6 = new StringBuilder();
                    //  var7为栈信息对象，后续打印异常信息使用
                    Object var7 = null;
                    //  先将vmId拼接到结果中
                    int var8 = var5;
                    var6.append(String.valueOf(var8));
                    //  是否是-q命令，该命令就仅显示vmId，不显示主方法等信息
                    if (arguments.isQuiet()) {
                        System.out.println(var6);
                    } else {
                        //  定义jvm实例对象
                        MonitoredVm var9 = null;
                        //  获取虚拟机信息的参数
                        String var10 = "//" + var8 + "?mode=r";
                        //  定义错误信息字符串，如果有错误会直接赋值给它，如果无错误就为null
                        //  后面判断其是否是null来检测是否发生错误
                        String var11 = null;

                        try {
                            //  先赋值错误信息，再执行代码，如果执行过程中发送错误var11就不会被设为null
                            //  如果成功执行最后会重新给var11赋值为null
                            var11 = " -- process information unavailable";
                            //  获取该虚拟机认证对象，该对象可以获取jvm的全路径，无论远程还是本地
                            VmIdentifier var12 = new VmIdentifier(var10);
                            //  从虚拟机实例集合中获取一个虚拟机实例，通过认证对象VmIdentifier获取
                            var9 = var25.getMonitoredVm(var12, 0);
                            var11 = " -- main class information unavailable";
                            //  拼接结果，加入主类路径
                            var6.append(" " + MonitoredVmUtil.mainClass(var9, arguments.showLongPaths()));
                            //  var13主要用于存储其他参数的结果信息
                            String var13;
                            //  是否显示main函数的参数，-m参数显示
                            if (arguments.showMainArgs()) {
                                var11 = " -- main args information unavailable";
                                var13 = MonitoredVmUtil.mainArgs(var9);
                                if (var13 != null && var13.length() > 0) {
                                    var6.append(" " + var13);
                                }
                            }

                            //  是否显示虚拟机参数，-v显示
                            if (arguments.showVmArgs()) {
                                var11 = " -- jvm args information unavailable";
                                var13 = MonitoredVmUtil.jvmArgs(var9);
                                if (var13 != null && var13.length() > 0) {
                                    var6.append(" " + var13);
                                }
                            }

                            //  是否显示虚拟机flags，-V显示
                            if (arguments.showVmFlags()) {
                                var11 = " -- jvm flags information unavailable";
                                var13 = MonitoredVmUtil.jvmFlags(var9);
                                if (var13 != null && var13.length() > 0) {
                                    var6.append(" " + var13);
                                }
                            }

                            var11 = " -- detach failed";
                            //  从集合中分离该虚拟机实例
                            var25.detach(var9);
                            //  最终打印所有命令的结果
                            System.out.println(var6);
                            var11 = null;
                        } catch (URISyntaxException var21) {
                            //  var7用于接收异常栈，这里不直接处理，在finally块中集中处理，下面一样
                            var7 = var21;

                            assert false;
                        } catch (Exception var22) {
                            var7 = var22;
                        } finally {
                            //  检查是否出现错误，即判断var11有没有被赋值
                            if (var11 != null) {
                                //  在最终结果中添加相应的错误信息
                                var6.append(var11);
                                //  如果是调试模式（jps.debug）,或者报错了，就在结果中添加错误信息
                                if (arguments.isDebug() && var7 != null && ((Throwable)var7).getMessage() != null) {
                                    var6.append("\n\t");
                                    var6.append(((Throwable)var7).getMessage());
                                }

                                System.out.println(var6);
                                //  是否打印堆栈信息（jps.printStackTrace）
                                if (arguments.printStackTrace()) {
                                    ((Throwable)var7).printStackTrace();
                                }
                                continue;
                            }
                        }
                    }
                }

                return;
            }
        } catch (MonitorException var24) {
            //  打印异常栈
            if (var24.getMessage() != null) {
                System.err.println(var24.getMessage());
            } else {
                Throwable var2 = var24.getCause();
                if (var2 != null && var2.getMessage() != null) {
                    System.err.println(var2.getMessage());
                } else {
                    var24.printStackTrace();
                }
            }
            //  虚拟机异常退出
            System.exit(1);
        }
    }
}

