<script>
import '@/css/global.css'; // 引入全局样式
export default {
  data() {
    return {
      startProcessForm: {
        userId: '',
      },
      getResult: '',
      containerHeight: 200, // 初始高度，根据需要调整
    };
  }, methods: {
    startProcess() {
      this.$axios.post(`/startProcess?userId=${this.startProcessForm.userId}`).then((response) => {
        console.log(response.data);
        this.getResult = JSON.stringify(response.data, null, 0);
        // alert('流程已启动，流程ID: ' + response.data);
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    }
  }
}
</script>

<template>
  <div class="form-container">
    <h2>启动请假流程</h2>
    <!-- 启动请假流程表单 -->
    <el-form :model="startProcessForm" label-width="80px">
      <el-form-item label="用户ID">
        <el-input v-model="startProcessForm.userId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="startProcess">启动请假流程</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="result-container" :style="{ height: containerHeight + 'px' }">
    {{ getResult }}
  </div>

</template>