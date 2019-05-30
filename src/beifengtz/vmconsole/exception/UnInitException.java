package beifengtz.vmconsole.exception;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.exception.VmConsole-Api</p>
 * Created in 12:29 2019/5/30
 *
 * <p>未初始化异常类</p>
 */
public class UnInitException extends Exception {
    public UnInitException() {
        super();
    }

    public UnInitException(String message) {
        super(message);
    }
}
