package beifengtz.vmconsole.test;

import beifengtz.vmconsole.JpsCmd;
import sun.jvmstat.monitor.MonitorException;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.test.VmConsole-Api</p>
 * Created in 11:23 2019/5/30
 */
public class JpsTest {
    public static void main(String[] args) {
        try {
            System.out.println(JpsCmd.run(new String[]{"-l"}));
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }catch (MonitorException e){
            e.printStackTrace();
        }
    }
}
