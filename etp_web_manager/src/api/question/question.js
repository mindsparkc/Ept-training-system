import request from '@/utils/request'

// 查询题目库列表
export function listQuestion(query) {
  return request({
    url: '/admin/v1/question/list',
    method: 'get',
    params: query
  })
}

// 查询题目库详细
export function getQuestion(id) {
  return request({
    url: '/admin/v1/question/' + id,
    method: 'get'
  })
}

// 新增题目库
export function addQuestion(data) {
  return request({
    url: '/admin/v1/question',
    method: 'post',
    data: data
  })
}

// 修改题目库
export function updateQuestion(data) {
  return request({
    url: '/admin/v1/question',
    method: 'put',
    data: data
  })
}

// 删除题目库
export function delQuestion(id) {
  return request({
    url: '/admin/v1/question/' + id,
    method: 'delete'
  })
}

// 导出题目库
export function exportQuestion(query) {
  return request({
    url: '/admin/v1/question/export',
    method: 'get',
    params: query
  })
}

// 查询分类树列表
export function categoryTreeselect(query) {
  return request({
    url: '/admin/v1/question/category/treeselect',
    method: 'get',
    params: query
  })
}

//题目难易程度
export function questionFact(query){
  return request({
      url: '/admin/v1/question/fact/level',
      method: 'get',
      params: query
    })
}
//随机获取题目个数
export function questionRandom(query){
  return request({
      url: '/admin/v1/question/random',
      method: 'get',
      params: query
    })
}

// 查询导入题目详细
export function getImportQuestion(recordId) {
  return request({
    url: '/admin/v1/question/importData/' + recordId,
    method: 'get'
  })
}

// 修改忽略导入题目状态
export function delImport(quId) {
  return request({
    url: '/admin/v1/question/importData/' + quId,
    method: 'delete'
  })
}

// 提交导入题目
export function submitImport(recordId) {
  return request({
    url: '/admin/v1/question/importData/submit/' + recordId,
    method: 'get'
  })
}

// 放弃导入题目
export function giveupImport(recordId) {
  return request({
    url: '/admin/v1/question/importData/giveup/' + recordId,
    method: 'get'
  })
}
