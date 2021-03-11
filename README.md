# **ara-framework工程简要说明**
命名上尽量保证能够通过名字得知含义，包括module、package、class、method、变量、常量等。

#1. Module
- **ara-framework-domain** : domain模块，存放所有的domain，不依赖任何其他模块。
- **ara-framework-core** : 核心功能模块，存放所有的controller、service、dao等，依赖domain模块。
- **ara-framework-generator** : 代码生成器模块，存放代码生成器相关代码，依赖core模块。
- **ara-framework-start-demo** : 模拟启动模块，模拟子系统引用框架jar包，用于启动服务验证，依赖core模块。

#2. Package
- 所有包均以*com.aratek.framework*开头，然后加上*模块名*，再加上*具体功能组件*如controller。
##2.1 controller
- 以Controller结尾，如“UserController”。
##2.2 service
- 以Service结尾，如“UserService”。
- 实现类在后面加上Impl，如“UserServiceImpl”。
##2.3 dao
- 以DAO结尾，如“UserDAO”。
- 可使用通用mapper，继承“BaseDAO”即可。

#3. 增删改查等
##3.1 增
- insertXxx()，insertXxxBatch()，insertXxxList()
##3.2 删
- deleteXxx()，deleteXxxBatch()，deleteXxxList()
##3.3 改
- updateXxx()，updateXxxBatch()，updateXxxList()
##3.4 查
- selectXxx()，selectXxxList()，selectXxxByXxx()
##3.5 导出
- exportXxx()

#4. 注意事项
##4.1 编码格式
- xxx.java、xxx.xml、xxx.properties等统一为**UTF-8**(with no BOM)
- xxx.java、xxx.xml、xxx.properties等统一换行符为Linux的换行符“\n”（即LF，windows下默认为CRLF）
##4.2 版本兼容
- JDK:1.6+
- Maven:3.2.5+
##4.3 **ara-framework-start-demo**与**ara-xxservice-demo**的差异
###4.3.1 模块功能目标
- **ara-framework-start-demo** : 仅为了启动框架进行验证及测试用
- **ara-xxservice-demo** : 为了给子系统提供一个尽可能详尽的demo
###4.3.2 POM
- **ara-framework-start-demo**的jar包版本由parent pom统一控制
- **ara-xxservice-demo**pom jar包可以自行定制
###4.3.3 JDK版本
- **ara-framework-start-demo**仅可使用JDK1.6
- **ara-xxservice-demo**可以使用JDK1.6、1.7、1.8
##4.4 core模块的实体对象
- 引入了通用mapper，扫描“**com.aratek.framework.domain.core**”包下的实体类，该包下的类可增加对应注解如：
<br/>@Table：对应数据库表名；
<br/>@Column：对应数据库字段名；
<br/>@Transient：非表字段需加上此注解；

#5. 其他
请参考svn相关文档，[点击这里](https://192.168.3.251:1088/svn/Aratek/Software/05_UniversalBasePlatform/BasicFrameworkForJava/doc)

#6. 版本说明
## V1.3.0
- 操作记录去掉查询操作
- 增加BaseController
- 修复国际化的bug
- 修复App登录生成Token的bug
- 返回请求头允许获取code和message
- 增加token类型:1,系统用户;2,APP;

## V1.0.0.RELEASE
- 初始版，包含用户、菜单、角色、权限等基础功能