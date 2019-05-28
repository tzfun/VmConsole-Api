package beifengtz.vmconsole.util;

import beifengtz.vmconsole.entity.JStatResult;
import sun.jvmstat.monitor.Monitor;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.StringMonitor;
import sun.tools.jstat.OutputFormatter;

import java.io.PrintStream;
import java.util.*;
import java.util.regex.PatternSyntaxException;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.util.javase_learning</p>
 * Created in 18:02 2019/5/27
 */
public class JStatUtil {
    private MonitoredVm monitoredVm;
    private volatile boolean active = true;

    public JStatUtil(MonitoredVm var1) {
        this.monitoredVm = var1;
    }

    public void printNames(String var1, Comparator<Monitor> var2, boolean var3,
                           PrintStream var4,ArrayList<JStatResult> resList) throws MonitorException, PatternSyntaxException {
        List var5 = this.monitoredVm.findByPattern(var1);
        Collections.sort(var5, var2);
        Iterator var6 = var5.iterator();
        ArrayList<String> list = new ArrayList<>();
        while(true) {
            Monitor var7;
            do {
                if (!var6.hasNext()) {
                    return;
                }

                var7 = (Monitor)var6.next();
            } while(!var7.isSupported() && !var3);
            //  注入names列表
            list.add(var7.getName());
            resList.get(0).setNames(list);
//            var4.println(var7.getName());
        }
    }

    public void printSnapShot(String var1, Comparator<Monitor> var2, boolean var3,
                              boolean var4, PrintStream var5,ArrayList<JStatResult> resList) throws MonitorException, PatternSyntaxException {
        List var6 = this.monitoredVm.findByPattern(var1);
        Collections.sort(var6, var2);
        this.printListWithReturn(var6, var3, var4, var5,resList);
    }

    public void printListWithReturn(List<Monitor> var1, boolean var2, boolean var3, PrintStream var4,ArrayList<JStatResult> resList) throws MonitorException {
        Iterator var5 = var1.iterator();
        ArrayList<String> snap = new ArrayList<>();
        while(true) {
            Monitor var6;
            do {
                if (!var5.hasNext()) {
                    return;
                }

                var6 = (Monitor)var5.next();
            } while(!var6.isSupported() && !var3);

            StringBuilder var7 = new StringBuilder();
            var7.append(var6.getName()).append("=");
            if (var6 instanceof StringMonitor) {
                var7.append("\"").append(var6.getValue()).append("\"");
            } else {
                var7.append(var6.getValue());
            }

            if (var2) {
                var7.append(" ").append(var6.getUnits());
                var7.append(" ").append(var6.getVariability());
                var7.append(" ").append(var6.isSupported() ? "Supported" : "Unsupported");
            }
            snap.add(var7.toString());
//            var4.println(var7);
            resList.get(0).setSnapShot(snap);
        }
    }

    public void printList(List<Monitor> var1, boolean var2, boolean var3, PrintStream var4) throws MonitorException {
        Iterator var5 = var1.iterator();

        while(true) {
            Monitor var6;
            do {
                if (!var5.hasNext()) {
                    return;
                }

                var6 = (Monitor)var5.next();
            } while(!var6.isSupported() && !var3);

            StringBuilder var7 = new StringBuilder();
            var7.append(var6.getName()).append("=");
            if (var6 instanceof StringMonitor) {
                var7.append("\"").append(var6.getValue()).append("\"");
            } else {
                var7.append(var6.getValue());
            }

            if (var2) {
                var7.append(" ").append(var6.getUnits());
                var7.append(" ").append(var6.getVariability());
                var7.append(" ").append(var6.isSupported() ? "Supported" : "Unsupported");
            }

            var4.println(var7);
        }
    }

    public void stopLogging() {
        this.active = false;
    }

    public void logSamples(OutputFormatter var1, int var2, int var3, int var4, PrintStream var5, int vmId,String option,ArrayList<JStatResult> resList) throws MonitorException {
        long var6 = 0L;
        int var8 = 0;
        int var9 = var2;
        if (var2 == 0) {
//            var5.println(var1.getHeader());
            var9 = -1;
        }

        while(this.active) {
            if (var9 > 0) {
                --var8;
                if (var8 <= 0) {
                    var8 = var9;
//                    var5.println(var1.getHeader());
                }
            }
//            var5.println(var1.getRow());

            JStatResult jStatResult = JStatResultFactory.getJStatResult(option,var1.getRow());
            jStatResult.setVmId(vmId);
            jStatResult.setStrResult(var1.getRow());
            resList.add(jStatResult);
            if (var4 > 0 && ++var6 >= (long)var4) {
                break;
            }

            try {
                Thread.sleep((long)var3);
            } catch (Exception var11) {
            }
        }

    }
}
