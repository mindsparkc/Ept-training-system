import request from '@/utils/request'

// 查询问卷调查列表
export function listQuestionnaire(query) {
  return request({
    url: '/etp/questionnaire/list',
    method: 'get',
    params: query
  })
}

// 查询问卷调查详细
export function getQuestionnaire(id) {
  return request({
    url: '/etp/questionnaire/' + id,
    method: 'get'
  })
}

// 新增问卷调查
export function addQuestionnaire(data) {
  return request({
    url: '/etp/questionnaire',
    method: 'post',
    data: data
  })
}

// 修改问卷调查
export function updateQuestionnaire(data) {
  return request({
    url: '/etp/questionnaire',
    method: 'put',
    data: data
  })
}

// 删除问卷调查
export function delQuestionnaire(id) {
  return request({
    url: '/etp/questionnaire/' + id,
    method: 'delete'
  })
}

// 导出问卷调查
export function exportQuestionnaire(query) {
  return request({
    url: '/etp/questionnaire/export',
    method: 'get',
    params: query
  })
}
