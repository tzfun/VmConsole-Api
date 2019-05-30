package beifengtz.vmconsole.entity.jstat;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jstat</p>
 * Created in 20:00 2019/5/27
 *
 * <p>JStatResultForGcUtil实体类</p>
 * <p>用于封装jstat -gcutil命令的结果</p>
 */
public class JStatResultForGcUtil extends JStatResult {
    //S0：幸存1区当前使用比例
    private double s0;
    //S1：幸存2区当前使用比例
    private double s1;
    //E：Eden区使用比例
    private double e;
    //O：老年代使用比例
    private double o;
    //M：元数据区使用比例
    private double m;
    //CCS：压缩使用比例
    private double ccs;
    //YGC：年轻代垃圾回收次数
    private int ygc;
    //YGCT：年轻代代垃圾回收消耗时间
    private double ygct;
    //FGC：老年代垃圾回收次数
    private int fgc;
    //FGCT：老年代垃圾回收消耗时间
    private double fgct;
    //GCT：垃圾回收消耗总时间
    private double gct;

    public JStatResultForGcUtil(String result) {
        String[] strs = result.trim().split("\\s+");
        this.s0 = Double.valueOf(strs[0]);
        this.s1 = Double.valueOf(strs[1]);
        this.e = Double.valueOf(strs[2]);
        this.o = Double.valueOf(strs[3]);
        this.m = Double.valueOf(strs[4]);
        this.ccs = Double.valueOf(strs[5]);
        this.ygc = Integer.valueOf(strs[6]);
        this.ygct = Double.valueOf(strs[7]);
        this.fgc = Integer.valueOf(strs[8]);
        this.fgct = Double.valueOf(strs[9]);
        this.gct = Double.valueOf(strs[10]);
    }

    @Override
    public String toString() {
        return "JStatResultForGcUtil{" +
                "vmId=" + super.getVmId() +
                ", s0=" + s0 +
                ", s1=" + s1 +
                ", e=" + e +
                ", o=" + o +
                ", m=" + m +
                ", ccs=" + ccs +
                ", ygc=" + ygc +
                ", ygct=" + ygct +
                ", fgc=" + fgc +
                ", fgct=" + fgct +
                ", gct=" + gct +
                '}';
    }

    public double getS0() {
        return s0;
    }

    public double getS1() {
        return s1;
    }

    public double getE() {
        return e;
    }

    public double getO() {
        return o;
    }

    public double getM() {
        return m;
    }

    public double getCcs() {
        return ccs;
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

    public double getYgct() {
        return ygct;
    }
}
