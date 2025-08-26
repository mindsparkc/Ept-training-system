<template>
	<view class="colomn">
		<image src="../../static/logo.png" mode='aspectFit' class="login-logo" style="width: 180rpx;
		height: 180rpx;
		margin-top: 100rpx;"></image>
		<!-- #ifdef APP-PLUS -->
		<text style="margin-top: 40rpx; font-size: 28rpx; color: #666666; font-weight: bold;">{{appName}} v{{versionName}}</text>
		<!-- #endif -->
		
		<text style="margin-top: 20rpx; font-size: 24rpx; color: #666666; ">{{name}}</text>

		<view class="bg-phone" hover-class="u-hover-class" @click="callPhone()" v-if="false">

			<text>联系电话：{{phone}}</text>
		</view>
		
		<!-- <navigator url="/pages/test/test">test</navigator> -->
		
		<text class="text-copyright">Copyright © {{(new Date).getFullYear()}} Sino Soft Incorporated. All rights
			reserved. </text>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				appName: '',
				// name: '中科软科技股份有限公司提供技术支持',
				name: '物畅网提供技术支持',
				versionName: '',
				wh: '',
				phone: '0531-1000000',

				error: '',

			}
		},

		onLoad() {

			uni.getSystemInfo({
				success: (res) => {
					this.wh = res.windowHeight;
					this.appName = res.appName;
					this.versionName = res.appVersion;
				}
			})
			
			
			
		},
		methods: {
			
			
			playError(e) {
				this.error = e;
				console.log(JSON.stringify(e));
			},

			showData() {
				uni.onWindowResize(function(res) {
					if (res.size.windowWidth < this.wh) {
						res.size.windowWidth = this.wh;
					}
				})
			},
			callPhone() {
				uni.makePhoneCall({
					phoneNumber: this.phone //仅为示例
				});
			}
		}
	}
</script>

<style>
	page {
		background: #f8f8f8;
	}

	.colomn {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.bg-phone {
		background-color: #FFFFFF;
		width: 100%;
		padding: 20rpx;
		text-align: center;
		margin-top: 100rpx;
		color: #303133;
		font-size: 28rpx;
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
</style>
