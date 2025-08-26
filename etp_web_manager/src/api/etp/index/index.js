import request from '@/utils/request'

/** 统计*/
export function getTotal(){
  return request({
    url: `/etp/indexWithCp/total`,
    method: 'get',
  })
}
/** 当前课程*/
export function getCurrentCourse(){
  return request({
    url: `/etp/indexWithCp/getCurrentCourse`,
    method: 'get',
  })
}
/** 当前考试*/
export function getCurrentTest(){
  return request({
    url: `/etp/indexWithCp/getCurrentTest`,
    method: 'get',
  })
}
/** 热门课件*/
export function getCurrentCourseware(){
  return request({
    url: `/etp/indexWithCp/getCurrentCourseware`,
    method: 'get',
  })
}
/** 活跃学员*/
export function getHotMembers(){
  return request({
    url: `/etp/indexWithCp/getHotMembers`,
    method: 'get',
  })
}

/** 通知公告*/
export function getNotices(){
  return request({
    url: `/etp/indexWithCp/getNotices`,
    method: 'get',
  })
}

/** 本企业学员待反馈意见*/
export function getOpinions(){
  return request({
    url: `/etp/indexWithCp/getOpinions`,
    method: 'get',
  })
}





