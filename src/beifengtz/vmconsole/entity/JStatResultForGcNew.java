package beifengtz.vmconsole.entity;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.javase_learning</p>
 * Created in 19:43 2019/5/27
 */
public class JStatResultForGcNew extends JStatResult implements JStatTransferable {
    //S0C：第一个幸存区大小
    private double s0c;
    //S1C：第二个幸存区的大小
    private double s1c;
    //S0U：第一个幸存区的使用大小
    private double s0u;
    //S1U：第二个幸存区的使用大小
    private double s1u;
    //TT:对象在新生代存活的次数
    private int tt;
    //MTT:对象在新生代存活的最大次数
    private int mtt;
    //DSS:期望的幸存区大小
    private double dss;
    //EC：Eden区的大小
    private double ec;
    //EU：Eden区的使用大小
    private double eu;
    //YGC：年轻代垃圾回收次数
    private int ygc;
    //YGCT：年轻代垃圾回收消耗时间
    private double ygct;

    public JStatResultForGcNew(String result) {
        String[] strs = result.trim().split("\\s+");
        this.s0c = Double.valueOf(strs[0]);
        this.s1c = Double.valueOf(strs[1]);
        this.s0u = Double.valueOf(strs[2]);
        this.s1u = Double.valueOf(strs[3]);
        this.tt = Integer.valueOf(strs[4]);
        this.mtt = Integer.valueOf(strs[5]);
        this.dss = Double.valueOf(strs[6]);
        this.ec = Double.valueOf(strs[7]);
        this.eu = Double.valueOf(strs[8]);
        this.ygc = Integer.valueOf(strs[9]);
        this.ygct = Double.valueOf(strs[10]);
    }

    @Override
    public String toString() {
        return "JStatResultForGcNew{" +
                "vmId=" + super.getVmId() +
                ", s0c=" + s0c +
                ", s1c=" + s1c +
                ", s0u=" + s0u +
                ", s1u=" + s1u +
                ", tt=" + tt +
                ", mtt=" + mtt +
                ", dss=" + dss +
                ", ec=" + ec +
                ", eu=" + eu +
                ", ygc=" + ygc +
                ", ygct=" + ygct +
                '}';
    }

    public double getS0c() {
        return s0c;
    }

    public double getS1c() {
        return s1c;
    }

    public double getS0u() {
        return s0u;
    }

    public double getS1u() {
        return s1u;
    }

    public int getTt() {
        return tt;
    }

    public int getMtt() {
        return mtt;
    }

    public double getDss() {
        return dss;
    }

    public double getEc() {
        return ec;
    }

    public double getEu() {
        return eu;
    }

    public int getYgc() {
        return ygc;
    }

    public double getYgct() {
        return ygct;
    }
}
