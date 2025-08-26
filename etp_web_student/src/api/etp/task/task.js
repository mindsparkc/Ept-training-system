import request from '@/utils/request'

// 查询任务列表
export function listTask(query) {
  return request({
    url: '/etp/task/list',
    method: 'get',
    params: query
  })
}

// 查询任务目录详细
export function getTaskInfo(id) {
  return request({
    url: '/etp/task/' + id,
    method: 'get'
  })
}
// 查询任务详细
export function getTaskInfoDetail(id) {
  return request({
    url: '/etp/task/task/' + id,
    method: 'get'
  })
}

// 根据id查询课件详细信息
export function getCourseware(id) {
  return request({
    url: '/etp/courseware/' + id,
    method: 'get'
  })
}

export function updateTaskStatus(rwid,kjid){
  return request({
    url: '/etp/task/updateTaskStatus',
    method: 'post',
    data: {rwid:rwid,kjid:kjid}
  })
}
