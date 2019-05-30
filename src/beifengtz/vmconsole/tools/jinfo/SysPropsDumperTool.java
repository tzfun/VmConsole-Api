package beifengtz.vmconsole.tools.jinfo;

import beifengtz.vmconsole.entity.jinfo.JInfoNode;
import beifengtz.vmconsole.entity.jinfo.JInfoResult;
import beifengtz.vmconsole.exception.NotAvailableException;
import beifengtz.vmconsole.tools.MyTool;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.runtime.VM;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jinfo.VmConsole-Api</p>
 * Created in 17:27 2019/5/30
 *
 * <p>系统属性处理工具</p>
 */
public class SysPropsDumperTool extends MyTool {


    public SysPropsDumperTool() {
    }

    public SysPropsDumperTool(JVMDebugger d) {
        super(d);
    }

    @Override
    public void run() {
        this.run(System.out);
    }

    /**
     * 将系统属性输出到打印流中
     *
     * @param out {@link PrintStream}打印流
     */
    public void run(PrintStream out){
        Properties sysProps = VM.getVM().getSystemProperties();
        if (sysProps != null) {
            Enumeration keys = sysProps.keys();

            while(keys.hasMoreElements()) {
                Object key = keys.nextElement();
                out.print(key);
                out.print(" = ");
                out.println(sysProps.get(key));
            }
        } else {
            out.println("System Properties info not available!");
        }
    }

    /**
     * 将系统属性保存至对象中
     *
     * @param jInfoResult {@link JInfoResult}对象
     * @throws NotAvailableException 不可获取异常
     */
    public void run(JInfoResult jInfoResult) throws NotAvailableException{
        Properties sysProps = VM.getVM().getSystemProperties();
        if (sysProps != null) {
            Enumeration keys = sysProps.keys();

            while(keys.hasMoreElements()) {
                Object key = keys.nextElement();
                jInfoResult.getInfoList().add(
                        new JInfoNode(key.toString(),sysProps.get(key).toString())
                );
            }
        } else {
            throw new NotAvailableException("System Properties info not available!");
        }
    }
}
