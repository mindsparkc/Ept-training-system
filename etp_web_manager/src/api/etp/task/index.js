
import request from '@/utils/request'

// 查询任务列表
export function listTask(query) {
  return request({
    url: '/etp/taskWithCp/list',
    method: 'get',
    params: query
  })
}

// 取消发布任务
export function pushBack(id){
  return request({
    url: `/etp/taskWithCp/back/${id}`,
    method: 'get',
  })
}

// 查询任务详细
export function getTask(id) {
  return request({
    url: '/etp/taskWithCp/' + id,
    method: 'get'
  })
}

// 查询任务详细
export function getTaskInfo(id) {
  return request({
    url: '/etp/task/' + id,
    method: 'get'
  })
}

// 分步骤完善任务
export function completeTask(data) {
  return request({
    url: '/etp/taskWithCp/completeTask',
    method: 'post',
    data: data
  })
}

// 新增任务
export function addTask(data) {
  return request({
    url: '/etp/taskWithCp',
    method: 'post',
    data: data
  })
}

// 修改任务
export function updateTask(data) {
  return request({
    url: '/etp/taskWithCp',
    method: 'put',
    data: data
  })
}

// 删除任务
export function delTask(id) {
  return request({
    url: '/etp/taskWithCp/' + id,
    method: 'delete'
  })
}

// 导出任务
export function exportTask(query) {
  return request({
    url: '/etp/taskWithCp/export',
    method: 'get',
    params: query
  })
}

// 校验名称
export function checkExistRW(query) {
  return request({
    url: '/etp/taskWithCp/checkExistRW' ,
    method: 'get',
    params: query
  })
}
