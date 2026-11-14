-- 本地管理员初始化；保留用户 id、角色及支部关联。
UPDATE user SET job_id='admin', password='admin' WHERE job_id='111';

INSERT INTO user(id,job_id,password,name,role,department) SELECT 1,'admin','admin','演示管理员','超级管理员','电光学院' WHERE NOT EXISTS(SELECT 1 FROM user WHERE job_id='admin');
