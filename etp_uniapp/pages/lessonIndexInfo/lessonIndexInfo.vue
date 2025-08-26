<!-- 新版课程详情页面 -->
<template>
	<view class="page">
		<!-- #ifdef APP-PLUS -->
		<u-navbar :title="coursewareInfo.mc?coursewareInfo.mc:'课程'" @leftClick="leftClick" :autoBack="true"
			v-if="!isFullScreen&&!isLandscape" :placeholder="true" :border="true">
		</u-navbar>
		<!-- #endif -->
		<!-- #ifdef MP-WEIXIN -->
		<u-navbar :title="coursewareInfo.mc?coursewareInfo.mc:'课程'" @leftClick="leftClick" :autoBack="true"
			:placeholder="true" :border="true">
		</u-navbar>
		<!-- #endif -->

		<!-- 视频类课程 referrer-policy='origin'防盗链-->
		<video id="myVideo" :src="encodeURI(coursewareInfo.kjdz)" referrer-policy='origin'
			:style="'position:fixed;top:'+topSize+'px;width:100vw;z-index:20;' " @ended="end()" @play="play()"
			@pause="pause()" :poster="teachCover(coursewareInfo.fm)" :page-gesture="true" :show-mute-btn="true"
			:title="coursewareInfo.mc" :enable-play-gesture="true" @fullscreenchange="fullscreenchange"
			@error="playError" :custom-cache='false' :autoplay="true" @timeupdate="timeupdate"
			v-if="coursewareInfo&&coursewareInfo.xs == '2'"></video>
		<view class="main" :style="'position:fixed;top:'+topSize+'px;width:100vw;z-index:20;background:#ffffff' "
			v-if="coursewareInfo&&coursewareInfo.xs !='2'" @touchmove.stop.prevent="moveStop">

			<!-- 文章类课程 -->
			<view v-if="coursewareInfo&&coursewareInfo.xs == '3'" style="position: relative">
				<image :src="teachCover(coursewareInfo.fm)" class="img-blur" mode="widthFix"></image>
				<view class="btn-study" hover-class="u-hover-class" @click="toStudyArticle">立即学习</view>
			</view>

			<!-- office类型课程  PPT :style="'height:'+swiperHeight+'px;width:'+swiperWidth+'px'"-->
			<view class="container" v-if="coursewareInfo&&coursewareInfo.xs=='1'&&!isWXPageContainerShow"
				@click.stop="clickSwiperItem" :style="'height:'+swiperHeight+'px;width:100vw'">
				<!-- h5旋转 -->
				<!-- <view :class="[isRotate?'transform-90':'container']"
				v-if="coursewareInfo&&coursewareInfo.xs=='1'&&!isWXPageContainerShow" @click.stop="clickSwiperItem"
				:style="{'height': (isRotate? '100vw':swiperHeight+'px')}"> -->

				<text class="bg-page-index" @touchmove.stop v-if="!isFullScreen">
					{{currentIndex+1}}/{{list.length}}
				</text>
				<text class="bg-page-index-fullscreen" @touchmove.stop v-if="isFullScreen">
					{{currentIndex+1}}/{{list.length}}
				</text>

				<view class="bg-back" @click="exitFullScreen" hover-class="u-hover-class" v-if="isFullScreen">
					<uni-icons type="arrow-left" size="20" color="#ffffff"></uni-icons>
				</view>
				<view v-if="!isHideOperationBtn">
					<view :class="[isFullScreen?'bg-operation-bottom-fixed':'bg-operation-bottom']">
						<view class="slider">
							<u-slider v-model="currentIndex" :max="list.length-1" blockColor="#037EFF"
								activeColor="#037EFF" @changing="sliderChange" @input='sliderChange'
								@click.stop.native="()=>{}" @touchmove.stop>
							</u-slider>


						</view>
						<!-- 下载功能 暂时隐藏 -->
						<!-- 	<view @click.stop="downloadOffice" class="bg-operation-fullscreen" hover-class="u-hover-class"
						v-if="!isTimeline">
						<image class="icon" src="../../static/lesson/icon-download.png"></image>
					</view> -->
						<!-- 竖屏全屏 -->
						<view @click.stop="fullScreen('portrait')" class="bg-operation-fullscreen"
							hover-class="u-hover-class" v-if="!isTimeline&&!isLandscape">
							<image class="icon" :src="fullscreenSrc"></image>
						</view>
						<!-- 横屏全屏 -->
						<!-- #ifndef H5 -->
						<view @click.stop="fullScreen('landscape')" class="bg-operation-fullscreen"
							hover-class="u-hover-class" v-if="!isTimeline&&!isPortrait">
							<image class="icon" :src="landscapeSrc"></image>
						</view>
						<!-- #endif -->
						<!-- #ifdef H5 -->
						<view @click.stop="h5fullScreen" class="bg-operation-fullscreen" hover-class="u-hover-class"
							v-if="!isTimeline&&!isPortrait&&!isLandscape">
							<image class="icon" :src="isLandscape?landtoPortSrc:landscapeSrc"></image>
						</view>
						<!-- #endif -->

					</view>
					<view @click.stop="$u.throttle(pre, 250)" class="bg-page-pre" hover-class="u-hover-class"
						v-if="currentIndex!=0" @touchmove.stop>
						<uni-icons type="back" size="20" color="#ffffff"></uni-icons>
					</view>
					<view @click.stop="$u.throttle(next, 250)" class="bg-page-next" hover-class="u-hover-class"
						v-if="currentIndex!=list.length-1" @touchmove.stop>
						<uni-icons type="forward" size="20" color="#ffffff"></uni-icons>
					</view>


				</view>
				<!-- 滑动组件ppt -->
				<kt-swiper-moveable ref="swiper" :list="list" :getPreviousHandler="gph" :getNextHandler="gnh"
					:dataIndex="currentIndex" @change="change" :vertical="swiperVertical" :width="swiperWidth"
					:height="swiperHeight" :hostPath="coursewareInfo.kjdz" :mode="mode" v-if="isPPT&&!isPortrait">

				</kt-swiper-moveable>
				<!-- word&pdf 竖屏放大  使用list形式 -->
				<view v-else>

					<scroll-view @touchmove.stop scroll-y :scroll-into-view="officeSvgItemId"
						:style="'height:'+swiperHeight+'px;width:100vw;'" @scrolltolower="lower()"
						@scrolltoupper="upper()" :scroll-with-animation="false" @touchstart="isClickEvent = false"
						class="office-scroll" scroll-anchoring>

						<view v-for="(item,imgIndex) in cacheList" :key="imgIndex" :id="'officeSvgItemId'+imgIndex">
							<view class="main-item" :id="imgIndex">
								<kt-img :src="item" :hostPath="svgHostSlice(coursewareInfo.kjdz)">
								</kt-img>
							</view>

						</view>
					</scroll-view>
				</view>

			</view>
			<!-- h5横屏提示框 -->
			<!-- #ifdef H5 -->
			<view class="landspace-tip" v-if="isShowLandSpaceTips&&!isLandscape">
				<image src="../../static/lesson/icon-landscape.png" class="icon-land"></image>
				<view>为了您良好的体验，请确保手机竖屏锁定关闭并将手机横屏操作</view>
			</view>


			<!-- #endif -->

			<!-- #ifdef MP-WEIXIN -->
			<!-- 微信全屏框  微信小程序无法监听onbackpress事件,若想office全屏之后点击返回键退出全屏 需要使用page-container组件-->
			<page-container :show="isWXPageContainerShow" position="center" @beforeleave="beforeLeaveFullScreen()"
				@afterleave="afterLeaveFullScreen()">


				<view class="container" v-if="coursewareInfo&&coursewareInfo.xs=='1'" @click.stop="clickSwiperItem"
					style="'height:100vh;width:100vw'">

					<text class="bg-page-index" @touchmove.stop v-if="!isFullScreen">
						{{currentIndex+1}}/{{list.length}}
					</text>
					<text class="bg-page-index-fullscreen" @touchmove.stop v-if="isFullScreen">
						{{currentIndex+1}}/{{list.length}}
					</text>

					<view class="bg-back" @click="exitFullScreen()" hover-class="u-hover-class" v-if="isFullScreen">
						<uni-icons type="arrow-left" size="20" color="#ffffff"></uni-icons>
					</view>
					<view v-if="!isHideOperationBtn">
						<view class="bg-operation-bottom">
							<view class="slider">
								<u-slider v-model="currentIndex" :max="list.length-1" blockColor="#037EFF"
									activeColor="#037EFF" @changing="sliderChange" @input='sliderChange'>
								</u-slider>


							</view>

							<!-- 竖屏全屏 -->
							<view @click.stop="fullScreen('portrait')" class="bg-operation-fullscreen"
								hover-class="u-hover-class" v-if="!isTimeline&&!isLandscape">
								<image class="icon" :src="fullscreenSrc"></image>
							</view>
							<!-- 横屏全屏 -->
							<!-- #ifndef H5 -->
							<view @click.stop="fullScreen('landscape')" class="bg-operation-fullscreen"
								hover-class="u-hover-class" v-if="!isTimeline&&!isPortrait">
								<image class="icon" :src="landscapeSrc"></image>
							</view>
							<!-- #endif -->


						</view>
						<view @click.stop="$u.throttle(pre, 250)" class="bg-page-pre" hover-class="u-hover-class"
							v-if="currentIndex!=0">
							<uni-icons type="back" size="20" color="#ffffff"></uni-icons>
						</view>
						<view @click.stop="$u.throttle(next, 250)" class="bg-page-next" hover-class="u-hover-class"
							v-if="currentIndex!=list.length-1">
							<uni-icons type="forward" size="20" color="#ffffff"></uni-icons>
						</view>


					</view>
					<view v-if="isPPT&&!isPortrait" style="height:100vh;width:100vw;">
						<!-- 滑动组件ppt -->

						<kt-swiper-moveable ref="swiper" :list="list" :getPreviousHandler="gph" :getNextHandler="gnh"
							:dataIndex="currentIndex" @change="change" :vertical="swiperVertical" :width="swiperWidth"
							:height="swiperHeight" :hostPath="coursewareInfo.kjdz" :mode="mode">

						</kt-swiper-moveable>
					</view>
					<!-- word&pdf 竖屏放大  使用list形式 -->
					<view v-else>

						<scroll-view scroll-y :scroll-into-view="officeSvgItemId"
							:style="'height:'+swiperHeight+'px;width:100vw'" @scrolltolower="lower()"
							@scrolltoupper="upper()" :scroll-with-animation="false" @touchstart="isClickEvent = false"
							class="office-scroll">

							<view v-for="(item,imgIndex) in list" :key="imgIndex" :id="'officeSvgItemId'+imgIndex">
								<view class="main-item" :id="imgIndex">
									<kt-img :src="completePath(item)">
									</kt-img>
								</view>

							</view>
							<!-- <view class="fill-last" :style="'height:'+fillHeight + 'px' "></view> -->
						</scroll-view>
					</view>
				</view>
			</page-container>

			<!-- #endif -->

		</view>

		<!-- 该课程不是任务 并且没有领取的时候显示提示框 -->
		<view class="top" v-if="!isReceived&&!isFullScreen&&!isLandscape&&!isTimeline">

			<text>本课程为免费课程，领取后可在我的课程中查看</text>
			<view class="btn" hover-class="u-hover-class" @click="receiveLesson()">免费领取</view>
		</view>
		<!-- office下载进度条 -->
		<view v-if="isDownLoad">
			<u-line-progress :percentage="downloadPercent" activeColor="#007DFF" :showText="true" height="10">
			</u-line-progress>
		</view>
		<!-- 课程-->
		<view :class="[isReceived?'lesson-info-top':'lesson-info-notop']" v-if="!isFullScreen&&!isLandscape">
			<u-gap height="10" bgColor="#f8f8f8" marginBottom="10px"></u-gap>
			<!-- 课程 -->
			<!-- <view hover-class="u-hover-class" @click="goToLessonInfo"> -->
			<view>
				<view class="flex-row">
					<image :src="teachCover(lesson.fmt)" style="width: 110px;height: 65px;border-radius: 5px;"
						mode="aspectFill"></image>
					<view class="bg-lesson-title" style="margin-left: 8px;">
						<view class="text-lesson-title">{{lesson.mc}}</view>
						<view class="text-lesson-subtitle">
							{{lesson.ms?lesson.ms:""}}
						</view>
						<view class="flex-row-center">
							<uni-icons type="staff" size="15" color="#909199"></uni-icons>
							<text class="text-grey"
								style="margin-left: 3px;margin-right: 8px;">{{lesson.num&&lesson.num!=0?lesson.num:1}}人正在学习</text>
							<text class="text-grey"
								v-if="lesson&&lesson.kcjd&&JSON.parse(lesson.kcjd).rate&&!isTimeline">已学{{Math.round(JSON.parse(lesson.kcjd).rate)}}%</text>
							<!-- <text class="bg-lesson-type" style="margin-left:12px;"
								v-if="taskId && taskId != -1">任务</text> -->
						</view>
					</view>
					<text class="bg-lesson-type" v-if="taskId && taskId != -1">任务</text>

					<!-- 	<uni-icons type="upload" size="28" color="#303133" @click="share()"></uni-icons> -->

					<!-- <view v-if="true"><text class="text-details">详情</text>
						<uni-icons type="forward" size="14" color="#303133"></uni-icons>
					</view> -->
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="share" class="btn-share"></button>
					<!-- #endif -->

				</view>

				<u-gap height="10" bgColor="#f8f8f8" marginBottom="10px"></u-gap>

			</view>


			<!-- 目录列表 -->
			<view class="k-flex-row  k-margin-bottom-m k-flex-row-between k-flex-col-bottom">
				<view class="k-flex-row k-flex-col-center">
					<view class="divier-line"></view>
					<text class="k-margin-start-m k-text-black k-text-bold k-text-xl">目录</text>
				</view>
				<view class="k-flex-row k-margin-end-l " @click="isShowIndex = !isShowIndex"
					hover-class="u-hover-class">
					<text
						class="k-text-grey k-text-sm ">总计{{indexList&&indexList.length>0? indexList.length:0}}节课</text>
					<u-icon name="arrow-right" color="#666666" size="14"></u-icon>
				</view>


			</view>
			<scroll-view :scroll-x="true" style="white-space: nowrap;width: 100%;" :scroll-into-view="itemId">

				<view
					style="width: 150px;display: inline-block;height: 100px;margin: 8px 2px 15px 12px;vertical-align:top;position: relative;"
					v-for="(item, index) in indexList" :key="index" @click="clickIndexItem(index)"
					:class="[indexStyle[getState(index)]]" :id="'view'+index">
					<view
						style="display: flex;flex-direction: row;justify-content: space-between;align-items: center; ">
						<text :class="[typeStyle[getState(index)]]">{{item.xsLabel?item.xsLabel:'课程'}}</text>
						<text style="font-size: 12px;color: #999999;margin-right: 5px;"
							v-if="getState(index)==2&&!isTimeline">已学完</text>
					</view>

					<text class="text-lesson-name">{{item.source_name}}</text>

					<view class="text-num">
						<text>{{index+1}}</text>
					</view>

				</view>

			</scroll-view>

			<u-gap height="10" bgColor="#f8f8f8" marginBottom="10px"></u-gap>
			<!-- 课件详情 -->
			<view class="k-flex-row k-flex-col-center">
				<view class="divier-line"></view>
				<text class="text-lesson-title k-margin-start-m">简介</text>
			</view>
			<view class="k-margin-start-l k-margin-end-l" v-if="coursewareInfo">
				<view class="k-flex-row k-flex-row-between k-flex-col-start">

					<view class="k-margin-top-l" style="flex: 8;width: 0;">
						<text class="text-type">{{$point.transfType(coursewareInfo.xs)}}</text>
						<text class="k-text-break-word k-text-black k-text-bold k-text-lg ">{{coursewareInfo.mc}}</text>

						<view class="k-text-grey k-text-md k-margin-top-sm">{{coursewareInfo.ms}}</view>
					</view>
					<view
						class="k-margin-top-l k-margin-start-m k-text-center k-flex-1 k-text-sm  k-border-s k-text-grey"
						style="height: 35px;
				 background-color:#fff;padding: 8px; border: 1px solid #E8ECF7;vertical-align: middle; align-content:flex-start">
						本节已学
						<view style="color:#f9ae3d ;font-size: 12px;font-weight: bold;width: 60px;"> {{learnTime}}
						</view>
					</view>
				</view>


				<u-gap height="1" bgColor="#f8f8f8" marginBottom="5" marginTop="5"></u-gap>
				<text class="text-tag-bg k-margin-bottom-l"
					v-if="coursewareInfo&&coursewareInfo.learnTime!=0">建议学习时长:{{coursewareInfo.learnTime}}分钟</text>


			</view>
			<u-gap height="10" bgColor="#f8f8f8" marginTop="8">
			</u-gap>
			<!-- 课件对应习题 -->
			<view v-if="exercisesList&&exercisesList.length>0" class="k-margin-top-l">
				<view class="k-flex-row k-flex-col-center">
					<view class="divier-line"></view>
					<text class="text-lesson-title k-margin-start-m">习题</text>
				</view>
				<view hover-class="u-hover-class" class="bg-exercises" @click="toDoExercises">
					<view class="silk-ribbon2">练习</view>

					<view>
						<view class="text-ex-title">{{lesson.mc}}课后习题
						</view>
						<view style="margin-top: 10px;">
							<text class="text-grey-content">学后巩固提升</text>
							<image style="width: 12px; height: 12px; vertical-align: middle;"
								src="../../static/exam/icon_exercises.png"></image>
							<text class="text-primary-content">共{{exercisesList.length}}题</text>

						</view>
					</view>

					<view class="btn-blue">去练习</view>


				</view>
			</view>
		</view>


		<view class="bottom" v-if="isShare&&!isTimeline">

			<text style="margin-left: 15px;">前往首页体验小程序完整功能</text>
			<view class="btn" style="margin-right: 15px;" hover-class="u-hover-class" @click="toIndex()">立即跳转</view>
		</view>

		<view style="z-index: 20;">
			<u-loading-page loading-text="加载中..." loading-mode="spinner" :loading="!isLoaded"></u-loading-page>
		</view>


		<!-- 目录弹窗 -->
		<u-popup :show="isShowIndex" mode="bottom" :round="10" :closeable="true" :closeOnClickOverlay="true"
			@close="close">
			<view style="padding: 15px;">
				<view class="k-text-bold k-text-lg k-margin-bottom-l">选择课件</view>
				<!-- <scroll-view scroll-y style="max-height: 40vh;">
					<view v-for="(item,index) in indexList" :key="index" @click="clickIndexItem(index)"
						:class="[indexStyle[getState(index)]]"
						style="display: inline-block;width: 55px; height: 55px; text-align: center;vertical-align: middle; line-height: 50px;margin-top: 8px;font-weight: bold;">
						<text>{{index+1}}</text>
 r
					</view>


				</scroll-view> -->
				<scroll-view scroll-y style="max-height: 40vh;">
					<view v-for="(item,index) in indexList" :key="index" @click="clickIndexItem(index)"
						:class="[indexStyle[getState(index)]]" style="margin-top: 8px;padding: 8px;">
						<text>{{index+1}}. {{item.source_name}}</text>

					</view>

				</scroll-view>

			</view>
		</u-popup>


	</view>


</template>
<script>
	let observer = null;
	export default {
		data() {
			return {
				isShowIndex: false, //是否展示目录弹窗
				learnSec: 0, //当前课件学习秒数
				learnTime: '00:00:00', //已学时间  从接口获取时间后根据时间推移计时显示
				learnTimeList: [], //学习时长列表
				learnTimer: null, //已学时间的计时器


				time: 10 * 1000, //定时器间隔时长 10s上报一次
				timeInterval: null, //驻留函数的定时器
				isLoaded: false,

				title: "课程", //标题  
				lesson: {},
				coursewareInfo: {}, //课件详情

				lessonId: -1, //课程id
				taskId: -1, //任务id

				isReceived: true, //是否已经领取 默认已经领取 不显示领取按钮

				itemId: '', //横向滑动列表滚动id
				itemClickId: -1, //目录选中id  即当前正在学习的课件id
				currentStudyId: -1, //正在学习的id 
				studyScheduleIndex: -1, //学习课件列表位置

				indexList: null, //课程目录
				exercisesList: [], //习题目录
				list: [], //ppt  svg图片列表

				cacheList: [], //根据当前展示的页码动态加载的图片list

				currentIndex: 0, //office图片查看 swiper展示位置
				swiperVertical: false, //swiper的滑动方式  纵向 横向
				windowWidth: 375, //屏幕宽 单位px
				windowHeight: 600, //屏幕高
				swiperWidth: 375,
				swiperHeight: 225,
				defaultSwiperHeight: 225,
				isFullScreen: false, //是否全屏查看
				isLandscape: false, //是否横屏
				isPortrait: false, //是否竖屏

				isPPT: false, //是否是ppt  若为ppt 当全屏查看时  需要旋转至横屏 支持左右滑动  若不为ppt 全屏查看则为高度铺满 其他元素隐藏 上下滑动查看
				focusIndex: 0, //当前课件位置

				indexStyle: ['bg-selected-index', 'bg-normal-index', 'bg-done-index'],
				typeStyle: ['bg-selected-type', 'bg-normal-type', 'bg-done-type'],
				fullscreenSrc: '../../static/lesson/icon-fullscreen.png',
				landscapeSrc: '../../static/lesson/icon-landscape.png',
				landtoPortSrc: '../../static/lesson/icon-exit-fullscreen.png',

				isDownLoad: false, //是否显示下载条
				downloadPercent: 0, //下载进度
				officePageMap: null, //记录office文件阅读位置 
				isHideOperationBtn: false, //是否隐藏操作按钮
				videoContext: null,
				isWXPageContainerShow: false, //微信页面容器是否显示

				mode: 'aspectFit', //office图片展示样式设置  当非全屏时 使用aspectFit：保持纵横比缩放图片，使图片的长边能完全显示出来。也就是说，可以完整地将图片显示出来 全屏时使用widthFix:宽度不变，高度自动变化，保持原图宽高比不变

				videoOffset: 0,

				isTimeline: false,
				isShare: false, //是否来自分享
				token: "", //分享朋友圈时  朋友圈打开的页面为单页模式 即只能显示单页  无法跳转 无法进行登录操作  则在分享时将当前用的token传过来 请求接口时使用
				officeSvgItemId: "", //纵向滑动的word ppt等文档形式scorll view  的id
				oldOfficeSvgItemId: "", //防止组件属性设置不生效
				topArr: [],
				landscapeTopArr: [],
				portraitTopArr: [],

				scrollTopSize: 0,


				// #ifdef APP
				topSize: 70, //距离顶部默认70 app端默认70 h5端默认45
				// #endif
				// #ifdef MP-WEIXIN
				// topSize: 70, //距离顶部默认70 app端默认70 h5端默认45
				// #endif

				// #ifdef H5
				topSize: 0, //距离顶部默认70 app端默认70 h5端默认45
				// #endif
				fillHeight: 0, // 填充高度，用于最后一项低于滚动区域时使用
				mainThrottle: null,
				cacheThrottle: null,
				isClickEvent: false, //若为点击事件导致的scrollview滑动 不执行scroll事件

				isShowLandSpaceTips: false, //h5横屏提示显示

			};
		},
		onLoad(options) {

			//微信场景代码1154代表 pyq
			// #ifdef MP-WEIXIN
			let launch = uni.getLaunchOptionsSync();
			if (launch.scene == 1154) { //朋友圈
				this.isTimeline = true
				this.token = options.token
				uni.setStorageSync("token", this.token)
			}
			// #endif

			if (options.isShare) {
				this.isShare = options.isShare
				//用户未登录的情况下才保存  若有token则不保存
				if (!uni.getStorageSync("token")) {
					//判断是否分享跳转  若为分享跳转则保存当前页面  在成功登录时候删除
					let routes = getCurrentPages(); // 获取当前打开过的页面路由数组
					let curRoute = routes[routes.length - 1].route // 获取当前页面路由，也就是最后一个打开的页面路由
					//将参数拼接
					curRoute = curRoute.concat("?id=" + options.id)
					uni.setStorageSync("shareRoute", curRoute)
				}


			}



			let that = this;
			that.lessonId = options.id;
			that.taskId = options.taskId;
			if (options.index) {
				that.studyScheduleIndex = Number(options.index) ? Number(options.index) : -1;
			} else {
				//查看进度中正在学习的课程位置
				that.studyScheduleIndex = -1;
			}

			//获取屏幕宽高
			that.initWindow();

		},
		onShareAppMessage() {
			return {
				title: "专家培训平台邀您共同学习《" + this.lesson.mc + "》",
				path: '/pages/lessonIndexInfo/lessonIndexInfo?isShare=true&id=' + this.lessonId,
				imageUrl: this.lesson.fmt

			}
		},
		onShareTimeline() {
			let token = uni.getStorageSync("token");

			return {
				title: "专家培训平台邀您共同学习《" + this.lesson.mc + "》",
				query: `isShare=true&id=${this.lessonId}&token=${token}`,
				imageUrl: this.lesson.fmt
			}
		},
		onShow() {
			//获取课程信息
			if (!this.isLoaded) {
				this.getLesson();
			}

			let that = this;


			//用于onhide关闭定时器--->onshow时重新开启定时器 
			if (that.timeInterval && !that.isTimeline) {
				// console.log("onshow 开启定时器");
				that.startInterval()

			}

		},
		onResize() {
			let that = this;
			uni.getSystemInfo({
				success: function(res) {
					let width = res.windowWidth;
					let height = res.windowHeight;
					if (width > height) { //横屏
						that.isLandscape = true;
						that.swiperWidth = that.windowHeight;
						that.swiperHeight = that.windowWidth;

					} else { //竖屏
						that.isLandscape = false;
						that.swiperHeight = that.defaultSwiperHeight
						that.swiperWidth = width;
					}

				}
			});


		},
		onUnload() {
			// console.log("onUnload");
			//页面卸载时 销毁定时器
			this.pauseInterval();

			// #ifdef APP-PLUS
			plus.navigator.setFullscreen(false);
			//设置竖屏
			plus.screen.lockOrientation("portrait")
			// #endif

			//页面卸载上报一次
			if (!this.isTimeline) {
				this.stayEventUpload()
			}

			//停止监听
			if (observer) {
				observer.disconnect();
			}

		},
		onHide() {
			// console.log("onHide");
			//页面隐藏 暂停定时器
			this.pauseInterval();
		},
		onBackPress() {
			//页面返回时 销毁定时器
			this.pauseInterval();

			// #ifdef APP-PLUS || MP-WEIXIN
			if (this.isFullScreen && this.coursewareInfo.xs == '1') { //当课件类型为office时

				this.fullScreen('landscape');
				return true;
			}
			// #endif


		},
		onReady() {
			this.videoContext = uni.createVideoContext('myVideo', this)
			this.initScrollView();

			// #ifdef H5
			window.addEventListener('orientationchange', this.monitorScreen)
			// #endif


		},

		methods: {
			close() {
				this.isShowIndex = false;
			},


			//h5监听旋转
			monitorScreen() {
				if (window.orientation == 0 || window.orientation == 180) {
					if (this.coursewareInfo.xs == 1) {
						this.isLandscape = false;
						this.swiperHeight = this.defaultSwiperHeight;
						this.isShowLandSpaceTips = false;
						this.isFullScreen = false;
						this.isPortrait = false;
					}


				} else if (window.orientation == 90 || window.orientation == -90) {
					if (this.coursewareInfo.xs == 1) {
						this.swiperHeight = this.windowWidth;
						this.isShowLandSpaceTips = false;
						this.isLandscape = true;
						this.isFullScreen = true;
						this.isPortrait = false;
					}

				}
			},

			//配置list数据

			changeList(currentIndex) {

				//根据当前index 替换list中的元素  前后三个图片
				if (!this.cacheList || this.cacheList.length <= 0) {
					this.cacheList = new Array(this.list.length);
				}


				this.cacheList[currentIndex] = this.list[currentIndex];

				for (let i = 1; i < 5; i++) {

					let lastIndex = currentIndex - i;
					let preIndex = currentIndex + i;

					if (lastIndex >= 0) {
						this.cacheList[lastIndex] = this.list[lastIndex]
					}
					if (preIndex < this.list.length) {
						this.cacheList[preIndex] = this.list[preIndex]
					}

				}
				// console.log(JSON.stringify(this.cacheList));

			},

			//分页控制器
			intersectionLinster() {
				let that = this;
				if (observer) {
					observer.disconnect();
				}
				let margins = this.swiperHeight / 2;
				let marignTop = -(margins - 20);
				let marginBottom = -(margins - 20);

				observer = uni.createIntersectionObserver(this, {
					observeAll: true
				});



				observer.relativeTo('.office-scroll', {
					top: marignTop,
					bottom: marginBottom
				}).observe('.main-item', (res) => {

					if (res.intersectionRatio > 0 && !this.isClickEvent) {
						// console.log(JSON.stringify(res));
						that.currentIndex = Number(res.id)
						// console.log(">>>>>index改变intersectionLinster>>>", that.currentIndex);

						that.changeList(that.currentIndex)
					}
				})


			},

			//驻留事件上报
			stayEventUpload() {
				let that = this;
				let pt = (that.taskId && that.taskId != -1) ? 1 : 0; //课件入口类型，0-课程；1-任务
				let pid = (that.taskId && that.taskId != -1) ? that.taskId : that.lessonId; //课件入口id  课程id或者任务id
				//根据当前课件类型  记录学习进度
				let offset = null;
				if (that.coursewareInfo.xs == '2') { //视频
					offset = that.videoOffset;
				} else if (that.coursewareInfo.xs == '1') { //office
					offset = that.currentIndex + 1;
				}
				if (that.coursewareInfo != null && that.coursewareInfo.id != null) {
					that.$point.stayEvent(that.$constant.ING_STUDY, that.coursewareInfo.id,
						pt, pid, offset);
				}

			},
			timeupdate(event) {
				this.videoOffset = Math.round(event.detail.currentTime);
			},
			playError(e) {
				uni.showToast({
					title: '视频播放出错 ' + e.target.errMsg,
					icon: 'none'
				});
			},
			initWindow() {
				let that = this;
				that.swiperHeight = that.defaultSwiperHeight;
				uni.getSystemInfo({
					success: function(res) {
						that.windowWidth = res.windowWidth;
						that.windowHeight = res.windowHeight;
						that.swiperWidth = that.windowWidth;
						// console.log("屏幕宽度windowWidth", that.windowWidth);
						// console.log("屏幕高度windowHeight", that.windowHeight);

					}
				});
			},

			//微信全屏离开前触发
			// #ifdef MP-WEIXIN
			beforeLeaveFullScreen() { //将屏幕修改为竖屏 模式改为非全屏


				this.isClickEvent = true; //阻止scroll事件回调
				// this.isHideOperationBtn = true; //隐藏操作按钮

				this.isWXPageContainerShow = false; //将全屏页隐藏
				this.isLandscape = false;
				this.isPortrait = false;
				this.isFullScreen = false;

				this.swiperVertical = false;
				this.mode = "aspectFit";
				this.topSize = this.scrollTopSize;

				this.swiperHeight = this.defaultSwiperHeight

				this.officeSvgItemId = "officeSvgItemId0";
				this.$nextTick(() => {
					setTimeout(() => {
						this.officeSvgItemId = "officeSvgItemId" + this.currentIndex;
						this.intersectionLinster()

					}, 10);

				})

			},
			afterLeaveFullScreen() { //全屏页面隐藏之后

			},

			// #endif
			moveStop() {},

			//h5横屏放大 todo  当前问题  旋转之后 scorllview 滑动xy轴未改变 宽度未改变
			h5fullScreen() {
				if (this.isLandscape) { //横屏状态点击缩小按钮
					this.swiperHeight = this.defaultSwiperHeight
					this.isShowLandSpaceTips = false;
					this.isLandscape = false;
					this.isPortrait = false;
					this.isFullScreen = false;
				} else {
					this.isShowLandSpaceTips = true;
				}

			},
			//放大缩小
			fullScreen(type) { //landscape为横屏放大  portrait为竖屏放大
				this.isClickEvent = true; //阻止scroll事件回调
				// this.isHideOperationBtn = true; //隐藏操作按钮

				if (this.isFullScreen) { //当前为全屏 点击退出全屏
					//点击缩小 退出全屏
					// #ifdef APP-PLUS
					//显示状态栏
					plus.navigator.setFullscreen(false);
					// #endif
					// #ifdef MP-WEIXIN
					this.isWXPageContainerShow = false; //将全屏页隐藏
					// #endif
					this.isLandscape = false;
					this.isPortrait = false;

					this.swiperVertical = false;
					this.mode = "aspectFit";

					if (type == 'landscape') {

						// #ifdef APP-PLUS
						//设置竖屏锁定
						plus.screen.lockOrientation("portrait")

						// #endif
						// #ifdef MP-WEIXIN
						wx.setPageOrientation({
							orientation: 'portrait'
						})
						// #endif
						this.swiperWidth = this.windowWidth
						this.landscapeSrc = '../../static/lesson/icon-landscape.png'
						// //横屏状态退出全屏
						// this.topArr = [];
						// this.topArr = this.portraitTopArr; //恢复竖屏时的高度大小

					} else {
						this.fullscreenSrc = '../../static/lesson/icon-fullscreen.png';
					}
					this.topSize = 0;
					this.topSize = this.scrollTopSize;
					this.swiperHeight = this.defaultSwiperHeight


				} else { //点击放大 进入全屏

					this.topSize = 0;
					// #ifdef APP-PLUS
					plus.navigator.setFullscreen(true); //设置全屏
					// #endif
					// #ifdef MP-WEIXIN
					this.isWXPageContainerShow = true;
					// #endif

					//判断是否是ppt  
					if (this.isPPT) {
						//若为ppt 当全屏查看时  需要旋转至横屏 支持左右滑动  若不为ppt 全屏查看则为高度铺满 其他元素隐藏 上下滑动查看
						//设置左右滑动
						this.swiperVertical = false;
						//设置图片展示为默认
						this.mode = "aspectFit";
					} else {
						this.mode = "widthFix";
						this.swiperVertical = true;
					}

					if (type == 'landscape') {
						this.isPortrait = false;
						this.landscapeSrc = '../../static/lesson/icon-portrait.png'
						//设置App切换横屏
						// #ifdef APP-PLUS
						plus.screen.lockOrientation("landscape"); //设置横屏
						// #endif

						// #ifdef MP-WEIXIN
						wx.setPageOrientation({
							orientation: 'landscape'
						})
						// #endif

					} else {
						this.isPortrait = true;
						this.fullscreenSrc = '../../static/lesson/icon-exit-fullscreen.png';
						this.swiperHeight = this.windowHeight
					}

				}


				this.isFullScreen = !this.isFullScreen;


				// if (this.isPortrait||(this.isLandscape&&!this.isPPT)) {
				// 	this.$nextTick(() => {
				// 		setTimeout(() => {
				// 			console.log("页码控制器启动》》》》");
				// 			this.intersectionLinster()

				// 		}, 10);
				// 	})
				// }

				/* 获取最后一项的高度，设置填充高度。判断和填充时做了 +-20 的操作，是为了滚动时更好的定位  全屏之后 由于高度改变 需要重新设置填充高度 */
				// if (this.topArr && this.topArr.length > 0) {
				// 	let last = this.topArr[this.topArr.length - 1].height;
				// 	if (last - 20 < this.swiperHeight) {
				// 		this.fillHeight = this.swiperHeight - last + 20;
				// 	} else {
				// 		this.fillHeight = 0;
				// 	}
				// 	// console.log(">>>fillHeight:" + this.fillHeight + ">>>swiperHeight:" + this.swiperHeight +
				// 	// 	">>>last:" + last + ">>>topsize:" + this.topSize);
				// } else {
				// 	this.$nextTick(() => {
				// 		setTimeout(() => {
				// 			// this.getElementTop();
				// 			this.intersectionLinster()
				// 		}, 10);
				// 	});

				// }

				this.officeSvgItemId = "officeSvgItemId0";
				this.$nextTick(() => {
					setTimeout(() => {
						this.officeSvgItemId = "officeSvgItemId" + this.currentIndex;
						this.intersectionLinster()

					}, 10);

				})


			},
			//点击某一项
			clickIndexItem(index) {
				// this.itemId = "view" + index; //横向目录滚动列表 位置定位
				//切换课件时上报一次
				if (!this.isTimeline) {
					this.stayEventUpload();
				}


				this.focusIndex = index;
				this.itemClickId = this.indexList[index].sid;
				this.studyScheduleIndex = index;
				//若点击当前项目显示的为office则记录
				if (this.coursewareInfo.xs == '1') {
					if (!this.officePageMap) {
						this.officePageMap = new Map();
					}

					this.officePageMap.set(this.coursewareInfo.id, this.currentIndex);
				}


				//请求课件详情 
				this.getCoursewareInfo(this.indexList[index].sid);
				//更新学习进度
				this.updateSchedule(this.indexList[index].sid);

			},
			//获取课件当前状态  0 选中 1正常 2已学完
			getState(index) {

				let currectId = this.indexList[index].sid;

				if (this.itemClickId == currectId) { //选中
					return 0;
				}
				if (this.indexList[index].rate && this.indexList[index].rate == 100) {
					return 2;
				} else {
					//判断进度
					return 1;
				}

			},

			downloadOffice() {

				let that = this;
				//查看缓存中是否已经下载  保存缓存地址 
				let openUrl = encodeURI(that.coursewareInfo.kjdz);

				let path = uni.getStorageSync(openUrl);

				if (path) {
					uni.openDocument({
						filePath: path,
						showMenu: true,
						success: function(res) {
							uni.hideLoading();
						},
						fail: function() {
							that.download(openUrl)
							uni.hideLoading();

						}
					});
				} else {
					that.download(openUrl)
				}


			},

			//下载
			download(openUrl) {
				// #ifndef H5
				let that = this;
				that.isDownLoad = true;

				const downloadTask = uni.downloadFile({
					url: openUrl,
					success: function(res) {
						that.isDownLoad = false;
						if (res.statusCode == 200) {

							uni.showToast({
								title: '文档下载成功',
								icon: 'none'
							});
							let filePath = res.tempFilePath;
							uni.setStorage({
								key: openUrl,
								data: filePath
							});
							//下载完成后打开文档
							uni.openDocument({
								filePath: filePath,
								showMenu: true,
								success: function(res) {
									//打开文档成功
								}
							});
						} else {
							uni.showToast({
								title: '文件下载失败',
								icon: 'none'
							});
						}

					}
				});

				downloadTask.onProgressUpdate((res) => {
					this.downloadPercent = res.progress;
				});
				// #endif

				// #ifdef H5
				window.location.href = openUrl;
				// #endif

			},
			//视频开始继续播放时触发
			play() {
				// this.$emit("study",this.coursewareInfo.id);//通知父组件更新进度
				//驻留事件埋点  定时五分钟向接口发送一次请求 开始播放
				this.startInterval();

			},
			pause() {
				//视频暂停播放  暂停定时器
				this.pauseInterval();
			},
			//视频播放结束
			end() {
				this.pauseInterval();
				if (this.studyScheduleIndex != -1) {
					let nextIndex = this.studyScheduleIndex + 1;
					if (this.indexList.length > nextIndex) {
						uni.showToast({
							title: '视频播放结束 即将为您跳转下一节学习',
							icon: 'none'
						});
						setTimeout(() => {
							this.clickIndexItem(nextIndex);
						}, 3000);
					}
				}

			},
			fullscreenchange() {},
			//跳转到课程详情
			goToLessonInfo() {

				uni.navigateTo({
					url: '/pages/lessonDetail/lessonDetail?id=' + this.lesson.id,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			//根据课程id获取课程目录和详细信息
			getLesson() {

				if (this.lessonId == null || this.lessonId == -1) {
					return;
				}
				let params = {
					id: this.lessonId
				}
				//如果是任务中的课程 
				if (this.taskId && this.taskId != -1) {
					params.rwid = this.taskId;
				}
				let config = "";
				if (this.token) {
					config = {
						header: {
							"Authorization": this.token
						}
					}
				}

				uni.$u.api.getLessonIndex(params, config).then(res => {

					if (res.data) {
						this.isLoaded = true;
						this.lesson = res.data
						let index = res.data.tCurriculumContents;
						let lessonList = [];
						let exercises = [];
						if (index && index.length > 0) {
							//将课程与习题分开
							for (let i = 0; i < index.length; i++) {
								if (index[i].source_type == "kj") {
									lessonList.push(index[i])
								} else if (index[i].source_type == "st") {
									exercises.push(index[i])
								}
							}
							this.indexList = lessonList; //目录
							this.exercisesList = exercises; //习题
						}


						let status = this.lesson.getStatus;
						if (status == "1") { //已经领取
							this.isReceived = true;
						} else {
							this.isReceived = false;
							if (this.taskId && this.taskId != -1) { //任务中的课程不显示领取提示
								this.isReceived = true;
							}
						}
						let jd = {};
						if (this.lesson.kcjd) {
							jd = JSON.parse(this.lesson.kcjd);
						}


						//判断是否是任务中的课程 取currentId
						if (this.taskId && this.taskId != -1) { //任务中的课程

							this.currentStudyId = this.lesson.currentId ? this.lesson.currentId : -1;

							if (this.studyScheduleIndex && this.studyScheduleIndex == -
								1) { //若studyId为-1即不是点击目录而来 那么目录选中的id等于当前进度中的id
								this.itemClickId = this.currentStudyId == -1 ? this.indexList[0]
									.sid : this.currentStudyId;
							} else {
								this.itemClickId = this.indexList[this.studyScheduleIndex].sid;
							}


						} else { //普通课程
							if (this.studyScheduleIndex && this.studyScheduleIndex == -1) {


								if (jd.lastStudy && JSON.stringify(jd.lastStudy != '{}')) {
									if (jd.lastStudy.current) {
										this.itemClickId = jd.lastStudy.current;
										this.currentStudyId = jd.lastStudy.current;
									} else { //没有进度  从第一个开始
										this.itemClickId = this.indexList[0].sid;
									}




								} else { //没有进度  从第一个开始
									this.itemClickId = this.indexList[0].sid;
								}
							} else {
								this.itemClickId = this.indexList[this.studyScheduleIndex].sid;
							}
						}



						//获取当前课件详情
						this.getCoursewareInfo(this.itemClickId);

						//判断当前id在列表中的位置
						let currentIndex = (this.indexList || []).findIndex((item) => item.sid == this
							.itemClickId);
						let that = this;
						that.$nextTick(() => {
							that.itemId = "view" + currentIndex;

						});

						this.studyScheduleIndex = currentIndex;



					}


				}).catch(err => {
					console.log(err);
				})
			},

			//领取课程
			receiveLesson() {
				uni.showLoading({
					title: '领取中...',
					mask: false
				});

				let params = {
					kcid: Number(this.lessonId)
				}

				uni.$u.api.receiveLesson(params).then(res => {
					uni.hideLoading();
					if (res.code == 200) {
						this.isReceived = true;

						uni.showToast({
							title: '课程领取成功'
						});
					}

				}).catch(err => {
					uni.hideLoading();
				})
			},

			//根据学习进度 读取课件信息 已废弃
			getStudySchedule() {
				if (this.lesson.tCurriculumContents) {
					let currectStudy = this.lesson.tCurriculumContents[this.currentIndex];
					if (currectStudy.source_type == "st") {
						this.isExam = true;
						//加载习题相关内容
						this.getQuestionInfo(currectStudy.sid);
					} else {
						this.isExam = false;
						//加载课件相关内容
						this.getCoursewareInfo(currectStudy.sid);
					}
				}

			},

			//获取课件信息
			getCoursewareInfo(id) {
				uni.showLoading({
					title: '请稍候',
					mask: false
				});
				let config = "";
				if (this.token) {
					config = {
						header: {
							"Authorization": this.token
						}
					}
				}

				uni.$u.api.getCourswareInfo(id, config).then(res => {
					uni.hideLoading();
					if (res.data) {

						this.coursewareInfo = res.data;
						//获取本节课历史学习时长 若列表中包含 比对两个时间大小  使用时间长的继续展示 
						let t = this.coursewareInfo.lastStudyTime;
						let studyTime;
						if (t) {
							studyTime = t * 60;
						} else {
							studyTime = 0;
						}

						let localStudyTimeIndex = (this.learnTimeList || []).findIndex((item) => item.id ==
							this.coursewareInfo.id);
						if (localStudyTimeIndex != -1 && this.learnTimeList[localStudyTimeIndex].time >
							studyTime) {
							//比较本地保存时间与云端时间
							this.learnSec = this.learnTimeList[localStudyTimeIndex].time;
							this.learnTime = this.formatSeconds(this.learnTimeList[localStudyTimeIndex].time);
						} else {
							this.learnSec = studyTime;
							this.learnTime = this.formatSeconds(studyTime);
						}

						//判断课件类型
						let type = this.coursewareInfo.xs;
						if (type != '2') { //类型不为视频 直接开始定时器
							this.startInterval();
						}

						if (type == '1') { //课件类型为office

							this.isHideOperationBtn = false;
							//5秒后隐藏操作按钮
							// setTimeout(() => {
							// 	this.isHideOperationBtn = true;
							// }, 5000);

							//判断是否是ppt
							if (this.coursewareInfo.suffix == 'ppt') {
								this.isPPT = true;
							} else {
								this.isPPT = false;
							}
							this.currentIndex = 0; //初始化页码
							if (this.coursewareInfo.metaData) {
								let svgList = JSON.parse(this.coursewareInfo.metaData);
								this.list = [];
								this.list = svgList.list;

								//获取页码
								if (this.officePageMap) {
									let num = this.officePageMap.get(this.coursewareInfo.id);
									if (num) {
										this.currentIndex = Number(num);
									}
								} else { //若本地没有保存的页码 则初始化为第一页
									this.currentIndex = 0;
								}

								// console.log(">>>>>index改变getCoursewareInfo>>>", this.currentIndex);

							}
							//初始化图片列表
							this.cacheList = []
							this.changeList(this.currentIndex)
							// 监听页面元素相交
							if (!this.isPPT) {

								this.$nextTick(() => {
									setTimeout(() => {
										//防止组件元素不生效
										this.officeSvgItemId = "officeSvgItemId0";
										this.officeSvgItemId = "officeSvgItemId" + this
											.currentIndex; //跳转到对应的页  再对元素进行监听
										this.intersectionLinster();
									}, 10);
								});


							}

						}

					}


				}).catch(err => {
					console.log(err);
					uni.hideLoading();

				})
				//课件点击事件埋点
				this.$point.clickEvent(this.$constant.CLI_COUR, id);
			},

			//获取练习题的信息
			getQuestionInfo(id) {

				uni.$u.api.getQuestionInfo(id).then(res => {

					if (res.data) {}

				}).catch(err => {
					console.log(err);
				})
			},

			//更新进度
			updateSchedule(studyId) {

				if (this.taskId && this.taskId != -1) {
					this.updateTaskSchedule(studyId)
				} else {
					this.updateLessonSchedule(studyId)
				}


			},
			//更新课程进度
			updateLessonSchedule(studyId) {
				let currIds = [];
				let ids = this.indexList;
				for (var i = 0; i < ids.length; i++) {
					if (ids[i].source_type == "kj") {
						currIds.push(ids[i].sid)
					}

				}
				let contentIds = currIds.join(",");

				let params = {
					id: this.lesson.id,
					jd: studyId,
					contentIds: contentIds
				}

				uni.$u.api.updateStatus(params).then(res => {

					let rate = res.data;
					if (rate == 100) { //全部课程已经学完

					}

				}).catch(err => {
					console.log(err);
				})


			},
			//更新任务进度
			updateTaskSchedule(studyId) {

				let params = {
					rwid: this.taskId,
					kjid: studyId,
				}

				uni.$u.api.updateTaskStatus(params).then(res => {
					let rate = res.data;
					if (rate == 100) {
						// uni.showToast({
						// 	title: '太棒啦，任务完成啦'
						// });
					}


				}).catch(err => {
					console.log(err.errMsg);
				})
			},
			//富文本学习
			toStudyArticle() {
				let articleId = this.coursewareInfo.id;
				uni.navigateTo({
					url: '/pages/article/article?articleId=' + articleId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

				this.startInterval()
			},
			//开启定时器
			startInterval() {
				if (!this.isTimeline) { //判断是否是从朋友圈分享打开 朋友圈打开不进行埋点
					this.pauseInterval(); //首先清除定时器
					this.stayEventUpload(); //定时开始之前先执行一次该函数
					this.timeInterval = setInterval(() => {
						this.stayEventUpload()

					}, this.time);

					this.learnTimerbegin();

				}
			},
			//定时器暂停
			pauseInterval() {
				//定时器关闭之前执行一次该函数
				this.stayEventUpload()
				if (this.timeInterval) {
					clearInterval(this.timeInterval)
					this.timeInterval = null;

					this.pauseLearnTimer();
				}
			},


			//去做习题
			toDoExercises() {
				uni.navigateTo({
					url: '/pages/lessonExercises/lessonExercises?lessonId=' + this.lessonId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},


			leftClick() {
				//判断页面栈有几个页面 若小于两个 则返回首页
				let pages = getCurrentPages(); //获取加载的页面

				if (pages && pages.length >= 2) {
					return false
				} else {
					//跳转到首页
					uni.switchTab({
						url: '/pages/index/index',
					})
				}



				// #ifdef APP-PLUS
				if (this.isFullScreen && this.coursewareInfo.xs == '1') {

					this.fullScreen();
					return true;
				}
				// #endif


			},
			pre() {
				if (this.isPPT && !this.isPortrait) {
					this.$refs.swiper.goPrevious()
				} else {
					this.isClickEvent = true;
					this.currentIndex--;
					this.officeSvgItemId = "officeSvgItemId" + this.currentIndex;
					this.changeList(this.currentIndex);
				}

			},
			next() {
				if (this.isPPT && !this.isPortrait) {
					this.$refs.swiper.goNext()
				} else { //若课件不是ppt即 课件为word或pdf格式
					this.isClickEvent = true;
					this.currentIndex++;
					this.officeSvgItemId = "officeSvgItemId" + this.currentIndex;
					this.changeList(this.currentIndex);
				}

			},
			gph: () => {


			},
			gnh: () => {

			},
			change(currentIndex) {
				this.currentIndex = currentIndex

			},
			//滑动选择器滑动
			sliderChange(value) {
				if (!this.isPPT || this.isPortrait) { //若课件不是ppt即 课件为word或pdf格式 或者竖向全屏
					this.isClickEvent = true;
					this.officeSvgItemId = "officeSvgItemId" + value;

					this.changeList(value)
				}
			},
			//图片host 截取
			svgHostSlice(path) {
				let host = "";
				if (path && path.lastIndexOf('.') != -1) {
					let end = path.lastIndexOf('.');
					host = path.slice(0, end);
				}
				return host;

			},
			completePath(svgPath) {
				let fullPath = svgPath;

				if (svgPath && svgPath.indexOf("http") == -1) { //需要拼接
					let hostPath = this.svgHostSlice(this.coursewareInfo.kjdz);
					fullPath = hostPath + "/" + svgPath;
				}
				return fullPath;

			},
			getHostPath() {
				return this.svgHostSlice(this.coursewareInfo.kjdz);
			},

			//点击office 显示和隐藏 操作按钮
			clickSwiperItem() {
				this.isHideOperationBtn = !this.isHideOperationBtn;

			},
			encodeUrl(url) {
				return encodeURIComponent(url);
			},
			//跳转到首页
			toIndex() {
				//跳转到首页
				uni.switchTab({
					url: '/pages/index/index',
				})
			},

			//学习时长开始计时
			learnTimerbegin() {
				this.learnTimer = setInterval(this.startLearnTimer, 1000);
			},
			//开始正计时
			startLearnTimer() {
				//秒数+1 后format
				this.learnSec++;
				this.learnTime = this.formatSeconds(this.learnSec);

				//找到定时器数组中是否存在当前id  有则更新 没有则新增
				let currentStudyTimeIndex = (this.learnTimeList || []).findIndex((item) => item.id == this.coursewareInfo
					.id);
				if (currentStudyTimeIndex != -1) {
					this.learnTimeList[currentStudyTimeIndex].time = this.learnSec;
				} else {
					this.learnTimeList.push({
						id: this.coursewareInfo.id,
						time: this.learnSec
					})
				}
			},
			// 暂停倒计时
			pauseLearnTimer() {
				if (this.learnTimer) {
					clearInterval(this.learnTimer);
					this.learnTimer = null
				}
			},

			//将秒转换为时分秒显示
			formatSeconds(value) {
				let secondTime = parseInt(value); // 秒
				let minuteTime = 0; // 分
				let hourTime = 0; // 小时
				if (secondTime > 60) { //如果秒数大于60，将秒数转换成整数
					//获取分钟，除以60取整数，得到整数分钟
					minuteTime = parseInt(secondTime / 60);
					//获取秒数，秒数取佘，得到整数秒数
					secondTime = parseInt(secondTime % 60);
					//如果分钟大于60，将分钟转换成小时
					if (minuteTime > 60) {
						//获取小时，获取分钟除以60，得到整数小时
						hourTime = parseInt(minuteTime / 60);
						//获取小时后取佘的分，获取分钟除以60取佘的分
						minuteTime = parseInt(minuteTime % 60);
					}
				}
				//补0
				if (hourTime < 10) {
					hourTime = "0" + hourTime
				}
				if (minuteTime < 10) {
					minuteTime = "0" + minuteTime
				}
				if (secondTime < 10) {
					secondTime = "0" + secondTime
				}

				let result = hourTime + ":" + minuteTime + ":" + secondTime

				return result;
			},
			lower() {},
			upper() {},
			//word  pdf文档查看
			scroll(e) {

				// 节流方法
				clearTimeout(this.mainThrottle);
				if (!this.isClickEvent) {
					this.mainThrottle = setTimeout(() => {
						// console.log(">>>>scrollFn();");
						scrollFn();
					}, 10);
				}


				let scrollFn = () => {
					let top = e.detail.scrollTop;
					let index = 0;
					/* 查找当前滚动距离 */
					for (let i = (this.topArr.length - 1); i >= 0; i--) {
						/* 在部分安卓设备上，因手机逻辑分辨率与rpx单位计算不是整数，滚动距离与有误差，增加2px来完善该问题 */
						if ((top + 2) >= this.topArr[i].top) {
							// console.log("滚动距离top", top);
							index = i;
							break;
						}
					}
					this.currentIndex = (index < 0 ? 0 : index);

				}
			},
			/* 获取元素顶部信息 */
			getElementTop() {

				new Promise((resolve, reject) => {
					setTimeout(() => {
						let view = uni.createSelectorQuery().in(this).selectAll('.main-item');

						view.boundingClientRect(data => {
							resolve(data);
						}).exec();
					}, 0)

				}).then((res) => {
					//res中top的值是相对于显示屏幕来说的，所以使用元素的高度迭代相加来计算top的值
					if (res && res.length > 0) {
						for (let i = 0; i < res.length; i++) {
							if (i == 0) {
								res[i].top = 0
							} else {
								res[i].top = res[i - 1].top + res[i - 1].height - this.topSize
							}
						}
						// console.log(JSON.stringify(res));
						if (this.isLandscape) {
							this.landscapeTopArr = res;
						} else {
							this.portraitTopArr = res;
						}

						this.topArr = res;

						/* 获取最后一项的高度，设置填充高度。判断和填充时做了 +-20 的操作，是为了滚动时更好的定位   */

						let last = res[res.length - 1].height;
						if (last - 20 < this.swiperHeight) {
							this.fillHeight = this.swiperHeight - last + 20;
						} else {
							this.fillHeight = 0;
						}
					} else {
						console.log("未获取到res");
					}


				});
			},
			/* 初始化滚动区域 */
			initScrollView() {

				return new Promise((resolve, reject) => {
					let view = uni.createSelectorQuery().select('.main');
					view.boundingClientRect(res => {

						if (res) {
							// console.log(JSON.stringify(res));
							this.scrollTopSize = res.top;
							this.topSize = res.top;
						}
						setTimeout(() => {
							resolve();
						}, 100);
					}).exec();
				});
			},
			// toJSON() {
			// 	console.log(">>>>>>>>>>>TOJSON");
			// }

			//全屏状态下退出全屏
			exitFullScreen() {
				let param = this.isPortrait ? 'portrait' : 'landscape';
				this.fullScreen(param);
			},
		},


	};
</script>
<style lang="scss">
	page {
		height: 100vh
	}

	.title-placeholder {
		/* #ifndef H5 || MP-WEIXIN || APP-PLUS */
		height: 75px;
		/* #endif */


	}

	.status-bar {
		/* #ifndef H5 */
		height: var(--status-bar-height);
		/* #endif */
	}

	.bg-title {
		text-align: center;
		height: 44px;
		padding-left: 15px;
	}

	.icon-back {
		display: absoult;
		left: 0;
	}

	.text-title {
		flex: 1;
		text-align: center;
	}

	.page {
		// height: 100vh;
		// display: flex;
		// flex-direction: column;

		.main {
			// flex: 1
		}

		.actions {}

		.body {
			height: 100%;
		}
	}

	.lesson-info-notop {
		margin-top: 0px;
	}

	.lesson-info-top {
		margin-top: 225px;
	}

	.container {

		position: relative;
		width: 100vw;
		// height: auto;
		// min-height: 200px;
	}

	.bg-page-index {
		background-color: rgba(0, 0, 0, 0.2);
		border-radius: 2px;
		padding: 5px 5px;
		font-size: 14px;
		color: white;
		position: absolute;
		top: 0;
		left: 0;
		margin: 30px 25px 0px 20px;
		z-index: 1;
		height: 25px;
		line-height: 25px;
		text-align: center;
		vertical-align: middle;
	}

	.bg-page-index-fullscreen {
		background-color: rgba(0, 0, 0, 0.2);
		border-radius: 2px;
		padding: 5px 5px;
		font-size: 14px;
		color: white;
		position: absolute;
		top: 0;
		left: 0;
		margin: 45px 20px 0px 65px;
		z-index: 1;
		height: 25px;
		line-height: 25px;
		text-align: center;
		vertical-align: middle;

	}



	.bg-page-pre {
		width: 40px;
		height: 40px;
		background-color: rgba(0, 0, 0, 0.2);
		border-radius: 2px;
		position: absolute;
		top: 45%;
		left: 0;
		margin: 0 0 0 45px;
		width: 40px;
		height: 40px;
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 1;
		text-align: center;
		vertical-align: middle;
		line-height: 40px;
	}

	.bg-page-next {
		width: 40px;
		height: 40px;
		background-color: rgba(0, 0, 0, 0.2);
		border-radius: 2px;
		position: absolute;
		top: 45%;
		right: 0;
		margin: 0 45px 0 0;
		z-index: 1;
		display: flex;
		justify-content: center;
		align-items: center;
		text-align: center;
		vertical-align: middle;
		line-height: 40px;
	}

	.bg-operation-bottom-fixed {
		width: 100%;
		height: auto;
		background-color: rgba(0, 0, 0, 0.2);
		position: fixed;
		bottom: 0;
		padding: 8px 0px;
		z-index: 10;
		display: flex;
		flex-direction: row;

	}

	.bg-operation-bottom {
		width: 100%;
		height: auto;
		background-color: rgba(0, 0, 0, 0.2);
		position: absolute;
		bottom: 0;
		padding: 8px 0px;
		z-index: 10;
		display: flex;
		flex-direction: row;



	}

	.slider {
		flex: 1;
	}

	.bg-operation-fullscreen {
		width: 35px;
		height: 35px;
		background-color: rgba(0, 0, 0, 0.2);
		border-radius: 2px;
		color: white;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-right: 20px;

		.icon {
			width: 20px;
			height: 20px;
		}
	}


	.landspace-tip {
		background-color: #2e2e2e;
		color: #626262;
		font-size: 22px;
		text-align: center;
		width: 100%;
		height: 226px;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;

		position: fixed;
		top: 0;
		z-index: 30;
	}


	.bg-normal-index {
		background: rgba(255, 255, 255, 0.08);
		border-radius: 6px;
		color: #303133;
		border: 1px solid #E8ECF7;
		font-size: 16px;
		display: flex;
		flex-direction: column;
		margin-right: 12px;
	}

	.bg-selected-index {
		// box-shadow: rgba(100, 100, 111, 0.1) 0px 7px 12px 0px;
		border: 1px solid #3A86F4;
		background: rgba(0, 125, 255, 0.08);
		border-radius: 6px;
		color: #3A86F4;
		font-weight: bold;
		font-size: 16px;
		display: flex;
		flex-direction: column;
		flex-wrap: wrap;
		margin-right: 12px;
	}

	.bg-done-index {
		background: rgba(255, 255, 255, 0.08);
		color: #999999;
		border-radius: 6px;
		border: 1px solid #E8ECF7;
		font-size: 16px;
		display: flex;
		flex-direction: column;
		margin-right: 12px;
	}

	.text-type {
		background: #037EFF;
		border-radius: 4px;
		color: white;
		font-size: 12px;
		padding: 3px 5px;
		text-align: center;
		margin-right: 8px;
	}

	.bg-normal-type {
		background: #037EFF;
		border-radius: 0px 4px 4px 0px;
		margin-top: 6px;
		color: white;
		font-size: 12px;
		width: 30px;
		padding: 2px;
		text-align: center;
	}

	.bg-selected-type {
		background: #007DFF;
		border-radius: 0px 4px 4px 0px;
		margin-top: 6px;
		color: white;
		font-size: 12px;
		width: 30px;
		padding: 2px;
		text-align: center;
	}

	.bg-done-type {
		background: #BABDC6;
		border-radius: 0px 4px 4px 0px;
		margin-top: 6px;
		color: white;
		font-size: 12px;
		width: 30px;
		padding: 2px;
		text-align: center;
	}


	.text-lesson-name {
		white-space: normal;
		text-overflow: ellipsis;
		word-break: break-all;
		display: -webkit-box;
		/** 对象作为伸缩盒子模型显示 **/
		-webkit-box-orient: vertical;
		/** 设置或检索伸缩盒对象的子元素的排列方式 **/
		-webkit-line-clamp: 3;
		/** 显示的行数 **/
		overflow: hidden;
		/** 隐藏超出的内容 **/
		font-size: 14px;
		margin: 10px;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		padding: 10px 15px;

	}

	.flex-row-center {
		display: flex;
		flex-direction: row;
		align-items: center;
		// margin-left: 15px;
		// margin-bottom: 15px;
	}

	.bg-lesson-title {
		flex: 1;
		overflow: hidden;
	}

	.text-lesson-title {
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
		font-size: 18px;
		color: #303133;
		font-weight: bold;
		margin-right: 15px;
	}

	.text-lesson-subtitle {
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
		font-size: 14px;
		color: #606266;
		margin-right: 15px;
	}

	.text-details {
		color: #303133;
		font-size: 14px;

	}

	.text-grey {
		font-size: 12px;
		color: #909199;

	}

	.bg-lesson-type {
		border: 1px solid #909199;
		border-radius: 3px;
		font-size: 12px;
		color: #909199;
		padding: 1px;
		flex-shrink: 0;
		height: 18px;
		text-align: center;

	}

	.bg-exercises {
		position: relative;
		background-color: white;
		border-radius: 5px;
		box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1);
		margin: 15px;
		padding: 12px 15px 12px 40px;
		display: flex;
		flex-direction: row;
		align-items: center;

	}

	.text-grey-content {
		max-width: 165px;
		color: #909199;
		font-size: 12px;
		margin-right: 10px;
	}

	.text-primary-content {
		color: #007DFF;
		font-size: 12px;

	}


	.text-ex-title {
		min-width: 220px;
		color: #303133;
		font-size: 16px;

	}

	.silk-ribbon2 {
		display: inline-block;
		width: 22px;
		padding: 3px 0px;
		background: #FF9B19;
		top: -5px;
		left: 12px;
		position: absolute;
		text-align: center;
		border-top-left-radius: 3px;
		color: white;
		font-size: 12px;
		line-height: 14px;
	}

	.btn-blue {
		background: #E5F2FF;
		border-radius: 10px;
		color: #007DFF;
		font-size: 14px;
		height: 20px;
		flex-shrink: 0;
		padding: 5px 9px;
		text-align: center;
		vertical-align: middle;
		// margin-left: 15px;
	}

	.silk-ribbon2:before {
		height: 0;
		width: 0;
		border-bottom: 5px solid #8D5A20;
		border-right: 5px solid transparent;
		right: -5px;
		top: 0;
	}

	.silk-ribbon2:before,
	.silk-ribbon2:after {
		content: "";
		position: absolute;
	}

	.silk-ribbon2:after {
		height: 0;
		width: 0;
		border-left: 11px solid #FF9B19;
		border-right: 11px solid #FF9B19;
		border-bottom: 10px solid transparent;
		bottom: -10px;
		left: 0;
	}



	.btn-study {

		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		color: #fff;
		border-radius: 10px;
		font-size: 16px;
		padding: 10px 15px;
		box-shadow: 0px 2px 5px 0px rgba(22, 99, 255, 0.1);
		background: -webkit-linear-gradient(to left, #007DFF, #5EC4F9);
		background: linear-gradient(to left, #007DFF, #5EC4F9);

	}

	.top {
		background-color: rgba(253, 246, 236, 0.7);
		border-radius: 5px;
		padding: 5px 15px;
		margin-left: 15px;
		margin-top: 15px;
		margin-right: 15px;
		margin-bottom: 5px;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		font-size: 12px;
		color: rgb(249, 174, 61);

		/* #ifdef APP-PLUS */
		margin-top: 230px;
		/* #endif */
		/* #ifdef MP-WEIXIN */
		margin-top: 230px;
		/* #endif */
		/* #ifdef H5*/
		margin-top: 250px;
		/* #endif */
	}

	.btn {
		background-color: rgb(249, 174, 61);
		font-size: 12px;
		color: white;
		margin-left: 30px;
		border-radius: 10px;
		flex-shrink: 0;
		padding: 3px 5px;
	}

	.img-blur {
		filter: blur(2px) brightness(90%);
		width: 100%;
		height: 225px;
	}

	.btn-share {
		background-image: url('../../static/common/icon_share.png');
		background-color: white;
		background-repeat: no-repeat;
		background-position: center;
		background-size: 100% 100%;
		height: 30px;
		width: 30px;


	}

	.btn-share::after {
		border: 0;
	}

	.bottom {
		position: fixed;
		width: 100%;
		bottom: 0;
		background-color: rgba(253, 246, 236, 0.7);
		display: flex;
		border-top-left-radius: 10px;
		border-top-right-radius: 10px;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		font-size: 14px;
		padding-top: 15px;
		padding-bottom: 15px;
		color: rgb(249, 174, 61);
	}

	.text-tag-bg {
		background-color: rgba(249, 174, 61, 0.1);
		color: #f9ae3d;
		font-size: 12px;
		padding: 3rpx 20rpx;
		border-radius: 10rpx;
	}

	.divier-line {
		background: -webkit-linear-gradient(to left, #007DFF, #5EC4F9);
		background: linear-gradient(to left, #007DFF, #5EC4F9);
		width: 5px;
		height: 12px;
		display: inline-block;
	}

	.text-num {
		position: absolute;
		left: 0;
		top: 0;
		width: 150px;
		height: 100px;
		text-align: center;
		line-height: 100px;
		z-index: -1;
		font-size: 56px;
		color: #E8ECF7;
		font-weight: bold;
		text-shadow: 2px 2px 2px #E8ECF7;
	}

	.fill-last {
		height: 0;
		width: 100%;
		background-color: #007DFF;
		background: none;
	}

	.main-item {
		box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.1);
		min-height: 1px;
	}

	.office-scroll {}


	.transform-90 {
		width: 100vh;
		height: 100vw;
		-webkit-transform: rotate(90deg);
		-webkit-transform-origin: 50vw 50vw;
		transform: rotate(90deg);
		transform-origin: 50vw 50vw;

	}


	.icon-land {
		opacity: 0.5;
		width: 35px;
		height: 35px;
		margin-bottom: 10px;
	}

	.bg-back {
		width: 30px;
		height: 25px;
		border-radius: 2px;
		display: flex;
		justify-content: center;
		align-items: center;
		background-color: rgba(0, 0, 0, 0.2);
		padding: 5px 5px;
		position: absolute;
		top: 0;
		left: 0;
		margin: 45px 25px 0px 20px;
		z-index: 1;

	}
</style>
