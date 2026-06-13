<template>
  <div class="profile-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="profile-card">
          <div class="profile-avatar">
            <el-upload class="avatar-uploader" action="" :show-file-list="false" :http-request="handleAvatarUpload" accept="image/*">
              <el-avatar :size="100" :src="userInfo.avatar || undefined" style="background:#667eea;font-size:40px">
                {{ userInfo.username ? userInfo.username.charAt(0).toUpperCase() : 'U' }}
              </el-avatar>
            </el-upload>
            <h3 class="profile-name">{{ userInfo.username }}</h3>
            <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'info'" size="small">
              {{ userInfo.role === 'admin' ? '管理员' : '普通用户' }}
            </el-tag>
          </div>
          <div class="profile-actions">
            <el-button type="warning" plain @click="showPasswordDialog = true">
              <el-icon><Lock /></el-icon> 修改密码
            </el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <span>个人信息</span>
          </template>
          <el-form ref="profileFormRef" :model="profileForm" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" disabled />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleSave">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="showPasswordDialog" title="修改密码" width="420px" destroy-on-close>
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="80px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" :loading="pwdLoading" @click="handleChangePassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Lock } from '@element-plus/icons-vue'
import { getUserInfo, updateProfile, changePassword } from '../api/auth'
import { uploadFile } from '../api/file'

const loading = ref(false)
const pwdLoading = ref(false)
const userInfo = ref({})
const profileFormRef = ref()
const passwordFormRef = ref()
const showPasswordDialog = ref(false)
const profileForm = reactive({ username: '', email: '', phone: '', avatar: '' })

const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    userInfo.value = res.data
    Object.assign(profileForm, res.data)
  } catch (e) {}
}

const handleSave = async () => {
  loading.value = true
  try {
    await updateProfile(profileForm)
    ElMessage.success('保存成功')
    const res = await getUserInfo()
    userInfo.value = res.data
    localStorage.setItem('user', JSON.stringify(res.data))
  } finally {
    loading.value = false
  }
}

const handleChangePassword = async () => {
  const valid = await passwordFormRef.value.validate().catch(() => false)
  if (!valid) return

  pwdLoading.value = true
  try {
    await changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    showPasswordDialog.value = false
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    router.push('/login')
  } finally {
    pwdLoading.value = false
  }
}

const handleAvatarUpload = async (options) => {
  try {
    const res = await uploadFile(options.file)
    profileForm.avatar = res.data
    await updateProfile({ avatar: res.data })
    ElMessage.success('头像更新成功')
    const infoRes = await getUserInfo()
    userInfo.value = infoRes.data
    localStorage.setItem('user', JSON.stringify(infoRes.data))
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

onMounted(() => { loadUserInfo() })
</script>

<style scoped>
.profile-card {
  text-align: center;
}
.profile-avatar {
  padding: 20px 0;
}
.avatar-uploader {
  cursor: pointer;
}
.profile-name {
  margin: 16px 0 8px;
  color: #333;
}
.profile-actions {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
</style>
