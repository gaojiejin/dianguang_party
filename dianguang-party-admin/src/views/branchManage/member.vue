<template>
  <div>
    <el-card>
      <!-- 添加用户 & 批量添加用户按钮 -->
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" @click="openAddUserDialog">添加用户</el-button>
          <el-button type="primary" @click="openBatchAddUserDialog">批量添加用户</el-button>
          <el-button type="primary" @click="downMoBan">下载导入模板</el-button>
        </div>
        <!-- 搜索框 -->
        <el-input
            v-model="searchQuery"
            prefix-icon="el-icon-search"
            placeholder="请输入学/工号或名字"
            clearable
            @input="handleSearch"
            style="width: 300px"
        />
      </div>

      <!-- 表格 -->
      <el-table :data="filteredMembers" border stripe style="width: 100%">
        <el-table-column
            fixed
            prop="id"
            label="用户id">
        </el-table-column>

        <el-table-column
            prop="jobId"
            label="学/工号"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="password"
            label="密码"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="branchName"
            label="所属支部"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="name"
            label="姓名"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="post"
            label="岗位"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="portrait"
            label="头像"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="role"
            label="角色"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="phone"
            label="手机号"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="department"
            label="部门"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="grade"
            label="年级"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            fixed="right"
            label="操作"
            align="center">
          <template slot-scope="scope">
            <el-button @click="deleteMember(scope.row)" style="color: red" type="text" size="small">删除</el-button>
            <el-button @click="editMember(scope.row)" type="text" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pager-wrapper">
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="totalMembers"
            layout="total, prev, pager, next, jumper"
        />
      </div>
    </el-card>

    <!-- 添加用户的对话框 -->
    <el-dialog title="添加用户" :visible.sync="addUserDialogVisible" @cancel="cancelAdd" width="500px">
      <el-form :model="newUser" label-width="80px" class="user-form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="姓名">
              <el-input v-model="newUser.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="学/工号">
              <el-input v-model="newUser.job_id" placeholder="请输入学/工号" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属支部">
              <el-select v-model="newUser.branchName" placeholder="请选择">
                <el-option
                    v-for="item in branches.filter(branch => branch.branch.authority === 0)"
                    :key="item.branch.branchName"
                    :label="item.branch.branchName"
                    :value="item.branch.branchName">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="角色">
              <el-select v-model="newUser.role" placeholder="请选择">
                <el-option
                    v-for="item in roles"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="岗位">
              <el-input v-model="newUser.post" placeholder="请输入岗位" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="手机号">
              <el-input v-model="newUser.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="部门">
              <el-input v-model="newUser.department" placeholder="请输入部门" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="年级">
              <el-input v-model="newUser.grade" placeholder="请输入年级" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="dialog-footer">
          <el-button @click="cancelAdd">取消</el-button>
          <el-button type="primary" @click="addMember">添加</el-button>
        </div>
      </el-form>
    </el-dialog>

    <el-dialog title="编辑用户" :visible.sync="editUserDialogVisible" @cancel="cancelEdit" width="500px">
      <el-form :model="currentMember" label-width="80px" class="user-form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="姓名">
              <el-input v-model="currentMember.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="学/工号">
              <el-input v-model="currentMember.jobId" placeholder="请输入学/工号" disabled />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属支部">
              <el-select v-model="newUser.branchName" placeholder="请选择">
                <el-option
                    v-for="item in branches.filter(branch => branch.branch.authority === 0)"
                    :key="item.branch.branchName"
                    :label="item.branch.branchName"
                    :value="item.branch.branchName">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="角色">
              <el-select v-model="currentMember.role" placeholder="请选择" disabled>
                <el-option
                    v-for="item in roles"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="岗位">
              <el-input v-model="currentMember.post" placeholder="请输入岗位" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="手机号">
              <el-input v-model="currentMember.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="部门">
              <el-input v-model="currentMember.department" placeholder="请输入部门" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="年级">
              <el-input v-model="currentMember.grade" placeholder="请输入年级" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="dialog-footer">
          <el-button @click="cancelEdit">取消</el-button>
          <el-button type="primary" @click="updateMember">保存</el-button>
        </div>
      </el-form>
    </el-dialog>


<!--     批量添加用户的对话框 -->
    <el-dialog title="批量添加用户" :visible.sync="batchAddUserDialogVisible">
      <el-upload
          class="upload-demo"
          drag
          action=""
          :limit="1"
          :before-upload="beforeUpload"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :on-remove="handleUploadRemove"
          :http-request="httpRequest"
          multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">提示：上传的xlsx文件必须包含以下字段：姓名、学工号、支部名称、角色、手机号、部门、年级。</div>
      </el-upload>
      <div style=" text-align: right">
        <el-button style="margin-top: 2vh;" type="primary" @click="batchAddUsers" :disabled="!file" >
          批量添加用户
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import { queryAllUser, addUser, uploadAdminFile, update, deleteUser, getMoban} from "@/api/user";
import { getBranchManage } from '@/api/branch'
import axios from "axios"
import {deleteCertificate} from "@/api/classLearnInfo";
export default {
  data() {
    return {
      members: [],
      filteredMembers: [],
      searchQuery: "",
      currentPage: 1,
      pageSize: 13,
      totalMembers: 0,

      // 对话框相关
      addUserDialogVisible: false,
      editUserDialogVisible: false,
      batchAddUserDialogVisible: false,

      // 新用户数据
      newUser: {
        job_id: "",
        name: "",
        branch_id: "",
        branchName: "",
        post: "",
        role: "",
        phone: "",
        department: "",
        grade: "",
        portrait: "",
      },
      // 编辑当前用户数据
      currentMember: {
        jobId: "",
        name: "",
        branchId: "",
        post: "",
        role: "",
        phone: "",
        department: "",
        grade: "",
        portrait: "",
      },

      branches: [],
      roles: ['党员', '预备党员'],

      file: '',
    };
  },
  mounted() {
    getBranchManage().then(res => {
      this.branches = res.data;
      // 获取全部用户信息
      queryAllUser().then(res => {
        const users = res.data.map(member => {
          const branch = this.branches.find(branch => branch.branch.branchId === member.branchId);
          return {
            ...member,
            branchName: branch ? branch.branch.branchName : '未知支部'
          };
        });
        this.members = users.filter(user => user.role !== '积极分子')
        this.totalMembers = res.data.length;
        this.updateTableData();
      });
    });
  },
  methods: {
    updateTableData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.filteredMembers = this.members.slice(start, end);
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      this.updateTableData();
    },
    handleSearch() {
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        this.filteredMembers = this.members.filter(member =>
            member.jobId.toLowerCase().includes(query) || member.name.toLowerCase().includes(query),
        );
      } else {
        this.updateTableData();
      }
    },
    handleAvatarSuccess(res, file) {
      this.newUser.portrait = URL.createObjectURL(file.raw);
    },
    openAddUserDialog() {
      this.addUserDialogVisible = true;
    },
    openBatchAddUserDialog() {
      this.batchAddUserDialogVisible = true;
    },
    // 添加用户
    async addMember() {
      try { await this.$confirm('确认提交当前操作？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      if (this.newUser.jobId === "" || this.newUser.name === "" || this.newUser.branchId === "" || this.newUser.role === "" || this.newUser.phone === "" || this.newUser.department === "" || this.newUser.grade === "") {
        this.$message.error("请填写完整信息");
        return;
      }
      this.newUser.branch_id = this.branches.find(branch => branch.branch.branchName === this.newUser.branchName).branch.branchId;
      console.log(this.newUser)
      addUser(this.newUser).then((res) => {
        if (res.data === "添加用户成功"){
          this.addUserDialogVisible = false;
          this.$message.success(res.data)
          location.reload()
        }
        else {
          this.$message.warning(res.data)
        }
      });
    },
    // 取消添加用户
    cancelAdd(){
      this.addUserDialogVisible = false
      this.newUser ={
        job_id: "",
        name: "",
        branch_id: "",
        post: "",
        role: "",
        phone: "",
        department: "",
        grade: "",
        portrait: "",
      }
    },
    editMember(row) {
      this.currentMember = { ...row };
      this.editUserDialogVisible = true;
    },
    async updateMember() {
      try { await this.$confirm('确认保存当前修改？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      console.log(this.currentMember)
      console.log(this.currentMember.branchName)
      let user = {}
      if (this.currentMember.branchName !== '未知支部'){
        user = {
          ...this.currentMember,
          branchId: this.branches.find(branch => branch.branch.branchName === this.currentMember.branchName).branch.branchId,
        };
      }
      else {
        user = this.currentMember
      }

      update(user).then(res => {
        this.editUserDialogVisible = false;
        this.$message.success('用户信息更新成功');
        location.reload();
      }).catch(error => {
        this.$message.error('更新失败');
      });
    },
    cancelEdit() {
      this.editUserDialogVisible = false;
    },
    downMoBan(){
      getMoban().then((res) => {
        const fileUrl = res.data;
        // 使用 axios 下载文件
        axios({
          url: fileUrl,
          method: 'GET',
          responseType: 'blob',  // 确保返回的是 Blob 数据
        }).then((response) => {
          console.log(response)
          // 创建 Blob 对象
          const blob = new Blob([response.data], { type: response.headers['content-type'] });
          const downloadUrl = URL.createObjectURL(blob);

          // 创建 <a> 标签并设置下载
          const link = document.createElement('a');
          link.href = downloadUrl;
          link.setAttribute('download', '导入模板.xlsx');  // 自定义文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);

          const data = {
            certificate_path: fileUrl
          }
          deleteCertificate(data).then((res)=>{
            console.log(res)
          })

          // 释放 URL 对象
          URL.revokeObjectURL(downloadUrl);

          this.$message.success('导出成功');
        }).catch((error) => {
          this.$message.error('下载失败');
        });
      })
    },
    // 删除用户
    deleteMember(row) {
      console.log(row)
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认删除
        const data = {
          job_id: row.jobId
        };
        deleteUser(data).then((res) => {
          console.log(res);
          this.$message.success('删除成功');
          location.reload();
        }).catch(error => {
          console.error('删除失败', error);
          this.$message.error('删除失败');
        });
      }).catch(() => {
        // 取消删除
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    beforeUpload(file) {
      // 校验文件类型
      const isXLSX = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
      if (!isXLSX) {
        this.$message.error('只能上传xlsx格式的文件！');
        return false;
      }
      return true;
    },
    handleUploadSuccess(response, file, fileList) {
      // 处理上传成功后的逻辑
      this.$message.success('文件上传成功');
      this.file = fileList[0].raw
      console.log(this.file)
      // 在此处可以调用后端的批量添加用户逻辑
    },
    handleUploadError(err, file, fileList) {
      // 处理上传失败后的逻辑
      this.$message.error('文件上传失败，请重试');
      console.error('上传错误', err);
    },
    handleUploadRemove(file, fileList){
      this.file = ''
    },
    httpRequest(item) {
      console.log(item.file)
      this.file = item.file
    },
    async batchAddUsers() {
      try { await this.$confirm('确认提交当前操作？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      const data = {
        file: this.file
      }
      const formData = new FormData();
      formData.append('file', this.file);
      uploadAdminFile(formData).then((res) => {
        if (res.data === '用户上传成功'){
          this.batchAddUserDialogVisible = false;
          this.$message.success('批量添加成功');
          location.reload();
        }
        else {
          this.batchAddUserDialogVisible = false;
          this.$message.warning(res.data);
        }
        console.log(res)

      }).catch((error) => {
        this.$message.error('批量添加用户失败');
        console.error('批量添加用户失败', error);
      });
    },
    cancelBatchAdd() {
      this.batchAddUserDialogVisible = false;
      this.file = ''
    }
  }
};
</script>

<style scoped lang="less">
.toolbar-wrapper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
.pager-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.dialog-footer {
  text-align: right;
  padding: 10px 20px;
}
</style>
