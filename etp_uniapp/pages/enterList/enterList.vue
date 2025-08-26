<template>
	<view class="page">

		<z-paging ref="paging" v-model="list" @query="queryEnterList" class="z-paging">
			<view solt="top" class="margin">
				<u-search placeholder="请输入活动名称" v-model="keyword" bgColor="#ffffff" :show-action="true" actionText="搜索" :animation="true" @custom="search"
					@search="search">
				</u-search>
			</view>

			<view v-for="(item,index) in list" :key="index" @click="enterClick(item)">
				<view class="item-flex-col">
					<view class="list-box">
						<view style="margin: 24rpx 20rpx; position: relative;">
							<u--image :src="teachCover(item.hdfm)" :showLoading="true" mode="aspectFill" width="240rpx"
								height="140rpx" :fade="true" radius="10rpx"></u--image>

							<view :class="[item.joinStatus=='已报名'?'bg-top-state-signed':'bg-top-state']">
								{{item.joinStatus}}
							</view>
						</view>
						<view class="flex-col">
							<text class="ell-text">{{item.hdmc}}</text>

							<text class="text-small-grey ">{{item.hdfmc?item.hdfmc:""}}</text>

							<view class="tag-flex-row">
								<view v-for="(tag,tagIndex) in splitTags(item.tags)" :key="tagIndex">
									<text class="tag-blue">{{tag}}</text>
								</view>
							</view>

						</view>
					</view>
					<view class="line"></view>
					<view class="flex-row">
						<count-down tipText="距开始" :end-time="item.kssj" background="rgba(254, 168, 1, 0.1)"
							color="#FEA801" text-color="#919199" v-if="item.status==-1" />
						<count-down v-else tipText="距截止" :end-time="item.jssj" background="rgba(254, 168, 1, 0.1)"
							color="#FEA801" text-color="#919199" />
						<view :class="[blueOrGreyBg(item.status,item.joinStatus)?'btn-normal':'btn-grey']"
							hover-class="u-hover-class">{{btnText(item.status,item.joinStatus)}}</view>
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
				keyword: '',
				list: [],

			}
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

			blueOrGreyBg(status, joinStatus) { //活动状态 -1 未开始 0 进行中 1已结束
				//已经结束  灰色背景 展示报名状态
				//正在进行 未报名：蓝色背景 展示未报名  已经报名：灰色背景 展示已经报名
				//未开始  展示灰色背景 未开始
				if (status == -1 || status == 1 || (status == 0 && joinStatus == '已报名')) {
					return false;
				} else {
					return true;
				}
			},
			btnText(status, joinStatus) {
				if (status == -1) {
					return "未开始";
				} else if (status == 0) {
					if (joinStatus == '未报名') {
						return "去报名";
					} else {
						return "已报名";
					}

				} else {
					return "已结束"
				}
			},

			//仅展示四个标签  多的展示不下
			splitTags(tags) {
				//将标签拆分成数组 后取前四个
				if (tags != null && tags.length > 0) {
					let tagsArray = tags.split(",");
					return tagsArray.slice(0, 4);
				}


			},
			//请求活动报名
			queryEnterList(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: false
				});

				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"hdmc": this.keyword,
				}
				uni.$u.api.getEnterList(params).then(res => {
					uni.hideLoading();
					self.$refs.paging.completeByTotal(res.rows,res.total,true);

				}).catch(err => {
					uni.hideLoading();
					console.log("getEnterList>>>>" + err.errMsg);
					uni.showToast({
						title: err.errMsg || '请求失败',
						icon: 'none'
					});
				});;

				// let self = this;
				// setTimeout(function() {
				// 	uni.hideLoading()
				// 	let tasks = [{
				// 			"title": "Vue3从入门到实战进阶式掌握完整知识体系进阶式掌握完整知识体系进阶式掌握完整知识体系",
				// 			"progress": "14",
				// 			"state": 0,
				// 			"startDate": "",
				// 			"endDate": "",
				// 			"tags": [
				// 				"UI",
				// 				"PS"
				// 			],
				// 			"cover": "https://cdn.uviewui.com/uview/swiper/swiper3.png"
				// 		},
				// 		{
				// 			"title": "Vue3从入门",
				// 			"progress": "12",
				// 			"startDate": "",
				// 			"endDate": "",
				// 			"state": 1,
				// 			"tags": ["UI", "PS", "UI", "PS", "UI", "PS", "UI", "PS"],
				// 			"cover": "https://cdn.uviewui.com/uview/swiper/swiper3.png"
				// 		}
				// 	];
				// 	self.$refs.paging.complete(tasks);

				// }, 100);
			},
			enterClick(item) {

				uni.navigateTo({
					url: '/pages/enter/enter?' + 'enterId=' + item.id,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},


		}
	}
</script>

<style lang="scss">
	.page {
		background-color: #F8F8F8;
		height: 100vh;

	}

	.margin {
		padding: 30rpx 30rpx 0rpx 30rpx;
	}

	.cover-blue {
		background-color: rgba(0, 125, 255, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 4rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;
	}


	.cover-red {
		background-color: rgba(221, 11, 11, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 4rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #DD0B0B;
		font-size: $uni-font-size-ssm;
	}

	.cover-green {
		background-color: rgba(17, 188, 77, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 4rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #11BC4D;
		font-size: $uni-font-size-ssm;
	}

	.cover-grey {
		background-color: rgba(44, 44, 44, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 4rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #909199;
		font-size: $uni-font-size-ssm;
	}

	.list-box {
		display: flex;
		flex-direction: row;
		justify-content: space-between;

	}

	.flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
		margin: 15rpx 20rpx;
	}

	.tag-flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
		flex-wrap: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		flex: 1;
		margin: 24rpx 20rpx 24rpx 20rpx;
		overflow: hidden;
		width: 0;
	}

	.item-flex-col {
		display: flex;
		flex-direction: column;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		margin-top: 30rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}


	.tag-blue {
		background-color: rgba(0, 125, 255, 0.2);
		padding: 6rpx 10rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
		border-radius: 4rpx;
		margin-right: 8rpx;
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;


	}

	.tag-grey {
		background-color: rgba(44, 44, 44, 0.2);
		padding: 6rpx 10rpx 6rpx 10rpx;
		color: #909199;
		font-size: $uni-font-size-ssm;
		border-radius: 4rpx;
		margin-right: 8rpx;
	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-ssm;
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		width: 350rpx;
	}

	.ell-text {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		color: $uni-text-color;
		font-size: $uni-font-size-base;
		width: 350rpx;
	}

	.line {
		height: 1rpx;
		width: 100%;
		background-color: $uni-bg-color-grey;
	}

	.btn-normal {
		background-color: $uni-color-primary;
		font-size: $uni-font-size-sm;
		color: #FFFFFF;
		padding: 6rpx 16rpx;
		border-radius: 40rpx;
	}

	.btn-grey {
		background-color: #EEEEEE;
		font-size: $uni-font-size-sm;
		color: $uni-text-color-grey;
		padding: 6rpx 16rpx;
		border-radius: 40rpx;
	}

	.bg-top-state {
		position: absolute;
		top: 0;
		left: 0;
		background-color: RGBA(252, 112, 50, 0.8);
		color: #FFFFFF;
		font-size: 20rpx;
		padding: 6rpx 10rpx;
		border-top-left-radius: 10rpx;
		border-bottom-right-radius: 20rpx;
	}
	
	.bg-top-state-signed {
		position: absolute;
		top: 0;
		left: 0;
		background-color: rgba(17, 188, 77, 0.8);
		color: #FFFFFF;
		font-size: 20rpx;
		padding: 6rpx 10rpx;
		border-top-left-radius: 10rpx;
		border-bottom-right-radius: 20rpx;
	}
	
</style>
