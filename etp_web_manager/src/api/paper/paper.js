import request from '@/utils/request'

// 查询试卷基础信息列表
export function listPaper(query) {
  return request({
    url: '/admin/v1/paper/list',
    method: 'get',
    params: query
  })
}

// 查询试卷基础信息详细
export function getPaper(paperId) {
  return request({
    url: '/admin/v1/paper/' + paperId,
    method: 'get'
  })
}

// 新增试卷基础信息
export function addPaper(data) {
  return request({
    url: '/admin/v1/paper',
    method: 'post',
    data: data
  })
}

// 修改试卷基础信息
export function updatePaper(data) {
  return request({
    url: '/admin/v1/paper',
    method: 'put',
    data: data
  })
}
// 修改试卷发布状态
export function updateStatus(data) {
  return request({
    url: '/admin/v1/paper/status',
    method: 'put',
    data: data
  })
}

// 删除试卷基础信息
export function delPaper(paperId) {
  return request({
    url: '/admin/v1/paper/' + paperId,
    method: 'delete'
  })
}

// 导出试卷基础信息
export function exportPaper(query) {
  return request({
    url: '/admin/v1/paper/export',
    method: 'get',
    params: query
  })
}

// 试卷预览详情
export function paperView(paperId) {
  return request({
    url: '/admin/v1/paper/preview/'+paperId,
    method: 'Get',

  })
}
