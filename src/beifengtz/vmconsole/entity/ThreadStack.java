package beifengtz.vmconsole.entity;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.VmConsole-Api</p>
 * Created in 21:48 2019/5/29
 */
public class ThreadStack {
    private String id;
    private String state;
    private StringBuilder stacks;
    private String deadLocks;
    private String currentJavaSP;
    private String concurrentLocks;

    @Override
    public String toString() {
        return "ThreadStack{" +
                "id='" + id + "\'\n" +
                ", state='" + state + "\'\n" +
                ", stacks=" + stacks + "\n" +
                ", deadLocks='" + deadLocks + "\'\n" +
                ", currentJavaSP='" + currentJavaSP + "\'\n" +
                ", concurrentLocks='" + concurrentLocks + "\'\n" +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurrentJavaSP() {
        return currentJavaSP;
    }

    public String getDeadLocks() {
        return deadLocks;
    }

    public void setDeadLocks(String deadLocks) {
        this.deadLocks = deadLocks;
    }

    public StringBuilder getStacks() {
        return stacks;
    }

    public void setStacks(StringBuilder stacks) {
        this.stacks = stacks;
    }

    public void setCurrentJavaSP(String currentJavaSP) {
        this.currentJavaSP = currentJavaSP;
    }

    public String getConcurrentLocks() {
        return concurrentLocks;
    }

    public void setConcurrentLocks(String concurrentLocks) {
        this.concurrentLocks = concurrentLocks;
    }
}
