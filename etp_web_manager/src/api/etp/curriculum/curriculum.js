import request from '@/utils/request'

// 退回
export function pushBack(id){
  return request({
    url: `/etp/curriculumWithCp/back/${id}`,
    method: 'get',
  })
}

// 查询curriculum列表
export function listCurriculum(query) {
  return request({
    url: '/etp/curriculumWithCp/list',
    method: 'get',
    params: query
  })
}

// 完善课程
export function completeTCurriculum(data){
  return request({
    url: '/etp/curriculumWithCp/completeTCurriculum',
    method: 'post',
    data: data
  })
}

export function getTCurriculumContentDetail(id) {
  return request({
    url: `/etp/curriculumWithCp/getTCurriculumContentDetailByCid/${id}`,
    method: 'get'
  })
}

// 查询curriculum详细
export function getCurriculum(id) {
  return request({
    url: '/etp/curriculumWithCp/' + id,
    method: 'get'
  })
}

// 查询curriculum内容详细
export function getCurriculumContent(id) {
  return request({
    url: `/etp/curriculumWithCp/curriculumContent/${id}`,
    method: 'get'
  })
}

// 新增curriculum
export function addCurriculum(data) {
  return request({
    url: '/etp/curriculumWithCp',
    method: 'post',
    data: data
  })
}

// 修改curriculum
export function updateCurriculum(data) {
  return request({
    url: '/etp/curriculumWithCp',
    method: 'put',
    data: data
  })
}
export function updateTjzt(data) {
  return request({
    url: '/etp/curriculumWithCp/updateTjzt',
    method: 'put',
    data: data
  })
}

// 删除curriculum
export function delCurriculum(id) {
  return request({
    url: '/etp/curriculumWithCp/' + id,
    method: 'delete'
  })
}
// 校验名称
export function checkExistKC(query) {
  return request({
    url: '/etp/curriculumWithCp/checkExistKC' ,
    method: 'get',
    params: query
  })
}


