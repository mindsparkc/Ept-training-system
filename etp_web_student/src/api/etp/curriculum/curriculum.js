import request from '@/utils/request'

// 退回
export function pushBack(id){
  return request({
    url: `/etp/curriculum/back/${id}`,
    method: 'get',
  })
}

// 查询curriculum列表
export function listCurriculum(query) {
  return request({
    url: '/etp/curriculum/list',
    method: 'get',
    params: query
  })
}// 查询推荐curriculum列表
export function tjlistCurriculum(query) {
  return request({
    url: '/etp/curriculum/tjList',
    method: 'get',
    params: query
  })
}

// 查询知识库列表
export function getKnowledgeInfo(query) {
  return request({
    url: '/etp/curriculum/getKnowledgeInfo',
    method: 'get',
    params: query
  })
}

// 领取课程
export function receiveCurriculum(data){
  return request({
    url: '/etp/curriculum/receiveCurriculum',
    method: 'post',
    data: data
  })
}

// 完善课程
export function completeTCurriculum(data){
  return request({
    url: '/etp/curriculum/completeTCurriculum',
    method: 'post',
    data: data
  })
}

export function getTCurriculumContentDetail(id) {
  return request({
    url: `/etp/curriculum/getTCurriculumContentDetailByCid/${id}`,
    method: 'get'
  })
}

// 查询curriculum详细
export function getCurriculum(id) {
  return request({
    url: '/etp/curriculum/' + id,
    method: 'get'
  })
}

// 查询curriculum内容详细
export function getCurriculumContent(id) {
  return request({
    url: `/etp/curriculum/curriculumContent/${id}`,
    method: 'get'
  })
}

// 新增curriculum
export function addCurriculum(data) {
  return request({
    url: '/etp/curriculum',
    method: 'post',
    data: data
  })
}

// 修改curriculum
export function updateCurriculum(data) {
  return request({
    url: '/etp/curriculum',
    method: 'put',
    data: data
  })
}

// 删除curriculum
export function delCurriculum(id) {
  return request({
    url: '/etp/curriculum/' + id,
    method: 'delete'
  })
}
//修改课程学习状态
export function updateStatus(id,jd,contentIds){
  return request({
    url: '/etp/curriculum/updateStatus',
    method: 'post',
    data: {id:id,jd:jd,contentIds:contentIds}
  })
}

// 查询curriculum详细及进度
export function selectTCurriculumByIds(id) {
  return request({
    url: '/etp/curriculum/' + id,
    method: 'get'
  })
}
//根据课程id，查看进度 获取打开课程要展示的内容
export function showInitContent(id) {
  return request({
    url: '/etp/curriculum/showInitContent/' + id,
    method: 'get'
  })
}

// 查询习题答题记录详细
export function getCurriculumSubject(kcid,stid) {
  return request({
    url: '/etp/curriculum/getSubjectInfo/' + kcid+'/'+stid,
    method: 'get'
  })
}

// 新增习题答题记录
export function addCurriculumSubject(data) {
  return request({
    url: '/etp/curriculum/addSubject',
    method: 'post',
    data: data
  })
}

