# intellij-sql-alias-camel-plugin

修改自intellij官方示例[intellij-sdk-docs
](https://github.com/JetBrains/intellij-sdk-docs)中的editor_basics

## 功能
给SQL查询语句字段添加驼峰别名，右键选中的文本，点击`sql alias CamelCase`，可以将“foo_bar, b.foo_foo_bar”转换"foo_bar fooBar, b.foo_foo_bar fooFooBar”

## 安装
releases页面下载zip包，IDEA中file -> setting -> plugins -> install plugin from disk... -> 选择下载的zip包
