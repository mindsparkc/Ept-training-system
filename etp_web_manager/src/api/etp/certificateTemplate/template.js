import request from '@/utils/request'

// 查询电子证书模板列表
export function listTemplate(query) {
  return request({
    url: '/api/v1/certificate/list',
    method: 'get',
    params: query
  })
}

// 查询电子证书模板详细
export function getTemplate(tempId) {
  return request({
    url: '/api/v1/certificate/' + tempId,
    method: 'get'
  })
}

// 新增电子证书模板
export function addTemplate(data) {
  return request({
    url: '/api/v1/certificate',
    method: 'post',
    data: data
  })
}

// 修改电子证书模板
export function updateTemplate(data) {
  return request({
    url: '/api/v1/certificate',
    method: 'put',
    data: data
  })
}

// 删除电子证书模板
export function delTemplate(tempId) {
  return request({
    url: '/api/v1/certificate/' + tempId,
    method: 'delete'
  })
}

// 导出电子证书模板
export function exportTemplate(query) {
  return request({
    url: '/api/v1/certificate/export',
    method: 'get',
    params: query
  })
}
// 提交证书颁发
export function submitPublish(data) {
  return request({
    url: '/api/v1/certificate/submitPublish',
    method: 'post',
    data: data
  })
}
