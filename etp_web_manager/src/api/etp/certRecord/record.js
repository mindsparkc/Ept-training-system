import request from '@/utils/request'

// 查询电子证书颁发记录列表
export function listRecord(query) {
  return request({
    url: '/api/v1/record/list',
    method: 'get',
    params: query
  })
}

// 查询电子证书颁发记录详细
export function getRecord(recordId) {
  return request({
    url: '/api/v1/record/' + recordId,
    method: 'get'
  })
}

// 新增电子证书颁发记录
export function addRecord(data) {
  return request({
    url: '/api/v1/record',
    method: 'post',
    data: data
  })
}

// 修改电子证书颁发记录
export function updateRecord(data) {
  return request({
    url: '/api/v1/record',
    method: 'put',
    data: data
  })
}

// 删除电子证书颁发记录
export function delRecord(recordId) {
  return request({
    url: '/api/v1/record/' + recordId,
    method: 'delete'
  })
}

// 导出电子证书颁发记录
export function exportRecord(query) {
  return request({
    url: '/api/v1/record/export',
    method: 'get',
    params: query
  })
}
