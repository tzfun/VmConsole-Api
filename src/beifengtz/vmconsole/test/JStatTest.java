package beifengtz.vmconsole.test;

import beifengtz.vmconsole.JStatCmd;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.test.VmConsole-Api</p>
 * Created in 13:28 2019/5/30
 */
public class JStatTest {
    public static void main(String[] args) {
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
