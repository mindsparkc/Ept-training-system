<template>
	<view>
		<button @click="transform">切换{{orientation}}</button>

	<view style="background-color: #222222;" >占位占位占位占位
			<image src="../../static/lesson/icon-landscape.png"></image>
			<view>横屏功能依赖于手机横竖屏，请您解除手机竖屏锁定，并将手机横向拿起</view>
		</view>

		<scroll-view :scroll-x="isX" :scroll-y="isY" :class="[isX?'scroll-land':'scroll-port']" show-scrollbar>
			<view v-for="(item,index) in list " :key="index"
				style="background-color: coral;margin-top: 10px;text-align:center;height: 300px;">
				<text>{{index}}</text>
			</view>


		</scroll-view>

		<view style="position: fixed;bottom: 0;background-color: aqua;height: 50px;width: 100vw;">
			我是底部条
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isX: false,
				isY: true,
				list: [{},
					{},
					{},
					{},
					{},
					{}
				],
				orientation: ""
			}
		},
		onReady() {
			// #ifdef H5
			window.addEventListener('orientationchange', this.monitorScreen)
			// #endif
		},

		methods: {
			//h5监听旋转
			monitorScreen() {
				if (window.orientation == 0) {
					this.orientation = "竖屏"
					console.log("竖屏")
				} else if (window.orientation == 90) {
					console.log("横屏")
					this.orientation = "横屏"
				}
			},
			transform() {
				this.isX = !this.isX;
				this.isY = !this.isY;

				console.log("当前scroll-view滑动模式是否横向x>>>", this.isX);
				console.log("当前scroll-view滑动模式是否纵向y>>>", this.isY);
			}
		}
	}
</script>

<style>
	.scroll-port {
		width: 100vw;
		height: 100vh;
	}

	.scroll-land {

		width: 100vh;
		height: 100vw;
		-webkit-transform: rotate(90deg);
		-webkit-transform-origin: 50vw 50vw;
		transform: rotate(90deg);
		transform-origin: 50vw 50vw;
	}



	@media (orientation: portrait) {

		/* 竖屏CSS */
		.a {
			z-index: 1;
			width: 100%;
			height: 100%;
			position: absolute;
			background-color: blue;
			visibility: visible;
		}


	}

	@media (orientation: landscape) {

		/* 横屏CSS */
		.a {
			background-color: mediumvioletred;
			visibility: hidden
		}
	}
</style>
