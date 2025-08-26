<template>
	<view class="chat">
		<scroll-view  :style="{height: `${windowHeight - inputHeight - (msgList.length?60:0)}rpx`}"
		id="scrollview"
		scroll-y="true" 
		:scroll-top="scrollTop"
		class="scroll-view"
		>
			<!-- 聊天主体 -->
			<view id="msglistview" class="chat-body">
				<!-- 聊天记录 -->
				<view v-for="(item,index) in msgList" :key="index">
					<!-- 自己发的消息 -->
					<view class="item self" v-if="item.key === 'user'" >
						<!-- 文字内容 -->
						<view class="content right">
						{{item.value}}
						</view>
						<!-- 头像 -->
						<image class="avatar" :src="meIcon">
						</image>
					</view>
					<!-- 机器人发的消息 -->
					<view class="item Ai" v-if="item.key === 'assistant'">
						<!-- 头像 -->     
						<image class="avatar" :src="dsIcon">
						</image>
						<!-- 文字内容 -->
						<view class="content left">
							{{item.value}}
						</view>
					</view>
				</view>
				<u-loading-icon text="思考中..." color="#dddddd" v-if="isLoading"></u-loading-icon>
			</view>
			
		</scroll-view>
		<view class="restart" v-if="msgList.length" @click="resetChat">
			<view class="restartBtn">开始新对话</view>
		</view>
		
		<!-- 底部消息发送栏 -->
		<!-- 用来占位，防止聊天消息被发送框遮挡 -->
		<view class="chat-bottom" :style="{height: `${inputHeight}rpx`}">
			<view class="send-msg" :style="{bottom:`${keyboardHeight}rpx`}">
				<view class="uni-textarea">
					<textarea v-model="chatMsg"
						maxlength="500"
						confirm-type="send"
						@confirm="handleSend"
						placeholder="给 Deepseek 发消息~"
						:show-confirm-bar="false"
						:adjust-position="false"
						@linechange="sendHeight"
						@focus="focus" 
						@blur="blur"
					   auto-height
					>
					</textarea>
				</view>
				<button @click="handleSend" class="send-btn">发送</button>
			</view>
		</view>
	</view>
</template>
<script>
	export default{
		data() {
			return {
				dsIcon : "/static/common/dsIcon.png",//deepseek头像
				meIcon : "/static/common/user.png",//me头像
				//键盘高度
				keyboardHeight:0,
				//底部消息发送高度
				bottomHeight: 0,
				//滚动距离
				scrollTop: 0,
				userId:'',
				//发送的消息
				chatMsg:"",
				isLoading:false,
				msgList:[]	
			}
		},
		updated(){
			//页面更新时调用聊天消息定位到最底部
			this.scrollToBottom();
		},
		computed: {
			windowHeight() {
			    return this.rpxTopx(uni.getSystemInfoSync().windowHeight)
			},
			// 键盘弹起来的高度+发送框高度
			inputHeight(){				
				return this.bottomHeight + this.keyboardHeight
			}
		},
		onLoad(){
			uni.onKeyboardHeightChange(res => {
				//这里正常来讲代码直接写
				//this.keyboardHeight=this.rpxTopx(res.height)就行了
				//但是之前界面ui设计聊天框的高度有点高,为了不让键盘和聊天输入框之间距离差太大所以我改动了一下。
				this.keyboardHeight = this.rpxTopx(res.height)
				if(this.keyboardHeight<0)this.keyboardHeight = 0;
			})
		},
		onUnload(){
			if (this.msgList.length){
				this.resetChat()
			}
			// uni.offKeyboardHeightChange()
		},
		methods: {			
			focus(){
				this.scrollToBottom()
			},
			blur(){
				this.scrollToBottom()
			},
			// px转换成rpx
			rpxTopx(px){
				let deviceWidth = uni.getSystemInfoSync().windowWidth
				let rpx = ( 750 / deviceWidth ) * Number(px)
				return Math.floor(rpx)
			},
			// 监视聊天发送栏高度
			sendHeight(){
				setTimeout(()=>{
					let query = uni.createSelectorQuery();
					query.select('.send-msg').boundingClientRect()
					query.exec(res =>{
						this.bottomHeight = this.rpxTopx(res[0].height)
					})
				},10)
			},
			// 滚动至聊天底部
			scrollToBottom(e){
				setTimeout(()=>{
					let query = uni.createSelectorQuery().in(this);
					query.select('#scrollview').boundingClientRect();
					query.select('#msglistview').boundingClientRect();
					query.exec((res) =>{
						if(res[1].height > res[0].height){
							this.scrollTop = this.rpxTopx(res[1].height - res[0].height)
						}
					})
				},15)
			},
			//重新对话
			resetChat(){
				if (this.isLoading) this.isLoading = false;
				uni.$u.api.deleteRedisCache().then(res => {
					this.msgList=[]
				}).catch(err => {
					console.log(err.errMsg);
				})
			},
			// 发送消息
			handleSend() {
				//如果消息不为空
				if(!this.chatMsg||!/^\s+$/.test(this.chatMsg)){
					let obj = {
						key: "user",
						value: this.chatMsg
					}
					this.msgList.push(obj);
					this.isLoading = true
					uni.$u.api.streamChat({message:this.chatMsg}).then(res => {
						if(res.code==200){						
							let data = JSON.parse(res.msg)
							this.isLoading = false
							this.msgList.push({key:"assistant",value:data.choices[0].message.content})
							
							this.scrollToBottom()
						}else{
							this.isLoading = false
							this.$modal.msgError(res.msg)				
						}
					}).catch(err => {
						this.isLoading = false
						console.log(err.errMsg);
					})
					this.chatMsg = '';
					
				}else {
					this.$modal.showToast('不能发送空白消息')
				}
			},
		}
	}
</script>
<style lang="scss" scoped>
	
	$chatContentbgc: #C2DCFF;
	$avatarbgc: #dddddd;
	
	view,button,text,input,textarea {
		margin: 0;
		padding: 0;
		box-sizing: border-box;
	}
 
	/* 聊天消息 */
	.chat {
		height: 100vh;     
		.scroll-view {
			::-webkit-scrollbar {
					    display: none;
					    width: 0 !important;
					    height: 0 !important;
					    -webkit-appearance: none;
					    background: transparent;
					    color: transparent;
					  }
			
			// background-color: orange;
			background-color: #F6F6F6;
			
			.chat-body {
				display: flex;
				flex-direction: column;
				padding-top: 23rpx;
				// background-color:skyblue;
				
				.self {
					justify-content: flex-end;
				}
				.item {
					display: flex;
					padding: 23rpx 30rpx;
					// background-color: greenyellow;
 
					.right {
						background-color: $chatContentbgc;
					}
					.left {
						background-color: #FFFFFF;
					}
                    // 聊天消息的三角形
					.right::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						left: 100%;
						top: 10px;
						border: 12rpx solid transparent;
						border-left: 12rpx solid $chatContentbgc;
					}
 
					.left::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						top: 10px;
						right: 100%;
						border: 12rpx solid transparent;
						border-right: 12rpx solid #FFFFFF;
					}
 
					.content {
						position: relative;
						max-width: 486rpx;
						border-radius: 8rpx;
						word-wrap: break-word;
						padding: 24rpx 24rpx;
						margin: 0 24rpx;
						border-radius: 5px;
						font-size: 32rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #333333;
						line-height: 42rpx;
					}
 
					.avatar {
						display: flex;
						justify-content: center;
						width: 78rpx;
						height: 78rpx;
						background: $avatarbgc;
						border-radius: 50%;
						overflow: hidden;
						
						image {
							align-self: center;
						}
 
					}
				}
			}
		}
		.restart{
			width: 100%;
			height: 60rpx;
			text-align: center;
			display: flex;
			justify-content: center;
			background-color: #F6F6F6;
			.restartBtn{
				width: 160rpx;
				height: 46rpx;
				line-height: 46rpx;
				border-radius: 16rpx;
				font-size: 24rpx;
				color: #4d6bfe;
				background-color: rgb(219 ,234 ,254);
			}
		}
		/* 底部聊天发送栏 */
		.chat-bottom {
			width: 100%;
			height: 100rpx;
			background: #F4F5F7;
			transition: all 0.1s ease;
			
			.send-msg {
				display: flex;
				align-items: flex-end;
				padding: 16rpx 30rpx;
				width: 100%;
				min-height: 115rpx;
				position: fixed;
				bottom: 0;
				background: #fff;
				transition: all 0.1s ease;
			}
 
			.uni-textarea {
				padding-bottom: 20rpx;  
				textarea {
					width: calc(100vw - 215rpx);
					min-height: 75rpx;
					max-height: 500rpx;
					background: #f1f1f1;
					border-radius: 40rpx;
					font-size: 32rpx;
					font-family: PingFang SC;
					color: #333333;
					line-height: 74rpx;
					padding: 5rpx 8rpx;
					text-indent: 30rpx;
				}
			}
            
			.send-btn {
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 20rpx;
				margin-left: 25rpx;
				width: 120rpx;
				height: 75rpx;
				background: #4d6bfe;
				border-radius: 50rpx;
				font-size: 28rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #FFFFFF;
				line-height: 28rpx;
			}
		}
	}
</style>