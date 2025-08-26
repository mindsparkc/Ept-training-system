import request from '@/utils/request'

// 查询考试列表
export function listExamination(query) {
  return request({
    url: '/etp/examinationWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询考试详细
export function getExamination(id) {
  return request({
    url: '/etp/examinationWithCp/' + id,
    method: 'get'
  })
}

// 查询缺考人员
export function getMissExam(id) {
  return request({
    url: '/etp/examinationWithCp/getMissExam/' + id,
    method: 'get'
  })
}

// 根据判卷状态查询考试详细
export function getExaminationInfo(id,pjzt) {
  return request({
    url: '/etp/examinationWithCp/info/' + id + '&' + pjzt,
    method: 'get'
  })
}

// 新增考试
export function addExamination(data) {
  return request({
    url: '/etp/examinationWithCp',
    method: 'post',
    data: data
  })
}

// 修改考试
export function updateExamination(data) {
  return request({
    url: '/etp/examinationWithCp',
    method: 'put',
    data: data
  })
}
export function updateTExaminationFb(data) {
  return request({
    url: '/etp/examinationWithCp/updateTExaminationFb',
    method: 'post',
    data: data
  })
}

// 删除考试
export function delExamination(id) {
  return request({
    url: '/etp/examinationWithCp/' + id,
    method: 'delete'
  })
}

// 导出考试
export function exportExamination(ksid) {
  return request({
    url: '/etp/examinationWithCp/exportCj/' + ksid,
    method: 'get'
  })
}

//获取考试信息
export function getHistoryTestPaper(id) {
  return request({
    url: '/etp/testpaperSubject/resultInquiry/'+ id,
    method: 'get'
  })
}

//获取试卷答题详情
export function selectAnswerInfo(id) {
  return request({
    url: '/etp/testpaperSubject/selectAnswerInfo?id='+id,
    method: 'get'
  })
}

// 修改评卷
export function updateDa(data) {
  return request({
    url: '/etp/testpaperSubjectWithCp/updateDa',
    method: 'post',
    data: data
  })
}
//考试取消发布
export function updateFb(data){
  return request({
    url:'/etp/examinationWithCp/updateTExaminationFb',
    method: 'post',
    data: data
  })
}

// 校验名称
export function checkExistKS(query) {
  return request({
    url: '/etp/examinationWithCp/checkExistKS' ,
    method: 'get',
    params: query
  })
}
