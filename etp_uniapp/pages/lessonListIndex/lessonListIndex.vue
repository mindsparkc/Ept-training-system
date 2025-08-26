<!-- 我的课程&全部课程 -->
<template>
	<view>
		<z-paging ref="paging" v-model="lessons" @query="queryAllLesson">
			<view solt="top">
				<u-gap height="10" bgColor="#f8f8f8"></u-gap>
				<view class=" k-margin-l k-flex-row k-flex-row-between">
					<text class="k-text-bold k-text-black k-text-xl ">我的课程</text>
					<view class="k-flex-row k-flex-col-center" @click="toLessonList()" hover-class="k-hover">
						<text class="k-text-light-grey k-text-md">更多</text>
						<u-icon name="arrow-right" color="#909399" size="14"></u-icon>
					</view>

				</view>
				<view class="k-margin-l" v-if="myLessons&&myLessons.length>0">
					<scroll-view :scroll-x="true" style="white-space: nowrap;width: 100%;">
						<view v-for="(lessonItem,lessonIndex) in myLessons" :key="lessonIndex"
							 @click="lessonClick(lessonItem)"
							v-if="lessons&&lessons.length>0" style="display: inline-block;vertical-align:top;">
							<view style="margin-right: 20rpx;position: relative;">
								<text class="label-rate">{{getLessonRate(lessonItem.jd)}}</text>
								<u--image :src="teachCover(lessonItem.fmt)" :showLoading="true" mode="aspectFill"
									width="280rpx" height="180rpx" :fade="true" radius="5"></u--image>

								<view style="margin-top: 20rpx; width:280rpx">
									<u-text lines="1" :text="lessonItem.mc" :size="14"></u-text>
								</view>


							</view>

						</view>
						<view
							style="background-color: #f3f5f7; border-radius:5px;text-align: center;line-height: 180rpx; height:180rpx;width: 200rpx; display:inline-block;color: #909199;font-size: 16px;"
							@click="toLessonList">查看更多</view>
						<!-- </view> -->
					</scroll-view>
				</view>
				<view class="k-flex-col k-flex-col-center" v-else>
					<image src="../../static/common/empty.png" style="width: 100px;height: 100px"></image>
					<text class="k-text-grey k-text-sm">暂无内容</text>
				</view>

				<u-gap height="10" bgColor="#f8f8f8" marginTop="15"></u-gap>

				<view class="margin">
					<view class="k-text-bold k-text-black k-text-xl k-margin-bottom-l">全部课程</view>
					<u-search placeholder="请输入课程名称" v-model="keyword" bgColor="#f8f8f8" :showAction="true"
						actionText="搜索" :animation="true" @custom="search" @search="search" shape="square">
					</u-search>

					<!-- 标签筛选 -->
					<view class="tags-root" v-if="tags!=null&&tags.length>0">
						<scroll-view class="scroll-nowrap" :scroll-x="true">
							<view class="tags-list">
								<view v-for="(item, index) in tags.slice(0,10)" :key="index" class="tags-margin">
									<u-tag :text="item.name" type="primary" :plain="!item.checked" :name="index"
										plainFill size="mini" borderColor="#E5EEF9" @click="checkboxClick"></u-tag>

								</view>
							</view>

						</scroll-view>
						<view hover-class="u-hover-class" @click="showCheckBox"
							style="display: flex;justify-content: center;padding-left: 10rpx;">
							<u--text prefixIcon="grid" iconStyle="font-size:20px;color:#007DFF" text="筛选"
								color="#007DFF" size="24rpx"></u--text>
						</view>

					</view>
				</view>

			</view>


			<view class="flex-grid">
				<view v-for="(item,index) in lessons" :key="index" @click="lessonClick(item)"
					 class="list-box">

					<view :class="[item.getStatus==0?'label-free':'label-acquired']">{{item.getStatus==0?'免费':'已领'}}
					</view>

					<image :src="teachCover(item.fmt)" class="cover-image" mode="aspectFill"></image>

					<view class="content-margin">
						<text class="ell-text">{{item.mc}}</text>
						<text class="ell-text"
							style="color: #606266;font-size: 28rpx;font-weight: normal;">{{item.ms?item.ms:"&emsp; "}}</text>
						<view class="flex-row-between">
							<text class="text-small-grey" >{{item.createBy?item.createBy:"管理员"}}</text>
							<text class="text-small-grey">{{item.num?item.num:"1"}}人已学</text>

						</view>

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
				lessons: [],
				myLessons: [],
				checkBoxShow: false,
				tags: [],
				checkTags: "", //选中的标签id
			}
		},
		onLoad() {
			this.getLessonTag();
			this.querylesson(1, 2); //展示2节课剩余更多中展示
		},
		onShow() {

		},

		methods: {
			search() {
				this.$refs.paging.reload();
			},

			//仅展示2个标签  多的展示不下
			splitTags(tags) {
				//将标签拆分成数组 后取前2个
				if (tags != null && tags.length > 0) {
					let tagsArray = tags.split(",");
					return tagsArray.slice(0, 2);
				}


			},
			//请求全部课程列表
			queryAllLesson(pageNo, pageSize) {
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
				uni.$u.api.getKnowledgeList(params).then(res => {
					uni.hideLoading();

					self.$refs.paging.completeByTotal(res.rows, res.total, true);

				});

			},

			//请求课程列表
			querylesson(pageNo, pageSize) {
				let self = this;
				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
				}
				uni.$u.api.getLessonList(params).then(res => {
					self.myLessons = res.rows
				}).catch(err => {
					console.log(err.errMsg);
				});

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
			lessonClick(item) {

				uni.navigateTo({
					url: '/pages/lessonIndexInfo/lessonIndexInfo?' + 'id=' + item.id,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

			},
			getLessonTag() {

				uni.$u.api.getLessonTags().then(res => {
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

			toLessonList() {
				uni.navigateTo({
					url: '/pages/lessonList/lessonList',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			//获取课程进度
			getLessonRate(jd) {
				if (jd) {
					let jdObj = JSON.parse(jd);
					let rate = Math.round(jdObj.rate);
					if (rate) {
						if (rate == 100) {
							return "已学完";
						} else {
							return "已学" + Math.round(rate) + "%";
						}
					} else {
						return "未学习"
					}

				} else {
					return "未学习"
				}

			},



		}
	}
</script>

<style lang="scss">
	page {
		background-color: #fff;
	}



	.margin {
		margin: 30rpx 30rpx 0rpx 30rpx;
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


	.tag-flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		flex-wrap: nowrap;
		width: 320rpx;
		overflow: hidden;
		margin-top: 10rpx;

	}

	.flex-row-between {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-top: 10rpx;
		justify-content: space-between;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
	}


	.tag-blue {
		background-color: rgba(0, 125, 255, 0.1);
		padding: 6rpx 10rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-sm;
		border-radius: 4rpx;
		margin-right: 8rpx;
	}

	.tag-grey {
		background-color: rgba(44, 44, 44, 0.2);
		padding: 6rpx 10rpx 6rpx 10rpx;
		color: #909199;
		font-size: $uni-font-size-ssm;
		border-radius: 4rpx;
		margin-right: 8rpx;
	}

	.text-sub {
		color: $uni-text-color-content;
		font-size: $uni-font-size-base;

	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-sm;

	}

	.ell-text {
		width: 280rpx;
		color: $uni-text-color;
		font-size: $uni-font-size-lg;
		text-overflow: ellipsis;
		word-break: break-all;
		display: -webkit-box;
		font-weight: bold;
		/** 对象作为伸缩盒子模型显示 **/
		-webkit-box-orient: vertical;
		/** 设置或检索伸缩盒对象的子元素的排列方式 **/
		-webkit-line-clamp: 1;
		/** 显示的行数 **/
		overflow: hidden;
		/** 隐藏超出的内容 **/
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
		height: 75vh;
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

	.btn-primary {
		background-color: $uni-color-primary;
		width: 150rpx;
		color: #FFFFFF;
		font-size: $uni-font-size-lg;
		border: none;
	}

	.btn-default {
		background-color: rgba($color: $uni-color-primary, $alpha: 0.2);
		width: 150rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-lg;
		border: none;
	}

	.label-free {
		border-top-right-radius: 5px;
		border-bottom-left-radius: 20rpx;
		background-color: RGBA(252, 112, 50, 0.8);
		padding: 4rpx 0rpx;
		color: #FFFFFF;
		font-size: 20rpx;
		position: absolute;
		width: 80rpx;
		text-align: center;
		right: 0;
		z-index: 99;
	}

	.label-acquired {
		border-top-right-radius: 5px;
		border-bottom-left-radius: 20rpx;
		background-color: RGBA(5, 196, 109, 0.8);
		padding: 4rpx 0rpx;
		color: #FFFFFF;
		font-size: 20rpx;
		position: absolute;
		width: 80rpx;
		text-align: center;
		right: 0;
		z-index: 99;
	}

	.flex-grid {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-top: 24rpx;
	}

	.cover-image {
		width: 320rpx;
		height: 166rpx;
		border-top-left-radius: 5px;
		border-top-right-radius: 5px;

	}

	.list-box {
		position: relative;
		width: 320rpx;
		display: flex;
		flex-direction: column;
		background-color: #FFFFFF;
		border-radius: 5px;
		margin-bottom: 30rpx;
		box-shadow: 0 2px 12px 0 rgba($color: #000000, $alpha: 0.1);
	}

	.content-margin {
		margin: 10rpx 20rpx 20rpx 20rpx;
	}

	.label-rate {
		border-top-right-radius: 5px;
		border-bottom-left-radius: 8px;
		background: #f46b45;
		background: linear-gradient(to left, #f46b45, #eea849);
		padding: 4rpx 10rpx;
		color: #FFFFFF;
		font-size: 20rpx;
		position: absolute;
		text-align: center;
		right: 0;
		z-index: 99;
	}
</style>
