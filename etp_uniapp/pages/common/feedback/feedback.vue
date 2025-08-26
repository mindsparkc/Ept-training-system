<template>
	<view class="page">
		<view v-if='isFeedback' class="bg-success">
			<text>反馈成功</text>
			<text>感谢您对我们的关注与支持，我们会认真处理您的反馈，尽快修复和完善相关功能。</text>


		</view>

		<view class="feedback-title">
			<text class="text-black">问题反馈和意见建议
				<text style="color: red;">*</text></text>

			<view @tap="chooseMsg" hover-class="u-hover-class">
				<text class="feedback-quick">一键吐槽</text>
				<uni-icons type="bottom" color="#606266"></uni-icons>
			</view>

		</view>
		<view class="feedback-body">
			<textarea placeholder="请详细描述您使用过程中遇到的问题或者您的意见建议..." v-model="sendData.yjms" class="feedback-textare" />
		</view>
		<view class="feedback-title">
			<text class="text-black">图片(选填,提供问题截图,大小不超过5M)</text>
		</view>
		<view class="feedback-body ">
			<view>
				<view class="image-title">
					<view class="text-grey">点击上传图片</view>
					<view class="text-grey">{{ sendData.fj.length }}/6</view>
				</view>

				<u-upload :fileList="sendData.fj" @afterRead="afterRead" @delete="deletePic" name="1" multiple
					:maxCount="6"></u-upload>


			</view>
		</view>
		<view v-if="false">
			<view class="feedback-title">
				<text>反馈类型</text>
			</view>
			<view class="feedback-body">
				<radio-group @change="handleFeedbackTypeChange">
					<label class="gender-item" v-for="(item, index) in feedbackType" :key="index">
						<radio class="gender-item-radio" color="#037EFF" :value="item.value"
							:checked="item.value === sendData.type" />
						<text class="gender-item-text">{{ item.name }}</text>
					</label>
				</radio-group>
			</view>
			<view class="feedback-title">
				<text>您的称呼</text>
			</view>
			<view class="feedback-body">
				<input class="feedback-input" v-model="sendData.contact" placeholder="选填,方便我们联系您" />
			</view>
			<view class="feedback-title">
				<text>QQ/邮箱/手机号</text>
			</view>
			<view class="feedback-body">
				<input class="feedback-input" v-model="sendData.mobile" placeholder="选填,方便我们联系您" />
			</view>
		</view>

		<button :disabled="btnLoading" :loading="btnLoading" class="btn-submit" @tap="submit">
			提交
		</button>

	</view>
</template>

<script>
	// baseurl
	// #ifndef H5
	import config from '../../../common/config.js'
	// #endif

	export default {
		data() {
			return {
				isFeedback: false, //是否反馈成功
				token: "",
				msgContents: [
					'界面显示错乱',
					'程序启动缓慢',
					'UI无法直视',
					'偶发性崩溃'
				],
				feedbackType: [{
						value: "0",
						name: '功能建议'
					},
					{
						value: "1",
						name: 'BUG反馈'
					},
					{
						value: "2",
						name: '业务咨询'
					}
				],
				sendData: {
					// type: "0", //反馈类型
					// contact: '', //联系人
					yjms: '', //反馈内容
					// mobile: '', //联系方式
					fj: []
				},
				btnLoading: false
			};
		},
		onLoad() {

			this.token = uni.getStorageSync("token")
		},
		methods: {
			// 监听反馈类型事件
			handleFeedbackTypeChange(e) {
				this.sendData.type = e.detail.value;
			},

			// 快速输入
			chooseMsg() {
				uni.showActionSheet({
					itemList: this.msgContents,
					success: res => {
						this.sendData.yjms = this.msgContents[res.tapIndex];
					}
				});
			},
			// 发送反馈
			/**
			 * 触发表单提交
			 */
			submit() {

				if (this.sendData.yjms) {
					// this.sendData.type = parseInt(this.sendData.type);

					this.submitForm(this.sendData);


				} else {
					uni.showToast({
						title: '请填写反馈建议',
						icon: 'none'
					});

				}
			},

			submitForm(value) {
				let params = {};

				params.yjms = this.sendData.yjms;
				console.log(JSON.stringify(this.sendData.fj));
				if (this.sendData.fj && this.sendData.fj.length > 0) {
					let imgs = this.sendData.fj;
					let fj = []
					for (let i = 0; i < imgs.length; i++) {
						let img = {};
						img.name = imgs[i].name;
						img.url = imgs[i].url;
						fj.push(img)
					}

					params.fj = JSON.stringify(fj);
				}




				uni.showLoading({
					title: '正在提交...',
					mask: false
				});
				uni.$u.api.feedback(params).then(res => {
					uni.hideLoading();
					uni.showToast({
						title: '提交成功 感谢您的反馈',
						icon: 'none'
					});
					this.sendData = {
						yjms: '', //反馈内容
						fj: []
					}

				}).catch(err => {
					uni.hideLoading();
					console.log(err.errMsg);
				})
			},
			// 删除图片
			deletePic(event) {
				this.sendData.fj.splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				console.log("list", JSON.stringify(lists));
				let imgsLen = this.sendData.fj.length
				lists.map((item) => {
					this.sendData.fj.push({
						...item,
						status: 'uploading',
						message: '上传中',
						url: '',
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)
					let res = JSON.parse(result)

					let item = this.sendData.fj[imgsLen]

					this.sendData.fj.splice(imgsLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: res.url,
						// name: res.fileName
					}))

					imgsLen++
				}
			},
			uploadFilePromise(url) {
				let uploadUrl = config.baseUrl + '/common/upload'
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
						name: 'file',
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
	};
</script>

<style lang="scss">
	page {
		background-color: #F8F8F8;
	}

	.text-black {
		color: $uni-text-color;
		font-size: $uni-font-size-base;
	}

	.text-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-base;
	}


	/*问题反馈*/
	.feedback-title {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		font-size: 28rpx;
	}

	.feedback-star-view.feedback-title {
		justify-content: flex-start;
		margin: 0;
	}

	.feedback-quick {
		padding-right: 10rpx;
		color: #606266;
		font-size: $uni-font-size-base;
	}

	.feedback-body {
		background: #FFFFFF;
		padding: 10rpx 30rpx;

		.gender-item {
			margin-right: 20rpx;

			.gender-item-text {
				padding-left: 10rpx;
			}

			radio .wx-radio-input.wx-radio-input-checked {
				background: $uni-color-primary;
				border-color: $uni-color-primary;
			}
		}
	}

	.feedback-textare {
		height: 200rpx;
		font-size: 28rpx;
		line-height: 50rpx;
		width: 100%;
		box-sizing: border-box;
		padding: 20rpx 30rpx 0;
	}

	.feedback-input {
		font-size: 28rpx;
		height: 72rpx;
		min-height: 50rpx;
		padding: 15rpx 20rpx;
		line-height: 72rpx;
	}

	.feedback-uploader {
		padding: 22rpx 20rpx;
	}

	.btn-submit {
		border-radius: 20rpx;
		width: 90%;
		color: #FFFFFF;
		margin-top: 100rpx;
		background-color: $uni-color-primary;
		margin-bottom: 70rpx;
	}

	.image-title {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		color: #606266;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
	}

	.bg-success {
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
</style>
