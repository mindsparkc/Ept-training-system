import request from '@/utils/request'

// 查询错题集列表
export function listErrorbook(query) {
  return request({
    url: '/etp/errorbook/list',
    method: 'get',
    params: query
  })
}

// 查询错题集详细
export function getErrorbook(id) {
  return request({
    url: '/etp/errorbook/' + id,
    method: 'get'
  })
}

// 新增错题集
export function addErrorbook(data) {
  return request({
    url: '/etp/errorbook',
    method: 'post',
    data: data
  })
}

// 修改错题集
export function updateErrorbook(data) {
  return request({
    url: '/etp/errorbook',
    method: 'put',
    data: data
  })
}

// 删除错题集
export function delErrorbook(id) {
  return request({
    url: '/etp/errorbook/' + id,
    method: 'delete'
  })
}

// 导出错题集
export function exportErrorbook(query) {
  return request({
    url: '/etp/errorbook/export',
    method: 'get',
    params: query
  })
}
