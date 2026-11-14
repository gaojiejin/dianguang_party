<template>
  <div>
    <el-card>
      <!-- 表格 -->
      <el-table :data="filteredArticles" border stripe style="width: 100%">
        <el-table-column
            prop="title"
            label="内容标题">
        </el-table-column>

        <el-table-column
            prop="type"
            label="所属类型"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="editor"
            label="编辑人"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="reviewer"
            label="审核人"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            prop="content"
            label="主要内容"
            show-overflow-tooltip>
        </el-table-column>


        <el-table-column
            prop="uploadTime"
            label="上传时间"
            show-overflow-tooltip>
        </el-table-column>

        <el-table-column
            fixed="right"
            label="操作"
            align="center">
          <template slot-scope="scope">
            <el-button @click="deleteARticle(scope.row)" style="color: red" type="text" size="small">删除</el-button>
            <el-button @click="checkArticle(scope.row)" type="text" size="small">查看</el-button>
            <el-button @click="editArticle(scope.row)" type="text" size="small">编辑</el-button>
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

    <el-drawer
        title="已审核文件"
        :visible.sync="checkArticleDrawer"
        direction="rtl">
      <h2 style="text-align: left; margin-left: 5%">{{currentArticle.title}}</h2>
      <div style="display: flex; flex-direction: row;">
        <div style="margin-left: 5%">{{currentArticle.editor}}</div>
        <div style="margin-left: 5%; color: #8c939d">{{ formatDate(currentArticle.uploadTime) }}</div>
      </div>
      <div v-if="currentArticle.type === '微课堂展示'" style="width: 90%;margin-left: 5%; margin-top: 2vh">
        <video
            class="video"
            :src="currentArticle.videoPath"
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
      <div v-if="currentArticle.type === '链接展示'" style="width: 90%;margin-left: 5%; margin-top: 2vh">
        <a :href="currentArticle.content" target="_blank">
          {{currentArticle.content}}
        </a>
       </div>
      <div v-else style="width: 90%;margin-top: 2vh; margin-left: 5%" v-html="currentArticle.content" class="auditContent"></div>
      <div v-if="currentArticle.type === '链接展示'" style="width: 90%;margin-left: 5%; margin-top: 2vh">
        此文章为链接展示
      </div>
      <div style="width: 90%; margin-left: 5%; margin-top: 2vh">
        审核人： {{ currentArticle.reviewer  }}
      </div>
      <div style="height: 3vh"></div>
    </el-drawer>

    <el-dialog title="编辑内容" :visible.sync="editArticleDialogVisible" @cancel="editArticleDialogVisible = false" width="500px">
      <el-form :model="currentArticle" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="内容标题">
              <el-input v-model="currentArticle.title" placeholder="请输入姓名" disabled/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="所属类型">
              <el-input v-model="currentArticle.type" placeholder="请输入学/工号" disabled/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="上传时间">
              <el-date-picker
                v-model="currentArticle.uploadTime"
                type="datetime"
                placeholder="选择上传时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                :default-time="['00:00:00']"
              ></el-date-picker>
            </el-form-item>
          </el-col>

          <div class="dialog-footer">
            <el-button @click="editArticleDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="updateArticle">保存</el-button>
          </div>
        </el-row>
      </el-form>
    </el-dialog>

  </div>
</template>
<script>
import { queryArticlePassed, changeArticleTime, deleteArticle } from "@/api/article";
import { formatDate } from "@/utils/formatDate";
import {deleteUser} from "@/api/user";
export default {
  data(){
    return{
      articles: [],
      filteredArticles: [],
      searchQuery: "",
      currentPage: 1,
      pageSize: 13,
      totalMembers: 0,

      // 对话框相关
      editArticleDialogVisible: false,
      checkArticleDrawer: false,

      currentArticle: '',
    }
  },
  mounted() {
    queryArticlePassed().then((res)=>{
      console.log(res)
      this.articles = res.data
      this.totalMembers = res.data.length;
      this.updateTableData();
    })
  },
  methods: {
    formatDate,
    updateTableData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.filteredArticles = this.articles.slice(start, end);
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      this.updateTableData();
    },
    deleteARticle(row){
      this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认删除
        const data = {
          article_id: row.articleId
        };
        deleteArticle(data).then((res) => {
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
    checkArticle(row){
      this.checkArticleDrawer = true
      this.currentArticle = row
    },
    editArticle(row){
      this.editArticleDialogVisible = true
      // 浅拷贝
      this.currentArticle = Object.assign({}, row);
      this.currentArticle.uploadTime = new Date(this.currentArticle.uploadTime)
    },
    async updateArticle(){
      try { await this.$confirm('确认保存当前修改？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      const data = {
        timestamp: new Date(this.currentArticle.uploadTime).toISOString(),
        article_id: this.currentArticle.articleId,
      }
      changeArticleTime(data).then((res)=>{
        console.log(res.data)
        this.editArticleDialogVisible = false
        this.$message.success("修改成功")
        location.reload()
      })
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
  },
}
</script>
<style scoped lang="less">
.pager-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
/* 让 el-dialog 中的图片适应对话框宽度 */
.auditContent{
  ::v-deep img{
    max-width: 100%;
    height: auto;
  }
}
.dialog-footer {
  text-align: right;
  padding: 10px 20px;
}
</style>