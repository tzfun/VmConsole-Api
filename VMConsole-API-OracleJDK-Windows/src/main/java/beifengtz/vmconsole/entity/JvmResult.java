package beifengtz.vmconsole.entity;

import java.util.Objects;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.VmConsole-Api</p>
 * Created in 16:53 2019/5/30
 *
 * <p>jvm信息对象</p>
 */
public class JvmResult {
    //  虚拟机唯一识别id
    private Integer vmId;

    private String vmVersion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JvmResult)) return false;
        JvmResult jvmResult = (JvmResult) o;
        return getVmId().equals(jvmResult.getVmId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVmId());
    }

    public Integer getVmId() {
        return vmId;
    }

    public void setVmId(Integer vmId) {
        this.vmId = vmId;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public void setVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
    }
}
