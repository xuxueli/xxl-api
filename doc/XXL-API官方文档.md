## 《API管理平台XXL-API》
## 一、简介

### 1.1 概述
XXL-API是一个简洁易用API管理平台，提供API的"管理"、"文档"、"Mock"和"测试"等功能。现已开放源代码，开箱即用。

### 1.2 特性
- 1、极致简单：交互简洁，一分钟上手；
- 2、项目隔离：API以项目为维度进行拆分隔离；
- 3、分组管理：单个项目内的API支持自定义分组进行管理；
- 4、标记星级：支持标注API星级，标记后优先展示；
- 5、API管理：创建、更新和删除API；
- 6、API属性完善：支持设置丰富的API属性如：API状态、请求方法、请求URL、请求头部、请求参数、响应结果、响应结果格式、响应结果参数、API备注等等；
- 7、markdown：支持为API添加markdown格式的备注信息；
- 8、Mock：支持为API定义Mock数据并制定数据响应格式，从而快速提供Mock接口，加快开发进度；
- 9、在线测试：支持在线对API进行测试并保存测试数据，提供接口测试效率；

### 1.3 下载

#### 文档地址

- [中文文档](http://www.xuxueli.com/xxl-api/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-api](https://github.com/xuxueli/xxl-api) | [Download](https://github.com/xuxueli/xxl-api/releases)  
[http://gitee.com/xuxueli0323/xxl-api](http://gitee.com/xuxueli0323/xxl-api) | [Download](http://gitee.com/xuxueli0323/xxl-api/releases)

#### 技术交流
- [社区交流](http://www.xuxueli.com/page/community.html)

### 1.4 环境
- Servlet/JSP Spec：3.0/2.2
- JDK：1.7+
- Tomcat：7+/Jetty8+
- Mysql：5.6+
- Maven：3+


## 二、快速部署

### 2.1 初始化“调度数据库”
请下载项目源码并解压，获取 "初始化SQL脚本"，脚本位置：
```
/xxl-api/doc/db/xxl-api-mysql.sql
```

### 2.2 编译源码
解压源码,按照maven格式将源码导入IDE, 使用maven进行编译即可，源码结构如下图所示：

![输入图片说明](https://static.oschina.net/uploads/img/201704/05201440_n2GZ.png "在这里输入图片标题")

### 2.3 配置JDBC连接
在以下项目文件中设置应用的JDBC连接；
```
/xxl-api/xxl-api-admin/src/main/resources/xxl-api.properties
```

### 2.4 部署
将应用"xxl-api-admin"部署在容器如Tomcat下之后，启动后访问即可进入以下界面：

![输入图片说明](https://static.oschina.net/uploads/img/201704/05202654_h3ze.png "在这里输入图片标题")

## 三、项目管理

系统中API以项目为单位进行管理，因此首先需要管理项目；项目管理界面如下图所示；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05210504_mE4W.png "在这里输入图片标题")

### 3.1 新建项目
进入项目管理界面，点击右侧"+新增项目"按钮可新建项目，如下图所示：

![输入图片说明](https://static.oschina.net/uploads/img/201704/05210158_YwbP.png "在这里输入图片标题")

项目属性说明：
    
    项目名称：项目的名称；
    项目描述：项目的描述信息；
    访问权限："公开"权限，表示所有人可操作；"私有"权限，表示只有管理员或者项目成员可以操作（功能自测中，即将推送）；
    跟地址-线上环境：项目线上环境跟地址，项目中的API共用该跟地址；
    跟地址-预发布环境：项目预发布环境跟地址；
    跟地址-测试环境：项目测试环境跟地址；
    版本：项目的版本信息；

### 3.2 更新项目
进入项目管理界面，点击项目右侧的"编辑"按钮可更新项目信息，如下图所示；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05211117_vUTZ.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05211057_wnqY.png "在这里输入图片标题")

### 3.3 删除项目

进入项目管理界面，点击项目右侧的"删除"按钮可删除项目信息；注意，项目中存在API时不允许删除；

## 四、API管理
在项目管理界面，点击项目右侧的"进入项目"按钮，可进入接口管理界面，如下图所示：

![输入图片说明](https://static.oschina.net/uploads/img/201704/05212801_oPyI.png "在这里输入图片标题")

### 4.1 API分组管理

- 新增API分组

如下图，点击"左侧接口分组区域"右上角的"+"按钮，可新增AIP接口分组；（点击"全部"将会展示项目中所有分组下的接口；"默认分组"为系统分组，不允许删除；）

![输入图片说明](https://static.oschina.net/uploads/img/201704/05213307_4ekE.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05213136_AbS4.png "在这里输入图片标题")

接口分组属性说明：

    分组名称：分组的名称
    分组排序：分组的排序顺序，数字类型，值越小越靠前；
    
- 更新API分组

在"左侧接口分组区域"，点击对应的API分组，右侧将会展示该分组下API接口列表；如下图，点击接口列表顶部的"编辑分组"按钮（新增的API分组才会有该功能），可修改API分组信息；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05214001_7QHI.png "在这里输入图片标题")

- 删除API分组

在"左侧接口分组区域"，点击对应的API分组，右侧将会展示该分组下API接口列表；点击接口列表顶部的"删除分组"按钮（新增的API分组才会有该功能），可修改API分组信息；

### 4.2 API管理

- 新增API

如下图，在API接口管理界面，点击接口列表顶部的"新增接口"按钮，可进入新增接口界面；
在新增接口界面，如下图所示，可以设置接口的API状态、请求方法、请求URL、请求头部、请求参数、响应结果、响应结果格式、响应结果参数、API备注等等信息；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05215241_E7yW.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05215355_Gwcs.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05215746_QTYz.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05215756_IRJc.png "在这里输入图片标题")

API属性说明：

    基础信息：
        接口分组：接口所属的分组；
        接口状态：接口的状态，在接口列表中，启用状态接口用绿色圆圈标识，维护状态接口用黄色圆圈标识，废弃状态接口用灰色圆圈标识；
        请求方法：请求方法，如POST、GET等；
        接口URL：接口请求的URL地址，注意此处为相对地址，跟地址从所属项目的跟地址属性上获取；
        接口名称：接口的名称；
    请求头部：同一接口支持设置多个请求头部；
        头部标签：请求头部的类型，如Accept-Encoding；
        头部内容：请求头部的数据，如Accept-Encoding头部标签的头部内容UTF-8；
    请求参数：同一接口支持设置多个请求参数；
        是否必填：该参数是否必填；
        参数类型：该参数的数据类型，如STRING；
        参数名称：参数的名称；
        参数说明：参数的说明；
    响应结果：分别支持设置 "成功响应结果" 和 "失败响应结果"，作为接口响应数据的参考；
        响应数据类型(MIME)：响应结果类型，如JSON、XML等；
        响应结果数据：响应结果的数据，如响应结果类型为JSON时可设置响应结果数据为一段JSON数据；
    响应结果参数：对接口"响应结果"中参数的补充说明，如响应结果类型为JSON时，可在此处一一列出JSON各个字段的参数信息；
        是否非空：该参数是否可能为空；
        参数类型：该参数的数据类型，如STRING；
        参数名称：参数的名称；
        参数说明：参数的说明；
    接口备注：markdown方式的接口备注；

- 更新API

在API接口管理界面，点击接口右侧的"更新接口图标"按钮，可进入更新接口界面；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05221702_uWs0.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05221654_gTs1.png "在这里输入图片标题")

- 删除API

在API接口管理界面，点击接口右侧的"删除接口图标"按钮，可删除接口数据；

### 4.3 API-Mock

- 新增Mock数据
在API接口管理界面，点击接口名称，进入"接口详情页"，在接口详情页的"Mock数据"模块右上角点击"+Mock数据"按钮，可新增Mock数据；

Mock数据属性说明：

    数据类型(MIME)：响应结果类型，如JSON、XML等；
    结果数据：响应结果的数据，如响应结果类型为JSON时可设置响应结果数据为一段JSON数据；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05222204_q9Rw.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05222214_HLAL.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05222223_sofA.png "在这里输入图片标题")

- 更新Mock数据
在"接口详情页"的"Mock数据"模块，点击Mock数据列表右侧的"修改"按钮，可修改Mock数据；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05222232_hgvY.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05222240_MXxC.png "在这里输入图片标题")

- 删除Mock数据

在"接口详情页"的"Mock数据"模块，点击Mock数据列表右侧的"删除"按钮，可删除Mock数据；

- 运行Mock数据

在"接口详情页"的"Mock数据"模块，点击Mock数据列表右侧的"运行"按钮，可运行Mock数据；
系统将会为每一条Mock数据生成一个唯一的Mock连接，访问该连接将会按照设置的数据类型如JSON返回对应格式的Mock数据，如下图所示；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05222730_HRvf.png "在这里输入图片标题")


### 4.4 API-测试

- API-测试
进入"接口详情页"，点击"Test历史"模块右上角的"+接口测试"按钮，可进入"接口测试界面"，
该界面将会自动初始化接口URL（测试界面支持选择运行环境，将会自动生成不同环境的完整URL连接）和参数等信息。
只需要填写测试的参数值，点击下方"运行"按钮，即可发起一次接口请求，请求结果将会在下方显示出来：

![输入图片说明](https://static.oschina.net/uploads/img/201704/05223243_d1wm.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05223129_4IvK.png "在这里输入图片标题")

- 保存Test历史

在"接口测试界面"，在进行接口测试后, 点击下方"保存"按钮将会把本次测试数据（接口URL，测试参数等信息）保存下来。
在"接口详情页"的"Test历史"模块可查看所有的接口测试历史记录。点击一次测试记录右侧的"运行"按钮，将会进入到本次测试记录对应的接口测试界面，还原当时测试时使用的测试数据；

- 删除Test历史

在"接口详情页"的"Test历史"模块，点击测试历史记录右侧的"删除"按钮可删除本条记录；


## 五、用户管理
### 5.1、新增用户
进入用户管理界面，点击右上角"+新增用户"按钮，可添加用户信息；

![输入图片说明](https://static.oschina.net/uploads/img/201704/05224558_AVPQ.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201704/05224610_6IlL.png "在这里输入图片标题")

用户属性说明：
    
    登录账号：用户的登录账号；
    登录密码：用户的登录密码；
    用户类型：
        普通用户：只允许操作公开项目，或者自己拥有项目权限的私有项目；
        超级管理员：拥有所有项目的操作权限；
    真实姓名：用户的真实姓名；

### 5.2、更新用户
进入用户管理界面，点击用户列表页中用户右侧的"编辑"按钮可编译用户信息；

### 5.3、删除用户
进入用户管理界面，点击用户列表页中用户右侧的"删除"按钮可删除用户信息；

## 六、版本更新日志
### 6.1 版本 V1.0.0，新特性
- 1、极致简单：交互简洁，一分钟上手；
- 2、项目隔离：API以项目为维度进行拆分隔离；
- 3、分组管理：单个项目内的API支持自定义分组进行管理；
- 4、标记星级：支持标注API星级，标记后优先展示；
- 5、API管理：创建、更新和删除API；
- 6、API属性完善：支持设置丰富的API属性如：API状态、请求方法、请求URL、请求头部、请求参数、响应结果、响应结果格式、响应结果参数、API备注等等；
- 7、markdown：支持为API添加markdown格式的备注信息；
- 8、Mock：支持为API定义Mock数据并制定数据响应格式，从而快速提供Mock接口，加快开发进度；
- 9、在线测试：支持在线对API进行测试并保存测试数据，提供接口测试效率；

### 6.12 版本 V1.1.0 特性（Coding）
- 1、新增"业务线"管理功能，针对项目以业务线为粒度进行分类管理，该功能仅向管理员开放；
- 2、项目新增"业务线"属性；项目列表支持通过"业务线"条件查询；
- 3、项目内API搜索关键字改为URL，更加贴合用户需求；
- 4、设计实现API "响应数据类型" 模块，"响应数据类型"不仅可以被多个API复用，而且支持数据类型嵌套；
- 5、"数据类型"功能：系统支持录入数据类型，数据类型支持嵌套，每个API只需要绑定一个数据类型，不需要单独执行响应数据参数； 
- 6、修改密码功能；
- 7、项目maven依赖升级；
- 8、UI交互优化，列表自适应性优化；
- 9、底层代码重构；
- 10、登陆Cookie启用HttpOnly；
- 11、弹框插件改为使用Layui；
- 12、AdminLTE版本升级；
- 13、密码进行md5加密；


### TODO LIST
- 1、项目权限：支持对项目设置权限，拥有权限才允许操作项目中API；
- 2、API历史版本：支持对API修改历史版本进行对比，版本回溯等操作；
- 3、弹框插件优化：考虑第三方插件；
- 4、支持接口签名，sign逻辑；
- 5、API导出为HTML或者PDF；
- 6、API响应结果对象管理；
- 7、支持设置RequestBody类型参数；
- 8、拥有人、管理员才允许删除；记录修改历史；
- 9、项目权限：支持对项目设置权限，拥有权限才允许操作项目中API；
- 10、根据URL全站匹配API；
- 11、数据类型，代码生成
- 12、配合xxl-web，实现线上Mocker，支持根据mock-uuid，对调用进行自动mock；
- 13、配合xxl-web，实现根据代码代码自动生成api文档并导入xxl-api管理平台；
- 14、支持Restful类型接口；
- 15、请求参数，除常规form之外，支持选择RequestBody方式，传递Json、XML和文本等数据；

## 七、其他

### 7.1 项目贡献
欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-api/issues/) 讨论新特性或者变更。

### 7.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-api/issues/1 ) 登记，登记仅仅为了产品推广。

### 7.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论金额多少都足够表达您这份心意，非常感谢 ：）    [XXL系列捐赠记录](http://www.xuxueli.com/page/donate.html )

微信：<img src="https://raw.githubusercontent.com/xuxueli/xxl-job/master/doc/images/donate-wechat.png" width="200">
支付宝：<img src="https://raw.githubusercontent.com/xuxueli/xxl-job/master/doc/images/donate-alipay.jpg" width="200">
