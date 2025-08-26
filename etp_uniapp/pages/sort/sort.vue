<!-- 知识库 -->
<template>
	<view class="page">

		<z-paging ref="paging" v-model="lessons" @query="querylesson">
			<view solt="top" class="margin">
				<u-search placeholder="搜索发现更大的世界" v-model="keyword" bgColor="#ffffff" :show-action="true" actionText="搜索"
					:animation="true" @custom="search" @search="search">
				</u-search>


				<!-- 标签筛选 -->
				<view class="tags-root" v-if="tags!=null&&tags.length>0">
				
					<view v-for="(item, index) in tags.slice(0,16)" :key="index"
						class="tags-margin k-flex-row k-flex-col-center k-flex-shrink ">
						<u-tag :text="item.name" type="primary" :plain="!item.checked" :name="index" plainFill
							size="mini" borderColor="#E5EEF9" @click="checkboxClick"></u-tag>
						<view @click="showCheckBox" hover-class="k-hover" class="k-margin-start-m"
							v-if="index==(tags.length-1)||index==15">
							<u--text prefixIcon="grid" iconStyle="font-size:25px;color:#007DFF" text="全部"
								color="#007DFF" size="14"></u--text>
						</view>

					</view>
					
					<!-- 	<view hover-class="u-hover-class" @click="showCheckBox"
						style="display: flex;justify-content: center;padding-left: 10rpx; flex-shrink: 0; flex: 1;">
						<u--text prefixIcon="grid" iconStyle="font-size:20px;color:#007DFF" text="全部" color="#007DFF"
							size="24rpx"></u--text>
					</view> -->

				</view>
				<view class="k-margin-top-l" v-if="true">
					<text :class="[isTj?'text-unfocus':'text-focus']" @click="newest()">最新</text>
					<text :class="[isTj?'text-focus':'text-unfocus']"  style="margin-left: 12px;" @click="tj()">推荐</text>
				</view>

			</view>


			<view class="flex-grid">
				<view v-for="(item,index) in lessons" :key="index" @click="lessonClick(item,index)"
					hover-class="u-hover-class" class="list-box">
					<view style="position: absolute; top: 50px; right: 4px;z-index: 99;" v-if="item.sftj==1">
							<uni-icons type="vip" size="28" color="orange"></uni-icons>
					</view>
					<view :class="[item.getStatus==0?'label-free':'label-acquired']">{{item.getStatus==0?'免费':'已领'}}
					</view>
					<!-- <view class="label-acquired">已领取</view> -->
				
					<image :src="teachCover(item.fmt)" class="cover-image" mode="aspectFill"></image>

					<view class="content-margin">
						<text class="ell-text">{{item.mc}}</text>
						<text class="ell-text"
							style="color: #606266;font-size: 28rpx;font-weight: normal;">{{item.ms?item.ms:" "}}</text>
						<view class="flex-row-between">
							<text class="text-small-grey" v-show="false">{{item.createBy?item.createBy:"管理员"}}</text>
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
		<float-ask></float-ask>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isTj: false,
				keyword: '',
				lessons: [],
				checkBoxShow: false,
				tags: [],
				checkTags: "", //选中的标签id
			
			}
		},
		onLoad() {
			this.getLessonTag();
		},
		onShow() {
			let index = getApp().globalData.knowledgeIndex;

			if (index && index != -1) {
				this.lessons[index].getStatus = "1";
				this.lessons[index].getStatusLabel = "已领取";
				getApp().globalData.knowledgeIndex = -1; //重置
			}
			// this.$refs.paging.refresh();
		},

		methods: {
			//最新
			newest(){
				this.isTj =false;
				this.search()
			},
			//推荐
			tj(){
				this.isTj = true;
				this.search()
			},
			
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
			//仅展示2个标签  多的展示不下
			splitTags(tags) {
				//将标签拆分成数组 后取前2个
				if (tags != null && tags.length > 0) {
					let tagsArray = tags.split(",");
					return tagsArray.slice(0, 2);
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
					"tags": this.checkTags,
					"sftj":this.isTj?'1':''
				}
				uni.$u.api.getKnowledgeList(params).then(res => {
					uni.hideLoading();

					self.$refs.paging.completeByTotal(res.rows, res.total, true);

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
				// console.log("选中的tag", this.checkTags);
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
			lessonClick(item, index) {
				// uni.navigateTo({
				// 	url: '/pages/lesson/lesson?' + 'id=' + item.id + "&title=" + item.mc + "&knowledgeIndex=" +
				// 		index,
				// 	success: res => {},
				// 	fail: () => {},
				// 	complete: () => {}
				// });
				uni.navigateTo({
					url: '/pages/lessonIndexInfo/lessonIndexInfo?' + 'id=' + item.id + "&title=" + item.mc +
						"&knowledgeIndex=" +
						index,
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
			}


		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F8F8F8;
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
		margin-right: 12rpx;
		margin-top: 20rpx;
	}

	.tags-margin-top {
		margin-top: 20rpx;

	}

	.tags-root {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		justify-content: flex-start;
		margin-top: 10rpx;
	}

	.scroll-nowrap {
		white-space: nowrap; //保持单行不换行
		width: 85%;
	}

	.scroll-vertical {
		white-space: nowrap; //保持单行不换行
		height: 75vh;
		border-bottom: solid 1px #f8f8f8;
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
		bottom: 30px;
		width: 400rpx;
		background-color: #FFFFFF;
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
		border-top-right-radius: 20rpx;
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
		border-top-right-radius: 20rpx;
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
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;

	}

	.list-box {
		position: relative;
		width: 320rpx;
		display: flex;
		flex-direction: column;
		background-color: #FFFFFF;
		border-radius: 20rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 2px 12px 0 rgba($color: #000000, $alpha: 0.1);
	}

	.content-margin {
		margin: 10rpx 20rpx 20rpx 20rpx;
	}


	.expand {
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
	}

	.notExpand {}
	
	.text-focus{
		color: #333;
		font-weight: bold;
		font-size: 16px;
	}
	.text-unfocus{
		color: #999;
		font-size: 14px;
		font-weight: normal;
		
	}
	
	
</style>
