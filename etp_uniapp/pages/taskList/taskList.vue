<template>
	<view class="page">

		<z-paging ref="paging" v-model="tasks" @query="queryTask" class="z-paging">
			<view solt="top" class="margin">
				<u-search placeholder="请输入任务名称" v-model="keyword" bgColor="#ffffff" :show-action="true" actionText="搜索"
					:animation="true" @custom="search" @search="search">
				</u-search>
				<!-- 标签筛选 -->
				<!-- <view class="tags-root"  v-if="tags!=null&&tags.length>0"> -->
				<view class="tags-root"  >
					<scroll-view class="scroll-nowrap" :scroll-x="true">
						<view class="tags-list">
							<view v-for="(item, index) in tags.slice(0,10)" :key="index" class="tags-margin">
								<u-tag :text="item.name" type="primary" :plain="!item.checked" :name="index" plainFill
									size="mini" borderColor="#E5EEF9" @click="checkboxClick"></u-tag>

							</view>
						</view>
					</scroll-view>
					<view hover-class="u-hover-class" @click="showCheckBox"
						style="display: flex;justify-content: center;padding-left: 10rpx;">
						<u--text prefixIcon="grid" iconStyle="font-size:20px;color:#007DFF" text="筛选" color="#007DFF"
							size="24rpx"></u--text>
					</view>

				</view>
			</view>

			<view v-for="(item,index) in tasks" :key="index" @click="taskClick(item)" class="list-box">
				<text :class="stateStyle[judgeState(item)]" class="badge-normal">{{item.rwjd}}</text>

				<view class="flex-row">

					<image :src="teachCover(item.fmt)" class="image-cover" mode="aspectFill"></image>
					<view class="flex-col">
						<text class="ell-text">{{item.mc}}</text>
						<text class="text-small-grey">{{item.ms?item.ms:""}}</text>
						<text class="text-small-grey ">{{item.taskBeginTime}}-{{item.taskEndTime}}</text>
						<view class="tag-flex-row">
							<view v-for="(tag,tagIndex) in splitTags(item.tags)" :key="tagIndex">
								<text class="tag-blue">{{tag}}</text>
							</view>
						</view>

					</view>





				</view>
			</view>

		</z-paging>
		<!-- 全部筛选框 弹窗-->
		<u-popup :show="checkBoxShow" mode="left" @close="close" @open="open">
			<view class="popup-root">
				<text class="text-bold">请选择标签（可多选）</text>
				<scroll-view class="scroll-vertical" :scroll-y="true" :show-scrollbar="true">
					<view class="tags-list-wrap">
						<view v-for="(item, index) in tags" :key="index" class="tags-margin tags-margin-top">
							<u-tag :text="item.name" type="primary" :plain="!item.checked" :name="index" plainFill
								borderColor="#E5EEF9" @click="checkboxClick"></u-tag>

						</view>
					</view>
				</scroll-view>
				<view class="fixed-bottom">
					<view class="btn-root">



						<view class="btn-normal" @click="resetTags()" hover-class="u-hover-class">重置</view>
						<view class="btn-primary" @click="btnAffrim()" hover-class="u-hover-class">确认</view>

					</view>
				</view>

			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				keyword: '',
				tasks: [],
				stateStyle: [
					'cover-red', //未开始 红色
					'cover-blue', //正在进行 蓝色
					'cover-green', //已完成 绿色
					'cover-grey' //已结束 灰色
				],
				checkBoxShow: false,
				tags: [],
				checkTags: "", //选中的标签id

			}
		},
		onLoad() {
			this.getTaskTag();
		},
		methods: {
			search() {
				this.$refs.paging.reload();
			},
			type(val) {
				switch (val) {
					case 0:
						return "未开始"
					case 1:
						return "已完成"
					case 2:
						return "已结束"
					case 3:
						return "正在进行"

					default:
						break;
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
			//请求任务
			queryTask(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: false
				});

				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"mc": this.keyword,
					"tags": this.checkTags
				}
				uni.$u.api.getTaskList(params).then(res => {
					uni.hideLoading();
					self.$refs.paging.completeByTotal(res.rows,res.total,true);

				});


			},
			taskClick(item) {
				//FIXME
				// uni.navigateTo({
				// 	url: '/pages/taskIndex/taskIndex?' + 'taskId=' + item.id + "&cover=" + item.fmt + "&jd=" + item
				// 		.jd,
				// 	success: res => {},
				// 	fail: () => {},
				// 	complete: () => {}
				// });
				
				//fixme
				uni.navigateTo({
					url: '/pages/taskIndex/taskIndex?' + 'taskId=' + item.id + "&cover=" + item.fmt + "&jd=" + item
						.jd,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
				
			},
			//判断任务状态
			judgeState(item) {
				//判断任务开始结束时间  显示未开始  已结束
				let beginTime = this.formatTime(item.taskBeginTime);
				let endTime = this.formatTime(item.taskEndTime);
				let now = new Date().getTime();
				if (now < beginTime) { //任务未开始
					item.rwjd = "未开始";
					return 0;
				} else if (now < endTime) { //任务进行中
					//判断任务的进度  展示已完成xx%  已完成
					let jd = item.jd;
					if (jd) {
						let rate = JSON.parse(jd).rate;
						if (rate) {
							rate = Math.round(rate);
							if (rate == 100) {
								item.rwjd = "已完成";
								return 2;
							} else {
								item.rwjd = "完成" + rate + "%";
								return 1;
							}
						}else{
							item.rwjd = "完成0%";
							return 1;
						}
						

					} else { //没有进度证明还没开始即已完成0%
						item.rwjd = "完成0%";
						return 1;
					}

				} else { //任务已结束
					item.rwjd = "已结束";
					return 3;
				}

			},
			//返回时间戳
			formatTime(time) {
				return (new Date(time)).getTime();;
			},
			checkboxClick(name) {

				this.tags[name].checked = !this.tags[name].checked
				//遍历tags 所有选中的tag的id 
				this.checkTags = "";

				for (let i = 0; i < this.tags.length; i++) {
					if (this.tags[i].checked) {
						this.checkTags = this.checkTags.concat(this.tags[i].id + ",");
					}

				}
				console.log("选中的tag", this.checkTags);
				//查询选中的tag
				this.$refs.paging.reload();


			},
			showCheckBox() {
				this.checkBoxShow = !this.checkBoxShow;
			},
			open() {},
			close() {
				this.checkBoxShow = false
			},
			//点击重置按钮
			resetTags() {
				//将标签中所有的checked属性置为false
				for (var i = 0; i < this.tags.length; i++) {
					this.tags[i].checked = false;
				}
				this.checkTags = "";
				this.$refs.paging.reload();


			},
			//点击确认按钮
			btnAffrim() {
				this.close();
			},
			
			getTaskTag() {

				uni.$u.api.getTaskTags().then(res => {
					let originalTags = res.data;
					if (originalTags) {
						for (var i = 0; i < originalTags.length; i++) {
							originalTags[i].checked = false;
						}
						this.tags = originalTags;
					}

				}).catch(err => {
					console.log(err.errMsg);
				})
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
		// width: 100rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;
	}


	.cover-red {
		background-color: rgba(221, 11, 11, 0.2);
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #DD0B0B;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.cover-green {
		background-color: rgba(17, 188, 77, 0.2);
		// width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #11BC4D;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.cover-grey {
		background-color: #EEEEEE;
		// width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #909199;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;
	}


	.list-box {

		position: relative;
		background-color: #FFFFFF;
		border-radius: 20rpx;
		margin-top: 24rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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




	.tag-blue {
		background-color: rgba(0, 125, 255, 0.2);
		padding: 6rpx 10rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-sm;
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
		font-size: $uni-font-size-base;
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
		width: 380rpx;
		
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

	.badge-normal {
		position: absolute;
		top: 0;
		right: 0;
		height: 40rpx;
		border-bottom-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;

	}


	.image-cover {
		width: 280rpx;
		height: 200rpx;
		border-top-left-radius: 20rpx;
		border-bottom-left-radius: 20rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-around;
		margin-left: 35rpx;
		margin-bottom: 10rpx;
		margin-top: 10rpx;
		flex:1;
		width: 0;
	}

	.tags-margin {
		margin-right: 20rpx;

	}

	.tags-margin-top {
		margin-top: 20rpx;
	}

	.tags-root {
		display: flex;
		flex-direction: row;
		margin-top: 20rpx;
	}

	.scroll-nowrap {
		white-space: nowrap; //保持单行不换行
		width: 85%;
	}

	.scroll-vertical {
		white-space: nowrap; //保持单行不换行
		height: 70vh;
	}

	.tags-list {
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
	}

	.tags-list-wrap {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;

	}


	.popup-root {
		width: 400rpx;
		padding: 30rpx;
		margin-top: 40rpx;

	}

	.fixed-bottom {
		position: fixed;
		bottom: 50px;
		width: 400rpx;
	}

	.text-bold {
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
		font-weight: bold;
	}

	.btn-root {
		display: flex;
		flex-direction: row;

	}

	.btn-primary {
		border-radius: 10rpx;
		background-color: #037EFF;
		color: white;
		flex: 1;
		margin-top: 30rpx;
		margin-bottom: 40rpx;
		margin-left: 20rpx;
		margin-right: 30rpx;
		padding: 20rpx;
		font-size: 28rpx;
		text-align: center;
	}

	.btn-normal {
		flex: 1;
		border-radius: 10rpx;
		background-color: rgba(0, 125, 255, 0.1);
		padding: 20rpx;
		color: #037EFF;
		margin-top: 30rpx;
		margin-right: 20rpx;
		margin-left: 30rpx;
		margin-bottom: 40rpx;
		font-size: 28rpx;
		text-align: center;

	}



	.btn-default {
		background-color: rgba($color: $uni-color-primary, $alpha: 0.2);
		width: 150rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-lg;
		border: none;
	}
</style>
