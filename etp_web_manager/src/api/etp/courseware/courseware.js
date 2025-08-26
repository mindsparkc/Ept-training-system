import request from '@/utils/request'

// 删除文件
export function delByUrl(url) {
  return request({
    url: `/common/delete`,
    method: 'post',
    params: url
  })
}
// 查询课件管理列表
export function listCourseware(query) {
  return request({
    url: '/etp/coursewareWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询课件管理详细
export function getCourseware(id) {
  return request({
    url: '/etp/coursewareWithCp/' + id,
    method: 'get'
  })
}

// 新增课件管理
export function addCourseware(data) {
  return request({
    url: '/etp/coursewareWithCp',
    method: 'post',
    data: data
  })
}

// 修改课件管理
export function updateCourseware(data) {
  return request({
    url: '/etp/coursewareWithCp',
    method: 'put',
    data: data
  })
}

// 删除课件管理
export function delCourseware(id) {
  return request({
    url: '/etp/coursewareWithCp/' + id,
    method: 'delete'
  })
}

// 导出课件管理
export function exportCourseware(query) {
  return request({
    url: '/etp/coursewareWithCp/export',
    method: 'get',
    params: query
  })
}

export function selectedKjByKc(id) {
  return request({
    url: '/etp/coursewareWithCp/selectedKjByKc',
    method: 'get',
    params: {kcid:id}
  })
}
// 校验名称
export function checkExistKJ(query) {
  return request({
    url: '/etp/coursewareWithCp/checkExistKJ' ,
    method: 'get',
    params: query
  })
}

//获取课程中的习题集
export function selectedXtjByKc(id) {
  return request({
    url: '/admin/v1/paper/selectedXtjByKc',
    method: 'get',
    params: {kcid:id}
  })
}
