package beifengtz.vmconsole.entity.jstat;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.jstat</p>
 * Created in 20:03 2019/5/27
 *
 * <p>JStatResultForCompilation实体类</p>
 * <p>用于封装jstat -printcompilation命令的结果</p>
 */
public class JStatResultForCompilation extends JStatResult {
    //Compiled：最近编译方法的数量
    private int compiled;
    //Size：最近编译方法的字节码数量
    private int size;
    //Type：最近编译方法的编译类型。
    private int type;
    //Method：方法名标识。
    private String method;

    public JStatResultForCompilation(String result) {
        String[] strs = result.trim().split("\\s+");
        this.compiled = Integer.valueOf(strs[0]);
        this.size = Integer.valueOf(strs[1]);
        this.type = Integer.valueOf(strs[2]);
        if (strs.length >= 4){
            this.method = strs[3];
        }
    }

    @Override
    public String toString() {
        return "JStatResultForCompilation{" +
                "vmId=" + super.getVmId() +
                ", compiled=" + compiled +
                ", size=" + size +
                ", type=" + type +
                ", method='" + method + '\'' +
                "}\n";
    }

    public int getCompiled() {
        return compiled;
    }

    public int getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }
}
