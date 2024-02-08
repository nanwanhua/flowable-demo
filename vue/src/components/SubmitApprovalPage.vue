<script>
import '@/css/global.css';

export default {
  name: "SubmitApprovalPage.vue",
  data() {
    return {
      submitForm: {
        userId: '',
        processId: '',
      },
      successMessageVisible: false,
    }
  },
  methods: {
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
        // 设置成功提示可见,并在五秒后隐藏
        this.successMessageVisible = true;
        setTimeout(() => {
          this.successMessageVisible = false;
        }, 2000)
        // alert('审批提交成功！');
      }).catch((error) => {
        console.error('请求失败：', error);
      });
    }
  }
}
</script>

<template>
  <div class="form-container">
      <h2>提交审批表单</h2>
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

    <!-- 成功提交提示-->
    <div class="success-message" v-show="successMessageVisible">
      审批提交成功!
    </div>
  </div>
</template>

