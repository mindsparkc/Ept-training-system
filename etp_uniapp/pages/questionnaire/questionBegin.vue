<template>
	<view class="page">

		<!-- <view>您好！感谢您百忙之中填写问卷，请根据自己的实际感受和看法如实填写。</view> -->
		<view v-if="queationnaire!=null">{{queationnaire.ms}}</view>

		<view class="question-box">
			<view class="question-item" v-for="(item,index) in questionList " :key="index">
				<!-- 0单选 1多选 2填空 3问答 -->
				<text class="text-title">{{index+1}}、{{item.tg}}</text>
				<!-- 单选 -->
				<u-radio-group v-model="item.da" placement="column" v-if="item.type=='0'" :disabled="isDone||isEnded">
					<u-radio :customStyle="{marginBottom: '10rpx'}" v-for="(option,optionIndex) in item.xxList"
						:key="option.key" :label="option.value" :name="option.key" @change="radioChange"
						:checked="option.isChecked">
					</u-radio>
				</u-radio-group>
				<!-- 多选 -->
				<view style="margin-top: 20rpx;">
					<u-checkbox-group v-model="item.answerList" placement="column" @change="checkboxChange"
						v-if="item.type=='1'" :disabled="isDone||isEnded">
						<u-checkbox :customStyle="{marginBottom: '36rpx',marginLeft:'20rpx'}"
							v-for="(check,checkIndex) in item.xxList" :key="checkIndex" :label="check.value"
							:name="check.key" :checked="check.isChecked">
						</u-checkbox>
					</u-checkbox-group>
				</view>

				<!-- 问答 -->
				<view class="idea-area">
					<u--input v-model="item.da" placeholder="请在此处输入..." v-if="item.type=='2'"
						:disabled="isDone||isEnded">
					</u--input>
					<u--textarea v-model="item.da" placeholder="请在此处输入..." v-if="item.type=='3'"
						:disabled="isDone||isEnded">
					</u--textarea>
				</view>
			</view>


			<view class="btn-normal" @click="submitQuestionnaire()" v-if="!isDone&&queationnaire!=null&&!isEnded">提交答卷
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		onLoad(option) { //option为object类型，会序列化上个页面传递的参数



			this.id = Number(option.id);

			this.getQueationnaireInfo();
		},
		data() {
			return {
				id: -1,
				queationnaire: null,
				selData: [],
				questionList: [],
				isDone: false, //是否已完成
				isEnded: false, //是否已结束
			}
		},

		methods: {

			parseQuestionList() {

				let wjnr = this.queationnaire.wjnr; //问卷内容 若用户填写了问卷则此值包含用户答案，否则为空

				let questions = [];

				if (wjnr != null) {
					this.isDone = true;
					questions = JSON.parse(wjnr);
				} else {
					this.isDone = false;
					questions = JSON.parse(this.queationnaire.nr);
				}



				for (let i = 0; i < questions.length; i++) {
					let answer = "";
					if (questions[i].da != null) {
						answer = questions[i].da;
					}

					let options = questions[i].xxList;
					if (options != null && options.length > 0) {
						for (let j = 0; j < options.length; j++) {
							if (options[j].key == answer) {
								options[j].isChecked = true;
							} else {
								options[j].isChecked = false;
							}

						}
					}


				}
				this.questionList = questions;



			},

			radioChange(n) {
				// console.log('radioChange', this.selData);
			},
			checkboxChange(n) {
				// console.log('change', n);
			},

			submitQuestionnaire() {
				//判断是否全部作答 并对多选题进行选项处理
				let isComplete = true;
				for (var i = 0; i < this.questionList.length; i++) {
					if (this.questionList[i].type == "1" && this.questionList[i].answerList && this.questionList[i]
						.answerList.length >
						0) { //多选题
						let answer = "";
						for (var j = 0; j < this.questionList[i].answerList.length; j++) {
							answer = answer.concat(this.questionList[i].answerList[j] + ",")

						}
						this.questionList[i].da = answer;
						console.log("多选题", JSON.stringify(answer));
					}
					if (!this.questionList[i].da || this.questionList[i].da == "" || this.questionList[i].da ==
						"undefined") {
						isComplete = false;
					}
				}
				if (!isComplete) {
					uni.showToast({
						title: '您还有问题没有填写',
						icon: 'none'
					});
					return;
				}
				uni.showModal({
					title: '温馨提示',
					content: "是否确认提交答卷",
					showCancel: true,

					success: res => {
						if (res.confirm) {
							//确认
							this.submit();
						}
					},
					fail: () => {},
					complete: () => {}
				});

			},

			submit() {
				uni.showLoading({
					title: '请稍候...',
					mask: false
				});
				let params = {
					id: this.id,
					nr: JSON.stringify(this.questionList),
					kfcd: this.queationnaire.kfcd

				}

				uni.$u.api.submitQuestionnaire(params).then(res => {
					uni.hideLoading()
					uni.showToast({
						title: "感谢您的填写",
						icon: 'none'
					});
					//退出
					setTimeout(function() {

						uni.navigateBack({
							delta: 1
						});
					}, 1000);



				}).catch(err => {
					uni.hideLoading()
					console.log(err.errMsg);
				})
			},

			getQueationnaireInfo() {
				uni.$u.api.getQuestionnaireInfo(this.id).then(res => {
					this.queationnaire = res.data;
					uni.setNavigationBarTitle({
						title: this.queationnaire.wjmc
					})
					//判断问卷完成状态
					if (this.queationnaire.status == '已结束') {
						this.isEnded = true;
					} else {
						this.isEnded = false;
					}

					if (this.queationnaire.wjmc) {
						uni.setNavigationBarTitle({
							title: this.queationnaire.wjmc
						})
					}


					this.parseQuestionList();
				}).catch(err => {
					console.log(err.errMsg);
				})
			}


		}
	}
</script>

<style lang="scss">
	.page {
		color: #303133;
		padding: 30rpx;
	}

	.question-box {
		margin: 30rpx 0rpx 20rpx;
	}

	.text-title {
		font-size: 32rpx;
		font-weight: bold;
	}

	.question-item {
		margin-top: 28rpx;

		.u-radio {
			margin: 24rpx 16rpx;
		}

		.u-radio-group--row {
			flex-wrap: wrap;
		}
	}

	.idea-area {
		margin-top: 20rpx;
	}

	.submit-btn {
		margin-top: 60rpx;
	}


	.btn-normal {
		background-color: $uni-color-primary;
		font-size: $uni-font-size-base;
		color: #FFFFFF;
		padding: 24rpx 0rpx;
		border-radius: 40rpx;
		margin-top: 100rpx;
		margin-bottom: 20rpx;
		text-align: center;
	}
</style>
