<template>
  <div class="form-container">
    <h2>请假流程前端测试页面</h2>

    <!-- 启动请假流程表单 -->
    <el-form :model="startProcessForm" label-width="80px">
      <el-form-item label="用户ID">
        <el-input v-model="startProcessForm.userId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="startProcess">启动请假流程</el-button>
      </el-form-item>
    </el-form>

    <!-- 提交审批表单 -->
    <el-form :model="submitForm" label-width="80px">
      <el-form-item label="用户ID">
        <el-input v-model="submitForm.userId"></el-input>
      </el-form-item>
      <el-form-item label="流程ID">
        <el-input v-model="submitForm.processId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="submit">提交审批</el-button>
      </el-form-item>
    </el-form>

    <!-- 获取审批管理列表表单 -->
    <el-form :model="listForm" label-width="80px">
      <el-form-item label="用户ID">
        <el-input v-model="listForm.assignee"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getList">获取审批管理列表</el-button>
      </el-form-item>
    </el-form>

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

<script>
export default {
  data() {
    return {
      startProcessForm: {
        userId: '',
      },
      submitForm: {
        userId: '',
        processId: '',
      },
      listForm: {
        assignee: '',
      },
      picForm: {
        processId: '',
      },
      picUrl: '',
    };
  },
  methods: {
    startProcess() {
      this.$axios.post(`/startProcess?userId=${this.startProcessForm.userId}`).then((response) => {
        console.log(response.data);
        alert('流程已启动，流程ID: ' + response.data);
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    },
    submit() {
      // console.log('Submitting with User ID:', this.submitForm.userId);
      // console.log('Submitting with Process ID:', this.submitForm.processId);
      //
      // const requestData = {
      //   userId: this.submitForm.userId,
      //   processId: this.submitForm.processId,
      // };
      //
      // console.log('Request Data:', requestData);

      this.$axios.post('/submit', {
        userId: this.submitForm.userId,
        processId: this.submitForm.processId,
      }).then((response) => {
        console.log(response.data);
        alert('审批提交成功！');
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    },
    getList() {
      this.$axios.post(`/list?assignee=${this.listForm.assignee}`).then((response) => {
        this.taskList = response.data;
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    },
    showPic() {
      this.$imageAxios.get(`/pic?processId=${this.picForm.processId}`, {responseType: 'blob'}).then((response) => {
        const blob = new Blob([response.data]);
        this.picUrl = URL.createObjectURL(blob);
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    },
  },
};
</script>

<style>
.form-container {
  width: 25%;
  margin: auto;
}
</style>
