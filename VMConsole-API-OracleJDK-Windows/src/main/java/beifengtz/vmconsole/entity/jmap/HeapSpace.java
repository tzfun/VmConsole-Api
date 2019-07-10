package beifengtz.vmconsole.entity.jmap;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jmap.VmConsole-Api</p>
 * Created in 14:36 2019/6/16
 */
public class HeapSpace {
    private String name;
    private String regions;
    private String capacity;
    private String used;
    private String free;
    private String useRatio;

    @Override
    public String toString() {
        return "HeapSpace{" +
                "name='" + name + '\'' +
                ", regions='" + regions + '\'' +
                ", capacity='" + capacity + '\'' +
                ", used='" + used + '\'' +
                ", free='" + free + '\'' +
                ", useRatio='" + useRatio + '\'' +
                '}';
    }

    public HeapSpace(String name, String capacity, String used, String free, String useRatio) {
        this.name = name;
        this.capacity = capacity;
        this.used = used;
        this.free = free;
        this.useRatio = useRatio;
    }

    public HeapSpace(String name, String regions, String capacity, String used, String free, String useRatio) {
        this.name = name;
        this.regions = regions;
        this.capacity = capacity;
        this.used = used;
        this.free = free;
        this.useRatio = useRatio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getUseRatio() {
        return useRatio;
    }

    public void setUseRatio(String useRatio) {
        this.useRatio = useRatio;
    }
}
