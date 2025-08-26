import request from '@/utils/request'

// 查询课件资源管理列表
export function listCoursewareresource(query) {
  return request({
    url: '/etp/coursewareresource/list',
    method: 'get',
    params: query
  })
}

// 查询课件资源管理详细
export function getCoursewareresource(id) {
  return request({
    url: '/etp/coursewareresource/' + id,
    method: 'get'
  })
}

// 新增课件资源管理
export function addCoursewareresource(data) {
  return request({
    url: '/etp/coursewareresource',
    method: 'post',
    data: data
  })
}

// 修改课件资源管理
export function updateCoursewareresource(data) {
  return request({
    url: '/etp/coursewareresource',
    method: 'put',
    data: data
  })
}

// 删除课件资源管理
export function delCoursewareresource(id) {
  return request({
    url: '/etp/coursewareresource/' + id,
    method: 'delete'
  })
}

// 导出课件资源管理
export function exportCoursewareresource(query) {
  return request({
    url: '/etp/coursewareresource/export',
    method: 'get',
    params: query
  })
}