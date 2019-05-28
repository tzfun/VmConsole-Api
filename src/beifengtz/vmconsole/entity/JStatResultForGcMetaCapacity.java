package beifengtz.vmconsole.entity;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.javase_learning</p>
 * Created in 19:57 2019/5/27
 */
public class JStatResultForGcMetaCapacity extends JStatResult implements JStatTransferable {
    //MCMN:最小元数据容量
    private double mcmn;
    //MCMX：最大元数据容量
    private double mcmx;
    //MC：当前元数据空间大小
    private double mc;
    //CCSMN：最小压缩类空间大小
    private double ccsmn;
    //CCSMX：最大压缩类空间大小
    private double ccsmx;
    //CCSC：当前压缩类空间大小
    private double ccsc;
    //YGC：年轻代垃圾回收次数
    private int ygc;
    //FGC：老年代垃圾回收次数
    private int fgc;
    //FGCT：老年代垃圾回收消耗时间
    private double fgct;
    //GCT：垃圾回收消耗总时间
    private double gct;

    public JStatResultForGcMetaCapacity(String result) {
        String[] strs = result.trim().split("\\s+");
        this.mcmn = Double.valueOf(strs[0]);
        this.mcmx = Double.valueOf(strs[1]);
        this.mc = Double.valueOf(strs[2]);
        this.ccsmn = Double.valueOf(strs[3]);
        this.ccsmx = Double.valueOf(strs[4]);
        this.ccsc = Double.valueOf(strs[5]);
        this.ygc = Integer.valueOf(strs[6]);
        this.fgc = Integer.valueOf(strs[7]);
        this.fgct = Double.valueOf(strs[8]);
        this.gct = Double.valueOf(strs[9]);
    }

    @Override
    public String toString() {
        return "JStatResultForGcMetaCapacity{" +
                "vmId=" + super.getVmId() +
                ", mcmn=" + mcmn +
                ", mcmx=" + mcmx +
                ", mc=" + mc +
                ", ccsmn=" + ccsmn +
                ", ccsmx=" + ccsmx +
                ", ccsc=" + ccsc +
                ", ygc=" + ygc +
                ", fgc=" + fgc +
                ", fgct=" + fgct +
                ", gct=" + gct +
                '}';
    }

    public double getMcmn() {
        return mcmn;
    }

    public double getMcmx() {
        return mcmx;
    }

    public double getMc() {
        return mc;
    }

    public double getCcsmn() {
        return ccsmn;
    }

    public double getCcsmx() {
        return ccsmx;
    }

    public double getCcsc() {
        return ccsc;
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
