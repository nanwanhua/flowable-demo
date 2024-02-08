<script>
import '@/css/global.css';
export default {
  name: "ShowProcessPicPage",
  data() {
    return {
      picForm: {
        processId: '',
      },
      picUrl: '',
    }
  },
  methods: {
    showPic() {
      this.$imageAxios.get(`/pic?processId=${this.picForm.processId}`, {responseType: 'blob'}).then((response) => {
        const blob = new Blob([response.data]);
        this.picUrl = URL.createObjectURL(blob);
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    }
  }
}
</script>

<template>
  <div class="form-container">
    <!-- 显示流程图表单 -->
    <el-form :model="picForm" label-width="80px">
      <el-form-item label="流程ID">
        <el-input v-model="picForm.processId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="showPic">显示流程图</el-button>
      </el-form-item>
    </el-form>
    <el-image :src="picUrl" style="max-width: 100%;" v-if="picUrl"></el-image>
  </div>
</template>