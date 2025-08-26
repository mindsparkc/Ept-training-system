<template>
	<view class="container">

		<view class="wx" v-if="isShow">
			<image src="../../static/download/wx.png" class="wx-image" mode="aspectFit"></image>
		</view>


		<!--插画-->
		<view class="right-top-sign"></view>
		<view class="wrapper">
			<view class="left-top-sign">Teach</view>

			<view class="text-title">
				{{appName}}
				<view class="div-relative">
					<view class="text-slogan">
						{{appDes}}
					</view>
					<view class="div-absoult"></view>
				</view>
			</view>
			<view class="flex-col">
				<image style="width: 90%;border-radius: 20px;  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);"
					:src="downloadPng"
					mode="widthFix">
				</image>
				<text v-if="!hasApk" style="margin-top: 20px;font-size: 14px;color: #666666;">系统管理员暂未上传Apk</text>
				<view class="btn" @click="down('android')" v-if="apkDownloadUrl">
					<image src="../../static/download/and.png" mode="aspectFit"></image>
					<view class="text">
						<view>Android版</view>
						<view>立即下载</view>
					</view>
				</view>
				<view class="btn" v-if="isInternet">
					<image src="../../static/download/and.png" mode="aspectFit"></image>
					<view class="text">
						<view>Android公网版下载</view>
						<view>适用于外部互联网使用</view>
					</view>
				</view>
			</view>

		</view>


		<text class="text-copyright">Copyright © {{(new Date).getFullYear()}} Sino Soft Incorporated. All rights
			reserved. </text>
	</view>
</template>
<script>
	export default {

		data() {
			return {
				isInternet: false, //是否外网
				appName: '专家培训平台', //app名称
				appDes: '每个企业都是一所大学', //app简述
				apkDownloadUrl: '', //安卓app下载地址
				// iosHref: '', //苹果appStore链接
				weixin: false, //是否为微信内浏览器打开
				scheme: '', //scheme地址 如果已安装app直接打开，置空则不执行
				isShow: false,
				hasApk:false,
				downloadPng:this.$constant.DOWNLOAD,
			};
		},
		onLoad() {
			// 动态设置页面标题
			this.setTitle()
			// 判断该页面是否为微信内置浏览器内打开 true>显示浏览器内打开引导
			this.isWX()
			// 如果需要通过scheme直接打开app
			this.openApp()
			this.getApkDownloadUrl()
		},
		methods: {
			//获取最新的apk
			getApkDownloadUrl() {
				uni.showLoading({
					title: '请稍候...',
					mask: false
				});
				let appCode = 'com.sinosoft.teach';
				let platform = ''
				console.log("当前系统名称", uni.getSystemInfoSync().osName);
				if (uni.getSystemInfoSync().osName == "ios") {
					platform = '1102'
				} else {
					platform = '1101'
				}
				uni.$u.api.updateVersion(appCode, platform).then(res => {
					uni.hideLoading()

					if (res && res.data && res.data.download) {
						this.apkDownloadUrl = res.data.download
						this.hasApk = true
					} else {
						this.hasApk = false
						uni.showToast({
							title: '管理端暂未上传Apk',
							icon: 'none'
						});
					}
				});
			},

			setTitle() {
				uni.setNavigationBarTitle({
					title: this.appName + '下载'
				});
			},
			//是否是在微信环境
			isWX() {
				// #ifndef MP-WEIXIN
				let ua = navigator.userAgent.toLowerCase();
				if (ua.match(/MicroMessenger/i) == "micromessenger") {
					this.weixin = true
				} else {
					this.weixin = false
				}
				// #endif

			},
			openApp() {
				if (this.scheme) {
					window.location = this.scheme
				}
			},
			down(type) {
				if (this.weixin) {
					uni.showModal({
						content: '请点击右上角按钮\n选择在浏览器打开',
						showCancel: false,

					});

					this.isShow = true;
				} else {
					if (type == 'android') {

						// #ifdef H5
						window.location.href = this.apkDownloadUrl;
						// #endif

						// #ifdef MP-WEIXIN

						this.download(this.apkDownloadUrl)

						// #endif

					} else {
						window.open(this.apkDownloadUrl);
					}
				}

			},
			download(openUrl) {
				uni.showLoading({
					title: '正在下载...',
					mask: false
				});
				uni.downloadFile({
					url: openUrl,
					success: function(res) {
						uni.hideLoading();
						console.log(res);
						var filePath = res.tempFilePath;

						uni.openDocument({
							filePath: filePath,
							showMenu: true,
							success: function(res) {}
						});
					}
				});
			},
		}
	}
</script>
<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.container {
		padding-top: 80px;
		position: relative;
		width: 100vw;
		overflow: hidden;


		.wrapper {
			position: relative;
			z-index: 90;
			background: #fff;
			padding-bottom: 40upx;
		}



		.left-top-sign {
			font-size: 120upx;
			color: #f8f8f8;
			position: relative;
			left: -16upx;
		}

		.right-top-sign {
			position: absolute;
			top: 80upx;
			right: -30upx;
			z-index: 95;

			&:before,
			&:after {
				display: block;
				content: '';
				width: 400upx;
				height: 80upx;
				background: #3BB1E3;
			}

			&:before {
				transform: rotate(50deg);
				border-radius: 0 50px 0 0;
			}

			&:after {
				position: absolute;
				right: -198upx;
				top: 0;
				transform: rotate(-50deg);
				border-radius: 50px 0 0 0;
			}
		}



		.text-title {
			position: relative;
			left: 50upx;
			top: -80upx;
			font-weight: bold;
			font-size: 46upx;
			color: #555;
			text-shadow: 1px 0px 1px rgba(0, 0, 0, 0.3);
		}

		.text-slogan {
			font-size: 32rpx;
			margin-top: 10rpx;
			font-weight: normal;
			color: #999;
			text-shadow: 1px 0px 1px rgba(0, 0, 0, 0.3);
		}

		.flex-col {
			display: flex;
			justify-content: center;
			align-items: center;
			flex-direction: column;

			image {
				width: 250px;
				height: 250px;
			}
		}

		.btn {
			display: flex;
			flex-direction: row;
			align-items: center;
			background-color: #037EFF;
			border-radius: 20rpx;
			color: #FFFFFF;
			padding: 20rpx 30rpx;
			margin-top: 60rpx;
			background: linear-gradient(to bottom, #3BB1E3 0%, #037EFF 100%);
			border-bottom: 4px solid #2076c6;
			text-shadow: 1px 1px 1px rgba(0, 0, 0, .4);
			box-shadow: 0px 3px 0px 0px rgba(0, 0, 0, .1);

			image {
				width: 60rpx;
				height: 60rpx;

			}

			.text {
				margin-left: 20rpx;
				font-weight: bold;

				view:nth-child(1) {

					font-size: 34upx;
				}

				view:nth-child(2) {
					font-size: 28upx;
				}
			}
		}

		.btn:active {
			box-shadow: 0px 2px 0px 0px rgba(0, 0, 0, .2);
			opacity: 0.9;
			transform: translate(1rpx, 1rpx);
		}


		.text-copyright {
			color: #bebebe;
			font-size: 24rpx;
			left: 0;
			position: fixed;
			bottom: 0;
			text-align: center;
			width: 100%;
			margin-bottom: 30rpx;

		}


		.wx {
			position: fixed;
			left: 0;
			top: 0;
			background-color: rgba(0, 0, 0, .7);
			width: 100vw;
			height: 100vh;
			z-index: 99;

		}

		.wx-image {
			position: absolute;
			right: 0;
			top: 0;
			margin-right: 20rpx;
		}

		.logo-image {
			width: 150rpx;
			margin-right: 20rpx;
		}

		.div-absoult {
			background: linear-gradient(90deg, #31beff 0%, rgba(255, 175, 0, 0) 100%);
			border-radius: 20px;
			height: 5px;
			width: 150px;
			position: absolute;
			top: 18px;
			z-index: -10;
		}

		.div-relative {
			position: relative;

		}
	}
</style>
