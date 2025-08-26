<template>
	<view class="root">
		<textarea maxlength="-1" :value="value" placeholder="请在此处填写您的回答" class="area" :auto-height="true"
			placeholder-style="color:#dadbde" @input="input"></textarea>
		<!-- #ifdef APP -->
		<image src="../kt-voice-textarea/static/mic.svg" class="img-mic" @click="speech()"></image>
		<!-- #endif -->

	</view>
</template>

<script>
	export default {
		name: "kt-voice-textarea",

		props: {
			value: {
				type: String,
				default: ""
			}
		},
		data() {
			return {
				// value: ''
				values: this.value
			};
		},
		methods: {
			input(e) {
				this.$emit("input", e.detail.value)
			},

			speech() {
				let that = this;
				let options = {
					engine: 'baidu',
					punctuation: false
				};

				plus.speech.startRecognize(options, function(s) {
					that.values = that.values ? that.values : '';
					that.values += s + " ";
					that.$emit("input", that.values)
				}, function(e) {
					uni.showToast({
						title: '语音识别失败',
						icon: 'none'
					});
					console.log('语音识别失败：' + JSON.stringify(e));

				});





			}
		}

	}
</script>

<style>
	.root {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		/* border: 0.5px solid #dadbde; */
		padding: 10px;
		background-color: #fbf7fe;
		border: solid 1px #f3eff6;
		border-radius: 10rpx;

	}

	.area {
		flex: 1;
		margin-right: 10px;
		min-height: 80px;
	}

	.img-mic {
		width: 30px;
		height: 30px;
		flex-shrink: 0;


	}
</style>
