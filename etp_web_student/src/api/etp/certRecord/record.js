import request from '@/utils/request'

// 查询电子证书颁发记录列表
export function listRecord(query) {
  return request({
    url: '/api/v1/record/certList',
    method: 'get',
    params: query
  })
}

