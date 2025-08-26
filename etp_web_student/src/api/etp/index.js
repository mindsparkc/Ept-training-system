import request from "@/utils/request";

//查询角标
export function queryCornerMark(query) {
  return request({
    url: '/etp/home/getTaskAndExamNum',
    method: 'get',
    params: query
  })
}

//查询轮播图
export function queryCarousel(query) {
  return request({
    url: '/etp/banner/list',
    method: 'get',
    params: query
  })
}

//获取首页问卷
export function getTheHomepageQuestionnaire(query) {
  return request({
    url: '/etp/home/getHomeQuestionnaire',
    method: 'get',
    params: query
  })
}

//获取首页活动报名
export function getHomepageEventRegistration(query) {
  return request({
    url: '/etp/home/getHomeActivite',
    method: 'get',
    params: query
  })
}

//获取首页推荐课程
export function getHomepageRecommendedCourses(query) {
  return request({
    url: '/etp/home/getHomeCurriculum',
    method: 'get',
    params: query
  })
}

//获取首页任务
export function getHomepageTask(query) {
  return request({
    url: '/etp/task/list',
    method: 'get',
    params: query
  })
}
//获取我的
export function getStudyObj(query) {
  return request({
    url: '/etp/home/studyLately',
    method: 'get',
    params: query
  })
}
// //获取我的考试
// export function getHomeExam(query) {
//   return request({
//     url: '/etp/examination/list',
//     method: 'get',
//     params: query
//   })
// }

//获取我的考试（新）
export function getHomeExam(query) {
  return request({
    url: '/user/v1/exam/list',
    method: 'get',
    params: query
  })
}
//热门课程top9
export function homeHotKc() {
  return request({
    url: '/etp/home/getHomeHotKc',
    method: 'get',
  })
}
export function categoryList() {
  return request({
    url: '/etp/category/treeselect',
    method: 'get',
  })
}
export function categoryShowData(query) {
  return request({
    url: '/etp/category/list',
    method: 'get',
    params: query
  })
}

export function getNewApp(query) {
  return request({
    url:'/app/appVersion/getAppByStudent',
    method: 'get',
    params: query
  })
}
