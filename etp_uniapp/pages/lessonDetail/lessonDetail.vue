<!-- 课程详情 暂时 -->
<template>
	<view>
		<!-- 课程目录组件 -->
		<lesson-index v-if="isLoaded" :lesson="lesson" :itemClickId="itemClickId" @indexClick="indexClick"
			:currentStudyId="currentStudyId">
		</lesson-index>
	</view>
</template>

<script>
	export default {
		onLoad(options) {
			this.lessonId = options.id;
			this.getLesson();
		},
		data() {
			return {
				lessonId: -1,
				isLoaded: false,

			}
		},
		methods: {
			//根据课程id获取课程目录和详细信息
			getLesson() {

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

					if (res.data) {
						this.lesson = res.data
						this.isLoaded = true;
					}


				}).catch(err => {
					console.log(err);
				})
			}
		}
	}
</script>

<style>
	page {
		background-color: #f8f8f8;
	}
</style>
