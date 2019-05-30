package beifengtz.vmconsole.test;

import beifengtz.vmconsole.JStackCmd;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.test</p>
 * Created in 15:41 2019/5/30
 */
public class JStackTest {
    public static void main(String[] args) {

        int vmId= 11004;

        try{
            System.out.println(JStackCmd.threadDump(vmId));
            System.out.println(JStackCmd.threadStack(vmId));
            System.out.println(JStackCmd.jniStack(vmId));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
