package beifengtz.vmconsole.entity.jmap;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jmap.VmConsole-Api</p>
 * Created in 13:06 2019/6/16
 */
public class JMapForHeapUsage {

    //  如果是Gen收集器，使用下面属性获取数据
    private HeapForGen heapForGen;
    //  如果是G1收集器，使用下面属性获取数据
    private HeapForG1 heapForG1;

    @Override
    public String toString() {
        return "JMapForHeapUsage{" +
                "heapForGen=" + heapForGen +
                ", heapForG1=" + heapForG1 +
                '}';
    }

    public HeapForGen getHeapForGen() {
        return heapForGen;
    }

    public void setHeapForGen(HeapForGen heapForGen) {
        this.heapForGen = heapForGen;
    }

    public HeapForG1 getHeapForG1() {
        return heapForG1;
    }

    public void setHeapForG1(HeapForG1 heapForG1) {
        this.heapForG1 = heapForG1;
    }
}
