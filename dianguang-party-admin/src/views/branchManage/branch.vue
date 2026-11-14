<template>
  <div>
    <el-card>
<!--       添加用户 & 批量添加用户按钮 -->
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" @click="addBranchDialogVisible = true">添加支部</el-button>
        </div>
        <!-- 搜索框 -->
        <el-input
            v-model="searchQuery"
            prefix-icon="el-icon-search"
            placeholder="请输入支部名称"
            clearable
            @input="handleSearch"
            style="width: 300px"
        />
      </div>

      <!-- 表格 -->
      <el-table :data="filteredBranches" border stripe style="width: 100%">
        <el-table-column
            fixed
            prop="branch.branchId"
            label="支部id">
        </el-table-column>

        <el-table-column
            prop="branch.branchName"
            label="支部名称"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="secretary.name"
            label="支部管理员"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="branch.branchDis"
            label="支部简介"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="branch.department"
            label="支部所属部门"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="branch.createTime"
            label="支部创建时间"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            fixed="right"
            label="操作"
            align="center">
          <template slot-scope="scope">
            <el-button @click="deleteBRanch(scope.row)" style="color: red" type="text" size="small">删除</el-button>
            <el-button @click="editBranch(scope.row)" type="text" size="small">编辑</el-button>
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
    <el-dialog title="添加支部" :visible.sync="addBranchDialogVisible" @cancel="cancelAdd" width="500px">
      <el-form :model="newBranch" label-width="80px" class="user-form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="支部名称">
              <el-input v-model="newBranch.branchName" placeholder="请输入支部名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="支部部门">
              <el-input v-model="newBranch.department" placeholder="请输入支部部门" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属支部">
              <el-select  v-model="newBranch.branchType">
                <el-option label="党员支部" value="党员支部"></el-option>
                <el-option label="积极分子支部" value="积极分子支部"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="支部简介">
              <el-input type="textarea" v-model="newBranch.branchDis"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="dialog-footer">
          <el-button @click="cancelAdd">取消</el-button>
          <el-button type="primary" @click="addBRanch">添加</el-button>
        </div>
      </el-form>
    </el-dialog>

    <el-dialog title="编辑支部" :visible.sync="editBranchDialogVisible" @cancel="cancelEdit" width="500px">
      <el-form :model="currentBranch" label-width="80px" class="user-form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="支部名称">
              <el-input v-model="currentBranch.current_name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="支部部门">
              <el-input v-model="currentBranch.department" placeholder="请输入学/工号" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="支部书记">
              <el-select v-model="currentBranch.managerName" placeholder="请选择">
                <el-option
                    v-for="item in currentUser.map(item => item.name)"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="支部简介">
              <el-input type="textarea" v-model="currentBranch.branch_dis" />
            </el-form-item>
          </el-col>
        </el-row>
        <div class="dialog-footer">
          <el-button @click="cancelEdit">取消</el-button>
          <el-button type="primary" @click="updateBRanch">保存</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { getBranchManage, addBranch, updateBranch, deleteBranch } from '@/api/branch'
export default {
  data() {
    return {
      branches: [],
      filteredBranches: [],
      searchQuery: "",
      currentPage: 1,
      pageSize: 13,
      totalMembers: 0,

      // 对话框相关
      addBranchDialogVisible: false,
      editBranchDialogVisible: false,

      newBranch: {
        branchName: "",
        branchDis: "",
        department: "",
        branchType: "",
      },
      currentBranch:{
        branch_name: '',
        branch_dis: '',
        department: '',
        manager_id: '',
        managerName: '',
        current_name: '',
      },
      currentUser: [],
    };
  },
  mounted() {
    getBranchManage().then(res => {
      console.log(res)
      this.branches = res.data;
      this.totalMembers = res.data.length;
      this.updateTableData()
    });
  },
  methods: {
    updateTableData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.filteredBranches = this.branches.slice(start, end);
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      this.updateTableData();
    },
    handleSearch() {
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        this.filteredBranches = this.branches.filter(branch =>
            branch.branch.branchName.toLowerCase().includes(query)
        );
      } else {
        this.updateTableData();
      }
    },
    // 添加支部
    async addBRanch() {
      try { await this.$confirm('确认提交当前操作？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      if (this.newBranch.branchName === "" || this.newBranch.department === "") {
        this.$message.error("请填写完整信息");
        return;
      }
      addBranch(this.newBranch).then((res) => {
        this.addBranchDialogVisible = false;
        console.log(res)
        this.$message.success(res.data)
        location.reload()
      });
    },
    // 取消添加
    cancelAdd(){
      this.addBranchDialogVisible = false
      this.newBranch ={
        branchName: "",
        branchDis: "",
        department: "",
      }
    },
    editBranch(row) {
      this.currentBranch.branch_name = row.branch.branchName
      this.currentBranch.current_name = row.branch.branchName
      this.currentBranch.branch_dis = row.branch.branchDis
      this.currentBranch.department = row.branch.department
      console.log(row)
      if (row.branch.managerId !== null){
        this.currentBranch.manager_id = row.branch.managerId
        this.currentBranch.managerName = row.secretary.name
      }
      this.currentUser = row.users
      this.editBranchDialogVisible = true;
    },
    async updateBRanch() {
      try { await this.$confirm('确认保存当前修改？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      if (this.currentBranch.managerName !== "" && this.currentUser.map(user => user.name).includes(this.currentBranch.managerName)){
        this.currentBranch.manager_id = this.currentUser.find(user => user.name === this.currentBranch.managerName).id
      }
      console.log(this.currentBranch)
      updateBranch(this.currentBranch).then(res => {
        console.log(res)
        if (res.data === "编辑支部信息成功"){
          this.editBranchDialogVisible = false;
          this.$message.success('支部信息更新成功');
          location.reload();
        }
        else {
          console.log(res.data)
          this.$message.warning(res.data);
        }
      }).catch(error => {
        this.$message.error('更新失败');
      });
    },
    cancelEdit() {
      this.editBranchDialogVisible = false;
    },

    // 删除支部
    deleteBRanch(row) {
      console.log(row)
      this.$confirm('此操作将永久删除该支部, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认删除
        const data = {
          branchName: row.branch.branchName
        };
        deleteBranch(data).then((res) => {
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
    //
    handleFileChange(file) {

    },
    submitBatchAdd() {

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
