<!-- 仅渲染三个swiper item 通过不断重组数组的形式 加载列表项 解决当列表项很多的时候的卡顿问题 -->
<template>
	<view>
		<swiper :style="'height:'+swiperHeight+'px'" :current="swiperCurrent" :duration="swiperDuration"
			@change="swiperChange" circular>
			<swiper-item v-for="(item, index) in swiperList" :key="index">
				<item-question :data="item" @nextQuestion="nextQuestion" :swiperHeight="swiperHeight"></item-question>
			</swiper-item>

		</swiper>


	</view>
</template>

<script>
	const START = 0 //swiper item 第一个
	const END = 2 //swiper item 最后一个
	const SWIPER_LENGTH = 3 //swiper item个数
	const NO_PREV_PAGE = -1 //没有上一页
	const NO_NEXT_PAGE = -2 //没有下一页
	export default {
		name: "swiper-limited-load",
		props: {
			swiperHeight: {
				type: Number,
				value: 0
			},
			list: {
				type: Array,
				value: []
			},
			current: {
				type: Number,
				value: 0
			},
			// 值为0禁止切换动画
			swiperDuration: {
				type: String,
				value: "250"
			},
			// 分页需要传此数据
			total: {
				type: Number,
				value: 0
			}

		},
		mounted() {
			// console.log("swiper-limited-load", JSON.stringify(this.list));
		},
		watch: {
			current: {
				handler(index) {
					console.log("watch检测到current改变：" + index + "当前swiperIndex:" + this.swiperIndex);
					let current = index % SWIPER_LENGTH

					if (this.swiperList.length == 0 || this.swiperList[current] == null) {
						return
					}
					// 如果change后还是之前的那一个item，直接return
					if (current == this.swiperIndex && this.swiperList[this.swiperIndex].index == index) {
						console.log("如果change后还是之前的那一个item，直接return");
						return
					}
					this.init(index);
					// 如果change之后还是当前的current，比如之前是1、点击后是4  之前是2、点击后是5之类
					// 那么不会走swiperChange的change方法，需要我们手动去给它加一个current，然后传出去

					console.log("current" + current + "swiperIndex" + this.swiperIndex);
					if (current == this.swiperIndex) {
						console.log("如果change之后还是当前的current不会走swiperChange的change方法，需要我们手动去给它加一个current，然后传出去");
						this.$emit("change", {
							source: "",
							current: index
						})
					} else {
						//FIXME
						// this.swiperIndex = current
						// this.swiperCurrent = current
					}
				},
				immediate: true
			}
		},
		data() {
			return {
				// 滑动到的位置
				swiperIndex: 0,
				// 此值控制swiper的位置
				swiperCurrent: 0,
				// 当前swiper渲染的items
				swiperList: [],

			};
		},
		methods: {


			init(defaultIndex) {
				let list = this.list
				if (list == null || list.length == 0) {
					return
				}
				// 默认显示的index
				let current = defaultIndex % SWIPER_LENGTH
				this.swiperList = this.getInitSwiperList(list, defaultIndex);

				this.swiperIndex = current;
				this.swiperCurrent = current
				console.log("swiperIndex初始化：", this.swiperIndex);
			},

			clear() {
				this.list = []
				this.swiperList = []

			},

			swiperChange(e) {

				let current = e.detail.current
				//若当前为第一道题，左滑进入最后一道题   若当前为最后一道题  右滑进入第一道题
				let lastIndex = this.swiperIndex
				let currentItem = this.swiperList[current]
				let info = {}
				info.source = e.detail.source

				// 正向滑动，到下一个的时候
				// 是正向衔接
				let isLoopPositive = (current == START && lastIndex == END);

				if ((current - lastIndex) == 1 || isLoopPositive) {
					// 如果是滑到了左边界或者下一个还未有值，弹回去
					if (currentItem == null) {
						info.current = NO_NEXT_PAGE
						console.log("change调用");
						this.$emit("change", info)
						this.swiperCurrent = lastIndex;
						return
					}
					this.swiperList.splice(this.getNextSwiperChangeIndex(current), 1,
						this.getNextSwiperNeedItem(
							currentItem, this.list))

				}

				// 反向滑动，到上一个的时候
				// 是反向衔接
				let isLoopNegative = (current == END && lastIndex == START);
				if (lastIndex - current == 1 || isLoopNegative) {
					// 如果滑到了右边界或者上一个还未有值，弹回去
					if (currentItem == null) {
						info.current = NO_PREV_PAGE
						this.$emit("change", info)
						console.log("change调用");
						this.swiperCurrent = lastIndex

						return
					}

					this.swiperList.splice(this.getLastSwiperChangeIndex(current), 1,
						this.getLastSwiperNeedItem(
							currentItem, this.list))


				}

				if (currentItem == null) return
				// 记录滑过来的位置，此值对于下一次滑动的计算很重要
				this.swiperIndex = current;
				info.current = currentItem.index;
				this.$emit("change", info);
				console.log("change调用 swiperChange()>>>>>>>当前swiperIndex", this.swiperIndex);
			},

			/**
			 * 获取初始化的swiperList
			 */
			getInitSwiperList(list, defaultIndex) {
				let current = defaultIndex % SWIPER_LENGTH
				let realIndex = list.findIndex(function(item) {
					return item.index == defaultIndex
				})
				let currentItem = list[realIndex]
				let swiperList = []
				swiperList[current] = currentItem
				swiperList[this.getLastSwiperChangeIndex(current)] = this.getLastSwiperNeedItem(currentItem, list)
				swiperList[this.getNextSwiperChangeIndex(current)] = this.getNextSwiperNeedItem(currentItem, list)
				// console.log("初始化swiperList", JSON.stringify(swiperList))


				return swiperList;
			},
			/**
			 * 获取swiperList中current上一个的index
			 */
			getLastSwiperChangeIndex(current) {
				return current > START ? current - 1 : END
			},
			/**
			 * 获取swiperLit中current下一个的index
			 */
			getNextSwiperChangeIndex(current) {
				return current < END ? current + 1 : START
			},
			/**
			 * 获取上一个要替换的list中的item
			 */
			getLastSwiperNeedItem(currentItem, list) {
				if (currentItem == null) return null;

				let listNeedIndex = currentItem.index - 1
				let item = {}
				if (listNeedIndex < 0) {
					//当当前题目为第一题时，左滑为最后一道题
					item = list[list.length - 1]

				} else {
					let realIndex = list.findIndex(function(item) {
						return item.index == listNeedIndex
					})
					if (realIndex == -1) return null
					item = (listNeedIndex == -1) ? null : list[realIndex]
				}



				return item
			},
			/**
			 * 获取下一个要替换的list中的item
			 */
			getNextSwiperNeedItem(currentItem, list) {
				if (currentItem == null) return null;
				let listNeedIndex = currentItem.index + 1
				let item = {}
				if (listNeedIndex >= list.length) { //当当前题目为最后一题时，右滑为第一道题
					item = list[0]
				} else {
					let realIndex = list.findIndex(function(item) {
						return item.index == listNeedIndex
					})
					if (realIndex == -1) return null
					let total = this.total != 0 ? this.total : list.length
					item = listNeedIndex == total ? null : list[realIndex]
				}


				return item
			},
			//自动到下一题
			nextQuestion(index) {

				this.$emit("next", "")
				// 				if (index >= this.list.length - 1) { //当当前题目为最后一题时，下一题为第一道题
				// 					index = 0
				// 				} else {
				// 					index++
				// 				}
				// console.log("自动到下一题nextQuestion", index);
				// 				this.init(index)
				// this.$emit("change", {
				// 	source: "",
				// 	current: index
				// })

			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #f8f8f8;
	}
</style>
