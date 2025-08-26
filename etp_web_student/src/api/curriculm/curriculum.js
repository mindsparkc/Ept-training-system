import request from '@/utils/request'

// 查询课程习题集信息
export function selectCTrainInfo(query) {
  return request({
    url: '/user/v1/curriculum/selectCTrainInfo',
    method: 'get',
    params: query
  })
}

// 新增课程习题集信息
export function insertCTrainInfo(data) {
  return request({
    url: '/user/v1/curriculum/insertCTrainInfo',
    method: 'post',
    data: data
  })
}

// 删除课程习题集信息
export function deleteCTrainInfo(data) {
  return request({
    url: '/user/v1/curriculum/deleteCTrainInfo',
    method: 'delete',
    data:data
  })
}
