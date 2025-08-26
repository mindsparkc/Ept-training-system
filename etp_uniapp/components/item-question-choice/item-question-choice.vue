<!-- 选择题  单选 多选 判断-->
<template>
	<view v-if="choices&&choices.length>0">
		<view v-for="(item,index) in choices" :key="index" class="row" @click="checkChange(index)">
			<view :class="bgStyle[checkStyle(item)]">
				<text :class="choiceStyle[checkStyle(item)]">{{transfNumToChar(index+1)}}</text>
				<image :src="item.image" mode="widthFix" style="width: 500rpx;" v-if="item.image"></image>
				<text :class="textStyle[checkStyle(item)]" v-if="item.content">{{item.content}}</text>
			</view>

		</view>

		<view class="bg-answer" v-if="isChoosed||isShowAnswer">

			<text class="text-tips">您选择</text>
			<text :class="answerTextStyle[checkAnswerTextStyle()]">{{userAnswer}}</text>
			<text class="text-error" v-if="!isRight&&isShowAnswer">回答错误</text>
			<text class="text-right" v-if="isRight&&isShowAnswer">回答正确</text>

		</view>

	</view>
</template>

<script>
	export default {
		name: "item-question-choice",
		props: {
			choicesList: {
				type: Array,
				default: _ => [],
				required: true
			},
			type: {
				type: String,
				default: ""
			},

			//是否显示答案  若显示答案 则不可操作  
			isShowAnswer: {
				type: Boolean,
				default: false
			},
			//当查看历史考试记录时，传入是否正确 正确绿色 错误红色
			isRight: {
				type: Boolean,
				default: false
			}
		},
		computed: {
			choices() {
				let choices = this.choicesList

				return choices
			}
		},
		watch: {
			choicesList: {
				handler(val) {

					this.getUserChoosed()

				},
				deep: true,
				immediate: true

			}
		},

		data() {
			return {
				isChoosed: false, //是否选择
				userAnswer: "", //用户答案

				bgStyle: [
					'bg-normal-choice',
					'bg-choose-choice',
					'bg-true-choice',
					'bg-false-choice',
				],
				choiceStyle: [
					'normal-choice',
					'choose-choice',
					'true-choice',
					'false-choice',
				],
				textStyle: [
					'normal-text',
					'choose-text',
					'true-text',
					'false-text',
				],
				answerTextStyle: [
					'text-normal',
					'text-right',
					'text-error'

				]
			};
		},
		methods: {
			//获取选择题答案
			getAnswer() {
				console.log("getAnswer");
				let answer = ""
				for (let i = 0; i < this.choicesList.length; i++) {
					if (this.choicesList[i].rightFlag) {
						answer = answer.concat(String.fromCharCode(64 + parseInt(i + 1)))
					}

				}
				return answer
			},
			//获取用户选项
			getUserChoosed() {
				//初始化用户选项
				this.userAnswer = ""
				for (let i = 0; i < this.choicesList.length; i++) {
					if (this.choicesList[i].selectFlag) {
						this.userAnswer = this.userAnswer.concat(String.fromCharCode(64 + parseInt(i + 1)))
					}
				}
				if (!this.userAnswer) {
					this.isChoosed = false;
					this.userAnswer = "未选择"
				} else {
					this.isChoosed = true;
				}




			},

			//将数字转换为字母
			transfNumToChar(num) {
				return String.fromCharCode(64 + parseInt(num));
			},


			//点击选项 将被点击的选项从列表中移除或者加入并改变状态
			checkChange(index) {
				//若显示答案解析 则不可操作 
				if (!this.isShowAnswer) {
					//判断是单选还是多选  若为单选 则取消其他选项  多选 则不变
					if (this.type == '1' || this.type == '3') { //单选或者判断题
						for (let i = 0; i < this.choices.length; i++) {
							this.choices[i].selectFlag = false;
							if (index == i) {
								this.choices[i].selectFlag = true;
							}

						}
					} else {
						this.choices[index].selectFlag = !this.choices[index].selectFlag;
					}

					//更新选项状态 
					this.getUserChoosed();
					//与父组件通信传值
					this.$emit('checked', this.choices);

				}
			},


			//判断选择题选项样式
			checkStyle(item) {
				//需要显示答案
				if (this.isShowAnswer) {
					// console.log(JSON.stringify(item));

					if (item.selectFlag) { //判断用户是否选中
						//若选中  正确显示绿色  不正确显示红色
						if (item.rightFlag) {
							return 2;
						} else {
							return 3;
						}
					} else { //用户未选中  正确显示绿色  不正确显示未被选中状态
						if (item.rightFlag) {
							return 2;
						} else {
							return 0;
						}
					}
				} else { //不需要显示答案  则只需判断用户是否选中
					if (item.selectFlag) {
						return 1;
					} else {
						return 0;
					}
				}
			},
			//用户答案显示样式
			checkAnswerTextStyle() {
				if (this.isShowAnswer) {
					if (this.isRight) {
						return 1;
					} else {
						return 2;
					}
				} else {
					return 0;
				}
			}
		},

	}
</script>

<style lang="scss">
	// 选项默认状态
	.bg-normal-choice {
		background-color: #fbf7fe;
		border: solid 1px #f3eff6;
		border-radius: 10rpx;
		display: flex;
		width: 100%;
		padding: 10px;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		color: #666666;
		font-weight: normal;
		font-size: 16px;

		.normal-choice {
			width: 60rpx;
			height: 60rpx;
			border-radius: 5px;
			background-color: #FFFFFF;
			margin-right: 20rpx;
			line-height: 60rpx;
			text-align: center;
		}

		.normal-text {
			line-height: 1.5em;
			flex: 1;
		}
	}

	// 选项选中状态
	.bg-choose-choice {
		width: 100%;
		padding: 10px;
		border: solid 1px rgba(3, 126, 255, 0.15);
		background-color: rgba(3, 126, 255, 0.1);
		border-radius: 10rpx;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		font-size: 16px;
		font-weight: bold;
		color: #037eff;

		.choose-choice {
			text-align: center;
			width: 60rpx;
			height: 60rpx;
			border-radius: 5px;
			background-color: #FFFFFF;
			line-height: 60rpx;
			margin-right: 20rpx;

			// box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
		}

		.choose-text {
			line-height: 1.5em;
			flex: 1;
		}
	}

	//选项正确
	.bg-true-choice {
		width: 100%;
		background-color: #edf4f3;
		border: solid 1px #edf4f3;
		border-radius: 10rpx;
		display: flex;
		padding: 10px;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;

		font-size: 16px;

		.true-choice {
			text-align: center;
			width: 60rpx;
			line-height: 60rpx;
			height: 60rpx;
			border-radius: 5px;
			background-color: #3db376;
			margin-right: 20rpx;
			color: #fff;

			// box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
		}

		.true-text {
			line-height: 1.5em;
			font-weight: bold;
			color: #3db376;
			flex: 1;
		}

	}

	//选项错误

	.bg-false-choice {
		width: 100%;
		background-color: #ffe9ef;
		border: solid 1px #ffe9ef;
		border-radius: 10rpx;
		display: flex;
		padding: 10px;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		font-size: 16px;

		.false-choice {
			text-align: center;
			line-height: 60rpx;
			width: 60rpx;
			height: 60rpx;
			border-radius: 5px;
			background-color: #f4404b;
			margin-right: 20rpx;
			color: #fff;
			// box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
		}

		.false-text {
			line-height: 1.5em;
			color: #f4404b;
			font-weight: bold;
			flex: 1;

		}

	}

	.row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		margin-top: 20px;
	}



	.text-question {
		line-height: 1.5em;
		font-size: 16px;
		color: #303133;
	}

	.bg-answer {
		padding: 10px 30rpx;
		margin-top: 40rpx;
		border: solid 1px rgba(3, 126, 255, 0.15);
		background-color: rgba(3, 126, 255, 0.1);
		border-radius: 10rpx;
	}

	.text-tips {
		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: $uni-text-color;

	}

	.text-right {
		font-weight: bold;
		font-size: 16px;
		color: #3db376;
		margin-left: 20rpx;
	}

	.text-error {
		font-weight: bold;
		font-size: 16px;
		color: #f4404b;
		margin-left: 20rpx;
	}

	.text-normal {
		font-weight: bold;
		font-size: 16px;
		color: #037EFF;
		margin-left: 20rpx;
	}

	.text-tips-yes {
		font-weight: bold;
		font-size: 16px;
		color: #3db376;
		margin-left: 20rpx;
	}

	.text-tips-no {
		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: #E30C0C;
		margin-left: 20rpx;
	}
</style>
