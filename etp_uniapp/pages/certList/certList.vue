<!-- 我的证书列表 -->
<template>
	<view>
		<kt-empty tips="暂未获得证书" v-if="certList.length<1"></kt-empty>
		<view v-else v-for="(item,index) in certList" :key="index" @click="catCertInfo(item)" hover-class="u-hover-class">

			<view class="bg-absoult">
				<view style="margin: 15px 35px;">
					<view :class="[item.status==99?'text-white-large':'text-black-large']">{{item.certName}}</view>
					<view :class="[item.status==99?'text-white-base':'text-black-base']"
						style="margin-top: 10rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
						{{item.content}}
					</view>
					<view class="bg-flex-row">
						<view class="bg-btn-white" hover-class="u-hover-class" v-if="item.status==99">查看证书</view>
						<view class="bg-flex-col">
							<view :class="[item.status==99?'text-white-base':'text-black-base']">
								{{item.status==99?'已颁发':'制证中'}}
							</view>
							<view :class="[item.status==99?'text-white-base':'text-black-base']">
								{{item.status==99? $u.timeFormat(item.updateTime,'yyyy年mm月dd日') :$u.timeFormat( item.createTime,'yyyy年mm月dd日')}}
							</view>
						</view>
					</view>
				</view>
			</view>
			<view style="margin: 15px;">
				<image :src="item.status==99?bgBlueCert:bgWhiteCert" class="bg-img"></image>
			</view>
		</view>




	</view>
</template>

<script>
	export default {
		data() {
			return {
				certList: [],
				bgBlueCert: this.$constant.BLUE_CERT,
				bgWhiteCert:this.$constant.WHITE_CERT

			}
		},
		onLoad() {
			this.getCertList()
		},
		methods: {
			getCertList() {
				uni.$u.api.getCertList().then(res => {
					uni.stopPullDownRefresh()

					this.certList = res.rows;



				}).catch(err => {
					console.log(err.errMsg);
					uni.stopPullDownRefresh()
				});

			},

			catCertInfo(e) {
				if (e && e.status == 99) {
					let info = JSON.stringify(e)
					uni.navigateTo({
						url: '/pages/certList/certInfo/certInfo?' + 'certName=' + e.certName + '&previewFile=' + e
							.previewFile + '&createTime=' + e.createTime + '&certDes=' + e.content+'&certId='+e.certId,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {
					uni.showToast({
						title: '制证中，请稍候查看',
						icon: 'none'
					});
				}


			},
			//按日期分组数据
			dataResort(data) {
				// 定义空数组，用于存储新组装的数据
				let newArr = [];
				// 遍历数组
				data.forEach((item, i) => {
					// 默认当前操作的数据下标 -1 
					let index = -1;
					// 判断数组中是否已经存在当前遍历数据的时间
					let isExists = newArr.some((newItem, j) => {
						if (item.date == newItem.date) {
							// 存在就保存当前数据下标  用于插入数据
							index = j;
							return true;
						}
					})
					// 如果没有就存储一条新对象数据
					if (!isExists) {
						newArr.push({
							date: item.date,
							subList: [item]
						})
					} else {
						// 如果有就插入到已存在的对象中
						newArr[index].subList.push(item);
					}
				})

				// 返回新数组
				return newArr
			}




		},
		onPullDownRefresh() {
			//下拉刷新
			this.getCertList()
		}
	}
</script>

<style>
	.bg-absoult {
		position: absolute;
		width: 100%;
		height: 130px;
	}

	.bg-flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
		margin-top: 15px;
	}

	.bg-flex-col {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.bg-img {
		position: relative;
		z-index: -1;
		width: 100%;
		height: 130px;
	}

	.bg-btn-white {
		background-color: white;
		border-radius: 15px;
		color: #1E8CFF;
		font-size: 12px;
		padding: 5px 10px;
	}

	.text-white-base {
		color: #ffffff;
		font-size: 14px;
	}

	.text-white-large {
		color: #ffffff;
		font-size: 16px;
		font-weight: bold;
	}

	.text-black-large {
		color: #303133;
		font-size: 16px;
		font-weight: bold;
	}

	.text-black-base {
		color: #303133;
		font-size: 14px;
	}
</style>
