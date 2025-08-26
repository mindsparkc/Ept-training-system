import request from '@/utils/request'

// 查询考试列表
export function processingPersonnelAnalyzeData(ksids,xyids) {
  return request({
    url: '/etp/peopleAnalysisWithCp/getData/' + ksids + '/' + xyids,
    method: 'get',
  })
}
