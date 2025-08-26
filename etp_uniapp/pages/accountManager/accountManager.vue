<template>
	<view>

		<view class="flex-row margin-top-10" @click="uploadAvatarImg">
			<text class="text-title">头像</text>
			<u-avatar :src="fullAvatar(userInfo.avatar)"  shape="circle" size="54"></u-avatar>
		</view>
		<view class="flex-row">
			<text class="text-title">昵称</text>
			<text class="text-title">{{userInfo.nickName?userInfo.nickName:"匿名"}}</text>
		</view>
		<!-- #ifndef MP-WEIXIN -->
		<view class="flex-row" v-if="false">
			<text class="text-title">电话</text>
			<text class="text-title">{{userInfo.phonenumber?userInfo.phonenumber:"暂无"}}</text>
		</view>
		<view class="flex-row" v-if="false">
			<text class="text-title">邮箱</text>
			<text class="text-title">{{userInfo.email?userInfo.email:"暂无"}}</text>
		</view>
		<view class="flex-row" v-if="false">
			<text class="text-title">性别</text>
			<text class="text-title">{{tranSex( userInfo.sex)}}</text>
		</view>
		<!-- #endif -->
		<!-- <view class="flex-row">
			<text class="text-title">年龄</text>
			<text class="text-title">{{userInfo.age}}岁</text>
		</view> -->
		<view class="flex-row margin-top-10" hover-class="u-hover-class" @click="changePassword()">
			<text class="text-title">修改密码</text>
			<u-icon name="arrow-right" color="#B8B8B8" size="24"></u-icon>

		</view>

		<button class="btn-normal" @click="logout()">退出登录</button>
	</view>
</template>

<script>
	// #ifndef H5
	import config from '../../common/config.js'
	// #endif

	export default {
		data() {
			return {
				userInfo: {
					avatar: "",
					nickName: "",
					phone: "",
					email: "",
					sex: "",
					age: "",

				},
				avatar_file: '',
				token: ''
			};
		},
		onLoad() {
			this.userInfo = uni.getStorageSync('userInfo');
			this.token = uni.getStorageSync("token");
		},
		methods: {
			tranSex(sex) {
				switch (sex) {
					case "0":
						return "男";
					case "1":
						return "女";
					default:
						break;
				}
			},

			fullAvatar(avatar) {
				return config.baseUrl + avatar;
			},
			changePassword() {
				uni.navigateTo({
					url: '/pages/changePassword/changePassword',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},

			//退出登录
			logout() {
				uni.showModal({
					title: '提示',
					content: '退出登录将无法体验完整功能 是否继续退出',
					showCancel: true,
					cancelText: '取消',
					confirmText: '确定',
					success: res => {
						if (res.confirm) {

							//清除token缓存
							uni.removeStorageSync("token");

							uni.removeStorageSync("userInfo");
							//关闭所有页面 打开登录页面
							uni.reLaunch({
								url: '/pages/login/login'
							})

						}
					},
					fail: () => {},
					complete: () => {}
				});
			},
			uploadAvatarImg(res) {
				const crop = {
					quality: 100,
					width: 600,
					height: 600,
					resize: true
				};
				uni.chooseImage({
					count: 1,
					crop,
					success: async (res) => {
						// console.log(res);
						let tempFile = res.tempFiles[0],
							avatar_file = {
								// #ifdef H5
								extname: tempFile.name.split('.')[tempFile.name.split('.').length - 1],
								// #endif
								// #ifndef H5
								extname: tempFile.path.split('.')[tempFile.path.split('.').length - 1]
								// #endif
							},
							filePath = res.tempFilePaths[0];
						// #ifndef APP-PLUS
						//非app端用前端组件剪裁头像，app端用内置的原生裁剪
						filePath = await new Promise((callback) => {
							uni.navigateTo({
								url: '/pages/accountManager/cropImage?path=' + filePath +
									`&options=${JSON.stringify(crop)}`,
								animationType: "fade-in",
								events: {
									success: url => {
										callback(url)
									}
								}
							});
						})
						// #endif

						let cloudPath = this.userInfo.nickName + '' + Date.now(); //命名
						avatar_file.name = cloudPath;
						console.log(">>>>>>>>>>>", avatar_file);
						uni.showLoading({
							title: '正在设置...',
							mask: true
						});


						//上传图片
						const result = await this.uploadFilePromise(filePath);
						let resp = JSON.parse(result)
						console.log(resp)
						avatar_file.url = resp.fileName
						console.log({
							avatar_file
						});


						//回显头像
						this.setAvatarFile(avatar_file)
					}
				})
			},
			//设置头像
			setAvatarFile(avatar_file) {
				//重新请求用户接口  更新头像
				uni.$u.api.getUserInfo().then(res => {
					uni.hideLoading();
					let userInfo = res.data;
					if (userInfo) {
						this.userInfo = userInfo;
						uni.showToast({
							title: '头像设置成功',
							icon: 'none'
						});
						uni.setStorage({
							key: 'userInfo',
							data: userInfo
						})

					}
				}).catch(err => {
					uni.hideLoading()
					console.log(err.errMsg);
				})


			},

			//上传图片
			uploadFilePromise(url) {
				let uploadUrl = config.baseUrl + '/system/user/profile/avatar'
				return new Promise((resolve, reject) => {
					let a = uni.uploadFile({
						url: uploadUrl,
						header: {
							Authorization: this.token,
							// #ifdef APP-PLUS
							'x-platform': 'MH',
							// #endif
							// #ifdef MP-WEIXIN
							'x-platform': 'WX',
							// #endif
							// #ifdef H5
							'x-platform': 'H5',
							// #endif
						},
						filePath: url,
						name: 'avatarfile',
						formData: {},
						success: (res) => {
							setTimeout(() => {
								resolve(res.data)
							}, 1000)
						}
					});
				})
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #f8f8f8;
	}

	.flex-row {
		background-color: #FFFFFF;
		padding: 30rpx;
		margin-top: 2rpx;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;

	}

	.text-title {
		color: #303133;
		font-size: 32rpx;
	}

	.margin-top-10 {
		margin-top: 20rpx;
	}

	.btn-normal {

		margin: 40rpx 30rpx;
		text-align: center;

		color: #fff;
		border: 0;
		border-radius: 5px;

		font-size: 32rpx;
		box-shadow: 0px 5px 25px 0px rgba(22, 99, 255, 0.3);
		background: #037EFF;
	}



	.btn-normal:after {
		border: 0;
	}

	/*按钮点击效果*/
	.btn-normal.button-hover {
		transform: translate(1rpx, 1rpx);
		background: #4b6cb7;
	}
</style>
