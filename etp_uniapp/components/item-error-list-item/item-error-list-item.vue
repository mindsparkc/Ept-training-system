<template>
	<view class="answer-grid">
		<view v-for="(item,index) in list" :key="index" @click="onClickAnswerCardItem(item.serialNum)">
			<view :class="[item.serialNum==currentSerialNum?'answer-done':'answer-undo']">{{item.serialNum}}</view>


		</view>
	</view>
</template>

<script>
	export default {
		name: "item-error-list-item",
		props: {

			currentSerialNum: {
				type: Number,
				default: -1
			},


			list: {
				type: Array,
				default: []
			}

		},
		data() {
			return {

				onClickAnswerCardItem(serialNum) {
					this.$emit("clickItem", serialNum);
				}
			};
		}
	}
</script>

<style>
	.answer-grid {
		display: flex;
		flex-direction: row;
		align-items: center;
		flex-wrap: wrap;
		justify-content: flex-start;
	}


	.answer-done {
		border-radius: 50%;
		background-color: #007DFF;
		width: 80rpx;
		height: 80rpx;
		color: #FFFFFF;
		margin: 16rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.answer-undo {
		border-radius: 50%;
		background-color: rgba(0, 125, 255, 0.1);
		width: 80rpx;
		height: 80rpx;
		margin: 17rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		color: #007DFF;

	}


	.answer-undo-select {
		border: solid 2rpx #007DFF;
		border-radius: 50%;
		background-color: #rgba(0, 125, 255, 0.2);
		width: 80rpx;
		height: 80rpx;
		text-align: center;
		vertical-align: middle;
		color: #007DFF;
	}
</style>
