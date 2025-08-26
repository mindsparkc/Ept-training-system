<template>
	<view class="page">
		<z-paging ref="paging" v-model="list" @query="queryList">
			<!-- 需要固定在顶部不滚动的view放在slot="top"的view中 -->
			<view slot="top">
				<view style="padding: 20rpx 20rpx 0rpx;">
					<u-search placeholder="请输入问卷名称" v-model="keyword" bgColor="#ffffff" @search="search" :show-action="true" actionText="搜索" :animation="true" @custom="search">
					</u-search>
				</view>
			</view>
			<!-- 问卷列表 -->
			<view v-for="(item,index) in list" :key="index" @click="startQuestion(item)">

				<view class="list-box">
					<view style="margin: 24rpx 20rpx 24rpx 20rpx;">
						<u--image :src="teachCover(item.fm)" :showLoading="true" mode="aspectFill" width="240rpx" height="140rpx"
							:fade="true" radius="5"></u--image>
						<view class="tag-box">
							<u-tag v-if="item.status=='未开始'" :text="item.status" size="mini" borderColor="#FEA801"
								bgColor="#FEA801" color="#ffffff"></u-tag>
							<u-tag v-if="item.status=='进行中'" :text="item.status" size="mini" borderColor="#2FCCA7"
								bgColor="#2FCCA7" color="#ffffff"></u-tag>
							<u-tag v-if="item.status=='已结束'" :text="item.status" size="mini" borderColor="#EEEEEE"
								bgColor="#EEEEEE" color="#919199"></u-tag>
							<u-tag v-if="item.status=='已完成'" :text="item.status" size="mini" borderColor="#EEEEEE"
								bgColor="#EEEEEE" color="#919199"></u-tag>
						</view>
					</view>



					<view class="flex-col">

						<text class="ell-text">{{item.wjmc}}</text>
						<text class="text-des">{{item.ms?item.ms:""}}</text>
						<count-down tipText="距开始" :isBig="false" :end-time="item.beginTime"
							background="rgba(254, 168, 1, 0.1)" color="#FEA801" text-color="#919199"
							v-if="item.status=='未开始'" />
						<count-down tipText="距结束" :isBig="false" :end-time="item.endTime"
							background="rgba(254, 168, 1, 0.1)" color="#FEA801" text-color="#919199"
							v-if="item.status=='进行中'" />
						<text class="text-small-grey"
							v-if="item.status=='已完成'||item.status=='已结束'">{{item.beginTime}}至{{item.endTime}}</text>

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
				list: [{

				}]
			}
		},
		onShow() {
			if (this.$refs.paging) {
				this.$refs.paging.refresh();
			}
		},
		methods: {
			queryList(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: false
				});

				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"wjmc": this.keyword,
				}
				uni.$u.api.getQuestionnaireList(params).then(res => {
					uni.hideLoading();
					self.$refs.paging.completeByTotal(res.rows,res.total,true);

				}).catch((err) => {
					uni.hideLoading();
					console.log(err);
				});


				// //这里的pageNo和pageSize会自动计算好，直接传给服务器即可
				// uni.showLoading({
				//     title: '加载中'
				// });

				// let that = this;
				// setTimeout(function() {
				// 	uni.hideLoading()
				// 	that.list = [{
				// 				"title": "问卷名称1",
				// 				"endDay": "02",
				// 				"endTime": "12:20:32",
				// 				"jd":"进行中",
				// 				"cover": "https://cdn.uviewui.com/uview/swiper/swiper3.png"
				// 			},
				// 			{
				// 				"title": "Vue3问卷名称",
				// 				"endDay": "02",
				// 				"endTime": "12:20:32",
				// 				"jd":"已结束",
				// 				"cover": "https://cdn.uviewui.com/uview/swiper/swiper3.png"
				// 			}
				// 		];
				// 	//通过this.$refs.paging.complete(请求回来的数组);将请求结果传给z-paging
				// 	that.$refs.paging.complete(that.list);
				// }, 3000);
			},
			search() {
				this.$refs.paging.reload();
			},
			startQuestion(item) {
				//未开始 进行中 已结束 已完成
				if (item.status == '未开始') { //进行中可以点击进入修改
					uni.showToast({
						title: '本次问卷' + item.status,
						icon: 'none'
					});


				} else {
					if (item.status == '已结束') {
						uni.navigateTo({
							url: "/pages/questionnaire/questionBegin?id=" + item.id + "&title=" + item.wjmc +
								"&isEnded=" + true,
							success: res => {},
							fail: () => {},
							complete: () => {}
						});
					} else {
						uni.navigateTo({
							url: "/pages/questionnaire/questionBegin?id=" + item.id + "&title=" + item.wjmc +
								"&isEnded=" + false,
							success: res => {},
							fail: () => {},
							complete: () => {}
						});
					}

				}


			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F8F8F8;
	}

	.tag-box {
		position: absolute;
		top: 24rpx;
		z-index: 99;
	}

	.ell-text {
		color: $uni-text-color;
		font-size: $uni-font-size-base;
		font-weight: bold;
		text-overflow: ellipsis;
		word-break: break-all;
		display: -webkit-box;
		/** 对象作为伸缩盒子模型显示 **/
		-webkit-box-orient: vertical;
		/** 设置或检索伸缩盒对象的子元素的排列方式 **/
		-webkit-line-clamp: 2;
		/** 显示的行数 **/
		overflow: hidden;
		/** 隐藏超出的内容 **/

	}
	.text-des {
		color: $uni-text-color;
		font-size: $uni-font-size-sm;
		text-overflow: ellipsis;
		word-break: break-all;
		display: -webkit-box;
		/** 对象作为伸缩盒子模型显示 **/
		-webkit-box-orient: vertical;
		/** 设置或检索伸缩盒对象的子元素的排列方式 **/
		-webkit-line-clamp: 1;
		/** 显示的行数 **/
		overflow: hidden;
		/** 隐藏超出的内容 **/
	
	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-sm;
		margin-top: 20rpx;

		.text-end-time {
			color: #FEA801;
			margin: 0 10rpx;
		}
	}

	.list-box {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		margin: 30rpx;
		position: relative;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		flex: 1;
		margin: 24rpx 20rpx 24rpx 0rpx;
		overflow: hidden;
		width: 0;
	}

	.btn-margin {
		margin-top: 14rpx;
		width: 176rpx;
		align-self: flex-end;
	}
</style>
