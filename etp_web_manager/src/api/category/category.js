import request from '@/utils/request'

// 查询分类树详细
export function getCategory(id) {
  return request({
    url: '/admin/v1/question/category/' + id,
    method: 'get'
  })
}

// 新增分类树
export function addCategory(data) {
  return request({
    url: '/admin/v1/question/category',
    method: 'post',
    data: data
  })
}

// 修改分类树
export function updateCategory(data) {
  return request({
    url: '/admin/v1/question/category',
    method: 'put',
    data: data
  })
}

// 删除分类树
export function delCategory(id) {
  return request({
    url: '/admin/v1/question/category/' + id,
    method: 'delete'
  })
}

// 查询下拉树结构
export function getTreeselect() {
  return request({
    url: '/admin/v1/question/category/treeselect',
    method: 'get'
  })
}


//====================试卷分类============================
// 查询分类树详细
export function getPaperCategory(id) {
  return request({
    url: '/admin/v1/paper/category/' + id,
    method: 'get'
  })
}

// 新增分类树
export function addPaperCategory(data) {
  return request({
    url: '/admin/v1/paper/category',
    method: 'post',
    data: data
  })
}

// 修改分类树
export function updatePaperCategory(data) {
  return request({
    url: '/admin/v1/paper/category',
    method: 'put',
    data: data
  })
}

// 删除分类树
export function delPaperCategory(id) {
  return request({
    url: '/admin/v1/paper/category/' + id,
    method: 'delete'
  })
}

// 查询试卷下拉树结构
export function getPaperTreeselect() {
  return request({
    url: '/admin/v1/paper/category/treeselect',
    method: 'get'
  })
}

//================================================================
// 查询分类树详细
export function getExamCategory(id) {
  return request({
    url: '/admin/v1/exam/category/' + id,
    method: 'get'
  })
}

// 新增分类树
export function addExamCategory(data) {
  return request({
    url: '/admin/v1/exam/category',
    method: 'post',
    data: data
  })
}

// 修改分类树
export function updateExamCategory(data) {
  return request({
    url: '/admin/v1/exam/category',
    method: 'put',
    data: data
  })
}

// 查询下拉树结构
export function getExamTreeselect() {
  return request({
    url: '/admin/v1/exam/category/treeselect',
    method: 'get'
  })
}

// 删除分类树
export function delExamCategory(id) {
  return request({
    url: '/admin/v1/exam/category/' + id,
    method: 'delete'
  })
}

//=====================课件分类==================================

// 查询分类树详细
export function getCoursewareCategory(id) {
  return request({
    url: '/admin/v1/courseware/category/' + id,
    method: 'get'
  })
}

// 新增分类树
export function addCoursewareCategory(data) {
  return request({
    url: '/admin/v1/courseware/category',
    method: 'post',
    data: data
  })
}

// 修改分类树
export function updateCoursewareCategory(data) {
  return request({
    url: '/admin/v1/courseware/category',
    method: 'put',
    data: data
  })
}

// 删除分类树
export function delCoursewareCategory(id) {
  return request({
    url: '/admin/v1/courseware/category/' + id,
    method: 'delete'
  })
}

// 查询下拉树结构
export function getCoursewareTreeselect() {
  return request({
    url: '/admin/v1/courseware/category/treeselect',
    method: 'get'
  })
}


//=====================课程分类==================================

// 查询分类树详细
export function getCurriculumCategory(id) {
  return request({
    url: '/admin/v1/curriculum/category/' + id,
    method: 'get'
  })
}

// 新增分类树
export function addCurriculumCategory(data) {
  return request({
    url: '/admin/v1/curriculum/category',
    method: 'post',
    data: data
  })
}

// 修改分类树
export function updateCurriculumCategory(data) {
  return request({
    url: '/admin/v1/curriculum/category',
    method: 'put',
    data: data
  })
}

// 删除分类树
export function delCurriculumCategory(id) {
  return request({
    url: '/admin/v1/curriculum/category/' + id,
    method: 'delete'
  })
}

// 查询下拉树结构
export function getCurriculumTreeselect() {
  return request({
    url: '/admin/v1/curriculum/category/treeselect',
    method: 'get'
  })
}



//=====================习题集分类==================================

// 查询分类树详细
export function getTrainCategory(id) {
  return request({
    url: '/admin/v1/train/category/' + id,
    method: 'get'
  })
}

// 新增分类树
export function addTrainCategory(data) {
  return request({
    url: '/admin/v1/train/category',
    method: 'post',
    data: data
  })
}

// 修改分类树
export function updateTrainCategory(data) {
  return request({
    url: '/admin/v1/train/category',
    method: 'put',
    data: data
  })
}

// 删除分类树
export function delTrainCategory(id) {
  return request({
    url: '/admin/v1/train/category/' + id,
    method: 'delete'
  })
}

// 查询下拉树结构
export function getTrainTreeselect() {
  return request({
    url: '/admin/v1/train/category/treeselect',
    method: 'get'
  })
}
