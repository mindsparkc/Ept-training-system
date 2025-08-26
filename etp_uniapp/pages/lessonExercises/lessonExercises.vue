<!-- 课后习题 -->
<template>
	<view>
		<view v-for="(item,index) in exercises" :key="index">
			<item-exercises :data="item" @saveAnswer='saveAnswer' :lessonId="lessonId">
			</item-exercises>
		</view>

	</view>
</template>

<script>
	export default {

		onLoad(option) {
			this.lessonId = option.lessonId;
			this.getLesson();
		},
		data() {
			return {
				lessonId: "",
				exercises: [],
				title:""
			}
		},
		methods: {
			saveAnswer(e){
				console.log("答案",JSON.stringify(e));
				let params = e;
				params.kcid=Number(this.lessonId);
				params.remake = this.title;
				//上传答题结果
			
				
				console.log(JSON.stringify(params));
					uni.$u.api.saveSubject(params).then(res => {
						console.log(res);
				
					}).catch(err => {
						console.log(err.errMsg);
					})
			},
			getLesson() {
				let that = this;
				let params = {
					id: this.lessonId
				}
				uni.$u.api.getLessonIndex(params).then(res => {
					if (res.data) {
						if (res.data) {
							this.title = res.data.mc;
							console.log("title",this.title);
							let index = res.data.tCurriculumContents;
							let exercises = [];
							if (index && index.length > 0) {
								//将课程与习题分开
								for (let i = 0; i < index.length; i++) {
									if (index[i].source_type == "st") {
										exercises.push(index[i])
									}
								}
							}
							for (let j = 0; j < exercises.length; j++) {
								that.getQuestionInfo(exercises[j].sid);
							}



						}


					}


				}).catch(err => {
					console.log(err);
				});
			},


			//获取练习题的信息
			getQuestionInfo(id) {
				uni.$u.api.getQuestionInfo(id).then(res => {

					if (res.data) {
						this.exercises.push(res.data)
					}

				}).catch(err => {
					console.log(err);
				})
			}
		}
	}
</script>

<style>

</style>
