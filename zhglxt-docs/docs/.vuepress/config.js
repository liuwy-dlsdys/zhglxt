// .vuepress/config.js
module.exports = {
  // 主机ip
  host: '127.0.0.1',
  // 访问端口
  port: '80',
  // 基础访问路径
  base:'/',
  // 网站的标题（左上角）
  title: 'WEB应用系统',
  // 网站的描述，它将会以 <meta> 标签渲染到当前页面的 HTML 中
  description: 'WEB应用系统',
  head: [
    ['link', { rel: 'icon', href: '/images/favicon.png' }] // 需要被注入到当前页面的 HTML <head> 中的标签
  ],
  // 语言
  //lang: 'zh-CN',

  // 站点-多语言配置
/*  locales: {
    // 键名是该语言所属的子路径
    // 作为特例，默认语言可以使用 '/' 作为其路径。
    '/': {
      lang: 'en-US',
      title: 'WEB应用系统',
      description: 'web application system document',
    },
    '/zh/': {
      lang: 'zh-CN',
      title: 'WEB应用系统',
      description: 'WEB应用系统-文档',
    }
  },*/

  // 主题配置 begin
  themeConfig: {
    // Logo 图片的 URL，将会显示在导航栏的左端。
    // Public 文件路径（默认： logo: 'https://vuejs.org/images/logo.png'）
    logo: '/images/favicon.png',

    // 是否启用切换夜间模式的功能（默认true）
    // 如果设置为 true ，将会在导航栏展示一个切换夜间模式的按钮，并会根据 prefers-color-scheme 自动设置初始模式
    darkMode: true,
    // 配置夜间模式的logo
    // 如果你想在夜间模式中使用不同的 Logo 图片，就可以使用该配置项
    // 设置为 null 可以在夜间模式下禁用 Logo 。忽略该配置项将会在夜间模式中使用 logo 配置
    //logoDark: null,

    // 导航栏（右上角）
    navbar: [
      {
        text: '首页',
        link: '/',
      },
      // 嵌套 Group - 最大深度为 2
      {
        text: '系统生态',
        children: [
          {
            text: '项目',
            children: ['/group/sub/project.md', '/group/sub/projectBar.md'],
          },
          {
            text: '扩展列表',
            children: ['/group/sub/extendList.md', '/group/sub/extendListBar.md'],
          },
          {
            text: '帮助',
            children: ['/group/sub/help.md', '/group/sub/helpBar.md'],
          },
        ],
      },

      {
        text: 'GitHub',
        link: 'https://github.com/liuwy-dlsdys/zhglxt',
      },
      {
        text: 'GitEE',
        link: 'https://gitee.com/liuwy_dlsdys/zhglxt',
      },

      // 控制元素何时被激活
/*      {
        text: 'ElementActivate',
        children: [
          {
            text: 'Always active',
            link: '/',
            // 该元素将一直处于激活状态
            activeMatch: '/',
          },
          {
            text: 'Active on /foo/',
            link: '/not-foo/',
            // 该元素在当前路由路径是 /foo/ 开头时激活
            // 支持正则表达式
            activeMatch: '^/foo/',
          },
        ],
      },*/

      //
    ],

    // 侧边栏配置。你可以通过页面的 sidebar frontmatter 来覆盖这个全局配置
    // 设置为 false 可以禁用侧边栏
    // 如果你设置为 'auto'，侧边栏会根据页面标题自动生成
    // 为了手动配置侧边栏元素，你可以将其设置为 侧边栏数组 ，其中的每个元素是一个 SidebarItem 对象或者一个字符串
    // SidebarItem 对象应该有一个 text 字段，有一个可选的 link 字段和一个可选的 children 字段。 children 字段同样是一个 侧边栏数组
    // 字符串应为目标页面文件的路径。它将会被转换为 SidebarItem 对象，将页面标题作为 text ，将页面路由路径作为 link ，并根据页面小标题自动生成 children
    // 如果你想在不同子路径中使用不同的侧边栏，你可以将该配置项设置为 侧边栏对象
    // Key 为路径前缀
    // Value 为 侧边栏数组

    // 侧边栏数组
    // 所有页面会使用相同的侧边栏
    /*sidebar: [
      // SidebarItem
      {
        text: 'Foo',
        link: '/foo/',
        children: [
          // SidebarItem
          {
            text: 'github',
            link: 'https://github.com',
            children: [],
          },
          // 字符串 - 页面文件路径
          '/foo/bar.md',
        ],
      },
      // 字符串 - 页面文件路径
      '/bar/README.md',
    ],*/

    // 侧边栏对象
    // 不同子路径下的页面会使用不同的侧边栏
    sidebar: {
      '/guide/': [
        {
          text: '系统文档',
          children: ['/guide/README.md','/guide/getting-started.md'],
        }
      ],
      '/other/': [
        {
          text: '其它',
          children: ['/other/README.md','/other/donateSupport.md'],
        }
      ],
      // fallback 侧边栏被最后定义。
      // 官网提示：不能放在数组第一个位置，否则会混乱
      '/':['']
    },

    // 项目仓库的 URL。它将被用作 仓库链接 的链接。仓库链接 将会显示为导航栏的最后一个元素
    // 如果你按照 `organization/repository` 的格式设置它
    // 我们会将它作为一个 GitHub 仓库
    //repo: 'zhglxt/gitHub',
    // 你也可以直接将它设置为一个 URL
    //repo: 'https://gitlab.com/foo/bar',
    // 项目仓库的标签。
    // 它将被用作 仓库链接 的文字。仓库链接 将会显示为导航栏的最后一个元素。如果你不明确指定该配置项，它将会根据 repo 配置项自动推断。
    //repoLabel: 'GitHub仓库链接 的文字说明',

    // 主题-多语言配置
/*    locales: {
      '/zh/': {
        selectLanguageName: '简体中文',
      },
      '/': {
        // Locale 的语言名称。该配置项 仅能在主题配置的 locales 的内部生效 。它将被用作 locale 的语言名称，展示在 选择语言菜单 内
        selectLanguageName: 'English',
      },
    },*/

    // 选择语言菜单 的文字
    // 如果你在站点配置中设置了多个 locales ，那么 选择语言菜单 就会显示在导航栏中仓库按钮的旁边
    selectLanguageText: '选择语言',

    // 选择语言菜单 的 aria-label 属性。它主要是为了站点的可访问性
    // 类型： string
    //selectLanguageAriaLabel: '',


    // 设置根据页面标题自动生成的侧边栏的最大深度。（默认值： 2）
    /*
      设为 0 来禁用所有级别的页面标题。
      设为 1 来包含 <h2> 标题。
      设为 2 来包含 <h2> 和 <h3> 标题。
    */
    // 最大值取决于你通过 markdown.extractHeaders.level 提取了哪些级别的标题
    // 由于 markdown.extractHeaders.level 的默认值是 [2, 3] ，因此 sidebarDepth 的默认最大值是 2
    // 你可以通过页面的 sidebarDepth frontmatter 来覆盖这个全局配置
    sidebarDepth: 2,

    // 是否启用 编辑此页 链接（默认值： true）
    // 你可以通过页面的 editLink frontmatter 来覆盖这个全局配置
    editLink: true,

    // 编辑此页 链接的文字（默认值： 'Edit this page'）
    editLinkText: 'Edit this page',

    /*
      文档源文件的仓库 URL 。它将会用于生成 编辑此页 的链接
      如果你不设置该选项，则默认会使用 repo 配置项。但是如果你的文档源文件是在一个不同的仓库内，你就需要设置该配置项了
    */
    // docsRepo: 'https://gitee.com/liuwy_dlsdys/zhglxt',

    /*
      文档源文件的仓库分支
      它将会用于生成 编辑此页 的链接
    */
    // docsBranch: 'master',

    /*
      文档源文件存放在仓库中的目录名
      它将会用于生成 编辑此页 的链接
    */
    // docsDir: 'zhglxt-docs',

    /*
    编辑此页 链接的 Pattern
    它将会用于生成 编辑此页 的链接
    如果你不设置该选项，则会根据 docsRepo 配置项来推断 Pattern 。但是如果你的文档仓库没有托管在常用的平台上，比如 GitHub 、 GitLab 、 Bitbucket 、 Gitee 等，那么你必须设置该选项才能使 编辑此页 链接正常工作
      用法:
       Pattern	       描述
        :repo	    文档仓库 URL ，即 docsRepo
        :branch	    文档仓库分支 ，即 docsBranch
        :path	    页面源文件的路径，即 docsDir 拼接上页面文件的相对路径
     */
    // editLinkPattern: ':repo/-/edit/:branch/:path',

    // 则会生成类似于 'https://gitlab.com/owner/name/-/edit/master/docs/path/to/file.md' 的链接。

    // 是否启用 最近更新时间戳（默认值： true）
    /*
      你可以通过页面的 lastUpdated frontmatter 来覆盖这个全局配置
      要注意的是，如果你将 themeConfig.lastUpdated 设为了 false ，那么这个功能会被完全禁用，并且无法在 locales 或页面 frontmatter 中启用
    */
    lastUpdated: true,

    // 最近更新时间戳 标签的文字（默认值： 'Last Updated'）
    lastUpdatedText: 'Last Updated',

    // 是否启用 贡献者列表 （默认值： true）
    /*
      你可以通过页面的 contributors frontmatter 来覆盖这个全局配置。
      要注意的是，如果你将 themeConfig.contributors 设为了 false ，那么这个功能会被完全禁用，并且无法在 locales 或页面 frontmatter 中启用
    */
    contributors: true,

    // 贡献者列表 标签的文字（默认值： 'Contributors'）
    contributorsText: 'Contributors',

    //Tip 自定义容器 的默认标题（默认值： 'TIP'）
    tip: 'TIP',

    // Warning 自定义容器 的默认标题（默认值： 'WARNING'）
    warning: 'WARNING',

    // Danger 自定义容器 的默认标题（默认值： 'DANGER'）
    danger: 'DANGER',

    // 404 页面的提示信息（默认值： ['Not Found']）
    // 当用户进入 404 页面时，会从数组中随机选取一条信息进行展示
    notFound: ['Not Found'],

    // 404 页面中 返回首页 链接的文字（默认值： 'Back to home'）
    backToHome: 'Back to home',

    // OutboundLink 链接内的 sr-only 文字。它主要是为了站点的可访问性 （默认值： 'open in new window'）
    openInNewWindow: 'open in new window',

    // 切换夜间模式按钮的标题文字。它主要是为了站点的可访问性（默认值： 'toggle dark mode'）
    toggleDarkMode: 'toggle dark mode',

    // 切换侧边栏按钮的标题文字。它主要是为了站点的可访问性（默认值： 'toggle sidebar'）
    toggleSidebar: 'toggle sidebar',

  },
  // 主题配置 end

  // 插件配置
  // 设置默认主题使用的插件
  // 默认主题使用了一些插件，如果你确实不需要该插件，你可以选择禁用它。在禁用插件之前，请确保你已了解它的用途
  themePlugins: {
    // 是否启用 @vuepress/plugin-active-header-links（默认值： true）
    activeHeaderLinks: true,
    // 是否启用 @vuepress/plugin-back-to-top（默认值： true）
    backToTop: true,
    // 是否启用由 @vuepress/plugin-container 支持的自定义容器
    // 类型： Record<ContainerType, boolean>
    /*
    ContainerType 类型为：
      tip
      warning
      danger
      details
      codeGroup
      codeGroupItem

      md用法：
      ::: <type> [title]
      [content]
      :::

      type 是必需的， title 和 content 是可选的。

      支持的 type 有：
        tip
        warning
        danger
        details
        CodeGroup 和 CodeGroupItem 的别名：
          code-group
          code-group-item

      示例：
      ::: tip
      这是一个提示
      :::

      ::: warning
      这是一个警告
      :::

      ::: danger
      这是一个危险警告
      :::

      ::: details
      这是一个 details 标签
      :::
    */
    container: true,

    // 是否启用 @vuepress/plugin-git（默认值： true）
    git: true,
    // 是否启用 @vuepress/plugin-medium-zoom （默认值： true）
    mediumZoom: true,
    // 是否启用 @vuepress/plugin-nprogress （默认值： true）
    nprogress: true,
  },

}