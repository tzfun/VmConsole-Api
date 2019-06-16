package beifengtz.test;

import beifengtz.vmconsole.JMapCmd;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.test.VmConsole-Api</p>
 * Created in 12:12 2019/6/16
 */
public class JMapTest {
    public static void main(String[] args) throws Exception{
//        JMapCmd.run(new String[]{"-dump:live,format=b,file=test.bin","8468"});
//        System.out.println(JMapCmd.dumpAll(8468, "c:/users/tzplay/desktop/test.bin"));
        System.out.println(JMapCmd.heapInfo(8468));
    }
}
