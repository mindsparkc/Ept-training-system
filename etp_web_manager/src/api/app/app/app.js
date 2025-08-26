import request from '@/utils/request'

// 查询app管理列表
export function listApp(query) {
  return request({
    url: '/app/app/list',
    method: 'get',
    params: query
  })
}

// 查询app管理详细
export function getApp(id) {
  return request({
    url: '/app/app/' + id,
    method: 'get'
  })
}

// 新增app管理
export function addApp(data) {
  return request({
    url: '/app/app',
    method: 'post',
    data: data
  })
}

// 修改app管理
export function updateApp(data) {
  return request({
    url: '/app/app',
    method: 'put',
    data: data
  })
}

// 删除app管理
export function delApp(id) {
  return request({
    url: '/app/app/' + id,
    method: 'delete'
  })
}

// 导出app管理
export function exportApp(query) {
  return request({
    url: '/app/app/export',
    method: 'get',
    params: query
  })
}

// 判断app名称是否存在
export function getAppName(appCode) {
  return request({
    url: '/app/app/selectAppCode/' + appCode,
    method: 'get'
  })
}

// 判断app名称是否存在
export function getVersionCode(id,versionCode) {
  return request({
    url: '/app/app/selectVersionCode/'+id +'&'+ versionCode,
    method: 'get'
  })
}
