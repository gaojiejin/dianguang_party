const BASE = 'http://127.0.0.1:8080/';
export const getBase_url = () => BASE;
let redirecting = false;
export default function request(options) {
 const method=(options.method||'GET').toUpperCase();
 return new Promise((resolve,reject)=>uni.request({url:BASE+options.url,method,data:options.data||options.params||{},timeout:20000,
 header:{'Content-Type':'application/x-www-form-urlencoded','Authorization':uni.getStorageSync('token')||'',...(options.header||options.headers||{})},
 success(res){
  if(res.statusCode>=200&&res.statusCode<300){
   if(res.data&&typeof res.data==='object'&&res.data.success===false){const err=new Error(res.data.message||res.data.msg||'操作未成功');if(!options.silent)uni.showToast({title:err.message,icon:'none'});return reject(err)}
   return resolve(res.data);
  }
  const detail=typeof res.data==='string'?res.data:res.data&&(res.data.message||res.data.error);
  const error=new Error(res.statusCode===401?'登录已失效，请重新登录':detail||'请求失败，请重试');error.status=res.statusCode;
  if(res.statusCode===401&&!redirecting){redirecting=true;['token','job_id','role','phone','user'].forEach(key=>uni.removeStorageSync(key));uni.reLaunch({url:'/pages/login/login',complete:()=>{redirecting=false}})}
  if(!options.silent)uni.showToast({title:error.message.slice(0,40),icon:'none'});reject(error);
 },fail(){const error=new Error('网络连接失败，请检查网络后重试');if(!options.silent)uni.showToast({title:error.message,icon:'none'});reject(error)}
 }));
}
