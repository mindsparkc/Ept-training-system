<!-- 错题本专用答题组件 -->
<template>
	<scroll-view scroll-y style="height:calc(100vh - 45px);">

		<view class="page" v-if="question">
			<mp-html :content="addQuestionType(tranType(question.stlx),question.tg)"></mp-html>

			<!-- 填空题 答案为数组形式 -->
			<view v-if="question.stlx=='4'">

				<view class="row" style="margin-bottom: 20rpx;">
					<view class="vertical-line"></view>
					<text class="text-tips" style="margin-left:25rpx ;">您的回答</text>

				</view>
				<view v-if="question.answered">
					<view v-for="(item,index) in stringToArray(question.answered) " :key="index" class="text-input">
						<text class="text-no">【{{index+1}}】</text>{{item?item:'没有作答'}}
					</view>

				</view>
				<view v-else>

					<view class="text-input">没有作答</view>
				</view>
				<view class="row" style="margin-bottom: 20rpx;">
					<view class="vertical-line"></view>
					<text class="text-tips" style="margin-left:25rpx ;">正确答案</text>
				</view>



				<view v-for="(answerItem,answerIndex) in stringToArray(question.da)" :key="answerIndex"
					class="text-input">
					<view class="flex-row">
						<text class="text-no">【{{answerIndex+1}}】</text>
						<view v-for="(answerDataItem,answerDataIndex) in answerItem" :key="answerDataIndex">
							<text
								class="text-correct-yes">{{answerDataItem}}{{answerDataIndex==answerItem.length-1?"":"/"}}</text>
						</view>
					</view>
				</view>

			</view>

			<!-- 问答题 -->
			<view v-if="question.stlx=='5'" style="margin-top: 30rpx;">
				<view class="row" style="margin-bottom: 20rpx;">
					<view class="vertical-line"></view>
					<text class="text-tips" style="margin-left:25rpx ;">您的回答</text>
				</view>
				<view class="text-input">{{question.answered?question.answered:'没有作答'}}</view>
			</view>

			<!-- 选择 判断 -->
			<view v-for="(item,optionsIndex) in options" :key="optionsIndex" class="row"
				v-if="question.stlx=='1'||question.stlx=='2'||question.stlx=='3'">
				<text
					:class="{'option-yes':item.isAnswer||item.isAnswer&&item.isChecked,'option-no':!item.isAnswer&&item.isChecked}"
					class="option-unchecked">{{item.key}}</text>
				<!-- <text  class="text-question">{{item.value}}</text> -->
				<view style="flex: 1;" class="text-question">
					<mp-html :content="item.value"></mp-html>
				</view>

			</view>

			<view class="bg-answer" v-if="question.stlx=='1'||question.stlx=='2'||question.stlx=='3'">
				<view>
					<!-- <view :class="[question.isRight?'text-correct':'text-error']" style="margin-bottom: 20rpx;">
					{{question.isRight?'恭喜您 回答正确!':'很抱歉 回答错误'}}
				</view> -->
					<text class="text-tips">正确答案</text>
					<text class="text-tips-yes">{{tfAnswer(question.da)}}</text>
				</view>

				<view style="margin-top: 20rpx;">
					<text class="text-tips">您的答案</text>

					<text
						v-if="question.answered&&question.answered!=''&&question.answered!='[]'&&question.answered!='undefined'"
						:class="[question.isRight? 'text-tips-yes':'text-tips-no']">{{checkAnswer()}}</text>


					<text v-else class="text-tips-no">您没有作答</text>

				</view>
			</view>

			<view>
				<view class="row" style="margin-bottom: 20rpx;">
					<view class="vertical-line"></view>
					<text class="text-tips" style="margin-left:25rpx ;">题目解析</text>
				</view>

				<!-- <text style="margin-left: 30rpx;" class="text-question">{{question.jx?question.jx:"暂无解析"}}</text> -->

				<view style="margin-left: 30rpx;">
					<mp-html :content="question.jx?question.jx:'暂无解析'"></mp-html>
				</view>

			</view>
			<view>
				<view class="row" style="margin-bottom: 20rpx;" v-if="question.remake">
					<view class="vertical-line"></view>
					<text class="text-tips" style="margin-left:25rpx ;">备注</text>
				</view>

				<!-- 	<text style="margin-left: 30rpx;" class="text-question">{{question.remake?question.remake:"暂无"}}</text> -->

				<view style="margin-left: 30rpx;">
					<mp-html :content="question.remake?question.remake:'暂无'"></mp-html>
				</view>
			</view>




		</view>
	</scroll-view>
</template>

<script>
	export default {
		props: {
			swiperHeight: {
				type: Number,
				value: 0
			},
			//试题
			question: {
				type: Object,
				default: function() {
					return {}
				}
			},

		},
		mounted() {

		},
		watch: {
			question: {
				handler() {
					//模式  类型 1-单选 ，2-复选，3-判断（即只有两个选项 正确 错误的选择题  判断题答案  0：错  1：对），4-填空，5-问答
					if (this.question.stlx == "3") { //处理判断题选项

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
						if (this.question.da) { //正确答案
							if (this.question.da == "0") { //错误 b
								this.options[1].isAnswer = true;
								this.options[0].isAnswer = false;
							} else { //正确 a
								this.options[0].isAnswer = true;
								this.options[1].isAnswer = false;
							}
						}
						let answer = this.question.answered;
						if (answer) { //用户选项
							if (answer == "0") { //错误  即选择了b
								this.options[1].isChecked = true;
								this.options[0].isChecked = false;
							} else {
								this.options[0].isChecked = true;
								this.options[1].isChecked = false;
							}
						}

						// console.log("判断题",JSON.stringify(this.options));

					} else {
						if (this.question.xx && this.question.xx != '') { //处理选择题选项
							this.options = this.stringToArray(this.question.xx);

							for (let i = 0; i < this.options.length; i++) {
								this.options[i].isChecked = false;
								this.options[i].isAnswer = false;

								if (this.question.da) {
									if (this.options[i].key == this.question.da || (this.question.da.indexOf(this
											.options[i]
											.key)) != -1) {
										this.options[i].isAnswer = true;
									}
								}
								if (this.question.answered) {
									if (this.options[i].key == this.question.answered || (this.question.answered.indexOf(
											this
											.options[i]
											.key)) != -1) {
										this.options[i].isChecked = true;
									}
								}


							}
						}

					}


				},
				deep: true,
				immediate: true

			}
		},




		data() {
			return {
				//判断题  单选题 选完选项后自动跳转下一题  多选题 填空题 问答题出现确认答案按钮 
				checkedIndex: [], //被选中的选项index
				options: [], //选项
				btnText: "确认答案",
				value: '', //输入的答案

			};
		},
		methods: {
			//判断选项是否被选中
			optionIsChecked(index) {
				if (this.answer && this.answer.length > 0) {
					if (this.answer.indexOf(index) == -1) { //没找到
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}


			},

			//用户选择的答案 主要用于处理多选题
			checkAnswer() {
				let answer = "";
				for (let i = 0; i < this.options.length; i++) {
					if (this.options[i].isChecked) {
						answer = answer.concat(this.options[i].key + ",")
					}

				}
				answer = answer.substr(0, answer.length - 1);
				return answer;
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

			//判断选项是否全部选对

			judgeAnswer() {
				let isCorrect = true;
				for (let i = 0; i < this.options.length; i++) {
					if (this.options[i].isAnswer) {
						if (!this.options[i].isChecked) {
							isCorrect = false;
						}
					} else {
						if (this.options[i].isChecked) {
							isCorrect = false;
						}
					}

				}
				return isCorrect;

			},


			//json string转换 json array
			stringToArray(str) {
				let content = ""
				if (str) {
					try {
						content = JSON.parse(str);
					} catch (e) {
						console.log("解析失败", e);
						//TODO handle the exception

					}

				}else{
					console.log("解析失败  str为空");
				}
				return content;

			},

			tranType(type) {
				//1-单选 ，2-复选，3-判断（即只有两个选项 正确 错误的选择题  判断题答案  0：错  1：对），4-填空，5-问答
				switch (type) {
					case '1':
						return '单选题';
					case '2':
						return '复选题';
					case '3':
						return '判断题';
					case '4':
						return '填空题';
					case '5':
						return '问答题';
					default:
						break;
				}
			},
			//将题目类型添加到题干的html前  目的是为了页面美观 可以使得试题类型的标签、分值与题干在一行显示
			addQuestionType(type, content) {

				let typeHtml =
					`<span style='font-size: 12px;color: #FFFFFF;background-color: #037EFF;padding: 3px 10rpx;border-top-left-radius: 10rpx;border-top-right-radius: 10rpx;border-bottom-right-radius: 10rpx;margin-right:10px'>${type}</span>`;

				//判断题干最外层的标签是否为<p>标签 若为p标签 则将拼接的题目类型加到p标签内防止换行
				let mergeContent = "";
				if (content.substring(0, 3) == '<p>') {

					let innerContent = content.substring(content.indexOf('<p>') + 3, content.indexOf('</p>'));
					let mergeString = typeHtml.concat(innerContent);

					mergeContent = content.replace(innerContent, mergeString);
				} else {
					mergeContent = typeHtml.concat(`<span>${content}</span>`);
				}

				// console.log(">>>>>>",mergeContent);
				return mergeContent;

			}




		}
	}
</script>

<style lang="scss">
	.page-item {
		display: flex;
		flex-direction: column;
		height: 100%;

	}

	.count-box {
		display: flex;
		flex-direction: row;
		justify-content: flex-end;
		align-items: center;
		margin-right: 30rpx;
	}

	.text-current-count {
		font-size: 40rpx;
		color: #00c6c4;
		font-weight: bold;
	}

	.text-total-count {
		font-size: 30rpx;
		color: #999999;
		font-weight: bold;
	}

	.content {
		font-size: 80rpx;
		align-self: center;
		margin-top: 80rpx
	}

	.page {
		background-color: #FFFFFF;
		padding: 30rpx 30rpx;

	}

	.bg-tag {
		background-color: $uni-color-primary;
		border-radius: 4rpx;
		padding: 6rpx 10rpx;
	}

	.bg-answer {
		background-color: rgba(3, 126, 255, 0.1);
		border-radius: 10rpx;
		padding: 20rpx 30rpx;
		margin-top: 70rpx;
	}

	.text-score {
		font-size: $uni-font-size-base;
		color: $uni-text-color-grey;
		margin-left: 10rpx;
	}

	.text-tag {
		font-size: $uni-font-size-ssm;
		color: #FFFFFF;
		flex-shrink: 0;
		background-color: $uni-color-primary;
		padding: 6rpx 10rpx;
		margin-right: 24rpx;
		border-top-left-radius: 10rpx;
		border-top-right-radius: 10rpx;
		border-bottom-right-radius: 10rpx;
	}

	.text-question {
		line-height: 1.5em;
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
	}

	.text-tips {
		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: $uni-text-color;

	}

	.text-correct {
		font-size: $uni-font-size-lg;
		color: $uni-color-primary;
	}

	.text-error {
		font-size: $uni-font-size-lg;
		color: #f74e07;
	}


	.text-correct-yes {
		// font-weight: bold;
		font-size: $uni-font-size-lg;
		color: $uni-color-primary;

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
		color: #f74e07;
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
		box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
	}

	.option-yes {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		margin-right: 20rpx;
		background-color: $uni-color-primary;
		font-size: $uni-font-size-lg;
		color: #FFFFFF;
		box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
	}

	.option-no {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: #f74e07;
		font-size: $uni-font-size-lg;
		color: #FFFFFF;
	}

	.row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		margin-top: 32rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
	}

	.btn-margin {
		margin: 86rpx 100rpx;
	}

	.vertical-line {
		height: 25rpx;
		width: 6rpx;
		border-radius: 10rpx;
		background-color: $uni-color-primary;
	}

	.text-answer-tip {
		margin-top: 40rpx;
		margin-left: 25rpx;

		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: $uni-text-color;

	}

	.text-input {
		background-color: rgba(3, 126, 255, 0.1);
		border-radius: 10rpx;
		padding: 20rpx 30rpx;
		vertical-align: middle;

		color: #303133;
		margin-top: 10rpx;
		font-size: $uni-font-size-base;
	}

	.text-no {
		font-size: $uni-font-size-base;
		color: #909193;
		margin-right: 20rpx;

	}
</style>
