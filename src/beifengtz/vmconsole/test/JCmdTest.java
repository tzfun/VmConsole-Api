package beifengtz.vmconsole.test;

import beifengtz.vmconsole.JCmd;
import beifengtz.vmconsole.entity.jcmd.JCmdEnum;
import beifengtz.vmconsole.entity.jstack.JStackResult;
import beifengtz.vmconsole.tools.jstack.JStackTool;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.javase_learning</p>
 * Created in 19:38 2019/5/23
 */
public class JCmdTest {
    public static void main(String[] args) throws Exception{
//        System.out.println(JCmd.executeCommand(11004,JCmdEnum.GC_RUN,JCmdEnum.JFR_START));
        System.out.println(JCmd.executeCommand(11004,JCmdEnum.GC_RUN));
    }
}
