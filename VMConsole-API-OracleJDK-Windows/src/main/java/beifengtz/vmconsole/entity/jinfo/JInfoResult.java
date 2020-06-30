package beifengtz.vmconsole.entity.jinfo;

import beifengtz.vmconsole.entity.JvmResult;

import java.util.ArrayList;
import java.util.List;


/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jinfo.VmConsole-Api</p>
 * Created in 16:45 2019/5/30
 */
public class JInfoResult extends JvmResult {
    private List<JInfoNode> infoList;
    private List<JInfoFlag> flags;
    private String commandLine;
    private String commandType;//   命令类型，设置或者是查询，设置为set，查询为query
    private boolean setSuccess;//   如果命令类型为set，此字段用于判断是否操作成功

    public static final String COMMANDTYPE_QUERY = "query";
    public static final String COMMANDTYPE_SET = "set";

    @Override
    public String toString() {
        return "JInfoResult{" +
                "infoList=" + infoList +
                ", flags=" + flags +
                ", commandLine='" + commandLine + '\'' +
                ", commandType='" + commandType + '\'' +
                ", setSuccess=" + setSuccess +
                "}\n";
    }

    public JInfoResult() {
        this.infoList = new ArrayList<>();
        this.flags = new ArrayList<>();
    }

    public List<JInfoNode> getInfoList() {
        return infoList;
    }

    public List<JInfoFlag> getFlags() {
        return flags;
    }

    public void setFlags(List<JInfoFlag> flags) {
        this.flags = flags;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public boolean isSetSuccess() {
        return setSuccess;
    }

    public void setSetSuccess(boolean setSuccess) {
        this.setSuccess = setSuccess;
    }
}
