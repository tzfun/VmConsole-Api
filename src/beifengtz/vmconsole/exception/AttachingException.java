package beifengtz.vmconsole.exception;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.exception</p>
 * Created in 16:07 2019/5/30
 *
 * <p>连接异常类</p>
 */
public class AttachingException extends Exception{
    public AttachingException() {
    }

    public AttachingException(String message) {
        super(message);
    }
}
