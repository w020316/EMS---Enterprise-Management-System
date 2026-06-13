import request from '../utils/request'

export function getEmployeeList(params) {
  return request.get('/api/employee/list', { params })
}

export function getEmployeeById(id) {
  return request.get(`/api/employee/${id}`)
}

export function addEmployee(data) {
  return request.post('/api/employee', data)
}

export function updateEmployee(data) {
  return request.put('/api/employee', data)
}

export function deleteEmployee(id) {
  return request.delete(`/api/employee/${id}`)
}

export function getEmployeeStats() {
  return request.get('/api/employee/stats')
}
