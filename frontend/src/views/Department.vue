<template>
  <div class="department-page">
    <el-card shadow="hover">
      <div class="toolbar">
        <span class="page-label">部门列表</span>
        <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>添加部门</el-button>
      </div>
      <el-row :gutter="16" style="margin-top:16px">
        <el-col :span="8" v-for="dept in departmentList" :key="dept.id">
          <el-card shadow="hover" class="dept-card">
            <div class="dept-card-header">
              <div class="dept-icon">
                <el-icon :size="24"><OfficeBuilding /></el-icon>
              </div>
              <div class="dept-info">
                <h3 class="dept-name">{{ dept.name }}</h3>
                <p class="dept-manager">负责人：{{ dept.manager || '暂无' }}</p>
              </div>
              <el-dropdown trigger="click">
                <el-button link><el-icon><MoreFilled /></el-icon></el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleEdit(dept)"><el-icon><Edit /></el-icon> 编辑</el-dropdown-item>
                    <el-dropdown-item @click="handleDelete(dept)"><el-icon><Delete /></el-icon> 删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <p class="dept-desc">{{ dept.description || '暂无描述' }}</p>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-if="!departmentList.length" description="暂无部门数据" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="450px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.manager" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入部门描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, OfficeBuilding, MoreFilled, Edit, Delete } from '@element-plus/icons-vue'
import { getDepartmentList, addDepartment, updateDepartment, deleteDepartment } from '../api/department'

const loading = ref(false)
const submitLoading = ref(false)
const departmentList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

const form = reactive({ id: null, name: '', manager: '', description: '' })
const formRules = { name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }] }

const resetForm = () => { Object.assign(form, { id: null, name: '', manager: '', description: '' }) }

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDepartmentList()
    departmentList.value = res.data || []
  } finally { loading.value = false }
}

const handleAdd = () => { resetForm(); dialogTitle.value = '添加部门'; dialogVisible.value = true }
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    name: row.name,
    manager: row.manager,
    description: row.description
  })
  dialogTitle.value = '编辑部门'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除部门"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    await deleteDepartment(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (form.id) { await updateDepartment(form); ElMessage.success('更新成功') }
    else { await addDepartment(form); ElMessage.success('添加成功') }
    dialogVisible.value = false
    loadData()
  } finally { submitLoading.value = false }
}

onMounted(() => { loadData() })
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; }
.page-label { font-size: 16px; font-weight: 600; color: #333; }
.dept-card { margin-bottom: 16px; transition: all 0.3s; }
.dept-card:hover { transform: translateY(-2px); }
.dept-card-header { display: flex; align-items: center; gap: 12px; }
.dept-icon {
  width: 48px; height: 48px; border-radius: 12px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex; align-items: center; justify-content: center; color: #fff;
  flex-shrink: 0;
}
.dept-info { flex: 1; }
.dept-name { font-size: 16px; font-weight: 600; color: #333; margin-bottom: 2px; }
.dept-manager { font-size: 13px; color: #8c8c8c; }
.dept-desc { margin-top: 12px; font-size: 13px; color: #666; line-height: 1.6; }
</style>
