import request from '../utils/request'

export function getDepartmentList() {
  return request.get('/api/department/list')
}

export function getDepartmentById(id) {
  return request.get(`/api/department/${id}`)
}

export function addDepartment(data) {
  return request.post('/api/department', data)
}

export function updateDepartment(data) {
  return request.put('/api/department', data)
}

export function deleteDepartment(id) {
  return request.delete(`/api/department/${id}`)
}
