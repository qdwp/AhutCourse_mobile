# Ahut_mobile_course

## 中国十七冶移动OA办公系统

**描述**

系统使用`python`作为开发语言，使用`Flask`web框架

以原有`.NET`框架PC版页面功能为对照，实现移动OA办公

**技术实现**

* 使用`http`进行模拟登陆，使用 python 的第三方库`requests`
* `GET`、`POST`方法获取目的网页`html`或者`json`信息
* 使用`BeautifulSoup4`解析`requests`下载的网页信息或者使用`json`解析返回信息
* `MySQLdb`，`python` 连接 `mysql` 数据库
* 使用 `.NET` 将 `doc`、`pdf` 文档解析为 `html` 并展示