<!-- 任务模块的目录 -->
<template>
	<view>
		<view v-for="(item,index) in groupList" :key="index" class="flex-row"
			@click.stop="onClick(item.id,item.oldIndex)">
			<view class="flex-col-center">
				<!-- 图标 -->
				<view :class="[changeBg[state(item)]]">

					<image :src="changeImg[state(item)]" class="small-image"></image>

				</view>


				<view class="line-col" v-show="index != (groupList.length-1)"></view>
			</view>
			<!-- 名称 -->
			<view class="flex-col-start" >

				<view
					style="display: flex;flex-direction: row;align-items: center;justify-content: space-between;width: 100%;">

					<text :class="textStyle[state(item)]">{{item.type=='st'?'习题': item.name}}</text>

				<!-- 	<view v-if="item.exercises" @click.stop="onClickExercises(index)"
						style="display: flex;align-items: center;" hover-class="u-hover-class"> -->
						<view v-if="false" @click.stop="onClickExercises(index)"
							style="display: flex;align-items: center;" hover-class="u-hover-class">
						<text class="text-exem-title">习题({{item.exercises.length}})</text>
						<image v-if="item.isExpand" src="../../static/lesson/icon-arrow-up-bold.png" class="img-arrow">
						</image>
						<image v-else src="../../static/lesson/icon-arrow-down-bold.png" class="img-arrow"></image>

					</view>
				</view>
				<text v-if="item.id == currentStudyId" class="tag-blue">最近学习</text>
				<view style="margin-top: 10rpx; margin-bottom: 30rpx;" v-if="false">
					<view v-for="(exeItem,exeIndex) in item.exercises" v-if="item.exercises&&item.isExpand"
						:key="exeIndex" style="margin: 10rpx 0rpx 0rpx 30rpx;"
						@click.stop="onClick(exeItem.id,exeItem.oldIndex)">
						<text
							:class="[exeItem.id == itemClickIndexId?'text-exem-title-click':'text-exem-title']">课后习题【{{exeIndex+1}}】</text>

					</view>

				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "item-index",
		props: {
			list: {
				type: Array,
				default: []
			},
			itemClickId: {
				type: Number,
				default: -1
			},
			currentStudyId: {
				type: Number,
				default: -1
			},
		},
		watch: {
			itemClickId: 'itemClickIdChanged'

		},
		data() {
			return {
				groupList: [],
				itemClickIndexId: -1, //点击的id
				// 0已学完：绿色  1未学习：黑色   2点击：蓝色
				changeBg: [
					'bg-grey',
					'bg-undo',
					'bg-blue',
				],
				textStyle: [
					'text-done',
					'text-undo',
					'text-doing',
				],

				changeImg: [
					"../../static/common/icon-yes.png",
					"../../static/lesson/icon-played.png",
					"../../static/lesson/icon-playing.png",
				],
			};
		},

		mounted() {
			//对list进行重组 若类型为课程的课件下有习题节点，则将习题节点归属到此课件下，若习题上方没有课件节点 则自己归属一类
			let temp = [];
			//判断课件节点下方有没有习题 直到遇到下一个课件
			let lessonIndex = -1; //记录课件的位置

			for (let i = 0; i < this.list.length; i++) {
				this.list[i].oldIndex = i; //保存在原列表中的节点
				if (this.list[i].type == "kj") { //课件

					lessonIndex = temp.push(this.list[i]) - 1;
				} else { //习题  
					if (lessonIndex != -1) { //有课件节点 则加入最近的一个课件节点下
						if (temp[lessonIndex].exercises == null || temp[lessonIndex].exercises == 'undefined') {
							temp[lessonIndex].exercises = [];
						}
						temp[lessonIndex].exercises.push(this.list[i]);
						temp[lessonIndex].isExpand = false; //默认不展开

					} else { //没有课件节点  则自己为主节点
						temp.push(this.list[i])
					}

				}

			}
			this.groupList = temp;

			// console.log(JSON.stringify(this.groupList));
		},
		methods: {
			//状态  0已学完：绿色  1未学习：黑色   2点击：蓝色
			state(item) {
				let state = 1; //默认未学习
				if (item.rate && item.rate == 100) { //已学习
					state = 0;
				} else {
					state = 1; //未学完
				}	
							
				if (item.id == this.itemClickIndexId) { //点击
					state = 2;
				}

				// console.log("状态：》》》》》》》》》》》》", state);
				return state;
			},

			//点击目录
			onClick(id, index) {
				// console.log(">>>>>>>>>>>>>>>>>>>>>", JSON.stringify(this.groupList));
				this.itemClickIndexId = id;
				this.$emit("indexClick", id, index);
			},

			onClickExercises(index) {
				let item = this.groupList[index];
				item.isExpand = !item.isExpand;
				this.groupList.splice(index, 1, item);

			},
			itemClickIdChanged() {
				this.itemClickIndexId = this.itemClickId;
			}

		}
	}
</script>

<style lang="scss">
	.bg-grey {
		border-radius: 50%;
		background-color: rgba(24, 227, 160, 0.1);
		width: 40rpx;
		height: 40rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-shrink: 0
	}

	.bg-blue {
		border-radius: 50%;
		background-color: #EBF5FF;
		width: 40rpx;
		height: 40rpx;
		flex-shrink: 0;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.bg-undo {
		border-radius: 50%;
		background-color: rgba($color: #707070, $alpha: 0.1);
		width: 40rpx;
		height: 40rpx;
		flex-shrink: 0;
		display: flex;
		justify-content: center;
		align-items: center;

		// border: 10rpx solid #FFFFFF;
		// width: 20rpx;
		// height: 20rpx;
		// flex-shrink: 0;

	}



	.line-col {
		width: 2rpx;
		background-color: rgba(24, 227, 160, 0.1);
		flex-grow: 1;
		// min-height: 0rpx;
		// height: 20rpx;

	}

	.text-done {
		font-size: $uni-font-size-base;
		color: #909199;


	}

	.text-doing {
		font-size: $uni-font-size-base;
		color: $uni-color-primary;

	}

	.text-undo {
		font-size: $uni-font-size-base;
		color: $uni-text-color;

	}

	.text-small {
		font-size: $uni-font-size-sm;
	}

	.text-exem-title {
		font-size: $uni-font-size-sm;
		color: #606266;
	}

	.text-exem-title-click {
		font-size: $uni-font-size-sm;
		color: $uni-color-primary;
	}

	.flex-col-center {
		display: flex;
		flex-direction: column;
		align-items: center;
		min-height: 80rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		min-height: 80rpx;
		justify-content: space-between;
	}

	.small-image {
		width: 20rpx;
		height: 20rpx;
	}

	.img-arrow {
		width: 32rpx;
		height: 32rpx;
	}

	.flex-col-start {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		flex: 1;
		margin: 0rpx 30rpx;
	}

	.tag-blue {
		background-color: rgba(0, 125, 255, 0.1);
		padding: 3rpx 10rpx;
		color: $uni-color-primary;
		font-size: 20rpx;
		border-radius: 4rpx;
	}
</style>
