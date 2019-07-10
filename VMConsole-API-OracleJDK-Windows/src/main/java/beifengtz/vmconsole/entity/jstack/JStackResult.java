package beifengtz.vmconsole.entity.jstack;

import beifengtz.vmconsole.entity.JvmResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jstack</p>
 * Created in 21:45 2019/5/29
 *
 * <p>JStackResult实体类</p>
 * <p>用于封装jstack命令返回的结果集</p>
 */
public class JStackResult extends JvmResult {
    private String deadlocks;
    private String concurrentLocks;
    private List<ThreadStack> threadStacks;
    private ArrayList<StringBuilder> jniStack;
    private String threadDump;

    @Override
    public String toString() {
        return "JStackResult{ " +
                "vmId=" + super.getVmId() +
                ", vmVersion='" + super.getVmVersion() + '\'' +
                ", deadlocks='" + deadlocks + '\'' +
                ", concurrentLocks='" + concurrentLocks + '\'' +
                ", threadStacks=" + threadStacks +
                ", jniStack=" + jniStack +
                ", threadDump='" + threadDump + "\'" +
                "} ";
    }

    public List<ThreadStack> getThreadStacks() {
        return threadStacks;
    }

    public void setThreadStacks(List<ThreadStack> threadStacks) {
        this.threadStacks = threadStacks;
    }

    public String getDeadlocks() {
        return deadlocks;
    }

    public void setDeadlocks(String deadlocks) {
        this.deadlocks = deadlocks;
    }

    public ArrayList<StringBuilder> getJniStack() {
        return jniStack;
    }

    public void setJniStack(ArrayList<StringBuilder> jniStack) {
        this.jniStack = jniStack;
    }

    public String getConcurrentLocks() {
        return concurrentLocks;
    }

    public void setConcurrentLocks(String concurrentLocks) {
        this.concurrentLocks = concurrentLocks;
    }

    public String getThreadDump() {
        return threadDump;
    }

    public void setThreadDump(String threadDump) {
        this.threadDump = threadDump;
    }
}
