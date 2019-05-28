package beifengtz.vmconsole.entity;


/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.javase_learning</p>
 * Created in 19:03 2019/5/27
 */
public class JStatResultForClass extends JStatResult implements JStatTransferable {

    //  加载的class数量
    private int loaded;
    //  所占用空间大小
    private double bytes;
    //  未加载数量
    private int unLoaded;
    //  未占用空间大小
    private double unBytes;
    //  时间
    private double time;

    public JStatResultForClass(String result) {
        String[] strs = result.trim().split("\\s+");
        this.loaded = Integer.valueOf(strs[0]);
        this.bytes = Double.valueOf(strs[1]);
        this.unLoaded = Integer.valueOf(strs[2]);
        this.unBytes = Double.valueOf(strs[3]);
        this.time = Double.valueOf(strs[4]);
    }

    @Override
    public String toString() {
        return "JStatResultForClass{" +
                "vmId=" + super.getVmId() +
                ", loaded=" + loaded +
                ", bytes=" + bytes +
                ", unLoaded=" + unLoaded +
                ", unBytes=" + unBytes +
                ", time=" + time +
                '}';
    }

    public int getLoaded() {
        return loaded;
    }

    public double getBytes() {
        return bytes;
    }

    public int getUnLoaded() {
        return unLoaded;
    }

    public double getUnBytes() {
        return unBytes;
    }

    public double getTime() {
        return time;
    }
}
