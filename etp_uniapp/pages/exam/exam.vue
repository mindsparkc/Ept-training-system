<template>
	<z-paging-swiper class="page">

		<!-- 需要固定在顶部不滚动的view放在slot="top"的view中 -->
		<view slot="top" style="background-color: #F8F8F8;">
			<u-tabs-swiper ref="uTabs" :list="tabList" :current="current" @change="tabsChange" :is-scroll="false"
				swiperWidth="750"></u-tabs-swiper>
			
		</view>
		<!-- swiper必须设置height:100%，因为swiper有默认的高度，只有设置高度100%才可以铺满页面  -->
		<swiper class="swiper" :current="swiperCurrent" @transition="transition" @animationfinish="animationfinish">
		
			<swiper-item class="swiper-item" >
				<swiper-list-item-exam tabIndex="0" :currentIndex="swiperCurrent" ></swiper-list-item-exam>
			</swiper-item>
			<swiper-item class="swiper-item" >
				<swiper-list-item-history-exam tabIndex="1" :currentIndex="swiperCurrent" ></swiper-list-item-history-exam>
			</swiper-item>
			
			
		</swiper>
	</z-paging-swiper>
</template>

<script>
	export default {
		data() {
			return {
				// keyword: '',

				tabList: [{
					name: '当前考试'
				}, {
					name: '历史考试'
				}],
				// 因为内部的滑动机制限制，请将tabs组件和swiper组件的current用不同变量赋值
				current: 0, // tabs组件的current值，表示当前活动的tab选项
				swiperCurrent: 0, // swiper组件的current值，表示当前那个swiper-item是活动的
			};
		},
		methods: {
			search() {
					// this.$refs.paging.reload();
					console.log("搜索");
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
		height: 100%;
	}

	.swiper {
		height: 100%;
	}
	
	.margin {
		margin: 30rpx 30rpx 30rpx 30rpx;
	}
</style>
