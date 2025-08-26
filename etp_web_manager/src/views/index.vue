<template>
  <div class="app-container home">
    <div style="position: relative">
      <div class="etp-index-left">
        <!--   左侧第一行 会员信息、课程、课件、试题、试卷信息    -->
        <div class="etp-box bgI first-row-left">
          <p>{{ $store.getters.userInfo.dept.deptName }}</p>
          <p>当前登录用户 <span>{{ $store.getters.userInfo.userName }}</span></p>
<!--          <el-button size="mini" @click="renewal">续 费</el-button>-->
        </div>
        <div class="etp-box first-row-right">
          <div class="first-row-right-item">
            <div class="flex_1">
              <div class="etp-icon">
                <svg style="margin-top: calc(50% - 16px)" t="1639967367232" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2015" width="32" height="32">
                  <path d="M240 512h256c8.836 0 16-7.164 16-16s-7.164-16-16-16H240c-8.836 0-16 7.164-16 16s7.164 16 16 16z m384 96H240c-8.836 0-16 7.163-16 16s7.164 16 16 16h384c8.837 0 16-7.163 16-16s-7.163-16-16-16z m272-416h-96v-64c0-17.673-14.327-32-32-32H160c-35.346 0-64 28.654-64 64v704c0 35.347 28.654 64 64 64h736c17.673 0 32-14.327 32-32V224c0-17.673-14.327-32-32-32z m-736-64h592c8.837 0 16 7.164 16 16v48H160c-17.673 0-32-14.327-32-32s14.327-32 32-32z m736.01 752.01c0 8.837-7.164 16-16 16H159.99c-17.673 0-32-14.327-32-32V223.99h0.01v-8.574a63.681 63.681 0 0 0 32 8.583h640v-0.01h80.01c8.836 0 16 7.164 16 16V880.01zM784 352H240c-8.836 0-16 7.164-16 16s7.164 16 16 16h544c8.837 0 16-7.164 16-16s-7.163-16-16-16zM496 736H240c-8.836 0-16 7.163-16 16s7.164 16 16 16h256c8.836 0 16-7.163 16-16s-7.164-16-16-16z" p-id="2016" fill="#1296db"></path>
                </svg>
              </div>
            </div>
            <div class="flex_2">
              <div style="height: 35px">
                <ScrollNumber :value="0" v-show="coursewareFlag" :itemStyle="{fontSize: fontSize + 'px',fontWeight:fontWeight }" ref="course"/>
                <div v-if="!coursewareFlag" style="font-size: 21px;font-weight: bold;">{{ courseTotal }}</div>
              </div>
              <div style="font-size: 14px;margin-bottom: 5px;">课件</div>
            </div>
            <el-divider direction="vertical"></el-divider>
          </div>
          <div class="first-row-right-item">
            <div class="flex_1">
              <div class="etp-icon">
                <svg style="margin-top: calc(50% - 16px)" t="1639967975225" class="icon" viewBox="0 0 1171 1024"
                     version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3347" width="32" height="32">
                  <path d="M1121.034 947.942c0 0-142.543-24.912-274.03-24.912-130.08 0-249.119 24.912-249.119 24.912-4.327 0-8.453-0.725-12.456-1.761-4.003 1.036-8.129 1.761-12.456 1.761 0 0-142.543-24.912-274.03-24.912-130.08 0-249.119 24.912-249.119 24.912-27.518 0-49.824-22.306-49.824-49.824L0 175.674c0 33.215 29.565-99.647 309.892-99.647 157.587 0 236.431 40.771 275.57 71.502 38.97-30.731 117.131-71.502 272.491-71.502 284.344 0 312.905 132.862 312.905 99.647l0 722.444C1170.858 925.636 1148.552 947.942 1121.034 947.942zM548.061 225.498c0 0-49.824-74.736-249.119-74.736-166.081 0-224.207 74.736-224.207 74.736l0 622.797c0 27.518 22.306 24.912 49.824 24.912 0 0 85.433-24.912 174.384-24.912 97.495 0 199.295 24.912 199.295 24.912 27.518 0 49.824 2.606 49.824-24.912L548.062 225.498zM1096.122 225.498c0 0-27.802-41.543-124.559-62.676l0 386.53-99.647-74.736-99.647 74.736L772.269 156.537c-109.032 17.411-149.471 68.961-149.471 68.961l0 622.797c0 27.518 22.306 24.912 49.824 24.912 0 0 85.433-24.912 174.383-24.912 97.495 0 199.295 24.912 199.295 24.912 27.518 0 49.824 2.606 49.824-24.912L1096.124 225.498zM423.502 673.911 199.295 673.911c-13.759 0-24.912-11.153-24.912-24.912l0-24.912c0-13.759 11.153-24.912 24.912-24.912l224.207 0c13.759 0 24.912 11.153 24.912 24.912L448.414 649C448.414 662.758 437.261 673.911 423.502 673.911zM423.502 449.705 199.295 449.705c-13.759 0-24.912-11.153-24.912-24.912l0-24.912c0-13.759 11.153-24.912 24.912-24.912l224.207 0c13.759 0 24.912 11.153 24.912 24.912l0 24.912C448.414 438.552 437.261 449.705 423.502 449.705z" p-id="3348" fill="#1296db"></path>
                </svg>
              </div>
            </div>
            <div class="flex_2">
              <div style="height: 35px">
                <ScrollNumber :value="0" v-if="courseFlag" :itemStyle="{fontSize: fontSize + 'px',fontWeight:fontWeight }" ref="curriculum"/>
                <div v-if="!courseFlag" style="font-size: 21px;font-weight: bold">{{ curriculumTotal }}</div>
              </div>
              <div style="font-size: 14px;margin-top: 5px">课程</div>
            </div>
            <el-divider direction="vertical"></el-divider>
          </div>
          <div class="first-row-right-item">
            <div class="flex_1">
              <div class="etp-icon">
                <svg style="margin-top: calc(50% - 16px)" t="1639968064542" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3910" width="32" height="32">
                  <path d="M775.68 244.053333l-244.053333 351.146667 2.56 78.506667v0.426666c0 11.946667 9.386667 21.333333 21.333333 21.333334 4.266667 0 8.106667-1.28 11.52-3.413334-0.853333 0.426667-2.133333 1.28-2.986667 1.706667l73.386667-24.32 244.053333-351.573333-105.813333-73.813334z m-235.093333 414.72z m10.666666-5.546666c1.28-0.426667 2.986667-0.426667 4.266667-0.426667-1.706667 0-2.986667 0-4.266667 0.426667z m-7.68 2.986666c0.426667 0 0.853333-0.426667 0.853334-0.426666s-0.426667 0.426667-0.853334 0.426666z m3.413334-2.133333c0.853333-0.426667 1.28-0.426667 2.133333-0.426667-0.426667 0.426667-1.28 0.426667-2.133333 0.426667z m28.16 11.52c-2.133333-5.12-6.4-9.386667-11.52-11.52 5.12 2.56 9.386667 6.4 11.52 11.52z m-5.12 23.893333c-0.426667 0.426667-1.28 0.853333-1.706667 1.706667 0.426667-0.426667 0.853333-1.28 1.706667-1.706667z m0.853333-0.853333c0.853333-0.853333 1.28-1.706667 2.133333-2.56-0.853333 0.853333-1.28 1.706667-2.133333 2.56z m3.84-5.973333c-0.426667 1.28-0.853333 2.133333-1.706667 2.986666 0.853333-0.853333 1.28-1.706667 1.706667-2.986666z m1.706667-4.693334c0.426667-1.28 0.426667-2.56 0.426666-3.84 0 1.28-0.426667 2.56-0.426666 3.84z m-1.28 3.413334c0.426667-0.853333 0.426667-1.706667 0.853333-2.56 0 0.853333-0.426667 1.706667-0.853333 2.56z"
                    fill="#1296db" p-id="3911"></path>
                  <path d="M553.386667 670.72l-3.413334-8.533333c-0.426667 0.426667-0.853333 0.426667-1.28 0.853333l4.693334 7.68zM558.933333 686.08c0.426667 0 0.426667 0 0 0l-1.28-4.693333 1.28 4.693333z"
                    fill="#1296db" p-id="3912"></path>
                  <path d="M659.2 697.6l-83.626667 27.733333c-6.4 2.56-13.226667 3.84-20.053333 3.84-30.72 0-55.466667-24.746667-55.466667-55.466666l-2.986666-89.173334 270.08-388.266666 0.426666 0.426666 8.106667-11.52c-5.12-23.04-25.6-40.106667-49.92-40.106666H136.533333c-28.16 0-51.2 23.04-51.2 51.2v631.466666c0 28.16 23.04 51.2 51.2 51.2h588.8c28.16 0 51.2-23.04 51.2-51.2v-299.093333l-117.333333 168.96zM204.8 273.066667h341.333333c18.773333 0 34.133333 15.36 34.133334 34.133333s-15.36 34.133333-34.133334 34.133333H204.8c-18.773333 0-34.133333-15.36-34.133333-34.133333s15.36-34.133333 34.133333-34.133333z m187.733333 477.866666H204.8c-18.773333 0-34.133333-15.36-34.133333-34.133333s15.36-34.133333 34.133333-34.133333h187.733333c18.773333 0 34.133333 15.36 34.133334 34.133333s-15.36 34.133333-34.133334 34.133333z m68.266667-204.8H204.8c-18.773333 0-34.133333-15.36-34.133333-34.133333s15.36-34.133333 34.133333-34.133333h256c18.773333 0 34.133333 15.36 34.133333 34.133333s-15.36 34.133333-34.133333 34.133333zM555.52 674.133333zM925.866667 226.56l-79.786667-55.466667c-7.253333-5.12-17.066667-2.986667-21.76 3.84l-1.706667 2.56-21.76 31.146667L906.666667 281.6l14.08-20.053333 1.706666-2.133334 4.266667-6.4 3.413333-5.12c4.693333-6.826667 2.56-16.64-4.266666-21.333333z"
                    fill="#1296db" p-id="3913"></path>
                </svg>
              </div>
            </div>
            <div class="flex_2">
              <div style="height: 35px">
                <ScrollNumber :value="0" v-if="subjectFlag" :itemStyle="{fontSize: fontSize + 'px',fontWeight:fontWeight }" ref="subject"/>
                <div v-if="!subjectFlag" style="font-size: 21px;font-weight: bold">{{ subjectTotal }}</div>
              </div>
              <div style="font-size: 14px;margin-top: 5px">试题</div>
            </div>
            <el-divider direction="vertical"></el-divider>
          </div>
          <div class="first-row-right-item">
            <div class="flex_1">
              <div class="etp-icon">
                <svg style="margin-top: calc(50% - 16px)" t="1639968107253" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4643" width="32" height="32">
                  <path d="M870.4 85.333333c-37.546667 0-68.266667 30.72-68.266667 68.266667v62.72h34.133334V153.6c0-18.773333 15.36-34.133333 34.133333-34.133333s34.133333 15.36 34.133333 34.133333v62.72h34.133334V153.6c0-37.546667-30.72-68.266667-68.266667-68.266667z" fill="#1296db" p-id="4644"></path>
                  <path d="M802.133333 187.733333h136.533334v34.133334h-136.533334z" fill="#1296db" p-id="4645"></path>
                  <path d="M759.466667 85.333333h76.8v130.986667h-76.8z" fill="#1296db" p-id="4646"></path>
                  <path d="M785.066667 85.333333h88.746666v34.133334H785.066667zM682.666667 904.533333H144.213333c-13.653333 0-24.746667-11.093333-24.746666-24.746666V836.266667h-34.133334v43.52c0 32.426667 26.453333 58.88 58.88 58.88h538.88l-0.426666-34.133334zM85.333333 802.133333h102.4v34.133334H85.333333z" fill="#1296db" p-id="4647"></path>
                  <path d="M785.066667 85.333333H238.933333c-28.16 0-51.2 23.04-51.2 51.2v699.733334h487.253334v34.133333c0 11.946667 2.56 23.466667 7.68 34.133333H190.72c6.826667 20.053333 26.026667 34.133333 48.213333 34.133334h546.133334c28.16 0 51.2-23.04 51.2-51.2V136.533333c0-28.16-23.04-51.2-51.2-51.2zM341.333333 682.666667c-18.773333 0-34.133333-15.36-34.133333-34.133334s15.36-34.133333 34.133333-34.133333 34.133333 15.36 34.133334 34.133333-15.36 34.133333-34.133334 34.133334z m0-204.8c-18.773333 0-34.133333-15.36-34.133333-34.133334s15.36-34.133333 34.133333-34.133333 34.133333 15.36 34.133334 34.133333-15.36 34.133333-34.133334 34.133334z m0-204.8c-18.773333 0-34.133333-15.36-34.133333-34.133334s15.36-34.133333 34.133333-34.133333 34.133333 15.36 34.133334 34.133333-15.36 34.133333-34.133334 34.133334z m344.32 406.613333h-244.906666c-17.066667 0-31.146667-14.08-31.146667-31.146667s14.08-31.146667 31.146667-31.146666h244.906666c17.066667 0 31.146667 14.08 31.146667 31.146666s-14.08 31.146667-31.146667 31.146667z m0-204.8h-244.906666c-17.066667 0-31.146667-14.08-31.146667-31.146667s14.08-31.146667 31.146667-31.146666h244.906666c17.066667 0 31.146667 14.08 31.146667 31.146666s-14.08 31.146667-31.146667 31.146667z m0-204.8h-244.906666c-17.066667 0-31.146667-14.08-31.146667-31.146667s14.08-31.146667 31.146667-31.146666h244.906666c17.066667 0 31.146667 14.08 31.146667 31.146666s-14.08 31.146667-31.146667 31.146667z" fill="#1296db" p-id="4648"></path>
                </svg>
              </div>
            </div>
            <div class="flex_2">
              <div style="height: 35px">
                <ScrollNumber :value="0" v-if="paperFlag" :itemStyle="{fontSize: fontSize + 'px',fontWeight:fontWeight }" ref="testPaper"/>
                <div v-if="!paperFlag" style="font-size: 21px;font-weight: bold">{{ testPaperTotal }}</div>
              </div>
              <div style="font-size: 14px;margin-top: 5px">试卷</div>
            </div>
          </div>
        </div>
        <div style="clear:both"></div>
        <!--   左侧第二行 快捷入口     -->
        <div class="etp-box second-row">
          <el-row :gutter="12" style="height: 120px;">
            <el-col :span="8">
              <el-card shadow="hover">
                <div class="etp-hover flex-box" @click="toTarPage(1)">
                  <div class="flex_1">
                    <div style="width: 48px;height: 48px;margin-top:12px;text-align: center;">
                      <svg t="1639968856845" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="11172" width="48" height="48">
                        <path d="M380.4 281.1h128.9c15.5 0 25.8-10.3 25.8-25.8s-10.3-25.8-25.8-25.8H380.4c-15.5 0-25.8 10.3-25.8 25.8 0.1 15.6 10.4 25.8 25.8 25.8m309.5 51.6H478.5c2.1 2.6 4.1 5.2 6.2 8.1l31 43.5h174.2c15.5 0 25.8-10.3 25.8-25.8 0-15.4-10.3-25.8-25.8-25.8z" p-id="11173" fill="#1296db"></path>
                        <path d="M741.5 106.4H328.9c-5.4 0-10.8 0.5-15.9 1.5s-10 2.5-14.6 4.4c-13.9 5.7-25.7 15.4-33.9 27.7-4.2 6.1-7.3 12.9-9.5 20.4-2.2 7.3-3.4 15.2-3.4 23.4v116.8h51.6V183.8c0-2 0.2-3.8 0.5-5.5 1.9-10.6 9.2-17.9 19.8-19.8 1.8-0.3 3.7-0.5 5.5-0.5h412.5c15.5 0 25.8 10.3 25.8 25.8v209.7h51.6V183.8c0-43.9-33.5-77.4-77.4-77.4z" p-id="11174" fill="#1296db"></path>
                        <path d="M860.3 921.5h-693c-37.6 0-68.2-30.6-68.2-68.2V393.6c0-37.6 30.6-68.2 68.2-68.2h200.9c25.7-0.2 57.7-0.5 94 44.7l0.1 0.1 0.4 0.5c1.9 2.4 4 5.1 6.2 8.2l27.9 39h363.6c1.1 0 2.1 0.1 3.2 0.2 0.5 0.1 1.1 0.1 1.6 0.2 35.6 2.6 63.3 32.3 63.3 67.9v367.2c0 37.5-30.6 68.1-68.2 68.1z m-693-545.1c-9.5 0-17.2 7.7-17.2 17.2v459.7c0 9.5 7.7 17.2 17.2 17.2h693c9.5 0 17.2-7.7 17.2-17.2V486.1c0-9-7.1-16.5-16.2-17-0.4 0-0.8-0.1-1.2-0.1-0.4 0-0.8-0.1-1.2-0.1H483.6c-8.3 0-16-4-20.8-10.7l-6.6-9.3-28.8-40.3c0-0.1-0.1-0.1-0.1-0.2-1.3-1.9-2.9-3.9-4.5-5.9l-0.4-0.5c-20.8-25.8-31.8-25.7-53.7-25.5H167.3z" p-id="11175" fill="#1296db"></path>
                      </svg>
                    </div>
                  </div>
                  <div class="flex_4" style="margin-left: 10px">
                    <p style="font-size: 16px;font-weight: 900">创建课件</p>
                    <p class="one-row-hide">创建在线学习课件，支持视频、文档、PPT</p>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover">
                <div class="etp-hover flex-box" @click="toTarPage(2)">
                  <div class="flex_1">
                    <div style="width: 48px;height: 48px;margin-top:12px;text-align: center;">
                      <svg t="1639968584965" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6807" width="48" height="48">
                        <path d="M532.7 884.6h-36.2c-3.8 0-7.6-1.1-10.8-3.2l-35.5-22.8c-88.3-56.7-256.5-55.8-384.4-36-13.4 2.1-26.8-2.2-36.6-11.6-9.9-9.6-14.7-22.9-13-36.6l68.1-567c2.1-17.7 17.1-31 34.9-31h38.3c11 0 20 9 20 20s-9 20-20 20h-34.1L55.9 779.1c-0.2 1.6 0.6 2.6 1.1 3.1 0.8 0.7 1.7 1 2.7 0.9 72.4-11.2 148.1-15.2 213.1-11.3 85 5.1 152 23 199 53.2l30.5 19.6h24.5l30.5-19.6c47.2-30.3 113.9-48.6 198.2-54.4 65.6-4.5 141-1 212.4 9.9 0.4 0.1 1.5 0.2 2.6-0.9 0.5-0.5 1.3-1.5 1.1-3.1l-66-560.1h-34c-11 0-20-9-20-20s9-20 20-20h38.3c17.8 0 32.9 13.4 35 31.1l66.5 564.4c1.6 13.7-3.1 27-13 36.6-9.8 9.4-23.1 13.7-36.5 11.7-68.6-10.4-140.9-13.8-203.7-9.5-76.4 5.3-138.4 21.9-179.3 48.2l-35.5 22.8c-3.1 1.8-6.9 2.9-10.7 2.9z" fill="#1296db" p-id="6808"></path>
                        <path d="M486.4 694.7c-4.1 0-8.1-1.2-11.6-3.7l-27.7-19.6c-39.2-27.8-85.9-41.3-142.6-41.3-27.7 0-58.8 3.2-95 9.8-2.4 0.4-4.8 0.6-7.2 0.6-11 0-21.5-4.6-28.7-12.6-7.1-7.8-10.4-17.8-9.5-28.2L206 145.4c0.8-8.8 7.2-16 15.9-17.8 41.8-8.6 76.4-15.2 109.5-15.2 49.2 0 90.8 14.5 138.9 48.6l27.7 19.6c5.3 3.7 8.4 9.8 8.4 16.3v477.7c0 7.5-4.2 14.3-10.8 17.8-2.9 1.5-6 2.3-9.2 2.3zM304.6 590.1c63.5 0 116.5 15 161.9 46V207.3l-19.3-13.6c-41.4-29.3-74.9-41.3-115.8-41.3-25.7 0-53.8 4.8-86.9 11.4l-40.2 436.3c37.8-6.7 70.6-10 100.3-10z" fill="#1296db" p-id="6809"></path>
                        <path d="M514 694.7c-6.3 0-12.4-2.9-16.3-8.4-6.4-9-4.3-21.5 4.8-27.9l27.7-19.6c85.9-60.9 187.1-52.8 266.1-38.6L756 163.8c-84.4-17-131.6-20.6-202.7 29.8l-27.7 19.6c-9 6.4-21.5 4.3-27.9-4.8-6.4-9-4.3-21.5 4.8-27.9l27.7-19.6c88.3-62.6 148.9-54 248.4-33.4 8.6 1.8 15.1 9 15.9 17.8l41.9 454.4c1.1 11.5-3.3 22.7-11.9 30.6-8.9 8.2-21.4 11.7-33.5 9.5-76-13.8-163.7-20.9-237.7 31.5L525.6 691c-3.5 2.5-7.6 3.7-11.6 3.7z" fill="#1296db" p-id="6810"></path>
                        <path d="M562.5 363c-6.3 0-12.4-2.9-16.3-8.4-6.4-9-4.3-21.5 4.8-27.9l24.7-17.5c55.1-39.1 99-32.2 145.5-24.9 6.4 1 13 2 19.6 3 10.9 1.5 18.6 11.6 17.1 22.5s-11.6 18.6-22.5 17.1c-7-1-13.8-2-20.4-3.1-43.1-6.8-74.3-11.7-116.2 18L574 359.3c-3.5 2.5-7.5 3.7-11.5 3.7zM562.5 472.1c-6.3 0-12.4-2.9-16.3-8.4-6.4-9-4.3-21.5 4.8-27.9l24.7-17.5c54.9-38.9 108-30.5 154.8-23.1 6.8 1.1 13.8 2.2 20.6 3.1 10.9 1.5 18.6 11.6 17.1 22.5s-11.6 18.6-22.5 17.1c-7.2-1-14.4-2.1-21.4-3.2-44.9-7.1-83.7-13.3-125.4 16.3L574 468.4c-3.5 2.5-7.5 3.7-11.5 3.7zM436.3 362.5c-4 0-8-1.2-11.5-3.7l-24.2-17.2c-41-29.1-71.6-24.3-113.9-17.6-6.4 1-13.1 2.1-20 3-10.9 1.5-21-6.1-22.5-17.1-1.5-10.9 6.1-21 17.1-22.5 6.5-0.9 13-1.9 19.3-2.9 45.7-7.2 88.9-14 143.2 24.5l24.2 17.2c9 6.4 11.1 18.9 4.8 27.9-4 5.5-10.2 8.4-16.5 8.4zM436.3 469.8c-4 0-8-1.2-11.5-3.7l-24.2-17.2c-41.6-29.5-80.9-23.4-126.5-16.4-6.2 1-12.6 1.9-18.9 2.8-10.9 1.5-21-6.1-22.5-17.1-1.5-10.9 6.1-21 17.1-22.5 6.1-0.8 12.3-1.8 18.3-2.7 47.3-7.3 100.9-15.5 155.7 23.3l24.2 17.2c9 6.4 11.1 18.9 4.8 27.9-4 5.4-10.2 8.4-16.5 8.4z" fill="#1296db" p-id="6811"></path>
                      </svg>
                    </div>
                  </div>
                  <div class="flex_4" style="margin-left: 10px">
                    <p style="font-size: 16px;font-weight: 900">创建课程</p>
                    <p class="one-row-hide">创建在线学习课程，支持视频</p>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover">
                <div class="etp-hover flex-box" @click="toTarPage(3)">
                  <div style="flex: 1;">
                    <div style="width: 48px;height: 48px;margin-top:12px;text-align: center;">
                      <svg t="1639968640298" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7931" width="48" height="48">
                        <path d="M448 896H163.04C143.904 896 128 879.68 128 859.072V164.928C128 144.32 143.904 128 163.04 128h569.92C752.096 128 768 144.32 768 164.928v182.464a32 32 0 0 0 64 0V164.928C832 109.376 787.84 64 732.96 64H163.04C108.16 64 64 109.376 64 164.928v694.144C64 914.624 108.16 960 163.04 960H448a32 32 0 0 0 0-64z" p-id="7932" fill="#1296db"></path>
                        <path d="M736 448a256 256 0 1 0 0 512 256 256 0 0 0 0-512z m0 448a192 192 0 1 1 0-384 192 192 0 0 1 0 384z" p-id="7933" fill="#1296db"></path>
                        <path d="M831.456 688H752v-64a32 32 0 0 0-64 0v96a32 32 0 0 0 32 32h111.456a32 32 0 0 0 0-64zM640 320a32 32 0 0 0-32-32H288a32 32 0 0 0 0 64h320a32 32 0 0 0 32-32zM288 544a32 32 0 0 0 0 64h80.288a32 32 0 0 0 0-64H288z" p-id="7934" fill="#1296db"></path>
                      </svg>
                    </div>
                  </div>
                  <div class="flex_4" style="margin-left: 10px">
                    <p style="font-size: 16px;font-weight: 900">创建任务</p>
                    <p class="one-row-hide">创建在线学习任务，支持视频</p>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="12">
            <el-col :span="8">
              <el-card shadow="hover">
                <div class="etp-hover flex-box" @click="toTarPage(4)">
                  <div class="flex_1">
                    <div style="width: 48px;height: 48px;margin-top:12px;text-align: center;">
                      <svg t="1639968679174" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8496" width="48" height="48">
                        <path d="M682.666667 69.333333a122.666667 122.666667 0 0 1 122.581333 117.973334l0.085333 4.693333v218.666667H832a122.666667 122.666667 0 0 1 122.581333 117.973333l0.085334 4.693333v298.666667a122.666667 122.666667 0 0 1-117.973334 122.581333l-4.693333 0.085334H234.666667a122.666667 122.666667 0 0 1-122.581334-117.973334L112 832V192a122.666667 122.666667 0 0 1 117.973333-122.581333L234.666667 69.333333h448zM805.333333 682.666667a37.333333 37.333333 0 0 1-74.538666 3.072L730.666667 682.666667V192a48 48 0 0 0-44.714667-47.893333L682.666667 144H234.666667a48 48 0 0 0-47.893334 44.714667L186.666667 192v640a48 48 0 0 0 44.714666 47.893333l3.285334 0.106667h597.333333a48 48 0 0 0 47.893333-44.714667L880 832V533.333333a48 48 0 0 0-44.714667-47.893333L832 485.333333h-26.666667V682.666667z" p-id="8497" fill="#1296db"></path>
                        <path d="M565.333333 325.333333a37.333333 37.333333 0 0 1 3.072 74.538667l-3.072 0.128h-213.333333a37.333333 37.333333 0 0 1-3.072-74.538667l3.072-0.128h213.333333zM458.666667 538.666667a37.333333 37.333333 0 0 1 3.072 74.538666l-3.072 0.128h-106.666667a37.333333 37.333333 0 0 1-3.072-74.538666l3.072-0.128h106.666667z" p-id="8498" fill="#1296db"></path>
                      </svg>
                    </div>
                  </div>
                  <div class="flex_4" style="margin-left: 10px">
                    <p style="font-size: 16px;font-weight: 900">创建试题</p>
                    <p class="one-row-hide">创建在线试题</p>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover">
                <div class="etp-hover flex-box" @click="toTarPage(5)">
                  <div class="flex_1">
                    <div style="width: 48px;height: 48px;margin-top:12px;text-align: center;">
                      <svg t="1639968734425" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9246" width="48" height="48">
                        <path d="M864 96H256c-35.346 0-64 28.654-64 64v640H96v64c0 35.347 28.654 64 64 64h608c35.346 0 64-28.653 64-64V224h96v-64c0-35.346-28.654-64-64-64zM160 896c-17.673 0-32-14.327-32-32v-32h544v32a63.692 63.692 0 0 0 8.583 32H160z m640-736v704c0 17.673-14.327 32-32 32h-32c-17.673 0-32-14.327-32-32v-64H224V160c0-17.673 14.327-32 32-32h552.584A63.682 63.682 0 0 0 800 160z m96 32h-64v-32c0-17.673 14.327-32 32-32s32 14.327 32 32v32z m-560 96h160c8.836 0 16-7.164 16-16s-7.164-16-16-16H336c-8.836 0-16 7.164-16 16s7.164 16 16 16z m0 128h352c8.837 0 16-7.164 16-16s-7.163-16-16-16H336c-8.836 0-16 7.164-16 16s7.164 16 16 16z m0 128h224c8.837 0 16-7.163 16-16s-7.163-16-16-16H336c-8.836 0-16 7.163-16 16s7.164 16 16 16z m0 128h352c8.837 0 16-7.163 16-16s-7.163-16-16-16H336c-8.836 0-16 7.163-16 16s7.164 16 16 16z" p-id="9247" fill="#1296db"></path>
                      </svg>
                    </div>
                  </div>
                  <div class="flex_4" style="margin-left: 10px">
                    <p style="font-size: 16px;font-weight: 900">创建试卷</p>
                    <p class="one-row-hide">创建在线试卷</p>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover">
                <div class="etp-hover flex-box" @click="toTarPage(6)">
                  <div class="flex_1">
                    <div style="width: 48px;height: 48px;margin-top:12px;text-align: center;">
                      <svg t="1639968795096" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="10494" width="48" height="48">
                        <path d="M113.777778 0h796.444444a56.888889 56.888889 0 0 1 56.888889 56.888889v910.222222a56.888889 56.888889 0 0 1-56.888889 56.888889H113.777778a56.888889 56.888889 0 0 1-56.888889-56.888889V56.888889a56.888889 56.888889 0 0 1 56.888889-56.888889z m0 56.888889v910.222222h796.444444V56.888889H113.777778z m171.434666 204.060444a28.444444 28.444444 0 1 1 0-56.888889H740.124444a28.444444 28.444444 0 0 1 0 56.888889H285.212444z m0 236.828445a28.444444 28.444444 0 0 1 0-56.888889h292.152889a28.444444 28.444444 0 0 1 0 56.888889h-292.124444z m0 227.555555a28.444444 28.444444 0 0 1 0-56.888889h146.090667a28.444444 28.444444 0 0 1 0 56.888889h-146.090667z" fill="#1296db" p-id="10495"></path>
                        <path d="M590.051556 663.153778a28.444444 28.444444 0 1 0-40.192 40.305778l78.563555 78.307555a28.444444 28.444444 0 0 0 44.828445-6.087111l115.2-202.979556a28.444444 28.444444 0 0 0-49.464889-28.103111l-96.796445 170.552889-52.138666-51.996444z" fill="#1296db" p-id="10496"></path>
                      </svg>
                    </div>
                  </div>
                  <div class="flex_4" style="margin-left: 10px">
                    <p style="font-size: 16px;font-weight: 900">创建考试</p>
                    <p class="one-row-hide">创建在线考试</p>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!--  左侧第三行 当前课程/当前考试      -->
        <div class="etp-box third-row">
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="当前课程" name="first">
              <div class="course-box">
                <swiper style="height: 100%;"
                        :options="swiperOptionF"
                        @swiper="onSwiper"
                        @slideChange="onSlideChange">
                  <swiper-slide v-for="(item,index) in courseList" :key="item.id">
                    <el-card shadow="never">
                      <div @click="preCourseOrTest(item.id,'course')" style="cursor: pointer">
                        <div>
                          <el-image class="course-box-item-img"
                                    :src="item.fmt==null?require('@/assets/images/cover.jpg'):item.fmt" fit="cover"/>
                        </div>
                        <el-tooltip class="item" effect="dark" :content="item.mc" placement="top-start">
                          <div class="course-box-item-text">
                            {{ item.mc }}
                          </div>
                        </el-tooltip>

                      </div>
                      <div class="margin_top_15">
                        {{ item.regNum }}人报名学习
                      </div>

                    </el-card>
                  </swiper-slide>
                  <div class="swiper-pagination" slot="pagination"></div>
                  <div class="swiper-button-prev" slot="button-prev"></div>
                  <div class="swiper-button-next" slot="button-next"></div>
                </swiper>

              </div>
            </el-tab-pane>
            <el-tab-pane label="当前考试" name="second">
              <div class="course-box">
                <swiper style="height: 100%;"
                        :options="swiperOption"
                        @swiper="onSwiper"
                        @slideChange="onSlideChange">
                  <swiper-slide v-for="i in examList" :key="i.examId">
                    <el-card shadow="never">
                      <div @click="preCourseOrTest(i.examId,'exam',i.paperId)">
                        <div>
                          <el-image class="course-box-item-img"
                                    :src="i.cover==null?require('@/assets/images/cover.jpg'):i.cover" fit="cover"/>
                        </div>
                        <el-tooltip class="item" effect="dark" :content="i.name" placement="top-start">
                          <div class="course-box-item-text">
                            {{ i.name}}
                          </div>
                        </el-tooltip>
                      </div>
                      <div class="margin_top_15">
                        开始日期：{{ i.startTime }} <br>
                        截止日期：{{ i.endTime }}
                      </div>
                    </el-card>
                  </swiper-slide>
                  <div class="swiper-pagination" slot="pagination"></div>
                  <div class="swiper-button-prev" slot="button-prev"></div>
                  <div class="swiper-button-next" slot="button-next"></div>
                </swiper>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!--   左侧第四行 热门课件   -->
        <div class="etp-box four-row">
          <span>热门课件</span>
          <div class="four-row-box">
            <swiper style="height: 100%;"
                    :options="swiperOptionHot"
                    @swiper="onSwiper"
                    @slideChange="onSlideChange">
              <swiper-slide v-for="item in hotCoursewareList" :key="item.id">
                <el-card shadow="never">
                  <div @click="coursewarePre(item)" style="display: grid;cursor: pointer;
                              grid-template-columns: repeat(2, 1fr);
                              grid-template-rows: 1fr;
                              grid-column-gap: 15px;
                              grid-row-gap: 0px;padding-top: 15px">
                    <div style="grid-area: 1 / 1 / 2 / 2;">
                      <el-image  class="four-row-box-item-img"
                                 :src="item.fm==null?require('@/assets/images/cover.jpg'):item.fm" fit="cover" />
                    </div>
                    <div style="grid-area: 1 / 2 / 2 / 3;">
                      <div class="four-row-box-item-text">
                        {{ item.mc }}
                      </div>
                      <div class="margin_top_15">{{ item.downNum }}人已学习</div>
                    </div>
                  </div>
                </el-card>
              </swiper-slide>
              <div class="swiper-pagination" slot="pagination"></div>
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
            </swiper>

          </div>
        </div>

        <!--   左侧第五行 活跃学员     -->
        <!-- div class="etp-box five-row">
          <span>活跃学员</span>
          <div class="five-row-box">
            <swiper style="height: 100%;"
              :options="swiperOptionUser"
              @swiper="onSwiper"
              @slideChange="onSlideChange"
            >
              <swiper-slide v-for="i in hotUserList">
                <el-card shadow="never" style="padding-top: 15px">
                  <el-image class="five-row-box-item-img" style="border-radius: 20px"
                            :src="i.avatar" fit="cover" />
                  <div style="margin-top: 5px;font-size: 16px">{{ i.userName }}</div>
                  <div style="margin-top: 5px;color: #909399">{{ i.frequency }}</div>
                </el-card>
              </swiper-slide>
              <div class="swiper-pagination" slot="pagination"></div>
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
            </swiper>
          </div>
        </div -->
      </div>

      <!-- etp 右侧区域-->
      <div class="etp-index-right">
        <div class="etp-box">
          <span style="cursor: pointer" @click="setStudentURI">学员入口 <i class="el-icon-edit" style="margin-left: 20px;"></i></span>
          <el-divider/>
          <div class="etp-index-right-start">
            <div class="qrcode" ref="qrCodeUrl"></div>
            <div class="etp-index-right-start-url" @click="clickCopy(studentStart)" id="uri">
              {{ studentStart }}
            </div>
          </div>
        </div>
        <!--   运营通知     -->
<!--        <div class="etp-box">-->
<!--          <span>运营通知</span>-->
<!--          <span class="etp-more">查看更多</span>-->
<!--          <el-divider/>-->
<!--          <div class="etp-list">-->
<!--            <ul>-->
<!--              <li>-->
<!--                <span style="color:red">【党建】</span> 关于莫莫莫企业偷税漏税传播为报复偶尔狗头人保护套日弄-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:green">【生活】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:red">【党建】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:red">【党建】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:red">【党建】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:green">【生活】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:red">【党建】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:green">【生活】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--              <li>-->
<!--                <span style="color:red">【党建】</span> 关于莫莫莫企业偷税漏税-->
<!--                <p class="etp-list-time">2021-12-17 16:03</p>-->
<!--              </li>-->
<!--            </ul>-->
<!--          </div>-->
<!--        </div>-->
        <!--   通知公告     -->
        <div class="etp-box">
          <span>通知公告</span>
          <span class="etp-more" @click="toNoticeMore">查看更多</span>
          <el-divider/>
          <div class="etp-list">
            <ul>
              <li @click="showDetail(notice)" v-for="notice in noticesList" :key="notice.noticeId">
                <span style="color:red" v-if="notice.noticeType == 1">【通知】</span>
                <span style="color:green" v-if="notice.noticeType == 2">【公告】</span>
                <template>{{ notice.noticeTitle }}</template>
                <p class="etp-list-time">{{ notice.createTime }}</p>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <!-- 客服图案  -->
      <!-- div class="kf" @click="opinionSum">
        <el-badge :value="opinionCount" style="margin-top:calc(50% - 16px)" >
          <svg t="1640047878119" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1946" width="32" height="32"><path d="M679.408 882.08a63.312 63.312 0 0 1-46.656 54.064l-81.728 21.808a63.488 63.488 0 0 1-77.232-44.832 63.2 63.2 0 0 1 44.384-77.312l81.712-21.808a63.584 63.584 0 0 1 60.16 15.248 304 304 0 0 0 147.76-125.92h-20.832c-62.304 0-112.8-50.336-112.8-112.416v-112.416c0-62.08 50.496-112.4 112.8-112.4h28.208c4.064 0 8.096 0.208 12.064 0.64C784.128 240.112 659.504 148.288 512 148.288S239.872 240.128 196.752 366.72c3.968-0.416 8-0.64 12.064-0.64h28.208c62.304 0 112.8 50.336 112.8 112.416v112.416c0 62.08-50.496 112.4-112.8 112.4H208.8C146.512 703.312 96 652.992 96 590.912v-112.416c0-2.928 0.112-5.84 0.32-8.72a42.688 42.688 0 0 1-0.32-5.328C96 242.912 282.608 64 512 64s416 178.912 416 400.448c0 1.808-0.112 3.584-0.32 5.328 0.208 2.88 0.32 5.792 0.32 8.72v112.416a112.128 112.128 0 0 1-46.88 91.232A360.128 360.128 0 0 1 679.392 882.08z" fill="#ffffff" p-id="1947"></path></svg>
        </el-badge>
      </div -->
    </div>

    <el-dialog
      :title="noticeTitle"
      :visible.sync="dialogNoticeVisible"
      :close-on-click-modal="false"
      width="1000px">
      <div v-html="noticeContent" style="overflow: hidden;padding-left: 30px;padding-right: 30px"></div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogNoticeVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogNoticeVisible = false">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="设置学员访问地址"
      :close-on-click-modal="false"
      width="30%"
      :visible.sync="dialogFormVisible">

      <el-form ref="form"  label-width="120px">
        <el-form-item label="学员访问地址：">
          <el-input v-model="studentStart" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmURI">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog :visible.sync="contentTextVisible" width="1000px"
               :close-on-click-modal="false"
               :fullscreen="isFullscreen"
               :before-close="handleContentClose" append-to-body>
      <template slot="title">
        <div class="avue-crud__dialog__header">
            <span class="el-dialog__title">
            <span style="display:inline-block;background-color: #3478f5;width:3px;height:20px;margin-right:5px; float: left;margin-top:2px"></span>
              课件预览
            </span>
          <div class="avue-crud__dialog__menu" @click="isFullscreen? isFullscreen = false: isFullscreen = true">
            <i class="el-icon-full-screen"></i>
          </div>
        </div>
      </template>
      <RichTextPreview :infoObj="infoObj"/>
    </el-dialog>

    <!--选择试卷-->
    <paperList
      ref="paper"
      @confirm="confirm"></paperList>
  </div>
</template>

<script>
import {  getTotal,getCurrentCourse,
          getCurrentTest,getCurrentCourseware,
          getHotMembers,getNotices,getOpinions } from "@/api/etp/index";
import 'swiper/css/swiper.css';
import QRCode from 'qrcodejs2';
import Cookies from "js-cookie";
import {FILE_PREVIEW_URL} from "@/utils/ruoyi";
import RichTextPreview from "./etp/courseware/components/index"
import paperList from "@/views/exam/exam/components/paperList";
import { listExam } from "@/api/exam/exam";

export default {
  name: "index",
  components:{ RichTextPreview,paperList },
  data() {
    return {
      dialogFormVisible:false,
      dialogNoticeVisible:false,
      noticeTitle:'',
      noticeContent:'',
      coursewareFlag:true,
      courseFlag:true,
      subjectFlag:true,
      paperFlag:true,
      fontSize:21,
      fontWeight: 'bold',
      activeName: 'first',
      studentStart: process.env.VUE_APP_DEFAULT_QR_URL == undefined || process.env.VUE_APP_DEFAULT_QR_URL == ""
        ?window.location.protocol+"//"+window.location.host+"/student":process.env.VUE_APP_DEFAULT_QR_URL,
      courseTotal:0,
      curriculumTotal:0,
      subjectTotal:0,
      testPaperTotal:0,
      swiperOptionHot:{
        slidesPerView: 2,
        // 设置分页器
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        },
        /*autoplay: {
          delay: 5000 // 5秒切换一次
        },
        loop: true*/
      },

      swiperOptionUser: {
        slidesPerView: 5,
        // 设置分页器
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        },
        /*autoplay: {
          delay: 5000 // 5秒切换一次
        },
        loop: true*/
      },

      swiperOption: {
        slidesPerView: 4,
        // 设置分页器
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        },
        /*autoplay: {
          delay: 5000 // 5秒切换一次
        },
        loop: true*/
      },
      swiperOptionF: {
        slidesPerView: 4,
        // 设置分页器
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        },
        /*autoplay: {
          delay: 5000 // 5秒切换一次
        },
        loop: true*/
      },

      // testList:[],
      examList:[],
      courseList:[],
      hotCoursewareList:[],
      noticesList:[],
      opinionsList:[],
      opinionCount:0,

      hotUserList:[
        { userName:"王强",avatar:'http://10.153.3.35/group1/M00/00/05/CpkDI2IgGpqARm0oAAE866Dckdc951.jpg',frequency:'3小时内访问过50次'},
        { userName:"张华",avatar:'http://10.153.3.35/group1/M00/00/05/CpkDI2IgGpqARm0oAAE866Dckdc951.jpg',frequency:'4小时内访问过50次'},
        { userName:"徐卫东",avatar:'http://10.153.3.35/group1/M00/00/05/CpkDI2IgGpqARm0oAAE866Dckdc951.jpg',frequency:'3小时内访问过35次'},
        { userName:"王德华",avatar:'http://10.153.3.35/group1/M00/00/05/CpkDI2IgGpqARm0oAAE866Dckdc951.jpg',frequency:'3小时内访问过25次'},
        { userName:"徐小生",avatar:'http://10.153.3.35/group1/M00/00/05/CpkDI2IgGpqARm0oAAE866Dckdc951.jpg',frequency:'3小时内访问过15次'},
        { userName:"王翠花",avatar:'http://10.153.3.35/group1/M00/00/05/CpkDI2IgGpqARm0oAAE866Dckdc951.jpg',frequency:'3小时内访问过5次'},
      ],
      infoObj:"",
      contentTextVisible:false,
      isFullscreen:false,
    };
  },
  mounted() {
    this.creatQrCode();
  },
  created() {
    this.getTotal();
    this.getCurrentCourse();
    this.getCurrentCourseware();
    this.getNotices();
    this.getOpinions();
    // this.getCurrentTest();
    this.getExamList();
  },
  activated() {
    this.getTotal();
  },
  methods: {
      handleContentClose(){
        this.contentTextVisible = false;
        this.isFullscreen = false;
      },

      coursewarePre(obj){
        // if(obj.xs == '1' || obj.xs == '5' || obj.xs == '2'){
        //   let url = obj.kjdz;
        //   window.open(FILE_PREVIEW_URL + '?url=' + encodeURIComponent(window.btoa(url)) )
        // }
        // if (obj.xs == '3'){
        //   this.contentTextVisible = true;
        //   this.infoObj = {
        //     type:"richText",
        //     content:obj.nr
        //   };
        // }
        // if (obj.xs == '4'){
        //   window.open(obj.lj)
        // }
        let router = this.$router.resolve({
          path: "/etp/course/previewCourse",
          query: {kjid: obj.id}
        })
        window.open(router.href,'_blank')

      },

      preCourseOrTest(id,type,paperId){
        if (type == 'course'){
          // 课程预览
         /* this.$router.push({
            path:"/etp/task/previewCurriculum/" + id,
            query:{ t:Date.now(),where:'index' }
          });*/
          window.open('/student/etp/curriculum/previewCurriculum?cid='+ id+'&requestFrom='+"admin" )
        }else {
          // 考试预览
          //  this.$router.push({
          //    path:"/etp/task/previewExamination/" + id,
          //    query:{ t:Date.now(),where:'index' }
          //  });

          // this.$router.push({
          //   path:"/exam-edit/edit/",
          //   query:{examId:id,paperId:paperId,flag:0,t: Date.now(),where:'index'}
          // });

          window.open('/admin/exam-edit/edit/?examId='+id+'&paperId='+paperId+'&flag='+0+'&t='+Date.now())

        }
      },
      renewal(){
        this.$message.warning("续费")
      },

      confirmURI(){
        this.$refs.qrCodeUrl.innerHTML = '';
        Cookies.set("etp_student_rul",this.studentStart);
        this.creatQrCode();
        this.dialogFormVisible = false;
      },
      setStudentURI(){
        this.dialogFormVisible = true;
      },

      toNoticeMore(){
        // this.$router.push('/system/notice');
        window.open('/admin/notice')
      },
     onSwiper () {},
     onSlideChange () {},
     creatQrCode() {
        let url = Cookies.get("etp_student_rul");
        this.studentStart = url || this.studentStart
        new QRCode(this.$refs.qrCodeUrl, {
          text: this.studentStart, // 需要转换为二维码的内容
          width: 180,
          height: 180,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        })
     },
      showDetail(data){
        this.dialogNoticeVisible = true;
        this.noticeTitle = data.noticeTitle;
        this.noticeContent = data.noticeContent;
      },
      getNotices(){
        getNotices().then(res => {
          // console.log(res);
          this.noticesList = res.data
        })
      },
    getOpinions(){
      getOpinions().then(res => {
        // console.log(res);
        this.opinionsList = res.data;
        this.opinionCount=res.data.length;
        /*this.valueOf(res.data.length);
        this.$forceUpdate();*/
        // console.log("意见反馈条数："+res.data.length);
      })
    },
    opinionSum(){
      this.$router.push({
        path:"/opinion",
      });
    },
     getCurrentCourseware(){
       getCurrentCourseware().then(res => {
         this.hotCoursewareList = res.data;
       })
     },
     getCurrentCourse(){
       getCurrentCourse().then(res => {
         this.courseList = res.data;
       })
     },

    // getCurrentTest(){
    //   getCurrentTest().then( res =>{
    //     this.testList = res.data;
    //   })
    // },

    getExamList() {

      let queryParams = {pageNum: 1, pageSize: 10, status: 2}
      listExam(queryParams).then(response => {
        this.examList = response.rows;
      });
    },

     getTotal(){
       getTotal().then(res => {
        if (res.code == 200){
          let { CourseTotal,CurriculumTotal,SubjectTotal,PaperTotal } = res.data;
            this.courseTotal = CourseTotal.total;
            this.curriculumTotal = CurriculumTotal.total;
            this.subjectTotal = SubjectTotal.total;
            this.testPaperTotal = PaperTotal.total;
            this.$refs.course.changeTo(this.courseTotal).then(()=>{
              this.coursewareFlag = false;
            })
            this.$refs.curriculum.changeTo(this.curriculumTotal).then(()=>{
              this.courseFlag = false;
            })
            this.$refs.subject.changeTo(this.subjectTotal).then(()=>{
              this.subjectFlag = false;
            })
            this.$refs.testPaper.changeTo(this.testPaperTotal).then(()=>{
              this.paperFlag = false;
            })

        }
      })
    },
    clickCopy(e){
      let input = document.createElement("input");
      input.value = e;
      document.body.appendChild(input);
      input.select();
      document.execCommand("Copy");
      document.body.removeChild(input);
      if (document.execCommand("Copy") === true) {
        this.$message.success("链接复制成功")
      }
    },
    handleClick(tab, event) {
      // console.log(tab, event);
    },
    handleNodeClick(data) {
      // console.log(data);
    },
    confirm(val) {
      this.$router.push({
        path:"/exam-edit/edit/",
        query:{ sjData: val }
      });
    },
    toTarPage(type) {
      switch (type) {
        case 1:
          this.$router.push({path: "/etp/courseware/components/addOrUpdateCourseware/",query: {from: 'index'}});
          break;
        case 2:
          this.$router.push({path: "/etp/curriculum/addOrUpdateCurriculum/", query: {from: 'index'}});
          break;
        case 3:
          this.$router.push({path: "/etp/task/addOrUpdateTask/", query: {from: 'index'}});
          break;
        case 4:
          this.$router.push({path: "/question-edit/edit/", query: {from: 'index'}});
          break;
        case 5:
          this.$router.push({path: "/paper-edit/edit/", query: {from: 'index'}});
          break;
        case 6:
          this.$refs.paper.getPapers()
          // this.$router.push({path: "/exam-edit/edit/", query: {from: 'index'}});
          break;
        default:
          break;
      }

    }
  },
};
</script>

<style scoped lang="scss">
.qrcode{
  display: inline-block;
  img {
    width: 200px;
    height: 200px;
    background-color: #fff; //设置白色背景色
    padding: 6px; // 利用padding的特性，挤出白边
    box-sizing: border-box;
  }
}
.swiper-container-horizontal > .swiper-pagination-bullets{
  bottom: 0 !important;
}
.swiper-button-prev:after,
.swiper-button-next:after{
  font-size: 20px;
  font-weight: 900;
  width: auto;
}
.swiper-button-prev{
  left: -6px;
  padding: 0;
}
.swiper-button-next{
  right: -6px;
  padding:0;
}
/*::v-deep .scroll-number-item{
  width: 10px !important;
  height: 20px !important;
}*/
/* .scroll-number-item .number-item{
  font-size: 21px;
  font-weight: bold;
}*/
/** 客服*/
.kf{
  position: fixed;right: 15px;
  top: 300px;width: 42px;height: 42px;
  background: rgb(21,135,254);border-radius: 50%;
  text-align: center;cursor: pointer
}
/** 第一行 会员信息、课程、课件、试题、试卷信息*/
.first-row-left{
  float: left;width: 20%;height: 120px;position: relative;
}
.bgI{
  background: url(../assets/images/true.jpg) 55% 50% no-repeat;
}
.first-row-left p:nth-child(1){
  color: white;font-size: 18px;font-weight: bold
}
.first-row-left p:nth-child(2){
  color: white;font-size: 14px;
}
.first-row-left span{
  font-size: 18px;font-weight:900;color:#eea20a;margin: 0 8px
}
.first-row-left button{
  position: absolute;right: 15px;bottom: 28px
}
.first-row-right{
  float: right;width: calc(100% - 20% - 15px);margin-left:15px;height: 120px;display: flex
}
.first-row-right .first-row-right-item{
  flex: 1;display: flex;padding: 0 10px; -webkit-box-align: center;align-items: center;
}
.first-row-right .first-row-right-item .etp-icon{
  background: rgb(230,242,255);border-radius: 50%;width: 48px;height: 48px;text-align: center;margin: 0 auto
}

/** 左侧第二行 快捷入口  */
.second-row{}
.second-row .etp-hover{cursor: pointer;}
.second-row .flex-box {
  display: flex;transition: all .5s ease-in-out;
}

/** 左侧第三行 当前课程/当前考试  */
.third-row{}
.third-row .course-box{
  height: 250px;width: 100%
}
.third-row .course-box .course-box-item-img{
  border-radius: 5px;
  height: 130px;
  transition: all 0.5s ease-in-out;
  width: 100%;
}
.third-row .course-box .course-box-item-img:hover{
  transform:scale(1.05);
}
.third-row .course-box .course-box-item-text{
  text-overflow:ellipsis; display: -webkit-box;
  margin-top: 10px;font-size: 14px;letter-spacing:2px;color: black;
  -webkit-box-orient: vertical;-webkit-line-clamp: 2;overflow: hidden;
}

/** 左侧第四行 热门课件*/
.four-row{}
.four-row .four-row-box{
  width: 100%;height: 220px;
}
.four-row .four-row-box .four-row-box-item-img{
  border-radius: 5px;
  height: 135px;
  transition: all 0.5s ease-in-out;
  width: 100%;
}
.four-row .four-row-box .four-row-box-item-img:hover{
  transform:scale(1.05);
}
.four-row .four-row-box .four-row-box-item-text{
  text-overflow:ellipsis; display: -webkit-box;font-size: 14px;letter-spacing:2px;color: black;
  -webkit-box-orient: vertical;overflow: hidden;-webkit-line-clamp: 5;
}

/** 左侧第五行 活跃学员 */
.five-row{}
.five-row .five-row-box{
  height: 240px;width: 100%;
}
.five-row .five-row-box .five-row-box-item-img{
  height: 140px;border-radius: 5px;
  transition: all 0.5s ease-in-out;
  width: 100%;
}
.five-row .five-row-box .five-row-box-item-img:hover{
  transform:scale(1.05);
}

/** 右侧区域 */
.etp-index-right .etp-index-right-start{
  height: 250px;text-align: center
}
.etp-index-right .etp-index-right-start .etp-index-right-start-url{
  margin: 8px;cursor:pointer;background: rgb(230,242,255);
  border-radius: 5px;padding: 6px;color: blue;font-weight: bold;text-align: center;
  word-wrap: break-word;
}
.etp-index-right .etp-more{
  float: right;color: gray;cursor: pointer
}
.etp-index-right ul li {
  line-height: 32px;
  font-size: 15px;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
}

.etp-index-right .etp-list {
  overflow: hidden;
  height: 630px;
}
.etp-index-right .etp-list .etp-list-time{
  float: right;font-size: 12px;margin: 0
}
.etp-index-right .el-divider--vertical {
  height: 65%;
}
.etp-index-right .el-divider--horizontal {
  margin: 10px 0;
}


/** 容器、公共样式 */
.flex{ display: flex;}
.flex_1{ flex: 1; }
.flex_1_padding_left_15{ flex: 1;padding-left: 15px}
.flex_2{ flex: 2;padding: 0 25px }
.flex_4{ flex: 4; }
.margin_top_15{ margin-top: 8px;color: #909399}
.etp-index-left { width: 80%; height: auto;}
.etp-index-right {
  position: absolute;
  right: 10px;
  margin-left: 15px;
  top: 0;
  width: calc(100% - 80% - 10px - 15px);
}

.etp-box {
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  margin-top: 15px;
  border-radius: 5px;
  padding: 15px;
}

.one-row-hide{
  text-overflow:ellipsis; display: -webkit-box;font-size: 14px;letter-spacing:2px;color: black;
  -webkit-box-orient: vertical;overflow: hidden;-webkit-line-clamp: 1;
}
.home .el-card{
  border: 0;
  border-radius: 5px;
}
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
::v-deep .avue-crud__dialog__menu {
  padding-right: 30px;
  float: left;
}
::v-deep .avue-crud__dialog__menu i {
  color: #909399;
  font-size: 18px;
}
::v-deep .el-icon-full-screen{
  cursor: pointer;
}
::v-deep .el-icon-full-screen:before {
  content: "\e719";
}
::v-deep .avue-crud__dialog__header {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
}
</style>

