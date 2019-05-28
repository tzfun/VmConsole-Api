package beifengtz.vmconsole.entity;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.javase_learning</p>
 * Created in 19:47 2019/5/27
 */
public class JStatResultForGcNewCapacity extends JStatResult implements JStatTransferable {
    //NGCMN：新生代最小容量
    private double ngcmn;
    //NGCMX：新生代最大容量
    private double ngcmx;
    //NGC：当前新生代容量
    private double ngc;
    //S0CMX：最大幸存1区大小
    private double s0cmx;
    //S0C：当前幸存1区大小
    private double s0c;
    //S1CMX：最大幸存2区大小
    private double s1cmx;
    //S1C：当前幸存2区大小
    private double s1c;
    //ECMX：最大Eden区大小
    private double ecmx;
    //EC：当前Eden区大小
    private double ec;
    //YGC：年轻代垃圾回收次数
    private int ygc;
    //FGC：老年代回收次数
    private int fgc;

    public JStatResultForGcNewCapacity(String result) {
        String[] strs = result.trim().split("\\s+");
        this.ngcmn = Double.valueOf(strs[0]);
        this.ngcmx = Double.valueOf(strs[1]);
        this.ngc = Double.valueOf(strs[2]);
        this.s0cmx = Double.valueOf(strs[3]);
        this.s0c = Double.valueOf(strs[4]);
        this.s1cmx = Double.valueOf(strs[5]);
        this.s1c = Double.valueOf(strs[6]);
        this.ecmx = Double.valueOf(strs[7]);
        this.ec = Double.valueOf(strs[8]);
        this.ygc = Integer.valueOf(strs[9]);
        this.fgc = Integer.valueOf(strs[10]);
    }

    @Override
    public String toString() {
        return "JStatResultForGcNewCapacity{" +
                "vmId=" + super.getVmId() +
                ", ngcmn=" + ngcmn +
                ", ngcmx=" + ngcmx +
                ", ngc=" + ngc +
                ", s0cmx=" + s0cmx +
                ", s0c=" + s0c +
                ", s1cmx=" + s1cmx +
                ", s1c=" + s1c +
                ", ecmx=" + ecmx +
                ", ec=" + ec +
                ", ygc=" + ygc +
                ", fgc=" + fgc +
                '}';
    }

    public double getNgcmn() {
        return ngcmn;
    }

    public double getNgcmx() {
        return ngcmx;
    }

    public double getNgc() {
        return ngc;
    }

    public double getS0cmx() {
        return s0cmx;
    }

    public double getS0c() {
        return s0c;
    }

    public double getS1cmx() {
        return s1cmx;
    }

    public double getS1c() {
        return s1c;
    }

    public double getEcmx() {
        return ecmx;
    }

    public double getEc() {
        return ec;
    }

    public int getYgc() {
        return ygc;
    }

    public int getFgc() {
        return fgc;
    }
}
