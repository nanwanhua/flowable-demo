<script>
import '@/css/global.css'

export default {
  name: "LeaderApply",
  data() {
    return {
      leaderApplyForm: {
        audit: "",
        taskId: ""
      },
      successMessageVisible: false,
      leaderApplyResult: ''
    }
  },
  methods: {
    leaderApply() {
      this.$axios.post("/leaderApply", {
        audit: this.leaderApplyForm.audit,
        taskId: this.leaderApplyForm.taskId,
      }).then((response) => {
        this.leaderApplyResult = response.data
        this.successMessageVisible = true;
        setTimeout(() => {
          this.successMessageVisible = false;
        }, 2000)
      }).catch((error) => {
        console.error("请求失败", error);
      })
    }
  }
}
</script>

<template>
  <div class="form-container">
    <h2>组长审核</h2>
    <el-form :modle="leaderApplyForm" label-width="80px">
      <el-form-item label="通过/拒绝">
        <el-select v-model="leaderApplyForm.audit" placeholder="请选择">
          <el-option label="通过" value="通过"></el-option>
          <el-option label="拒绝" value="拒绝"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="taskId">
        <el-input v-model="leaderApplyForm.taskId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="leaderApply">组长审批</el-button>
      </el-form-item>
    </el-form>

    <div class="success-message" v-show="successMessageVisible">
      {{ this.leaderApplyResult }}
    </div>
  </div>
</template>