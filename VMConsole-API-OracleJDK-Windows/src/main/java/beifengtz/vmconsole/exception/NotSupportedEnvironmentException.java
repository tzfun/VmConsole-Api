package beifengtz.vmconsole.exception;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.exception.VMConsoleAPI</p>
 * Created in 12:15 2019/7/10
 */
public class NotSupportedEnvironmentException extends Exception {
    public NotSupportedEnvironmentException(){super("Environment does not support");}
    public NotSupportedEnvironmentException(String message){super(message);}
}
