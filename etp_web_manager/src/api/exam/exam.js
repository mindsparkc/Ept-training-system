import request from '@/utils/request'
import da from 'element-ui/src/locale/lang/da'

// 查询考试信息列表
export function listExam(query) {
  return request({
    url: '/admin/v1/exam/list',
    method: 'get',
    params: query
  })
}

// 查询考试信息详细
export function getExam(examId) {
  return request({
    url: '/admin/v1/exam/' + examId,
    method: 'get'
  })
}

// 新增考试信息
export function addExam(data) {
  return request({
    url: '/admin/v1/exam',
    method: 'post',
    data: data
  })
}

// 修改考试信息
export function updateExam(data) {
  return request({
    url: '/admin/v1/exam',
    method: 'put',
    data: data
  })
}

// 删除考试信息
export function delExam(examId) {
  return request({
    url: '/admin/v1/exam/' + examId,
    method: 'delete'
  })
}

// 修改考试状态
export function updateStatus(data) {
  return request({
    url: '/admin/v1/exam/status',
    method: 'put',
    data: data
  })
}

// 校验考试名称是否存在
export function checkExistKS(name,examId) {
  return request({
    url: '/admin/v1/exam/checkName/' + name + '&' + examId,
    method: 'get'
  })
}

//查询学员考试成绩列表
export function selectStudentList(query) {
  return request({
    url: '/admin/v1/exam/record/list',
    method: 'get',
    params: query
  })
}

//获取学员考试成绩详细信息
export function getHistoryExam(id) {
  return request({
    url: '/admin/v1/exam/record/'+ id,
    method: 'get'
  })
}

//获取学员考试成绩详细信息
export function reviewSubmit(data) {
  return request({
    url: '/admin/v1/exam/review/submit',
    method: 'put',
    data: data
  })
}

