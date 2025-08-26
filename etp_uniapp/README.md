# 专家培训平台

#### 介绍
企业版专家培训平台，提供学习课程、考试测验、活动报名、问卷调查等功能。

#### 软件架构
软件使用uniapp开发，遵循Vue2以及微信小程序规则。


#### 安装配置教程

使用HBulider X开发，HBulider X点击文件-->导入-->从本地目录导入，选择本项目导入。本项目访问的Api接口地址定义于/common/config.js文件中，修改其中的baseUrl的值即可。

App打包步骤(以下步骤均在使用HBulider X中进行)：

1.  配置manifest.json文件

(1) 打开项目根目录下的manifest.json文件，在基础配置中点击获取uni-app应用标识(仅首次需获取)，修改应用名称、版本名称、版本号等；

(2) 打开App图标配置，在自动生成图标一栏选择static文件夹下的logo.png，点击自动生成按钮即可自动配置不同分辨率的图标；

(3) 打开App启动界面配置，在Android启动界面设置一栏勾选自定义启动图，在下方Android启动图片设置中1080p高分屏启动图片浏览选择static文件夹下的splash.9.png；

(4) 打开App模块配置，打包模块配置中勾选Speech以及VideoPlayer两个模块，其中Speech中选择百度语音识别，配置在百度开放平台申请的AppId等内容；

(5) 打开App权限配置，可参照下方提供的权限列表进行勾选，或至源码视图修改；

2. App云打包

(1) 点击HBulider X中的发行-->原生App云打包；

(2) 在打开的页面中勾选Android(apk包)；

(3) 在Android设置选项卡中，勾选使用自有证书。在static文件夹下本项目提供了一个自有签名证书app_recycle.keystore文件，该证书别名：recycle，证书私钥密码：recycle。若仅测试使用可不使用自有证书，直接勾选使用公共测试证书即可；

(4) 以上步骤完成之后勾选打正式包，选择快速安心打包，点击打包按钮等待即可；

(5) 云打包完成之后会在HBulider X的控制台中显示打包好的apk文件地址链接可点击直接跳转，也可手动至unpackage/release/apk文件夹下找到；

 

Android打包权限配置列表：

`/* android打包配置 */`

`"android" : {`

  `"permissions" : [`

​     `"<uses-feature android:name=\"android.hardware.camera\"/>",`

​     `"<uses-feature android:name=\"android.hardware.camera.autofocus\"/>",`

​     `"<uses-permission android:name=\"android.permission.ACCESS_NETWORK_STATE\"/>",`

​     `"<uses-permission android:name=\"android.permission.ACCESS_WIFI_STATE\"/>",`

​     `"<uses-permission android:name=\"android.permission.CAMERA\"/>",`

​     `"<uses-permission android:name=\"android.permission.CHANGE_NETWORK_STATE\"/>",`

​     `"<uses-permission android:name=\"android.permission.CHANGE_WIFI_STATE\"/>",`

​     `"<uses-permission android:name=\"android.permission.FLASHLIGHT\"/>",`

​     `"<uses-permission android:name=\"android.permission.GET_ACCOUNTS\"/>",`

​     `"<uses-permission android:name=\"android.permission.INSTALL_PACKAGES\"/>",`

​     `"<uses-permission android:name=\"android.permission.INTERNET\"/>",`

​     `"<uses-permission android:name=\"android.permission.MOUNT_UNMOUNT_FILESYSTEMS\"/>",`

​     `"<uses-permission android:name=\"android.permission.READ_LOGS\"/>",`

​     `"<uses-permission android:name=\"android.permission.READ_PHONE_STATE\"/>",`

​     `"<uses-permission android:name=\"android.permission.REQUEST_INSTALL_PACKAGES\"/>",`

​     `"<uses-permission android:name=\"android.permission.VIBRATE\"/>",`

​     `"<uses-permission android:name=\"android.permission.WAKE_LOCK\"/>",`

​     `"<uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\"/>",`

​     `"<uses-permission android:name=\"android.permission.WRITE_SETTINGS\"/>"`

​      `]`

 `},`
 
 
#### 其他注意事项

由于小程序项目大小以及背景图等限制，项目中使用到的大图均不保存在本地，而是上传至后台，使用网络图片。网络图片地址配置在util/constant.js中
其中使用网络图片的有：
 
- 首页直播和证书两张背景图
- 考试成绩页面波浪背景图
- 登录页背景图 
- 课程等封面的默认图片 当封面为null时显示此图片
- 证书背景图


#### H5打包
> npm run serve

> npm run build

打包成功之后位置位于dist/build/h5

#### Git
[Git地址](https://gitee.com/yuki07/teach-cli.git)
> 使用Git导入项目，需要首先运行 npm install安装必要的node_modules（若npm安装失败  可以直接使用HBulidX 仅运行src文件夹即可）