<template>
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
			<scroll-view scroll-y :style="'height:'+height+'px;width:'+width+'px'" :scroll-top="scrollTop"
				@scroll="scroll">
				<!-- <u--image :src="completePath(list[swiperList[index]])" :showLoading="true" :mode="mode"
					:fade="true" :lazyLoad="false" v-if="list[swiperList[index]]" :width="width" :height="height">
				</u--image> -->
				<!-- <movable-area :style="'height:'+height+'px;width:'+width+'px'">
					<movable-view direction="all" :scale="true" :scale-max="scaleMax" :scale-min="scaleMin"
						:htouchmove.stop="scaleValueTemp>1.5?mtouchmove: ''"
						:vtouchmove.stop="scaleValueTemp>1.5?mtouchmove:''" @scale="movableScale"
						@change="changeMove"> -->
						<image :src="completePath(list[swiperList[index]])" :mode="mode"
							:style="'width:'+width+'px; min-height:'+height+'px'"></image>
			<!-- 		</movable-view>
				</movable-area> -->

			</scroll-view>

		</swiper-item>
	</swiper>
</template>
<script module="rjs" lang="renderjs">
	export default {
		methods: {
			swiperCurrentChange(newVal, oldVal, vm) {
				// console.log("swiperCurrentChange：new:" + newVal + "old:" + oldVal);

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
			width: {
				type: Number
			},
			height: {
				type: Number
			},
			hostPath: {
				type: String
			},
			mode: {
				type: String,
				default: 'aspectFit'
			}
		},
		data() {
			return {
				dataLength: 0,
				swiperCurrent: 0,
				swiperList: [],
				current: 0,
				touchStartObj: {},
				disableTouch: false,
				scrollTop: 0,
				old: {
					scrollTop: 0
				},
				scaleValueTemp: 0, //图片缩放倍数
				scaleMin: 0.8,
				scaleMax: 3, //缩放系数 最大3倍

			};
		},
		created() {},
		methods: {
			mtouchmove() {},

			movableScale(e) {
				//缩放过程中的触发事件 event.detail = {x: x, y: y, scale: scale}
				this.scaleValueTemp = e.detail.scale
				console.log(this.scaleValueTemp);

			},
			changeMove(e) {
				// console.log(JSON.stringify(e));
				// if (e.detail.source === 'touch-out-of-bounds' &&
				// 	(e.detail.x > 0 || e.detail.x < (this.scaleValue > 1 ? (this.scaleValue < 2 ? -this.screen_width * this
				// 		.scaleValue * 0.1 : -this.screen_width) : 0))) {
				// }
			},


			scroll(e) {
				this.old.scrollTop = e.detail.scrollTop
			},
			goTop(e) {
				this.scrollTop = this.old.scrollTop
				this.$nextTick(() => {
					this.scrollTop = 0
				});
			},
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
				this.goTop();
				// if (e.detail.source == 'touch' || e.detail.source == 'autoplay') { //解决快速滑动 不停抖动问题

				// }



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
			},

			//图片host 截取
			svgHostSlice(path) {
				let host = "";
				if (path.lastIndexOf('.') != -1) {
					let end = path.lastIndexOf('.');
					host = path.slice(0, end);
				}
				return host;

			},
			completePath(svgPath) {
				let fullPath = svgPath;

				if (svgPath.indexOf("http") == -1) { //需要拼接
					let host = this.svgHostSlice(this.hostPath);
					fullPath = host + "/" + svgPath;
				}
				return fullPath;

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
		height: 100%;
	}
</style>
