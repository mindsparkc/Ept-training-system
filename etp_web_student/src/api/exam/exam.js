import request from '@/utils/request'

// 查询考试列表
export function listExam(query) {
  return request({
    url: '/user/v1/exam/list',
    method: 'get',
    params: query
  })
}

// 查询考试详情
export function getExamInfo(id) {
  return request({
    url: '/user/v1/exam/' + id,
    method: 'get'
  })
}


// 开始考试接口
export function startExam(query) {
  return request({
    url: '/user/v1/exam/start',
    method: 'post',
    data: query
  })
}

// 提交考试
export function submitTheExam(data) {
  return request({
    url: '/user/v1/exam/submit',
    method: 'post',
    data: data
  })
}

//获取考试记录列表
export function getHistoryList(query) {
  return request({
    url: '/user/v1/exam/history',
    method: 'get',
    params: query
  })
}

//获取考试记录详情信息
export function getHistoryExam(id) {
    return request({
      url: '/user/v1/exam/record/'+ id,
      method: 'get'
    })
}

export function giveUpExam(recordId) {
  return request({
    url: '/user/v1/exam/giveup/'+ recordId,
    method: 'post'
  })
}

export function submitAnswer(recordId,data) {
  return request({
    url: '/user/v1/exam/submit/answer/'+ recordId,
    method: 'post',
    data:data
  })
}
