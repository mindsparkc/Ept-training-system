<template>
	<view v-if="list&&list.length>0">
		<swiper class="swiper" <!-- #ifdef APP-VUE || H5 -->
			:swiperCurrent="swiperCurrent"
			:change:swiperCurrent="rjs.swiperCurrentChange"
			<!-- #endif -->
			<!-- #ifndef APP-VUE || H5 -->
			:current="swiperCurrent"
			<!-- #endif -->
			ref="swiper"
			@change="change"
			@transition="transition"
			@animationfinish="animationfinish"
			:disable-touch="disableTouch"
			:indicator-dots="false"
			:vertical="vertical"
			:duration="duration"
			circular
			:autoplay="autoplay"
			:interval="interval"
			:easingFunction="easingFunction"
			>

			<swiper-item v-for="(item, index) in 3" :key="index" :data-vertical="vertical" :data-current="current"
				:data-data-length="dataLength" <!-- #ifdef APP-VUE || H5 || MP-WEIXIN || MP-QQ -->
				@touchstart="wxs.touchStart"
				@touchmove="wxs.touchMove"
				<!-- #endif -->
				<!-- #ifndef APP-VUE || H5 || MP-WEIXIN || MP-QQ -->
				@touchstart="touchStart"
				@touchmove="touchMove"
				@touchend="touchEnd"
				<!-- #endif -->
				>
				<!-- <slot v-if="list[swiperList[index]]" :item="list[swiperList[index]]" :index="swiperList[index]"></slot> -->

				<item-error-book-question :question="list[swiperList[index]]" v-if="list[swiperList[index]]">
				</item-error-book-question>


			</swiper-item>
		</swiper>
	</view>
</template>
<script module="rjs" lang="renderjs">
	export default {
		methods: {
			swiperCurrentChange(newVal, oldVal, vm) {

				let offset = newVal - oldVal
				if (offset > 1) offset = -1
				if (offset < -1) offset = 1
				const swiper = this.$refs.swiper
				if (swiper) {
					// 指定动画行为，否则当滑到循环处时，会出现反向滑动的BUG
					swiper.currentChangeSource = 'autoplay'
					// 滑动索引跟进  
					swiper.currentSync = newVal
					// 播放滑动动画，第三个参数决定了滑动方向，大于0时右滑，小于0时左滑  
					swiper._animateViewport(newVal, '', offset)
				}

			}
		}
	}
</script>
<script module="wxs" lang="wxs">
	var touchStartObj = {}
	module.exports.touchStart = function(e) {
		touchStartObj = e.touches[0];
	}
	module.exports.touchMove = function(e, instance) {
		var moveObj = e.touches[0];
		var dataset = e.instance.getDataset()
		var current = dataset.current
		var vertical = dataset.vertical
		var dataLength = dataset.dataLength
		var preDir = vertical ? moveObj.clientY - touchStartObj.clientY > 0 : moveObj.clientX - touchStartObj.clientX >
			0
		var nextDir = vertical ? moveObj.clientY - touchStartObj.clientY < 0 : moveObj.clientX - touchStartObj
			.clientX < 0
		if (current == 0 && preDir) {
			// #ifdef APP-PLUS || H5
			e.stopPropagation();
			// #endif
			// #ifndef APP-PLUS || H5
			return false
			// #endif
		} else if (current == dataLength - 1 && nextDir) {
			// #ifdef APP-PLUS || H5
			e.stopPropagation();
			// #endif
			// #ifndef APP-PLUS || H5
			return false
			// #endif
		}
	}
</script>
<script>
	export default {
		components: {},
		props: {
			list: {
				type: Array,
				default: _ => [],
				required: true
			},
			dataIndex: {
				type: Number,
				default: 0
			},
			vertical: {
				type: Boolean,
				default: false
			},
			duration: {
				type: Number,
				default: 250
			},
			autoplay: {
				type: Boolean,
				default: false
			},
			interval: {
				type: Number,
				default: 5000
			},
			easingFunction: {
				type: String,
				default: 'default'
			},
			getPreviousHandler: {
				type: Function
			},
			getNextHandler: {
				type: Function
			},

		},
		data() {
			return {
				dataLength: 0,
				swiperCurrent: 0,
				swiperList: [],
				current: 0,
				touchStartObj: {},
				disableTouch: false
			};
		},

		created() {},
		methods: {
			goPrevious() {
				if (this.current > 0) {
					this.swiperCurrent = (3 + this.swiperCurrent - 1) % 3
				}
			},
			goNext() {
				if (this.current < this.dataLength - 1) {
					this.swiperCurrent = (this.swiperCurrent + 1) % 3
				}
			},
			//app nvue
			touchStart(e) {
				this.touchStartObj = e.changedTouches[0];
			},
			touchMove(e) {
				var moveObj = e.changedTouches[0];
				var current = this.current
				var vertical = this.vertical
				var dataLength = this.dataLength
				var touchStartObj = this.touchStartObj
				var preDir = vertical ? moveObj.pageY - touchStartObj.pageY > 0 : moveObj.pageX - touchStartObj.pageX > 0
				var nextDir = vertical ? moveObj.pageY - touchStartObj.pageY < 0 : moveObj.pageX - touchStartObj.pageX < 0
				if (current == 0 && preDir) {
					this.disableTouch = true
				} else if (current == dataLength - 1 && nextDir) {
					this.disableTouch = true
				}
			},
			touchEnd() {
				this.disableTouch = false
			},
			async change(e) {
				this.swiperCurrent = e.detail.current
				this.handleList(e.detail.current);
				this.current = this.swiperList[e.detail.current];
				if (this.current == 0 && this.getPreviousHandler) {
					let hasPrevious = await this.getPreviousHandler()
					if (hasPrevious) {
						this.insertPrevious()
					}
				} else if (this.current == this.dataLength - 1 && this.getNextHandler) {
					let hasNext = await this.getNextHandler()
					if (hasNext) {}
				}
				//必须在这，可能insertPrevious 会++
				this.$emit('change', this.current)
			},
			animationfinish(e) {
				this.$emit('animationfinish', e);
			},
			transition(e) {
				this.$emit('transition', e);
			},
			handleList(index) {

				switch (index) {
					case 0:
						this.$set(this.swiperList, 1, (this.swiperList[0] + 1) % this.dataLength);
						this.$set(this.swiperList, 2, this.swiperList[0] == 0 ? this.dataLength - 1 : this.swiperList[0] -
							1);
						break;
					case 1:
						this.$set(this.swiperList, 2, (this.swiperList[1] + 1) % this.dataLength);
						this.$set(this.swiperList, 0, this.swiperList[1] == 0 ? this.dataLength - 1 : this.swiperList[1] -
							1);
						break;
					case 2:
						this.$set(this.swiperList, 0, (this.swiperList[2] + 1) % this.dataLength);
						this.$set(this.swiperList, 1, this.swiperList[2] == 0 ? this.dataLength - 1 : this.swiperList[2] -
							1);
						break;
					default:
						this.$set(this.swiperList, 0, this.dataIndex);
						this.$set(this.swiperList, 1, (this.swiperList[0] + 1) % this.dataLength);
						this.$set(this.swiperList, 2, this.swiperList[0] == 0 ? this.dataLength - 1 : this.swiperList[0] -
							1);
				}
				this.$emit('indexesChange', this.swiperList);
			},
			handleListDataIndexChange() {
				let index = this.swiperCurrent
				switch (index) {
					case 0:
						this.$set(this.swiperList, 0, this.dataIndex);
						this.$set(this.swiperList, 1, (this.swiperList[0] + 1) % this.dataLength);
						this.$set(this.swiperList, 2, this.swiperList[0] == 0 ? this.dataLength - 1 : this.swiperList[0] -
							1);
						break;
					case 1:
						this.$set(this.swiperList, 1, this.dataIndex);
						this.$set(this.swiperList, 2, (this.swiperList[1] + 1) % this.dataLength);
						this.$set(this.swiperList, 0, this.swiperList[1] == 0 ? this.dataLength - 1 : this.swiperList[1] -
							1);
						break;
					case 2:
						this.$set(this.swiperList, 2, this.dataIndex);
						this.$set(this.swiperList, 0, (this.swiperList[2] + 1) % this.dataLength);
						this.$set(this.swiperList, 1, this.swiperList[2] == 0 ? this.dataLength - 1 : this.swiperList[2] -
							1);
						break;
					default:
						this.$set(this.swiperList, 0, this.dataIndex);
						this.$set(this.swiperList, 1, (this.swiperList[0] + 1) % this.dataLength);
						this.$set(this.swiperList, 2, this.swiperList[0] == 0 ? this.dataLength - 1 : this.swiperList[0] -
							1);
				}

				// console.log("index改变数组数据",JSON.stringify( this.swiperList));

				this.$emit('indexesChange', this.swiperList);
			},
			insertPrevious() {
				this.current++
				this.swiperList[this.swiperCurrent]++
				this.handleList(this.swiperCurrent);
			}
		},
		watch: {
			dataIndex: {
				immediate: true,
				handler(dataIndex) {

					this.current = dataIndex;
					this.$emit('change', this.current)
					this.handleListDataIndexChange();
				}
			},
			list: {
				immediate: true,
				handler(list) {
					this.dataLength = list.length
					if (this.dataLength) {
						this.handleList(this.swiperCurrent);
					}
				}
			}
		}
	};
</script>

<style scoped>
	.swiper {
		width: 100%;
		height: 100vh;

	}
</style>
