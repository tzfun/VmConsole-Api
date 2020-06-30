package beifengtz.vmconsole.entity.jcmd;

import beifengtz.vmconsole.entity.JvmResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jcmd.VmConsole-Api</p>
 * Created in 20:28 2019/5/30
 *
 * <p>JCmd返回结果集对象</p>
 */
public class JCmdResult extends JvmResult {
    private List<JCmdProcess> processes;
    private String result;

    @Override
    public String toString() {
        return "JCmdResult{" +
                "processes=" + processes +
                ", result='" + result + '\'' +
                "}\n";
    }

    public JCmdResult() {
        this.processes = new ArrayList<>();
    }

    public List<JCmdProcess> getProcesses() {
        return processes;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
