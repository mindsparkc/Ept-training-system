import request from '@/utils/request'

// 查询题库列表
export function listSubject(query) {
  return request({
    url: '/etp/subject/list',
    method: 'get',
    params: query
  })
}

// 查询题库详细
export function getSubject(id) {
  return request({
    url: '/etp/subject/' + id,
    method: 'get'
  })
}

// 新增题库
export function addSubject(data) {
  return request({
    url: '/etp/subject',
    method: 'post',
    data: data
  })
}

// 修改题库
export function updateSubject(data) {
  return request({
    url: '/etp/subject',
    method: 'put',
    data: data
  })
}

// 删除题库
export function delSubject(id) {
  return request({
    url: '/etp/subject/' + id,
    method: 'delete'
  })
}

// 导出题库
export function exportSubject(query) {
  return request({
    url: '/etp/subject/export',
    method: 'get',
    params: query
  })
}
// 验证xx
export function checkXx(rule, value, callback) {
  for(var i in value){

  }
  // var pattern = /^1[3-9]\d{9}$/
  // if (pattern.test(value)) {
  //   return callback()
  // }
  return callback(new Error('输入的手机号错误'))
}

export function getSubjectRand(query) {
  return request({
    url: '/etp/subject/getSubjectRand',
    method: 'get',
    params: query
  })
}
