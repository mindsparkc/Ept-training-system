<!-- 试题答案解析组件  -->
<template>
	<view>
		<view class="bg-title">
			<view class="line-vertical"></view>
			<text class="text-title">答案解析</text>

		</view>

		<view class="line"></view>

		<view class="text-answer" v-if="answer">正确答案：{{answer}}</view>
		<view class="text-analysis">
			<mp-html :content="question.analysis?question.analysis:'暂无解析'"></mp-html>
		</view>
		
		<view class="k-text-md k-text-grey k-margin-top-m" v-if="question.review">评语：{{question.review?question.review:""}}</view>
		<!-- 难易程度 -->
		<view class="k-margin-top-m"></view>
		<text class="level">难易程度：{{question.level?tranfLevel(question.level):"暂未分级"}}</text><text class="k-text-md k-text-grey k-margin-start-m k-text-bold " v-if="isShowAnswer">
			本题得分{{question.useScore?question.useScore:0}}分 满分{{question.score?question.score:0}}分</text>
	</view>
</template>

<script>
	export default {
		name: "item-question-analysis",
		props: {
			isShowAnswer: {
				type: Boolean,
				default: false
			},
			question: {
				type: Object,
				default: null
			},


			answer: {
				type: String,
				default: "暂无答案"
			},

		},
		computed: {

		},


		data() {
			return {}
		},
		methods: {
			//转换level
			tranfLevel(type) {
				let typeName = "";
				switch (type) {
					case "1":
						return "简单";
					case "2":
						return "一般";
					case "3":
						return "较难";
					case "4":
						return "困难";

					default:
						return "简单";
				}
			}
		},

	}
</script>

<style>
	.bg-title {
		display: flex;
		flex-direction: row;
		align-items: flex-end;
		margin-top: 24px;

	}

	.text-title {
		font-size: 18px;
		color: #333333;
		font-weight: bold;
		margin: 0px 10px;
	}

	.line-vertical {
		height: 25px;
		width: 6px;
		border-radius: 10px;
		background: linear-gradient(135deg, #037eff 10%, #736EFE 100%);
		box-shadow: rgba(136, 165, 191, 0.48) 5px 2px 10px 0px, rgba(255, 255, 255, 0.8) -5px -2px 10px 0px;

	}



	.level {
		background-color: #edf4f3;
		color: #3db376;
		border-radius: 5px;
		padding: 5px 10px;
		font-size: 12px;
	}

	.line {
		height: 1px;
		background-color: #f8f8f8;
		margin: 12px 0px;
	}

	.text-answer {
		color: #333333;
		font-size: 16px;
		font-weight: bold;
	}

	.text-analysis {
		color: #666666;
		font-size: 14px
	}
</style>
