<template>
  <div class="employee-page">
    <el-card shadow="hover">
      <div class="toolbar">
        <div class="search-bar">
          <el-input v-model="searchName" placeholder="搜索员工姓名" clearable style="width:200px" @clear="loadData" @keyup.enter="loadData">
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
          <el-select v-model="searchDept" placeholder="选择部门" clearable style="width:160px" @change="loadData">
            <el-option v-for="d in departments" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
          <el-select v-model="searchStatus" placeholder="选择状态" clearable style="width:120px" @change="loadData">
            <el-option label="在职" :value="1" />
            <el-option label="离职" :value="0" />
          </el-select>
          <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
          <el-button @click="resetSearch"><el-icon><Refresh /></el-icon>重置</el-button>
        </div>
        <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>添加员工</el-button>
      </div>

      <el-table :data="employeeList" stripe v-loading="loading" style="margin-top:16px" row-class-name="table-row">
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="name" label="姓名" width="90">
          <template #default="{ row }">
            <div class="name-cell">
              <el-avatar :size="28" :src="row.avatar || undefined" style="background:linear-gradient(135deg,#667eea,#764ba2);font-size:12px">
                {{ row.name ? row.name.charAt(0) : '?' }}
              </el-avatar>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="departmentName" label="部门" width="100" />
        <el-table-column prop="position" label="职位" width="110" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="salary" label="薪资" width="100">
          <template #default="{ row }">
            <span class="salary">¥{{ row.salary ? Number(row.salary).toLocaleString() : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="hireDate" label="入职日期" width="110" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small" effect="light">
              {{ row.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="160">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio label="男" />
                <el-radio label="女" />
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="出生日期">
              <el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="form.hireDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="部门" prop="departmentId">
              <el-select v-model="form.departmentId" placeholder="请选择部门" style="width:100%">
                <el-option v-for="d in departments" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位">
              <el-input v-model="form.position" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="薪资">
              <el-input-number v-model="form.salary" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">在职</el-radio>
                <el-radio :label="0">离职</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="头像">
          <el-upload class="avatar-uploader" action="" :show-file-list="false" :http-request="handleAvatarUpload" accept="image/*">
            <img v-if="form.avatar" :src="form.avatar" class="avatar-preview" />
            <div v-else class="avatar-placeholder">
              <el-icon :size="24"><Plus /></el-icon>
              <span>上传头像</span>
            </div>
          </el-upload>
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
import { Search, Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import { getEmployeeList, addEmployee, updateEmployee, deleteEmployee } from '../api/employee'
import { getDepartmentList } from '../api/department'
import { uploadFile } from '../api/file'

const loading = ref(false)
const submitLoading = ref(false)
const employeeList = ref([])
const departments = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

const searchName = ref('')
const searchDept = ref(null)
const searchStatus = ref(null)

const form = reactive({
  id: null, name: '', gender: '男', birthDate: '', phone: '', email: '',
  departmentId: null, position: '', salary: null, hireDate: '', status: 1, avatar: ''
})

const formRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请选择部门', trigger: 'change' }]
}

const resetForm = () => {
  Object.assign(form, {
    id: null, name: '', gender: '男', birthDate: '', phone: '', email: '',
    departmentId: null, position: '', salary: null, hireDate: '', status: 1, avatar: ''
  })
}

const resetSearch = () => {
  searchName.value = ''
  searchDept.value = null
  searchStatus.value = null
  loadData()
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchName.value) params.name = searchName.value
    if (searchDept.value !== null) params.departmentId = searchDept.value
    if (searchStatus.value !== null) params.status = searchStatus.value
    const res = await getEmployeeList(params)
    employeeList.value = res.data || []
  } finally {
    loading.value = false
  }
}

const loadDepartments = async () => {
  const res = await getDepartmentList()
  departments.value = res.data || []
}

const handleAdd = () => { resetForm(); dialogTitle.value = '添加员工'; dialogVisible.value = true }
const handleEdit = (row) => {
  Object.assign(form, {
    id: row.id,
    name: row.name,
    gender: row.gender,
    birthDate: row.birthDate,
    phone: row.phone,
    email: row.email,
    departmentId: row.departmentId,
    position: row.position,
    salary: row.salary,
    hireDate: row.hireDate,
    status: row.status,
    avatar: row.avatar
  })
  dialogTitle.value = '编辑员工'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除员工"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    await deleteEmployee(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (form.id) { await updateEmployee(form); ElMessage.success('更新成功') }
    else { await addEmployee(form); ElMessage.success('添加成功') }
    dialogVisible.value = false
    loadData()
  } finally { submitLoading.value = false }
}

const handleAvatarUpload = async (options) => {
  try {
    const res = await uploadFile(options.file)
    form.avatar = res.data
    ElMessage.success('头像上传成功')
  } catch (e) { ElMessage.error('上传失败') }
}

onMounted(() => { loadData(); loadDepartments() })
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px; }
.search-bar { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.name-cell { display: flex; align-items: center; gap: 8px; }
.salary { font-weight: 600; color: #409EFF; }
.avatar-uploader :deep(.el-upload) {
  border: 2px dashed #e0e0e0; border-radius: 8px; cursor: pointer;
  width: 100px; height: 100px; display: flex; align-items: center; justify-content: center;
  transition: border-color 0.3s;
}
.avatar-uploader :deep(.el-upload:hover) { border-color: #409EFF; }
.avatar-preview { width: 100px; height: 100px; object-fit: cover; border-radius: 6px; }
.avatar-placeholder {
  display: flex; flex-direction: column; align-items: center; gap: 4px;
  color: #8c939d; font-size: 12px;
}
</style>
