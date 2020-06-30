package beifengtz.vmconsole.entity.jmap;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jmap.VmConsole-Api</p>
 * Created in 14:34 2019/6/16
 */
public class HeapForGen {
    private HeapSpace newGen;// New Generation (Eden + 1 Survivor Space)
    private HeapSpace youngEden;
    private HeapSpace youngFrom;
    private HeapSpace youngTo;
    private HeapSpace oldGen;// Old Generation

    @Override
    public String toString() {
        return "HeapForGen{" +
                "newGen=" + newGen +
                ", youngEden=" + youngEden +
                ", youngFrom=" + youngFrom +
                ", youngTo=" + youngTo +
                ", oldGen=" + oldGen +
                "}\n";
    }

    public HeapSpace getNewGen() {
        return newGen;
    }

    public void setNewGen(HeapSpace newGen) {
        this.newGen = newGen;
    }

    public HeapSpace getYoungEden() {
        return youngEden;
    }

    public void setYoungEden(HeapSpace youngEden) {
        this.youngEden = youngEden;
    }

    public HeapSpace getYoungFrom() {
        return youngFrom;
    }

    public void setYoungFrom(HeapSpace youngFrom) {
        this.youngFrom = youngFrom;
    }

    public HeapSpace getYoungTo() {
        return youngTo;
    }

    public void setYoungTo(HeapSpace youngTo) {
        this.youngTo = youngTo;
    }

    public HeapSpace getOldGen() {
        return oldGen;
    }

    public void setOldGen(HeapSpace oldGen) {
        this.oldGen = oldGen;
    }
}
