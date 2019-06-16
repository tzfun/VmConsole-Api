package beifengtz.vmconsole.entity.jmap;

import beifengtz.vmconsole.entity.JvmResult;

import java.util.HashMap;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jmap.VmConsole-Api</p>
 * Created in 13:04 2019/6/16
 */
public class JMapForHeap extends JvmResult {
    private HashMap<String,Object> heapConf;
    private JMapForHeapUsage heapUsage;

    {
        heapConf = new HashMap<>(12);
        heapUsage = new JMapForHeapUsage();
    }

    @Override
    public String toString() {
        return "JMapForHeap{" +
                "heapConf=" + heapConf +
                ", heapUsage=" + heapUsage +
                '}';
    }

    public HashMap<String, Object> getHeapConf() {
        return heapConf;
    }

    public void setHeapConf(HashMap<String, Object> heapConf) {
        this.heapConf = heapConf;
    }

    public JMapForHeapUsage getHeapUsage() {
        return heapUsage;
    }

    public void setHeapUsage(JMapForHeapUsage heapUsage) {
        this.heapUsage = heapUsage;
    }
}
