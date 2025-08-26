<!-- 错题集 -->
<template>
	<view class="page">

		<kt-empty tips="暂无错题" v-if="isEmpty" class="empty"></kt-empty>
		<view v-else>
			<kt-swiper-errorbook ref="swiper" :list="list" :getPreviousHandler="gph" :getNextHandler="gnh"
				:dataIndex="currentIndex" @change="change">
				</kt-swiper-errorbook>
		</view>
		
		<view class="footer" v-if="list&&list.length>0">
			<view class="row" @click="openAnswerCard" style="margin-left: 30rpx;">
				<u-icon name="grid" color="#919199" size="28"></u-icon>
				<text class="text-bold">{{currentIndex+1}}</text>
				<text class="text-grey">/{{list.length}}</text>

			</view>
			<!-- 上一题 -->
			<view class="flex-row-center" hover-class="u-hover-class" @click="pre()">
				<u-icon name="arrow-left" color="#606266" size="15"></u-icon>
				<text>上一题</text>
			</view>

			<!-- 下一题 -->
			<view class="flex-row-center" hover-class="u-hover-class" @click="next()">
				<text>下一题</text>

				<u-icon name="arrow-right" color="#606266" size="15"></u-icon>
			</view>
			<!-- 删除按钮 -->
			<view style="margin-right: 30rpx; " hover-class="u-hover-class" @click="deleteErrorQuestion">
				<image src="../../static/exam/icon-delete.png" class="small-image"></image>
			</view>
		</view>

		<!-- 答题卡弹窗 -->
		<u-popup :show="showAnswerCard" @close="close" @open="open" mode="bottom" :safeAreaInsetTop="true">
			<view>
				<view class="top-pop">
					<view class="row" @click="close()" style="margin-left: 30rpx;">
						<u-icon name="grid" color="#919199" size="28"></u-icon>
						<text class="text-bold">{{currentIndex+1}}</text>
						<text class="text-grey">/{{list.length}}</text>

					</view>

				</view>
				<scroll-view scroll-y="true" style="height: 900rpx;" scroll-with-animation="true">
					<!-- 按照题目类型分类 -->
					<view class="book">
						<view v-for="(item,index) in classifyQuestionList" :key="index" style="width: 100%;">
							<view class="bg-type">
								{{tranType(item.type)}}({{item.errorQuestions.length}})
							</view>

							<item-error-list-item :list="item.errorQuestions"
								:currentSerialNum="list[currentIndex].serialNum" @clickItem='onClickItem'>
							</item-error-list-item>
						</view>
					</view>

				</scroll-view>
			</view>
		</u-popup>




	</view>
</template>

<script>
	const NO_PREV_PAGE = -1
	const NO_NEXT_PAGE = -2
	export default {
		data() {
			return {
				examId: 0,
				paperId: 0,
				examTime: 0, //考试倒计时
				timeData: {},

				list: [],
				classifyQuestionList: [],
				current: 0, //swiper组件的current值，表示当前那个swiper-item是活动的
				currentIndex: 0,
				swiperDuration: "0",
				showAnswerCard: false, //是否显示答题卡
				showDonePopup: false, //完成提示弹窗
				showUnDonePopup: false, //未完成提示弹窗
				swiperHeight: 0,
				isEmpty: false, //是否为空
			}
		},
		onLoad(option) {
			this.examId = option.examId;
			this.paperId = option.paperId;

			this.title = option.examTitle;
			this.examTime = option.examTime;

			this.swiperHeight = uni.getSystemInfoSync().windowHeight;
			this.requestQuestionInfo();
		},
		onReady() {
			uni.setNavigationBarTitle({
				title: this.title
			});
		},
		methods: {

			pre() {
				this.$refs.swiper.goPrevious()
			},
			next() {
				this.$refs.swiper.goNext()
			},
			gph: () => {
				//当前为第一个滑动触发事件
				uni.showToast({
					title: '已为第一题',
					icon: 'none'
				});

			},
			gnh: () => {
				uni.showToast({
					title: '已为最后一题',
					icon: 'none'
				});

			},
			change(currentIndex) {
				// console.log('currentIndex', currentIndex);
				this.currentIndex = currentIndex
			},
			requestQuestionInfo() {
				//接口请求数据 
				let params = {

				}
				uni.$u.api.getErrorBooks(params).then(res => {
					let questionList = []
					questionList = res.rows
					if (questionList && questionList.length > 0) {
						this.formatQuestionList(questionList)
					} else {
						this.isEmpty = true;
					}



				}).catch(err => {
					console.log(err.errMsg);
				})


			},
			swiperChange(e) {
				console.log("change滑动改变题目序号index>>>>" + JSON.stringify(e) + "当前current" + this.current);

				let current = e.current
				// this.current = current;
				this.currentIndex = current

				// if (current == NO_PREV_PAGE) {
				// 	uni.showToast({
				// 		title: "已经是第一题了",
				// 		icon: "none"
				// 	})
				// 	return
				// }

				// if (current == NO_NEXT_PAGE) {
				// 	uni.showModal({
				// 		title: "提示",
				// 		content: "已经是最后一题了",
				// 	})
				// 	return
				// }
			},

			onClickLast() {
				if (this.currentIndex - 1 < 0) {
					uni.showToast({
						title: '已经是第一题啦',
						icon: 'none'
					});
					return
				}
				this.current = this.currentIndex - 1


			},

			onClickNext() {

				console.log("onClickNext下一题：当前的currentIndex" + this.currentIndex + "当前的current" + this.current);
				if (this.currentIndex + 1 > this.list.length - 1) {
					uni.showToast({
						title: '已经是最后一题啦',
						icon: 'none'
					});

					return
				}
				this.current = this.currentIndex + 1

				console.log("修改后的current", this.current);
			},
			onClickAnswerCardItem(index) {
				this.swiperDuration = '0'; //关闭动画
				this.current = index;
				this.close()
			},

			//打开答题卡
			openAnswerCard() {
				this.showAnswerCard = true;
			},
			//答题卡打开时的回调
			open() {},
			//答题卡关闭回调
			close() {
				this.swiperDuration = '250'; //开启动画
				this.showAnswerCard = false;
			},
			//删除错题
			deleteErrorQuestion() {
				let that = this;
				let itemId = that.list[that.currentIndex].id;


				uni.showModal({
					title: '提示',
					content: '是否从错题集删除本题',
					showCancel: true,

					success: res => {
						if (res.confirm) {
							console.log("确认删除");
							//确认
							//1.调用删除错题接口
							uni.$u.api.deleteErrorBook(itemId).then(res => {
								let question = that.list[that.currentIndex];
								console.log("删除题目列表中对应的项", that.currentIndex);
								console.log("删除题目列表中对应的项", JSON.stringify(that.list));
								//2.删除swiper题目列表中对应的项
								that.list.splice(that.currentIndex, 1);
								//当删除最后一题时
								if (that.currentIndex == that.list.length) {
									that.currentIndex -= 1;
								}
								//若本题删除之后错题集没有错题 则显示空白页面 
								if (that.list.length == 0) {
									that.isEmpty = true;
								}

								//3.删除答题卡中的对应的项  答题卡list 由type和errorQuestions组成
								console.log("删除题目列表中对应的项", JSON.stringify(question));
								let type = question.stlx;
								console.log("type", type);
								let index = that.classifyQuestionList.findIndex((item) => item.type ==
									type);
								console.log("index", index);
								let errorQuestions = that.classifyQuestionList[index].errorQuestions;
								console.log("errorQuestions", JSON.stringify(errorQuestions));

								let errorIndex = errorQuestions.findIndex((item) => item.id == itemId);
								errorQuestions.splice(errorIndex, 1);
								console.log(JSON.stringify(errorQuestions));
								uni.showToast({
									title: '删除成功',
									icon: 'none'
								});

							}).catch(err => {
								console.log(JSON.stringify(err));
								uni.showToast({
									title: '删除出错 ' + err.errMsg,
									icon: 'none'
								});
							})


						}
					},
					fail: () => {},
					complete: () => {}
				});

			},
			//点击答题卡  
			onClickItem(serialNum) {
				this.currentIndex = this.list.findIndex((item) => item.serialNum == serialNum);

			},

			//格式化数据
			formatQuestionList(questionList) {
				for (let i = 0; i < questionList.length; i++) {
					let item = questionList[i];
					item.index = i;
					item.total = questionList.length;
				}
				//对数据进行分组
				this.classifyQuestions(questionList);

			},
			//给错题重新分组排序  
			classifyQuestions(questionList) {
				let classify = []; //按照题目类型进行分类后的数组
				let flag = 0;
				let mlist = questionList;
				let index = 0; //分类后数组的值需要有一个连续的序号

				let temp = {
					type: '',
					errorQuestions: []
				}
				for (let i = 0; i < mlist.length; i++) {
					let az = ''; //暂存角标
					for (let j = 0; j < classify.length; j++) {
						if (classify[j].type == mlist[i]['stlx']) { //如果类型相等
							flag = 1; //找到了类型相等的标志位设置为1
							az = j;
							break;
						}
					}
					if (flag == 1) { //找到了类型一致的 则将其加入该类型
						let ab = classify[az];

						ab.errorQuestions.push(mlist[i]);
						flag = 0;

					} else if (flag == 0) { //未找到一致的 则新建
						temp = {};
						temp.type = mlist[i]['stlx'];
						temp.errorQuestions = new Array();

						temp.errorQuestions.push(mlist[i]);
						classify.push(temp);
					}


				}

				let sortList = [];
				//为分好组的列表数据重新编号
				for (let i = 0; i < classify.length; i++) {

					for (let k = 0; k < classify[i].errorQuestions.length; k++) {
						index++;
						classify[i].errorQuestions[k].serialNum = index;

					}
					sortList.push.apply(sortList, classify[i].errorQuestions); //拼接追加list
				}


				this.list = sortList; //swiper题目列表
				this.classifyQuestionList = classify; //答题卡目录显示



			},
			tranType(type) {
				//1-单选 ，2-复选，3-判断（即只有两个选项 正确 错误的选择题  判断题答案  0：错  1：对），4-填空，5-问答
				switch (type) {
					case '1':
						return '单选题';
					case '2':
						return '复选题';
					case '3':
						return '判断题';
					case '4':
						return '填空题';
					case '5':
						return '问答题';
					default:
						break;
				}
			}

		}
	}
</script>

<style lang="scss">
	.page {
		position: relative;
	}

	.empty {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		z-index: 99;

	}

	.main {}

	.footer {
		background-color: #FFFFFF;
		width: 100%;
		position: absolute;
		bottom: 0;
		z-index: 99;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding-top: 16rpx;
		padding-bottom: 16rpx;
		box-shadow: 0 -1rpx 6rpx rgba(0, 0, 0, 0.1);
	}

	.cd-top {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		// margin-top: 16rpx;
		// margin-bottom: 16rpx;
	}


	.line {
		width: 100%;
		height: 1rpx;
		background-color: #E5E5E5;
	}

	.row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
	}

	.text-bold {
		font-size: $uni-font-size-lg;
		font-weight: bold;
		color: $uni-text-color;
		margin-left: 6rpx;
	}

	.text-grey {
		font-size: $uni-font-size-lg;
		color: $uni-text-color-grey;
	}

	.top-pop {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding-top: 16rpx;
	}



	.done {
		width: 30rpx;
		height: 14rpx;
		background-color: #007DFF;
		border-radius: 4rpx;
		margin-right: 3rpx;
	}

	.unDone {
		width: 30rpx;
		height: 14rpx;
		margin-right: 3rpx;
		margin-left: 30rpx;
		background-color: rgba(0, 125, 255, 0.2);
		border-radius: 4rpx;
	}



	.book {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		flex-wrap: wrap;
	}

	.flex-row-center {
		display: flex;
		flex-direction: row;
		align-items: center;
	}

	.small-image {
		width: 32rpx;
		height: 32rpx;
	}

	.bg-type {
		padding: 20rpx 30rpx;
		background-color: #F7F7F7;
		font-size: $uni-font-size-base;
		font-weight: bold;
		color: $uni-text-color;
	}
</style>
