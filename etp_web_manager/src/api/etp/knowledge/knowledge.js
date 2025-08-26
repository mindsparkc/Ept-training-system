import request from '@/utils/request'

// 查询知识点列表
export function listKnowledge(query) {
  return request({
    url: '/admin/v1/knowledge/knowledge/list',
    method: 'get',
    params: query
  })
}

// 查询知识点详细
export function getKnowledge(knoledgeId) {
  return request({
    url: '/admin/v1/knowledge/knowledge/' + knoledgeId,
    method: 'get'
  })
}

// 新增知识点
export function addKnowledge(data) {
  return request({
    url: '/admin/v1/knowledge/knowledge',
    method: 'post',
    data: data
  })
}

// 修改知识点
export function updateKnowledge(data) {
  return request({
    url: '/admin/v1/knowledge/knowledge',
    method: 'put',
    data: data
  })
}

// 删除知识点
export function delKnowledge(knoledgeId) {
  return request({
    url: '/admin/v1/knowledge/knowledge/' + knoledgeId,
    method: 'delete'
  })
}

// 导出知识点
export function exportKnowledge(query) {
  return request({
    url: '/knowledge/knowledge/export',
    method: 'get',
    params: query
  })
}

// 查询知识点详细
export function getChildList(parentId) {
  return request({
    url: '/admin/v1/knowledge/knowledge/getChildren/' + parentId,
    method: 'get'
  })
}
