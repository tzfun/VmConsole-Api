package beifengtz.vmconsole.entity;

import java.util.Arrays;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.vmconsole.entity.javase_learning</p>
 * Created in 19:22 2019/5/27
 */
public class JStatResultForCompiler extends JStatResult implements JStatTransferable {
    //Compiled：编译数量。
    private int compiled;
    //Failed：失败数量
    private int failed;
    //Invalid：不可用数量
    private int invalid;
    //Time：时间
    private double time;
    //FailedType：失败类型
    private int failedType;
    //FailedMethod：失败的方法
    private String failedMethod;

    public JStatResultForCompiler(String result) {
        String[] strs = result.trim().split("\\s+");
        this.compiled = Integer.valueOf(strs[0]);
        this.failed = Integer.valueOf(strs[1]);
        this.invalid = Integer.valueOf(strs[2]);
        this.time = Double.valueOf(strs[3]);
        this.failedType = Integer.valueOf(strs[4]);
        if (strs.length >= 6){
            this.failedMethod = strs[5];
        }
    }

    @Override
    public String toString() {
        return "JStatResultForCompiler{" +
                "vmId=" + super.getVmId() +
                ", compiled=" + compiled +
                ", failed=" + failed +
                ", invalid=" + invalid +
                ", time=" + time +
                ", failedType=" + failedType +
                ", failedMethod='" + failedMethod + '\'' +
                '}';
    }

    public int getCompiled() {
        return compiled;
    }

    public int getFailed() {
        return failed;
    }

    public int getInvalid() {
        return invalid;
    }

    public double getTime() {
        return time;
    }

    public int getFailedType() {
        return failedType;
    }

    public String getFailedMethod() {
        return failedMethod;
    }
}
