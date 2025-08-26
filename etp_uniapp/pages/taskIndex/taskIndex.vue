<template>
	<view >
		<!-- 封面 -->
		<u--image width="100%" :src="teachCover(cover)" mode="widthFix" height="auto"></u--image>

		<!-- 详情 -->
		<view class="white-box-top">
			<text class="text-title">{{task.mc}}</text>


			<view class="margin-top-10 flex-row-center">
				<text class="text-undo">任务时间</text>
				<text class="text-time">{{task.taskBeginTime}}</text>
				<text class="text-sub">至</text>
				<text class="text-time">{{task.taskEndTime}}</text>
				<!-- 任务进度 -->
				<text class="text-complete" v-show="jd!='null'&&jd!=''&&jd">已完成{{Math.round(schedule.rate)}}%</text>
			</view>

			<text class="text-sub margin-top-10">{{task.ms?task.ms:""}}</text>
		</view>

		<!-- 目录 -->
		<view class="white-box-bottom">

			<text class="text-title">目录</text>
			<u-line margin="12rpx 0rpx"></u-line>


			<view v-for="(item,index) in task.sources" :key="index" class="flex-col" @click="onClick(index)">
				<view class="flex-row-center">
					<text class="bg-type">{{tranType(item.type)}}</text>
					<!-- 名称 -->
					<text :class="[item.id == clickIndexId?'text-title-click':'text-title']">{{item.name}}</text>
					<!-- <image class="small-image"></image> -->
				</view>
				<item-index :list="getLessonIndexList(item.sources)" @indexClick="onItemClick(arguments,item)" :itemClickId='clickIndexId'
					:currentStudyId="currentStudyId"></item-index>
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				jd: null, //任务进度字符串
				schedule: {}, //任务进度对象
				currentStudyId: -1, //最近学习的

				taskId: "",
				cover: "", //封面图
				task: {},
				clickIndexId: -1,
				// task: {
				// 	"title": "Vue3从入门到实战进阶式掌握完整知识体系进阶式掌握完整知识体系进阶式掌握完整知识体系",
				// 	"progress": "14",
				// 	"description": "指学校学生所应学习的学科总和及其进程与安排，课程是对教育的目标、教学内容、教学活动方式的规划和设计，是教学计划、教学大纲等诸多方面实施过程的总和。",
				// 	"state": 0,
				// 	"startDate": "",
				// 	"endDate": "",
				// 	"tags": ["UI", "PS"],
				// 	"cover": "https://cdn.uviewui.com/uview/swiper/swiper3.png",
				// 	"author": "张三",
				// 	"contents": [{
				// 			"coursewareId": "11",
				// 			"coursewareTitle": "浅谈Android",
				// 			"coursewareStudyDate": "1642994024716",
				// 			"coursewareState": 0,
				// 		},
				// 		{
				// 			"coursewareId": "11",
				// 			"coursewareTitle": "Android开发工具介绍与安装",
				// 			"coursewareStudyDate": "1642994024716",
				// 			"coursewareState": 1,
				// 		},
				// 		{

				// 			"coursewareId": "11",
				// 			"coursewareTitle": "Android基本组件介绍",
				// 			"coursewareStudyDate": "1642994024716",
				// 			"coursewareState": 2,
				// 		}

				// 	]
				// },
			}
		},
		onLoad(option) {
			if (option.jd!='null') {
				this.jd = option.jd;
				this.schedule = JSON.parse(this.jd)
				if (this.schedule.lastStudy) {
					this.currentStudyId = this.schedule.lastStudy.current;
				}

			}


			this.taskId = option.taskId;
			this.cover = option.cover;
			this.getTaskInfo(this.taskId);
		},



		methods: {
			getTaskInfo(taskId) {
				uni.showLoading({
					title: '加载中',
					mask: false
				});
				uni.$u.api.getTaskInfo(taskId).then(res => {

					uni.hideLoading();
					if (res.data) {
						this.task = res.data
					}
					// console.log("getTask", JSON.stringify(this.task));

				}).catch(err => {
					console.log(err);
				})

			},
			
			getLessonIndexList(index){
				let lessonIndexList = [];
				for (let i = 0; i < index.length; i++) {
					if (index[i].type == "kj") {
						lessonIndexList.push(index[i])
					} 
				}
				return lessonIndexList;
			},

			tranType(type) {
				switch (type) {
					case "kc":
						return "课程学习"
					case "ks":
						return "考试测验"
					default:
						return "其他"
				}
			},

			//点击目录若有最近学习的课程直接显示最近学习
			onClick(index) {
				let item = this.task.sources[index]
				this.clickIndexId = item.id;
				uni.setStorageSync(this.taskId, item.id);

				if (item.type == 'ks') { //若点击考试 则跳转到考试开始页面
					uni.navigateTo({
						url: '/pages/exam/examIndex/examIndex?examId=' + item.id + "&examTitle=" + item.name +
							'&taskId=' + this.task.id,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else { //点击的是课程 则跳转到课程详情  若所点击的课程中有课件是正在学习状态 则跳转到正在学习的课件
				let wares = item.sources;
				let currentIndex = -1;
				for (let i = 0; i < wares.length; i++) {
					if (wares[i].id==this.currentStudyId) {
						currentIndex = i;
					}
					
				}
				

					// uni.navigateTo({
					// 	url: '/pages/lesson/lesson?id=' + item.id + '&title=' + item.name + "&index="+currentIndex+"&taskId="+this.taskId,
					// 	success: res => {},
					// 	fail: () => {},
					// 	complete: () => {}
					// });
					uni.navigateTo({
						url: '/pages/lessonIndexInfo/lessonIndexInfo?id=' + item.id + '&title=' + item.name + "&taskId="+this.taskId,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				}

			},
			//点击二级列表 进入对应的课件或者习题
			onItemClick(val, item) {
				// console.log("》》》》》taskindex", val);
				this.clickIndexId = val[0];
				//将点击项的id保存  用于状态展示
				uni.setStorageSync(this.taskId, item.id);


				// uni.navigateTo({
				// 	url: '/pages/lesson/lesson?id=' + item.id + '&title=' + item.name + "&index=" + val[1]+'&taskId='+this.taskId,
				// 	success: res => {},
				// 	fail: () => {},
				// 	complete: () => {}
				// });
				uni.navigateTo({
					url: '/pages/lessonIndexInfo/lessonIndexInfo?id=' + item.id + '&title=' + item.name + "&index=" + val[1]+'&taskId='+this.taskId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F8F8F8;
	}

	.white-box-top {
		background-color: #FFFFFF;
		border-radius: 4rpx;
		padding: 20rpx;
		margin: 20rpx 30rpx;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;

	}

	.white-box-bottom {
		background-color: #FFFFFF;
		border-radius: 4rpx;
		padding: 16rpx 32rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-bottom: 20rpx;
	}

	.text-done {
		font-size: $uni-font-size-base;
		color: $uni-text-color-grey;

	}

	.text-doing {
		font-size: $uni-font-size-base;
		color: $uni-color-primary;
	}

	.text-undo {
		font-size: $uni-font-size-base;
		color: $uni-text-color;
	}
	
	.text-complete {
		font-size: $uni-font-size-sm;
		color: $uni-text-color-grey;
		margin-left: 20rpx;
		
	}

	.text-small {
		font-size: $uni-font-size-sm;
	}

	.row-center {
		display: flex;
		flex-direction: row;
		align-items: center;

	}

	.row-top {
		display: flex;
		flex-direction: row;
		align-items: flex-start;
		margin-top: 20rpx;

	}

	.tag-blue {
		background-color: rgba(0, 125, 255, 0.2);

		padding: 6rpx 10rpx 6rpx 10rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
		border-radius: 4rpx;
	}

	.small-image {
		width: 16rpx;
		height: 16rpx;
	}

	.bg-grey {
		border-radius: 50%;
		background-color: #E5E5E5;
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

	.line-col {
		width: 2rpx;
		background-color: #e5e5e5;
		height: 100%;

	}

	.text-title {
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
		font-weight: bold;
	}

	.text-title-click {
		font-size: $uni-font-size-lg;
		color: $uni-color-primary;
		font-weight: bold;
	}

	.text-sub {
		font-size: $uni-font-size-base;
		color: $uni-text-color-grey;
	}

	.text-time {
		background-color: rgba(254, 168, 1, 0.1);
		color: #FEA801;
		font-size: $uni-font-size-sm;
		padding: 3rpx 20rpx;
		border-radius: 10rpx;
	}

	.bg-type {
		background-color: rgba(254, 168, 1, 0.1);
		color: #FEA801;
		font-size: $uni-font-size-sm;
		padding: 3rpx 10rpx;
		flex-shrink: 0;
		border-radius: 10rpx;
		border: solid 2rpx #FEA801;
		margin-right: 24rpx;
		margin: 24rpx 24rpx 24rpx 0rpx;
	}


	.flex-col {
		display: flex;
		flex-direction: column;
		justify-content: flex-start;

	}

	.flex-col-start {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		flex: 1;
		margin: 0rpx 30rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		min-height: 150rpx;
		justify-content: space-between;
	}

	.flex-row-center {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.margin-top-10 {
		margin-top: 20rpx;
	}
</style>
