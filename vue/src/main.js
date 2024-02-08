// createApp(App).mount('#app')
// main.ts
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import axios from 'axios'
import { createRouter, createWebHashHistory } from 'vue-router';
import MainPage from '@/components/MainPage.vue';
import StartProcessPage from '@/components/StartProcessPage.vue';
import SubmitApprovalPage from '@/components/SubmitApprovalPage.vue';
import ApprovalListPage from '@/components/ApprovalListPage.vue';
import ShowProcessPicPage from '@/components/ShowProcessPicPage.vue';

axios.defaults.baseURL = 'http://localhost:8080';

const imageAxios = axios.create();

const app = createApp(App)

app.config.globalProperties.$axios = axios;
app.config.globalProperties.$imageAxios = imageAxios;

app.use(ElementPlus)

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', component: MainPage },
    { path: '/start-process', component: StartProcessPage },
    { path: '/submit-approval', component: SubmitApprovalPage },
    { path: '/approval-list', component: ApprovalListPage },
    { path: '/show-process-pic', component: ShowProcessPicPage },
  ],
});

app.use(router);

app.mount('#app')
