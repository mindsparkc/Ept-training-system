import request from '@/utils/request'

// 查询意见反馈列表
export function listOpinion(query) {
  return request({
    url: '/etp/opinion/list',
    method: 'get',
    params: query
  })
}

// 查询意见反馈详细
export function getOpinion(id) {
  return request({
    url: '/etp/opinion/' + id,
    method: 'get'
  })
}

// 新增意见反馈
export function addOpinion(data) {
  return request({
    url: '/etp/opinion',
    method: 'post',
    data: data
  })
}

// 修改意见反馈
export function updateOpinion(data) {
  return request({
    url: '/etp/opinion',
    method: 'put',
    data: data
  })
}

// 删除意见反馈
export function delOpinion(id) {
  return request({
    url: '/etp/opinion/' + id,
    method: 'delete'
  })
}

// 导出意见反馈
export function exportOpinion(query) {
  return request({
    url: '/etp/opinion/export',
    method: 'get',
    params: query
  })
}