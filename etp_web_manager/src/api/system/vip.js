import request from '@/utils/request'

// 查询租户vip资源控制列表
export function listVip(query) {
  return request({
    url: '/system/vip/list',
    method: 'get',
    params: query
  })
}

// 查询租户vip资源控制详细
export function getVip(id) {
  return request({
    url: '/system/vip/' + id,
    method: 'get'
  })
}

// 新增租户vip资源控制
export function addVip(data) {
  return request({
    url: '/system/vip',
    method: 'post',
    data: data
  })
}

// 修改租户vip资源控制
export function updateVip(data) {
  return request({
    url: '/system/vip',
    method: 'put',
    data: data
  })
}

// 删除租户vip资源控制
export function delVip(id) {
  return request({
    url: '/system/vip/' + id,
    method: 'delete'
  })
}

// 导出租户vip资源控制
export function exportVip(query) {
  return request({
    url: '/system/vip/export',
    method: 'get',
    params: query
  })
}
