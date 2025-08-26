import request from '@/utils/request'

// 删除文件
export function delByUrl(url) {
  return request({
    url: `/common/delete`,
    method: 'post',
    params: url
  })
}
// 查询课件管理列表
export function listCourseware(query) {
  return request({
    url: '/etp/courseware/list',
    method: 'get',
    params: query
  })
}

// 查询课件管理详细
export function getCourseware(id) {
  return request({
    url: '/etp/courseware/' + id,
    method: 'get'
  })
}

// 新增课件管理
export function addCourseware(data) {
  return request({
    url: '/etp/courseware',
    method: 'post',
    data: data
  })
}

// 修改课件管理
export function updateCourseware(data) {
  return request({
    url: '/etp/courseware',
    method: 'put',
    data: data
  })
}

// 删除课件管理
export function delCourseware(id) {
  return request({
    url: '/etp/courseware/' + id,
    method: 'delete'
  })
}

// 导出课件管理
export function exportCourseware(query) {
  return request({
    url: '/etp/courseware/export',
    method: 'get',
    params: query
  })
}
