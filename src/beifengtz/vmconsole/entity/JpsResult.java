package beifengtz.vmconsole.entity;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.javase_learning</p>
 * Created in 13:26 2019/5/27
 *
 *
 *
 * <p>用于封装jps命令返回的结果集，将各个结果参数分离出来</p>
 */
public class JpsResult {

    //  虚拟机唯一识别码
    private Integer vmId;

    //  主类
    private String mainClass;

    //  虚拟机主类参数信息
    private String mainArgs;

    //  虚拟机参数信息
    private String vmArgs;

    //  虚拟机标志数据
    private String vmFlags;

    //  jps命令错误信息
    private String errMessage;

    //  jps命令错误异常栈
    private ArrayList<Object> errStacks;

    //  String形式的jps命令结果，与原始jps命令结果一样
    private String strResult;

    @Override
    public String toString() {
        return "JpsResult{" +
                "vmId=" + vmId +
                ", mainClass='" + mainClass + '\'' +
                ", mainArgs='" + mainArgs + '\'' +
                ", vmArgs='" + vmArgs + '\'' +
                ", vmFlags='" + vmFlags + '\'' +
                ", errMessage='" + errMessage + '\'' +
                ", errStacks=" + errStacks +
                ", strResult='" + strResult + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpsResult)) return false;
        JpsResult jpsResult = (JpsResult) o;
        return getVmId().equals(jpsResult.getVmId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVmId());
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public Integer getVmId() {
        return vmId;
    }

    public void setVmId(Integer vmId) {
        this.vmId = vmId;
    }

    public String getMainArgs() {
        return mainArgs;
    }

    public void setMainArgs(String mainArgs) {
        this.mainArgs = mainArgs;
    }

    public String getVmArgs() {
        return vmArgs;
    }

    public void setVmArgs(String vmArgs) {
        this.vmArgs = vmArgs;
    }

    public String getVmFlags() {
        return vmFlags;
    }

    public void setVmFlags(String vmFlags) {
        this.vmFlags = vmFlags;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public ArrayList<Object> getErrStacks() {
        return errStacks;
    }

    public void setErrStacks(ArrayList<Object> errStacks) {
        this.errStacks = errStacks;
    }

    public String getStrResult() {
        return strResult;
    }

    public void setStrResult(String strResult) {
        this.strResult = strResult;
    }
}
