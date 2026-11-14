<template><div class="chapter-studio content-studio"><div class="chapter-guide"><div><span class="eyebrow">CONTENT PUBLISHING</span><h2>传递组织声音，记录奋进足迹</h2><p>完善发布信息，编写正文，核对无误后提交审核。</p></div><span><i class="el-icon-edit-outline"/> 内容创作</span></div><el-form class="chapter-layout" label-position="top"><el-card class="chapter-settings"><div slot="header"><span class="section-kicker">01 / BASIC INFORMATION</span><h3>发布信息</h3></div><el-form-item label="内容标题 · 必填"><el-input v-model="title" placeholder="请输入清晰、准确的内容标题"/></el-form-item><el-form-item label="内容分类"><el-select v-model="option" placeholder="请选择分类"><el-option v-for="item in options" :key="item" :label="item" :value="item"/></el-select></el-form-item><el-form-item v-if="option === '微课堂展示'" label="课堂视频"><el-upload action="" :before-upload="beforeUploadVideo" :http-request="httpRequestVideo" :on-remove="handleUploadRemoveVideo" :on-error="handleVideoUploadError"><el-button plain icon="el-icon-upload2">选择视频</el-button><div slot="tip" class="el-upload__tip">选择视频文件，提交时一并上传。</div></el-upload></el-form-item><el-form-item v-if="option === '链接展示'" label="展示链接"><el-input v-model="webUrl" placeholder="https://"/></el-form-item><div class="publishing-note"><i class="el-icon-info"/><strong>发布小提示</strong><p>请检查内容分类、正文排版与媒体文件，确认后提交审核。</p></div></el-card><el-card class="chapter-writing"><div slot="header"><span class="section-kicker">02 / CONTENT EDITOR</span><h3>正文编辑</h3><p>使用下方工具设置标题、段落、图片与链接。</p></div><div class="chapter-editor"><quill-editor v-model="content" ref="myQuillEditor" :options="editorOption" @blur="onEditorBlur($event)" @focus="onEditorFocus($event)" @change="onEditorChange($event)" @ready="onEditorReady($event)"/></div><div class="chapter-actionbar"><span><i class="el-icon-info"/> 提交前请核对内容分类及正文</span><el-button type="primary" icon="el-icon-check" @click="submitContent">提交审核</el-button></div></el-card></el-form></div></template>

<script>
import { Quill } from 'vue-quill-editor'; // 确保 quill-editor 导入正确
import  ImageResize  from 'quill-image-resize-module';
Quill.register('modules/imageResize', ImageResize);
import { upload } from '@/api/class'
import { createArticle} from '@/api/article'
export default {
  name: "HomeView",
  data() {
    return {
      title: "",
      options: ["支部动态", "时政要闻", "微课堂展示", "链接展示"],
      option: "支部动态",
      content: "",
      images: [],
      timestamp: new Date(),
      video: '',
      videoUrl: '',
      webUrl: '',
      editorOption: {
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
    onEditorChange ({quill, html, text}) {
      this.content = html;
      console.log(this.content)
    },
    async submitContent() {
      try { await this.$confirm('确认提交当前内容？请核对填写的信息。', '操作确认', { confirmButtonText: '确认提交', cancelButtonText: '返回检查', type: 'warning', closeOnClickModal: false }); } catch (_) { this.$message.info('已取消操作'); return; }

      if (this.title === "") {
        this.$message.error("标题不能为空");
        return;
      }
      if (this.option === '链接展示'){
        const user = this.$store.getters.getUser;
        const formData = new FormData();
        formData.append('title', this.title);
        formData.append('type', this.option);
        formData.append('content', this.webUrl);
        formData.append('editor', user.name);
        formData.append('editorId', user.id);
        formData.append('imagesPath', []); // 添加每个图像路径
        createArticle(formData).then((res) => {
          console.log(res)
          this.$message.success('内容上传成功')
          location.reload()
        });
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
              formData.append('type', this.option);
              formData.append('content', this.content);
              this.images.forEach((image) => {
                formData.append('imagesPath', image); // 添加每个图像路径
              });
              formData.append('editor', user.name);
              formData.append('editorId', user.id);

              if (this.option === '微课堂展示'){
                const formDataVideo = new FormData();
                formDataVideo.append('url', this.video);
                upload(formDataVideo).then((res) => {
                  this.videoUrl = res.data;
                  formData.append('videoPath', this.videoUrl);
                  console.log(formData)
                  createArticle(formData).then((res) => {
                    console.log(res)
                    this.$message.success('内容上传成功')
                    location.reload()
                  });
                })
              }
              else{
                createArticle(formData).then((res) => {
                  console.log(res)
                  this.$message.success('内容上传成功')
                  location.reload()
                });
              }
            })
            .catch((error) => {
              console.error("有文件上传失败:", error);
            });
      } else {
        const user = this.$store.getters.getUser;
        const formData = new FormData();
        formData.append('title', this.title);
        formData.append('type', this.option);
        formData.append('content', this.content);
        formData.append('editor', user.name);
        formData.append('editorId', user.id);
        formData.append('imagesPath', []); // 添加每个图像路径

        if (this.option === '微课堂展示'){
          const formDataVideo = new FormData();
          formDataVideo.append('url', this.video);
          upload(formDataVideo).then((res) => {
            this.videoUrl = res.data;
            formData.append('videoPath', this.videoUrl);
            console.log(formData)
            createArticle(formData).then((res) => {
              console.log(res)
              this.$message.success('内容上传成功')
              location.reload()
            });
          })
        }
        else{
          createArticle(formData).then((res) => {
            console.log(res)
            this.$message.success('内容上传成功')
            location.reload()
          });
        }
      }
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

  },
};
</script>



