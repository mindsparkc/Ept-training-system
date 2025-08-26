<!-- 已迭代为新版本 此版本不再使用 -->
<!-- 课后练习习题总组件 -->
<template>
	<view class="page">
		<view class="row">
			<view class="line"></view>
			<text class="title">课后习题</text>
		</view>

		<view class="content">
			<item-question-check v-if="isCheck&&isLoaded" :data="question" @next="nextClick()" :isFinal="isFinal">
			</item-question-check>
			<item-question-input v-if="isInput&&isLoaded" :data="question" @next="nextClick()" :isFinal="isFinal">
			</item-question-input>
		</view>
	</view>
</template>

<script>
	export default {
		name: "item-question-lesson",
		props: {
			//习题详情
			question: {
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
				isCheck: false, //是否是选择题
				isInput: false, //是否是填空题
				isLoaded: false, //数据是否加载
			};
		},
		mounted() {
			if (this.question) {
				//若題目的类型为1单选2多选3判断  则展示选择题组件
				//类型 1-单选 ，2-复选，3-判断，4-填空，5-问答
				if (this.question.lx == "1" || this.question.lx == "2" || this.question.lx == "3") {
					this.isCheck = true;
				} else if (this.question.lx == "4" || this.question.lx == "5") { //否则展示填空题组件
					this.isInput = true;
				}
				this.isLoaded = true;
			}


		},
		methods: {
			//点击下一节
			nextClick() {
				if (this.isFinal) {
					uni.showToast({
						title: '本课程到此结束',
						icon: 'none'
					});
				} else {
					this.$emit("next", "习题");
				}

			}

		},


	}
</script>

<style lang="scss">
	.page {
		position: relative;
		margin-top: 24rpx;
		display: flex;
		flex-direction: column;
	}

	.row {
		display: flex;
		flex-direction: row;
		align-items: center;
	}

	.line {
		height: 25rpx;
		width: 6rpx;
		border-radius: 10rpx;
		margin-left: 30rpx;
		background-color: #037EFF;
	}

	.title {
		color: #303133;
		font-weight: bold;
		font-size: 32rpx;
		margin-left: 20rpx;
	}

	.content {
		margin-top: 24rpx;
	}
</style>
