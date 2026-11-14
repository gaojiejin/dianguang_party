export function accountKey(suffix){return 'dg:'+uni.getStorageSync('job_id')+':'+suffix}
export function readLocal(key,fallback){const value=uni.getStorageSync(accountKey(key));return value===''||value===undefined?fallback:value}
export function saveLocal(key,value){uni.setStorageSync(accountKey(key),value)}
export function clearSession(){['token','job_id','role','phone','user'].forEach(key=>uni.removeStorageSync(key))}
export function decodeLegacy(value,fallback={}){try{return value?JSON.parse(decodeURIComponent(value)):fallback}catch(e){return fallback}}
