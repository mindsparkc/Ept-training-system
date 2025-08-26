<!-- 已迭代为新版本 此版本不再使用 -->
<!-- 控制课程详情展示总组件 -->
<template>
	<view>

		<!-- office文件播放组件1 预览或下载即为学习-->
		<!-- <item-office v-if="!isExam&&courseware.xs == '1'&&isLoaded" :coursewareInfo="courseware" :isFinal="isFinal"
			@next="next" @study="updateSchedule"></item-office> -->
		<!-- 视频组件2 点击播放视为学习-->
		<item-video v-if="!isExam&&courseware.xs == '2'&&isLoaded" :coursewareInfo="courseware" :isFinal="isFinal"
			@next="next" @study="updateSchedule">
		</item-video>
		<!-- 富文本组件 3 调用查询课件接口即视为学习-->
		<item-richtext v-if="!isExam&&courseware.xs == '3'&&isLoaded" :coursewareInfo="courseware" :isFinal="isFinal"
			@next="next"></item-richtext>
		<!-- 外部链接组件 4 点击链接或者点击复制链接视为学习-->
		<item-webview v-if="!isExam&&courseware.xs == '4'&&isLoaded" :coursewareInfo="courseware" :isFinal="isFinal"
			@next="next" @openUrl="openUrl" @study="updateSchedule"></item-webview>
		<!-- 文件下载组件 5 同office-->
		<!-- <item-office v-if="!isExam&&courseware.xs == '5'&&isLoaded" :coursewareInfo="courseware" :isFinal="isFinal"
			@next="next" @study="updateSchedule"></item-office> -->

		<!-- 练习题  source_type为ks  考试 -->
		<item-question-lesson v-if="isExam&&isLoaded" :question="question" @next="next()" :isFinal="isFinal">
		</item-question-lesson>



	</view>
</template>

<script>
	export default {
		props: {

			lesson: {
				type: Object,
				default: function() {
					return {}
				}
			},
			//课件学习进度
			studyScheduleIndex: {
				type: Number,
				default: function() {
					return -1
				}
			},
			//任务id 若有则为任务中的课程  进度更新时更新任务的进度
			taskId: {
				type: Number,
				default: function() {
					return -1
				}
			},

		},



		data() {
			return {
				courseware: {
					// "id": 123,
					// "mc": "全面支持主流的开源数据库及商业数据以及消息",
					// "fm": "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-1acbc9a0-a6e8-45cc-a231-308cbd47885e/612fc63e-93cb-40b1-9226-77c9963ab388.zip",
					// "xs": "5",
					// "lj": "https://www.baidu.com",
					// "nr": "<div>Hello World!</div><div>Hello World!</div><div>Hello World!</div><div>Hello World!</div>",
					// "kjdz": "https://cdn.uviewui.com/uview/resources/video.mp4",
					// "kjwjmc": "course.jpg",
					// "learnTime": "25",
					// "kfcd": "1",
					// "delFlag": "1",
					// "createBy": "admin",
					// "createTime": "2021-12-22 10:19:20"

				}, //课件详情

				question: {}, //练习题详情

				isExam: false, //是否是习题
				isFinal: false, //是否最后一节课
				isLoaded: false, //课件详情是否请求成功 

				currentIndex: -1, //当前正在学习的课件位置
			};
		},

		mounted() {
			// this.getStudySchedule();

		},
		watch: {

			studyScheduleIndex: {
				handler(val) {
					this.studyScheduleIndexChange();
				},
				deep: true,
				immediate: true //watch只有在值变化时才会触发,所以第一次是不会监听值的变化,如果要在侦听开始之后被立即调用(子组件props首次获取到父组件传来的默认值时),设置immediate: true即可.
			}
		},
		methods: {
			getCurrectStudyIndex() {
				//若为任务中的课程 则取currentId
				let currentStudyId = -1;
				let sourceType = "";
				if (this.taskId && this.taskId != -1) {
					currentStudyId = this.lesson.currentId ? this.lesson.currentId : -1;
					let kcjd = this.lesson.kcjd;
					if (kcjd) {
						let jd = JSON.parse(kcjd);
						if (jd.lastStudy) {
							sourceType =  jd.lastStudy.source_type;
						}
					}
				} else {
					//正在学习的课件index
					let kcjd = this.lesson.kcjd;
					if (kcjd) {
						let jd = JSON.parse(kcjd);
						if (jd.lastStudy) {
							currentStudyId = jd.lastStudy.current;
							sourceType =  jd.lastStudy.source_type;
						}
					}
				}

				let curriculums = this.lesson.tCurriculumContents;
				for (let i = 0; i < curriculums.length; i++) {
					if (curriculums[i].sid == currentStudyId&&curriculums[i].source_type==sourceType) {
						this.currentIndex = i;
					}
				}
				console.log("获取当前正在学习的位置currentStudyId",currentStudyId);
				console.log("获取当前正在学习的位置",this.currentIndex);
				if (this.currentIndex == -1) {
					this.currentIndex = 0
				}



			},

			studyScheduleIndexChange() {
				this.getCurrectStudyIndex();
				//如果studyScheduleIndex 不为0则证明是目录点击跳转的
				if (this.studyScheduleIndex != -1) {
					this.currentIndex = this.studyScheduleIndex;
				}
				if (this.currentIndex == -1) {
					this.currentIndex = 0;
				}
				this.isLoaded = false;
				if (this.currentIndex == (this.lesson.tCurriculumContents.length - 1)) {
					this.isFinal = true;

				} else {
					this.isFinal = false;
				}
				this.getStudySchedule();

			},
			//根据学习进度 读取课件信息  
			getStudySchedule() {
				if (this.lesson.tCurriculumContents) {
console.log("根据学习进度获取课件信息",this.currentIndex);
					let currectStudy = this.lesson.tCurriculumContents[this.currentIndex];
					if (currectStudy.source_type == "st") {
						this.isExam = true;
						//加载习题相关内容
						this.getQuestionInfo(currectStudy.sid);
					} else {
						this.isExam = false;
						//加载课件相关内容
						this.getCoursewareInfo(currectStudy.sid);
					}
				}

			},

			//获取课件信息
			getCoursewareInfo(id) {

				uni.$u.api.getCourswareInfo(id).then(res => {

					if (res.data) {

						this.courseware = res.data;
						this.isLoaded = true;
						//若课件类型为 富文本 则直接更新学习进度
						if (this.courseware.xs == '3') {
							this.updateSchedule(this.courseware.id)
						}

					}


				}).catch(err => {
					console.log(err);
				})
			},

			//获取练习题的信息
			getQuestionInfo(id) {

				uni.$u.api.getQuestionInfo(id).then(res => {

					// console.log("获取练习题的信息", JSON.stringify(res.data));
					if (res.data) {
						this.isLoaded = true;
						this.question = res.data;
					}

				}).catch(err => {
					console.log(err);
				})
			},

			//子组件点击下一步
			next(data) {

				this.$emit("nextClick", this.currentIndex);
			},
			//更新进度
			updateSchedule(studyId) {

				if (this.taskId && this.taskId != -1) {
					this.updateTaskSchedule(studyId)
				} else {
					this.updateLessonSchedule(studyId)
				}


			},
			//更新课程进度
			updateLessonSchedule(studyId) {
				let currIds = [];
				let ids = this.lesson.tCurriculumContents;
				for (var i = 0; i < ids.length; i++) {
					if (ids[i].source_type == "kj") {
						currIds.push(ids[i].sid)
					}

				}
				let contentIds = currIds.join(",");



				let params = {
					id: this.lesson.id,
					jd: studyId,
					contentIds: contentIds
				}
				console.log(">>>>>>>", JSON.stringify(params));

				uni.$u.api.updateStatus(params).then(res => {

					let rate = res.data;
					if (rate == 100) { //全部课程已经学完

					}

				}).catch(err => {
					console.log(err.errMsg);
				})


			},
			//更新任务进度
			updateTaskSchedule(studyId) {

				let params = {
					rwid: this.taskId,
					kjid: studyId,
				}

				uni.$u.api.updateTaskStatus(params).then(res => {
					let rate = res.data;
					if (rate == 100) {
						// uni.showToast({
						// 	title: '太棒啦，任务完成啦'
						// });
					}


				}).catch(err => {
					console.log(err.errMsg);
				})
			},


			//打开链接
			openUrl() {
				uni.navigateTo({
					url: '/pages/common/webview/webview?url=' + this.courseware.lj,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}

		}
	}
</script>

<style>


</style>
