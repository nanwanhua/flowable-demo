<template>
  <div>
    <h1>Flowable 测试</h1>
    <router-link to="/start-process">
      <button class="navigation-button">启动请假流程</button>
    </router-link>
    <!-- 提交审批按钮 -->
    <router-link to="/submit-approval">
      <button class="navigation-button">提交审批</button>
    </router-link>

    <!-- 获取审批管理列表按钮 -->
    <router-link to="/approval-list">
      <button class="navigation-button">获取审批管理列表</button>
    </router-link>

    <!-- 显示流程图按钮 -->
    <router-link to="/show-process-pic">
      <button class="navigation-button">显示流程图</button>
    </router-link>
    <router-view></router-view>
  </div>
</template>

<script>
import '@/css/global.css'; // 引入全局样式
export default {
  name: 'MainPage',
};
</script>
