package beifengtz.vmconsole.test;

import beifengtz.vmconsole.JStackCmd;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.test.VmConsole-Api</p>
 * Created in 15:41 2019/5/30
 */
public class JStackTest {
    public static void main(String[] args) {

        String[] arg = new String[]{"-l", "11004"};

        try{
            System.out.println(JStackCmd.threadDump(11004));
            System.out.println(JStackCmd.threadStack(11004));
            System.out.println(JStackCmd.jniStack(11004));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
