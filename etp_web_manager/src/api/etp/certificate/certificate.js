import request from '@/utils/request'

// 查询证书管理列表
export function listCertificate(query) {
  return request({
    url: '/etp/certificateWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询证书管理详细
export function getCertificate(id) {
  return request({
    url: '/etp/certificateWithCp/' + id,
    method: 'get'
  })
}

// 新增证书管理
export function addCertificate(data) {
  return request({
    url: '/etp/certificateWithCp',
    method: 'post',
    data: data
  })
}

// 修改证书管理
export function updateCertificate(data) {
  return request({
    url: '/etp/certificateWithCp',
    method: 'put',
    data: data
  })
}

// 删除证书管理
export function delCertificate(id) {
  return request({
    url: '/etp/certificateWithCp/' + id,
    method: 'delete'
  })
}

// 导出证书管理
export function exportCertificate(query) {
  return request({
    url: '/etp/certificateWithCp/export',
    method: 'get',
    params: query
  })
}
