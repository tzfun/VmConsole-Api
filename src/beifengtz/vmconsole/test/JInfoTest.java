package beifengtz.vmconsole.test;

import beifengtz.vmconsole.JInfoCmd;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.test.VmConsole-Api</p>
 * Created in 18:40 2019/5/30
 */
public class JInfoTest {
    public static void main(String[] args) throws Exception{
        System.out.println(JInfoCmd.run(new String[]{"11004"}));
    }
}
