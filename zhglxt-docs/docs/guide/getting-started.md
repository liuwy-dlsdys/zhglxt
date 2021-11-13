# 快速开始
## WEB应用系统
### 开发环境、部署
1. 安装好系统所需的运行、开发环境（JDK1.8+、MySQL5.7+、maven3.6+、Git环境等...）
2. 使用IntelliJ IDEA拉取并打开项目
![](zhglxt/userfiles/system/images/helpImages/sys/1.jpg)

3. 复制项目Git地址
![](zhglxt/userfiles/system/images/helpImages/sys/2.jpg)

4. 接着填入IDEA
![](zhglxt/userfiles/system/images/helpImages/sys/3.jpg)

5. 拉取完毕后，Maven就会自动下载项目所有依赖包到本地中央仓库。目录结构如下
![](zhglxt/userfiles/system/images/helpImages/sys/4.jpg)

6. 第一次需要install安装到本地仓库
![](zhglxt/userfiles/system/images/helpImages/sys/5.jpg)

7. 导入数据文件的数据到本地数据库  
![](zhglxt/userfiles/system/images/helpImages/sys/6.jpg)

8. 修改MySQL数据库的端口、用户名、密码为自己本地的数据库信息
![](zhglxt/userfiles/system/images/helpImages/sys/7.jpg)

9. 所有步骤没问题之后，就可以直接运行项目了，启动文件
![](zhglxt/userfiles/system/images/helpImages/sys/8.jpg)

10. 访问路径：localhost:8888/zhglxt  登录账号密码：system/system【提示：系统中的所有用户的密码与该登录名一致。如 test1密码为test1；test2密码为test2】

11. 打包部署（本项目是打成war包的方式进行部署的）
![](zhglxt/userfiles/system/images/helpImages/sys/9.jpg)

12. 打包完成后，到项目的目录下找到zhglxt\zhglxt-web\target\zhglxt-web.war包，重命名为zhglxt.war，然后直接放到Tomcat的webapps目录下，就可以运行起来了
![](zhglxt/userfiles/system/images/helpImages/sys/10.jpg)

13. 本项目不需要修改过多的配置信息，即可打包、部署、运行

## Activiti工作流
### 新增流程模型
工作流程管理-模型-模型管理-添加模型
![](zhglxt/userfiles/system/images/helpImages/activiti/1.jpg)

![](zhglxt/userfiles/system/images/helpImages/activiti/2.jpg)

![](zhglxt/userfiles/system/images/helpImages/activiti/3.jpg)

### 设计好流程图后，进行部署
![](zhglxt/userfiles/system/images/helpImages/activiti/4.jpg)

### 查看成功部署的流程
![](zhglxt/userfiles/system/images/helpImages/activiti/5.jpg)

### 流程管理
![](zhglxt/userfiles/system/images/helpImages/activiti/6.jpg)
其中 转为模型 操作：
如果 模型-模型管理 中已经存在需要转换的（流程标识、流程版本）时，会在原有的版本基础上升1级（比如 版本号为1 升级后为 2），再次部署后就会以升级后的版本为基准进行任务流转。
如果 模型-模型管理 中不存在需要转换的（流程标识、流程版本）时，转换模型后，就新增一个模型，且版本号为1，成功部署后就可以进行任务流转了。

![](zhglxt/userfiles/system/images/helpImages/activiti/7.jpg)

![](zhglxt/userfiles/system/images/helpImages/activiti/8.jpg)

下面进行部署新版本的模型：
![](zhglxt/userfiles/system/images/helpImages/activiti/9.jpg)

![](zhglxt/userfiles/system/images/helpImages/activiti/10.jpg)
流程列表中 的 转为模型 和 流程管理 中的 转为模型 是一样的，流程标识、流程版本存在就升级版本，不存在就新增一个模型。删除后 流程管理 中的也会进行同步删除。

### 查看运行中的流程
![](zhglxt/userfiles/system/images/helpImages/activiti/11.jpg)

## DOCS文档系统
### 安装、运行
::: warning
   注意：此文档系统使用的是`VuePress2.X`。开始之前，确保已经安装`Node.js v12+`环境
:::

使用idea的终端(也可以使用powerShell命令窗口)输入一下命令：
```
# 进入zhglxt-docs目录
cd zhglxt-docs

# 安装
npm install -D vuepress@next

# 启动
npm run dev
```
![](zhglxt/userfiles/system/images/helpImages/docs/1.png)
![](zhglxt/userfiles/system/images/helpImages/docs/2.png)

启动成功后，访问：`http://127.0.0.1:80`

### 部署到nginx
此教程是部署到windows的，linux的就不赘述了（基本差不多）。
```
# 构建静态文件(构建完毕后会在zhglxt-docs\docs\.vuepress目录下生成dist文件夹)
npm run build
```
![](zhglxt/userfiles/system/images/helpImages/docs/3.png)
build完成后，修改nginx（下载好nginx：`http://nginx.org/en/download.html`）conf目录下的nginx.conf文件，找到里面的'server'节点的（主要修改 `listen` 端口和新增`location /zhglxt-docs`节点，其中`alias`后面为需要部署的`dist`路径）
```
    server {
        listen       88;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }
		
		location /zhglxt-docs {
           alias  C:\\Users\\Administrator\\Documents\\dist;
           index  index.html index.htm;
           try_files $uri $uri/ /index.html =404;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }
```
修改完成保存后，启动nginx：进入nginx目录执行以下命令（也可以直接双击`nginx.exe`）：
```
start nginx
```
执行完毕之后是一闪而过的，查看下`任务管理器`,看后台进程那里，往下拉看到`nginx.exe(32位)`，说明启动nginx成功，访问路径：`localhost:88`


### 配置(`此后步骤了解即可`)
如果没有任何配置，你的 VuePress 站点仅有一些最基础的功能。为了更好地自定义你的网站，让我们首先在你的文档目录下创建一个 .vuepress 目录，所有 VuePress 相关的文件都将会被放在这里。你的项目结构可能是这样：
```
├─ docs
│  ├─ .vuepress
│  │  ├── public
│  │  └─ config.js
│  └─ README.md
├─ .gitignore
└─ package.json

```
VuePress 站点必要的配置文件是 .vuepress/config.js，它应该导出一个 JavaScript 对象。如果你使用 TypeScript ，你可以将其替换为 .vuepress/config.ts ，以便让 VuePress 配置得到更好的类型提示。

```js
// .vuepress/config.js
module.exports = {
  lang: 'zh-CN',
  title: '你好， VuePress ！',
  description: '这是我的第一个 VuePress 站点',

  themeConfig: {
    logo: '/images/favicon.png'
  }
}
```


### 常见目录结构、说明
```
.
├── docs
│   ├── .vuepress (可选的)
│   │   ├── components (可选的)
│   │   ├── theme (可选的)
│   │   │   └── Layout.vue
│   │   ├── public (可选的)
│   │   ├── styles (可选的)
│   │   │   ├── index.styl
│   │   │   └── palette.styl
│   │   ├── templates (可选的, 谨慎配置)
│   │   │   ├── dev.html
│   │   │   └── ssr.html
│   │   ├── config.js (可选的)
│   │   └── enhanceApp.js (可选的)
│   │ 
│   ├── README.md（会被编译成index.html文件）
│   ├── guide (一般用户都在这个目录下创建网站指南,当然可以不用)
│   │   └── README.md （指南里面的具体内容）
│   └── config.md
│ 
└── package.json 项目初始化时，根目录下自动生成的配置文件,定义了项目的基本配置信息及需要依赖的各个模块、指定运行脚本命令的npm命令行缩写等。
 
```
- docs/.vuepress: 用于存放全局的配置、组件、静态资源等。
- docs/.vuepress/components: 该目录中的 Vue 组件将会被自动注册为全局组件。
- docs/.vuepress/theme: 用于存放本地主题。
- docs/.vuepress/styles: 用于存放样式相关的文件。
- docs/.vuepress/styles/index.styl: 将会被自动应用的全局样式文件，会生成在最终的 CSS 文件结尾，具有比默认样式更高的优先级。
- docs/.vuepress/styles/palette.styl: 用于重写默认颜色常量，或者设置新的 stylus 颜色常量。
- docs/.vuepress/public: 静态资源目录。
- docs/.vuepress/templates: 存储 HTML 模板文件。
- docs/.vuepress/templates/dev.html: 用于开发环境的 HTML 模板文件。
- docs/.vuepress/templates/ssr.html: 构建时基于 Vue SSR 的 HTML 模板文件。
- docs/.vuepress/config.js: 配置文件的入口文件，也可以是 YML 或 toml。
- docs/.vuepress/enhanceApp.js: 客户端应用的增强。

### 配置作用域
在 VuePress 配置中有一项 themeConfig 配置项。

在 themeConfig 外部的配置项属于 站点配置 ，而在 themeConfig 内部的配置项则属于 主题配置。

### 站点配置
站点配置的意思是，无论你使用什么主题，这些配置项都可以生效。

每一个站点都应该有它的 lang, title 和 description 等属性，因此 VuePress 内置支持了这些属性的配置。

### 主题配置
主题配置将会被 VuePress 主题来处理，所以它取决于你使用的主题是什么。

如果你没有设置 VuePress 配置的 theme 配置项，则代表使用的是默认主题。