import request from '@/utils/request'




// 查询最新轮播图
export function getNewBanners(query) {
  return request({
    url: '/system/bannerWithCp/list',
    method: 'get',
    params: query
  })
}


// 新增轮播图
export function addBanners(data) {
  return request({
    url: '/system/bannerWithCp',
    method: 'post',
    data: data
  })
}
