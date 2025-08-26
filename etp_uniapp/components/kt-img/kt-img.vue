<!-- 图片查看组件 -->
<template>
	<view>
		<!-- :style="'width:'+width+'px;'" -->
		<image v-if="!isError" :src="cacheUrl" mode="widthFix" @error="onErrorHandler" @load="onLoadHandler"
			:lazy-load="true" style="width:100vw;min-height: 1px;"></image>

		<!-- 	<view
			:style="'width:100vw;height:225px;background:#f8f8f8;display:flex;justify-content:center;align-item:center'"
			v-if="loading">
			<u-loading-icon color="#037EFF"></u-loading-icon>
		</view> -->
		<view v-if="loading"
			:style="'width:100vw;height:225px;background:#f8f8f8;color:#999999;text-align:center;line-height:225px'">
			<text>正在加载...</text>
		</view>
		<view v-if="isError && !loading"
			:style="'width:100vw;height:225px;background:#f8f8f8;color:#999999;text-align:center;line-height:225px'">
			<text>加载失败</text>
		</view>


	</view>
</template>

<script>
	/**
	 * 图片查看组件 
	 * @description 图片查看组件，显示图片正常加载状态、图片loading状态，失败状态。支持图片缓存功能，若本地有缓存则读取缓存，若无缓存则重新加载
	 * @property {String} src 图片段地址
	 * @property {String} hostPath 图片路径host
	 * @example <kt-empty tips="暂无数据" ></kt-empty>
	*/
	export default {
		name: "kt-img",
		props: {
			// 图片短地址
			src: {
				type: String,
				default: ''
			},

			hostPath: {
				type: String,
				default: ''
			}


			// // 宽度，单位任意
			// width: {
			// 	type: [String, Number],
			// 	default: ''
			// },
		},
		data() {
			return {
				// 图片是否加载错误，如果是，则显示错误占位图
				isError: false,
				// 初始化组件时，默认为加载中状态
				loading: true,

				cacheUrl: '', //图片地址

			};
		},
		watch: {
			src: {
				immediate: true,
				handler(n) {
					if (n) {
						this.isError = false;
						this.loading = true;
						this.getLocalImageCache()
					}
				}
			}
		},


		methods: {

			// 查找获取图片缓存
			getLocalImageCache() {

				// #ifdef APP-PLUS
				this.getImageCache(this.src).then(
					res => {
						this.cacheUrl = res
					}, rej => {
						this.cacheUrl = rej
					}

				);
				// #endif
				// #ifndef APP-PLUS
				this.cacheUrl = this.completePath(this.src)
				// #endif
			},

			// 图片加载失败
			onErrorHandler(err) {

				// #ifdef APP-PLUS
				let that = this;

				//判断是否加载的本地图片 若加载的本地图片则换成网络图片重试  若加载的网络图片显示加载失败
				if (that.cacheUrl.indexOf("http") == -1) { //本地图片缺失 删除本地缓存的值 重新进行缓存
					let storageKey = 'IMAGE_CACHE_INFO_' + that.src;
					uni.removeStorage({
						key: storageKey,
						success: function(res) {
							that.cacheUrl = "";
							that.getLocalImageCache()
						}
					})


				} else {
					that.loading = false
					that.isError = true
				}
				// #endif

				// #ifndef APP-PLUS
				that.loading = false
				that.isError = true
				console.log(JSON.stringify(err));
				// #endif

			},
			// 图片加载完成，标记loading结束
			onLoadHandler(event) {
				this.loading = false
				this.isError = false
			},



			getImageCache(svgPath) {
				// 图片缓存key值
				let storageKey = 'IMAGE_CACHE_INFO_' + svgPath;
				let filePath = this.completePath(svgPath);
				return new Promise((resolve, reject) => {
					// 首先获取本地存储的数据，查询是否有对应文件路径，如果有缓存内容，直接返回
					let cacheFileInfo = uni.getStorageSync(storageKey)
					if (cacheFileInfo) {
						resolve(cacheFileInfo)
					} else {
						// 如果没有，执行下载，并存储起来后
						uni.downloadFile({
							url: filePath,
							success: (res) => {
								if (res.statusCode === 200) {
									// 再进行本地保存
									uni.saveFile({
										tempFilePath: res.tempFilePath,
										success: function(res2) {
											uni.setStorageSync(storageKey, res2
												.savedFilePath)
											resolve(res2.savedFilePath)
										},
										fail: function(res2) {
											reject(filePath)
										}
									})
								} else {
									console.log('下载临时文件失败', filePath)
									reject(filePath)
								}
							},
							fail: (res) => {
								console.log(res)
								reject(filePath)
							}
						})
					}

				});






			},



			completePath(svgPath) {
				let fullPath = svgPath;

				if (svgPath && svgPath.indexOf("http") == -1) { //需要拼接
					fullPath = this.hostPath + "/" + svgPath;
				}
				return fullPath;

			},



		}
	}
</script>

<style>
	image {
		will-change: transform
	}
</style>
