<script>
import '@/css/global.css';

export default {
  name: "ApprovalListPage",
  data() {
    return {
      listForm: {
        assignee: '',
      },
      drawerVisible: false,
      taskList: [], // 存放从后端获取的审批管理列表数据
      getListResult: '',

      containerHeight: 200, // 初始高度，根据需要调整
    }
  },
  methods: {
    getList() {
      this.$axios.post(`/list?assignee=${this.listForm.assignee}`).then((response) => {
        this.taskList = response.data;
        this.getListResult = JSON.stringify(response.data, null, 2);

        this.drawerVisible = true; // 显示 el-drawer 弹窗
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    },
    // // el-drawer 关闭前的钩子函数
    // beforeClose(done) {
    //   this.drawerVisible = false; // 关闭 el-drawer 弹窗
    //   done();
    // },
  }
}
</script>

<template>
  <h2>获取审批管理列表表单</h2>
  <!-- 获取审批管理列表表单 -->
  <div class="form-container">
    <el-form :model="listForm" label-width="80px">
      <el-form-item label="审核人ID">
        <el-input v-model="listForm.assignee"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getList">获取待审批管理列表</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="result-container" :style="{ height: containerHeight + 'px' }">
    {{ getListResult }}
  </div>

<!--    <div class="approval-list">-->
<!--      <el-form-item label="返回值">-->
<!--        <el-input type="textarea" :rows="5" :value="getListResult" disabled-->
<!--                  :style="{width: '100%', height: '100%'}"-->
<!--                  style="width: 100%; height: 100%;"></el-input>-->
<!--      </el-form-item>-->
<!--    </div>-->

<!--  &lt;!&ndash; el-drawer 弹窗 &ndash;&gt;-->
<!--  <el-drawer v-model="drawerVisible" title="审批管理列表" :direction="rtl" :before-close="beforeClose" :index="1"-->
<!--             size="30%" height="250px"-->
<!--             style="font-family: Arial, sans-serif;">-->
<!--    <div v-if="taskList.length > 0" style="overflow-y: auto;">-->
<!--      <ul>-->
<!--        <li v-for="item in taskList" :key="item.id" style="margin-bottom: 10px;">{{ item.name }}</li>-->
<!--      </ul>-->
<!--    </div>-->
<!--    <div v-else>-->
<!--      <p>暂无数据</p>-->
<!--    </div>-->
<!--  </el-drawer>-->

</template>