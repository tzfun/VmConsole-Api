package beifengtz.vmconsole.entity.jcmd;

import java.util.Objects;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jcmd.VmConsole-Api</p>
 * Created in 20:29 2019/5/30
 *
 * <p>虚拟机进程实体对象</p>
 */
public class JCmdProcess {
    private int vmId;
    private String content;

    public JCmdProcess(int vmId, String content) {
        this.vmId = vmId;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JCmdProcess)) return false;
        JCmdProcess that = (JCmdProcess) o;
        return getVmId() == that.getVmId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVmId());
    }

    @Override
    public String toString() {
        return "JCmdProcess{" +
                "vmId=" + vmId +
                ", content='" + content + '\'' +
                "}\n";
    }

    public int getVmId() {
        return vmId;
    }

    public String getContent() {
        return content;
    }
}
