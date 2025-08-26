<template>
	<view class="content">

		<!-- 不是任务 并且没有领取的时候显示提示框 -->
		<view class="top" v-show="!isReceived">

			<text>本课程为免费课程，领取后可在我的课程中查看</text>
			<view class="btn" hover-class="u-hover-class" @click="receiveLesson()">免费领取</view>
		</view>
		<!-- 课件总组件包括习题 -->
		<lesson-info v-if="tabIndex==0&&isLoaded" :lesson="lesson" :studyScheduleIndex="studyScheduleIndex"
			@nextClick="nextClick" :taskId="taskId">
		</lesson-info>
		<!-- 课程目录组件 -->
		<lesson-index v-if="tabIndex==1&&isLoaded" :lesson="lesson" :itemClickId="itemClickId" @indexClick="indexClick"
			:currentStudyId="currentStudyId">
		</lesson-index>

		<u-loading-page loading-text="加载中..." :loading="!isLoaded"></u-loading-page>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				firstLoaded: false,
				lesson: {},
				isLoaded: false, //数据是否加载
				isReceived: true, //是否已经领取 默认已经领取 不显示领取按钮

				itemClickId: -1, //目录选中id
				currentStudyId: -1, //正在学习的id
			}
		},
		mounted() {

		},

		props: {
			studyScheduleIndex: {
				type: Number,
				default: function() {
					return -1
				}
			},
			knowledgeIndex: {
				type: Number,
				default: function() {
					return 0
				}
			},


			//课程id
			lessonId: {
				type: Number,
				default: function() {
					return -1
				}

			},
			//任务id
			taskId: {
				type: Number,
				default: function() {
					return -1
				}
			},
			//当前组件的index，也就是当前组件是swiper中的第几个
			tabIndex: {
				type: Number,
				default: function() {
					return 0
				}
			},
			//当前swiper切换到第几个index
			currentIndex: {
				type: Number,
				default: function() {
					return 0
				}
			}
		},
		watch: {
			tabIndex: {
				handler(val) {},
				immediate: true

			},

			currentIndex: {
				handler(newVal) {
					if (newVal === this.tabIndex) {
						//懒加载，当滑动到当前的item时，才去加载
						this.getLesson();

					}
				},
				immediate: true
			},

			studyScheduleIndex: {
				handler(val) {
					// console.log("studyScheduleIndex  lesson", JSON.stringify(this.lesson));

					if (val != -1 && this.lesson && this.lesson.tCurriculumContents) {
						this.itemClickId = this.lesson.tCurriculumContents[val].sid
					}


				},
				immediate: true

			},
		},



		methods: {

			//根据课程id获取课程目录和详细信息
			getLesson() {
				console.log("<<<<<<<<<<<lessonId", this.lessonId);

				if (this.lessonId == null || this.lessonId == -1) {
					return;
				}
				let params = {
					id: this.lessonId
				}
				//如果是任务中的课程 
				if (this.taskId && this.taskId != -1) {
					params.rwid = this.taskId;
				}

				uni.$u.api.getLessonIndex(params).then(res => {
					this.firstLoaded = true;
					this.isLoaded = true;
					if (res.data) {
						this.lesson = res.data
						let status = this.lesson.getStatus;
						if (status == "1") { //已经领取
							this.isReceived = true;
						} else {
							this.isReceived = false;
							if (this.taskId && this.taskId != -1) { //任务中的课程不显示领取提示
								this.isReceived = true;
							}
						}
						let jd = {};
						if (this.lesson.kcjd) {
							jd = JSON.parse(this.lesson.kcjd);
						}

						//判断是否是任务中的课程 取currentId
						if (this.taskId && this.taskId != -1) { //任务中的课程
							this.currentStudyId = this.lesson.currentId ? this.lesson.currentId : -1;
							if (this.studyScheduleIndex && this.studyScheduleIndex == -
								1) { //若studyId为-1即不是点击目录而来 那么目录选中的id等于当前进度中的id
								this.itemClickId = this.currentStudyId == -1 ? this.lesson.tCurriculumContents[0]
									.sid : this.currentStudyId;
							} else {
								this.itemClickId = this.lesson.tCurriculumContents[this.studyScheduleIndex].sid;
							}

						} else { //普通课程
							console.log(">>>studyScheduleIndex", this.studyScheduleIndex);
							if (this.studyScheduleIndex && this.studyScheduleIndex == -1) {
								if (jd.lastStudy) {
									this.itemClickId = jd.lastStudy.current;
									this.currentStudyId = jd.lastStudy.current;
								} else { //没有进度  从第一个开始
									this.itemClickId = this.lesson.tCurriculumContents[0].sid;
								}
							} else {
								this.itemClickId = this.lesson.tCurriculumContents[this.studyScheduleIndex].sid;
							}
						}
						console.log(">>>currentStudyId", this.currentStudyId);


					}


				}).catch(err => {
					console.log(err);
				})
			},
			//目录点击事件
			indexClick(index) {
				//控制tab切换到详情页
				this.$emit("changeIndex", 0, Number(index));
			},
			nextClick(currentIndex) {
				if (this.isReceived) {
					this.$emit("nextClick", currentIndex);
				} else {
					uni.showModal({
						title: '温馨提示',
						content: '您还没有领取本课程 是否立即领取',
						showCancel: true,
						cancelText: '下次再说',
						confirmText: '立即领取',
						success: res => {
							if (res.confirm) {
								//领取
								this.receiveLesson()
							} else {
								//用户点击下次再说  将标志位先置为true 防止重复提醒
								this.isReceived = true;
								this.$emit("nextClick", currentIndex);
							}

						},
						fail: () => {},
						complete: () => {}
					});
				}


			},
			//领取课程
			receiveLesson() {
				uni.showLoading({
					title: '领取中...',
					mask: false
				});

				let params = {
					kcid: this.lessonId
				}

				uni.$u.api.receiveLesson(params).then(res => {
					uni.hideLoading();
					if (res.code == 200) {
						this.isReceived = true;
						getApp().globalData.knowledgeIndex = this.knowledgeIndex;

						uni.showToast({
							title: '课程领取成功'
						});
					}

				}).catch(err => {
					uni.hideLoading();
					console.log("receiveLesson", err.errMsg);
				})
			}



		}
	}
</script>

<style lang="scss">
	.content {
		// display: flex;
		// flex-direction: column;
	}

	.item {
		position: relative;
		height: 150rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0rpx 30rpx;
	}

	.cover-blue {
		background-color: rgba(0, 125, 255, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 4rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;
	}


	.cover-red {
		background-color: rgba(221, 11, 11, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 4rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #DD0B0B;
		font-size: $uni-font-size-ssm;
	}

	.cover-green {
		background-color: rgba(17, 188, 77, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 4rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #11BC4D;
		font-size: $uni-font-size-ssm;
	}

	.list-box {
		display: flex;
		flex-direction: column;
		background-color: #FFFFFF;
		border-radius: 4rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-bottom: 30rpx;

	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		margin: 24rpx 20rpx 24rpx 20rpx;
	}

	.ell-text {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		color: $uni-text-color;
		margin-top: 26rpx;
		margin-left: 26rpx;
		font-size: $uni-font-size-base;
	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-ssm;
	}

	.top {
		background-color: rgba(253, 246, 236, 0.7);
		border-radius: 10rpx;
		padding: 10rpx 30rpx;
		margin-left: 30rpx;
		margin-top: 24rpx;
		margin-right: 30rpx;
		margin-bottom: 10rpx;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		font-size: 24rpx;
		color: rgb(249, 174, 61);
		z-index: 99;
	}

	.btn {
		background-color: rgb(249, 174, 61);
		font-size: 20rpx;
		color: white;
		margin-left: 60rpx;
		border-radius: 20rpx;
		flex-shrink: 0;
		padding: 6rpx 10rpx;
	}
</style>
