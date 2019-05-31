# VmConsole-Api
🔨VmConsole-Api是一个jvm虚拟机性能监控API，将oracle jdk提供的tools.jar包进行了功能拓展，对一些监控命令结果进行了封装，你可以方便地从对象中读取每一个参数和结果。该类库并不是简单地使用运行时exec()调用jps、jstat、jstack等命令，而是从底层深度拓展而来，所以你不需要配置java环境变量就可以通过Java代码对虚拟机进行监控。

**说明**：本工具API适用于jdk8及以上（jdk7以下部分功能可能会出错），虚拟机必须是**HotSpot**。

**本项目遵守Apache开源协议，核心代码由本人根据jdk源码拓展或改造而来，无模仿或抄袭。**

# API文档
我已经将相关类生成了API文档，请访问：[doc.vmconsole.beifengtz.com](http://doc.vmconsole.beifengtz.com)

# 如何使用
我已将核心依赖库打包生成了一个jar包(3MB左右)，你只需要将jar包导入到项目就可以直接使用其中的类，无需导入任何其他包。

* **jar包下载**
jar包下载地址（百度云盘地址）：[https://pan.baidu.com/s/1ydtemNmUzBUTOOP_Qi7VgQ](https://pan.baidu.com/s/1ydtemNmUzBUTOOP_Qi7VgQ)（提取码：57uc）

* **Maven仓库**
```
<dependency>
  <groupId>com.github.tzfun</groupId>
  <artifactId>vmconsole</artifactId>
  <version>1.0.0</version>
</dependency>
```

使用示例：
```
import beifengtz.vmconsole.JpsCmd;
import beifengtz.vmconsole.entity.jps.JpsResult;

import java.util.List;

public class JpsTest {
    public static void main(String[] args) throws Exception{
        //  获取本地虚拟机所有虚拟机进程id
        List<JpsResult> jpsResult1 = JpsCmd.quit();
        //  获取本地虚拟机所有虚拟机进程id，同时获取虚拟机参数
        List<JpsResult> jpsResult2 = JpsCmd.withVmArgs();
        //  获取本地虚拟机所有虚拟机进程id，同时获取主类参数
        List<JpsResult> jpsResult3 = JpsCmd.withMainClassArgs();
        //  获取本地虚拟机所有虚拟机进程id，同时获取jar或者主类全称
        List<JpsResult> jpsResult4 = JpsCmd.withFullName();

        //  自定义执行jps命令的参数，此处相当于在命令行执行jps -l
        List<JpsResult> jpsResult4 = JpsCmd.run(new String[]("-l"));
    }
}
```

# 联系

如果你在使用中发现了Bug，或者想对本作品提出意见或建议，请发送邮件到作者本人邮箱：**beifengtz@163.com**

# 作者

* 个人首页：[www.beifengtz.com](http://www.beifengtz.com)
* 博客地址：[blog.beifengtz.com](http://blog.beifengtz.com)
* GitHub：[https://github.com/tzfun](https://github.com/tzfun)
* 个人微信公众号：**北风IT之路**

<img src=https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/beifengtz/%E5%85%AC%E4%BC%97%E5%8F%B7.png width=200/>

# VmConsole详细介绍文档

目前为止，我已经封装了jps、jstat、jstack、jinfo、jcmd五个命令，使用者可以直接通过每个工具类的run方法直接执行相关命令（主需要传入参数，不需要传入jps、jstat等命令），命令格式和jdk提供的工具命令格式一模一样，也可以使用我已经封装好了的方法，不过为了方便以及可靠性保证，建议你使用已经封装好的方法。

## 1.jps

在jdk中的jps可以获取虚拟机实例的虚拟机唯一识别VMID、虚拟机参数、主类参数和jar全称等。

调用类位置：`beifengtz.vmconsole.JpsCmd`

返回的JpsResult对象：`beifengtz.vmconsole.entity.jps.JpsCmd`

**JpsResult对象参数及其含义：**
```
//  主类信息
private String mainClass;

//  虚拟机主类参数信息
private String mainArgs;

//  虚拟机参数信息
private String vmArgs;

//  虚拟机标志数据
private String vmFlags;

//  jps命令错误信息
private String errMessage;

//  jps命令错误异常栈
private ArrayList<Object> errStacks;

//  String形式的jps命令结果，与原始jps命令结果一样
private String strResult;
```

**JpsCmd可使用方法：**
```
//  仅获取虚拟机的vmId，封装命令：jps -q 
public static List<JpsResult> quit();

//  获取虚拟机的vmId和main函数的参数，封装命令：jps -m 
public static List<JpsResult> withMainClassArgs();

//  获取虚拟机的vmId和主类或Jar的全名，封装命令：jps -l 
public static List<JpsResult> withFullName();

//  获取虚拟机的vmId和虚拟机参数，封装命令：jps -v
public static List<JpsResult> withVmArgs();

//  jps命令执行函数，用户可传入自定义命令执行
public static List<JpsResult> run(String[] var0)
```

## 2.jstat

在jdk中的jstat可以获取虚拟机内存状态信息，包括GC信息、编译类信息、元数据内存信息等。

调用类位置：`beifengtz.vmconsole.JStatCmd`

返回的JStatResult对象：`beifengtz.vmconsole.entity.jstat.JStatResult`
```
java.lang.Object
    beifengtz.vmconsole.entity.JvmResult
        beifengtz.vmconsole.entity.jstat.JStatResult
```

**JvmResult对象参数及其含义：**
```
//  虚拟机唯一识别vmId，大部分命令执行都需要此信息
private Integer vmId;

//  虚拟机版本号
private String vmVersion;
```

**JStatResult对象参数及其含义：**
```
//  虚拟机信息列表，仅包含名字
private ArrayList<String> names;

//  虚拟机信息及其值列表，包含名字和对应值
private ArrayList<String> snapShot;

//  字符串结果，直接使用jstat命令的结果，当使用-list、-snap时该字段不存值，使用-class、gc、compiler等命令时保存相应结果值
private String strResult;
```

**JStatResult各子类属性含义（监控参数）**（详细讲解看我的博客：[JVM虚拟机性能监控与故障处理工具](http://blog.beifengtz.com/article/52#directory051025813848240414)）
```
S0C：s0（from）的大小
S1C：s1（from）的大小
S0U：s0（from）已使用的空间
S1U：s1(from)已经使用的空间
EC：eden区的大小
EU：eden区已经使用的空间
OC：老年代大小
OU：老年代已经使用的空间
MC：元空间的大小（Metaspace）
MU：元空间已使用大小
CCSC：压缩类空间大小（compressed class space）
CCSU：压缩类空间已使用大小
YGC：新生代gc次数
YGCT：新生代gc耗时
FGC：Full gc次数
FGCT：Full gc耗时
GCT：gc总耗时
e：Eden区使用比例
o：老年代使用比例
m：元数据区使用比例
css：压缩使用比例
Loaded：表示载入了类的数量
Bytes：所占用空间大小
Unloaded：表示卸载类的数量
UnBytes：卸载空间大小
Time：执行时间
Size：最近编译字节码的数量
Type：最近编译方法的编译类型
Method：方法名
Compiled：表示编译任务执行的次数
Failed：表示编译失败的次数
Invalid：不可用数量
FailedType：失败的类型
FailedMethod：失败的方法
```

**JStatCmd可使用方法：**
```
//  获取虚拟机信息列表，仅包含名字，封装jstat -list命令
public static JStatResult list();

//  获取虚拟机信息及其值列表，包含名字和对应值，封装jstat -snap命令
public static JStatResult snap(int vmId)

//  获取虚拟机加载的类信息，封装jstat -class [vmid]命令
public static JStatResultForClass clazz(int vmId)

//  批量获取虚拟机加载的类信息，在interval时间内获取count个，封装jstat -class [vmid] [interval] [count]命令
public static ArrayList<JStatResultForClass> clazz(int vmId, long interval, int count)

//  获取gc数据，封装jstat -gc [vmid]命令
public static JStatResultForGc gc(int vmId)

//  批量获取gc数据，在interval时间内获取count个，封装jstat -gc [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGc> gc(int vmId, long interval, int count)

//  获取虚拟机编译信息，封装jstat -compiler [vmid]命令
public static JStatResultForCompiler compiler(int vmId)

//  批量获取虚拟机编译信息，在interval时间内获取count个，封装jstat -compiler [vmid] [interval] [count]命令
public static ArrayList<JStatResultForCompiler> compiler(int vmId, long interval, int count)

//  获取虚拟机GC内存情况，封装jstat -gc [vmid]命令
public static JStatResultForGcCapacity gcCapacity(int vmId)

//  获取虚拟机GC内存情况，在interval时间内获取count个，封装jstat -gc [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGcCapacity> gcCapacity(int vmId, long interval, int count)

//  获取虚拟机新生代GC情况，封装jstat -gcnew [vmid]命令
public static JStatResultForGcNew gcNew(int vmId)

//  获取虚拟机新生代GC情况，在interval时间内获取count个，封装jstat -gcnew  ew [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGcNew> gcNew(int vmId, long interval, int count)

//  获取虚拟机新生代GC内存情况，封装jstat -gcnewcapacity  [vmid]命令
public static JStatResultForGcNewCapacity gcNewCapacity(int vmId)

//  获取虚拟机新生代GC内存情况，在interval时间内获取count个，封装jstat -gcnewcapacity  [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGcNewCapacity> gcNewCapacity(int vmId, long interval, int count)

//  获取虚拟机老年代GC情况，封装jstat -gcold [vmid]命令
public static JStatResultForGcOld gcOld(int vmId)

//  获取虚拟机老年代GC情况，在interval时间内获取count个，封装jstat -gcold  ew [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGcOld> gcOld(int vmId, long interval, int count)

//  获取虚拟机老年代GC内存情况，封装jstat -gcoldcapacity  [vmid]命令
public static JStatResultForGcOldCapacity gcOldCapacity(int vmId)

//  获取虚拟机老年代GC内存情况，在interval时间内获取count个，封装jstat -gcoldcapacity  [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGcOldCapacity> gcOldCapacitiy(int vmId, long interval, int count)

//  获取元数据空间内存情况，封装jstat -gcmetacapacity [vmid]命令
public static JStatResultForGcMetaCapacity gcMetaCapacity(int vmId)

//  获取元数据空间内存情况，在interval时间内获取count个，封装jstat -gcmetacapacity [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGcMetaCapacity> gcMetaCapacitiy(int vmId, long interval, int count)

//  获取gc统计数据，封装jstat -gcutil [vmid]命令
public static JStatResultForGcUtil gcUtil(int vmId)

//  获取gc统计数据，在interval时间内获取count个，封装jstat -gcutil [vmid] [interval] [count]命令
public static ArrayList<JStatResultForGcUtil> gcUtil(int vmId, long interval, int count)

//  获取已经被JIT编译的方法，封装jstat -printcompilation [vmid]命令
public static JStatResultForCompilation printCompilation(int vmId)

//  获取已经被JIT编译的方法，在interval时间内获取count个，封装jstat -printcompilation [vmid] [interval] [count]命令
public static ArrayList<JStatResultForCompilation> printCompilation(int vmId, long interval, int count)

//  jstat命令执行起点函数，调用者可以传入自定义参数执行
public static ArrayList<JStatResult> run(String[] var0)
```

## 3.jstack
在jdk中jstack工具用于查询虚拟机堆栈信息，包括线程堆栈、dump文件、c/c++方法堆栈等。

调用类位置： `beifengtz.vmconsole.JStackCmd`

返回的JStackResult对象： `beifengtz.vmconsole.entity.jstack.JStackResult`
```
java.lang.Object
    beifengtz.vmconsole.entity.JvmResult
        beifengtz.vmconsole.entity.jstack.JStackResult
```

**JStackResult对象参数及其含义：**
```
//  死锁堆栈信息
private String deadlocks;

//  并发锁堆栈信息
private String concurrentLocks;

//  线程堆栈
private List<ThreadStack> threadStacks;

//  Java及c/c++方法堆栈
private ArrayList<StringBuilder> jniStack;

//  dump文件信息
private String threadDump;
```

**JStackCmd可使用方法：**
```
//  除堆栈外，获取关于锁的附加信息，封装jstack -l [vmid]命令
public static JStackResult threadStack(int vmId)

//  如果调用到本地方法的话，可以获取C/C++的堆栈，封装jstack -m [vmid]命令
public static JStackResult jniStack(int vmId)

//  除堆栈外，获取关于锁、dump文件的附加信息，封装jstack -l [vmid]命令
public static JStackResult threadDump(int vmId)

//  jstack命令起点方法，调用者可以传入自定义参数调用
public static JStackResult run(String[] var0) 
```

## 4.jinfo

在jdk中提供的jinfo命令用于获取以及设置系统参数、虚拟机参数等信息。

调用位置：**beifengtz.vmconsole.JInfoCmd**

返回的JStackResult对象：**beifengtz.vmconsole.entity.jinfo.JInfoResult**
```
java.lang.Object
    beifengtz.vmconsole.entity.JvmResult
        beifengtz.vmconsole.entity.jinfo.JInfoResult
```

**JInfoResult对象参数及其含义：**
```
//  信息列表，成员为JInfoNode对象，其形式为key-value
private List<JInfoNode> infoList;

//  标志信息列表，成员为JInfoFlag对象，继承自JInfoNode
private List<JInfoFlag> flags;

//  命令行内容
private String commandLine;

//   命令类型，设置或者是查询，设置为set，查询为query
private String commandType;

//   如果命令类型为set，此字段用于判断是否操作成功
private boolean setSuccess;
```

**JInfoCmd可使用方法：**
```
//  查询flag信息和系统参数信息，相当于直接执行命令jinfo [vmId]
public static JInfoResult queryFlagsAndSysInfo(int vmId)

//  查询flag信息 封装jinfo -flags [vmId]命令
public static JInfoResult queryFlags(int vmId)

//  查询系统参数信息，封装jinfo -sysprops [vmId]命令
public static JInfoResult querySysInfo(int vmId)

//  查询某一个Flag，封装jinfo -flag [name] [vmId]命令
public static JInfoResult queryFlag(int vmId,String flagName)

//  新开启一个Flag，封装jinfo -flag [+name] [vmId]命令
public static JInfoResult addFlag(int vmId,String flagName)

//  关闭一个Flag，封装jinfo -flag [-name] [vmId]命令
public static JInfoResult removeFlag(int vmId,String flagName)

//  给一个Flag设置新值，封装jinfo -flag [name=value] [vmId]命令
public static JInfoResult setFlag(int vmId,String flagName,String value)

//  jinfo起点执行函数，调用者可以传入自定义参数执行
public static JInfoResult run(String[] var0)
```

## 5.jcmd

在jdk7以后提供了一个新的工具jcmd，它可以为在虚拟机上执行一些命令然后返回信息。

调用位置：`beifengtz.vmconsole.JCmd`

返回的JStackResult对象：`beifengtz.vmconsole.entity.jinfo.JCmdResult`
```
java.lang.Object
    beifengtz.vmconsole.entity.JvmResult
        beifengtz.vmconsole.entity.jinfo.JCmdResult
```

**JCmdResult对象参数及其含义：**
```
//  虚拟机线程列表，其中包含vmId和content
private List<JCmdProcess> processes;

//  执行命令后的返回结果
private String result;
```

**JCmd可使用方法：**
```
//  列出虚拟机进程列表，相当于直接执行jcmd命令
public static JCmdResult listProcess() 

//  列出虚拟机支持的命令，封装jcmd [vmId] help命令
public static JCmdResult listCommands(int vmId)

//  执行命令，封装cmd [vmId] [command]命令
public static JCmdResult executeCommand(int vmId, JCmdEnum jCmdEnum) 
```

**JCmdEnum枚举类支持的命令**
```
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
```