<script>
	// #ifdef APP-PLUS
	import APPUpdate from '@/uni_modules/zhouWei-APPUpdate/js_sdk/appUpdate';
	// #endif
	export default {
	
		globalData:{
			
		},
		onLaunch: function() {
			console.log('App Launch')
			// #ifdef APP-PLUS
			APPUpdate();
			// #endif
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	@import "@/uni_modules/uview-ui/index.scss";
	
	@import url("@/base.css");
	
	/*  #ifdef  H5  */
	// body {
	//   max-width: 828rpx;  //最大宽度自己可以调整
	//   margin: auto !important;
	// }
	/*  #endif  */
</style>
