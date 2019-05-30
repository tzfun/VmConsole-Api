package beifengtz.vmconsole.exception;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.exception</p>
 * Created in 12:46 2019/5/30
 *
 * <p>配置信息错误异常类</p>
 */
public class ConfigurationException extends Exception {
    public ConfigurationException() {
    }

    public ConfigurationException(String message) {
        super(message);
    }
}
