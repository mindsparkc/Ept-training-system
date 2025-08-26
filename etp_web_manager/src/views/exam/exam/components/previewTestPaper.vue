<template>
<!--试卷预览-->
  <div class="app-container">
    <div class="textBox pre-pad">
      <div style="position: relative;left:15px;height: 40px">
        <div class="pre-desc">
          <span style="margin-left:10px;font-size: 14px">试题详情</span>
        </div>
      </div>

      <div class="subjectBox" style="margin-left: 20px;padding:0 30px 30px">
        <div v-if="dxArr.length > 0" id="dx" style="padding-top: 10px">
          <el-divider content-position="left" >单选题（{{dxArr.length}}）</el-divider>
          <div v-for="(item,index) in dxArr" :key="item.id" style="margin-left: 10px">
            <template v-if="item.xx">
              <span>{{ index + 1 }}、
                <el-tag size="mini" type="success" style="margin-left: 5px;line-height: 17px;">{{levelFormat(item.level)}}</el-tag>

              </span>
              <span class="span-a" style="font-weight: bold" v-html="'（'+item.score+ '分）'+item.tg"></span><br/>
              <span v-for="xx in JSON.parse(item.xx)" style="margin-left: 25px">
                    {{ xx.key }}、{{ xx.value }} <br/>
                  </span>
              <span style="color: green;font-weight: bold;margin-left: 30px;" >
                    正确答案：{{ item.da }}
                  </span>
            </template>
          </div>
        </div>
        <div v-if="fxArr.length > 0" id="fx"  style="padding-top: 10px">
          <el-divider content-position="left">复选题（{{fxArr.length}}）</el-divider>
          <div v-for="(item,index) in fxArr" :key="item.id" style="margin-left: 10px">
            <template v-if="item.xx">
              <span>{{ index + 1 }}、
                <el-tag size="mini" type="success" style="margin-left: 5px;line-height: 17px;">{{levelFormat(item.level)}}</el-tag>

              </span>
              <span class="span-a" style="font-weight: bold" v-html="'（'+item.score+'分）'+item.tg"></span><br/>
              <span v-for="xx in JSON.parse(item.xx)" style="margin-left: 25px">
                    {{ xx.key }}、{{ xx.value }} <br/>
                  </span>
              <span style="margin-left: 25px;color: green;font-weight: bold" >
                    正确答案：{{ item.da }}
                  </span>
            </template>
          </div>
        </div>
        <div v-if="pdArr.length > 0" id="pd"  style="padding-top: 10px">
          <el-divider content-position="left">判断题（{{pdArr.length}}）</el-divider>
          <div v-for="(item,index) in pdArr" :key="item.id" style="margin-left: 10px">
            <span>{{ index + 1 }}、
              <el-tag size="mini" type="success" style="margin-left: 5px;line-height: 17px;">{{levelFormat(item.level)}}</el-tag>

            </span>
            <span style="font-weight: bold">（ {{ item.score}} 分 ）</span><a v-html="item.tg"></a> <br/>
            <span style="margin-left: 25px;color: green;font-weight: bold" >
                  正确答案：{{ item.da=='1'?'对':'错' }}
                </span>
          </div>
        </div>
        <div v-if="tkArr.length > 0" id="tk"  style="padding-top: 10px">
          <el-divider content-position="left">填空题（{{tkArr.length}}）</el-divider>
          <div v-for="(item,index) in tkArr" :key="item.id" style="margin-left: 10px">
            <span>{{ index + 1 }}、
              <el-tag size="mini" type="success" style="margin-left: 5px;line-height: 17px;">{{levelFormat(item.level)}}</el-tag>

            </span>
            <span style="font-weight: bold">（ {{ item.score}} 分 ）</span><a v-html="item.tg"></a> <br/>
            <span style="margin-left: 25px;color: green;font-weight: bold" >
                  正确答案：<span v-for="(val,key) in JSON.parse(item.da)">
                            <span v-for="(v,k) in val">{{v}}</span><a>,</a>
                          </span>
                </span>
          </div>
        </div>
        <div v-if="zgArr.length > 0" id="zg"  style="padding-top: 10px">
          <el-divider content-position="left">问答题（{{zgArr.length}}）</el-divider>
          <div v-for="(item,index) in zgArr" :key="item.id" style="margin-left: 10px">
            <span>{{ index + 1 }}、
              <el-tag size="mini" type="success" style="margin-left: 5px;line-height: 17px;">{{levelFormat(item.level)}}</el-tag>

            </span>
            <span style="font-weight: bold">（ {{ item.score}} 分 ）</span> <a v-html="item.tg"></a><br/>
            <span style="margin-left: 25px;color: green;font-weight: bold" >
                  正确答案：答案言之有理即可
                </span>
          </div>
        </div>

        <div v-if="zgArr.length > 0" id="zg"  style="padding-top: 10px">
          <el-divider content-position="left">组合题（{{zgArr.length}}）</el-divider>
          <div v-for="(item,index) in zgArr" :key="item.id" style="margin-left: 10px">
            <span>{{ index + 1 }}、
              <el-tag size="mini" type="success" style="margin-left: 5px;line-height: 17px;">{{levelFormat(item.level)}}</el-tag>

            </span>
            <span style="font-weight: bold">（ {{ item.score}} 分 ）</span> <a v-html="item.tg"></a><br/>
            <span style="margin-left: 25px;color: green;font-weight: bold" >
                  正确答案：答案言之有理即可
                </span>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import {getSubjectByPaper} from "@/api/etp/testpaper/testpapersubject"
import Testpaper from "@/views/etp/testpaper";
export default {
  name: "previewTestPaper",
  components: {Testpaper},
  data(){
    return{
      subjects:null,

      dxArr:[],
      fxArr:[],
      pdArr:[],
      tkArr:[],
      zgArr:[],
      zhArr:[],
      levelOptions:[]
    }
  },
  computed:{
    total(){
      let total = 0;
      if (this.dxArr.length > 0)
        this.dxArr.forEach(item => {if (item.score) total += item.score;})
      if (this.fxArr.length > 0)
        this.fxArr.forEach(item => {if (item.score) total += item.score;})
      if (this.pdArr.length > 0)
        this.pdArr.forEach(item => {if (item.score) total += item.score;})
      if (this.tkArr.length > 0)
        this.tkArr.forEach(item => {if (item.score) total += item.score;})
      if (this.zgArr.length > 0)
        this.zgArr.forEach(item => {if (item.score) total += item.score;})
      if (this.zhArr.length > 0)
        this.zhArr.forEach(item => {if (item.score) total += item.score;})
      return total;
    }
  },
  mounted() {
    this.getDicts("subject_level").then(response => {
      this.levelOptions = response.data;
    });

    let id = this.$route.params.id;
    if (id !== undefined && id !== '') {
      this.getTestpaper(id)
    }
  },
  methods:{
    // 难易程度：
    levelFormat(level, column) {
      return this.selectDictLabel(this.levelOptions, level);
    },
    async getTestpaper(id){
      // 获取试卷信息
      let txMap = new Map();
      txMap.set('1','单选题')
      txMap.set('2','复选题')
      txMap.set('3','判断题')
      txMap.set('4','填空题')
      txMap.set('5','问答题')
      txMap.set('6','组合题')

      await getSubjectByPaper(id).then(res => {
        if (res.code == '200'){
          this.subjects = res.data
          for (let key in this.subjects){
            switch (key + '') {
              case '1':
                this.dxArr = this.subjects[key + ''];
                break;
              case '2':
                this.fxArr = this.subjects[key + ''];
                break;
              case '3':
                this.pdArr = this.subjects[key + ''];
                break;
              case '4':
                this.tkArr = this.subjects[key + ''];
                break;
              case '5':
                this.zgArr = this.subjects[key + ''];
                break;
              case '6':
                this.zhArr = this.subjects[key + ''];
                break;
              default:break;
            }
          }
        }
      })
    },
  }
}
</script>

<style scoped lang="scss">
.paper{
  height: auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  border-radius: 5px;
  margin-top: 15px;
}
.pre-pad {
  padding: 15px 8px 8px;
}
.textBox{
  border-radius: 5px;
  /*height: 260px;*/
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.pre-desc:before{
  content: "";
  width: 3px;
  height:20px;
  border-radius: 5px;
  position: absolute;
  top: 0;
  left: 0;
  background: blue;
}
.el-empty__image {
  width: 260px;
  margin: 0 auto;
  text-align: center;
}
.desc {
  text-align: center;
  font-size: 18px;
  color: #ccb1ea;
  letter-spacing: 2px;
}
.subjectBox span{
  line-height: 40px;
  font-size: 15px;
}

.el-divider__text.is-left{
  font-size: 18px;
  font-weight: 600;
}
//#dx{
// ::v-deep p{
//    display: inline;
//  }
//}
//p:first-child {
//    display: inline;
//}
::v-deep p:first-child {
  display: inline;
}
/*$(标签).attr("style","display: inline;");*/
</style>
