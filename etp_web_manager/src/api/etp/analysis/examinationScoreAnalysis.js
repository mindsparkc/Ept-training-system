import request from '@/utils/request'

// 查询考试列表
export function listExamination(query) {
  return request({
    url: '/etp/examinationScoreAnalysisWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询考试分析详细
export function getExamination(id) {
  return request({
    url: '/etp/examinationScoreAnalysisWithCp/' + id,
    method: 'get'
  })
}

// 查询试卷题目信息
export function getSubjectInfo(sjid) {
  return request({
    url: '/etp/examinationScoreAnalysisWithCp/getSubjectInfo/' + sjid,
    method: 'get'
  })
}
//根据题目id获取题目分析
export function getSubjectAnalysis(id,ksid,lx) {
  return request({
    url: '/etp/examinationScoreAnalysisWithCp/getSubjectAnalysis/' + id + '/' + ksid + '/' + lx,
    method: 'get'
  })
}
