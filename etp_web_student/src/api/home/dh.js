import request from '@/utils/request'



// 获取用户详细信息
export function streamChat(query) {
  return request({
    url: '/system/deepseek/stream',
    method: 'get',
    params: {"message":query},
    timeout: 300000
  })
}

export function deleteRedisCache() {
  return request({
    url: '/system/deepseek/reset',
    method: 'post'
  })
}
