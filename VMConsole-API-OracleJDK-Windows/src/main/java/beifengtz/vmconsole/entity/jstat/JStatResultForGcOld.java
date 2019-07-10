package beifengtz.vmconsole.entity.jstat;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jstat</p>
 * Created in 19:51 2019/5/27
 *
 * <p>JStatResultForGcOld实体类</p>
 * <p>用于封装jstat -gcold命令的结果</p>
 */
public class JStatResultForGcOld extends JStatResult {
    //MC：方法区大小
    private double mc;
    //MU：方法区使用大小
    private double mu;
    //CCSC:压缩类空间大小
    private double ccsc;
    //CCSU:压缩类空间使用大小
    private double ccsu;
    //OC：老年代大小
    private double oc;
    //OU：老年代使用大小
    private double ou;
    //YGC：年轻代垃圾回收次数
    private int ygc;
    //FGC：老年代垃圾回收次数
    private int fgc;
    //FGCT：老年代垃圾回收消耗时间
    private double fgct;
    //GCT：垃圾回收消耗总时间
    private double gct;

    public JStatResultForGcOld(String result) {
        String[] strs = result.trim().split("\\s+");
        this.mc = Double.valueOf(strs[0]);
        this.mu = Double.valueOf(strs[1]);
        this.ccsc = Double.valueOf(strs[2]);
        this.ccsu = Double.valueOf(strs[3]);
        this.oc = Double.valueOf(strs[4]);
        this.ou = Double.valueOf(strs[5]);
        this.ygc = Integer.valueOf(strs[6]);
        this.fgc = Integer.valueOf(strs[7]);
        this.fgct = Double.valueOf(strs[8]);
        this.gct = Double.valueOf(strs[9]);
    }

    @Override
    public String toString() {
        return "JStatResultForGcOld{" +
                "vmId=" +super.getVmId()+
                ", mc=" + mc +
                ", mu=" + mu +
                ", ccsc=" + ccsc +
                ", ccsu=" + ccsu +
                ", oc=" + oc +
                ", ou=" + ou +
                ", ygc=" + ygc +
                ", fgc=" + fgc +
                ", fgct=" + fgct +
                ", gct=" + gct +
                '}';
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

    public double getOc() {
        return oc;
    }

    public double getOu() {
        return ou;
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
