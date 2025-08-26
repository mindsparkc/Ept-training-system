import request from '@/utils/request'

// 查询题库资源管理列表
export function listSubjectResource(query) {
  return request({
    url: '/etc/subjectResourceWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询题库资源管理详细
export function getSubjectResource(id) {
  return request({
    url: '/etc/subjectResourceWithCp/' + id,
    method: 'get'
  })
}

// 新增题库资源管理
export function addSubjectResource(data) {
  return request({
    url: '/etc/subjectResourceWithCp',
    method: 'post',
    data: data
  })
}

// 修改题库资源管理
export function updateSubjectResource(data) {
  return request({
    url: '/etc/subjectResourceWithCp',
    method: 'put',
    data: data
  })
}

// 删除题库资源管理
export function delSubjectResource(id) {
  return request({
    url: '/etc/subjectResourceWithCp/' + id,
    method: 'delete'
  })
}

// 导出题库资源管理
export function exportSubjectResource(query) {
  return request({
    url: '/etc/subjectResourceWithCp/export',
    method: 'get',
    params: query
  })
}
