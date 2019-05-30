package beifengtz.test;

import beifengtz.vmconsole.JInfoCmd;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: test.VmConsole-Api</p>
 * Created in 18:40 2019/5/30
 */
public class JInfoTest {
    public static void main(String[] args) throws Exception{
        System.out.println(JInfoCmd.run(new String[]{"-flag","CICompilerCount=3","11004"}));
//        System.out.println(JInfoCmd.run(new String[]{"-flag","+HeapDumpOnOutOfMemoryError","11004"}));
//        System.out.println(JInfoCmd.run(new String[]{"-flag","-HeapDumpOnOutOfMemoryError","11004"}));
    }
}
