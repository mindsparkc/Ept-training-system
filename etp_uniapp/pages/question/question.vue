<!-- 未使用 以封装为组件 -->
<template>
	<view class="page">

		<text class="text-tag">{{data.lx}} </text>
		<text class="text-question">{{data.tg}}</text>
		<view v-for="(item,index) in options" :key="index" class="row" hover-class="u-hover-class"
			@click="checkChange(index)">

			<text
				:class="{'option-yes':(((confirmAnswer&&item.isAnswer&&item.isChecked))||(!confirmAnswer&&item.isChecked)||((confirmAnswer&&item.isAnswer))),'option-no':(confirmAnswer&&!item.isAnswer&&item.isChecked)}"
				class="option-unchecked">{{item.key}}</text>
			<text>{{item.value}}</text>

		</view>

		<view class="bg-answer" v-if="confirmAnswer">
			<view>
				<text class="text-tips">答案</text>
				<text class="text-tips-yes">{{data.da}}</text>
			</view>
			<view style="margin-top: 30rpx;">
				<text class="text-tips">您选择</text>
				<text :class="[checkAnswer()==data.da? 'text-tips-yes':'text-tips-no']">{{checkAnswer()}}</text>
			</view>
		</view>

		<view v-if="confirmAnswer">
			<view class="row" style="margin-bottom: 30rpx;">
				<view class="vertical-line"></view>
				<text class="text-tips" style="margin-left:16rpx ;">题目解析</text>
			</view>

			<text>{{data.jx?data.jx:"暂无解析"}}</text>
		</view>


		<view class="btn-margin" :animation="animationData">
			<u-button type="primary" shape="circle" :text="btnText" @click="confirm()"></u-button>
		</view>

	</view>
</template>

<script>
	export default {
		props: {

		},

		data() {
			return {
				animationData: {},
				mode: 0, //模式  0单选  1多选
				checkedIndex: [], //被选中的选项index
				confirmAnswer: false, //是否确认答案  即选择结束
				options: [], //选项
				btnText: "确认答案",

				data: {
					"id": 2,
					"tg": "一颗树上七只鸟，被猎人用枪打死一只，请问树上还有几只鸟？",
					"lx": "单选",
					"xx": "[{\"key\":\"A\",\"value\":\"1只\"},{\"key\":\"B\",\"value\":\"0只\"},{\"key\":\"C\",\"value\":\"6只\"}]",
					"da": "B",
					"jx": "题目解析题目解析题目解析题目解析题目解析题目解析题目解析",
					"sflx": "0",
					"sfks": "1",
					"delFlag": null,
					"tags": null
				}
			}
		},
		onLoad() {
			this.options = this.stringToArray(this.data.xx);
			for (var i = 0; i < this.options.length; i++) {
				this.options[i].isChecked = false;
				this.options[i].isAnswer = false;
				if (this.options[i].key == this.data.da) {
					this.options[i].isAnswer = true;
				}
			}

			console.log(JSON.stringify(this.options));
		},
		methods: {

			//点击选项 将被点击的选项从列表中移除或者加入并改变状态
			checkChange(index) {
				if (!this.confirmAnswer) {
					//判断是单选还是多选  若为单选 则取消其他选项  多选 则不变
					if (this.mode == 0) { //单选
						for (var i = 0; i < this.options.length; i++) {
							this.options[i].isChecked = false;
							if (index == i) {

								let option = this.options[i];
								option.isChecked = true;
								this.options.splice(i, 1, option);
							}

						}
					} else { //多选
						let muliOption = this.options[index];
						console.log(muliOption);
						muliOption.isChecked = !muliOption.isChecked;
						this.options.splice(index, 1, muliOption);

					}
				}

			},
			//用户选择答案
			checkAnswer() {
				let answer = "";
				for (var i = 0; i < this.options.length; i++) {
					if (this.options[i].isChecked) {
						answer = answer.concat(this.options[i].key + ",")
					}

				}
				answer = answer.substr(0, answer.length - 1);

				return answer;
			},

			//确认答案
			confirm() {


				//先判断是否选择了选项
				let isCheckedAnswer = false;
				for (var i = 0; i < this.options.length; i++) {
					if (this.options[i].isChecked) {
						isCheckedAnswer = true;
					}

				}
				if (isCheckedAnswer) {
					if (this.confirmAnswer) {
						//执行下一步操作 TODO
						uni.showToast({
							title: '执行下一步操作·',
							icon: 'none'
						});
					} else {
						this.scaleTranslate();
						this.confirmAnswer = true;
						this.btnText = "下一步";
						
						//若回答错误  提交到错题本 TODO


					}
				} else {
					uni.showToast({
						title: '请先选择选项',
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
				// 先旋转同时放大，然后平移
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
		margin: 40rpx 30rpx;
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
		border-radius: 4rpx;
		padding: 6rpx 10rpx;
		margin-right: 40rpx;
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
		width: 4rpx;
		height: 35rpx;
		background-color: $uni-color-primary;
		border-radius: 2rpx;
	}
</style>
