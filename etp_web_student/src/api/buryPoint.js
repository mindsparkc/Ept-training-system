import request from '@/utils/request'


//点击事件埋点
export function clickSendRedis(data) {
  return request({
    url: '/etp/redisStream/sendRedis',
    method: 'get',
    params: data
  })
}
//驻留事件埋点
export function stopSendRedis(data) {
  return request({
    url: '/etp/redisStream/sendRedisByIng',
    method: 'get',
    params: data
  })
}
