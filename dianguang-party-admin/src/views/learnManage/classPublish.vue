<template>
  <div>
    <el-card>
      <!--       添加用户 & 批量添加用户按钮 -->
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" @click="addClassDialogVisible = true">添加课程</el-button>
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
      <el-table :data="filteredClasses" border stripe style="width: 100%" @expand-change="expandChange">
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-card>
              <!-- 表格 -->
              <el-table :data="getExpandedData(scope.row)" border stripe style="width: 100%">
                <el-table-column
                    prop="title"
                    label="章节名称">
                </el-table-column>

                <el-table-column
                    prop="founder"
                    label="章节发布人"
                    show-overflow-tooltip>
                </el-table-column>

                <el-table-column
                    prop="authority"
                    label="章节类型"
                    show-overflow-tooltip>
                </el-table-column>

                <el-table-column
                    prop="uploadTime"
                    label="章节上传时间"
                    show-overflow-tooltip>
                </el-table-column>

                <el-table-column
                    prop="duration"
                    label="章节默认时长"
                    show-overflow-tooltip>
                </el-table-column>


                <el-table-column
                    fixed="right"
                    label="操作"
                    align="center">
                  <template slot-scope="scope1">
                    <el-button @click="deleteChapter(scope1.row)" style="color: red" type="text" size="small">删除</el-button>
                    <el-button @click="checkChapter(scope1.row)" type="text" size="small">查看</el-button>
                    <el-button @click="editChapter(scope1.row)" type="text" size="small">编辑</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </template>
        </el-table-column>

        <el-table-column
            prop="classTitle"
            label="课程名称">
        </el-table-column>

        <el-table-column
            prop="founder"
            label="创建人"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="classAuthority"
            label="课程权限"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="department"
            label="所属部门"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="uploadTime"
            label="课程发布时间"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="classPortrait"
            label="课程头像"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            fixed="right"
            label="操作"
            align="center">
          <template slot-scope="scope">
            <el-button @click="deleteCLass(scope.row)" style="color: red" type="text" size="small">删除</el-button>
            <el-button @click="editClass(scope.row)" type="text" size="small">编辑</el-button>
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

    <!-- 添加课程的对话框 -->
    <el-dialog title="添加课程" :visible.sync="addClassDialogVisible" @cancel="cancelAdd" width="500px">
      <el-form :model="newClass" label-width="80px" class="user-form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="课程名称">
              <el-input v-model="newClass.class_title" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属部门">
              <el-input v-model="newClass.department" placeholder="请输入部门" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="课程分类">
              <el-select v-model="newClass.authority" placeholder="请选择">
                <el-option
                  label="党员学习"
                  value="党员学习">
                </el-option>
                <el-option
                  label="积极分子学习"
                  value="积极分子学习">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="课程头像">
              <el-upload
                  class="avatar-uploader"
                  action=""
                  :limit="1"
                  :before-upload="beforeUpload"
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                  :on-remove="handleUploadRemove"
                  :http-request="httpRequest">
                <img v-if="newClass.class_portrait" :src="imageUrl" class="avatar" alt="">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="dialog-footer">
          <el-button @click="cancelAdd">取消</el-button>
          <el-button type="primary" @click="addCLass">添加</el-button>
        </div>
      </el-form>
    </el-dialog>

    <el-dialog title="编辑课程" :visible.sync="editClassDialogVisible" @cancel="editClassDialogVisible = false" width="500px">
      <el-form :model="currentClass" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="课程标题">
              <el-input v-model="currentClass.classTitle" disabled/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属权限">
              <el-input v-model="currentClass.classAuthority"  disabled/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="上传时间">
              <el-date-picker
                  v-model="currentClass.uploadTime"
                  type="datetime"
                  placeholder="选择上传时间"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  :default-time="['00:00:00']"
              ></el-date-picker>
            </el-form-item>
          </el-col>

          <div class="dialog-footer">
            <el-button @click="editClassDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="updateClass">保存</el-button>
          </div>
        </el-row>
      </el-form>
    </el-dialog>

    <el-dialog title="编辑章节" :visible.sync="editChapterDialogVisible" @cancel="editChapterDialogVisible = false" width="500px">
      <el-form :model="currentChapter" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="课程标题">
              <el-input v-model="currentChapter.title" disabled/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属权限">
              <el-input v-model="currentChapter.authority"  disabled/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="上传时间">
              <el-date-picker
                  v-model="currentChapter.uploadTime"
                  type="datetime"
                  placeholder="选择上传时间"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  :default-time="['00:00:00']"
              ></el-date-picker>
            </el-form-item>
          </el-col>

          <div class="dialog-footer">
            <el-button @click="editChapterDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="updateChapter">保存</el-button>
          </div>
        </el-row>
      </el-form>
    </el-dialog>

    <el-drawer
        title="章节学习内容"
        :visible.sync="checkChapterDrawer"
        direction="rtl">
      <h2 style="text-align: left; margin-left: 5%">{{currentChapter.title}}</h2>
      <div style="display: flex; flex-direction: row;">
        <div style="margin-left: 5%">{{currentChapter.founder}}</div>
        <div style="margin-left: 5%; color: #8c939d">{{ formatDate(currentChapter.uploadTime) }}</div>
      </div>

      <div v-if="currentChapter.authority === '视频学习'" style="width: 90%;margin-left: 5%; margin-top: 2vh">
        <video
            class="video"
            :src="currentChapter.chapterVideo"
            controls
            style="width: 100%; height: auto;"
            @play="handleVideoPlay($event)"
            @pause="handleVideoPause($event)"
            @ended="handleVideoEnd($event)"
            @error="handleVideoError($event)"
        >
          您的浏览器不支持视频播放。
        </video>
      </div>
      <div v-if="currentChapter.authority === '链接学习'" style="width: 90%;margin-left: 5%; margin-top: 2vh">
        <a :href="currentChapter.content" target="_blank" >{{currentChapter.content}}</a>
      </div>
      <div v-else style="width: 90%;margin-left: 5%; margin-top: 2vh" v-html="currentChapter.content" class="auditContent"></div>
      <div v-if="currentChapter.authority === '链接学习'" style="width: 90%;margin-left: 5%; margin-top: 2vh">
        此章节为链接学习，将在小程序中跳转链接进行内容学习，观看5分钟之后，自动记录学习内容
      </div>
      <div style="height: 3vh"></div>
    </el-drawer>

  </div>
</template>

<script>
import { getClasses, addClass, deleteClass, changeClassTime } from '@/api/class'
import { getChapterByClassId, deleteChapter, changeChapterTime } from "@/api/chapter";
import { formatDate } from "@/utils/formatDate";
export default {
  data() {
    return {
      classes: [],
      filteredClasses: [],
      searchQuery: "",
      currentPage: 1,
      pageSize: 13,
      totalMembers: 0,

      // 对话框相关
      addClassDialogVisible: false,
      editClassDialogVisible: false,
      editChapterDialogVisible: false,

      checkChapterDrawer: false,

      newClass: {
        class_portrait: "",
        class_title: "",
        department: "",
        authority: "",
        founder_id: "",
        founder: "",
      },

      imageUrl: '',

      expandedData: {},

      currentClass: '',

      currentChapter: '',

    };
  },
  mounted() {
    getClasses().then(res => {
      console.log(res)
      this.classes = res.data.map(class_ => {
        const classAuthority = class_.authority === 0 ? '党员学习' : '积极分子学习';
        return {
          ...class_,
          classAuthority: classAuthority ? classAuthority : '党员学习'
        };
      });
      this.totalMembers = res.data.length;
      this.updateTableData()
    });
  },
  methods: {
    formatDate,
    updateTableData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.filteredClasses = this.classes.slice(start, end);
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      this.updateTableData();
    },
    handleSearch() {
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        this.filteredClasses = this.classes.filter(class_ =>
            class_.classTitle.toLowerCase().includes(query)
        );
      } else {
        this.updateTableData();
      }
    },
    // 添加课程
    async addCLass() {
      try { await this.$confirm('确认提交当前操作？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      if (this.newClass.class_portrait === "" || this.newClass.class_title === "" || this.newClass.department === "" || this.newClass.authority === "") {
        this.$message.error("请填写完整信息");
        return;
      }
      const user = this.$store.getters.getUser
      this.newClass.founder_id = user.id;
      this.newClass.founder = user.name;
      this.newClass.authority = this.newClass.authority === '党员学习' ? 0 : 1;
      console.log(this.newClass)
      addClass(this.newClass).then((res) => {
        this.addClassDialogVisible = false;
        console.log(res)
        this.$message.success(res.data)
        location.reload()
      });
    },
    // 取消添加
    cancelAdd(){
      this.addClassDialogVisible = false
      this.newClass = {
        class_portrait: "",
        class_title: "",
        department: "",
        authority: "",
        founder_id: "",
        founder: "",
      }
    },
    editClass(row) {
      this.editClassDialogVisible = true
      // 浅拷贝
      this.currentClass = Object.assign({}, row);
      this.currentClass.uploadTime = new Date(this.currentClass.uploadTime)
    },
    async updateClass(){
      try { await this.$confirm('确认保存当前修改？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      const data = {
        timestamp: new Date(this.currentClass.uploadTime).toISOString(),
        class_id: this.currentClass.classId,
      }
      changeClassTime(data).then((res)=>{
        console.log(res.data)
        this.editClassDialogVisible = false
        this.$message.success("修改成功")
        location.reload()
      })
    },
    editChapter(row){
      this.editChapterDialogVisible = true
      // 浅拷贝

      this.currentChapter = Object.assign({}, row);
      console.log(this.currentChapter)
      this.currentChapter.uploadTime = new Date(this.currentChapter.uploadTime)
    },
    async updateChapter(){
      try { await this.$confirm('确认保存当前修改？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      const data = {
        timestamp: new Date(this.currentChapter.uploadTime).toISOString(),
        chapter_id: this.currentChapter.chapterId,
      }
      changeChapterTime(data).then((res)=>{
        console.log(res.data)
        this.editChapterDialogVisible = false
        this.$message.success("修改成功")
        location.reload()
      })
    },
    checkChapter(row){
      this.checkChapterDrawer = true
      this.currentChapter = row
    },
    // 删除课程
    deleteCLass(row) {
      console.log(row)
      this.$confirm('此操作将永久删除该课程, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认删除
        const data = {
          class_id: row.classId
        };
        deleteClass(data).then((res) => {
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
      // 定义图片文件的类型
      const imageTypes = ['image/jpeg', 'image/png', 'image/gif'];
      // 校验文件类型
      const isImage = imageTypes.includes(file.type);
      if (!isImage) {
        this.$message.error('只能上传图片文件（jpeg, png, gif）！');
        return false;
      }
      return isImage;
    },
    httpRequest(item) {
      // var imgId = item.file.lastModified
      console.log(item.file);
      this.newClass.class_portrait = item.file
      this.imageUrl = URL.createObjectURL(item.file)
      console.log(this.imageUrl)
    },
    handleUploadSuccess(response, file, fileList) {
      this.$message.success('文件上传成功');
      console.log('文件上传成功')
    },
    handleUploadError(err, file, fileList) {
      // 处理上传失败后的逻辑
      this.$message.error('文件上传失败，请重试');
      console.error('上传错误', err);
    },
    handleUploadRemove(file, fileList){
      this.newClass.class_portrait = ''
      this.imageUrl = ''
    },
    //
    expandChange(row, expandedRows){
      const data = {
        class_id: row.classId,
      }
      getChapterByClassId(data).then((res) => {
        console.log(res.data)
        if (!this.expandedData[data.class_id]) {
          this.$set(this.expandedData, data.class_id, {}); // 使用 $set 确保响应性
        }
        this.expandedData[data.class_id] = res.data.map(chapter => {
          const learningTypeMap = {
            1: '图文学习',
            2: '视频学习',
            3: '链接学习'
          };
          return {
            ...chapter,
            authority: learningTypeMap[chapter.type] || '未知类型' // 如果 type 不是 1, 2, 3 中的一个，可以返回 '未知类型'
          };
        });
        console.log(this.expandedData)
      });
    },
    getExpandedData(row) {
      return this.expandedData[row.classId] ? this.expandedData[row.classId] : null;
    },
    deleteChapter(row){
      this.$confirm('此操作将永久删除该章节, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认删除
        const data = {
          chapter_id: row.chapterId
        };
        deleteChapter(data).then((res) => {
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
    handleVideoPlay(){
      console.log('视频播放')
    },
    handleVideoPause(){
      console.log('视频暂停')
    },
    handleVideoError(){
      console.log('视频错误')
    },
    handleVideoEnd(){
      console.log('视频结束')
    },
  }
};
</script>

<style>
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
.avatar-uploader .el-upload{
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
  color: black;
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
