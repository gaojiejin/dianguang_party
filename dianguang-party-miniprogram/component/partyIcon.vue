<template>
 <image class="party-icon" :src="source" :style="dimensions" mode="aspectFit" @click="$emit('click',$event)"/>
</template>
<script>
const names=['home','list','person','staff','compose','checkbox','medal','locked','gear','search','right','down','plus','trash','flag','video','eye','dot','logo'];
export default {
 props:{type:{type:String,default:'list'},size:{type:[String,Number],default:24},color:{type:String,default:'#7C8490'}},
 computed:{
  source(){const aliases={'person-filled':'person','home-filled':'home','grid':'list','book':'list','arrowright':'right','arrowdown':'down','plusempty':'plus','checkmarkempty':'checkbox','closeempty':'plus','videocam':'video','weixin':'staff'};const name=aliases[this.type]||this.type;const color=this.color.toLowerCase();const tone=['#fff','#ffffff','white'].includes(color)?'white':['#d4af37'].includes(color)?'gold':/c91010|d81e06|b1273c|df655c|e2544a|ff0000|red/.test(color)?'red':'gray';return '/static/icons/'+(names.includes(name)?name:'list')+'-'+tone+'.png'},
  dimensions(){const value=typeof this.size==='number'||/^\d+(\.\d+)?$/.test(this.size)?this.size+'px':this.size;return 'width:'+value+';height:'+value+';'}
 }
}
</script>
<style>
.party-icon { display: inline-block; width: 24px; height: 24px; flex-shrink: 0; vertical-align: middle; }
</style>
