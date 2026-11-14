-- Schema only; no historical users, passwords or content. Fresh database only.
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;
CREATE TABLE `article`  (
  `article_id` int NOT NULL AUTO_INCREMENT COMMENT '文章号',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `uploadTime` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章名称',
  `editor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编辑',
  `editor_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编辑学/工号',
  `reviewer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人',
  `reviewer_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人学/工号',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容路径',
  `review_state` int NULL DEFAULT NULL,
  `likes` int NULL DEFAULT NULL COMMENT '点赞量',
  `image_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图片路径',
  `video_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微课堂视频路径',
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

CREATE TABLE `article_review`  (
  `article_id` int NOT NULL COMMENT '审核表id',
  `reviewer_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人id',
  `reviewer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人',
  `review_state` int NULL DEFAULT NULL COMMENT '审核状态',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `advice` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  INDEX `article_review_article_id_index`(`article_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章审核表' ROW_FORMAT = Dynamic;

CREATE TABLE `branch`  (
  `branch_id` int NOT NULL AUTO_INCREMENT COMMENT '支部序号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '支部成立时间',
  `manager_id` int NULL DEFAULT NULL COMMENT '支部管理员序号',
  `branch_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支部名称',
  `branch_dis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '支部描述',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支部所属院系',
  `authority` int NULL DEFAULT NULL COMMENT '课程权限 0党员支部 1积极分子支部',
  PRIMARY KEY (`branch_id`) USING BTREE,
  INDEX `branch_branch_name_index`(`branch_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支部表' ROW_FORMAT = Dynamic;

CREATE TABLE `chapter`  (
  `chapter_id` int NOT NULL AUTO_INCREMENT COMMENT '章节序号',
  `class_id` int NOT NULL COMMENT '所属课程序号',
  `founder_id` int NULL DEFAULT NULL COMMENT '发布人id',
  `founder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布人名字',
  `upload_time` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章标题',
  `type` int NULL DEFAULT NULL COMMENT '类型（1图文2视频）',
  `chapter_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '如果是图文，保存图片路径',
  `chapter_video` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '如果是视频，保存视频路径',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '章节内容',
  `chapter_portrait` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '章节头像',
  `duration` int NULL DEFAULT NULL COMMENT '视频时长',
  PRIMARY KEY (`chapter_id`) USING BTREE,
  INDEX `chapter_class_null_fk`(`class_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '章节表' ROW_FORMAT = Dynamic;

CREATE TABLE `class`  (
  `class_id` int NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `founder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `founder_id` int NULL DEFAULT NULL COMMENT '创建人学/工号',
  `upload_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `content_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容路径',
  `class_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程标题',
  `class_portrait` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程头像',
  `authority` int NULL DEFAULT NULL COMMENT '课程权限 0党员课程 1积极分子课程',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程所属院系',
  PRIMARY KEY (`class_id`, `class_title`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

CREATE TABLE `class_learn_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `class_id` int NULL DEFAULT NULL COMMENT '课程id',
  `if_learned` int NULL DEFAULT NULL COMMENT '课程是否完成',
  `learning_duration` int NULL DEFAULT NULL COMMENT '课程学习时长',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `class_learn_info_class_id_user_id_uindex`(`class_id` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程学习信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `learn_rate`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '学习进度id',
  `user_id` int NULL DEFAULT NULL COMMENT '学习人id',
  `class_id` int NULL DEFAULT NULL COMMENT '学习课程id',
  `chapter_id` int NULL DEFAULT NULL COMMENT '学习章节id',
  `ifLearned` int NULL DEFAULT NULL COMMENT '是否学习完成 0未完成 1完成',
  `learning_duration` int NULL DEFAULT NULL COMMENT '学习时长',
  `learn_date` datetime NULL DEFAULT NULL COMMENT '学习开始时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `learn_rate_user_id_chapter_id_uindex`(`user_id` ASC, `chapter_id` ASC) USING BTREE,
  INDEX `learn_rate_class_id_index`(`class_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `swiper`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '轮播图id',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '轮播图路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '序号',
  `job_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工号/学号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `branch_id` int NULL DEFAULT NULL COMMENT '所属支部id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `post` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位',
  `portrait` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户角色',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '院系',
  `grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_branch_id_index`(`branch_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS=1;
