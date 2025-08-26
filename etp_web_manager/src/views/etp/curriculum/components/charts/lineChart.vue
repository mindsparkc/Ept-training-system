<template>
  <div :id="idName"
       :ref="idName"
       :style="{height:height,width:width}" />
</template>

<script>
import echarts from "echarts";
import resize from '@/views/dashboard/mixins/resize'

export default {
  name: 'lineChart',
  mixins: [resize],
  props: {
    idName: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    xData: {
      type: Array,
      required: true
    },
    yData: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    yData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs[this.idName])
      this.setOptions()
    },
    setOptions() {
      this.chart.setOption({
        title: {
          text: '近7日完成课程人数',
          textStyle:{
            color: "#494747"
          },
          padding: 15
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {

          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: true,
          splitLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            align: "center"
          },
          data: this.xData
        },
        yAxis: {
          type: 'value',
          minInterval:1,
          axisLine: {
            show: false
          },
          splitLine: {
            show: false
          },
          axisTick: {
            show: false
          }
        },
        series: [
          {
            name: '完成人数',
            type: 'line',
            stack: 'Total',
            data: this.yData,
            lineStyle: {
              color: "rgba(0, 89, 255, 1)"
            },
            itemStyle: {
              color: "#6c50f3"
            }
          }
        ]
      })
    },

  }
}
</script>

<style scoped>

</style>
