<template>
  <div class="login-container">
    <div class="login-left">
      <div class="left-content">
        <div class="brand-area">
          <div class="brand-icon">
            <el-icon :size="48"><OfficeBuilding /></el-icon>
          </div>
          <h1 class="brand-title">EMS</h1>
          <p class="brand-subtitle">Enterprise Management System</p>
        </div>
        <div class="feature-list">
          <div class="feature-item">
            <el-icon :size="20"><Check /></el-icon>
            <span>高效的员工信息管理</span>
          </div>
          <div class="feature-item">
            <el-icon :size="20"><Check /></el-icon>
            <span>智能的部门组织架构</span>
          </div>
          <div class="feature-item">
            <el-icon :size="20"><Check /></el-icon>
            <span>安全的数据权限控制</span>
          </div>
          <div class="feature-item">
            <el-icon :size="20"><Check /></el-icon>
            <span>便捷的文件上传管理</span>
          </div>
        </div>
        <div class="left-footer">© 2026 EMS 企业员工管理系统</div>
      </div>
    </div>
    <div class="login-right">
      <div class="login-form-wrapper">
        <div class="form-header">
          <h2 class="form-title">欢迎回来</h2>
          <p class="form-subtitle">请登录您的账号</p>
        </div>
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form" size="large">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" :prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password @keyup.enter="handleLogin" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">登 录</el-button>
          </el-form-item>
        </el-form>
        <div class="form-footer">
          还没有账号？<router-link to="/register" class="link">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, OfficeBuilding, Check } from '@element-plus/icons-vue'
import { login } from '../api/auth'

const router = useRouter()
const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({ username: '', password: '' })
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await login(loginForm)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('user', JSON.stringify(res.data.user))
    ElMessage.success('登录成功')
    router.push('/')
  } catch (e) {} finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}
.login-left {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}
.login-left::before {
  content: '';
  position: absolute;
  width: 600px;
  height: 600px;
  background: rgba(255,255,255,0.05);
  border-radius: 50%;
  top: -200px;
  right: -100px;
}
.login-left::after {
  content: '';
  position: absolute;
  width: 400px;
  height: 400px;
  background: rgba(255,255,255,0.05);
  border-radius: 50%;
  bottom: -150px;
  left: -100px;
}
.left-content {
  position: relative;
  z-index: 1;
  color: #fff;
  padding: 60px;
  max-width: 500px;
}
.brand-area {
  margin-bottom: 50px;
}
.brand-icon {
  width: 80px;
  height: 80px;
  background: rgba(255,255,255,0.15);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
}
.brand-title {
  font-size: 42px;
  font-weight: 700;
  letter-spacing: 4px;
  margin-bottom: 8px;
}
.brand-subtitle {
  font-size: 16px;
  opacity: 0.8;
  letter-spacing: 2px;
}
.feature-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 60px;
}
.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 15px;
  opacity: 0.9;
  padding: 10px 16px;
  background: rgba(255,255,255,0.08);
  border-radius: 8px;
  backdrop-filter: blur(5px);
}
.left-footer {
  font-size: 13px;
  opacity: 0.6;
}
.login-right {
  width: 520px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
}
.login-form-wrapper {
  width: 380px;
  animation: fadeInUp 0.6s ease-out;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}
.form-header {
  margin-bottom: 36px;
}
.form-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 8px;
}
.form-subtitle {
  font-size: 15px;
  color: #8c8c8c;
}
.login-form :deep(.el-input__wrapper) {
  padding: 4px 16px;
  height: 48px;
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e0e0e0 inset;
  transition: all 0.3s;
}
.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409EFF inset;
}
.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409EFF inset, 0 0 0 3px rgba(64,158,255,0.1);
}
.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px !important;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s;
}
.login-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.4);
}
.form-footer {
  text-align: center;
  color: #8c8c8c;
  font-size: 14px;
  margin-top: 24px;
}
.link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}
.link:hover {
  color: #764ba2;
}
</style>
