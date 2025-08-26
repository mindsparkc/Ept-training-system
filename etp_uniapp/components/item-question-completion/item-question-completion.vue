<!-- 填空题答题组件 -->
<template>
	<view>
		<view class="text-answer-tip" v-if="!isShowAnswer">请在下方填写填空题答案</view>
		<view v-for="(item,index) in completionValues" :key="index">
			<view class="flex-row">
				<text class="text-no" v-if="completionValues!=null&&completionValues.length>1">{{index+1}}</text>

				<input type="text" :value="item.answered?item.answered:''" :placeholder="setCompletionTips(index)"
					@input="input($event,item)" class="k-width-full" v-if="!isShowAnswer" />
				<view class="k-text-lg k-text-grey k-flex-1 k-text-word-break-all" v-else>{{item.answered?item.answered:'未填写答案'}}</view>


			</view>




		</view>

	</view>
</template>

<script>
	export default {
		name: "item-question-completion",
		props: {
			completionValues: {
				type: Array,
				default: _ => [],
				required: true
			},
			//是否显示答案  若显示答案 则不可操作
			isShowAnswer: {
				type: Boolean,
				default: false
			},
		},
		data() {
			return {

			};
		},



		methods: {

			setCompletionTips(index) {
				let kong = this.completionValues.length;
				if (kong != null && kong == 1) {
					return "请输入您的答案";
				} else {
					let num = index + 1
					return "请输入第" + num + "个空的答案";
				}


			},

			input(e, item) {
				let value = e.detail.value;
				item.answered = value;
				this.$emit("completionInput", this.completionValues)
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

	.text-no {
		font-size: 16px;
		color: #666666;
		width: 60rpx;
		height: 60rpx;
		border-radius: 5px;
		background-color: #FFFFFF;
		margin-right: 20rpx;
		line-height: 60rpx;
		text-align: center;
		flex-shrink: 0;
	}


	.flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-top: 30rpx;
		background-color: #fbf7fe;
		border: solid 1px #f3eff6;
		border-radius: 10rpx;
		padding: 10px;
	}
</style>
