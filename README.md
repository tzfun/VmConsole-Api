<div align=center>
<img src=http://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/beifengtz/VMConsole.png width=200/>
</div>

# VmConsole-Api
🔨VmConsole-Api是一个jvm虚拟机性能监控API，将oracle jdk提供的tools.jar包进行了功能拓展，对一些监控命令结果进行了封装，你可以方便地从对象中读取每一个参数和结果。该类库并不是简单地使用运行时exec()调用jps、jstat、jstack等命令，而是从底层深度拓展而来，所以你不需要配置java环境变量就可以通过Java代码对虚拟机进行监控。

**说明**：本工具API适用于jdk8及以上（jdk7以下部分功能可能会出错），虚拟机必须是**HotSpot**。

**本项目遵守Apache开源协议，核心代码由本人根据jdk源码拓展或改造而来，无模仿或抄袭。**

# 如何使用
我已将核心依赖库打包生成了一个jar包(3MB左右)，你只需要将jar包导入到项目就可以直接使用其中的类，无需导入任何其他包。

* **jar包下载**

  提供两种下载方式，任选其一即可
    * github下载：[https://github.com/tzfun/VmConsole-Api/releases](https://github.com/tzfun/VmConsole-Api/releases)
    * 百度云盘下载：链接：[https://pan.baidu.com/s/15ptIBoJJqSJghxCewOyEGg](https://pan.baidu.com/s/15ptIBoJJqSJghxCewOyEGg) 提取码：nuia 

* **Maven依赖**

在pom.xml中引入如下依赖(以下是最新版本)：
```xml
<dependency>
  <groupId>com.github.tzfun</groupId>
  <artifactId>vmconsole</artifactId>
  <version>1.1.0</version>
  <classifier>jar-with-dependencies</classifier>
</dependency>
```

使用示例：
```java
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

* 使用说明(建议查看此文档)
    * [https://www.kancloud.cn/beifengtz/vmconsole/1118492](https://www.kancloud.cn/beifengtz/vmconsole/1118492)
    
* API文档
    * [doc.vmconsole.beifengtz.com](http://doc.vmconsole.beifengtz.com)
# 交流

如果你在使用中发现了Bug，或者想对本作品提出意见或建议，请发送邮件到作者本人邮箱：[beifengtz@163.com](mailto:beifengtz@163.com)

以下QQ群用于开发者交流，欢迎与作者互动。

* 865687569

<img src=http://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/beifengtz/QQGroup.jpg width=200/>

# 关于作者

* 个人首页：[www.beifengtz.com](http://www.beifengtz.com)
* 博客地址：[blog.beifengtz.com](http://blog.beifengtz.com)
* GitHub：[https://github.com/tzfun](https://github.com/tzfun)
* 个人微信公众号：**北风IT之路**

<img src=https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/beifengtz/%E5%85%AC%E4%BC%97%E5%8F%B7.png width=200/>

# VmConsole详细介绍文档

目前为止，我已经封装了jps、jstat、jstack、jinfo、jcmd、jamp六个命令，使用者可以直接通过每个工具类的run方法直接执行相关命令（主需要传入参数，不需要传入jps、jstat等命令），命令格式和jdk提供的工具命令格式一模一样，也可以使用我已经封装好了的方法，不过为了方便以及可靠性保证，建议你使用已经封装好的方法。

* `JpsCmd`：获取本地虚拟机实例的ID、主类信息、虚拟机参数等，封装于命令`jps`
* `JInfoCmd`：获取和设置系统参数信息、虚拟机参数信息，封装于命令`jinfo`
* `JStatCmd`：获取虚拟机内存状态信息，包括GC信息、编译类信息、元数据内存信息，封装于命令`jstat`
* `JStackCmd`：获取虚拟机堆栈信息，包括线程堆栈、dump文件、c/c++方法堆栈等，封装于命令`jstack`
* `JMapCmd`：获取虚拟机堆内存配置数据、Eden & Survivor & Old区内存使用量和比例、输出dump文件、获取class实例信息等，封装于`jmap`
* `JCmd`：向某一个虚拟机示例执行命令，封装于`jcmd`

## 1.JPSCmd

JpsCmd类可以获取本地虚拟机实例的虚拟机唯一识别VMID、虚拟机参数、主类参数和jar全称等。对应原生jdk命令`jps`。

### 1.1方法
| 名称 | 参数 | 返回值 |  含义  |
| --- | --- | --- | --- |
| quit()  |   无  | List\<JpsResult\> | 仅获取本地所有虚拟机实例的vmId |
| withMainClassArgs()  |   无  | List\<JpsResult\> | 获取本地所有虚拟机实例的vmId和main函数的参数 |
|withVmArgs()  |   无  | List\<JpsResult\> | 获取本地所有虚拟机实例的vmId和虚拟机参数 |
| withFullName()  |   无  | List\<JpsResult\> | 获取本地所有虚拟机实例的vmId和主类或jar全名 |
| run()  |  String[] var0 （命令参数） | List\<JpsResult\> | 自定义参数执行jps命令，建议使用上面的方法 |

### 1.2结果对象
所有可能返回对象的继承关系结构
```java
|—— beifengtz.vmconsole.entity.JvmResult
    |—— beifengtz.vmconsole.entity.jps.JpsResult
```
JvmResult的内容介绍请见开始使用。
#### 1.2.1 JpsResult
|   属性  |  类型  |   含义  |
| --- | --- | --- |
|   mianClass | String  |   虚拟机主类信息  |
|   mainArgs |  String  |   虚拟机主类参数  |
|   vmArgs |  String  |   虚拟机参数  |
|   vmFlags |  String  |   虚拟机标志数据  |
| errMessage | String | jps命令错误信息 |
| ~~strResult~~ | String | 未经处理的 jps命令执行结果，**不建议使用，后续版本将去掉**  |

## 2.JInfo

获取以及设置系统参数、虚拟机参数等信息。封装自命令：`jinfo`。

### 2.1 方法
| 名称 | 参数 | 返回值 |  含义  |
| --- | --- | --- | --- |
| queryFlagsAndSysInfo() |  int vmId （虚拟机ID，JpsCmd可获取） | JInfoResult | 查询虚拟机flag信息和系统参数信息|
| queryFlags() | int vmId（虚拟机ID，JpsCmd可获取） | JInfoResult | 查询虚拟机flag信息|
| querySysInfo() |  int vmId （虚拟机ID，JpsCmd可获取） | JInfoResult | 查询系统参数信息|
| queryFlag() | 1. int vmId<br/> 2. String flagName（标志名称）| JInfoResult | 查询某一个Flag的值|
| addFlag() | 1. int vmId<br/> 2. String flagName（标志名称）| JInfoResult | 新增一个Flag|
| removeFlag() | 1. int vmId<br/> 2. String flagName（标志名称）| JInfoResult | 移除一个Flag|
| setFlag() | 1. int vmId<br/> 2. String flagName（标志名称）<br/>  3. String value（标志的值）| JInfoResult | 为一个Flag设置值|
| run() | String[] var0（命令参数）| JInfoResult | 自定义执行jinfo命令，不过建议你使用上面的方法|
### 2.2 结果对象
所有可能返回对象的继承关系结构
```java
|—— beifengtz.vmconsole.entity.JvmResult
    |—— beifengtz.vmconsole.entity.jinfo.JInfoResult

|—— beifengtz.vmconsole.entity.jinfo.JInfoNode
    |—— beifengtz.vmconsole.entity.jinfo.JInfoFlag
```
#### 2.2.1 JInfoNode / JInfoFlag
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| option | String | 选项/flag名 |
| value | String | 选项/flag的值 |

#### 2.2.2 JInfoResult
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| infoList | List\<JInfoNode\> | 系统信息列表 |
| flags | List\<JInfoFlag\> | 虚拟机标志信息列表 |
| commandLine | String | 命令行 |
| commandType | String | 命令类型 |
| setSuccess | boolean | 用于判断写操作是否成功，仅当执行命令为**写**操作该属性才有用 |

## 3.JStatCmd

获取虚拟机内存状态信息，包括GC信息、编译类信息、元数据内存信息，封装于命令`jstat`

### 3.1 方法
| 名称 | 参数 | 返回值 | 含义 |
| --- | --- | --- | --- |
| list() | 无 | JStatResult | 获取虚拟机信息列表，仅包含名字 |
| snap() | int vmId（虚拟机ID，JpsCmd可获取） | JStatResult | 获取虚拟机信息及其值列表，包含名字和对应值|
| clazz() | int vmId（虚拟机ID，JpsCmd可获取） | JStatResultForClass |获取虚拟机加载的类信息 |
| clazz() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForClass \> | 批量获取虚拟机加载的类信息，在interval时间内获取count个 |
| gc() | int vmId（虚拟机ID，JpsCmd可获取） | JStatResultForGc | 获取gc数据|
| gc() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForGc\> | 批量获取虚拟机gc数据，在interval时间内获取count个 |
| compiler() | int vmId（虚拟机ID，JpsCmd可获取）| JStatResultForGcCapacity  | 获取虚拟机编译信息 |
| compiler() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForGcCapacity\> | 批量获取虚拟机编译信息，在interval时间内获取count个 |
| gcCapacity() | int vmId（虚拟机ID，JpsCmd可获取） | JStatResultForGcCapacity  | 获取虚拟机GC内存情况|
| gcCapacity() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量） | ArrayList\<JStatResultForGcCapacity\>| 批量获取虚拟机GC内存情况，在interval时间内获取count个|
| gcNew() | int vmId（虚拟机ID，JpsCmd可获取） | JStatResultForGcNew | 获取虚拟机新生代GC情况 |
| gcNew() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量） | ArrayList\<JStatResultForGcNew\> | 批量获取虚拟机新生代GC情况 |
| gcNewCapacity() | int vmId（虚拟机ID，JpsCmd可获取）| JStatResultForGcNewCapacity | 获取虚拟机新生代GC内存情况 |
| gcNewCapacity() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量） | ArrayList\<JStatResultForGcNewCapacity \>| 批量获取虚拟机新生代GC内存情况 |
| gcOld() | int vmId（虚拟机ID，JpsCmd可获取）| JStatResultForGcOld | 获取虚拟机老年代GC情况 |
| gcOld() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForGcOld\> | 批量获取虚拟机老年代GC情况 |
| gcOldCapacity() | int vmId（虚拟机ID，JpsCmd可获取）| JStatResultForGcOldCapacity | 获取虚拟机老年代GC内存情况 |
| gcOldCapacity() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForGcOldCapacity\> | 批量获取虚拟机老年代GC内存情况 |
| gcMetaCapacity() | int vmId（虚拟机ID，JpsCmd可获取）| JStatResultForGcMetaCapacity | 获取元数据空间内存情况 |
| gcMetaCapacity() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForGcMetaCapacity \>| 批量获取元数据空间内存情况 |
| gcUtil() | int vmId（虚拟机ID，JpsCmd可获取）| JStatResultForGcUtil | 获取gc统计数据 |
| gcUtil() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForGcUtil \>| 批量获取gc统计数据 |
| printCompilation() | int vmId（虚拟机ID，JpsCmd可获取）| JStatResultForCompilation | 获取已经被JIT编译的方法 |
| printCompilation() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. long interval（命令执行时间）<br/>3. int count（在该执行时间内获取的数量）| ArrayList\<JStatResultForCompilation\>| 批量获取已经被JIT编译的方法 |
| run() | String[] var0（命令参数） | ArrayList\<JStatResult\> | 自定义执行jstat命令 |  
### 3.2 结果对象
所有可能返回对象的继承关系结构
```java
|—— beifengtz.vmconsole.entity.JvmResult
    |—— beifengtz.vmconsole.entity.jstat.JStatResult
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForClass
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForCompilation
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForCompiler
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGc
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGcCapacity
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGcMetaCapacity
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGcNew
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGcNewCapacity
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGcOld
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGcOldCapacity
        |—— beifengtz.vmconsole.entity.jstat.JStatResultForGcUtil
```
#### 3.2.1 JStatResult
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| names | ArrayList\<String\> | 虚拟机信息列表，仅包含名字 |
| snapShot| ArrayList\<String\> | 虚拟机信息及其值列表，包含名字和对应值 |
| ~~strResult~~ | String | 未经处理的 jstat命令执行结果，**不建议使用，后续版本将去掉** |
#### 3.2.2 JStatResult各子类属性含义
参数详细讲解看我的博客：[JVM虚拟机性能监控与故障处理工具](http://blog.beifengtz.com/article/52#directory051025813848240414)
~~~java
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
~~~

## 4.JStackCmd

获取虚拟机堆栈信息，包括线程堆栈、dump文件、c/c++方法堆栈等，封装于命令`jstack`

### 4.1 方法
| 名称 | 参数 | 返回值 | 含义 |
| --- | --- | --- | --- |
| threadStack() | int vmId（虚拟机ID，JpsCmd可获取）| JStackResult | 除堆栈外，获取关于锁的附加信息 | 
| jniStack() | int vmId（虚拟机ID，JpsCmd可获取）| JStackResult | 如果调用到本地方法的话，可以获取C/C++的堆栈 | 
| threadDump() | int vmId（虚拟机ID，JpsCmd可获取）| JStackResult | 除堆栈外，获取关于锁、dump文件的附加信息 | 
| run() | String[] var0（命令参数） | JStackResult  | 自定义执行jstack命令 |  
### 4.2 结果对象
所有可能返回对象的继承关系结构
```java
|—— beifengtz.vmconsole.entity.JvmResult
    |—— beifengtz.vmconsole.entity.jstack.JStackResult
    |—— beifengtz.vmconsole.entity.jstack.ThreadStack
```
#### 4.2.1 ThreadStack
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| id | String  | 线程id|
| state | String | 线程状态 | 
| stacks | StringBuilder | 线程堆栈信息 |
|deadLocks | String | 线程死锁信息|
|~~currentJavaSP~~ | String | 当前线程全限名地址 |
| concurrentLocks | String | 并发锁信息|
####  4.2.2 JStackResult
|   属性  |  类型  |   含义  |
| --- | --- | --- |
|  deadLocks | String |  虚拟机死锁信息 | 
| concurrentLocks | String | 虚拟机并发锁信息 |
| threadStacks | List\<ThreadStacks\> | 线程栈信息列表 |
| jniStack | ArrayList\<StringBuilder\> | Java及C/C++方法的堆栈信息 |
| threadDump | String | 锁、dump文件的附加信息 |

## 5.JMapCmd

获取虚拟机堆内存配置数据、Eden & Survivor & Old区内存使用量和比例、输出dump文件、获取class实例信息等，封装于`jmap`

### 5.1 方法
| 名称 | 参数 | 返回值 | 含义 |
| --- | --- | --- | --- |
| dumpAll() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2.String filePath（保存的文件路径）| boolean| 输出jvm的heap内容到文件，保存成功返回true，失败返回false |
| dumpLive() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2.String filePath（保存的文件路径）| boolean| 输出jvm的heap内容到文件，但只输出还存活的对象，保存成功返回true，失败返回false |
| histoAll() | int vmId（虚拟机ID，JpsCmd可获取）| InputStream | 获取每个class的实例信息|
| histoList() | int vmId（虚拟机ID，JpsCmd可获取）| InputStream | 获取每个class的实例信息，但只输出还存活的对象|
| heapInfo() | int vmId（虚拟机ID，JpsCmd可获取）| JMapForHeapResult | 获取堆内存信息，包括Eden、Survivor From、Survivor To、Old区等 |
### 5.2 结果对象
所有可能返回对象的继承关系结构
```java
|—— beifengtz.vmconsole.entity.JvmResult
    |—— beifengtz.vmconsole.entity.jmap.JMapForHeapResult 

|—— beifengtz.vmconsole.entity.jmap.JMapForHeapUsage
|—— beifengtz.vmconsole.entity.jmap.HeapForSpace
|—— beifengtz.vmconsole.entity.jmap.HeapForG1
|—— beifengtz.vmconsole.entity.jmap.HeapForGen
```
#### 5.2.1 HeapForSpace
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| name | String | 堆区域名|
| regions | String | 使用G1垃圾收集器时，表示所在区域 |
| capacity | String | 该区域总容量 ，单位byte|
| used | String | 已使用的大小 ，单位byte|
| free | Sting | 空闲的大小，单位byte|
| useRatio | String | 内存使用比例，百分数% |

#### 5.2.2 HeapForG1
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| heap | HeapSpace | G1收集器的堆空间情况 |
| eden | HeapSpace | G1收集器的Eden区空间情况 |
| survivor | HeapSpace | G1收集器的Survivor区空间情况 |
| old | HeapSpace | G1收集器的Old区空间情况 |

#### 5.2.3 HeapForGen
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| newGen | HeapSpace | 普通收集器的Eden 和 Survivor区空间情况 |
| youngEden | HeapSpace | 普通收集器的Eden区空间情况 |
| youngFrom | HeapSpace | 普通收集器的Survivor From区空间情况 |
| youngTo | HeapSpace | 普通收集器的Survivor To区空间情况 |
| oldGen | HeapSpace | 普通收集器的Old区空间情况 |

#### 5.2.4 JMapForHeapUsage
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| heapForGen| HeapForGen | 普通收集器的堆空间情况 |
| heapForG1| HeapForG1 | G1收集器的堆空间情况 |

#### 5.2.5 JMapForHeapResult 
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| heapConf| HashMap\<String\, Object\>| 堆空间配置信息 |
| heapUsage| JMapForHeapUsage| 堆空间使用情况 |

## 6. JCmd

向某一个虚拟机示例执行命令，比如你当前是虚拟机A，需要向虚拟机B执行命令，则需要次类相关方法。封装于`jcmd`

### 6.1 方法
| 名称 | 参数 | 返回值 | 含义 |
| --- | --- | --- | --- |
| listProcess() | 无 | JCmdResult | 列出虚拟机进程列表 |
| ~~listCommands()~~ | int vmId（虚拟机ID，JpsCmd可获取）| JCmdResult | 列出虚拟机支持的命令,**后期将取消，由JCmdEnum对象代替** |
| executeCommand() | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. JCmdEnum jCmdEnum（命令枚举类）| JCmdResult| 向某一个虚拟机实例执行命令|
| ~~executeCommand()~~ | 1. int vmId（虚拟机ID，JpsCmd可获取）<br/>2. JCmdEnum... jCmdEnum（命令枚举类数组）| JCmdResult| 向某一个虚拟机实例执行批量命令，**后期将取消**|
### 6.2 结果对象
```java
|—— beifengtz.vmconsole.entity.JvmResult
    |—— beifengtz.vmconsole.entity.jcmd.JCmdResult

|—— beifengtz.vmconsole.entity.jcmd.JCmdProcess
|—— beifengtz.vmconsole.entity.jcmd.JCmdEnum
```
#### 6.2.1 JCmdEnum
~~~java
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
~~~

#### 6.2.2 JCmdProcess
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| vmId | int | 虚拟ID |
| content | String | 虚拟机进程信息内容 |

#### 6.2.3 JCmdResult
|   属性  |  类型  |   含义  |
| --- | --- | --- |
| processes | List\<JCmdProcess\> | 进程信息列表 |
| result| String | 命令执行结果 |