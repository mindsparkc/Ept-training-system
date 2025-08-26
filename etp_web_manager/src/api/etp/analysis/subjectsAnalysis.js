import request from '@/utils/request'


// 分析课程,考试列表
export function subjectAnalyzeData(sid) {
  return request({
    url: `/etp/subjectsAnalysisWithCp/coursesAndExams/${sid}`,
    method: 'get',
  })
}


