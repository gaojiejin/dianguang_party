<template>
<div class="chapter-studio"><div class="chapter-guide"><div><span class="eyebrow">CHAPTER PUBLISHING</span><h2>编写一节有温度的党课</h2><p>完善章节信息，编写学习内容，核对无误后提交审核。</p></div><span><i class="el-icon-edit-outline"/> 章节创作</span></div><el-form class="chapter-layout" label-position="top"><el-card class="chapter-settings"><div slot="header"><span class="section-kicker">01 / BASIC INFORMATION</span><h3>章节信息</h3></div><el-form-item label="章节标题 · 必填"><el-input v-model="title" placeholder="为本节课程拟一个清晰的标题"/></el-form-item><el-form-item label="所属课程 · 必选"><el-select v-model="course" placeholder="请选择所属课程"><el-option v-for="item in courseClass" :key="item.classId" :label="item.classTitle" :value="item.classTitle"/></el-select></el-form-item><el-form-item label="学习形式"><el-select v-model="learnClass"><el-option label="图文学习" value="图文学习"/><el-option label="视频学习" value="视频学习"/><el-option label="链接学习" value="链接学习"/></el-select></el-form-item><el-form-item v-if="learnClass === '视频学习'" label="课程视频 · 必填"><el-upload action="" :before-upload="beforeUploadVideo" :http-request="httpRequestVideo" :on-remove="handleUploadRemoveVideo" :on-error="handleVideoUploadError"><el-button plain icon="el-icon-upload2">选择视频</el-button><div slot="tip" class="el-upload__tip">上传课程视频后，将自动读取视频时长。</div></el-upload></el-form-item><template v-if="learnClass === '链接学习'"><el-form-item label="学习链接 · 必填"><el-input v-model="webUrl" placeholder="https://"/></el-form-item><el-form-item label="学习时间 · 必填"><el-input v-model="studyTime" type="number" placeholder="请输入学习时长"/></el-form-item></template><el-form-item label="章节封面 · 必填"><el-upload class="chapter-cover" action="" :limit="1" :before-upload="beforeUpload" :on-success="handleUploadSuccess" :on-error="handleUploadError" :on-remove="handleUploadRemove" :http-request="httpRequest"><img v-if="imageUrl" :src="imageUrl" alt="章节封面预览"/><div v-else><i class="el-icon-picture-outline"/><strong>上传章节封面</strong><span>支持 JPG、PNG、GIF</span></div></el-upload></el-form-item></el-card><el-card class="chapter-writing"><div slot="header"><span class="section-kicker">02 / LEARNING CONTENT</span><h3>正文编辑</h3><p>使用下方工具设置标题、段落、图片与链接。</p></div><div class="chapter-editor"><quill-editor v-model="content" ref="myQuillEditor" :options="editorOption" @blur="onEditorBlur($event)" @focus="onEditorFocus($event)" @change="onEditorChange($event)" @ready="onEditorReady($event)"/></div><div class="chapter-actionbar"><span><i class="el-icon-info"/> 提交前请核对课程、封面及正文内容</span><el-button type="primary" icon="el-icon-check" @click="submitContent">提交审核</el-button></div></el-card></el-form></div>
</template>
<script>

import { Quill } from 'vue-quill-editor'; // 确保 quill-editor 导入正确
import ImageResize from 'quill-image-resize-module';
Quill.register('modules/imageResize',ImageResize);

import {getClasses, upload} from '@/api/class'
import {createArticle} from "@/api/article";
import {addChapter} from '@/api/chapter'

export default {
  name: "HomeView",
  data() {
    return {
      title: "",
      learnClass: "图文学习",
      course: '',
      courseClass: [],
      portrait: '',
      video: '',
      studyTime: '',
      images: [],

      imageUrl: '',
      videoUrl: '',
      duration: '',

      webUrl: '',

      content: "",
      editorOption: {
        placeholder: '从这里开始编写课程正文…',
        modules: {
          toolbar: [
            [{ 'font': [] }, { 'size': [] }],  // 字体和字号
            ['bold', 'italic', 'underline', 'strike'],  // 加粗、斜体、下划线、删除线
            [{ 'color': [] }, { 'background': [] }],  // 字体颜色和背景颜色
            [{ 'script': 'sub' }, { 'script': 'super' }],  // 上标和下标
            [{ 'header': 1 }, { 'header': 2 }, { 'header': 3 }, { 'header': 4 }, { 'header': 5 }, { 'header': 6 }],  // 标题1和标题2
            ['blockquote', 'code-block'],  // 引用和代码块
            [{ 'list': 'ordered'}, { 'list': 'bullet' }],  // 有序列表和无序列表
            [{ 'align': [] }],  // 文本对齐方式
            ['link', 'image', 'video'],  // 超链接、图片和视频
            ['clean']  // 清除格式
          ],
          imageResize: {} // 配置 imageResize
        }
      }
    };
  },
  mounted() {
    this.$refs.myQuillEditor.quill.on('text-change', (delta, oldDelta, source) => {
      console.log('Text change detected', delta);
    });
    getClasses().then((res) => {
      this.courseClass = res.data
    })
  },
  methods: {
    onEditorBlur(quill) {
      console.log("editor blur!", quill);
    },
    onEditorFocus(quill) {
      console.log("editor focus!", quill);
    },
    onEditorReady(quill) {
      console.log("editor ready!", quill);
    },
    onEditorChange({ quill, html, text }) {
      this.content = html;
      console.log(this.content)
    },
    async submitContent(){
      try { await this.$confirm('确认提交当前内容？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      if (this.title === "" || this.course === '' || this.imageUrl === '') {
        this.$message.error("请完善内容");
        return;
      }
      if (this.learnClass === '链接学习' && (!Number.isFinite(Number(this.studyTime)) || Number(this.studyTime) <= 0)) { this.$message.warning('请输入大于 0 的学习时间'); return; }
      if (this.learnClass === "视频学习" && this.videoUrl === '') {
        this.$message.error("请上传视频");
        return;
      }

      if (this.learnClass === "链接学习" && this.webUrl === ''){
        this.$message.error("请输入链接");
        return;
      }

      if (this.learnClass === "链接学习"){
        const user = this.$store.getters.getUser;
        const formData = new FormData();
        formData.append('title', this.title);
        formData.append('content', this.webUrl);
        formData.append('founder', user.name);
        formData.append('founder_id', user.id);
        formData.append('chapterImage', []); // 添加每个图像路径
        const course = this.courseClass.find(item => item.classTitle === this.course);
        formData.append('class_id', course.classId)
        formData.append('type', 3);
        formData.append('duration', Number(this.studyTime))
        const chapter_portrait = new FormData();
        chapter_portrait.append('url', this.portrait)
        upload(chapter_portrait).then((res)=>{
          formData.append('chapter_portrait', res.data)
          addChapter(formData).then((res) => {
            console.log(res)
            this.$message.success('内容上传成功')
            location.reload()
          });
        })
        return;
      }



      const imageUrls = this.extractImageUrls(this.content);

      if (imageUrls.length > 0) {
        const uploadPromises = imageUrls.map((imageUrl, index) => {
          return new Promise((resolve, reject) => {
            // 将 Base64 转换为 Blob
            const blob = this.base64ToBlob(imageUrl);
            console.log(blob)
            const file = new File([blob], `image_${index}.png`, { type: 'image/png' });
            const formData = new FormData();
            formData.append('url', file);
            upload(formData).then((res) => {
              console.log(res.data)
              this.images.push(res.data);
              this.content = this.content.replace(new RegExp(this.escapeRegExp(imageUrl), 'g'), res.data);
              console.log(this.content)
              resolve(res.data);
            }).catch((error) => {
              console.error("上传失败:", error);
              reject(error);
            });
          });
        });

        Promise.all(uploadPromises)
            .then(() => {
              const user = this.$store.getters.getUser;
              const formData = new FormData();
              formData.append('title', this.title);
              formData.append('content', this.content);
              this.images.forEach((image) => {
                formData.append('chapterImage', image); // 添加每个图像路径
              });
              formData.append('founder', user.name);
              formData.append('founder_id', user.id);
              const course = this.courseClass.find(item => item.classTitle === this.course);
              console.log(course)
              formData.append('class_id', course.classId)
              console.log(formData)

              const chapter_portrait = new FormData();
              chapter_portrait.append('url', this.portrait)
              upload(chapter_portrait).then((res)=>{
                formData.append('chapter_portrait', res.data)
                if (this.learnClass === "图文学习") {
                  formData.append('type', 1);
                  formData.append('duration', 300)
                  addChapter(formData).then((res) => {
                    console.log(res)
                    this.$message.success('内容上传成功')
                    location.reload()
                  });
                }
                else {
                  formData.append('type', 2);
                  formData.append('duration', this.duration);
                  const video = new FormData();
                  video.append('url', this.video)
                  upload(this.video).then((res)=>{
                    formData.append('chapterVideo', res.data);
                    addChapter(formData).then((res) => {
                      console.log(res)
                      this.$message.success('内容上传成功')
                      location.reload()
                    });
                  })
                }
              })
            })
            .catch((error) => {
              console.error("有文件上传失败:", error);
            });
      } else {
        const user = this.$store.getters.getUser;
        const formData = new FormData();
        formData.append('title', this.title);
        formData.append('content', this.content);
        formData.append('chapterImage', []); // 添加每个图像路径
        formData.append('founder', user.name);
        formData.append('founder_id', user.id);
        const course = this.courseClass.find(item => item.classTitle === this.course);
        formData.append('class_id', course.classId)

        const chapter_portrait = new FormData();
        chapter_portrait.append('url', this.portrait)
        upload(chapter_portrait).then((res)=>{
          formData.append('chapter_portrait', res.data)
          if (this.learnClass === "图文学习") {
            formData.append('type', 1);
            formData.append('duration', 300)
            addChapter(formData).then((res) => {
              console.log(res)
              this.$message.success('内容上传成功')
              location.reload()
            });
          }
          else {
            formData.append('type', 2);
            formData.append('duration', this.duration);
            const video = new FormData();
            video.append('url', this.video)
            console.log(this.video)
            upload(video).then((res) => {
              console.log('video:', res.data)
              formData.append('chapterVideo', res.data);
              addChapter(formData).then((res) => {
                console.log(res)
                this.$message.success('内容上传成功')
                location.reload()
              });
            })
          }
        })
      }
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
      this.portrait = item.file
      this.imageUrl = URL.createObjectURL(item.file)
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
      this.portrait = ''
      this.imageUrl = ''
    },
    beforeUploadVideo(file){
      // 定义视频文件的类型
      const videoTypes = ['video/mp4', 'video/avi', 'video/mov', 'video/wmv', 'video/flv'];
      // 校验文件类型
      const isVideo = videoTypes.includes(file.type);
      if (!isVideo) {
        this.$message.error('只能上传视频文件（mp4, avi, mov, wmv, flv）！');
        return false;
      }
      return isVideo;
    },
    httpRequestVideo(item){
      this.video = item.file
      this.videoUrl = URL.createObjectURL(item.file)
      // 创建一个视频元素
      const videoElement = document.createElement('video');

      // 设置视频源
      videoElement.src = this.videoUrl;

      // 当视频元数据加载完成后获取时长
      videoElement.addEventListener('loadedmetadata', () => {
        this.duration = Math.floor(videoElement.duration);
        console.log(this.duration)
      });
    },
    handleUploadRemoveVideo(){
      this.video = ''
      this.videoUrl = ''
    },
    handleVideoUploadError(err){
      // 处理上传失败后的逻辑
      this.$message.error('文件上传失败，请重试');
      console.error('上传错误', err);
    },

    base64ToBlob(base64) {
      const byteCharacters = atob(base64.split(',')[1]);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      return new Blob([byteArray], { type: 'image/png' });
    },

    extractImageUrls(html) {
      const base64Regex = /<img[^>]+src="(data:image[^">]+)"/g;
      let match;
      const base64Images = [];
      while ((match = base64Regex.exec(html)) !== null) {
        base64Images.push(match[1]); // Capture only the base64 string
      }
      return base64Images;
    },
    escapeRegExp(string) {
      return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); // $& 表示整个匹配到的字符串
    }
  },
};
</script>



