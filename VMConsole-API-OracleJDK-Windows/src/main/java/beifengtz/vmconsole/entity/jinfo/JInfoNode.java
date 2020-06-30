package beifengtz.vmconsole.entity.jinfo;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jinfo.VmConsole-Api</p>
 * Created in 18:12 2019/5/30
 */
public class JInfoNode {

    private String option;
    private String value;

    public JInfoNode(String option, String value) {
        this.option = option;
        this.value = value;
    }

    @Override
    public String toString() {
        return "JInfoNode{" +
                "option='" + option + '\'' +
                ", value='" + value + '\'' +
                "}\n";
    }

    public String getOption() {
        return option;
    }

    public String getValue() {
        return value;
    }
}
