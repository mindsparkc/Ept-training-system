import request from '@/utils/request'

// 查询知识点分类树列表
export function listKnowledgeCategory(query) {
  return request({
    url: '/admin/v1/knowledge/knowledgeCategory/list',
    method: 'get',
    params: query
  })
}

// 查询知识点分类树详细
export function getKnowledgeCategory(catId) {
  return request({
    url: '/admin/v1/knowledge/knowledgeCategory/' + catId,
    method: 'get'
  })
}

// 新增知识点分类树
export function addKnowledgeCategory(data) {
  return request({
    url: '/admin/v1/knowledge/knowledgeCategory',
    method: 'post',
    data: data
  })
}

// 修改知识点分类树
export function updateKnowledgeCategory(data) {
  return request({
    url: '/admin/v1/knowledge/knowledgeCategory',
    method: 'put',
    data: data
  })
}

// 删除知识点分类树
export function delKnowledgeCategory(catId) {
  return request({
    url: '/admin/v1/knowledge/knowledgeCategory/' + catId,
    method: 'delete'
  })
}

// 导出知识点分类树
export function exportKnowledgeCategory(query) {
  return request({
    url: '/knowledge/knowledgeCategory/export',
    method: 'get',
    params: query
  })
}

// 查询下拉树结构
export function getTreeSelect() {
  return request({
    url: '/admin/v1/knowledge/knowledgeCategory/treeSelect',
    method: 'get'
  })
}

//校验分类名称是否存在
export function checkByName(query) {
  return request({
    url: '/admin/v1/knowledge/knowledgeCategory/checkName',
    method: 'get',
    params: query
  })
}
