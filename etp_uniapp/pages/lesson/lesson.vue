<template>
	<view class="page" :style="[swiperStyle]">

		<u-sticky>
			<u-tabs-swiper ref="uTabs" :list="tabList" :current="current" @change="tabsChange" :is-scroll="false"
				swiperWidth="750"></u-tabs-swiper>
		</u-sticky>
		<swiper class="swiper" :current="swiperCurrent" @transition="transition" @animationfinish="animationfinish">

			<swiper-item class="swiper-item" v-for="(item, index) in tabList" :key="index">
				<swiper-learn-item :tabIndex="index" :currentIndex="swiperCurrent" :lessonId="lessonId"
					@changeIndex="changeIndex(arguments)" :studyScheduleIndex="lessonIndex" @nextClick="nextClick"
					:knowledgeIndex="knowledgeIndex" :taskId="taskId" v-if="lessonId!=null&&lessonId!=-1">
				</swiper-learn-item>
			</swiper-item>


		</swiper>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				knowledgeIndex: 0, //位于列表中的第几个  用于知识库页面的刷新
				systemInfo: null,
				cssSafeAreaInsetBottom: -1,
				clientHeight: 0,

				title: "",
				lessonIndex: -1, //当前指向哪节课

				tabList: [{
					name: '课程'
				}, {
					name: '目录'
				}],
				// 因为内部的滑动机制限制，请将tabs组件和swiper组件的current用不同变量赋值
				current: 0, // tabs组件的current值，表示当前活动的tab选项
				swiperCurrent: 0, // swiper组件的current值，表示当前那个swiper-item是活动的
				lessonId: -1,
				taskId: -1, //任务id
			}
		},

		mounted() {
			this.$nextTick(() => {
				this.systemInfo = uni.getSystemInfoSync();
			})

		},
		computed: {
			swiperStyle() {
				if (!this.systemInfo) {
					return {};
				}
				let swiperStyle = {};
				const windowTop = this.systemInfo.windowTop;
				const windowBottom = this.systemInfo.windowBottom;

				if (windowTop && windowTop !== undefined) {
					swiperStyle.top = windowTop + 'px';
				}
				let bottom = 0;
				if (windowBottom && windowBottom !== undefined) {
					bottom = windowBottom;
				}

				bottom += this.safeAreaBottom;
				if (bottom > 0) {
					swiperStyle.bottom = bottom + 'px';
				}

				return swiperStyle;
			},
			safeAreaBottom() {
				if (!this.systemInfo) {
					return 0;
				}
				let safeAreaBottom = 0;
				// #ifdef APP-PLUS
				safeAreaBottom = this.systemInfo.safeAreaInsets.bottom || 0;
				// #endif
				// #ifndef APP-PLUS
				safeAreaBottom = this.cssSafeAreaInsetBottom === -1 ? 0 : this.cssSafeAreaInsetBottom;
				// #endif
				return safeAreaBottom;
			}
		},

		onLoad(option) {
			this.title = option.title;
			this.lessonId = Number(option.id);
			console.log("》》》》》》》》》》 parent lessonId"+this.lessonId);
			this.taskId = Number(option.taskId);
			this.knowledgeIndex = Number(option.knowledgeIndex); //用于知识库领取状态的精准刷新

			if (option.index) {
				this.lessonIndex = Number(option.index);
			} else {
				//查看进度中正在学习的课程位置
				this.lessonIndex = -1;
			}
			var that = this
			uni.getSystemInfo({
				success: function(res) {
					that.clientHeight = res.windowHeight - 177
				}
			})
		},
		onReady() {
			uni.setNavigationBarTitle({
				title: this.title
			});
		},
		methods: {
			//点击目录切换到详情页
			changeIndex(val) {
				this.swiperCurrent = val[0];
				this.index = val[0];
				this.lessonIndex = val[1];
			},
			//切换章节
			nextClick(currentIndex) {//获取到当前课件的index
				
				this.lessonIndex = currentIndex+1;
			},

			// tabs通知swiper切换
			tabsChange(index) {
				this.swiperCurrent = index;
			},
			// swiper-item左右移动，通知tabs的滑块跟随移动
			transition(e) {
				let dx = e.detail.dx;
				this.$refs.uTabs.setDx(dx);
			},
			// 由于swiper的内部机制问题，快速切换swiper不会触发dx的连续变化，需要在结束时重置状态
			// swiper滑动结束，分别设置tabs和swiper的状态
			animationfinish(e) {
				let current = e.detail.current;
				this.$refs.uTabs.setFinishCurrent(current);
				this.swiperCurrent = current;
				this.current = current;
			}
		}
	}
</script>

<style>
	.page {
		background-color: #F8F8F8;
		height: 100vh;
		display: flex;
		flex-direction: column;
		flex: 1;
		position: fixed;
		top: 0;
		left: 0;
		bottom: 0;
		right: 0;

	}

	.swiper {
		height: 100%;
		flex: 1;
		position: relative;
	}

	.swiper-item {
		height: 100%;
		overflow: scroll;
	}
</style>
