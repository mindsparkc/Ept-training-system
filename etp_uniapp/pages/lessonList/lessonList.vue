<template>
	<view class="page">

		<z-paging ref="paging" v-model="lessons" @query="querylesson">
			<view solt="top" class="margin">
				<u-search placeholder="请输入课程名称" v-model="keyword" bgColor="#ffffff" :show-action="true" actionText="搜索"
					:animation="true" @custom="search" @search="search" :clearable="false">
				</u-search>

				<!-- 标签筛选 -->
				<view class="tags-root">
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


			<view v-for="(item,index) in lessons" :key="index" @click="lessonClick(item)" hover-class="u-hover-class">

				<view class="list-box" style="position: relative;">
					<view>
						<u--image :src="teachCover(item.fmt)" :showLoading="true" mode="aspectFill" width="240rpx"
							height="240rpx" :fade="true" radius="5"></u--image>
					</view>

					<view class="flex-col">
						<!-- <u-text lines="2" :text="item.title"></u-text> -->
						<text class="ell-text">{{item.mc}}</text>

						<text class="text-small-content" v-if="item.ms">{{item.ms?item.ms:"暂无描述"}}</text>

						<text class="text-small-grey" v-if="item.createBy">发布人：{{item.createBy}}</text>
						<text class="text-small-grey" v-if="item.createBy">发布时间： {{item.createTime}}</text>
						<view class="tag-flex-row">
							<view v-for="(tag,tagIndex) in splitTags(item.tags)" :key="tagIndex">
								<text class="tag-blue">{{tag}}</text>
							</view>
						</view>

					</view>

					<text class="label-rate" v-if="parseJd(item.jd)">{{parseJd(item.jd)}}</text>


					<!-- <text class="cover-blue">{{type(item.state)}}</text> -->

				</view>


			</view>
		</z-paging>

		<!-- 全部筛选框 弹窗-->
		<u-popup :show="checkBoxShow" mode="left" @close="close" @open="open">
			<view class="popup-root">
				<text class="text-bold">分类</text>
				<view class="tags-list-wrap">
					<view v-for="(item, index) in typeList" :key="index" class="tags-margin tags-margin-top">
						<u-tag :text="item.name" type="primary" :plain="!item.isCheck" :name="index" plainFill
							borderColor="#E5EEF9" @click="typeCheckClick(index)"></u-tag>

					</view>
				</view>
				<text class="text-bold">排序</text>
				<view class="tags-list-wrap">
					<view v-for="(item, index) in sortList" :key="index" class="tags-margin tags-margin-top">
						<u-tag :text="item.name" type="primary" :plain="!item.isCheck" :name="index" plainFill
							borderColor="#E5EEF9" @click="sortCheckClick(index)"></u-tag>

					</view>
				</view>

				<text class="text-bold">标签（可多选）</text>
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

						<!-- 	<button @click="resetTags()" type="default"> 重置 </button>
						<button @click="btnAffrim()" type="primary"> 确认 </button> -->

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
				checkBoxShow: false,
				tags: [],
				checkTags: "", //选中的标签id
				typeFliter: "", //分类筛选
				sortFliter: "", //排序筛选
				sortList: [{
					name: "全部",
					isCheck: true
				}, 
				// {
				// 	name: "最新",
				// 	isCheck: false
				// }, {
				// 	name: "最热",
				// 	isCheck: false
				// },
				],
				typeList: [{
					name: "全部",
					isCheck: true
				}
				// , {
				// 	name: "视频",
				// 	isCheck: false
				// }, {
				// 	name: "文档",
				// 	isCheck: false
				// },
				]
			}
		},
		onLoad() {
			this.getLessonTag();
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
			//请求课程列表
			querylesson(pageNo, pageSize) {
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
				uni.$u.api.getLessonList(params).then(res => {
					uni.hideLoading();
					self.$refs.paging.completeByTotal(res.rows, res.total, true);

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
				//重新加载数据
				this.$refs.paging.reload();
			},
			//分类筛选
			typeCheckClick(index) {
				this.typeFliter = this.typeList[index].name; //Fixme
				for (let k = 0; k < this.typeList.length; k++) {
					if (k == index) {
						this.typeList[k].isCheck = true;
					} else {
						this.typeList[k].isCheck = false;
					}
				
				}

				//重新加载数据
				this.$refs.paging.reload();
			},

			//排序筛选
			sortCheckClick(index) {
				this.sortFliter = this.sortList[index].name; //Fixme
				//单选 全部设置为false

				for (let j = 0; j < this.sortList.length; j++) {
					if (j == index) {
						this.sortList[j].isCheck = true;
					} else {
						this.sortList[j].isCheck = false;
					}

				}

				//重新加载数据
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
				// uni.navigateTo({
				// 	url: '/pages/lesson/lesson?' + 'id=' + item.id + "&title=" + item.mc,
				// 	success: res => {},
				// 	fail: () => {},
				// 	complete: () => {}
				// });

				uni.navigateTo({
					url: '/pages/lessonIndexInfo/lessonIndexInfo?id=' + item.id + "&title=" + item.mc,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
				this.$point.clickEvent(this.$constant.CLI_CURR, item.id);
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
			parseJd(jd) {

				if (jd) {
					let schedule = JSON.parse(jd);

					if (schedule && schedule.rate) {
						return "已学" + Math.round(schedule.rate) + "%"
					}
				} else {
					return "";
				}

			},
			getJdProgress(jd) {
				if (jd) {
					let schedule = JSON.parse(jd);

					if (schedule && schedule.rate) {
						return Math.round(schedule.rate)
					}
				} else {
					return 0;
				}
			}


		}
	}
</script>

<style lang="scss">
	.page {
		background-color: #F8F8F8;
		height: 100vh;
	}



	.margin {
		margin: 30rpx 30rpx 0rpx 30rpx;
	}

	.cover-blue {
		background-color: rgba(0, 125, 255, 0.2);
		width: 120rpx;
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
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #DD0B0B;
		font-size: $uni-font-size-ssm;
	}

	.cover-green {
		background-color: rgba(17, 188, 77, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #11BC4D;
		font-size: $uni-font-size-ssm;
	}

	.cover-grey {
		background-color: rgba(44, 44, 44, 0.2);
		width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #909199;
		font-size: $uni-font-size-ssm;
	}

	.list-box {
		display: flex;
		flex-direction: row;
		background-color: #FFFFFF;
		border-radius: 5px;
		margin-top: 30rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;

	}

	.tag-flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
		flex-wrap: nowrap;

	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-around;
		// flex: 1;
		margin: 10rpx 20rpx 10rpx 20rpx;
		overflow: hidden;
		// width: 0;
	}


	.tag-blue {
		background-color: rgba(0, 125, 255, 0.2);

		padding: 0rpx 8rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
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

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: 12px;
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
		width: 350rpx;
	}

	.text-small-content {
		color: $uni-text-color-content;
		font-size: $uni-font-size-base;
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
		width: 350rpx;
	}

	.ell-text {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		color: $uni-text-color;
		font-size: $uni-font-size-lg;
		width: 300rpx;
		font-weight: bold;
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
		height: 50vh;
		margin-bottom: 50px;
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
		margin-bottom: 12px;
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
		bottom: 30px;
		width: 400rpx;
	}

	.text-bold {
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
		font-weight: bold;
		margin-top: 20px;
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
