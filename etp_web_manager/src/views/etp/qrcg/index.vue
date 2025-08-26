<template>
  <div class="content_box">
    <el-row>
      <el-col :span="11" class="content_left">
        <div class="content_left_b" ref="htmlToImage">
          <div class="content_left_b_title">
            <div class="title_box">
              <div class="title">
                <span>邀请你参加</span>
              </div>
              <div class="name">
                <span class="text-mc" :title="dataObj.title">{{ dataObj.mc }}</span>
              </div>
            </div>
          </div>
<!--          <div class="content_left_b_round content_left_b_round1"/>-->
<!--          <div class="content_left_b_round content_left_b_round2"/>-->
          <div class="content_left_c">
            <div class="content_foot">
              <img src="../../../assets/etp/qrcg.png">
            </div>
            <div style="float: left;margin-left: 16%;">
              <svg t="1647509123973" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="10787" width="60" height="60"><path d="M945.08544 366.6432l-425.97376-183.1936a20.5312 20.5312 0 0 0-8.12544-1.6896c-2.7648 0-5.52448 0.5632-8.14592 1.6896L76.87168 366.6432c-8.98048 3.84512-14.92992 13.65504-14.92992 24.59136 0 10.9568 5.94944 20.74112 14.92992 24.60672L502.8352 599.04a20.29056 20.29056 0 0 0 16.27136 0l331.1872-142.43328v145.7152c-17.75104 9.40032-30.08512 29.8496-30.08512 53.62688 0 23.7824 12.33408 44.22656 30.08512 53.62688v50.83136h43.6224v-50.82112c17.76128-9.40032 30.1056-29.8496 30.1056-53.63712s-12.34432-44.2368-30.1056-53.63712V437.84704l51.16416-22.00576c8.97536-3.8656 14.9248-13.65504 14.9248-24.60672 0-10.93632-5.94944-20.74624-14.9248-24.59136z m-434.0992 269.16352a47.96928 47.96928 0 0 1-19.03104-3.95776L226.4064 517.632v157.70112c0 129.16224 193.65888 167.936 254.208 167.936h60.69248c45.35808 0 254.22848-38.77376 254.22848-167.936v-157.696l-265.55904 114.21184a47.76448 47.76448 0 0 1-18.9952 3.95776z" fill="#077eff" p-id="10788" data-spm-anchor-id="a313x.7781069.0.i18" class="selected"></path></svg>
            </div>
            <div class="content_left_c_log">
              <a>专家考试平台</a>
            </div>
            <div class="time">
              <div>
                <span v-if="dataObj.startTime!=null">开始时间: {{ dataObj.startTime }}</span>
              </div>
              <div>
                <span v-if="dataObj.endTime!=null">结束时间: {{ dataObj.endTime }}</span>
              </div>
            </div>
            <div class="code">
              <div class="code_box">
                <qrcode-vue
                  v-bind:value="qrcode.value"
                  :size="qrcode.size"
                  :render-as="qrcode.render"
                  :foreground="qrcode.foreground"
                  :background="qrcode.background"
                  :level="qrcode.level"
                />
              </div>
              <div class="title"><span>识别二维码参加</span></div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="13" class="content_right">
        <div class="title">链接</div>
        <div class="address">
          <div class="title">地址</div>
          <div class="content">
            <span>{{ qrcode.value }}</span>
            <span>
              <el-button type="text" @click="copyLink(qrcode.value)">
                <i class="el-icon-document-copy"/> 复制链接</el-button>
            </span>
          </div>
<!--          <div class="title">发送通知</div>-->
<!--          <el-radio-group v-model="dxArr" style="margin-left: 6%;margin-top: 20px;">-->
<!--            <el-radio label="1">邮件</el-radio>-->
<!--            <el-radio label="2">短信</el-radio>-->
<!--            <el-radio label="3">微信公众号</el-radio>-->
<!--          </el-radio-group>-->
<!--          <div class="title">通知时间</div>-->
<!--          <el-select v-model="tzsj" placeholder="请选择" size="mini" style="width: 20%; margin-left: 6%;margin-top: 20px">-->
<!--            <el-option-->
<!--              v-for="item in options"-->
<!--              :key="item.value"-->
<!--              :label="item.label"-->
<!--              :value="item.value">-->
<!--            </el-option>-->
<!--          </el-select>-->
        </div>
        <div class="button">
          <el-button size="small" type="primary" plain @click="savePoster">下载图片</el-button>
<!--          <el-button size="small" type="primary" plain @click="">考一下</el-button>-->
          <el-button size="small" type="primary" @click="tzLog()" style="margin-left: 70px">确定</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import QrcodeVue from 'qrcode.vue';
import html2canvas from "html2canvas";
import Canvas2Image from "@/utils/canvas2image";

export default {
  name: "index",
  props: {  //组件属性
    data: {
      type: [Object],
      required: true,
    }
  },
  components: {
    QrcodeVue
  },
  data() {
    return {
      dataObj: {
        startTime: null,
        endTime: null,
        title: '课程',
        mc: '',
      },
      qrcode: {
        value: 'http://10.153.3.12/login',
        size: 200,
        render: 'canvas',
        foreground: '#000000',
        background: '#ffffff',
        level: 'L',
      },
      dxArr: '',
      tzsj: '立即',
    }
  },
  methods: {
    /** 通知提示 */
    tzLog(){
      if(this.dxArr == '1') this.$message.success("邮件通知已推送！")
      if(this.dxArr == '2') this.$message.success("短信通知已推送！")
      if(this.dxArr == '3') this.$message.success("微信公众号通知已推送！")
      this.$emit('closeQrcg');
    },

    submit() {
      //确定的回调
      this.$emit('submit');
    },
    savePoster() {
      //TODO 插件不支持css clip-path, 后边需要将蓝色框下的 三角换成图片！
      //生成图片之前，需先将整体的滚动条隐藏掉，否则生成出来的照片会偏移
      document.getElementById('app').style.overflowY = 'hidden'
      let shareContent = this.$refs.htmlToImage;
      let width = shareContent.offsetWidth;
      let height = shareContent.offsetHeight;
      let canvas = document.createElement("canvas");
      let scale = 5; //这里为了下载出更清晰的图片，值越大越清晰
      canvas.width = width * scale;
      canvas.height = height * scale;
      canvas.getContext("2d").scale(scale, scale);

      const opts = {
        scale: 1,
        canvas: canvas,
        logging: true,
        width: width,
        height: height,
        useCORS: true
      };
      let downloadName = '下载'
      html2canvas(shareContent, opts).then(canvas => {
        Canvas2Image.saveAsImage(canvas, canvas.width + '', canvas.height, 'png', downloadName);
        document.getElementById('app').style.overflowY = 'scroll';
      })
    },
    copyLink(link) {
      this.$copyText(link).then( _ => {
        this.$message.success("复制成功！");
      }, _ => {
        this.$message.success("复制失败，该浏览器不支持复制！");
      })
    }
  },
  watch: {
    data: {
      handler(val) {
        console.log(val)
        //title 标题
        //value 二维码链接
        //startTime 开始时间
        //endTime 结束时间
        //size 二维码大小整数
        const {title, value, size, startTime, endTime} = val;
        this.dataObj.startTime = startTime;
        this.dataObj.endTime = endTime;
        let arr = title.split("");
        let mc = "";
        if(arr.length>15){
          arr = arr.slice(0,14);
          for (let i = 0; i < arr.length; i++) {
            mc+=arr[i].toString();
          }
          console.log(mc);
          this.dataObj.mc = mc+"...";
        }else{
          this.dataObj.mc = title;
        }
        this.dataObj.title = title;
        this.qrcode.value = value;
        this.qrcode.size = size;
      },
      deep: true,
      immediate: true
    }
  }
}
</script>

<style scoped lang="scss">
$bg_color: rgba(221, 223, 229, 1);
$font_color: rgba(110, 113, 123, 1);

.content_box {
  position: absolute;
  width: 80%;
  height: 80%;
  margin-left:10%;

  ::v-deep {
    & > div {
      height: 100%;

      .content_left {
        height: 100%;
        background-color: $bg_color;
        display: flex;
        border-radius: 10px;

        .content_left_b {
          background-color: $bg_color;
          margin: auto;
          width: 88%;
          height: 95%;
          display: flex;
          position: relative;

          .content_left_c {
            margin: auto;
            width: 88%;
            background-color: rgba(255, 255, 255, 1);
            height: 100%;
            border-radius: 1.5%;

            .content_foot {
              width: 102%;
              position: absolute;
              top: 65%;
              left: -3px;
              > img {
                width: 100%;
                height: 180px;
              }
            }

            .content_left_c_log {
              width: 100%;
              height: 2%;
              margin-left: 31%;
              font-size: 180%;
              margin-top: 5%;
            }

            .time {
              height: 10%;
              margin-top: 120px;
              display: flex;
              flex-direction: column;

              > div {
                background-color: #ebf1ff;
                width: 80%;
                margin: auto;
                display: flex;
                flex-direction: column;

                > span {
                  margin: auto;
                  color: $font_color;
                }
              }
            }

            .code {
              height: 40%;
              margin-top: 4%;

              .code_box {
                height: 80%;
                display: flex;

                > * {
                  margin: auto;
                }
              }

              .title {
                height: 20%;
                display: flex;
                //margin-top: 20%;

                > span {
                  display: inline-block;
                  color: $font_color;
                  width: max-content;
                  margin: auto;
                }
              }
            }
          }



          .content_left_b_title {
            width: 100%;
            height: 20%;
            position: absolute;
            top: 10%;

            .title_box {
              width: 100%;
              height: 100%;
              position: relative;

              .title {
                height: 20%;
                display: flex;

                > span {
                  margin: auto;
                  font-size: 1.2vw;
                  color: #027efc;
                  margin-top: 5%;

                  &:before {
                    content: '';
                    position: absolute;
                    height: 2px;
                    top: calc(30% + 1px);
                    width: 10%;
                    left: 18%;
                    background-color: rgba(116, 173, 248, 1);
                  }

                  &::after {
                    content: '';
                    position: absolute;
                    height: 2px;
                    top: calc(30% + 1px);
                    width: 10%;
                    right: 18%;
                    background-color: rgba(116, 173, 248, 1);
                  }
                }
              }

              .name {
                height: 20%;

                > span {
                  display: block;
                  color: #017eff;
                  margin: 40px auto;
                  font-size: 1.2vw;
                  font-weight: bold;
                  width: max-content;
                }
              }
            }
          }

          .content_left_b_triangle {
            position: absolute;
            background-color: rgba(180, 182, 188, 1);
            width: 6%;
            height: 0;
            padding-bottom: 6%;
            clip-path: polygon(
                0 0,
                0 100%,
                100% 100%,
                0 0
            );
            top: 42%;
          }

          .content_left_b_triangle1 {
            left: 0;
            transform: rotate(180deg);
          }

          .content_left_b_triangle2 {
            right: 0;
            transform: rotate(90deg);
          }

          .content_left_b_round {
            position: absolute;
            border-radius: 100%;
            background-color: $bg_color;
            width: 6%;
            height: 0;
            padding-bottom: 6%;
            top: 55%;
          }

          .content_left_b_round1 {
            left: 3%;
          }

          .content_left_b_round2 {
            right: 3%;
          }
        }
      }

      .content_right {
        height: 100%;

        > .title {
          height: 5%;
          color: rgba(59, 62, 79, 1);
          font-size: 1.5vw;
          padding-left: 3%;
          text-align: left;
        }

        .address {
          height: 68%;

          .title {
            color: rgba(59, 62, 79, 1);
            font-size: 1.3vw;
            padding-left: 6%;
            padding-top:  5%;
            font-weight: bold;
            text-align: left;
            height: 8%;
          }

          .content {
            height: 20%;
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;

            > span {
              height: 100%;
              display: inline-block;
              margin-top: 35px;

              &:nth-child(1) {
                padding: 0 6%;
                word-wrap: break-word;
                white-space: normal;
                width: 80%;
              }

              &:nth-child(2) {
                width: 20%;
              }
            }
          }
        }

        .button {
          //display: flex;
          //flex-direction: row;
          //justify-content: space-around;
          margin-left: 51%;
          margin-top: 8%;
        }
      }
    }
  }
}

.text-mc{
  width: 235px;
  overflow: hidden;
  display: block;
  font-weight: lighter;
  font-size: x-large;
  margin-left: 10px;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
