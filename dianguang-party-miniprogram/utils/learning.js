import request from '@/api/index';
import {accountKey,readLocal,saveLocal} from './session';
const running={};
export function flushReports(){
 const key=accountKey('reports');if(!uni.getStorageSync('token'))return Promise.resolve();if(running[key])return running[key];
 running[key]=(async()=>{let queue=uni.getStorageSync(key)||[];while(queue.length){if(accountKey('reports')!==key)throw new Error('账号已切换');const entry=queue[0];await request({url:'classLearnInfo/addLearnDuration',method:'POST',data:entry,silent:true});queue=(uni.getStorageSync(key)||[]).filter(item=>item.report_id!==entry.report_id);uni.setStorageSync(key,queue)}})().finally(()=>{delete running[key]});return running[key];
}
export function reportLearning(data){
 const queue=readLocal('reports',[]);const existing=data.if_learned===1&&queue.find(item=>item.chapter_id===data.chapter_id&&item.if_learned===1);
 if(!existing){queue.push({...data,report_id:'r_'+Date.now().toString(36)+'_'+Math.random().toString(36).slice(2,12)});saveLocal('reports',queue)}
 return flushReports();
}
export async function chapterData(options){
 const old=options.chapter?JSON.parse(decodeURIComponent(options.chapter)):{};
 const chapter=await request({url:'chapter/getChapterByChapterId',method:'POST',data:{chapter_id:Number(options.chapter_id||old.chapterId)}});
 if(!chapter||!chapter.chapterId)throw new Error('章节不存在');
 const user=uni.getStorageSync('user');if(!user||!user.id)throw new Error('请重新登录');
 const detail=await request({url:'learnRate/getUserLearnDetail',method:'POST',data:{class_id:chapter.classId,user_id:user.id}});
 const index=(detail.chapterList||[]).findIndex(item=>item.chapterId===chapter.chapterId);
 return {chapter,completed:index>=0&&!!detail.chapterIfLearnedList[index],user};
}
export function chapterUrl(courseId,chapter){const path=chapter.type===2?'video/videoLearn':chapter.type===3?'image/linkweb':'image/ImageTextLearn';return '/pages/component/'+path+'?class_id='+courseId+'&chapter_id='+chapter.chapterId}
export function touchChapter(chapter){saveLocal('recent',{courseId:chapter.classId,chapterId:chapter.chapterId,time:Date.now()})}
export function htmlForPhone(html){return (html||'').replace(/<img\b/gi,'<img style="max-width:100%;height:auto" ').replace(/<script\b[^>]*>[\s\S]*?<\/script>/gi,'')}
