<template>
  <div class="dashboard" v-loading="loading">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card stat-blue" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">员工总数</p>
              <p class="stat-value">{{ stats.totalEmployees || 0 }}</p>
            </div>
            <div class="stat-icon-wrapper blue">
              <el-icon :size="32"><User /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-green" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">部门数量</p>
              <p class="stat-value">{{ stats.totalDepartments || 0 }}</p>
            </div>
            <div class="stat-icon-wrapper green">
              <el-icon :size="32"><OfficeBuilding /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-orange" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">在职员工</p>
              <p class="stat-value">{{ stats.activeEmployees || 0 }}</p>
            </div>
            <div class="stat-icon-wrapper orange">
              <el-icon :size="32"><Avatar /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-red" shadow="hover">
          <div class="stat-content">
            <div class="stat-info">
              <p class="stat-label">离职员工</p>
              <p class="stat-value">{{ (stats.totalEmployees || 0) - (stats.activeEmployees || 0) }}</p>
            </div>
            <div class="stat-icon-wrapper red">
              <el-icon :size="32"><Remove /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近员工</span>
              <el-button text type="primary" @click="$router.push('/employee')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentEmployees" stripe style="width:100%">
            <el-table-column prop="name" label="姓名" width="120">
              <template #default="{ row }">
                <div class="name-cell">
                  <el-avatar :size="28" :src="row.avatar || undefined" style="background:linear-gradient(135deg,#667eea,#764ba2);font-size:12px">
                    {{ row.name ? row.name.charAt(0) : '?' }}
                  </el-avatar>
                  <span>{{ row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="departmentName" label="部门" width="120" />
            <el-table-column prop="position" label="职位" width="120" />
            <el-table-column prop="phone" label="手机号" width="140" />
            <el-table-column prop="hireDate" label="入职日期" width="110" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                  {{ row.status === 1 ? '在职' : '离职' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="dept-card">
          <template #header>
            <span>部门人员分布</span>
          </template>
          <div class="dept-list">
            <div v-for="dept in deptStats" :key="dept.name" class="dept-item">
              <div class="dept-info">
                <span class="dept-name">{{ dept.name }}</span>
                <span class="dept-count">{{ dept.count }}人</span>
              </div>
              <el-progress :percentage="dept.percentage" :color="dept.color" :stroke-width="12" :show-text="false" />
            </div>
          </div>
        </el-card>

        <el-card shadow="hover" style="margin-top:20px">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/employee')" style="width:100%;margin-bottom:12px">
              <el-icon><Plus /></el-icon> 员工管理
            </el-button>
            <el-button type="success" @click="$router.push('/department')" style="width:100%">
              <el-icon><Plus /></el-icon> 部门管理
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, OfficeBuilding, Avatar, Remove, Plus } from '@element-plus/icons-vue'
import { getEmployeeList, getEmployeeStats } from '../api/employee'
import { getDepartmentList } from '../api/department'

const stats = ref({})
const recentEmployees = ref([])
const deptStats = ref([])
const loading = ref(false)

const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#00bcd4']

onMounted(async () => {
  loading.value = true
  try {
    const [empRes, deptRes, statsRes] = await Promise.all([
      getEmployeeList({ page: 1, size: 999 }),
      getDepartmentList(),
      getEmployeeStats()
    ])
    const employees = empRes.data.list || empRes.data || []
    const departments = deptRes.data || []

    recentEmployees.value = employees.slice(0, 6)
    stats.value = {
      totalEmployees: statsRes.data.totalEmployees || 0,
      totalDepartments: departments.length,
      activeEmployees: statsRes.data.activeEmployees || 0
    }

    // 计算部门人员分布
    const total = employees.length || 1
    deptStats.value = departments.map((d, i) => {
      const count = employees.filter(e => e.departmentId === d.id).length
      return {
        name: d.name,
        count,
        percentage: Math.round((count / total) * 100),
        color: colors[i % colors.length]
      }
    })
  } catch (e) {
    console.error('Dashboard load error:', e)
    ElMessage.error('数据加载失败，请刷新重试')
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.stat-label {
  color: #999;
  font-size: 14px;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #333;
}
.stat-icon-wrapper {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.stat-icon-wrapper.blue { background: rgba(64,158,255,0.1); color: #409EFF; }
.stat-icon-wrapper.green { background: rgba(103,194,58,0.1); color: #67C23A; }
.stat-icon-wrapper.orange { background: rgba(230,162,60,0.1); color: #E6A23C; }
.stat-icon-wrapper.red { background: rgba(245,108,108,0.1); color: #F56C6C; }

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dept-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.dept-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}
.dept-name {
  font-size: 14px;
  color: #333;
}
.dept-count {
  font-size: 14px;
  color: #666;
  font-weight: 600;
}

.quick-actions {
  display: flex;
  flex-direction: column;
}
.name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}
.stat-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
}
.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}
</style>
