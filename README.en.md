# WEB应用系统

## Description
&emsp;&emsp;本系统（基于SpringBoot+MyBatis+Apache Shiro+Bootstrap+Thymeleaf）可用于开发所有的WEB应用系统。
可进行深度定制开发，做出更强的企业级WEB应用系统。支持大部分主流浏览器（IE9+浏览器、Google Chrome、火狐浏览器、360浏览器等...)。  
&emsp;&emsp;让开发者专于注业务逻辑的实现，从而缩短项目周期，节省开发成本，提高软件安全、质量。

- 感谢[RuoYi](https://gitee.com/y_project/RuoYi "若依")提供的框架

## main feature
1. 完全响应式布局（支持电脑、平板、手机等所有主流设备）
2. 集成文件管理器（视频、音频、PDF、world、excel等多文件的上传与下载；图片、音频、视频在线预览等）
3. 集成企业官网CMS内容管理系统（简单的企业官网，如需更强大的企业官网可进行二次开发或重新开发）
4. 集成工作流程引擎Activiti5（支持在线设计、部署、查看、编辑流程图）
5. 通知通告（重写了原系统的通知通告模块）
6. 集成MarkDown在线编辑器editor.md
7. WAR包方式进行部署，简单、便捷，无需修改过多的配置
8. 全局事务处理
9. 集成定时任务调度框架，可用于创建简单或复杂的定时计划任务
10. 完善的XSS防范及脚本过滤，彻底杜绝XSS攻击
11. 完善的日志记录体系，简单注解即可实现
12. 数据监控，可查看数据库版本、驱动、数据源、SQL监控、SQL防火墙、WEB应用、URL监控、session监控等信息
13. 服务监控，可查看一些通用的监控，比如 CPU的使用情况、服务器内存使用情况、Java虚拟机信息、服务器的信息、磁盘状态的监控等
14. 缓存监控，可查系统的缓存信息、以及对缓存的清空操作
15. 对常用js插件进行二次封装，使js代码变得简洁，更加易维护
16. 支持按钮级数据权限，可自定义部门数据权限
17. Maven项目依赖管理，方便模块依赖版本升级、增减模块
18. 支持多数据源，简单配置即可实现切换
19. 集成在线表单构建器，拖拽表单元素即可生成相应的HTML代码、表单代码
20. 集成swagger3.0（swagger3 + knife4j）系统接口、UI框架
21. 丰富的演示示例，帮助开发者快速集成到项目中

## choosing technology
1. 系统环境【JDK 1.8、Tomcat 8】
2. 主框架【Spring Boot 2.2.13、Spring Framework 5.0.x(支持JDK8-10)】
3. 持久层【Apache MyBatis 3.5.6】
4. 视图层【Bootstrap 3.3、Thymeleaf 3.0】
5. 安全框架【Apache Shiro 1.7.1】
6. 缓存框架【shiro-ehcache 1.7.1】
7. 日志框架【slf4j日志接口+Logback日志的实现（SLF4J和Logback取代Commons Logging和Log4j；始终使用SLF4J的接口写入日志，使用Logback只需要配置，不需要修改代码）】
8. 任务调度框架【Quartz 2.3.2】（Quartz是一个功能强大的开源作业调度库，几乎可以集成到任何Java应用程序中-从最小的独立应用程序到最大的电子商务系统。可用于创建简单或复杂的计划，以执行数以万计的工作。任务定义为标准Java组件的作业，它可以执行您对其执行的任何编程操作。Quartz Scheduler包含许多企业级功能，例如对JTA事务和集群的支持。）
9. 业务流程管理（BPM）框架【Activiti 5.23.0】
10. 文件管理器【CKFinder2.6.3】
11. 服务端验证【Hibernate Validation 6.0】
12. 客户端验证【jQuery Validation Plugin 1.13.0】
13. 树型插件 【jQuery zTree 3.5.12】
14. 项目版本控制【Git(一个免费开源的分布式版本控制系统，旨在快速高效地处理从小型到大型项目的所有内容)】
15. 项目管理工具【Apache Maven 3.8.1】
16. JSON解析器【FastJSON 1.2.76】
17. 弹出层组件【layer 3.5.0】
18. 日期与时间组件【layDate 5.3.0】
19. 获取系统信息组件【oshi 5.6.0】，兼容系统： Linux, Windows, Solaris, AIX, HP-UX, FreeBSD and Mac OSX
20. 表格组件【BootstrapTable 1.18.2】
21. 获取浏览器名称及版本号组件【bitwalker 1.21】
22. 导入导出组件【Apache POI 4.1.2】
23. 富文本编辑器【summernote 0.8.18】
24. MarkDown在线编辑器【editor.md 1.5.0 】

## function declaration
1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置
2.  部门管理：配置系统组织机构（国家、城市、公司、部门），树结构展现支持数据权限
3.  岗位管理：配置系统用户所属担任职务
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护
7.  参数管理：对系统动态配置常用参数
8.  通知公告：系统通知公告信息发布维护
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询
10. 登录日志：系统登录日志记录查询包含登录异常
11. 在线用户：当前系统中活跃用户状态监控
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 
14. 系统接口：根据业务代码自动生成相关的api接口文档
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息
16. 在线构建器：拖动表单元素生成相应的HTML代码
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈
18. CMS企业官网：【栏目管理（栏目列表）、内容管理（广告列表、文章列表）、文档管理（文档列表）】；支持多站点建站，各个站点之间数据互不影响，可随意切换站点
19. 文件管理：每个用户拥有属于自己的文件管理器；支持多种格式文件（图片、视频、音频、Excel、Word、CSV等格式）的上传与下载、图片-视频-音频在线预览（播放）等功能
20. 在线办公：通知公告【我的通知、通告管理】、个人办公【我的任务、请假申请】
21. 工作流程管理：模型【模型管理】、流程【流程列表、运行中的流程、流程管理】
22. DEMO管理：大数据（百万级）导入Demo、MarkDown在线编辑器

## Client specification
1.  支持大部分主流浏览器（如：IE9+浏览器、Google Chrome、火狐浏览器、360浏览器等等。。。)
2.  支持手机、平板等主流设备


## instructions
1. 安装好系统所需的运行、开发环境（JDK1.8+、MySQL5.7+、maven3.6+、Git环境等...）
2. 使用IntelliJ IDEA拉取并打开项目
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/1.jpg?versionId=CAEQDRiBgICrx._C0xciIGY5YjU5ZjI3ZmY4NDQxMjRhZjRjMmE0YmQxNDM5Y2E0)

3. 复制项目Git地址
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/2.jpg?versionId=CAEQDRiBgMC26veB1BciIGQ5MzJhMDdkZTFlNjRjYjE5Y2VhZmVjZjBjNGQ0M2Q4)

4. 接着填入IDEA
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/3.jpg?versionId=CAEQDRiBgMD8t._C0xciIGJlNzQwY2Y2ZGRmZjQ0ZjI4YzVhMzU2YmIxMzNkYjE3)

5. 拉取完毕后，Maven就会自动下载项目所有依赖包到本地中央仓库。目录结构如下
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/4.jpg?versionId=CAEQDRiBgIC2wO_C0xciIDVjOTk0YmJjYzZiNDQzOTI5M2ExM2QyMTRiZWU5MGY2)

6. 第一次需要install安装到本地仓库
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/5.jpg?versionId=CAEQDRiBgMCkxu_C0xciIDI5MDQ3ZTE2YzIxYTRjN2NiZDA1YTI4NzIxMzE2OWI2)

7. 导入数据文件的数据到本地数据库  
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/11.jpg?versionId=CAEQDRiBgMCNooSC1BciIGUzNTVlZGUxNTQ3ZDRiNTdiMDQyMDExNDNhNWUyNTVl)

8. 修改MySQL数据库账号密码为自己本地的数据库的账号密码
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/6.jpg?versionId=CAEQDRiBgMDi2O_C0xciIDUxMzY3ZTBmNTljZjQ3YTY5OTAwNDcyMjJiODhmZGQx)

9. 所有步骤没问题之后，就可以直接运行项目了，启动文件
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/7.jpg?versionId=CAEQDRiBgMDm2._C0xciIGZjNzdjNzlkNzZkYzQ5ZDViNzU5ZGU4YTNmNzM5YjE5)

10. 访问路径：localhost:8888/zhglxt  登录账号密码：system/system

11. 打包部署（本项目是打成war包的方式进行部署的）
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/8.jpg?versionId=CAEQDRiBgICn1O_C0xciIGFjY2NmNmE2NTRhYzQzNjliYjk1ZTQ2NTY1YWM0OGM5)

12. 打包完成后，到项目的目录下找到zhglxt\zhglxt-web\target\zhglxt-web.war包，重命名为zhglxt.war，然后直接放到Tomcat的webapps目录下，就可以运行起来了
![](https://dlsdys-gz.oss-cn-guangzhou.aliyuncs.com/zhglxt/helpImages/sys/9.jpg?versionId=CAEQDRiBgICwze_C0xciIGUwOTYwNmE2ZWM2ODQzODZiZjZmYmRlYzI0ZTA0Mjg3)

13. 本项目不需要修改过多的配置信息，即可打包、部署、运行