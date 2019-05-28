package beifengtz.vmconsole.entity;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.javase_learning</p>
 * Created in 19:31 2019/5/27
 */
public class JStatResultForGcCapacity extends JStatResult implements JStatTransferable {

    //NGCMN：新生代最小容量
    private double ngcmn;
    //NGCMX：新生代最大容量
    private double ngcmx;
    //NGC：当前新生代容量
    private double ngc;
    //S0C：第一个幸存区大小
    private double s0c;
    //S1C：第二个幸存区的大小
    private double s1c;
    //EC：Eden区的大小
    private double ec;
    //OGCMN：老年代最小容量
    private double ogcmn;
    //OGCMX：老年代最大容量
    private double ogcmx;
    //OGC：当前老年代大小
    private double ogc;
    //OC:当前老年代大小
    private double oc;
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
    //YGC：年轻代gc次数
    private int ygc;
    //FGC：老年代GC次数
    private int fgc;

    public JStatResultForGcCapacity(String result) {
        String[] strs = result.trim().split("\\s+");
        this.ngcmn = Double.valueOf(strs[0]);
        this.ngcmx = Double.valueOf(strs[1]);
        this.ngc = Double.valueOf(strs[2]);
        this.s0c = Double.valueOf(strs[3]);
        this.s1c = Double.valueOf(strs[4]);
        this.ec = Double.valueOf(strs[5]);
        this.ogcmn = Double.valueOf(strs[6]);
        this.ogcmx = Double.valueOf(strs[7]);
        this.ogc = Double.valueOf(strs[8]);
        this.oc = Double.valueOf(strs[9]);
        this.mcmn = Double.valueOf(strs[10]);
        this.mcmx = Double.valueOf(strs[11]);
        this.mc = Double.valueOf(strs[12]);
        this.ccsmn = Double.valueOf(strs[13]);
        this.ccsmx = Double.valueOf(strs[14]);
        this.ccsc = Double.valueOf(strs[15]);
        this.ygc = Integer.valueOf(strs[16]);
        this.fgc = Integer.valueOf(strs[17]);
    }

    @Override
    public String toString() {
        return "JStatResultForGcCapacity{" +
                "vmId=" + super.getVmId() +
                ", ngcmn=" + ngcmn +
                ", ngcmx=" + ngcmx +
                ", ngc=" + ngc +
                ", s0c=" + s0c +
                ", s1c=" + s1c +
                ", ec=" + ec +
                ", ogcmn=" + ogcmn +
                ", ogcmx=" + ogcmx +
                ", ogc=" + ogc +
                ", oc=" + oc +
                ", mcmn=" + mcmn +
                ", mcmx=" + mcmx +
                ", mc=" + mc +
                ", ccsmn=" + ccsmn +
                ", ccsmx=" + ccsmx +
                ", ccsc=" + ccsc +
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

    public double getS0c() {
        return s0c;
    }

    public double getS1c() {
        return s1c;
    }

    public double getEc() {
        return ec;
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
}
