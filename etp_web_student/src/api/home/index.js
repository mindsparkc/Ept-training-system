import request from "@/utils/request";


export function kctjNum() {
  return request({
    url: '/etp/home/kctjNum',
    method: 'get',
  })
}
export function rwtjNum() {
  return request({
    url: '/etp/home/rwtjNum',
    method: 'get',
  })
}
export function kstjNum() {
  return request({
    url: '/etp/home/kstjNum',
    method: 'get',
  })
}

//查询轮播图
export function queryCarousel(query) {
  return request({
    url: '/etp/banner/list',
    method: 'get',
    params: query
  })
}

export function zstjNum() {
  return request({
    url: '/etp/home/zstjNum',
    method: 'get',
  })
}

export function lastStudy() {
  return request({
    url: '/etp/home/lastStudy',
    method: 'get',
  })
}

// 查询考试列表
export function comingEndExam(query) {
  return request({
    url: '/etp/home/comingEndExam',
    method: 'get',
    params: query
  })
}
