import request from '@/utils/request'

// 查询习题集详情
export function getTrainInfo(paperId) {
  return request({
    url: '/user/v1/train/' + paperId,
    method: 'get'
  })
}

