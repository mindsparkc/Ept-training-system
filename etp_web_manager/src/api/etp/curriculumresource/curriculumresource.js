import request from '@/utils/request'

// 查询课程资源管理列表
export function listCurriculumresource(query) {
  return request({
    url: '/curriculumresource/curriculumresource/list',
    method: 'get',
    params: query
  })
}

// 查询课程资源管理详细
export function getCurriculumresource(id) {
  return request({
    url: '/curriculumresource/curriculumresource/' + id,
    method: 'get'
  })
}

// 新增课程资源管理
export function addCurriculumresource(data) {
  return request({
    url: '/curriculumresource/curriculumresource',
    method: 'post',
    data: data
  })
}

// 修改课程资源管理
export function updateCurriculumresource(data) {
  return request({
    url: '/curriculumresource/curriculumresource',
    method: 'put',
    data: data
  })
}

// 删除课程资源管理
export function delCurriculumresource(id) {
  return request({
    url: '/curriculumresource/curriculumresource/' + id,
    method: 'delete'
  })
}

// 导出课程资源管理
export function exportCurriculumresource(query) {
  return request({
    url: '/curriculumresource/curriculumresource/export',
    method: 'get',
    params: query
  })
}