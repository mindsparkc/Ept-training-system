<!-- 当前考试列表-->
<template>
	<view class="content">
		<!-- 这里设置了z-paging加载时禁止自动调用reload方法，自行控制何时reload（懒加载）-->
		<!--  :enable-back-to-top="currentIndex===tabIndex" 在微信小程序上可以多加这一句，因为默认是允许点击返回顶部的，但是这个页面有多个scroll-view，会全部返回顶部，所以需要控制是当前index才允许点击返回顶部 -->
		<z-paging ref="paging" v-model="dataList" @query="queryExamList" :fixed="false" :auto="false"
			:enable-back-to-top="currentIndex===tabIndex">
			<view class="margin">
				<u-search placeholder="请输入考试名称" v-model="keyword" bgColor="#ffffff" @search="search" :show-action="true"
					actionText="搜索" :animation="true" @custom="search" shape="square">
				</u-search>

			</view>

			<view class="list-box" v-for="(item,index) in dataList" :key="index" @click="itemClick(item)">
				<text class="badge-normal"
					:class="state[judgeDate(item).stateIndex] ">{{judgeDate(item).stateName}}</text>

				<view class="flex-row">
					<view>
						<u--image :src="teachCover(item.cover)" :showLoading="true" mode="aspectFill" width="240rpx"
							height="240rpx" :fade="true" radius="5"></u--image>
					</view>

					<!-- <image :src="teachCover(item.cover)" class="image-cover" mode="aspectFill" v-if="item.cover"></image> -->
					<view class="flex-col">
						<text class="ell-text">{{item.name?item.name:"自定义考试"}}</text>
						<view> <text class="text-normal">{{item.totalTime?'考试限时'+item.totalTime +'分钟':'本场考试不限时长'}}
							</text>
							<!-- 总分数{{item.totalScore?item.totalScore:"N/A"}}分 -->
						</view>

						<text class="text-normal" v-if="item.chance ==0">已考<text
								style="color: #F0AD4E;">{{item.usedChance}}</text>次 考试不限次</text>
						<text class="text-normal" v-if="item.chance!=0">已考<text
								style="color: #F0AD4E;">{{item.usedChance}}</text>次 共<text
								style="color: #F0AD4E;">{{item.chance}}</text>次<text style="margin-left: 10rpx;"
								v-if="item.chance!=0&&(item.chance-item.usedChance)<=0">考试次数已用完</text></text>
						<text
							class="text-small-grey">开始时间：{{item.timeLimit==1? $u.timeFormat(item.startTime, 'yy-mm-dd hh:MM'):$u.timeFormat(item.publishTime, 'yy-mm-dd hh:MM')}}

						</text>
						<text class="text-small-grey"
							v-if="item.endTime">结束时间：{{$u.timeFormat(item.endTime, 'yy-mm-dd hh:MM')}}</text>



					</view>
				</view>



			</view>
		</z-paging>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//v-model绑定的这个变量不要在分页请求结束中自己赋值！
				dataList: [],
				firstLoaded: false,
				keyword: '',
				state: [
					'badge-orange', //未开始
					'badge-green', //进行中
					'badge-grey' //已结束
				]
			}
		},
		props: {
			//当前组件的index，也就是当前组件是swiper中的第几个
			tabIndex: {
				type: String,
				default: function() {
					return "0"
				}
			},
			//当前swiper切换到第几个index
			currentIndex: {
				type: Number,
				default: function() {
					return 0
				}
			}

		},
		watch: {
			currentIndex: {
				handler(newVal) {
					if (newVal == this.tabIndex) {
						//懒加载，当滑动到当前的item时，才去加载
						if (!this.firstLoaded) {
							setTimeout(() => {
								this.$refs.paging.reload();
							}, 5);
						}
					}
				},
				immediate: true
			},
		},

		onShow() {
			if (this.$refs.paging) {
				this.$refs.paging.refresh();
			}
		},
		methods: {

			search() {
				this.$refs.paging.reload();
			},

			//请求考试列表
			queryExamList(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: false
				});
				let keyword = ""
				//当前考试
				keyword = this.keyword
				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"name": keyword,
					"orders": "update_time,desc"
				}
				uni.$u.api.getExamList(params).then(res => {
					uni.hideLoading();
					this.firstLoaded = true;
					self.$refs.paging.completeByTotal(res.rows, res.total, true);

				}).catch(err => {
					uni.hideLoading();
					console.log(err.errMsg);
				});
			},

			itemClick(item) {
				uni.navigateTo({
					url: '/pages/exam/examIndex/examIndex?examId=' + item.examId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

			},


			//时间判断  当前时间是否大于开始时间 小于结束时间
			//新增 根据status判断 # 考试状态（1暂存 2发布 3正常结束 4强制结束），允许管理员强制结束已经开始的考试，学员只能搜索到已发布的考试  若status为4强制结束 显示已结束  当为发布状态时判断开始时间  未到考试时间显示未开始 已到开始时间未到结束时间显示 进行中 
			judgeDate(item) {
				let status = item.status;
				let obj = {}
				if (status == 3 || status == 4) { //强制结束
					obj.stateIndex = 2;
					obj.stateName = "已结束"
					return obj;
				} else if (status == 2) {


					let startTime = new Date(item.startTime);

					let now = new Date().getTime();

					if (now < startTime) { //当前时间小于开始时间 则考试未开始
						obj.stateIndex = 0;
						obj.stateName = "未开始"
						return obj;
					} else { //当前时间大于开始时间
						if (item.endTime) {
							let endTime = new Date(item.endTime);
							if (now < endTime) { //当前时间小于结束时间 考试进行中
								obj.stateIndex = 1;
								obj.stateName = "进行中"
								return obj;

							} else { //当前时间大于结束时间  考试已结束
								obj.stateIndex = 2;
								obj.stateName = "已结束"
								return obj;
							}
						} else { //endTime为空代表不限制结束时间 为一个长期的考试
							obj.stateIndex = 1;
							obj.stateName = "进行中"
							return obj;
						}


					}

				}




			}



		}
	}
</script>

<style lang="scss">
	/* 注意:父节点需要固定高度，z-paging的height:100%才会生效 */
	.content {
		background-color: #f8f8f8;
		height: 100%;
	}

	.item {
		position: relative;
		height: 150rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0rpx 30rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
	}

	.badge-normal {
		position: absolute;
		top: 0;
		right: 0;
		height: 40rpx;
		border-bottom-left-radius: 20rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;

	}

	.badge-green {
		background-color: #2FCCA7;
		color: #FFF;
	}

	.badge-orange {
		background-color: #FEA801;
		color: #FFF;
	}

	.badge-grey {
		background-color: #EEEEEE;
		color: #919199;
	}




	.list-box {

		position: relative;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;

		margin-bottom: 40rpx;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-around;
		margin-left: 35rpx;
		margin-bottom: 10rpx;
		margin-top: 10rpx;
	}

	.ell-text {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		color: $uni-text-color;
		font-weight: bold;
		font-size: $uni-font-size-lg;
		max-width: 300rpx;
	}

	.text-normal {
		color: $uni-text-color;
		font-size: $uni-font-size-sm;
	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-ssm;
	}

	.margin {
		margin: 30rpx 30rpx 30rpx 30rpx;
	}

	.image-cover {
		width: 240rpx;
		height: 240rpx;
		border-top-left-radius: 10rpx;
		border-bottom-left-radius: 10rpx;
	}


	.hover {
		background-color: #eee;
		opacity: 0.6;
	}
</style>
