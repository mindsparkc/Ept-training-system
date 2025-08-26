<!-- 问答题答题组件 -->
<template>
	<view>
		<view class="text-answer-tip" v-if="!isShowAnswer">请在下方输入框中填写答案</view>

		<kt-voice-textarea @input="input" v-if="!isShowAnswer" :value="question.answered"></kt-voice-textarea>
		<view class="text-bg" v-if="isShowAnswer">{{question.answered?question.answered:'未填写答案'}}</view>


	</view>
</template>

<script>
	export default {
		name: "item-quesiton-subjective",

		props: {
			//是否显示答案  若显示答案 则不可操作
			isShowAnswer: {
				type: Boolean,
				default: false
			},
			question: {
				type: Object,
				default: null
			}
		},
		computed: {
			answered() {
				let answered = JSON.parse(JSON.stringify(this.question))
				return answered
			}
		},
		data() {
			return {};
		},
		methods: {
			//问答题输入框
			input(value) {
				this.answered.answered = value
				if (value) {
					this.answered.answeredFlag = true
				} else {
					this.answered.answeredFlag = false
				}

				this.$emit('input', this.answered)
			},
		},

	}
</script>

<style>
	.text-answer-tip {
		margin-top: 12px;
		margin-bottom: 12px;
		color: #333333;
		font-size: 16px;
		font-weight: bold;
	}

	.text-bg {
		background-color: #fbf7fe;
		border: solid 1px #f3eff6;
		border-radius: 10rpx;
		display: flex;
		padding: 10px;
		margin-top: 12px;
		color: #666666;
		font-size: 16px;
	}
</style>
