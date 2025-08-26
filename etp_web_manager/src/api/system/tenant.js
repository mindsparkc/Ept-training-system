import request from "@/utils/request";

/**
 * 租户查询
 * @param query
 * @returns {*}
 */
export function listTenant(query) {
  return request({
    url: '/system/tenant/list',
    method: 'get',
    params: query
  })
}

// 查询租户详细
export function getTenant(tenantId) {
  return request({
    url: '/system/tenant/' + tenantId,
    method: 'get'
  })
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: '/system/tenant/addCustomer',
    method: 'post',
    data: data
  })
}

// 修改客户
export function updateCustomer(data) {
  return request({
    url: '/system/tenant/updateCustomer',
    method: 'post',
    data: data
  })
}

// 删除客户
export function delCustomer(tenantId) {
  return request({
    url: '/system/tenant/deleteCustomer/' + tenantId,
    method: 'delete',
  })
}
