<template>
	<view :style="{backgroundImage:'url('+this.$constant.LOGIN+')'}" class="page">
		<view class="login-box">
			<image src="../../static/logo.png" mode='aspectFit' class="login-logo"></image>

			<!-- <text class="login-title login_magin">你好，</text> -->
			<text class="login-title">培训教育平台</text>

			<u-tabs :list="tabs" :current="current" @click="click" lineColor="#037EFF" lineHeight="3" :activeStyle="{
		            color: '#037EFF',
		            fontWeight: 'bold',
					fontSize:'20px'
					
		        }" :inactiveStyle="{
		            color: '#303133',
					fontSize:'16px',
					 fontWeight: 'bold',
		        }" itemStyle="height: 60px;"></u-tabs>

			<swiper :current="current" @change="swiperChange" style="height:255px">
				<swiper-item>
					<view class="login-input">
						<image class="login-icon" src="../../static/login/icon_user.png"></image>
						<input @input="onUserNameInput" :value="userName" adjust-position='true' placeholder="请输入用户名" />
						<image class="login-icon" src="../../static/login/icon_delete.png" v-if="userName"
							@click="userName=''">
						</image>
					</view>
					<view class="login-input">
						<image class="login-icon" src="../../static/login/icon_unlock.png">
						</image>
						<input adjust-position='true' :password="passwordSwitch" type="text" placeholder="请输入密码"
							@input="onUserPasswordInput" :value="userPassword">
						</input>
						<image class="login-icon" :src="passwordSrc" @click="onSwitch"></image>
					</view>

					<view class="login-input" style="margin-top: 0;" v-if="isLoginCaptchaOn">
						<image class="login-icon" src="../../static/login/icon_captcha.png"></image>
						<input @input="onLoginCaptchCodeInput" adjust-position='true' placeholder="请输入计算答案"
							type="number" :value="loginCode" />
						<image class="img-captcha" :src="loginImg" @click="getLoginCaptchImage()"></image>
					</view>

					<view
						style="margin: 40rpx 20rpx; display:flex;flex-direction: row;align-items: center;justify-content: space-between;">
						<view class="flex-row">
							<u-switch v-model="isRemeberPassword" @change="change" size="14" activeColor="#037EFF">
							</u-switch>
							<text class="text-grey">记住密码</text>
						</view>
						<view class="text-blue" hover-class="u-hover-class" @click="guestLogin()">游客登录</view>

					</view>

					<button class="login-btn" @click="onBtnClick()">登录</button>

				</swiper-item>
				<swiper-item>
					<!-- 注册 -->
					<view class="login-input">
						<image class="login-icon" src="../../static/login/icon_user.png"></image>
						<input @input="onPhoneInput" adjust-position='true' placeholder="请输入您的手机号" type="number"
							maxlength="11" :value="registerName" />
						<image class="login-icon" src="../../static/login/icon_delete.png" v-if="registerName"
							@click="registerName=''"></image>
					</view>
					<view class="login-input">
						<image class="login-icon" src="../../static/login/icon_lock.png">
						</image>
						<input adjust-position='true' placeholder="请设置密码(长度5-20字符)" @input="onRegisterPasswordInput"
							maxlength="20" :value="registerPassword">
						<image class="login-icon" src="../../static/login/icon_delete.png" v-if="registerPassword"
							@click="registerPassword=''"></image>
						</input>

					</view>

					<view class="login-input" v-if="isCaptchaOn" style="margin-top: 0;">
						<image class="login-icon" src="../../static/login/icon_captcha.png"></image>
						<input @input="onCaptchCodeInput" adjust-position='true' placeholder="请输入计算答案" type="number"
							:value="registerCode" />
						<image class="img-captcha" :src="registerImg" @click="getCaptchImage()"></image>
					</view>
					<button class="login-btn" style="margin-top:80rpx;" @click="register()">立即注册</button>
				</swiper-item>
			</swiper>
			<!-- <image src="../../static/clipping.png" mode='aspectFit' class="login-logo"></image> -->
			<!-- <text class="login-title" style="font-weight:normal">物畅网提供技术支持</text> -->
			<u-no-network @disconnected="disconnected" @connected="connected" @retry="retry"></u-no-network>
		</view>
	</view>

</template>

<script>
	import sm4 from "../../util/SM4Encode.js"

	import {
		encrypt
	} from "../../util/jsencrypt.js"
	// import {
	// 	login
	// } from "../../common/api.js"
	export default {
		data() {
			return {
				sessionId: '',
				verifyCode: '',
				userName: '',
				userPassword: '',
				passwordSwitch: true, //默认为密码模式
				passwordSrc: '../../static/login/icon_hide.png',

				show: false,
				isVerifyCodeVisible: false,
				isRemeberPassword: true, //是否选择了记住密码  默认选中
				isRemeber: true,

				routeUrl: '', //跳转过来的路由名称
				loginImg: '',
				loginCode: '',
				loginUUID: '',
				isLoginCaptchaOn: false,


				current: 0, //登录注册
				tabs: [{
						name: "登录"
					},
					{
						name: "注册"
					},
				],
				registerName: "",
				registerPassword: "",
				registerCode: "",
				registerImg: "",
				isCaptchaOn: false,
				registerUUID: "",

				isGuest: false, //是否游客登录



			}
		},
		onLoad() {
			this.getLoginCaptchImage()
			this.getCaptchImage()
			this.getStorage()

		},
		methods: {

			getStorage() {
				//从缓存中自动获取用户名密码
				uni.getStorage({
					key: 'login',
					success: result => {
						let userLogin = {};
						userLogin = result.data;
						this.userName = userLogin.userName;

						uni.getStorage({
							key: 'isRemeberPassword',
							success: result => {
								this.isRemeberPassword = result.data;
								this.isRemeber = result.data;
								if (this.isRemeberPassword) {
									this.userPassword = userLogin.password;
								}

							}
						})

					}
				})
			},

			click(e) {
				this.current = e.index;
			},

			routeTo() {
				let pages = getCurrentPages(); //获取加载的页面

				if (pages && pages.length >= 2) {
					this.routeUrl = pages[pages.length - 2].route;
					uni.navigateBack({
						delta: 1
					})
				} else {
					//获取保存的页面
					let shareRoute = uni.getStorageSync("shareRoute")
					if (shareRoute) {
						uni.redirectTo({
							url: "/" + shareRoute,
							success() {
								uni.removeStorage({
									key: "shareRoute"
								})
							}
						})
					} else {
						//跳转到首页
						uni.switchTab({
							url: '/pages/index/index',
						})
					}


				}
			},

			onUserNameInput: function(event) {
				this.userName = event.target.value
			},
			onUserPasswordInput: function(event) {
				this.userPassword = event.target.value
			},
			onSwitch() {
				this.passwordSwitch = !this.passwordSwitch;
				if (this.passwordSwitch) { //密码模式
					this.passwordSrc = "../../static/login/icon_hide.png";
				} else {
					this.passwordSrc = "../../static/login/icon_show.png";
				}
			},
			onBtnClick() {
				this.isGuest = false;
				this.$u.throttle(this.onClickLogin(), 2000);
			},
			onClickLogin() {
				let userName = ''
				let password = ''
				if (this.isGuest) { //若为游客登录  用户名 密码 均设置为guest
					userName = 'guest'
					password = 'guest'
				} else {
					//判断用户名密码是否为空
					userName = this.userName;
					password = this.userPassword;
				}


				// userName = 'test'
				// password = 'admin123'
				if (userName == null || userName == '' || userName == 'undefined') {
					uni.showToast({
						title: "请输入您的用户名",
						icon: "none"
					})
					return
				};
				if (password == null || password == '' || password == 'undefined') {
					uni.showToast({
						title: "请输入您的密码",
						icon: "none"
					})
					return
				};


				if (this.isLoginCaptchaOn && !this.loginCode) {
					uni.showToast({
						title: '请输入验证码',
						icon: 'none'
					});
					return
				}

				//获取当前时间。
				// var currentTime = new Date().getTime();
				// var formatTime = this.$u.timeFormat(currentTime, 'yyyy-mm-dd hh:MM:ss')
				// var token = userName + "&" + password + "&" + formatTime;
				// this.verifyCode = sm4.encrypt_cbc(token); //加密
				//调用登录接口

				let params = {
					"username": userName,
					"password": encrypt(password),
					code: this.isLoginCaptchaOn ? this.loginCode : "",
					uuid: this.isLoginCaptchaOn ? this.loginUUID : "",
					// "password": password
				}
				uni.showLoading({
					title: '正在登录...',
					mask: false
				});

				uni.$u.api.login(params, {
					custom: {
						catch: true
					}
				}).then(res => {
					uni.hideLoading();
					let token = res.token;
					if (token) {
						if (!this.isGuest) { //若为游客登录不保存用户名密码

							//保存用户名密码
							let userLogin = {};
							userLogin.userName = userName;
							userLogin.password = password;
							uni.setStorage({
								key: 'login',
								data: userLogin,
								success: res => {}
							});
							uni.setStorage({
								key: 'isRemeberPassword',
								data: this.isRemeber,
								success: res => {}
							})

						}
						uni.setStorage({
							key: 'token',
							data: 'Bearer ' + token,
							success: res => {
								this.getUserInfo();
							}
						})
					}

				}).catch(err => {
					let msg = err.msg;
					//刷新验证码
					this.loginImg = ""
					this.loginCode = ""
					this.loginUUID = ""
					this.getLoginCaptchImage()

				})


			},

			getUserInfo() {
				uni.$u.api.getUserInfo().then(res => {
					uni.hideLoading();
					let userInfo = res.data;
					// console.log(JSON.stringify(userInfo));
					if (userInfo) {
						uni.showToast({
							title: '登录成功',
							icon: 'none'
						});
						uni.setStorage({
							key: 'userInfo',
							data: userInfo

						})

					}

					this.routeTo();


				}).catch(err => {
					uni.hideLoading();
					console.log(err);
				})

			},
			change(e) {
				this.isRemeber = e;
			},

			onPhoneInput(e) {
				this.registerName = e.target.value
			},
			onRegisterPasswordInput(e) {
				this.registerPassword = e.target.value
			},
			onCaptchCodeInput(e) {
				this.registerCode = e.target.value
			},
			onLoginCaptchCodeInput(e) {
				this.loginCode = e.target.value
			},

			//获取登录使用验证码
			getLoginCaptchImage() {
				uni.$u.api.getCaptchImage().then(res => {

					this.isLoginCaptchaOn = res.captchaOnOff
					if (res.captchaOnOff) {
						this.loginImg = 'data:image/jpg;base64,' + res.img
						this.loginUUID = res.uuid
					}


				}).catch(err => {
					console.log(JSON.stringify(err));
				})
			},
			//获取注册使用验证码
			getCaptchImage() {
				uni.$u.api.getCaptchImage().then(res => {

					this.isCaptchaOn = res.captchaOnOff
					if (res.captchaOnOff) {
						this.registerImg = 'data:image/jpg;base64,' + res.img
						this.registerUUID = res.uuid
					}


				}).catch(err => {
					console.log(JSON.stringify(err));
				})
			},




			//用户注册
			register() {
				//username  password code  uuid

				if (!this.registerName) {
					uni.showToast({
						title: '请输入手机号',
						icon: 'none'
					});
					return
				} else {
					if (!uni.$u.test.mobile(this.registerName)) {
						uni.showToast({
							title: '请输入正确的手机号',
							icon: 'none'
						});
						return
					}
				}


				if (!this.registerPassword) {
					uni.showToast({
						title: '请设置密码',
						icon: 'none'
					});
					return
				} else {
					if (!uni.$u.test.rangeLength(this.registerPassword, [5, 20])) {
						uni.showToast({
							title: '密码长度5~20个字符',
							icon: 'none'
						});
						return
					}

				}
				if (this.isCaptchaOn && !this.registerCode) {
					uni.showToast({
						title: '请输入验证码',
						icon: 'none'
					});
					return
				}


				let data = {
					username: this.registerName,
					password: this.registerPassword,
					code: this.isCaptchaOn ? this.registerCode : "",
					uuid: this.isCaptchaOn ? this.registerUUID : "",
				}

				console.log("注册数据", JSON.stringify(data));
				uni.showLoading({
					title: '正在注册...',
					mask: false
				});
				uni.$u.api.register(data, {
					custom: {
						catch: true
					}
				}).then(res => {
					console.log(res);
					uni.hideLoading();
					//注册成功之后执行登录操作
					this.userName = this.registerName
					this.userPassword = this.registerPassword;
					// this.onClickLogin()

					uni.showToast({
						title: '注册成功，请登录',
						icon: 'none',
						duration: 2000
					});
					this.current = 0;



				}).catch(err => {
					let msg = err.msg;
					//刷新验证码
					this.registerImg = ""
					this.registerCode = ""
					this.registerUUID = ""
					this.getCaptchImage()

				})
			},
			swiperChange(e) {
				if (e.detail.source == 'touch') { //解决快速滑动 不停抖动问题
					this.current = e.target.current;

				}

			},

			disconnected() {
				uni.showToast({
					title: '网络连接失败',
					icon: 'none'
				});
			},
			connected() {
				this.getLoginCaptchImage()
				this.getCaptchImage()

			},
			retry() {},
			guestLogin() {
				this.isGuest = true;
				this.onClickLogin();
			}


		}
	}
</script>

<style lang="scss">
	.page {
		background-repeat: no-repeat;
		background-position: center;
		background-size: cover;
		height: 100vh;
		width: 100vw;
		display: flex;
		align-items: center;
		background-color: #e8f4ff;

	}

	.login-box {
		width: 100%;
		margin-left: 50rpx;
		margin-right: 50rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		padding: 40rpx 40rpx 20rpx 40rpx;
		background-color: #fff;
		border-radius: 40rpx;
		box-shadow: 0px 7px 10px 3px rgba(103, 116, 130, 0.1);
	}

	.login-logo {
		margin: 0 auto;
		width: 415rpx;
		height: 115rpx;

	}

	.login-icon {
		width: 30rpx;
		height: 30rpx;
		flex-shrink: 0;
	}

	.img-captcha {
		width: 120px;
		height: 40px;
	}




	.login-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #303133;
		text-align: center;
		margin-top: 40rpx;
		width: 100%;

	}

	.login_magin {
		margin-top: 50rpx;
	}



	.login-input {
		display: flex;
		border-bottom: 1rpx solid #afb1ba;
		margin-top: 30rpx;
		padding: 20rpx 20rpx;
		align-items: center;

	}



	input {
		width: 80%;
		height: auto;
		margin-left: 20rpx;
		margin-right: 20rpx;
	}


	.login-register {
		text-align: left;
		font-size: 28rpx;
		color: #333333;
	}

	.login-code {
		width: 100%;
		margin-top: 50rpx;
		text-align: center;
		font-size: 34rpx;
		color: #666666;
	}

	.login-btn {

		color: #fff;
		border-radius: 10px;
		font-size: 34rpx;
		box-shadow: 0px 2px 5px 0px rgba(22, 99, 255, 0.1);
		// background: $uni-color-primary;
		margin-top: 60rpx;
		background: -webkit-linear-gradient(to left, #007DFF, #5EC4F9);
		background: linear-gradient(to left, #007DFF, #5EC4F9);

	}

	.login-btn:after {
		border: 0;
	}

	/*按钮点击效果*/
	.login-btn.button-hover {
		// transform: translate(1rpx, 1rpx);
		background: -webkit-linear-gradient(to right, #007DFF, #5EC4F9);
		background: linear-gradient(to right, #007DFF, #5EC4F9);
	}

	.login-flex {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
	}

	.text-grey {
		font-size: 28rpx;
		color: #333333;
		margin-left: 10rpx;
	}

	.text-blue {
		font-size: 28rpx;
		color: #037EFF;
		text-decoration: underline;
	}
</style>
