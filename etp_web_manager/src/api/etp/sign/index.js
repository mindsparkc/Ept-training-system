import request from '@/utils/request'

// 查询sign列表
export function listSign(query) {
  return request({
    url: '/etp/signWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询sign详细
export function getSign(id) {
  return request({
    url: '/etp/signWithCp/' + id,
    method: 'get'
  })
}

// 查询sign详细
export function exportByStudent(id) {
  return request({
    url: '/etp/signWithCp/exportByStudent/' + id,
    method: 'get'
  })
}

// 新增sign
export function addSign(data) {
  return request({
    url: '/etp/signWithCp',
    method: 'post',
    data: data
  })
}

// 修改sign
export function updateSign(data) {
  return request({
    url: '/etp/signWithCp',
    method: 'put',
    data: data
  })
}

// 删除sign
export function delSign(id) {
  return request({
    url: '/etp/signWithCp/' + id,
    method: 'delete'
  })
}

// 导出sign
export function exportSign(query) {
  return request({
    url: '/etp/signWithCp/export',
    method: 'get',
    params: query
  })
}
