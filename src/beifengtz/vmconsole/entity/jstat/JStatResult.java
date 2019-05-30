package beifengtz.vmconsole.entity.jstat;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jstat</p>
 * Created in 16:32 2019/5/27
 *
 * <p>JStatResult实体类</p>
 * <p>用于封装jstat命令的结果，通用结果集，所有jstat实体类的父类</p>
 */
public class JStatResult {

    //  虚拟机唯一识别id
    private Integer vmId;

    //  虚拟机信息列表，仅包含名字
    private ArrayList<String> names;

    //  虚拟机信息及其值列表，包含名字和对应值
    private ArrayList<String> snapShot;

    //  字符串结果，直接使用jstat命令的结果，
    //  当使用-list、-snap时该字段不存值，
    //  使用-class、gc、compiler等命令时保存相应结果值
    private String strResult;

    @Override
    public String toString() {
        return "JStatResult{" +
                "vmId=" + vmId +
                ", names=" + names +
                ", snapShot=" + snapShot +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JStatResult)) return false;
        JStatResult that = (JStatResult) o;
        return getVmId().equals(that.getVmId());
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

    public String getStrResult() {
        return strResult;
    }

    public void setStrResult(String strResult) {
        this.strResult = strResult;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getSnapShot() {
        return snapShot;
    }

    public void setSnapShot(ArrayList<String> snapShot) {
        this.snapShot = snapShot;
    }
}
