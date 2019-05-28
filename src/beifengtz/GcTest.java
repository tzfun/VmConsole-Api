package beifengtz;

/**
 * @author beifengtz
 * <a href='http://www.beifengtz.com'>www.beifengtz.com</a>
 * <p>location: beifengtz.javase_learning</p>
 * Created in 19:38 2019/5/23
 */
public class GcTest {
    public static GcTest instance = null;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("收集器检测到finalize方法，对象即将获得一次重生的机会");
        instance = this;
    }

    public static void main(String[] args) throws InterruptedException{
        instance = new GcTest();
        //  引用置为空，堆内对象将视为垃圾
        instance = null;
        //  执行gc
        System.gc();
        Thread.sleep(500);
        //  虽然执行了gc，但是可能在finalize方法中获得重生，
        //  因此可能会打印出myObject的地址
        System.out.println(instance);
        //  最后打印出jvm.GcTest@7cc355be
    }
}
