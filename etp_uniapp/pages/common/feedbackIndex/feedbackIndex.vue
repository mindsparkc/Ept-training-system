<!-- 帮助反馈页面 -->
<template>
	<view>
		<view class="text-top">常见问题</view>
		<view class="bg-list">
			<view v-for="(item,index) in problemBox" :key="index" :title="item.problemTitle" class="list-title">
				<view class="text-title">{{item.problemTitle}}</view>
				<view v-for="(problemItem,problemIndex) in item.problemList" :key="problemIndex" class="list-question"
					hover-class="hover" @click="onClick(problemItem)">
					<view class="text-item">{{problemItem.title}}</view>
					<view class="line" v-if="problemIndex!=item.problemList.length-1"></view>
				</view>

			</view>
		</view>
		<view class="bg-box">
			<view class="bg-white-box">
				<image src="../../../static/common/icon-letter.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedbackList">我的反馈</view>

				<view class="vertical-line"></view>

				<image src="../../../static/common/icon-edit.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedback">我要反馈</view>
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				problemBox: [{
						problemTitle: '课程相关',
						problemList: [
							{
								title: '如何领取课程',
								content: '请点击[发现]选择您未领取的系统公共课程，进入后点击[免费领取]按钮即可领取。'
							},
							{
								title: '如何下载文档类型的课程',
								content: '在学习文档类型的课程时，点击PPT页面会出现操作按钮，点击右下方的下载按钮即可下载。下载成功后可使用本机自带的阅读器打开。'
							},
							{
								title: '文档类型的课程如何学习',
								content: '对于文档类型的课程，对于横屏的文档可以使用左右手势切换上下页，竖屏的文档可以使用上下滑动的手势进行查看学习。同时可使用页面上左右两个图标进行上下页的切换，另外页面提供了滑块按钮可以快速切换到想学习的页面。'
							}

						]
					},
					{
							problemTitle: '考试相关',
							problemList: [{
									title: '开始考试之后是否可以离开',
									content: '不可以。请严肃对待每场考试，当点击了[开始考试]按钮即视为考试正式开始，中途不得返回。'
								},
								{
									title: '考试如何查看成绩',
									content: '请点击[考试]-[历史考试]，对于已经出了成绩的考试，点击列表中的[查看成绩]即可查看本次考试的成绩。'
								}
					
							]
						},
					
					{
						problemTitle: '活动报名',
						problemList: [{
								title: '已经报名的活动可以取消吗',
								content: '在报名截止日期之前均可以取消。点击活动报名详情下方的[取消报名]按钮即可。'
							}

						]
					},
					{
						problemTitle: '调查问卷',
						problemList: [{
								title: '调查问卷是否可以多次填写',
								content: '不可以。完成的调查问卷会处于不可点击状态，所以请您认真对待每一次问卷的填写工作。感谢您的支持。'
							}

						]
					}, {
						problemTitle: '其他',
						problemList: [{
								title: '如何修改用户头像',
								content: '请点击[我的] - [账号管理] - [头像]即可更换用户头像',
							},
							{
								title: '如何修改登录密码',
								content: '请点击[我的] - [账号管理] - [修改密码]即可修改登录密码',
							},
							{
								title: '如何退出登录',
								content: '请点击[我的] - [账号管理] - [退出登录]即可退出登录',
							},
							{
								title: '系统反应迟钝',
								content: '如果出现系统反应迟钝、响应时间过长的问题，可查看本机的网络状态，或重启程序后再次尝试。'
							}
						]
					}
				]
			}
		},
		methods: {
			onClick(item) {

				uni.navigateTo({
					url: '/pages/common/helpDetail/helpDetail?title=' + item.title + '&content=' + item.content,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

			},
			toFeedbackList() {
				uni.navigateTo({
					url: '/pages/common/feedbackList/feedbackList',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			toFeedback() {
				uni.navigateTo({
					url: '/pages/common/feedback/feedback',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style>
	page {
		background-color: #F8F8F8;
		height: 100%;
	}



	.bg-box {
		background-color: #F8F8F8;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.bg-list {
		margin-bottom: 100rpx;
		background-color: #FFFFFF;
		padding: 30rpx
	}

	.bg-white-box {
		background-color: #FFFFFF;
		margin: 30rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 10rpx;
		border-radius: 20rpx;
		color: #303133;
		font-size: 32rpx;
	}

	.vertical-line {
		height: 20rpx;
		background-color: #cecece;
		width: 2rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.line {
		width: 100%;
		height: 1rpx;
		background-color: #F5F5F5;
	}



	.text-title {
		color: #303133;
		font-size: 32rpx;
		font-weight: bold;
	}

	.text-item {
		color: #999999;
		font-size: 28rpx;
		padding: 24rpx;
	}

	.list-title {
		margin-bottom: 30rpx;
	}

	.list-question {
		color: #606266;
		font-size: 28rpx;
	}

	.hover {
		background-color: #eee;
		opacity: 0.6;
	}

	.image {
		width: 40rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}

	.text-feedback {
		padding: 20rpx;
	}

	.text-top {
		margin: 30rpx;
		color: #999999;
		font-size: 28rpx;
	}
</style>
