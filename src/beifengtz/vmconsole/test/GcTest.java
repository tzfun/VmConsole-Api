package beifengtz.vmconsole.test;

import beifengtz.vmconsole.entity.JStackResult;
import beifengtz.vmconsole.tools.jstack.JStackTool;

import java.util.Arrays;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.javase_learning</p>
 * Created in 19:38 2019/5/23
 */
public class GcTest {
    public static void main(String[] args) throws InterruptedException,Exception{

        JStackResult jStackResult = new JStackResult();

        JStackTool jstack = new JStackTool(true, false,jStackResult);
        jstack.execute(new String[]{"3156"},jStackResult);

        System.out.println(jStackResult);
    }
}
