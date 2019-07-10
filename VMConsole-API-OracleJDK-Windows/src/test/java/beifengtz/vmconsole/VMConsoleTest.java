package beifengtz.vmconsole;

import beifengtz.vmconsole.entity.jcmd.JCmdEnum;
import beifengtz.vmconsole.entity.jmap.JMapForHeapResult;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.practice</p>
 * Created in 9:07 2019/7/10
 */
public class VMConsoleTest {

    @Test
    public void JpsTest() throws Exception {
        System.out.println(JpsCmd.quit());
        System.out.println(JpsCmd.withFullName());
        System.out.println(JpsCmd.withMainClassArgs());
        System.out.println(JpsCmd.withVmArgs());
    }

    @Test
    public void JCmdTest() throws Exception {
        System.out.println(JCmd.executeCommand(11004, JCmdEnum.GC_RUN));
    }

    @Test
    public void JInfoTest() throws Exception {
        System.out.println(JInfoCmd.run(new String[]{"-flag", "CICompilerCount=3", "11004"}));
//        System.out.println(JInfoCmd.run(new String[]{"-flag","+HeapDumpOnOutOfMemoryError","11004"}));
//        System.out.println(JInfoCmd.run(new String[]{"-flag","-HeapDumpOnOutOfMemoryError","11004"}));
    }

    @Test
    public void JMapTest() throws Exception {
        //  保存全部对象信息的dump文件，保存成功返回true，失败false
        JMapCmd.dumpAll(8468, "c:/users/tzplay/desktop/test.bin");

        //  只保存存活对象信息的dump文件，保存成功返回true，失败false
        JMapCmd.dumpLive(8468, "c:/users/tzplay/desktop/test.bin");

        //  获取全部对象的class实例数据，从输出流中读取数据
        InputStream histoAllIS = JMapCmd.histoAll(8468);

        //  只获取存活对象的class实例数据，从输出流中读取数据
        InputStream histoLiveIS = JMapCmd.histoLive(8468);

        //  获取虚拟机heap信息，兼容jdk8之前提供的所有垃圾收集器，包括G1收集器
        JMapForHeapResult jMapForHeapResult = JMapCmd.heapInfo(8468);
        System.out.println(jMapForHeapResult);
    }

    @Test
    public void JStackTest() throws Exception {
        int vmId = 11004;

        System.out.println(JStackCmd.threadDump(vmId));
        System.out.println(JStackCmd.threadStack(vmId));
        System.out.println(JStackCmd.jniStack(vmId));
    }

    @Test
    public void JStatTest() throws Exception {
        int vmId = 11004;

        System.out.println(JStatCmd.list());
        System.out.println(JStatCmd.snap(vmId));
        System.out.println(JStatCmd.clazz(vmId));
        System.out.println(JStatCmd.gc(vmId));
        System.out.println(JStatCmd.gcNew(vmId));
        System.out.println(JStatCmd.gcOld(vmId));
        System.out.println(JStatCmd.gcNewCapacity(vmId));
        System.out.println(JStatCmd.gcOldCapacity(vmId));
        System.out.println(JStatCmd.gcMetaCapacity(vmId));
        System.out.println(JStatCmd.compiler(vmId));
        System.out.println(JStatCmd.printCompilation(vmId));
    }
}
