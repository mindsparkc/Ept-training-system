import request from '@/utils/request'

// 查询活动报名列表
export function listQuestionnaire(query) {
  return request({
    url: '/etp/questionnaireWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询活动报名详细
export function getQuestionnaire(id) {
  return request({
    url: '/etp/questionnaireWithCp/' + id,
    method: 'get'
  })
}

// 新增活动报名
export function addQuestionnaire(data) {
  return request({
    url: '/etp/questionnaireWithCp',
    method: 'post',
    data: data
  })
}

// 修改活动报名
export function updateQuestionnaire(data) {
  return request({
    url: '/etp/questionnaireWithCp',
    method: 'put',
    data: data
  })
}

// 删除活动报名
export function delQuestionnaire(id) {
  return request({
    url: '/etp/questionnaireWithCp/' + id,
    method: 'delete'
  })
}

// 导出活动报名
export function exportQuestionnaire(query) {
  return request({
    url: '/etp/questionnaireWithCp/export',
    method: 'get',
    params: query
  })
}

// 撤回问卷调查
export function backQuestionnaire(query) {
  return request({
    url: '/etp/questionnaireWithCp/back',
    method: 'get',
    params: query
  })
}

//*************统计分析************
export function totalByWjid(wjid){
  return request({
    url: '/etp/questionnaireWithCp/totalByWjid',
    method: 'get',
    params: { wjid:wjid }
  })
}

export function listQuestionnaireStudent(query) {
  return request({
    url: '/etp/questionnaireWithCp/studentList',
    method: 'get',
    params: query
  })
}
export function choseAnalysis(wjid) {
  return request({
    url: '/etp/questionnaireWithCp/choseAnalysis',
    method: 'get',
    params: { wjid:wjid }
  })
}
export function wdAnalysis(query) {
  return request({
    url: '/etp/questionnaireWithCp/wdAnalysis',
    method: 'get',
    params: query
  })
}
