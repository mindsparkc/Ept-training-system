<template>
	<view class="login-box">
		<u-form ref="form" :rules="rules" :borderBottom="false" :model="passwordInfo">

			<u-form-item prop="oldPassword" ref="item1">
				<view class="login-input">
					<image class="login-icon" style="margin-right: 12px;" src="../../static/login/icon_unlock.png">
					</image>
					<u--input :password="oldPasswordSwitch" type="text" placeholder="请输入原密码"
						v-model="passwordInfo.oldPassword" border="none">
					</u--input>
					<image class="login-icon" :src="oldPasswordSrc" @click="onOldSwitch"></image>
				</view>
			</u-form-item>
			<u-form-item prop="newPassword" ref="item1">
				<view class="login-input">
					<image class="login-icon" style="margin-right: 12px;" src="../../static/login/icon_lock.png">
					</image>

					<u--input :password="passwordSwitch" type="text" placeholder="请输入新密码" maxlength="20"
						v-model="passwordInfo.newPassword" border="none" />

					<image class="login-icon" :src="passwordSrc" @click="onSwitch"></image>
					</u--input>

				</view>

			</u-form-item>

			<u-form-item prop="confirmPassword" ref="item2">
				<view class="login-input">
					<image class="login-icon" style="margin-right: 12px;" src="../../static/login/icon_captcha.png">
					</image>

					<u--input :password="confirmPasswordSwitch" type="text" placeholder="请再次输入新密码" maxlength="20"
						v-model="passwordInfo.confirmPassword" border="none" />
					<image class="login-icon" :src="confirmPasswordSwitchSrc" @click="onConfirmSwitch"></image>

					</u--input>

				</view>
			</u-form-item>


		</u-form>

		<!-- <view class="login-input">
			<image class="login-icon" src="../../static/login/icon_unlock.png">
			</image>
			<input adjust-position='true' :password="oldPasswordSwitch" type="text" placeholder="请输入原密码"
				@input="onUserOldPasswordInput" :value="userOldPassword">
			</input>
			<image class="login-icon" :src="oldPasswordSrc" @click="onOldSwitch"></image>
		</view>

		<view class="login-input">
			<image class="login-icon" src="../../static/login/icon_lock.png">
			</image>

			<input adjust-position='true' :password="passwordSwitch" type="text" placeholder="请设置新密码(长度5-20字符)"
				maxlength="20" @input="onUserNewPasswordInput" />

			<image class="login-icon" :src="passwordSrc" @click="onSwitch"></image>
			</input>

		</view>
		<view class="login-input">
			<image class="login-icon" src="../../static/login/icon_captcha.png">
			</image>

			<input adjust-position='true' :password="confirmPasswordSwitch" type="text" placeholder="请确认新密码"
				maxlength="20" @input="onUserConfirmPasswordInput" />
			<image class="login-icon" :src="confirmPasswordSwitchSrc" @click="onConfirmSwitch"></image>

			</input>

		</view>
 -->

		<button class="login-btn" @click="onClickChange()">提交</button>




	</view>
</template>

<script>
	import sm4 from "../../util/SM4Encode.js"
	import config from '../../common/config.js'
	export default {
		data() {
			return {
				// userPhone: '',
				userOldPassword: '',
				userNewPassword: '',
				userConfirmPassword: '', //确认密码
				oldPasswordSwitch: true, //旧密码
				oldPasswordSrc: '../../static/login/icon_hide.png',
				passwordSwitch: true, //默认为密码模式  新密码
				passwordSrc: '../../static/login/icon_hide.png',
				confirmPasswordSwitch: true, //确认密码后面的小眼睛
				confirmPasswordSwitchSrc: '../../static/login/icon_hide.png',

				passwordInfo: {
					oldPassword: "",
					newPassword: "",
					confirmPassword: ""
				},

				rules: {

					'oldPassword': [{
							required: true,
							message: '请输入原密码',
							trigger: ['blur', 'change'],
						},


					],

					'newPassword': [{
							required: true,
							message: '请输入新密码',
							trigger: ['blur', 'change'],
						},
						// 6-20个字符之间的判断
						{
							min: 6,
							max: 20,
							message: '密码长度需要在6-20个字符之间',
							trigger: ['blur', 'change'],
						}

					],
					'confirmPassword': [{
							required: true,
							message: '请再次输入新密码',
							trigger: ['blur', 'change'],
						},
						{
							validator: (rule, value, callback) => {
								return value == this.passwordInfo.newPassword;
							},
							message: '再次输入密码与设置的新密码不相同',
							// 触发器可以同时用blur和change
							trigger: ['change', 'blur'],
						}
					],
				},
			}
		},
		methods: {
			onPhoneInput: function(event) {
				this.userPhone = event.target.value
			},
			onUserOldPasswordInput: function(event) {
				this.userOldPassword = event.target.value
			},
			onUserNewPasswordInput: function(event) {
				this.userNewPassword = event
				this.passwordInfo.newPassword = event
			},
			onUserConfirmPasswordInput: function(event) {
				this.userConfirmPassword = event.target.value
			},
			onOldSwitch() {
				this.oldPasswordSwitch = !this.oldPasswordSwitch;
				if (this.oldPasswordSwitch) { //密码模式
					this.oldPasswordSrc = "../../static/login/icon_hide.png";
				} else {
					this.oldPasswordSrc = "../../static/login/icon_show.png";
				}
			},
			onSwitch() {
				this.passwordSwitch = !this.passwordSwitch;
				if (this.passwordSwitch) { //密码模式
					this.passwordSrc = "../../static/login/icon_hide.png";
				} else {
					this.passwordSrc = "../../static/login/icon_show.png";
				}
			},
			onConfirmSwitch() {
				this.confirmPasswordSwitch = !this.confirmPasswordSwitch;
				if (this.confirmPasswordSwitch) { //密码模式
					this.confirmPasswordSwitchSrc = "../../static/login/icon_hide.png";
				} else {
					this.confirmPasswordSwitchSrc = "../../static/login/icon_show.png";
				}
			},

			onClickChange() {
				
				this.$refs.form.validate().then(res=>{
					//校验通过
					
					let change = "?oldPassword=" + this.passwordInfo.oldPassword + "&newPassword=" + this.passwordInfo.newPassword;
					let token = uni.getStorageSync('token')
					
					let changeUrl = config.baseUrl + '/system/user/profile/updatePwd' + change;
					
					uni.request({
						url: changeUrl,
						header: {
							'Authorization': token
						},
						method: 'PUT',
						success: (res) => {
					
							if (res.data.code == 200) {
								uni.showModal({
									title: '温馨提示',
									content: '密码修改成功，请重新登录',
									showCancel: false,
									success: (res) => {
										if (res.confirm) {
											//跳转到登录页
											uni.reLaunch({
												url: '/pages/login/login'
											});
										}
									}
								});
							} else {
								if (res.data.code == 401) { //token 失效 直接返回登录页面
									uni.showModal({
										title: '温馨提示',
										content: '您的登录状态已经失效，需要重新登录',
										showCancel: false,
										success: (res) => {
											if (res.confirm) {
												//跳转到登录页
												uni.reLaunch({
													url: '/pages/login/login'
												});
											}
										}
									});
								}
								if (res.data.code == 500) { //修改密码失败
									uni.showModal({
										title: '温馨提示',
										content: res.data.msg,
										showCancel: false,
										success: (res) => {
					
										}
									});
								}
							}
					
					
						},
						fail: (err) => {
							console.error(err)
							if (err.code == 401) { //token 失效 直接返回登录页面
								uni.showModal({
									title: '温馨提示',
									content: '您的登录状态已经失效，需要重新登录',
									showCancel: false,
									success: (res) => {
										if (res.confirm) {
											//跳转到登录页
											uni.reLaunch({
												url: '/pages/login/login'
											});
										}
									}
								});
							}
						}
					});
					
					
				}).catch(errors=>{
					uni.$u.toast('请填写完整后再提交')
				})
			


			

			},

			showAction() {
				this.show = true;
			},

		}
	}
</script>

<style>
	.login-box {
		display: flex;
		flex-direction: column;
		justify-content: center;
		padding: 0 80rpx;
	}

	.login-input {
		display: flex;
		border-bottom: 1rpx solid #afb1ba;
		/* margin-top: 30rpx; */
		padding: 20rpx 20rpx;
		align-items: center;
		width: 100%;

	}



	.login-icon {
		width: 20px;
		height: 20px;
		flex-shrink: 0;
	}


	input {
		width: 80%;
		height: auto;
		margin-left: 20rpx;
		margin-right: 20rpx;
	}

	.input-placeholder {
		color: #333333;
	}



	.login-btn {
		width: 100%;
		color: #fff;
		border-radius: 5px;
		font-size: 34rpx;
		box-shadow: 0px 2px 5px 0px rgba(22, 99, 255, 0.1);
		margin-top: 60rpx;
		background: -webkit-linear-gradient(to left, #007DFF, #145CDC);
		background: linear-gradient(to left, #007DFF, #145CDC);

	}

	.login-btn:after {
		border: 0;
	}

	/*按钮点击效果*/
	.login-btn.button-hover {
		background: -webkit-linear-gradient(to right, #007DFF, #145CDC);
		background: linear-gradient(to right, #007DFF, #145CDC);
	}
</style>
