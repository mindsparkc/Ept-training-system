<template>
	<view class="root">
		<!-- 封面 -->
		<image :src="teachCover(cover)" mode="aspectFill"
			style="filter: blur(2px);width: 100%;height: 150px;position: absolute;z-index: -2;"></image>
		<view style="width: 100%;height: 150px;background-color: rgba(0, 0, 0, 0.5);position: absolute;z-index: -1;">
		</view>

		<!-- 详情 -->
		<view class="white-box-top">
			<text class="text-title">{{task.mc}}</text>

			<view class="margin-top-10 flex-row-center">
				<text class="text-undo">任务时间</text>
				<text class="text-time">{{task.taskBeginTime}}</text>
				<text class="text-sub">至</text>
				<text class="text-time">{{task.taskEndTime}}</text>

			</view>
			<!-- 任务进度 -->
			<text class="text-complete" v-show="jd!='null'&&jd!=''&&jd">已完成{{Math.round(schedule.rate)}}%</text>
			<text class="text-sub margin-top-10">{{task.ms?task.ms:""}}</text>
		</view>
		<view
			style="z-index: 100;position: sticky;top :0;background-color: white;border-top-right-radius: 15px;border-top-left-radius: 15px;">
			<u-tabs :list="steps" @click="click" scrollable :current="currentIndex" keyName="name" :activeStyle="{
            color: '#303133',
            fontWeight: 'bold',
            transform: 'scale(1.05)'
        }" :inactiveStyle="{
		            color: '#606266',
		            transform: 'scale(1)'
		        }" itemStyle="padding-left: 15px; padding-right: 15px; height: 50px;"
				></u-tabs>
		</view>

		<view v-for="(item, index) in steps" :key="index" style="margin: 15px;">


			<view :class="'tabItem'+index" style="font-size: 16px;color: #303133;font-weight: bold;background: linear-gradient(90deg, #FFC566 0%, rgba(255, 175, 0, 0) 100%);
		border-radius: 5px;padding: 5px 10px;">{{item.name}}</view>

			<view v-for="(childrenItem,childrenIndex) in item.children" :key="childrenIndex"
				style="background-color: white;border-radius: 2px;margin-top: 10px;   box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);padding: 10px;">
				<view style="line-height: 1.5em;"><text style="font-size: 12px;border-radius: 5px;color: white;margin-right: 10px;
				background-color: rgba(254, 168, 1, 0.1);
				color: #FEA801;
				padding: 3rpx 10rpx;
				border-radius: 10rpx;
				border: solid 2rpx #FEA801;
				">{{childrenItem.type}}</text>{{childrenItem.projectName}}</view>

			</view>

		</view>


		<!-- 目录 -->
		<view class="white-box-bottom" v-show="false">

			<text class="text-title">目录</text>
			<u-line margin="12rpx 0rpx"></u-line>


			<view v-for="(item,index) in task.sources" :key="index" class="flex-col" @click="onClick(index)">
				<view class="flex-row-center">
					<text class="bg-type">{{tranType(item.type)}}</text>
					<!-- 名称 -->
					<text :class="[item.id == clickIndexId?'text-title-click':'text-title']">{{item.name}}</text>
					<!-- <image class="small-image"></image> -->
				</view>
				<item-index :list="getLessonIndexList(item.sources)" @indexClick="onItemClick(arguments,item)"
					:itemClickId='clickIndexId' :currentStudyId="currentStudyId"></item-index>
			</view>

		</view>
	</view>
</template>

<script>
	
	export default {

		data() {
			return {
				 lineBg :
					"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAOCAYAAABdC15GAAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAFxSURBVHgBzZNRTsJAEIb/WTW+lpiY+FZPIDew3ABP4GJ8hxsI9zBpOYHeQDwBPQI+mRiRvpLojtPdYhCorQqF/6GdbGd2vvwzBXZcNAt4oj1ANeUoAT5iqkUjbEFLHNmhD1YPEvpZ3ghkGlVDCkc94/BmHMq998I5ONiY1ZBfpKAyuOtgAc5yOEDmYEWNh32BHF91sGHZHmwW4azciN9aQwnz3SJEgOmte+R2tdLprTYoa50mvuomlLpD4Y3oQZnov6D2RzCqI93bWOHaEmAGqQUyRBlZR1WfarcD/EJ2z8DtzDGvsMCwpm8XOCfDUsVOCYhiqRxI/CTQo4UOvjzO7Pow18vfywneuUHHUUxLn55lLw5JFpZ8bEUcY8oXdOLWiHLTxvoGpLqoUmy6dBT15o/ox3znpoycAmxUsiJTbs1cmxeVKp+0zmFIS7bGWiVghC7Vwse8jFKAX9eljh4ggKLLv7uaQvG9/F59Oo2SouxPu7OTCxN/s8wAAAAASUVORK5CYII=",
				currentIndex: 0, //当前选中tab标签的索引

				anchorDistances: [], //各个锚点距离顶部的距离

				jd: null, //任务进度字符串
				schedule: {}, //任务进度对象
				currentStudyId: -1, //最近学习的

				taskId: "",
				cover: "", //封面图
				task: {},
				clickIndexId: -1,

				steps: [{
						name: "环节1",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是一个考试",
								type: "考试",
							}

						]

					},
					{
						name: "环节2",
						children: [{
								projectName: "我是问卷的名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "问卷",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}

						]
					},
					{
						name: "环节3",
						disabled: false,
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}

						]
					},
					{
						name: "环节4",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}

						]
					},
					{
						name: "环节5",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}
					
						]
					},
					{
						name: "环节6",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}
					
						]
					},
					{
						name: "环节7",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}
					
						]
					},
					{
						name: "环节8",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}
					
						]
					},
					{
						name: "环节9",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							}
					
						]
					},
					
					{
						name: "环节10",
						children: [{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},{
								projectName: "我是课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
							{
								projectName: "我是另一个课程名称我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长我的名字超级的长",
								type: "课程",
							},
					
						]
					},

				],


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
			if (option.jd != 'null') {
				this.jd = option.jd;
				this.schedule = JSON.parse(this.jd)
				if (this.schedule.lastStudy) {
					this.currentStudyId = this.schedule.lastStudy.current;
				}

			}


			this.taskId = option.taskId;
			this.cover = option.cover;
			this.getTaskInfo(this.taskId);


			//记录各个锚点与顶部的距离
			let that = this;


			this.$nextTick(() => {
				for (let i = 0; i < that.steps.length; i++) {
					uni.createSelectorQuery().in(that).select('.tabItem' + i).boundingClientRect(data => {
						// console.log("得到布局位置信息" + JSON.stringify(data));
						console.log(i + "节点离页面顶部的距离为" + data.top);

						let anchorDistance = {
							anchorIndex: i,
						 anchorTop: data.top
						}
						that.anchorDistances.push(anchorDistance)

					}).exec();
				}
			})






		},
		onPageScroll(e) {
			//锚点改变tab样式  思路：onLoad时 记录各个锚点与顶部的距离。watch里监听tab切换，onPageScroll里监听滑动 计算保存的锚点距离与实际滑动距离 实现tab样式切换


			let scrollTop = e.scrollTop + 40; //页面滚动的距离 fixme

			//生成高度区间数组
			// this.anchorDistances.forEach((item,index)=>{
			// 	let arr = []
			// 	arr.push(item.anchorTop)
			// 	arr2 = arr2.concat(arr)
			// })
			console.log("当前scrollTop", scrollTop);
			//控制高度区间
			for (let i = 0; i < this.anchorDistances.length; i++) {
			
				if (this.anchorDistances[i + 1]&&scrollTop < (this.anchorDistances[i + 1].anchorTop)) {


					this.currentIndex = this.anchorDistances[i].anchorIndex;
					console.log("切换");
					break;
				}

			}


		},


		methods: {

			click(e) {
				console.log("点击index", e);
				let index = e.index
				// this.currentIndex = index;
				//锚点  将所按按钮对应的内容滑动到顶部
				//从当前位置到达目标位置
				uni.createSelectorQuery().in(this).select('.tabItem' + index).boundingClientRect(data => { //目标位置的节点：类或者id
					uni.createSelectorQuery().in(this).select(".root").boundingClientRect(
						res => { //最外层盒子的节点：类或者id

							if (data.top > res.top) { //目标位置在屏幕下 点击上滑
								res.top = res.top + 30;

							} else { //目标位置在屏幕上方 点击下滑
								res.top = res.top - 30;
							}

							uni.pageScrollTo({
								duration: 100, //过渡时间
								scrollTop: data.top - res.top, //到达距离顶部的top值
							})
						}).exec()
				}).exec();



			},


			getTaskInfo(taskId) {
				uni.showLoading({
					title: '加载中',
					mask: false
				});
				uni.$u.api.getTaskInfo(taskId).then(res => {

					uni.hideLoading();
					if (res.data) {
						this.task = res.data
						
						uni.setNavigationBarTitle({
							title:this.task.mc
						})
						
					}
					// console.log("getTask", JSON.stringify(this.task));

				}).catch(err => {
					console.log(err);
				})

			},

			getLessonIndexList(index) {
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
						if (wares[i].id == this.currentStudyId) {
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
						url: '/pages/lessonIndexInfo/lessonIndexInfo?id=' + item.id + '&title=' + item.name +
							"&taskId=" + this.taskId,
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
					url: '/pages/lessonIndexInfo/lessonIndexInfo?id=' + item.id + '&title=' + item.name +
						"&index=" + val[1] + '&taskId=' + this.taskId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style lang="scss">
	page {}

	.root {
		// background-color: #F8F8F8;
	}

	.white-box-top {
		position: relative;
		// background-color: #FFFFFF;
		// border-radius: 4rpx;
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
		color: white;
	}

	.text-complete {
		font-size: $uni-font-size-sm;
		color: white;
		// margin-left: 20rpx;

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
		color: white;
		font-weight: bold;
	}

	.text-title-click {
		font-size: $uni-font-size-lg;
		color: $uni-color-primary;
		font-weight: bold;
	}

	.text-sub {
		font-size: $uni-font-size-base;
		color: white;
	}

	.text-time {
		background-color: rgba(254, 168, 1, 0.1);
		color: white;
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



	.div-absoult {
		background: linear-gradient(90deg, #FFC566 0%, rgba(255, 175, 0, 0) 100%);
		border-radius: 8px;
		height: 50rpx;
		width: 80rpx;
		position: absolute;
		top: 0px;
		z-index: -1;
	}
</style>
