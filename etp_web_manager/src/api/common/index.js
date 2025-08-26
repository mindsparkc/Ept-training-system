import request from '@/utils/request'

// 批量获取用户
export function userList(data) {
  return request({
    url: '/system/user/list/'+data ,
    method: 'get'
  })
}

// 批量获取岗位
export function postList(data) {
  return request({
    url: '/system/post/list/'+data,
    method: 'get'
  })
}

