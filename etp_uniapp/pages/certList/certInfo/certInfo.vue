<template>
	<view>
		<view class="bg-white">
			<view class="text-title">{{certName}}</view>
			<view class="text-content">{{certDes&&certDes!='null'?certDes:'暂无描述'}}</view>
			<view class="text-content">证书编号：{{certId}}</view>
			<view class="text-content">颁发时间：{{createTime}}</view>
		</view>
			<movable-area scale-area>
				<movable-view direction="all" scale scale-min="1" scale-max="3">
					<image :src="imageUrl" mode="widthFix"></image>
				</movable-view>
			</movable-area>
		<!-- <button class="btn" @click="downloadPic()">下载证书</button> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imageUrl: "",
				certName: "",
				createTime: "",
				certDes: "",
				certId: ""
			}
		},

		onLoad(e) {

			if (e) {
				this.imageUrl = e.previewFile;
				this.certName = e.certName;
				this.createTime = e.createTime;
				this.certDes = e.certDes;
				this.certId = e.certId
			}

		},
		methods: {

			//下载证书
			downloadPic() {
				this.saveImageToPhotosAlbum()

				// uni.getSetting({ //获取用户配置
				// 		success(res) {
				// 			console.log(res.authSetting)
				// 			if (res.authSetting['scope.writePhotosAlbum']) { //验证用户是否授权可以访问相册
				// 				this.saveImageToPhotosAlbum()
				// 			} else {
				// 				uni.authorize({ //重新发起获取授权
				// 					scope: 'scope.writePhotosAlbum',
				// 					success: () => {
				// 						this.saveImageToPhotosAlbum()
				// 					},
				// 					fail: () => {
				// 						uni.showToast({
				// 							title: '请打开保存相册权限，再点击下载证书',
				// 							icon: 'none',
				// 							duration: 2000
				// 						})
				// 						let timer = setTimeout(() => {
				// 							clearTimeout(timer)
				// 							uni.openSetting({ //调起客户端小程序设置界面,让用户开启访问相册
				// 								success: e => {}
				// 							})
				// 						}, 2000)
				// 					}
				// 				})
				// 			}
				// 		}
				// })
			},

			saveImageToPhotosAlbum() {
				uni.showLoading({
					title: '正在下载...',
					mask: false
				});
				uni.downloadFile({
					url: this.imageUrl,
					success: res => {
						uni.hideLoading()
						if (res.statusCode === 200) {
							uni.saveImageToPhotosAlbum({
								filePath: res.tempFilePath,
								success(res) {
									uni.showToast({
										title: '证书已保存到手机相册',
										icon: "none"
									})
								},
								fail(res) {
									uni.showToast({
										title: '证书下载失败',
										icon: "none"
									})
								}
							})
						} else {
							uni.showToast({
								title: '证书下载失败',
								icon: "none"
							})
						}
					},
					fail: err => {
						uni.hideLoading()
						console.log(err, 'err')
						uni.showToast({
							title: '证书下载失败',
							icon: "none"
						})
					}
				})
			}

		}
	}
</script>

<style>
	movable-view {
		display: flex;
		justify-content: center;
		width: 100%;
		height: 100%;
	}

	movable-area {
		height: 100%;
		width: 100%;
		position: fixed;
		overflow: hidden;
	}

	movable-view image {
		width: 100%;
		margin: 15px;
	}

	.bg-white {
		padding: 15px;
		margin: 15px;
		background: #FFFFFF;
		box-shadow: 0px 2px 5px 0px rgba(59, 59, 59, 0.15);
		border-radius: 3px;
	}

	.text-title {
		font-size: 18px;
		font-weight: bold;
		color: #303133;
		margin-bottom: 10px;
	}

	.text-content {
		font-size: 14px;
		color: #303133;
	}

	.img {
		box-shadow: 0px 2px 5px 0px rgba(59, 59, 59, 0.15);
		border-radius: 3px;
		width: 100%;
	}

	.btn {
		color: #fff;
		border: 0;
		border-radius: 50rpx;
		font-size: 34rpx;
		box-shadow: 0px 2px 5px 0px rgba(22, 99, 255, 0.1);
		margin-top: 60px;
		margin: 0 auto;
		width: 300px;
		background: -webkit-linear-gradient(to left, #007DFF, #5EC4F9);
		background: linear-gradient(to left, #007DFF, #5EC4F9);

	}

	.btn:after {
		border: 0;
	}

	/*按钮点击效果*/
	.btn.button-hover {
		// transform: translate(1rpx, 1rpx);
		background: -webkit-linear-gradient(to right, #007DFF, #5EC4F9);
		background: linear-gradient(to right, #007DFF, #5EC4F9);
	}
</style>
