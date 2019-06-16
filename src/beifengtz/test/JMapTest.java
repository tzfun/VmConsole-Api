package beifengtz.test;

import beifengtz.vmconsole.JMapCmd;
import beifengtz.vmconsole.entity.jmap.JMapForHeapResult;

import java.io.InputStream;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.test.VmConsole-Api</p>
 * Created in 12:12 2019/6/16
 */
public class JMapTest {
    public static void main(String[] args) throws Exception{
        //  保存全部对象信息的dump文件，保存成功返回true，失败false
        JMapCmd.dumpAll(8468, "c:/users/tzplay/desktop/test.bin");

        //  只保存存活对象信息的dump文件，保存成功返回true，失败false
        JMapCmd.dumpAll(8468, "c:/users/tzplay/desktop/test.bin");

        //  获取全部对象的class实例数据，从输出流中读取数据
        InputStream histoAllIS = JMapCmd.histoAll(8468);

        //  只获取存活对象的class实例数据，从输出流中读取数据
        InputStream histoLiveIS = JMapCmd.histoLive(8468);

        //  获取虚拟机heap信息，兼容jdk8之前提供的所有垃圾收集器，包括G1收集器
        JMapForHeapResult jMapForHeapResult = JMapCmd.heapInfo(8468);
        System.out.println(jMapForHeapResult);
    }
}
