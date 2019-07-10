package beifengtz.vmconsole.tools.jstat;

import beifengtz.vmconsole.entity.jstat.*;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jstat</p>
 * Created in 18:07 2019/5/27
 *
 * <p>jstat结果集生产工具类，根据传入的参数类型返回对应的结果集。</p>
 */
public class JStatResultTool {

    public static JStatResult getJStatResult(String arg, String result) throws IllegalArgumentException{
        switch (arg.toLowerCase()){
            case "class":
                return new JStatResultForClass(result);
            case "gc":
                return new JStatResultForGc(result);
            case "compiler":
                return new JStatResultForCompiler(result);
            case "gccapacity":
                return new JStatResultForGcCapacity(result);
            case "gcnew":
                return new JStatResultForGcNew(result);
            case "gcnewcapacity":
                return new JStatResultForGcNewCapacity(result);
            case "gcold":
                return new JStatResultForGcOld(result);
            case "gcoldcapacity":
                return new JStatResultForGcOldCapacity(result);
            case "gcmetacapacity":
                return new JStatResultForGcMetaCapacity(result);
            case "gcutil":
                return new JStatResultForGcUtil(result);
            case "printcompilation":
                return new JStatResultForCompilation(result);
            default:
                throw new IllegalArgumentException("Argument not found.");
        }
    }
}
