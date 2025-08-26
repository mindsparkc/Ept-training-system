<!--倒计时组件-->
<template>
	<view class="count-down-box">
		<view class="time-text" :style="{color:textColor}">{{tipText}}</view>
		<view class="time-bock" :style="{background,color}">{{date}}</view>
		<view class="time-text" :style="{color:textColor}">天</view>

		<view v-if="isBig" class="count-down-box">
			<view class="time-bock" :style="{background,color}">{{hour}}</view>
			<view class="time-text" :style="{color:textColor}">时</view>
			<view class="time-bock" :style="{background,color}">{{minute}}</view>
			<view class="time-text" :style="{color:textColor}">分</view>
			<view class="time-bock" :style="{background,color}">{{second}}</view>
			<view class="time-text" :style="{color:textColor}">秒</view>
		</view>
		<view v-else>
			<view class="time-bock" :style="{background,color}">{{hour}}:{{minute}}:{{second}}</view>
		</view>
	</view>
</template>

<script>
	/**
	 * 倒计时组件 
	 * @description 时间倒计时组件，显示剩余时间
	 * @property {String} endTime 结束时间
	 * @property {String} background 背景
	 * @property {String} color 颜色
	 * @property {String} textColor 文字颜色
	 * @property {String} tipText 提示文字
	 * @property {Boolean} isBig 时分秒分隔符用冒号还是用文字展示
	 * @example  <count-down tipText="距报名结束" :end-time="2022-01-01" background="rgba(254, 168, 1, 0.1)" color="#FEA801"
				text-color="#919199"  />
	*/
	export default {
		props: {
			endTime: [Date, Number, String], //结束时间
			background: String, //背景
			color: String, //颜色
			textColor: String, //文字颜色
			tipText: String, //前面的提示文字
			//时分秒 用冒号展示还是文字展示
			isBig: {
				type: Boolean,
				default: () => {
					return true
				}
			},
			timeStyle: {
				type: Object,
				default: () => {
					return {}
				}
			}, //时间样式
			textStyle: {
				type: Object,
				default: () => {
					return {}
				}
			}, //时分秒样式
		},
		data() {
			return {
				date: '', //天
				hour: '', //时
				minute: '', //分
				second: '', //秒
				spaceTime: 0, //间隔时间
			}
		},
		watch: {
			endTime: {
				handler: function(value) {
					this.startCount(value)
				},
				immediate: true
			}
		},
		methods: {
			/**
			 * 开始倒计时
			 */
			startCount(value) {
				let time = new Date(value)
				let now = Date.now()
				this.spaceTime = time - now
				this.getTimeData(this.spaceTime)
				this.timeOutCount()
			},
			/**
			 * 获取倒计时时间
			 */
			getTimeData(time) {
				let date = Math.floor(time / (1000 * 60 * 60 * 24)) //计算天数
				let hour = Math.floor(time / (1000 * 60 * 60) % 24) //计算小时数
				let minute = Math.floor(time / (1000 * 60) % 60) //计算分钟数
				let second = Math.floor(time / 1000 % 60) //计算秒数
				date < 0 && (date = 0)
				hour < 0 && (hour = 0)
				minute < 0 && (minute = 0)
				second < 0 && (second = 0);
				date < 10 && (date = '0' + date);
				hour < 10 && (hour = '0' + hour);
				minute < 10 && (minute = '0' + minute);
				second < 10 && (second = '0' + second)
				this.date = date
				this.hour = hour
				this.minute = minute
				this.second = second
			},
			/**
			 * 倒计时
			 */
			timeOutCount() {
				setTimeout(() => {
					this.spaceTime = this.spaceTime - 1000
					if (this.spaceTime < 0) {
						return
					}
					this.getTimeData(this.spaceTime)
					this.timeOutCount(this.spaceTime)
				}, 1000)
			}
		}
	}
</script>

<style scoped lang="scss">
	.count-down-box {
		display: flex;
		flex-direction: row;
		align-items: center;

		.time-bock {
			background: #FFFFFF;
			border-radius: 6rpx;
			padding: 6rpx 10rpx;
			font-size: 20rpx;
		}

		.time-text {
			font-size: 20rpx;
			margin: 0 10rpx;
		}
	}
</style>
