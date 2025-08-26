<!-- 考试专用答题组件 -->
<template>
	<scroll-view scroll-y  style="height:calc(100vh - 45px);">

		<view class="page">
			<!-- <view>
				<text class="text-tag">{{data.lxbq}}题</text>
				<text class="text-score">({{data.score}}分)</text>

				<view style="margin-top: 10px;">
					<mp-html :content="data.tg"></mp-html>
				</view>
			</view>
 -->
			<mp-html :content="addQuestionType(data.lxbq+'题',data.score+'分',data.tg)"></mp-html>


			<view v-if="data.lx=='4'||data.lx=='5'">
				<view class="text-answer-tip">请在下方输入框中输入您的答案</view>
				<view v-for="(item,index) in completionValues" :key="index" v-if="data.lx=='4'">
					<view class="flex-row">
						<text class="text-no" v-if="completionValues!=null&&completionValues.length>1">{{index+1}}.
						</text>
						<u--input :placeholder="setCompletionTips(index)" border="surround" v-model="item.value"
							clearable>
						</u--input>
					</view>

				</view>

				<view v-if="data.lx=='5'">

					<kt-voice-textarea @input="input"></kt-voice-textarea>

				</view>

			</view>
			<view v-for="(item,index) in options" :key="index" class="row" @click="checkChange(index)"
				v-if="data.lx=='1'||data.lx=='2'||data.lx=='3'">

				<view :class="[optionIsChecked(index)?'bg-selected-answer':'bg-normal-answer']">
					<text :class="[optionIsChecked(index)?'option-yes':'option-unchecked']">{{item.key}}</text>
					<text style="flex: 1;" class="text-question">{{item.value}}</text>
				</view>
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

			<view v-if="needAnalysis">
				<view class="row" style="margin-bottom: 20rpx;">
					<view class="vertical-line"></view>
					<text class="text-tips" style="margin-left:20rpx ;">题目解析</text>
				</view>

				<!-- <text style="margin-left: 30rpx;">{{data.jx?data.jx:"暂无解析"}}</text> -->
				<view style="margin-left: 30rpx;">
					<mp-html :content="data.jx?data.jx:'暂无解析'"></mp-html>
				</view>
			</view>


			<view class="btn-margin" v-if="data.lx=='2'||data.lx=='4'||data.lx=='5'">
				<u-button type="primary" shape="circle" :text="btnText" @click="confirm()"></u-button>
				<text class="btn-tips">做完本题记得点击确认答案按钮</text>

			</view>
			<view v-if="isShowUserAnswer" class="bg-user-answer">
				<text>您选择<text style="color: #037eff; margin-left: 20rpx;">{{checkAnswer()}}</text> </text>
			</view>
		</view>
	</scroll-view>
</template>

<script>
	export default {
		name: "item-question",
		props: {
			swiperHeight: {
				type: Number,
				value: 0
			},
			//试题
			data: {
				type: Object,
				default: function() {
					return {}
				}
			},
			//是否需要试题解析
			needAnalysis: {
				type: Boolean,
				default: function() {
					return false
				}
			},
			//是否直接展示用户选项
			isShowUserCheck: {
				type: Boolean,
				default: function() {
					return false
				}
			}
		},
		mounted() {


		},
		watch: {
			data: {
				handler() {
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
						//判断题答案 0：错误  1：正确   正确1 错误2
						if (this.data.da) {
							if (this.data.da == "0") {
								this.options[1].isAnswer = true;
							} else {
								this.options[0].isAnswer = true;
							}
						}


					} else if (this.data.lx == "4") { //填空题
						if (this.completionValues == null || this.completionValues.length <= 0) {
							let gaps = this.data.place;
							for (let i = 0; i < gaps; i++) {
								let item = {
									value: ""
								}
								this.completionValues.push(item);
							}
						}



					} else {
						if (this.options == null || this.options.length <= 0) {
							if (this.data.xx) {
								this.options = this.stringToArray(this.data.xx);
								for (var i = 0; i < this.options.length; i++) {
									this.options[i].isChecked = false;
									this.options[i].isAnswer = false;
									if (this.data.da) {
										if (this.options[i].key == this.data.da || (this.data.da.indexOf(this.options[i]
												.key)) != -1) {
											this.options[i].isAnswer = true;
										}
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
				confirmAnswer: false, //是否确认答案  即选择结束
				options: [], //选项
				btnText: "确认答案",
				value: '', //输入的答案
				answer: [], //本题选择的答案
				hasAnswer: false, //是否有答案
				completionValues: [], //填空题答案
				completionTips: "",
				isShowUserAnswer: false //是否显示用户选择选项提示语


			};
		},
		methods: {
			//问答题输入框
			input(value) {
				this.value = value;
			},

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

			saveAnswers(da) {
				let that = this;
				//同步缓存
				console.log("用户作答：", da);
				setTimeout(() => {
					//下一题 并将答案同步给父组件
					that.$emit("doneQuestion", da)
				}, 800);

			},

			//点击选项 将被点击的选项从列表中移除或者加入并改变状态
			checkChange(index) {

				//判断是单选还是多选  若为单选 则取消其他选项  多选 则不变
				if (this.data.lx == '1' || this.data.lx == '3') { //单选或者判断题

					this.answer = [];
					this.answer.push(index);

					let da = ""; //用户选择的答案 其中判断题 选A代表0正确  B代表1错误

					for (let i = 0; i < this.options.length; i++) {
						this.options[i].isChecked = false;
						if (index == i) {
							let option = this.options[i];
							option.isChecked = true;
							this.options.splice(i, 1, option);
							da = option.key
						}

					}
					if (this.data.lx == '3') { //判断题A正确-1  B错误-0
						if (da == 'A') { //正确
							da = '1'
						} else {
							da = '0'
						}
					}

					//单选或者判断 点击选项之后记录选项  并且跳转到下一题
					this.saveAnswers(da);

				} else { //多选
					let muliOption = this.options[index];
					muliOption.isChecked = !muliOption.isChecked;
					this.options.splice(index, 1, muliOption);
					this.answer = [];
					for (let j = 0; j < this.options.length; j++) {
						if (this.options[j].isChecked) {
							this.answer.push(j);
						}
					}


				}
				this.isShowUserAnswer = true;
				console.log("用户选择的答案", JSON.stringify(this.answer));

			},

			setCompletionTips(index) {
				let kong = this.completionValues.length;
				if (kong != null && kong == 1) {
					return "请输入您的答案";
				} else {
					let num = index + 1
					return "请输入第" + num + "个空的答案";
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

			//确认答案
			confirm() {
				// console.log("确认答案", JSON.stringify(this.completionValues));
				let isCheckedAnswer = false;

				let da = ""; //用户作答答案
				if (this.data.lx == '2') { //若为复选题 则判断是否选择了答案

					let das = [];
					for (let i = 0; i < this.options.length; i++) {
						if (this.options[i].isChecked) {
							isCheckedAnswer = true;
							das.push(this.options[i].key)
						}

					}
					da = das.join(",")

				}
				//若为问答题 则判断是否输入了答案
				if (this.data.lx == '5') {
					if (this.value) {
						isCheckedAnswer = true;
						da = this.value;
					}
				}
				//若为填空题
				if (this.data.lx == '4') {
					let values = this.completionValues;
					let isAllComplete = true;

					let daStrArray = [];
					for (let i = 0; i < values.length; i++) {
						daStrArray.push(values[i].value);
						if (!values[i].value) {
							isAllComplete = false;
						}
					}
					if (isAllComplete) {
						isCheckedAnswer = true;
						da = daStrArray;

					}


				}


				if (isCheckedAnswer) {
					this.saveAnswers(da);
				} else {
					uni.showToast({
						title: '请先作答',
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


			addQuestionType(type, score, content) {

				let typeHtml =
					`<span style='font-size: 12px;color: #FFFFFF;background-color: #037EFF;padding: 3rpx 10rpx;border-top-left-radius: 10rpx;border-top-right-radius: 10rpx;border-bottom-right-radius: 10rpx;'>${type}</span><span style='font-size: 14px;color: #909299;margin-right:10px;margin-left:10px'>(${score})</span>`;

				//判断题干最外层的标签是否为<p>标签 若为p标签 则将拼接的题目类型加到p标签内防止换行
				let mergeContent = "";
				if (content.substring(0, 3) == '<p>') {

					let innerContent = content.substring(content.indexOf('<p>') + 3, content.indexOf('</p>'));
					let mergeString = typeHtml.concat(innerContent);

					mergeContent = content.replace(innerContent, mergeString);
				} else {
					mergeContent = typeHtml.concat(content);
				}

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
		padding: 90rpx 30rpx;

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

	.text-score {
		font-size: $uni-font-size-base;
		color: $uni-text-color-grey;
		// margin-left: 10rpx;
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
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
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
		background-color: #E30C0C;
		font-size: $uni-font-size-lg;
		color: #FFFFFF;
	}

	.bg-normal-answer {
		background-color: #FFFFFF;
		border-radius: 10rpx;
		display: flex;
		width: 100%;
		padding: 10rpx 20rpx;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
	}

	.bg-selected-answer {
		width: 100%;
		padding: 10rpx 20rpx;
		background-color: rgba(3, 126, 255, 0.1);
		border-radius: 10rpx;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
	}

	.row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		margin-top: 24rpx;
	}

	.btn-margin {
		margin: 86rpx 100rpx;
		text-align: center;
	}

	.vertical-line {
		height: 25rpx;
		width: 6rpx;
		border-radius: 10rpx;
		background-color: $uni-color-primary;
	}

	.text-answer-tip {
		margin-top: 40rpx;
		margin-bottom: 20rpx;
		color: #606266;
		font-size: 28rpx;

	}

	.text-no {
		font-size: 40rpx;
		color: #909193;
		margin-right: 20rpx;

	}

	.btn-tips {
		color: #F1A532;
		font-size: 20rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-top: 30rpx;
	}

	.bg-user-answer {
		background-color: rgba(3, 126, 255, 0.1);
		border-radius: 10rpx;
		margin-top: 32rpx;
		padding: 20rpx;
		font-weight: bold;
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
	}
</style>
