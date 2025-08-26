import request from '@/utils/request'

// 查询分类树列表
export function listCategory(query) {
  return request({
    url: '/etp/category/list',
    method: 'get',
    params: query
  })
}

// 查询分类树详细
export function getCategory(id) {
  return request({
    url: '/etp/category/' + id,
    method: 'get'
  })
}

// 新增分类树
export function addCategory(data) {
  return request({
    url: '/etp/category',
    method: 'post',
    data: data
  })
}

// 修改分类树
export function updateCategory(data) {
  return request({
    url: '/etp/category',
    method: 'put',
    data: data
  })
}

// 删除分类树
export function delCategory(id) {
  return request({
    url: '/etp/category/' + id,
    method: 'delete'
  })
}

// 导出分类树
export function exportCategory(query) {
  return request({
    url: '/etp/category/export',
    method: 'get',
    params: query
  })
}

// 查询下拉树结构
export function getTreeselect() {
  return request({
    url: '/etp/category/treeselect',
    method: 'get'
  })
}
