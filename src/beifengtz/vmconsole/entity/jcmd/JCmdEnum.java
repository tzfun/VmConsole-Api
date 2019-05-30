package beifengtz.vmconsole.entity.jcmd;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jcmd.VmConsole-Api</p>
 * Created in 19:59 2019/5/30
 *
 * <p>jcmd提供的可执行命令枚举类</p>
 */
public enum JCmdEnum {
    JFR_STOP("JFR.stop"),
    JFR_START("JFR.start"),
    JFR_DUMP("JFR.dump"),
    JFR_CHECK("JFR.check"),
    VM_NATIVE_MEMORY("VM.native_memory"),
    VM_CHECK_COMMERCIAL_FEATURES("VM.check_commercial_features"),
    VM_UNLOCK_COMMERCIAL_FEATURES("VM.unlock_commercial_features"),
    MANAGEMENTAGENT_STOP("ManagementAgent.stop"),
    MANAGEMENTAGENT_START_LOCAL("ManagementAgent.start_local"),
    MANAGEMENTAGENT_START("ManagementAgent.start"),
    GC_ROTATE_LOG("GC.rotate_log"),
    THREAD_PRINT("Thread.print"),
    GC_CLASS_STATS("GC.class_stats"),
    GC_CLASS_HISTOGRAM("GC.class_histogram"),
    GC_HEAP_DUMP("GC.heap_dump"),
    GC_RUN_FINALIZATION("GC.run_finalization"),
    GC_RUN("GC.run"),
    VM_UPTIME("VM.uptime"),
    VM_FLAGS("VM.flags"),
    VM_SYSTEM_PROPERTIES("VM.system_properties"),
    VM_COMMAND_LINE("VM.command_line"),
    VM_VERSION("VM.version");

    private String command;

    JCmdEnum(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
