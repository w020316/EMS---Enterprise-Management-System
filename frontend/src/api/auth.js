import request from '../utils/request'

export function login(data) {
  return request.post('/api/auth/login', data)
}

export function register(data) {
  return request.post('/api/auth/register', data)
}

export function getUserInfo() {
  return request.get('/api/auth/info')
}

export function updateProfile(data) {
  return request.put('/api/auth/profile', data)
}

export function changePassword(data) {
  return request.put('/api/auth/password', data)
}
