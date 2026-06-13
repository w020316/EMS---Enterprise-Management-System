<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo" :class="{ 'logo-collapse': isCollapse }">
        <div class="logo-icon">
          <el-icon :size="24"><OfficeBuilding /></el-icon>
        </div>
        <transition name="fade">
          <span v-if="!isCollapse" class="logo-text">EMS</span>
        </transition>
      </div>
      <el-menu
        :default-active="route.path"
        router
        class="aside-menu"
        :collapse="isCollapse"
        background-color="transparent"
        text-color="rgba(255,255,255,0.65)"
        active-text-color="#fff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <template #title>首页概览</template>
        </el-menu-item>
        <el-menu-item index="/employee">
          <el-icon><User /></el-icon>
          <template #title>员工管理</template>
        </el-menu-item>
        <el-menu-item index="/department">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title>部门管理</template>
        </el-menu-item>
        <el-menu-item index="/profile">
          <el-icon><Setting /></el-icon>
          <template #title>个人中心</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse" :size="20">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="route.meta.title">{{ route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="32" :src="user.avatar || undefined" style="background:linear-gradient(135deg,#667eea,#764ba2);font-size:14px">
                {{ user.username ? user.username.charAt(0).toUpperCase() : 'U' }}
              </el-avatar>
              <span class="username">{{ user.username || '用户' }}</span>
              <el-icon :size="12"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon> 个人中心
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="slide-fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { HomeFilled, User, OfficeBuilding, Setting, ArrowDown, Fold, Expand, SwitchButton } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)

const user = computed(() => {
  try { return JSON.parse(localStorage.getItem('user') || '{}') }
  catch { return {} }
})

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(() => {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      router.push('/login')
    }).catch(() => {})
  } else if (command === 'profile') {
    router.push('/profile')
  }
}
</script>

<style scoped>
.layout-container { height: 100vh; }
.layout-aside {
  background: #001529;
  overflow: hidden;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.logo {
  height: 56px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  gap: 12px;
  border-bottom: 1px solid rgba(255,255,255,0.08);
  overflow: hidden;
  white-space: nowrap;
}
.logo-collapse { padding: 0; justify-content: center; }
.logo-icon {
  min-width: 32px; height: 32px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  color: #fff;
}
.logo-text {
  font-size: 20px; font-weight: 700; color: #fff; letter-spacing: 3px;
}
.aside-menu {
  border-right: none;
  padding: 8px;
}
.aside-menu .el-menu-item {
  margin: 4px 0;
  border-radius: 8px;
  height: 44px;
  line-height: 44px;
}
.aside-menu .el-menu-item:hover {
  background: rgba(255,255,255,0.06) !important;
}
.aside-menu .el-menu-item.is-active {
  background: linear-gradient(135deg, #667eea, #764ba2) !important;
  color: #fff !important;
}
.layout-header {
  height: 56px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0,21,41,0.08);
  z-index: 10;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.collapse-btn {
  cursor: pointer;
  color: #333;
  transition: color 0.3s;
}
.collapse-btn:hover { color: #409EFF; }
.header-right { display: flex; align-items: center; }
.user-info {
  display: flex; align-items: center; gap: 8px;
  cursor: pointer; padding: 4px 8px; border-radius: 8px;
  transition: background 0.3s;
}
.user-info:hover { background: #f5f5f5; }
.username { font-size: 14px; color: #333; font-weight: 500; }
.layout-main {
  background: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
