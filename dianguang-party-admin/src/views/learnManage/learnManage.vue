<template>
  <div>
    <el-card>
      <!--       添加用户 & 批量添加用户按钮 -->
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" @click="batchExportUserDialogVisible = true">批量导出学习信息</el-button>
          <el-button type="primary" @click="batchBranchLearnDialogVisible = true">修改支部课程信息</el-button>
        </div>
        <!-- 搜索框 -->
        <el-input
            v-model="searchQuery"
            prefix-icon="el-icon-search"
            placeholder="请输入课程名称"
            clearable
            @input="handleSearch"
            style="width: 300px"
        />
      </div>

      <!-- 表格 -->
      <el-table :data="filteredLearnRateStatistics"  v-loading="loading" border stripe style="width: 100%">
        <el-table-column
            type="expand">
          <template slot-scope="scope">
            <el-card>
              <el-table :data="scope.row.branches" @expand-change="(row, expandedRows) => expandChange(row, expandedRows, scope.row)" border stripe style="width: 100%">
                <el-table-column type="expand">
                  <template slot-scope="scope1">
                    <el-card>
                      <!-- 表格 -->
                      <el-table :data="getExpandedData(scope.row.learn_class.classId, scope1.row.branch.branchId)" border stripe style="width: 100%">
                        <el-table-column
                            prop="user.name"
                            label="姓名">
                        </el-table-column>

                        <el-table-column
                            prop="user.jobId"
                            label="学/工号"
                            show-overflow-tooltip>
                        </el-table-column>

                        <el-table-column
                            prop="isLearned"
                            label="是否完成"
                            show-overflow-tooltip>
                        </el-table-column>

                        <el-table-column
                            prop="duration"
                            label="学习时长/秒"
                            show-overflow-tooltip>
                        </el-table-column>

                        <el-table-column
                            fixed="right"
                            label="操作"
                            align="center">
                          <template slot-scope="scope">
                            <el-button @click="editMember(scope.row)" type="text" size="small">修改</el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </el-card>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="branch.branchName"
                    label="支部名称"
                    show-overflow-tooltip>
                </el-table-column>

                <el-table-column
                    prop="branch.department"
                    label="所属学院"
                    show-overflow-tooltip>
                </el-table-column>

                <el-table-column
                    prop="num"
                    label="支部人数"
                    show-overflow-tooltip>
                </el-table-column>

                <el-table-column
                    prop="finishNum"
                    label="完成人数">
                </el-table-column>
              </el-table>
            </el-card>
          </template>
        </el-table-column>
        <el-table-column
            prop="learn_class.classTitle"
            label="课程名称"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="learn_class.founder"
            label="课程创建人"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="label"
            label="课程用户">
        </el-table-column>

        <el-table-column
            prop="num"
            label="学习课程人数"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="finishNum"
            label="完成课程人数"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="learn_class.uploadTime"
            label="课程创建时间"
            show-overflow-tooltip>
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

    <el-dialog title="批量导出学习信息" :visible.sync="batchExportUserDialogVisible" @cancel="batchExportUserDialogVisible = false">
      <el-table
          ref="multipleTable"
          :data="branches"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="branch.branchName"
            label="支部名称">
        </el-table-column>
      </el-table>
      <div style="text-align: right; padding: 10px 20px;">
        <el-button type="primary" @click="batchExportUserLearning">批量导出</el-button>
      </div>
    </el-dialog>

    <el-dialog title="修改支部课程信息" :visible.sync="batchBranchLearnDialogVisible" @cancel="cancelBranchLearn">
      <el-form>
        <!-- 支部选择 -->
        <el-form-item label="支部选择">
          <el-select v-model="branchSelect" placeholder="请选择">
            <el-option
                v-for="item in branches"
                :key="item.branch.branchName"
                :label="item.branch.branchName"
                :value="item.branch.branchName">
            </el-option>
          </el-select>
        </el-form-item>

        <!-- 课程选择 -->
        <el-form-item label="课程选择">
          <el-select v-model="classSelect" placeholder="请选择">
            <el-option
                v-for="item in classes"
                :key="item.classTitle"
                :label="item.classTitle"
                :value="item.classTitle">
            </el-option>
          </el-select>
        </el-form-item>

        <!-- 是否学习完成 -->
        <el-form-item label="是否学习完成">
          <el-select v-model="if_learned" placeholder="请选择">
            <el-option key="已完成" label="已完成" value="已完成"></el-option>
            <el-option key="未完成" label="未完成" value="未完成"></el-option>
          </el-select>
        </el-form-item>

        <!-- 最小学习时长 -->
        <el-form-item label="最小学习时长(s)">
          <el-input v-model="min_duration" placeholder="请输入最小时长" />
        </el-form-item>

        <!-- 最大学习时长 -->
        <el-form-item label="最大学习时长(s)">
          <el-input v-model="max_duration" placeholder="请输入最大时长" />
        </el-form-item>
      </el-form>
      <div style="text-align: right; padding: 10px 20px;">
        <el-button type="primary" @click="cancelBranchLearn">取消</el-button>
        <el-button type="primary" @click="editLearnInformation">修改学习信息</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { getLearnRateStatistics, getLearnRateBranchStatistics } from '@/api/learnRate'
import { deleteCertificate, exportAllLearnStats, changeLearnDurationByBranch } from '@/api/classLearnInfo'
import {deleteBranch, getBranchManage} from "@/api/branch";
import { getClasses } from "@/api/class";
import axios  from "axios";
export default {
  data() {
    return {
      searchQuery: '',
      filteredLearnRateStatistics: [],
      currentPage: 1,
      pageSize: 13,
      totalMembers: 0,

      rowCurrentPage:1,

      // 对话框相关
      checkDialogVisible: false,
      checkLearnDetailsDialogVisible: false,
      batchExportUserDialogVisible: false,
      batchBranchLearnDialogVisible: false,

      learnRateStatistics: [],

      currentLearnRateStatistics: [],

      expandedData: {},

      multipleSelection: [],

      branches: [],
      classes: [],

      loading: true,

      branchSelect: '',
      classSelect: '',
      if_learned: '',
      min_duration: '',
      max_duration: '',

    };
  },
  mounted() {
    getBranchManage().then(res => {
      this.branches = res.data;
    });

    getClasses().then(res => {
      console.log(res.data)
      this.classes = res.data
    })

    getLearnRateStatistics().then((res) => {
      this.learnRateStatistics = res.data.map(learnRate => {
        return {
          ...learnRate,
          label: learnRate.learn_class && learnRate.learn_class.authority === 0 ? '党员学习' : '积极分子学习'
        };
      });
      this.loading = false
      console.log(this.learnRateStatistics)
      this.totalMembers = res.data.length;
      this.updateTableData()
    });
  },
  methods: {
    updateTableData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.filteredLearnRateStatistics = this.learnRateStatistics.slice(start, end);
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      this.updateTableData();
    },
    handleSearch() {
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        this.filteredLearnRateStatistics = this.filteredLearnRateStatistics.filter(learnRateStatistics =>
            learnRateStatistics.learn_class.classTitle.toLowerCase().includes(query)
        );
      } else {
        this.updateTableData();
      }
    },
    editMember(){
      this.$message.warning('暂时不支持修改个人学习')
    },
    expandChange(row, expandedRows, originRow){
      const data = {
        class_id: originRow.learn_class.classId,
        branch_id: row.branch.branchId
      }
      getLearnRateBranchStatistics(data).then((res) => {
        console.log(res.data)
        if (!this.expandedData[data.class_id]) {
          this.$set(this.expandedData, data.class_id, {}); // 使用 $set 确保响应性
        }
        if (!this.expandedData[data.class_id][data.branch_id]) {
          this.$set(this.expandedData[data.class_id], data.branch_id, {}); // 使用 $set 确保响应性
        }
        this.expandedData[data.class_id][data.branch_id] = res.data.userStatistics.map(userStatistics => {
          return {
            ...userStatistics,
            isLearned: userStatistics.ifLearned ? '已完成' : '未完成'
          };
        })
        console.log(this.expandedData)
      });
    },
    getExpandedData(classId, branchId) {
      return this.expandedData[classId] ? this.expandedData[classId][branchId] : null;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val.map(item => item.branch.branchName);
      console.log(this.multipleSelection)
    },
    batchExportUserLearning(){
      if (this.multipleSelection === ''){
        this.$message.error('请选择要导出的学习记录')
        return
      }
      const branchList = this.multipleSelection.map(item => item);
      console.log(branchList)
      exportAllLearnStats(branchList).then((res) => {
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
          link.setAttribute('download', '学习信息.xlsx');  // 自定义文件名
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
    editLearnInformation(){
      this.$confirm('确认修改学习信息嘛, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认删除
        const minDuration = Number(this.min_duration);
        const maxDuration = Number(this.max_duration);
        console.log(this.branches)
        console.log(this.branches.find(item => item.branch.branchName === this.branchSelect))
        const data = {
          class_id: this.classes.find(item => item.classTitle === this.classSelect).classId,
          branch_id: this.branches.find(item => item.branch.branchName === this.branchSelect).branch.branchId,
          if_learned: this.if_learned === '已完成' ? 1 : 0,
          min_duration: minDuration,
          max_duration: maxDuration,
        };
        console.log(data)
        if (minDuration > maxDuration) {
          this.$message.warning('最小学习时长不能大于最大学习时长');
          return
        }


        if (!Number.isInteger(minDuration) || !Number.isInteger(maxDuration)) {
          this.$message.warning('学习时长必须为整数');
          return;
        }

        changeLearnDurationByBranch(data).then((res) => {
          console.log(res);
          this.$message.success('修改成功');
          location.reload();
        }).catch(error => {
          console.error('修改失败', error);
          this.$message.error('修改失败');
        });
      }).catch(() => {
        // 取消删除
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    cancelBranchLearn(){
      this.branchSelect = ''
      this.classSelect = ''
      this.if_learned = ''
      this.max_duration = ''
      this.min_duration = ''
      this.batchBranchLearnDialogVisible = false
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
.form-styled {
  max-width: 600px; /* 控制表单宽度 */
  margin: 20px auto; /* 居中显示 */
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.el-form-item {
  margin-bottom: 20px; /* 表单项之间的间距 */
}

.el-radio-group,
.el-input,
.el-select {
  width: 100%; /* 使输入框和选择框占满表单宽度 */
}

.el-radio {
  margin-right: 10px; /* 单选框间的间距 */
}
</style>
