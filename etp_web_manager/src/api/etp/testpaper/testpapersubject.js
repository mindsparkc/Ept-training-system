import request from '@/utils/request'

// 查询试卷关联题目列表
export function listTestpaperSubject(query) {
  return request({
    url: '/etp/testpaperSubjectWithCp/list',
    method: 'get',
    params: query
  })
}

// 查询试卷关联题目详细
export function getSubjectByPaper(sjid) {
  return request({
    url: '/etp/testpaperSubjectWithCp/getSubjectByPaper' ,
    method: 'get',
    params: {"sjid":sjid}
  })
}
// 查询试卷关联题目 标签
export function getPaperSubjectTags(sjid) {
  return request({
    url: '/etp/testpaperSubjectWithCp/getPaperSubjectTags' ,
    method: 'get',
    params: {"sjid":sjid}
  })
}

// 新增试卷关联题目
export function addTestpaperSubject(data) {
  return request({
    url: '/etp/testpaperSubjectWithCp',
    method: 'post',
    data: data
  })
}

export function savePaperSubject(data) {
  return request({
    url: '/etp/testpaperSubjectWithCp/savePaperSubject',
    method: 'post',
    data: data
  })
}

// 修改试卷关联题目
export function updateTestpaperSubject(data) {
  return request({
    url: '/etp/testpaperSubjectWithCp',
    method: 'put',
    data: data
  })
}

// 删除试卷关联题目
export function delTestpaperSubject(id) {
  return request({
    url: '/etp/testpaperSubjectWithCp/' + id,
    method: 'delete'
  })
}

// 导出试卷关联题目
export function exportTestpaperSubject(query) {
  return request({
    url: '/etp/testpaperSubjectWithCp/export',
    method: 'get',
    params: query
  })
}

export function updateScore(data){
  return request({
    url: '/etp/testpaperSubjectWithCp/updateScore',
    method: 'post',
    data: data
  })
}
export function selectScoreByType(sjid) {
  return request({
    url: '/etp/testpaperSubjectWithCp/selectScoreByType' ,
    method: 'get',
    params: {"sjid":sjid}
  })
}
