<template>
  <div class="content_box">
    <el-row>
      <el-col :span="11" class="content_left">
        <div class="content_left_b" ref="htmlToImage">
          <div class="content_left_b_title">
            <div class="title_box">
              <div class="title">
                <span>邀请你参加课程</span>
              </div>
              <div class="name">
                <span>{{ dataObj.title }}</span>
              </div>
            </div>
          </div>
          <div class="content_left_b_triangle content_left_b_triangle1"/>
          <div class="content_left_b_triangle content_left_b_triangle2"/>
          <div class="content_left_b_round content_left_b_round1"/>
          <div class="content_left_b_round content_left_b_round2"/>
          <div class="content_left_c">
            <div class="content_left_c_log">
              考试星
            </div>
            <div class="time">
              <div><span>开始时间: {{ dataObj.startTime }}</span></div>
              <div><span>结束时间: {{ dataObj.endTime }}</span></div>
            </div>
            <div class="code">
              <div class="code_box">
                <qrcode-vue
                  :value="qrcode.value"
                  :size="qrcode.size"
                  :render-as="qrcode.render"
                  :foreground="qrcode.foreground"
                  :background="qrcode.background"
                  :level="qrcode.level"
                />
              </div>
              <div class="title"><span>识别二维码参加课程</span></div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="13" class="content_right">
        <div class="title">课程链接</div>
        <div class="address">
          <div class="title">课程地址</div>
          <div class="content">
            <span>{{ qrcode.value }}</span>
            <span>
              <el-button type="text" @click="copyLink(qrcode.value)">
                <i class="el-icon-document-copy"/> 复制链接</el-button>
            </span>
          </div>
        </div>
        <div class="button">
          <el-button size="medium" @click="savePoster">下载图片</el-button>
          <el-button size="medium" type="primary" @click="submit">确定</el-button>
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
    // Clipboard
  },
  data() {
    return {
      dataObj: {
        startTime: null,
        endTime: null,
        title: null
      },
      qrcode: {
        value: "https://v.kaoshixing.com/exam/pc/course/#/study?courseld=652042&companyld=395758https://v.kaoshixing.com/exam/pc/course/#/study?courseld=652042&companyld=395758https://v.kaoshixing.com/exam/pc/course/#/study?courseld=652042&companyld=395758",
        size: 250,
        render: 'canvas',
        foreground: '#000000',
        background: '#ffffff',
        level: 'L',
      }
    }
  },
  methods: {
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
        //title 标题
        //value 二维码链接
        //startTime 开始时间
        //endTime 结束时间
        //size 二维码大小整数
        const {title, value, size, startTime, endTime} = val;
        this.dataObj.startTime = startTime;
        this.dataObj.endTime = endTime;
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
  width: 100%;
  height: 100%;

  ::v-deep {
    & > div {
      height: 100%;

      .content_left {
        height: 100%;
        background-color: $bg_color;
        display: flex;

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

            .content_left_c_log {
              width: 100%;
              height: 6%;
            }

            .time {
              height: 8%;
              margin-top: 43%;
              display: flex;
              flex-direction: column;

              > div {
                background-color: rgba(247, 248, 250, 1);
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
                height: 70%;
                display: flex;

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
            height: 15%;
            background-color: rgba(66, 138, 246, 1);
            position: absolute;
            top: 17%;

            .title_box {
              width: 100%;
              height: 100%;
              position: relative;

              .title {
                height: 60%;
                display: flex;

                > span {
                  margin: auto;
                  font-size: 1.5vw;
                  color: rgba(150, 192, 250, 1);

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
                height: 40%;

                > span {
                  display: block;
                  color: white;
                  margin: 0 auto;
                  font-size: 1.6vw;
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
          height: 20%;
          color: rgba(59, 62, 79, 1);
          font-size: 1.5vw;
          padding: 4%;
        }

        .address {
          height: 68%;

          .title {
            color: rgba(59, 62, 79, 1);
            font-size: 1.3vw;
            padding: 2% 4%;
            font-weight: bold;
          }

          .content {
            height: 68%;
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;

            > span {
              height: 100%;
              display: inline-block;

              &:nth-child(1) {
                padding: 0 4%;
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
          display: flex;
          flex-direction: row;
          justify-content: space-around;
        }
      }
    }
  }
}
</style>
