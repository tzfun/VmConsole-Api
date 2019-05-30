package beifengtz.vmconsole.entity.jinfo;

import beifengtz.vmconsole.entity.JvmResult;


/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jinfo.VmConsole-Api</p>
 * Created in 16:45 2019/5/30
 */
public class JInfoResult extends JvmResult {
    private String option;
    private String value;

    @Override
    public String toString() {
        return "JInfoResult{" +
                "vmId=" + super.getVmId() +
                ", option='" + option + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
