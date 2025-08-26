// app版本升级接口方法
export default {
    getServerNo: (version, isPrompt = false, callback) => {
        // let httpData = {
        //     version: version.versionCode,
        //     // 版本名称
        //     versionName: version.versionName,
        //     // setupPage参数说明（判断用户是不是从设置页面点击的更新，如果是设置页面点击的更新，有不要用静默更新了，不然用户点击没反应很奇怪的）
        //     setupPage: isPrompt   
        // };
        // if (platform == "android") {
        //     httpData.type = 1101;
        // } else {
        //     httpData.type = 1102;
        // }
        /* 接口入参说明
         * version: 应用当前版本号（已自动获取）
         * versionName: 应用当前版本名称（已自动获取）
         * platform：平台（1101是安卓，1102是IOS）
		 * appCode:应用包名
         */
        let appCode = 'com.sinosoft.teach';
        let platform = ''
        if (uni.getSystemInfoSync().platform == "android") {
        	platform = '1101'
        } else {
        	platform = '1102'
        }
       uni.$u.api.updateVersion(appCode, platform).then(res => {
		 
		   // console.log("updateVersion",JSON.stringify(res));
            /* res的数据说明
             * | 参数名称         | 一定返回     | 类型        | 描述
             * | -------------|--------- | --------- | ------------- |
             * | versionCode     | y        | int        | 版本号        |
             * | versionName     | y        | String    | 版本名称      |
             * | versionDescribe     | y        | String    | 版本信息      |
             * | updateType         | y        | String    | forcibly = 强制更新, solicit = 弹窗确认更新, silent = 静默更新   暂不考虑此类情况|
             * | download     | y        | String    | 版本下载链接（IOS安装包更新请放跳转store应用商店链接,安卓apk和wgt文件放文件下载链接）  |
             */
            if (res &&res.data&& res.data.download) {
				callback && callback(res.data);
                // if(res.updateType){
                //     callback && callback(res);
                // } else {
                //     if(res.forceUpdate){
                //         res.updateType = "forcibly";
                //     } else {
                //         res.updateType = "solicit";
                //     }
                //     callback && callback(res);
                // }
            } else if (isPrompt) {//设置页面弹窗提示
                uni.showToast({
                    title: "当前已为最新版本",
                    icon: "none"
                });
            }
        });
    },
    // 弹窗主颜色
    appUpdateColor: "0D3CD4",
    // 弹窗图标（不填显示默认图标，链接配置示例如： '/static/demo/ic_attention.png'）
    appUpdateIcon: '/uni_modules/zhouWei-APPUpdate/static/ic_ar.png'
}