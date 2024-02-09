<template>
  <div class="main-page">
    <h1>Flowable 测试</h1>
    <div class="button-container">
      <div class="fixed-button-container">
        <router-link to="/start-process">
          <button class="navigation-button">启动请假流程</button>
        </router-link>
<!--        <router-view v-if="activeComponent === 'start-process'"></router-view>-->
      </div>

      <!-- 提交审批按钮 -->
      <div class="fixed-button-container">
        <router-link to="/submit-approval">
          <button class="navigation-button">提交审批</button>
        </router-link>
<!--        <router-view v-if="activeComponent === 'submit-approval'"></router-view>-->
      </div>

      <!-- 获取审批管理列表按钮 -->
      <div class="fixed-button-container">
        <router-link to="/approval-list">
          <button class="navigation-button">获取审批管理列表</button>
        </router-link>
<!--        <router-view v-if="activeComponent === 'approval-list'"></router-view>-->
      </div>

      <!-- 显示流程图按钮 -->
      <div class="fixed-button-container">
        <router-link to="/show-process-pic">
          <button class="navigation-button">显示流程图</button>
        </router-link>
<!--        <router-view v-if="activeComponent === 'show-process-pic'"></router-view>-->
      </div>

      <div class="fixed-button-container">
        <router-link to="/leader-apply">
          <button class="navigation-button">组长审批</button>
        </router-link>
<!--        <router-view v-if="activeComponent === 'leader-apply'"></router-view>-->
      </div>

      <div class="fixed-button-container">
        <router-link to="/manager-Reject">
          <button class="navigation-button">经理审批</button>
        </router-link>
<!--        <router-view v-if="activeComponent === 'manager-Reject'"></router-view>-->
      </div>
    </div>

    <div class="content-container">
      <router-view v-if="activeComponent !== ''"></router-view>
    </div>
  </div>
</template>

<script>
import '@/css/global.css'; // 引入全局样式
export default {
  name: 'MainPage',
  data() {
    return {
      activeComponent: '',
    };
  },
  watch: {
    $route(to) {
      this.activeComponent = to.name || '';
    },
  },
};
</script>

<style>

.content-container {
  width: 100%;
  max-width: 100%; /* 设置最大宽度，根据需要调整 */
  margin: auto;
}

.main-page {
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平居中 */
}

.button-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center; /* 水平居中 */
}

.navigation-button {
  margin: 5px; /* 根据需要调整按钮之间的间距 */
}
</style>
