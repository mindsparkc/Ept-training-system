<!-- 已迭代为新版本 此版本不再使用 -->
<template>
	<view class="page">
		<view>
			<text class="text-tag">{{data.lxbq}}题 </text>
			<text class="text-question">{{data.tg}}</text>
		</view>
		<view class="text-answer-tip">请在下方输入框中输入您的答案</view>
		<view>

			<u--input placeholder="请在此输入填空题答案" border="surround" v-model="value" clearable v-if="data.lx=='4'">
			</u--input>

		</view>
		<view>
			<u--textarea v-model="value" placeholder="请在此输入您的答案" v-if="data.lx=='5'" height="150rpx" maxlength="-1"
				autoHeight>
			</u--textarea>
		</view>

		<view class="bg-answer" v-if="confirmAnswer&&data.lx!=5">
			<view>
				<text class="text-tips">答案</text>
				<text class="text-tips-yes">{{data.da}}</text>
			</view>

		</view>

		<view v-if="confirmAnswer &&data.lx!=5">
			<view class="row" style="margin-bottom: 20rpx;">
				<view class="vertical-line"></view>
				<text class="text-tips" style="margin-left:20rpx ;">题目解析</text>
			</view>

			<text style="margin-left: 30rpx;">{{data.jx?data.jx:"暂无解析"}}</text>
		</view>


		<view class="btn-margin" :animation="animationData">
			<u-button type="primary" shape="circle" :text="btnText" @click="confirm()":disabled="confirmAnswer&&isFinal" ></u-button>

		</view>

	</view>
</template>

<script>
	export default {
		props: {
			data: {
				type: Object,
				default: function() {
					return {}
				}
			},
			//是否最后一节
			isFinal: {
				type: Boolean,
				default: function() {
					return false
				}
			},
		},

		data() {
			return {
				value: '', //输入的答案
				animationData: {},
				// mode: 5, //模式  0单选  1多选    4-填空  5-问答
				confirmAnswer: false, //是否确认答案 
				btnText: "确认答案",

				// data: {
				// 	"id": 2,
				// 	"tg": "一颗树上七只鸟，被猎人用枪打死一只，请问树上还有几只鸟？",
				// 	"lx": "单选",
				// 	"xx": "[{\"key\":\"A\",\"value\":\"1只\"},{\"key\":\"B\",\"value\":\"0只\"},{\"key\":\"C\",\"value\":\"6只\"}]",
				// 	"da": "B",
				// 	"jx": "题目解析题目解析题目解析题目解析题目解析题目解析题目解析",
				// 	"sflx": "0",
				// 	"sfks": "1",
				// 	"delFlag": null,
				// 	"tags": null
				// }
			}
		},

		methods: {

			//确认答案
			confirm() {

				if (this.value) {
					if (this.confirmAnswer) {
						//执行下一步操作 TODO
						if (this.isFinal) {
							uni.showToast({
								title: '本课程到此结束',
								icon: 'none'
							});
						} else {
							this.$emit("next", "习题");
						}

					} else {
						// this.scaleTranslate();
						this.confirmAnswer = true;
						if (this.isFinal) {
							this.btnText = "本课程到此结束";
						} else {

							this.btnText = "下一节";
						}

						//若回答错误  提交到错题本 TODO


					}
				} else {
					uni.showToast({
						title: '请作答',
						icon: 'none'
					});
				}

			},
			//json string转换 json array
			stringToArray(str) {
				return JSON.parse(str);
			},
			//缩小平移
			scaleTranslate() {
				var animation = uni.createAnimation({
					duration: 1000,
					timingFunction: 'ease',
				})

				this.animation = animation
				this.animation.scale(0.7, 1).translate(120, 0).step()
				this.animationData = this.animation.export()
			}




		}
	}
</script>

<style lang="scss">
	.page {
		background-color: #FFFFFF;
		// height: 100%;
		padding: 40rpx 30rpx;
		// margin-top: 80rpx;
		position: relative;
	}

	.bg-tag {
		background-color: $uni-color-primary;
		border-radius: 4rpx;
		padding: 6rpx 10rpx;
	}

	.bg-answer {
		background-color: rgba(3, 126, 255, 0.2);
		border-radius: 4rpx;
		padding: 20rpx 30rpx;
		margin-top: 70rpx;
	}

	.text-tag {
		font-size: $uni-font-size-ssm;
		color: #FFFFFF;
		background-color: $uni-color-primary;
		border-top-left-radius: 10rpx;
		border-top-right-radius: 10rpx;
		border-bottom-right-radius: 10rpx;
		padding: 6rpx 10rpx;
		margin-right: 24rpx;
	}

	.text-question {
		line-height: 1.5em;
	}

	.text-tips {
		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: $uni-text-color;

	}

	.text-tips-yes {
		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: $uni-color-primary;
		margin-left: 20rpx;
	}

	.text-tips-no {
		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: #E30C0C;
		margin-left: 20rpx;
	}


	.text-white {
		color: #FFFFFF;
		font-size: $uni-font-size-lg;
	}

	.text-answer-tip {
		margin-top: 40rpx;
		margin-bottom: 20rpx;
		color: #606266;
		font-size: 28rpx;

	}

	.option-unchecked {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: #FFFFFF;
		color: $uni-text-color;
		margin-right: 20rpx;
		font-size: $uni-font-size-lg;
		box-shadow: 0px 2px 2px 2px rgba(0, 0, 0, 0.16);
	}

	.option-yes {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: $uni-color-primary;
		margin-right: 20rpx;
		font-size: $uni-font-size-lg;
		color: #FFFFFF;

	}

	.option-no {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: #E30C0C;
		margin-right: 20rpx;
		font-size: $uni-font-size-lg;
		color: #FFFFFF;
	}

	.row {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-top: 32rpx;
	}

	.btn-margin {
		margin: 86rpx;
	}

	.vertical-line {
		height: 25rpx;
		width: 6rpx;
		border-radius: 10rpx;
		background-color: $uni-color-primary;
	}
</style>
