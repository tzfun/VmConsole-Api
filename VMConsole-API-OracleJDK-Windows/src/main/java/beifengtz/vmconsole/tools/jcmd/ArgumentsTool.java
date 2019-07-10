package beifengtz.vmconsole.tools.jcmd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.tools.jcmd.VmConsole-Api</p>
 * Created in 20:04 2019/5/30
 */
public class ArgumentsTool {
    private boolean listProcesses = false;
    private boolean listCounters = false;
    private boolean showUsage = false;
    private int pid = -1;
    private String command = null;
    private String processSubstring;

    public boolean isListProcesses() {
        return this.listProcesses;
    }

    public boolean isListCounters() {
        return this.listCounters;
    }

    public boolean isShowUsage() {
        return this.showUsage;
    }

    public int getPid() {
        return this.pid;
    }

    public String getCommand() {
        return this.command;
    }

    public String getProcessSubstring() {
        return this.processSubstring;
    }

    public ArgumentsTool(String[] var1) {
        if (var1.length != 0 && !var1[0].equals("-l")) {
            if (!var1[0].equals("-h") && !var1[0].equals("-help")) {
                try {
                    this.pid = Integer.parseInt(var1[0]);
                } catch (NumberFormatException var6) {
                    if (var1[0].charAt(0) != '-') {
                        this.processSubstring = var1[0];
                    }
                }

                StringBuilder var2 = new StringBuilder();

                for(int var3 = 1; var3 < var1.length; ++var3) {
                    if (var1[var3].equals("-f")) {
                        if (var1.length == var3 + 1) {
                            throw new IllegalArgumentException("No file specified for parameter -f");
                        }

                        if (var1.length == var3 + 2) {
                            try {
                                this.readCommandFile(var1[var3 + 1]);
                                return;
                            } catch (IOException var5) {
                                throw new IllegalArgumentException("Could not read from file specified with -f option: " + var1[var3 + 1]);
                            }
                        }

                        throw new IllegalArgumentException("Options after -f are not allowed");
                    }

                    if (var1[var3].equals("PerfCounter.print")) {
                        this.listCounters = true;
                    } else {
                        var2.append(var1[var3]).append(" ");
                    }
                }

                if (!this.listCounters && var2.length() == 0) {
                    throw new IllegalArgumentException("No command specified");
                } else {
                    this.command = var2.toString().trim();
                }
            } else {
                this.showUsage = true;
            }
        } else {
            this.listProcesses = true;
        }
    }

    private void readCommandFile(String var1) throws IOException {
        BufferedReader var2 = new BufferedReader(new FileReader(var1));
        Throwable var3 = null;

        try {
            StringBuilder var4 = new StringBuilder();

            String var5;
            while((var5 = var2.readLine()) != null) {
                var4.append(var5).append("\n");
            }

            this.command = var4.toString();
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (var2 != null) {
                if (var3 != null) {
                    try {
                        var2.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    var2.close();
                }
            }

        }
    }

    public static void usage() {
        System.out.println("Usage: jcmd <pid | main class> <command ...|PerfCounter.print|-f file>");
        System.out.println("   or: jcmd -l                                                    ");
        System.out.println("   or: jcmd -h                                                    ");
        System.out.println("                                                                  ");
        System.out.println("  command must be a valid jcmd command for the selected jvm.      ");
        System.out.println("  Use the command \"help\" to see which commands are available.   ");
        System.out.println("  If the pid is 0, commands will be sent to all Java processes.   ");
        System.out.println("  The main class argument will be used to match (either partially ");
        System.out.println("  or fully) the class used to start Java.                         ");
        System.out.println("  If no options are given, lists Java processes (same as -p).     ");
        System.out.println("                                                                  ");
        System.out.println("  PerfCounter.print display the counters exposed by this process  ");
        System.out.println("  -f  read and execute commands from the file                     ");
        System.out.println("  -l  list JVM processes on the local machine                     ");
        System.out.println("  -h  this help                                                   ");
    }
}
