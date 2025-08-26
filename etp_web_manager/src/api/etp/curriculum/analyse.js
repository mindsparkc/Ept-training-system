import request from '@/utils/request'

//课程完成总的用户数
export function allCompletedKcUser(id){
  return request({
    url: '/etp/curriculumWithCp/allCompletedKcUser',
    method: 'get',
    params:{ kcid:id }
  })
}
//近七日课程完成人数
export function completedKcUserByDate(id){
  return request({
    url: '/etp/curriculumWithCp/completedKcUserByDate',
    method: 'get',
    params:{ kcid:id }
  })
}
//课件总计已完成
export function completedCourse(id){
  return request({
    url: '/etp/curriculumWithCp/completedCourse',
    method: 'get',
    params:{ kcid:id }
  })
}
//已领取课程用户数
export function receivedKcUser(id){
  return request({
    url: '/etp/curriculumWithCp/receivedKcUser',
    method: 'get',
    params:{ kcid:id }
  })
}
//昨日学习课程用户数
export function yesterdayStudyKc(id){
  return request({
    url: '/etp/curriculumWithCp/yesterdayStudyKc',
    method: 'get',
    params:{ kcid:id }
  })
}
//课程总计包含课件数
export function getCourseNum(id){
  return request({
    url: '/etp/curriculumWithCp/getCourseNum',
    method: 'get',
    params:{ kcid:id }
  })
}
//课程各课件完成情况
export function completedKjByDate(query){
  return request({
    url: '/etp/curriculumWithCp/completedKjByDate',
    method: 'get',
    params: query
  })
}

//统计分析 -各课程课件完成情况
export function getCompletedKcKjDate(query){
  return request({
    url: '/etp/curriculumWithCp/getCompletedKcKjDate',
    method: 'get',
    params: query
  })
}
//已发布课程总数
export function getFbCurriculum(){
  return request({
    url: '/etp/curriculumWithCp/getFbCurriculum',
    method: 'get'
  })
}
//课程学习总时长
export function getKcLearnTime(kcid){
  return request({
    url: '/etp/curriculumWithCp/getKcLearnTime',
    method: 'get',
    params: {kcid:kcid}
  })
}
//课程每个课件的学习时长排序
export function getKcListByKjTime(kcid){
  return request({
    url: '/etp/curriculumWithCp/getKcListByKjTime',
    method: 'get',
    params: {kcid:kcid}
  })
}
export function getEveKcLearnTime(){
  return request({
    url: '/etp/curriculumWithCp/getEveKcLearnTime',
    method: 'get'
  })
}
