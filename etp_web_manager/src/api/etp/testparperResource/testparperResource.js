import request from '@/utils/request'

// 查询试卷资源管理列表
export function listTestparperResource(query) {
  return request({
    url: '/etc/testparperResourceWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询试卷资源管理详细
export function getTestparperResource(id) {
  return request({
    url: '/etc/testparperResourceWithCp/' + id,
    method: 'get'
  })
}

// 新增试卷资源管理
export function addTestparperResource(data) {
  return request({
    url: '/etc/testparperResourceWithCp',
    method: 'post',
    data: data
  })
}

// 修改试卷资源管理
export function updateTestparperResource(data) {
  return request({
    url: '/etc/testparperResourceWithCp',
    method: 'put',
    data: data
  })
}

// 删除试卷资源管理
export function delTestparperResource(id) {
  return request({
    url: '/etc/testparperResourceWithCp/' + id,
    method: 'delete'
  })
}

// 导出试卷资源管理
export function exportTestparperResource(query) {
  return request({
    url: '/etc/testparperResourceWithCp/export',
    method: 'get',
    params: query
  })
}
