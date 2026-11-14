<template>
  <div>
    <el-card>
      <!-- 添加用户 & 批量添加用户按钮 -->
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" @click="openAddUserDialog">添加积极分子</el-button>
          <el-button type="primary" @click="batchExportUser()">批量结业</el-button>
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
    <el-dialog title="添加积极分子" :visible.sync="addUserDialogVisible" @cancel="cancelAdd" width="500px">
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
                    v-for="item in branches.filter(branch => branch.branch.authority === 1)"
                    :key="item.branch.branchName"
                    :label="item.branch.branchName"
                    :value="item.branch.branchName">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="角色">
              <el-input v-model="newUser.role" disabled></el-input>
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

    <el-dialog title="编辑积极分子" :visible.sync="editUserDialogVisible" @cancel="cancelEdit" width="500px">
      <el-form :model="currentMember" label-width="80px" class="user-form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="姓名">
              <el-input v-model="currentMember.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="学/工号">
              <el-input v-model="currentMember.jobId" placeholder="请输入学/工号" disabled/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属支部">
              <el-select v-model="currentMember.branchName" placeholder="请选择">
                <el-option
                    v-for="item in branches.filter(branch => branch.branch.authority === 1)"
                    :key="item.branch.branchName"
                    :label="item.branch.branchName"
                    :value="item.branch.branchName">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="角色">
              <el-input v-model="currentMember.role" disabled></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="岗位">
              <el-input v-model="currentMember.post" placeholder="请输入岗位"></el-input>
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

    <el-dialog title="批量结业"  :visible.sync="batchAddUserDialogVisible" @cancel="cancelEdit" width="60vw">
      <el-table :data="activistLearned" border stripe style="width: 100%" @selection-change="handleSelectionChange">

        <el-table-column
            type="selection"
            width="55">
        </el-table-column>

        <el-table-column
            prop="name"
            label="姓名"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="jobId"
            label="学/工号"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="branchName"
            label="所属支部"
            show-overflow-tooltip>
        </el-table-column>


        <el-table-column
            prop="role"
            label="角色"
            show-overflow-tooltip>
        </el-table-column>


        <el-table-column
            fixed="right"
            label="操作"
            align="center">
          <template slot-scope="scope">
            <el-button @click="exportCert(scope.row)" type="text" size="small">导出培训证明</el-button>
          </template>
        </el-table-column>
      </el-table>


      <div class="dialog-footer">
        <el-button type="primary" @click="giveGraduation">给予结业</el-button>
      </div>
    </el-dialog>
    <el-dialog title="导出培训证明" :visible.sync="exportDialogVisible" width="400px">
      <el-form :model="exportForm" label-width="100px">
        <el-form-item label="学习开始时间">
          <el-date-picker
              v-model="exportForm.startTime"
              type="date"
              placeholder="请选择完成时间"
              style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="完成时间">
          <el-date-picker
              v-model="exportForm.completionTime"
              type="date"
              placeholder="请选择完成时间"
              style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="导出时间">
          <el-date-picker
              v-model="exportForm.exportTime"
              type="date"
              placeholder="请选择导出时间"
              style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitExportCert">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { queryAllUser, addUser, update, deleteUser} from "@/api/user";
import { getBranchManage } from '@/api/branch'
import {getRoleLearnedList, finishTrain, exportCertificate, deleteCertificate} from '@/api/classLearnInfo'
import axios from "axios";
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
      exportDialogVisible: false, // 导出对话框

      // 新用户数据
      newUser: {
        job_id: "",
        name: "",
        branch_id: "",
        branchName: "",
        role: "",
        post: "",
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
        role: "",
        post: "",
        phone: "",
        department: "",
        grade: "",
        portrait: "",
      },

      branches: [],
      roles: ['党员', '积极分子'],

      activistLearned: [],

      multipleSelection: [],

      // 导出表单
      exportForm: {
        startTime: "",
        completionTime: "", // 完成时间
        exportTime: "", // 导出时间
        jobId: "" // 当前导出的用户 jobId
      },
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
        this.members = users.filter(user => user.role === '积极分子')
        console.log(this.members);
        this.totalMembers = this.members.length;
        this.updateTableData();
      });

      this.newUser.role = '积极分子'
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

      if (this.newUser.jobId === "" || this.newUser.name === "" || this.newUser.branchId === "" || this.newUser.post === "" || this.newUser.role === "" || this.newUser.phone === "" || this.newUser.department === "" || this.newUser.grade === "") {
        this.$message.error("请填写完整信息");
        return;
      }
      console.log(this.branches)
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
        role: "",
        post: "",
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
      const user = {
        ...this.currentMember,
        branchId: this.branches.find(branch => branch.branch.branchName === this.currentMember.branchName).branch.branchId,
      };
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
    batchExportUser(){
      this.batchAddUserDialogVisible = true
      const data = {
        role: '积极分子'
      }
      getRoleLearnedList(data).then((res)=>{
        console.log(res)
        this.activistLearned = res.data.map(member => {
          const branch = this.branches.find(branch => branch.branch.branchId === member.branchId);
          return {
            ...member,
            branchName: branch ? branch.branch.branchName : '未知支部'
          };
        });
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val.map(item => item.id);
      console.log(this.multipleSelection)
    },
    exportCert(row) {
      this.exportForm.jobId = row.jobId; // 设置当前导出用户的 jobId
      this.exportDialogVisible = true; // 显示导出对话框
    },
    submitExportCert() {
      if (!this.exportForm.startTime || !this.exportForm.completionTime || !this.exportForm.exportTime) {
        this.$message.error("请填写学习开始时间、完成时间和导出时间！");
        return;
      }

      const data = {
        job_id: this.exportForm.jobId,
        start_time: this.exportForm.startTime,
        completion_time: this.exportForm.completionTime,
        export_time: this.exportForm.exportTime
      };

      exportCertificate(data).then((res) => {
        const fileUrl = res.data;

        // 使用 axios 下载文件
        axios({
          url: fileUrl,
          method: 'GET',
          responseType: 'blob',
        }).then((response) => {
          const blob = new Blob([response.data], { type: response.headers['content-type'] });
          const downloadUrl = URL.createObjectURL(blob);

          const link = document.createElement('a');
          link.href = downloadUrl;
          link.setAttribute('download', `${this.exportForm.jobId}_培训证明.pdf`);
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);

          URL.revokeObjectURL(downloadUrl);
          this.$message.success('导出成功！');
          this.exportDialogVisible = false; // 关闭对话框
        });
      }).catch((error) => {
        console.error(error);
        this.$message.error('导出失败，请重试！');
      });
    },
    async giveGraduation(){
      try { await this.$confirm('确认提交当前操作？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      if (this.multipleSelection.length === 0){
        this.$message.warning('请选择要导出的积极分子')
        return;
      }
      const users_id = this.multipleSelection.map(item => item);
      finishTrain(users_id).then((res) => {
        this.$message.success('结业成功')
        location.reload()
      })
    },
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
