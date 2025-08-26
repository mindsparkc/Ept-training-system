<!-- 试题  题目类型：1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题-->
<template>
	<scroll-view scroll-y style="height:calc(100vh - 80px);margin-top: 35px;">
		<view class="paper">
			<view class="bg-title">
				<view style="display: flex;flex-direction: row;align-items: flex-end;">
					<view class="line-vertical"></view>
					<text
						style="font-size: 18px;color: #333333;font-weight: bold;margin: 0px 10px;">{{question.paperUnitName?question.paperUnitName:transfType(question.type)}}</text>
				</view>

				<text style="font-size: 25px;color: #333333;font-weight: bold;"
					v-if="question.paperUnitSort">{{question.paperUnitSort}}
					<text style="font-size: 14px;color: #999999;">/{{question.paperUnitTotal}}</text>
				</text>

			</view>
			<view class="line"></view>

			<view v-if="question.type != '6'">
				<!-- 题干 -->
				<item-question-stem :type="transfType(question.type)" :content="question.content"
					:score="question.score?question.score:0">
				</item-question-stem>
				<!-- 单选题 多选题 判断题 -->
				<view v-if="question.type=='1'||question.type=='2'||question.type=='3'">
					<item-question-choice :choicesList="question.answers" :isShowAnswer="showAnswerFlag"
						:type="question.type" @checked="choiceChecked" :isRight="question.rightFlag">
					</item-question-choice>

				</view>
				<!-- 填空题 -->
				<view v-if="question.type=='4'">

					<item-question-completion :completionValues="question.answers" @completionInput="completionInput"
						:isShowAnswer="showAnswerFlag">
					</item-question-completion>
					<view v-if="showAnswerFlag" class="bg-answer">
						<text class="k-text-lg k-text-black k-text-bold">本题 <text class="text-error" v-if="!question.rightFlag">回答错误</text>
					<text class="text-right" v-else>回答正确</text></text>
					</view>
				</view>

				<!-- 问答题 -->
				<view v-if="question.type=='5'">

					<item-quesiton-subjective @input="input" :isShowAnswer="showAnswerFlag" :question="question">
					</item-quesiton-subjective>
				</view>
				<!-- 题目解析 -->
				<item-question-analysis v-if="showAnswerFlag&&question.type!='6'" :question="question"
					:answer="getCorrectAnswer(question)" :isShowAnswer="showAnswerFlag">

				</item-question-analysis>
			</view>
			<!-- 组合题 -->
			<view v-if="question.type == '6'">
				<!-- 题干 -->
				<view v-if="!showAnswerFlag">
					<text class="text-title">请仔细阅读题目并完成下列试题
						<text
							class="text-sub">（共{{question.children.length+'小题，总计'+question.score?question.score:"0"}}分）</text></text>
				</view>
				<view class="k-text-lg k-text-black k-margin-top-l">
					<mp-html :content="question.content"></mp-html>
				</view>

				<view class="line"></view>

				<view v-for="(item,index) in question.children" :key="index">
					<!-- 题干 -->
					<item-question-stem :type="transfType(item.type)" :content="item.content"
						:score="item.score?item.score:0">
					</item-question-stem>
					<!-- 单选题 多选题 判断题 -->
					<view v-if="item.type=='1'||item.type=='2'||item.type=='3'">
						<item-question-choice :choicesList="item.answers" :isShowAnswer="showAnswerFlag"
							:type="item.type" @checked="mixChoiceChecked" :isRight="item.rightFlag">
						</item-question-choice>

					</view>
					<!-- 填空题 -->
					<view v-if="item.type=='4'">
					
						<item-question-completion :completionValues="item.answers" @completionInput="mixCompletionInput"
							:isShowAnswer="showAnswerFlag">
						</item-question-completion>
						<view v-if="showAnswerFlag" class="bg-answer">
							<text class="k-text-lg k-text-black k-text-bold">本题 <text class="text-error" v-if="!item.rightFlag">回答错误</text>
						<text class="text-right" v-else>回答正确</text></text>
						</view>
					</view>

					<!-- 问答题 -->
					<view v-if="item.type=='5'">
						<item-quesiton-subjective @input="mixInput" :isShowAnswer="showAnswerFlag" :question="item">
						</item-quesiton-subjective>
					</view>
					<!-- 题目解析 -->
					<item-question-analysis v-if="showAnswerFlag" :answer="getCorrectAnswer(item)" :question="item"
						:isShowAnswer="showAnswerFlag">

					</item-question-analysis>
				</view>

			</view>
			<!-- 确定按钮 用于课程答案显示 -->
			<button class="btn" @click="onCompleteBtnClick()" v-if="isShowBtn">完成</button>

		</view>
	</scroll-view>
</template>

<script>
	export default {
		name: "item-question-mix",
		props: {
			//试题
			question: {
				type: Object,
				default: function() {
					return {}
				}
			},
			//是否显示答案  若显示答案 则不可操作
			isShowAnswer: {
				type: Boolean,
				default: false
			},



		},
		computed: {

		},


		watch: {
			question: {
				handler(val) {
					this.questionResult = val;

				},
				deep: true,
				immediate: true

			}
		},
		data() {
			return {
				showAnswerFlag: this.isShowAnswer,
				isShowBtn: false, //完成按钮显示逻辑：允许显示答案时 答题之后显示 无需显示答案时不显示  若显示用户操作答题之后再显示 且点击之后隐藏
				questionResult: null, //学员答案question
			};
		},
		methods: {
			onCompleteBtnClick() {
				this.showAnswerFlag = !this.showAnswerFlag;
				this.isShowBtn = !this.isShowBtn;
				this.uploadAnswer(e)
			},


			//填空题√
			completionInput(e) {

				if (this.isShowAnswer) { //若允许显示答案则显示确定按钮
					this.isShowBtn = true;
				}
				this.questionResult.answers = e
				//判断填空题是否所有空均已填完  已填完将标志位设为true;
				let flag = true;
				for (let i = 0; i < e.length; i++) {
					if (!e[i].answered) {
						flag = false;
					}

				}
				this.questionResult.answeredFlag = flag;

				this.uploadAnswer()

			},
			//问答题√
			input(e) {
				if (this.isShowAnswer) { //若允许显示答案则显示确定按钮
					this.isShowBtn = true;
				}
				this.questionResult = e;
				this.uploadAnswer()

			},


			//组合题填空题
			mixCompletionInput(e) {
				if (this.isShowAnswer) { //若允许显示答案则显示确定按钮
					this.isShowBtn = true;
				}

				//判断填空题是否所有空均已填完  已填完将标志位设为true;
				let flag = true;
				for (let i = 0; i < e.length; i++) {
					if (!e[i].answered) {
						flag = false;
					}
				}
				//找到本填空题在组合题中的位置 替换
				let answers = e;
				let questionId = answers[0].quId;
				//找到组合题位置 从组合题中找到该题的位置 替换answers
				let children = this.questionResult.children;
				let childQuestionIndex = (children || []).findIndex((item) => item.quId == questionId);
				let childQuestion = children[childQuestionIndex];
				childQuestion.answeredFlag = flag;
				childQuestion.answers = answers;

				//遍历组合题题目列表中是否所有题目均已完成 更新组合题的完成状态
				let questionAnsweredFlag = true;
				for (let i = 0; i < children.length; i++) {
					if (!children[i].answeredFlag) {
						questionAnsweredFlag = false;
					}

				}
				this.questionResult.answeredFlag = questionAnsweredFlag;

				this.uploadAnswer()
			},

			//组合题问答题 
			mixInput(e) {
				if (this.isShowAnswer) { //若允许显示答案则显示确定按钮
					this.isShowBtn = true;
				}
				//找到组合题位置 从组合题中找到该题的位置 替换answered
				let answers = e;
			
				let questionId = answers.quId;
				let children = this.questionResult.children;
				let childQuestionIndex = (children || []).findIndex((item) => item.quId == questionId);
				let childQuestion = children[childQuestionIndex];
				childQuestion.answeredFlag = answers.answeredFlag;
				childQuestion.answered = answers.answered;


				//遍历组合题题目列表中是否所有题目均已完成 更新组合题的完成状态
				let questionAnsweredFlag = true;
				for (let i = 0; i < children.length; i++) {
					if (!children[i].answeredFlag) {
						questionAnsweredFlag = false;
					}

				}
				
					
				this.questionResult.answeredFlag = questionAnsweredFlag;
				this.uploadAnswer()
			},
			//选择题√
			choiceChecked(e) {
				if (this.isShowAnswer) { //若允许显示答案则显示确定按钮
					this.isShowBtn = true;
				}
				this.questionResult.answers = e;
				this.questionResult.answeredFlag = true;
				this.uploadAnswer()
			},
			//组合题中的选择题
			mixChoiceChecked(e) {
				if (this.isShowAnswer) { //若允许显示答案则显示确定按钮
					this.isShowBtn = true;
				}

				let answers = e;
				let questionId = answers[0].quId;
				let children = this.questionResult.children;
				let childQuestionIndex = (children || []).findIndex((item) => item.quId == questionId);
				let childQuestion = children[childQuestionIndex];
				childQuestion.answeredFlag = true;
				childQuestion.answers = answers;

				//遍历组合题题目列表中是否所有题目均已完成 更新组合题的完成状态
				let questionAnsweredFlag = true;
				for (let i = 0; i < children.length; i++) {
					if (!children[i].answeredFlag) {
						questionAnsweredFlag = false;
					}

				}
				this.questionResult.answeredFlag = questionAnsweredFlag;

				this.uploadAnswer()
			},


			getCorrectAnswer(item) {

				let answer = "";
				if (item && item.answers && item.answers.length > 0) {
					let answers = item.answers;

					if (item.type == '1' || item.type == '2' || item.type == '3') {
						for (let i = 0; i < answers.length; i++) {

							if (answers[i].rightFlag) {
								answer = answer.concat(String.fromCharCode(64 + parseInt(i + 1)))
							}

						}


					} else if (item.type == '4') {
						for (let j = 0; j < answers.length; j++) {
							let a = answers[j].contents;
							answer = answer.concat((j + 1) + '、' + a.join(" / ") + '\n')
						}

					} else {
						answer = ""
					}
				}


				return answer
			},

			transfType(type) {
				switch (type) {
					case "1":
						return "单选";
					case "2":
						return "多选";
					case "3":
						return "判断";
					case "4":
						return "填空";
					case "5":
						return "问答";
					case "6":
						return "组合";
					default:
						break;
				}
			},
			//将当前题目的答案传递给父组件   每个子组件回调时均需要调用
			uploadAnswer() {

				// console.log(JSON.stringify(e));
				// console.log("回传的值", JSON.stringify(this.questionResult));
				this.$emit('answer', this.questionResult)
			},




		}
	}
</script>

<style>
	.paper {
		box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 24px;
		border-radius: 10px;
		background-color: #fff;
		padding: 15px;
		margin: 12px;
	}

	.bg-title {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
	}

	.line {
		height: 1px;
		background-color: #f8f8f8;
		margin-top: 12px;
	}

	.line-vertical {
		height: 25px;
		width: 6px;
		border-radius: 10px;
		background: linear-gradient(135deg, #037eff 10%, #736EFE 100%);
		box-shadow: rgba(136, 165, 191, 0.48) 5px 2px 10px 0px, rgba(255, 255, 255, 0.8) -5px -2px 10px 0px;

	}

	.text-title {
		font-size: 16px;
		color: #333333;
		font-weight: bold;
	}

	.text-sub {
		font-size: 12px;
		color: #999999;
		font-weight: normal;
	}

	.btn {
		border: 10px;
		font-size: 16px;
		color: #FFFFFF;
		margin-top: 30px;
		background: linear-gradient(135deg, #037eff 10%, #736EFE 100%);

		box-shadow: rgba(136, 165, 191, 0.48) 5px 2px 10px 0px, rgba(255, 255, 255, 0.8) -5px -2px 10px 0px;
	}

	.btn:hover {
		background: linear-gradient(-135deg, #037eff 10%, #736EFE 100%);
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
	
	.bg-answer {
		padding: 10px 30rpx;
		margin-top: 40rpx;
		border: solid 1px rgba(3, 126, 255, 0.15);
		background-color: rgba(3, 126, 255, 0.1);
		border-radius: 10rpx;
	}
</style>
