<template>
	<view class="flex-col">
		<view>
			
		
		</view>
		<text class="text-title">{{article.mc}}</text>
		<text class="text-subtitle">{{article.createTime}}</text>
		<!-- 内容 -->
		<mp-html :content="article.nr"></mp-html>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				articleId: 0,
				article: {}
			}
		},
		onLoad(option) {
			this.articleId = Number(option.articleId);
			this.getArticleInfo()
		},
		methods: {
			getArticleInfo() {
				uni.$u.api.getCourswareInfo(this.articleId).then(res => {
					this.article = res.data;
					if (this.article&&this.article.mc) {
						uni.setNavigationBarTitle({
							title:this.article.mc
						})
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
		background-color: #FFFFFF;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		margin: 30rpx;
	}



	.text-title {
		color: $uni-color-title;
		font-size: $uni-font-size-title;
		font-weight: bold;
	}

	.text-subtitle {
		margin-top: 24rpx;
		color: $uni-color-subtitle;
		font-size: $uni-font-size-subtitle;
		margin-bottom: 30rpx;

	}

	
</style>
