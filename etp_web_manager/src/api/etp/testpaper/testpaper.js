import request from '@/utils/request'

// 查询试卷列表
export function listTestpaper(query) {
  return request({
    url: '/etp/testpaperWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询试卷详细
export function getTestpaper(id) {
  return request({
    url: '/etp/testpaperWithCp/' + id,
    method: 'get'
  })
}

// 新增试卷
export function addTestpaper(data) {
  return request({
    url: '/etp/testpaperWithCp',
    method: 'post',
    data: data
  })
}

// 修改试卷
export function updateTestpaper(data) {
  return request({
    url: '/etp/testpaperWithCp',
    method: 'put',
    data: data
  })
}

// 删除试卷
export function delTestpaper(id) {
  return request({
    url: '/etp/testpaperWithCp/' + id,
    method: 'delete'
  })
}

// 导出试卷
export function exportTestpaper(query) {
  return request({
    url: '/etp/testpaperWithCp/export',
    method: 'get',
    params: query
  })
}

export function pushBack(id){
  return request({
    url: `/etp/testpaperWithCp/back/${id}`,
    method: 'get',
  })
}
// 校验名称
export function checkExistSJ(query) {
  return request({
    url: '/etp/testpaperWithCp/checkExistSJ' ,
    method: 'get',
    params: query
  })
}
