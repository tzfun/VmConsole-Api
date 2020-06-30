package beifengtz.vmconsole.entity.jstat;


/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jstat</p>
 * Created in 19:12 2019/5/27
 *
 * <p>JStatResultForGc实体类</p>
 * <p>用于封装jstat -gc命令的结果</p>
 */
public class JStatResultForGc extends JStatResult {

    //S0C：第一个幸存区的大小
    private double s0c;
    //S1C：第二个幸存区的大小
    private double s1c;
    //S0U：第一个幸存区的使用大小
    private double s0u;
    //S1U：第二个幸存区的使用大小
    private double s1u;
    //EC：Eden区的大小
    private double ec;
    //EU：Eden区的使用大小
    private double eu;
    //OC：老年代大小
    private double oc;
    //OU：老年代使用大小
    private double ou;
    //MC：方法区大小
    private double mc;
    //MU：方法区使用大小
    private double mu;
    //CCSC:压缩类空间大小
    private double ccsc;
    //CCSU:压缩类空间使用大小
    private double ccsu;
    //YGC：年轻代垃圾回收次数
    private int ygc;
    //YGCT：年轻代垃圾回收消耗时间
    private double ygct;
    //FGC：老年代垃圾回收次数
    private int fgc;
    //FGCT：老年代垃圾回收消耗时间
    private double fgct;
    //GCT：垃圾回收消耗总时间
    private double gct;

    public JStatResultForGc(String result) {
        String[] strs = result.trim().split("\\s+");
        this.s0c = Double.valueOf(strs[0]);
        this.s1c = Double.valueOf(strs[1]);
        this.s0u = Double.valueOf(strs[2]);
        this.s1u = Double.valueOf(strs[3]);
        this.ec = Double.valueOf(strs[4]);
        this.eu = Double.valueOf(strs[5]);
        this.oc = Double.valueOf(strs[6]);
        this.ou = Double.valueOf(strs[7]);
        this.mc = Double.valueOf(strs[8]);
        this.mu = Double.valueOf(strs[9]);
        this.ccsc = Double.valueOf(strs[10]);
        this.ccsu = Double.valueOf(strs[11]);
        this.ygc = Integer.valueOf(strs[12]);
        this.ygct = Double.valueOf(strs[13]);
        this.fgc = Integer.valueOf(strs[14]);
        this.fgct = Double.valueOf(strs[15]);
        this.gct = Double.valueOf(strs[16]);
    }

    @Override
    public String toString() {
        return "JStatResultForGc{" +
                "vmId=" + super.getVmId() +
                ", s0c=" + s0c +
                ", s1c=" + s1c +
                ", s0u=" + s0u +
                ", s1u=" + s1u +
                ", ec=" + ec +
                ", eu=" + eu +
                ", oc=" + oc +
                ", ou=" + ou +
                ", mc=" + mc +
                ", mu=" + mu +
                ", ccsc=" + ccsc +
                ", ccsu=" + ccsu +
                ", ygc=" + ygc +
                ", ygct=" + ygct +
                ", fgc=" + fgc +
                ", fgct=" + fgct +
                ", gct=" + gct +
                "}\n";
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

    public double getEc() {
        return ec;
    }

    public double getEu() {
        return eu;
    }

    public double getOc() {
        return oc;
    }

    public double getOu() {
        return ou;
    }

    public double getMc() {
        return mc;
    }

    public double getMu() {
        return mu;
    }

    public double getCcsc() {
        return ccsc;
    }

    public double getCcsu() {
        return ccsu;
    }

    public int getYgc() {
        return ygc;
    }

    public double getYgct() {
        return ygct;
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
