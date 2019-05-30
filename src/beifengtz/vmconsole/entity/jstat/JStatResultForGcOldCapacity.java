package beifengtz.vmconsole.entity.jstat;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jstat</p>
 * Created in 19:54 2019/5/27
 *
 * <p>JStatResultForGcOldCapacity实体类</p>
 * <p>用于封装jstat -gcoldcapacity命令的结果</p>
 */
public class JStatResultForGcOldCapacity extends JStatResult {
    //OGCMN：老年代最小容量
    private double ogcmn;
    //OGCMX：老年代最大容量
    private double ogcmx;
    //OGC：当前老年代大小
    private double ogc;
    //OC：老年代大小
    private double oc;
    //YGC：年轻代垃圾回收次数
    private int ygc;
    //FGC：老年代垃圾回收次数
    private int fgc;
    //FGCT：老年代垃圾回收消耗时间
    private double fgct;
    //GCT：垃圾回收消耗总时间
    private double gct;

    public JStatResultForGcOldCapacity(String result) {
        String[] strs = result.trim().split("\\s+");
        this.ogcmn = Double.valueOf(strs[0]);
        this.ogcmx = Double.valueOf(strs[1]);
        this.ogc = Double.valueOf(strs[2]);
        this.oc = Double.valueOf(strs[3]);
        this.ygc = Integer.valueOf(strs[4]);
        this.fgc = Integer.valueOf(strs[5]);
        this.fgct = Double.valueOf(strs[6]);
        this.gct = Double.valueOf(strs[7]);
    }

    @Override
    public String toString() {
        return "JStatResultForGcOldCapacity{" +
                "vmId=" + super.getVmId() +
                ", ogcmn=" + ogcmn +
                ", ogcmx=" + ogcmx +
                ", ogc=" + ogc +
                ", oc=" + oc +
                ", ygc=" + ygc +
                ", fgc=" + fgc +
                ", fgct=" + fgct +
                ", gct=" + gct +
                '}';
    }

    public double getOgcmn() {
        return ogcmn;
    }

    public double getOgcmx() {
        return ogcmx;
    }

    public double getOgc() {
        return ogc;
    }

    public double getOc() {
        return oc;
    }

    public int getYgc() {
        return ygc;
    }

    public int getFgc() {
        return fgc;
    }

    public double getFgct() {
        return fgct;
    }

    public double getGct() {
        return gct;
    }
}
