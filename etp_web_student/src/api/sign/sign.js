import request from '@/utils/request'

// 查询sign列表
export function listSign(query) {
  return request({
    url: '/etp/sign/list',
    method: 'get',
    params: query
  })
}

// 查询sign详细
export function getSign(id) {
  return request({
    url: '/etp/sign/' + id,
    method: 'get'
  })
}

// 新增sign
export function addSign(data) {
  return request({
    url: '/etp/sign',
    method: 'post',
    data: data
  })
}

// 修改sign
export function updateSign(data) {
  return request({
    url: '/etp/sign',
    method: 'put',
    data: data
  })
}

// 删除sign
export function delSign(id) {
  return request({
    url: '/etp/sign/' + id,
    method: 'delete'
  })
}

// 导出sign
export function exportSign(query) {
  return request({
    url: '/etp/sign/export',
    method: 'get',
    params: query
  })
}

//活动报名、取消报名
export function studentSign(query) {
  return request({
    url: '/etp/sign/studentSign',
    method: 'post',
    params: query
  })
}
