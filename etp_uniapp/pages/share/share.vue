<template>
	<view>

		<image src="../../static/share.png" mode="widthFix" class="img"></image>
		<view @click="toWx" hover-class="u-hover-class" class="btn">
			<text style="font-size: 40rpx; margin-right: 5px;font-weight: bold;">￥9.9</text><text>立即购买</text>

		</view>
	</view>


</template>

<script>
	export default {
		data() {
			return {
				URLScheme: "", //微信地址weixin://dl/business/?t=KH54tXs9Wkt
			}
		},
		onLoad() {
			this.getURLScheme()
		},
		methods: {
			toWx() {
				if (this.URLScheme) {

					// #ifdef H5
					window.location.href = this.URLScheme;
					// #endif

					// #ifdef MP-WEIXIN
					uni.navigateTo({
						url: '/pages/vip/joinVip/joinVip',
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
					// #endif
				} else {
					uni.showToast({
						title: '未获取到小程序链接',
						icon: 'none'
					});
				}
			},

			//获取微信地址	
			getURLScheme() {
				uni.request({
					// url: '/api/wx/url-scheme',
					url: 'https://api.shundesoft.com:4430/wenku/api/wx/url-scheme',
					success: (res) => {
						this.URLScheme = res.data.urlScheme
					}
				});



			},
		}
	}
</script>

<style>
	page {
		background-color: #9acadd;
	}

	.img {
		width: 100%;
		height: 500px;
		margin-bottom: 0px;
		vertical-align: middle;
	}

	.btn {
		background-color: #335d72;
		color: white;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
		position: fixed;
		right: 0;
		bottom: 0;
		left: 0;
		padding: 20rpx;
		font-size: 30rpx;
		text-align: center;
	}
</style>
