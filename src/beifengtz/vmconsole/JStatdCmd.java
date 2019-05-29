package beifengtz.vmconsole;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.VmConsole-Api</p>
 * Created in 8:58 2019/5/29
 *
 * jstatd命令介绍：
 *
 * jstatd是一个基于RMI（Remove Method Invocation）的服务程序，
 * 它用于监控基于HotSpot的JVM中资源的创建及销毁，并且提供了一个远
 * 程接口允许远程的监控工具连接到本地的JVM执行命令。
 *
 * jstatd是基于RMI的，所以在运行jstatd的服务器上必须存在RMI注册
 * 中心，如果没有通过选项"-p port"指定要连接的端口，jstatd会尝试
 * 连接RMI注册中心的默认端口。后面会谈到如何连接到一个默认的RMI内部
 * 注册中心，如何禁止默认的RMI内部注册中心的创建，以及如何启动一个外
 * 部注册中心。
 *
 */
public class JStatdCmd {
}
