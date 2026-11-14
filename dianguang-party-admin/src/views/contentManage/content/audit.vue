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

<!--    <el-dialog title="待审核文件" :visible.sync="checkArticleDialogVisible">-->

<!--      <div v-html="currentArticle.content" class="auditContent"></div>-->
<!--    </el-dialog>-->

    <el-drawer
        title="待审核文件"
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
      <div style="display: flex; justify-content: space-between; margin-top: 1vh">
        <el-button type="primary" style="margin-left: 15%" @click="pass">审核通过</el-button>
        <el-button type="danger" style="margin-right: 15%" @click="reject">驳回文件</el-button>
      </div>
      <div style="height: 3vh"></div>
    </el-drawer>

  </div>
</template>
<script>
import {deleteArticle, queryArticleNeedReview} from "@/api/article";
import  { uploadReviewState } from '@/api/articleReview'
import { formatDate } from "@/utils/formatDate";
import {deleteUser} from "@/api/user";
import Cookie from "js-cookie";
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
      checkArticleDialogVisible: false,
      checkArticleDrawer: false,
      currentArticle: '',
    }
  },
  mounted() {
    queryArticleNeedReview().then((res)=>{
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
    checkArticle(row){
      this.checkArticleDrawer = true
      this.currentArticle = row
    },
    pass(){
      this.$confirm('确认审核通过嘛?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const user = this.$store.getters.getUser;
        // 确认删除
        const data = {
          article_id: this.currentArticle.articleId,
          reviewer: user.name,
          reviewer_id: user.id,
          review_result: 1,
        };
        console.log(data);
        uploadReviewState(data).then((res) => {
          console.log(res);
          this.$message.success('审核成功');
          this.checkArticleDrawer = false
          location.reload();
        }).catch(error => {
          console.error('审核失败', error);
          this.$message.error('审核失败');
        });
      }).catch(() => {
        // 取消删除
        this.$message({
          type: 'info',
          message: '已取消审核'
        });
      });
    },
    reject(){
      this.$confirm('确认驳回文件嘛?', '提示', {
        confirmButtonText: '驳回',
        cancelButtonText: '取消',
        type: 'warning',
        inputPlaceholder: '请输入驳回原因',
        inputPattern: /.+/,  // 正则表达式，确保输入非空
        inputErrorMessage: '驳回原因不能为空',
        showInput: true,  // 启用输入框
      }).then((inputValue) => {
        const user = this.$store.getters.getUser;
        if (inputValue.value === null){
          this.$message.warning('驳回原因不能为空');
        }
        // 确认驳回
        const data = {
          article_id: this.currentArticle.articleId,
          reviewer: user.name,
          reviewer_id: user.id,
          review_result: 2,
          advice: inputValue.value  // 添加驳回原因
        };
        console.log(data);
        uploadReviewState(data).then((res) => {
          console.log(res);
          this.$message.success('驳回成功');
          this.checkArticleDrawer = false;
          location.reload();
        }).catch(error => {
          console.error('驳回失败', error);
          this.$message.error('驳回失败');
        });
      }).catch(() => {
        // 取消驳回
        this.$message({
          type: 'info',
          message: '已取消驳回'
        });
      });
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
.video{
  width: 100%;
  height: auto;
}
/* 让 el-dialog 中的图片适应对话框宽度 */
.auditContent{
  ::v-deep img{
    max-width: 100%;
    height: auto;
  }
}
</style>