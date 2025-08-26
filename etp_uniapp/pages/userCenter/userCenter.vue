<template>
	<view>
		<view class="header" :class="{'status':isH5Plus}">
			<view class="userinfo" @click="toUserAccountManager()">
				<view class="face">
					<u-avatar :src="fullAvatar(userinfo.avatar)" shape="circle" size="54">
					</u-avatar>
					

				</view>
				<view class="info">
					<view class="username">{{userinfo.nickName?userinfo.nickName:"未登录"}}</view>
					<view class="integral">{{userinfo.dept.deptName?userinfo.dept.deptName:"暂未设置所属单位"}}</view>
				</view>
			</view>
			<view class="setting" v-if="false">
				<image src="../../static/usercenter/icon-user-accont-setting.png"></image>
			</view>
		</view>
		<view class="orders">
			<view class="box">
				<view class="label" v-for="(row,index) in gridList" :key="row.name" hover-class="hover"
					@tap="toGrid(index)">
					<view class="icon">
						<!-- <view class="badge" v-if="row.badge>0">{{row.badge<99?row.badge:'99+'}}</view> -->
						<image :src="'../../static/usercenter/'+row.icon" class="image"></image>
					</view>
					<text class="text-grey">{{row.name}}</text>

				</view>
			</view>
		</view>

		<!-- <view class="list" v-for="(list,list_i) in severList" :key="list_i">
			<view class="li" v-for="(li,li_i) in list" @tap="toPage(list_i,li_i)"
				:class="{'noborder':li_i==list.length-1}" hover-class="hover" :key="li.name">
				<view class="icon">
					<image :src="'../../static/usercenter/'+li.icon"></image>
				</view>
				<view class="text">{{li.name}}</view>
			
				<image src="../../static/usercenter/icon-arrow-right-bold.png" class="img-arrow"></image>

			</view>
		</view> -->
		<view class="cell" v-for="(list,list_i) in severList" :key="list_i">
			<u-cell-group :border="false">
				<u-cell :icon="li.icon" :title="li.name" :name="li.name" v-for="(li,li_i) in list" :key="li.name"
					:url="li.to" isLink :iconStyle="iconStyle" :border="li_i!=list.length-1" @click='onCellClick'>
				</u-cell>
			</u-cell-group>
		</view>

		<!-- <button @click="toExamTest" v-if="true">新版考试题测试页面</button> -->
		<float-ask></float-ask>
	</view>
</template>
<script>
	// #ifndef H5
	import config from '../../common/config.js'
	// #endif
	// #ifdef APP-PLUS
	import APPUpdate, {
		getCurrentNo
	} from "@/uni_modules/zhouWei-APPUpdate/js_sdk/appUpdate";

	// #endif
	export default {
		data() {
			return {
				//#ifdef APP-PLUS
				isH5Plus: true,
				//#endif
				//#ifndef APP-PLUS
				isH5Plus: false,
				//#endif
				userinfo: {
					avatar: ""
				},
				versionName: '', //版本名称
				versionCode: 0, //版本号
				gridList: [
					//name-标题 icon-图标 badge-角标
					{
						name: '我的考试',
						icon: 'icon-user-exam.png',
						badge: 0,
						to: '/pages/exam/exam'
					},
					{
						name: '我的任务',
						icon: 'icon-user-task.png',
						badge: 0,
						to: '/pages/taskList/taskList'
					},
					{
						name: '我的课程',
						icon: 'icon-user-lesson.png',
						badge: 0,
						to: '/pages/lessonList/lessonList'
					},
					{
						name: '我的证书',
						icon: 'icon-user-apply.png',
						badge: 0,
						to: '/pages/certList/certList'
					},
				],
				//#ifdef APP-PLUS
				severList: [
					[{
							name: '我的错题',
							icon: 'pushpin',
							to: '/pages/errorBook/errorBook'
						},
						{
							name: '账号管理',
							icon: 'setting',
							to: '/pages/accountManager/accountManager'
						},
						{
							name: '修改密码',
							icon: 'lock-open',
							to: '/pages/changePassword/changePassword'
						}


					],
					[{
							name: '意见反馈',
							icon: 'edit-pen',
							to: '/pages/common/feedbackIndex/feedbackIndex'
						},
						{
							name: '版本更新',
							icon: 'download',
							to: ''
						},
						{
							name: '关于我们',
							icon: 'info-circle',
							to: '/pages/aboutUs/aboutUs'
						},
						{
							name: '隐私政策',
							icon: 'tags',
							to: '/pages/common/privacyPolicy/privacyPolicy'
						}
					]
				],
				//#endif
				//#ifdef H5
				severList: [
					[{
							name: '我的错题',
							icon: 'pushpin',
							to: '/pages/errorBook/errorBook'
						},
						{
							name: '账号管理',
							icon: 'setting',
							to: '/pages/accountManager/accountManager'
						},
						{
							name: '修改密码',
							icon: 'lock-open',
							to: '/pages/changePassword/changePassword'
						}

					],
					[{
							name: '意见反馈',
							icon: 'edit-pen',
							to: '/pages/common/feedbackIndex/feedbackIndex'
						},
						{
							name: '关于我们',
							icon: 'info-circle',
							to: '/pages/aboutUs/aboutUs'
						},
						{
							name: '下载App',
							icon: 'download',
							to: '/pages/download/download'
						},
						{
							name: '隐私政策',
							icon: 'tags',
							to: '/pages/common/privacyPolicy/privacyPolicy'
						}
					],

				],
				//#endif
				//#ifdef MP-WEIXIN
				severList: [
					[{
							name: '我的错题',
							icon: 'pushpin',
							to: '/pages/errorBook/errorBook'
						},
						{
							name: '账号管理',
							icon: 'setting',
							to: '/pages/accountManager/accountManager'
						},
						{
							name: '修改密码',
							icon: 'lock-open',
							to: '/pages/changePassword/changePassword'
						}

					],
					[{
							name: '意见反馈',
							icon: 'edit-pen',
							to: '/pages/common/feedbackIndex/feedbackIndex'
						},

						{
							name: '关于我们',
							icon: 'info-circle',
							to: '/pages/aboutUs/aboutUs'
						},
						{
							name: '下载App',
							icon: 'download',
							to: '/pages/download/download'
						},
						{
							name: '隐私政策',
							icon: 'tags',
							to: '/pages/common/privacyPolicy/privacyPolicy'
						}
					]
				],
				//#endif
				iconStyle: {
					"color": "#303133",
					"margin-right": "15rpx",
				}
			};
		},
		onLoad() {
			//加载
			this.init();
		},
		onShow() {
			this.init();
		},
		methods: {
			toExamTest() {
				uni.navigateTo({
					url: '/pages/testQuestion/testQuestion',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			init() {
				//用户信息
				this.userinfo = uni.getStorageSync("userInfo");

				//app获取版本号
				// #ifdef APP-PLUS
				getCurrentNo(res => {
					this.versionName = res.versionName;
					this.versionCode = res.versionCode;
					// this.onAPPUpdate(false) 
				});
				// #endif

			},
			fullAvatar(avatar) {
				return config.baseUrl + avatar;
			},

			toUserAccountManager() {
				//判断用户是否登录
				if (this.userinfo) {
					uni.navigateTo({
						url: '/pages/accountManager/accountManager',
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {
					uni.redirectTo({
						url: '/pages/login/login',
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				}

			},

			//用户点击上方按钮
			toGrid(index) {
				if (this.gridList[index].to) {
					uni.navigateTo({
						url: this.gridList[index].to,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {

					uni.showToast({
						title: this.gridList[index].name
					});
				}

			},

			onCellClick(e) {
				if (e.name == "版本更新") {
					this.onAPPUpdate(true);
					// #ifdef APP

					// #endif
				}
			},
			// 检查APP是否有新版本
			onAPPUpdate(isPrompt) {
				APPUpdate(isPrompt);

				// 	let that = this;

				// 	let appCode = 'com.sinosoft.teach';
				// 	let platform = ''
				// 	if (uni.getSystemInfoSync().platform == "android") {
				// 		platform = '1101'
				// 	} else {
				// 		platform = '1102'
				// 	}

				// 	uni.$u.api.updateVersion(appCode, platform)
				// 		.then(res => {
				// 			console.log("版本", JSON.stringify(res));

				// 			var result = res.data;
				// 			if (result) {
				// 				var lastestVersionCode = result.versionCode;
				// 				//判断本地版本号与最新版本号大小
				// 				if (that.versionCode >= lastestVersionCode) {
				// 					if (isPrompt) {
				// 						uni.showToast({
				// 							title: '当前已是最新版本',
				// 							icon: 'none'
				// 						});
				// 					}

				// 				} else {
				// 					var tRes = {
				// 						versionCode: result.versionCode,
				// 						versionName: result.versionName,
				// 						versionInfo: result.versionDescribe,
				// 						forceUpdate: false, //是否强制更新
				// 						downloadUrl: result.download,
				// 					};
				// 					if (tRes && tRes.downloadUrl) {
				// 						APPUpdate(true, tRes);
				// 					}
				// 				}
				// 			} else {
				// 				uni.showToast({
				// 					title: '当前已是最新版本',
				// 					icon: 'none'
				// 				});
				// 			}




				// 		}).catch(res => {
				// 			console.log(JSON.stringify(res));
				// 			uni.showToast({
				// 				title: res.message ? res.message : "请求失败",
				// 				icon: 'none'
				// 			});
				// 		});



			},

			//用户点击列表项
			toPage(list_i, li_i) {
				if (this.severList[list_i][li_i].to) {

					uni.navigateTo({
						url: this.severList[list_i][li_i].to,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {


					uni.showToast({
						title: this.severList[list_i][li_i].name
					});
				}


			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #fff
	}

	.header {
		&.status {
			padding-top: var(--status-bar-height);
		}

		background:linear-gradient(180deg, #037EFF, #49A2FF);
		width:92%;
		height:45vw;
		padding:0 4%;
		display:flex;
		align-items:center;

		.userinfo {
			width: 90%;
			display: flex;
			margin-top: 70rpx;

			.face {
				flex-shrink: 0;
				width: 15vw;
				height: 15vw;

				image {
					width: 100%;
					height: 100%;
					border-radius: 100%
				}
			}

			.info {
				display: flex;
				flex-flow: wrap;
				padding-left: 30rpx;

				.username {
					width: 100%;
					color: #fff;
					font-size: 40rpx
				}

				.integral {
					display: flex;
					align-items: center;
					padding: 0 20rpx;
					height: 40rpx;
					color: #fff;
					background-color: rgba(0, 0, 0, 0.1);
					border-radius: 20rpx;
					font-size: 24rpx
				}
			}
		}

		.setting {
			flex-shrink: 0;
			width: 6vw;
			height: 6vw;

			image {
				width: 100%;
				height: 100%
			}
		}
	}

	.img-arrow {
		width: 32rpx;
		height: 32rpx;
	}

	.hover {
		background-color: #eee
	}

	.orders {
		background-color: #49A2FF;
		width: 92%;
		height: 11vw;
		padding: 0 4%;
		margin-bottom: calc(11vw + 80rpx);
		display: flex;
		align-items: flex-start;
		border-radius: 0 0 100% 100%;
		margin-top: -1rpx;

		.box {
			width: 98%;
			padding: 30rpx 1%;
			// height: 22vw;
			background-color: #FFFFFF;
			border-radius: 24rpx;
			box-shadow: 0 0 20rpx rgba(0, 0, 0, 0.1);
			margin-bottom: 40rpx;
			display: flex;
			align-items: center;
			flex-direction: row;
			justify-content: center;

			.label {
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: space-between;
				align-content: center;
				width: 100%;
				color: #303133;
				font-size: 26rpx;

				.icon {
					position: relative;

					.badge {
						position: absolute;
						// width: 4vw;
						min-width: 4vw;
						height: 4vw;
						background-color: #ec6d2c;
						top: -1vw;
						right: -4vw;
						border-radius: 50%;
						font-size: 20rpx;
						color: #fff;
						display: flex;
						align-items: center;
						justify-content: center;
						z-index: 10;
					}

					.image {
						width: 80rpx;
						height: 80rpx;

						z-index: 9;
					}
				}
			}
		}
	}

	.cell {
		margin-left: 30rpx;
		margin-right: 30rpx;
		border-radius: 24rpx;
		box-shadow: 0 0 20rpx rgba(0, 0, 0, 0.1);
		margin-bottom: 30rpx;

	}

	.text-grey {
		color: #303133;
		font-size: $uni-font-size-base;
		margin-top: 10rpx;
	}

	.list {
		margin-left: 30rpx;
		margin-right: 30rpx;
		border-radius: 10rpx;
		box-shadow: 0 0 20rpx rgba(0, 0, 0, 0.15);
		margin-bottom: 30rpx;

		.li {
			width: 92%;
			height: 100rpx;
			padding: 0 4%;
			border-bottom: solid 1rpx #e7e7e7;
			display: flex;
			align-items: center;

			&.noborder {
				border-bottom: 0
			}

			.icon {
				flex-shrink: 0;
				width: 40rpx;
				height: 40rpx;

				image {
					width: 40rpx;
					height: 40rpx
				}
			}

			.text {
				padding-left: 20rpx;
				width: 100%;
				color: #303133;
				font-size: 28rpx;
			}

			.to {
				flex-shrink: 0;
				width: 40rpx;
				height: 40rpx
			}
		}
	}
</style>
