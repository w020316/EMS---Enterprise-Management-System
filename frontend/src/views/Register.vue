<template>
  <div class="register-container">
    <div class="register-left">
      <div class="left-content">
        <div class="brand-area">
          <div class="brand-icon">
            <el-icon :size="48"><OfficeBuilding /></el-icon>
          </div>
          <h1 class="brand-title">EMS</h1>
          <p class="brand-subtitle">Enterprise Management System</p>
        </div>
        <div class="info-text">
          <p>注册账号后即可使用以下功能：</p>
          <ul>
            <li>员工信息管理</li>
            <li>部门组织管理</li>
            <li>文件上传管理</li>
          </ul>
        </div>
        <div class="left-footer">© 2026 EMS 企业员工管理系统</div>
      </div>
    </div>
    <div class="register-right">
      <div class="register-form-wrapper">
        <div class="form-header">
          <h2 class="form-title">创建账号</h2>
          <p class="form-subtitle">请填写以下信息完成注册</p>
        </div>
        <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form" size="large">
          <el-form-item prop="username">
            <el-input v-model="registerForm.username" placeholder="请输入用户名" :prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码" :prefix-icon="Lock" show-password />
          </el-form-item>
          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item>
                <el-input v-model="registerForm.email" placeholder="邮箱（选填）" :prefix-icon="Message" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item>
                <el-input v-model="registerForm.phone" placeholder="手机号（选填）" :prefix-icon="Phone" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button type="primary" class="register-btn" :loading="loading" @click="handleRegister">注 册</el-button>
          </el-form-item>
        </el-form>
        <div class="form-footer">
          已有账号？<router-link to="/login" class="link">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, Phone, OfficeBuilding } from '@element-plus/icons-vue'
import { register } from '../api/auth'

const router = useRouter()
const registerFormRef = ref()
const loading = ref(false)

const registerForm = reactive({
  username: '', password: '', confirmPassword: '', email: '', phone: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) callback(new Error('两次输入的密码不一致'))
  else callback()
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await register({
      username: registerForm.username,
      password: registerForm.password,
      email: registerForm.email,
      phone: registerForm.phone
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {} finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}
.register-left {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}
.register-left::before {
  content: '';
  position: absolute;
  width: 600px;
  height: 600px;
  background: rgba(255,255,255,0.05);
  border-radius: 50%;
  top: -200px;
  right: -100px;
}
.left-content {
  position: relative;
  z-index: 1;
  color: #fff;
  padding: 60px;
  max-width: 500px;
}
.brand-area { margin-bottom: 40px; }
.brand-icon {
  width: 80px; height: 80px;
  background: rgba(255,255,255,0.15);
  border-radius: 20px;
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 24px;
}
.brand-title { font-size: 42px; font-weight: 700; letter-spacing: 4px; margin-bottom: 8px; }
.brand-subtitle { font-size: 16px; opacity: 0.8; letter-spacing: 2px; }
.info-text { margin-bottom: 60px; }
.info-text p { font-size: 16px; margin-bottom: 16px; opacity: 0.9; }
.info-text ul { list-style: none; padding: 0; }
.info-text li {
  padding: 8px 16px; margin-bottom: 8px;
  background: rgba(255,255,255,0.08);
  border-radius: 8px; font-size: 14px; opacity: 0.9;
}
.left-footer { font-size: 13px; opacity: 0.6; }
.register-right {
  width: 560px;
  display: flex; align-items: center; justify-content: center;
  background: #fff;
}
.register-form-wrapper {
  width: 440px;
  animation: fadeInUp 0.6s ease-out;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}
.form-header { margin-bottom: 32px; }
.form-title { font-size: 28px; font-weight: 700; color: #1a1a2e; margin-bottom: 8px; }
.form-subtitle { font-size: 15px; color: #8c8c8c; }
.register-form :deep(.el-input__wrapper) {
  padding: 4px 16px; height: 48px; border-radius: 10px;
  box-shadow: 0 0 0 1px #e0e0e0 inset; transition: all 0.3s;
}
.register-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409EFF inset;
}
.register-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409EFF inset, 0 0 0 3px rgba(64,158,255,0.1);
}
.register-btn {
  width: 100%; height: 48px; font-size: 16px; font-weight: 600;
  border-radius: 10px !important; letter-spacing: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none; transition: all 0.3s;
}
.register-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.4);
}
.form-footer { text-align: center; color: #8c8c8c; font-size: 14px; margin-top: 24px; }
.link { color: #667eea; text-decoration: none; font-weight: 500; }
.link:hover { color: #764ba2; }
</style>
