<!-- 已迭代为新版本 此版本不再使用 -->
<template>
	<view class="page">
		<view>
			<text class="text-tag">{{data.lxbq}}题</text>
			<text class="text-question">{{data.tg}}</text>
		</view>
		<view v-for="(item,index) in options" :key="index" class="row" 
			@click="checkChange(index)">

			<text
				:class="{'option-yes':(((confirmAnswer&&item.isAnswer&&item.isChecked))||(!confirmAnswer&&item.isChecked)||((confirmAnswer&&item.isAnswer))),'option-no':(confirmAnswer&&!item.isAnswer&&item.isChecked)}"
				class="option-unchecked">{{item.key}}</text>
			<text style="flex: 1;">{{item.value}}</text>

		</view>

		<view class="bg-answer" v-if="confirmAnswer">
			<view>
				<text class="text-tips">答案</text>
				<text class="text-tips-yes">{{tfAnswer(data.da)}}</text>
			</view>
			<view style="margin-top: 30rpx;">
				<text class="text-tips">您选择</text>
				<text
					:class="[checkAnswer()==tfAnswer(data.da)? 'text-tips-yes':'text-tips-no']">{{checkAnswer()}}</text>
			</view>
		</view>

		<view v-if="confirmAnswer">
			<view class="row" style="margin-bottom: 20rpx;">
				<view class="vertical-line"></view>
				<text class="text-tips" style="margin-left:20rpx ;">题目解析</text>
			</view>

			<text style="margin-left: 30rpx;">{{data.jx?data.jx:"暂无解析"}}</text>
		</view>


		<view class="btn-margin" :animation="animationData">
			<u-button type="primary" shape="circle" :text="btnText" @click="confirm()" :disabled="confirmAnswer&&isFinal"></u-button>
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
				animationData: {},
				checkedIndex: [], //被选中的选项index
				confirmAnswer: false, //是否确认答案  即选择结束
				options: [], //选项
				btnText: "确认答案",

				// data: {
				// 	"id": 2,
				// 	"tg": "一颗树上七只鸟，被猎人用枪打死一只，请问树上还有几只鸟？",
				// 	"lx": "单选",
				// 	"xx": "[{\"key\":\"A\",\"value\":\"1只\"},{\"key\":\"B\",\"value\":\"0只\"},{\"key\":\"C\",\"value\":\"6只\"}]",
				// 	"da": "0",
				// 	"jx": "题目解析题目解析题目解析题目解析题目解析题目解析题目解析",
				// 	"sflx": "0",
				// 	"sfks": "1",
				// 	"delFlag": null,
				// 	"tags": null
				// }
			}
		},
		mounted() {
			//模式  类型 1-单选 ，2-复选，3-判断（即只有两个选项 正确 错误的选择题  判断题答案  0：错  1：对），4-填空，5-问答
			if (this.data.lx == "3") { //处理判断题选项
				this.options = [{
						key: "A",
						value: "正确",
						isChecked: false,
					},
					{
						key: "B",
						value: "错误",
						isChecked: false,
					}
				];
				//判断题答案 0：错误  1：正确
				if (this.data.da == "0") {
					this.options[1].isAnswer = true;
				} else {
					this.options[0].isAnswer = true;
				}
			} else {
				this.options = this.stringToArray(this.data.xx);
				for (var i = 0; i < this.options.length; i++) {
					this.options[i].isChecked = false;
					this.options[i].isAnswer = false;
					if (this.options[i].key == this.data.da || (this.data.da.indexOf(this.options[i].key)) != -1) {
						this.options[i].isAnswer = true;
					}
				}


			}

		},
		methods: {

			//点击选项 将被点击的选项从列表中移除或者加入并改变状态
			checkChange(index) {
				if (!this.confirmAnswer) {
					//判断是单选还是多选  若为单选 则取消其他选项  多选 则不变
					if (this.data.lx == '1' || this.data.lx == '3') { //单选或者判断题
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
						muliOption.isChecked = !muliOption.isChecked;
						this.options.splice(index, 1, muliOption);

					}
				}

			},
			//用户选择的答案 主要用于处理多选题
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
					if (this.confirmAnswer) { //TODO
						this.$emit("next", "习题");
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
						title: '请先选择选项',
						icon: 'none'
					});
				}

			},
			//处理判断题答案显示
			tfAnswer(str) {
				if (str == "0") {
					return "B"
				} else if (str == "1") {
					return "A"
				} else {
					return str;
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
		position: relative;
		background-color: #FFFFFF;
		// margin-top: 80rpx;
		// height: 100%;
		padding: 40rpx 30rpx;
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
		padding: 6rpx 10rpx;
		margin-right: 24rpx;
		border-top-left-radius: 10rpx;
		border-top-right-radius: 10rpx;
		border-bottom-right-radius: 10rpx;
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
		box-shadow: 0px 2px 2px 2px rgba(0, 0, 0, 0.1);
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
