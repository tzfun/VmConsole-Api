package beifengtz.vmconsole.test;

import java.util.Arrays;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.javase_learning</p>
 * Created in 19:38 2019/5/23
 */
public class GcTest {
    public static void main(String[] args) throws InterruptedException{
        String str = "hello\nworld\nhhhh\n32";
        String[] strs = str.split("\\n");
        String threadId = strs[strs.length-1];

        System.out.println(str.substring(0,str.length()-threadId.length()-1));
        System.out.println(threadId);
    }
}
