package beifengtz.vmconsole.exception;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.exception.VmConsole-Api</p>
 * Created in 12:43 2019/5/30
 *
 * <p>资源不可用异常类</p>
 */
public class NotAvailableException extends Exception {
    public NotAvailableException() {
    }

    public NotAvailableException(String message) {
        super(message);
    }
}
