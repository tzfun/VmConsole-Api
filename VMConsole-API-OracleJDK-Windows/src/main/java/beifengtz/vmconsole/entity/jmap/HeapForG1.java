package beifengtz.vmconsole.entity.jmap;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jmap.VmConsole-Api</p>
 * Created in 14:34 2019/6/16
 */
public class HeapForG1 {
    private HeapSpace heap;
    private HeapSpace eden;
    private HeapSpace survivor;
    private HeapSpace old;

    @Override
    public String toString() {
        return "HeapForG1{" +
                "heap=" + heap +
                ", eden=" + eden +
                ", survivor=" + survivor +
                ", old=" + old +
                "}\n";
    }

    public HeapSpace getHeap() {
        return heap;
    }

    public void setHeap(HeapSpace heap) {
        this.heap = heap;
    }

    public HeapSpace getEden() {
        return eden;
    }

    public void setEden(HeapSpace eden) {
        this.eden = eden;
    }

    public HeapSpace getSurvivor() {
        return survivor;
    }

    public void setSurvivor(HeapSpace survivor) {
        this.survivor = survivor;
    }

    public HeapSpace getOld() {
        return old;
    }

    public void setOld(HeapSpace old) {
        this.old = old;
    }
}
