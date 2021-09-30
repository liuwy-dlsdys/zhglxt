# 使用文档

## 使用教程
1. 选择需要调试的API接口
2. 右侧主界面就会显示出该接口的详细信息（请求数据类型、请求参数、响应状态、响应参数、响应示例）
3. 主界面左边有`文档`、`调试`两个选项。调试接口时，选择`调试`即可
4. 接口是否需要请求参数，根据自己需求设置添加对应的注解。没有就直接点击上面的 `发送` 按钮
5. 如果接口请求前，需要添加请求头参数，在`文档管理`中的`全局参数设置`,单击`添加参数`，输入需要的`参数名称`、`参数值`、`参数类型`。保存后刷新下页面，之后的所有请求接口都会包含该请求参数，不需要时删除即可
6. 常用的操作就上面这些，如需了解尝试更多其它功能，可自行研究

## 常用注解
     @Api：用在controller类，描述API接口
     @ApiOperation：描述接口方法
     @ApiModel：描述对象
     @ApiModelProperty：描述对象属性
     @ApiImplicitParams：描述接口参数
       @ApiImplicitParam：(用在@ApiImplicitParams注解中，指定一个请求参数的信息)
         可用参数：
         name：参数名
         value：参数的汉字说明、解释
         required：参数是否必须传
         dataType：参数类型，默认String，其它值dataType="Integer"
         defaultValue：参数的默认值
         paramType ：参数放在哪个地方
            header：请求参数的获取@RequestHeader
            query：请求参数的获取@RequestParam
            path：请求参数的获取@PathVariable
            body：请求参数的获取@RequestBody
            form：普通表单提交
     @ApiResponses：描述接口响应
     @ApiIgnore：忽略接口方法
     

## 自定义（动态）请求参数
代码示例如下：

Controller`类`上添加 `@Api`注解
```java
    @Api(tags ="企业官网-栏目管理")
```

Controller`方法`上添加 `@ApiOperation、@ApiImplicitParams、@ApiImplicitParam`注解
```java
    @ApiOperation(value = "广告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "广告id",dataType = "String", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "title", value = "广告标题",dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo selectAdvertisingList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        startPage();
        List<Advertising> advertisingList = advertisingService.selectAdvertisingList(paramMap);
        return getDataTable(advertisingList);
    }
```

## 自定义文档配置
```yaml
knife4j:
 enable: true
 documents:
    -
       group: default
       name: default自定义标题分组
       # 某个文件夹下所有的.md文件
       locations: classpath:markdown/*
     -
       group: WEB应用系统
       name: WEB应用系统自定义标题分组
       # 某个文件夹下单个.md文件
       locations: classpath:markdown/helpDoc.md
```

| 属性名称 | 是否必须 | 说明 |
| :-----| :----: | :---- |
| group | true | 逻辑分组名称,最终在逻辑分组时该属性需要传入 |
| name | true | 自定义文档的分组名称，可以理解为开发者存在多个自定义文档，最终在Ui界面呈现时的一个分组名称 |
| location | true | 提供自定义.md文件的路径或者文件 |

## knife4j介绍
Knife4j的前身是swagger-bootstrap-ui,前身swagger-bootstrap-ui是一个纯swagger-ui的ui项目

Knife4j与swagger-bootstrap-ui是两种不一样风格的Ui,对比情况如下：

| 名称 | 开发语言&框架 | 状态 | 最后版本 | 风格 |
| :-----| :---- | :---- | :---- | :---- |
| Knife4j | Java、JavaScript、Vue | 持续更新中 |- |黑色 |
| swagger-bootstrap-ui | Java、JavaScript、jQuery | 停更 |1.9.6 |蓝色 |

## 版本说明
Knife4j从开源至今,目前主要经历版本的变化，分别如下：

| 版本 | 说明 |
| :-----| :---- |
|1.9.6|蓝色皮肤风格,开始更名，增加更多后端模块|
|2.0~2.0.5|Ui重写，底层依赖的springfox框架版本是2.9.2|
|2.0.6~|底层springfox框架版本升级知2.10.5,OpenAPI规范是v2|
|3.0~|底层依赖springfox框架版本升级至3.0.3,OpenAPI规范是v3|

`
需要注意的是，目前Knife4j的主版本依然是沿用2.x的版本号，也就是从2.0.6版本开始逐步升级，迭代发布时版本会随之升级，但同时3.x版本也会同步更新发布，主要是满足开发者对于springfox3以及OpenAPI3规范的使用
特别注意
1、目前已经发行的Knife4j版本，Knife4j本身已经引入了springfox，开发者在使用时不用再单独引入Springfox的具体版本，否额会导致版本冲突。另外在网关层聚合(例如gateway)时，必须禁用Knife4j的增强模式
2、使用Knife4j2.0.6及以上的版本，Spring Boot的版本必须大于等于2.2.x
`

自2.0.6版本开始，2.x与3.x的版本主要变化是底层springfox所引用的版本不同，但Knife4j提供的Ui其实是同一个，同时兼容OpenAPI2以及OpenAPI3规范，源码请参考knife4j-vue (opens new window)，如果开发者依然想沿用以前Knife4j一直以来发布的2.x版本，请继续更随Knife4j的更新步伐使用2.x的版本即可，如果开发者想尝鲜，则可以考虑3.x的版本

具体的对应关系如下：

| 2.x | 3.x |
| :-----: | :----: |
| 2.0.6 | 3.0 |
| 2.0.7 | 3.0.1 |
| 2.0.8 | 3.0.2 |
| 以此类推... | 以此类推... |


## 增强模式
Knife4j自2.0.6版本开始,将目前在Ui界面中一些个性化配置剥离,开发者可以在后端进行配置，并且提供的knife4j-spring-boot-strater组件自动装载

在以前的版本中,开发者需要在配置文件中手动使用@EnableKnife4j来使用增强，自2.0.6版本后,只需要在配置文件中配置knife4j.enable=true即可不在使用注解
注意：要使用Knife4j提供的增强，knife4j.enable=true必须开启

各个配置属性说明如下：

| 属性 | 默认值 | 说明 |
| :-----| :---- | :---- |
| knife4j.enable | false | 是否开启Knife4j增强模式 |
| knife4j.cors | false | 是否开启一个默认的跨域配置,该功能配合自定义Host使用 |
| knife4j.production | false | 是否开启生产环境保护策略 |
| knife4j.basic |  | 对Knife4j提供的资源提供BasicHttp校验,保护文档 |
| knife4j.basic.enable | false | 关闭BasicHttp功能 |
| knife4j.basic.username |  | basic用户名 |
| knife4j.basic.password |  | basic密码 |
| knife4j.documents |  | 自定义文档集合，该属性是数组 |
| knife4j.documents.group |  | 所属分组 |
| knife4j.documents.name |  | 类似于接口中的tag,对于自定义文档的分组 |
| knife4j.documents.locations |  | markdown文件路径,可以是一个文件夹(classpath:markdowns/*)，也可以是单个文件(classpath:md/sign.md) |
| knife4j.setting |  | 前端Ui的个性化配置属性 |
| knife4j.setting.enableAfterScript | true | 调试Tab是否显示AfterScript功能,默认开启 |
| knife4j.setting.language | zh-CN | Ui默认显示语言,目前主要有两种:中文(zh-CN)、英文(en-US) |
| knife4j.setting.enableSwaggerModels | true | 是否显示界面中SwaggerModel功能 |
| knife4j.setting.swaggerModelName | Swagger Models | 重命名SwaggerModel名称 |
| knife4j.setting.enableDocumentManage | true | 是否显示界面中"文档管理"功能 |
| knife4j.setting.enableReloadCacheParameter | false | 是否在每个Debug调试栏后显示刷新变量按钮,默认不显示 |
| knife4j.setting.enableVersion | false | 是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点 |
| knife4j.setting.enableRequestCache | true | 是否开启请求参数缓存 |
| knife4j.setting.enableFilterMultipartApis | false | 针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址 |
| knife4j.setting.enableFilterMultipartApiMethodType | POST | 具体接口的过滤类型 |
| knife4j.setting.enableHost | false | 是否启用Host |
| knife4j.setting.enableHomeCustom | false | 是否开启自定义主页内容 |
| knife4j.setting.homeCustomLocation |  | 主页内容Markdown文件路径 |
| knife4j.setting.enableSearch | false | 是否禁用Ui界面中的搜索框 |
| knife4j.setting.enableFooter | true | 是否显示Footer |
| knife4j.setting.enableFooterCustom | false | 是否开启自定义Footer |
| knife4j.setting.footerCustomContent | false | 自定义Footer内容 |
| knife4j.setting.enableDynamicParameter | false | 是否开启动态参数调试功能 |
| knife4j.setting.enableDebug | true | 启用调试 |
| knife4j.setting.enableOpenApi | true | 显示OpenAPI规范 |
| knife4j.setting.enableGroup | true | 显示服务分组 |