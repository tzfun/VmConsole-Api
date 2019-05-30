package beifengtz.test;

import beifengtz.vmconsole.JpsCmd;
import sun.jvmstat.monitor.MonitorException;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: test</p>
 * Created in 11:23 2019/5/30
 */
public class JpsTest {
    public static void main(String[] args) throws IllegalArgumentException,MonitorException {
        System.out.println(JpsCmd.quit());
        System.out.println(JpsCmd.withFullName());
        System.out.println(JpsCmd.withMainClassArgs());
        System.out.println(JpsCmd.withVmArgs());
    }
}
