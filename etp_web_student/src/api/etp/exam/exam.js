import request from '@/utils/request'

// 查询考试列表
export function listExam(query) {
  return request({
    // url: '/etp/examination/list',
    url: '/user/v1/exam/list',
    method: 'get',
    params: query
  })
}

// 查询考试详情
export function getHisInfo(id) {
  return request({
    url: '/etp/examination/hisInfo/' + id,
    method: 'get'
  })
}
//
export function getExaminationInfo(id) {
  return request({
    url: '/etp/examination/' + id,
    method: 'get'
  })
}

// 开始考试接口
export function getSubjectByPaper(query) {
  return request({
    url: '/etp/testpaperSubject/getSubjectByPaper',
    method: 'get',
    params: query
  })
}

// 提交考试
export function submitTheExam(data) {
  return request({
    url: '/etp/testpaperSubject/submitTheExam',
    method: 'post',
    data
  })
}

// 提交考试
export function resultInquiry(id) {
  return request({
    url: '/etp/testpaperSubject/resultInquiry/' + id,
    method: 'get'
  })
}

// 历史考试
export function getHistoryExam(query) {
  return request({
    url: '/etp/testpaperSubject/historyExam',
    method: 'get',
    params: query
  })
}

//获取历史考试信息
export function getHistoryTestPaper(id) {
    return request({
      url: '/etp/testpaperSubject/resultInquiry/'+ id,
      method: 'get'
    })
}

//获取历史试卷答题详情
export function selectAnswerInfo(id) {
    return request({
      url: '/etp/testpaperSubject/selectAnswerInfo?id='+id,
      method: 'get'
    })
}

//获取历史试卷答题详情
export function heartBeat(id) {
  return request({
    url: '/etp/examination/heartbeat/'+id,
    method: 'get'
  })
}
