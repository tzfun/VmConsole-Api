package beifengtz.vmconsole.security;

import beifengtz.vmconsole.exception.NotSupportedEnvironmentException;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.security.VMConsoleAPI</p>
 * Created in 12:39 2019/7/10
 */
public class SystemEnvironment {
    private static boolean isWindows() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) return true;
        return false;
    }

    private static boolean isLinux() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Linux")) return true;
        return false;
    }

    private static boolean isOpenJdk() {
        String jdk = System.getProperty("java.vm.name");
        if (jdk.contains("OpenJDK")) return true;
        return false;
    }

    private static boolean isOracleJdk() {
        String jdk = System.getProperty("java.vm.name");
        if (jdk.contains("Java HotSpot")) return true;
        return false;
    }

    public static void checkWindowsAndOpenJdk() throws NotSupportedEnvironmentException {
        if (isWindows() && isOpenJdk()) return;
        else throw new NotSupportedEnvironmentException("Environment does not support, and must be Windows OS and OpenJdk.");
    }

    public static void checkWindowsAndOracleJdk() throws NotSupportedEnvironmentException{
        if (isWindows() && isOracleJdk()) return;
        else throw new NotSupportedEnvironmentException("Environment does not support, and must be Windows OS and OracleJdk.");
    }

    public static void checkLinuxAndOracleJdk() throws NotSupportedEnvironmentException{
        if (isLinux() && isOracleJdk()) return;
        else throw new NotSupportedEnvironmentException("Environment does not support, and must be Linux OS and OracleJdk.");
    }

    public static void checkLinuxAndOpenJdk() throws NotSupportedEnvironmentException{
        if (isLinux() && isOpenJdk()) return;
        else throw new NotSupportedEnvironmentException("Environment does not support, and must be Linux OS and OpenJdk.");
    }
}
