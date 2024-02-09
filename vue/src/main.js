// createApp(App).mount('#app')
// main.ts
import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import axios from 'axios'
import {createRouter, createWebHashHistory} from 'vue-router';
import MainPage from '@/components/MainPage.vue';
import StartProcessPage from '@/components/StartProcessPage.vue';
import SubmitApprovalPage from '@/components/SubmitApprovalPage.vue';
import ApprovalListPage from '@/components/ApprovalListPage.vue';
import ShowProcessPicPage from '@/components/ShowProcessPicPage.vue';
import LeaderApply from "@/components/LeaderApply.vue";
import ManagerReject from "@/components/ManagerReject.vue";

axios.defaults.baseURL = 'http://localhost:8080';

const imageAxios = axios.create();

const app = createApp(App)

app.config.globalProperties.$axios = axios;
app.config.globalProperties.$imageAxios = imageAxios;

app.use(ElementPlus)

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {path: '/', component: MainPage, name: 'main-page'},
        {path: '/start-process', component: StartProcessPage, name: 'start-process'},
        {path: '/submit-approval', component: SubmitApprovalPage, name: 'submit-approval'},
        {path: '/approval-list', component: ApprovalListPage, name: 'approval-list'},
        {path: '/show-process-pic', component: ShowProcessPicPage, name: 'show-process-pic'},
        {path: '/leader-apply', component: LeaderApply, name: 'leader-apply'},
        {path: '/manager-Reject', component: ManagerReject, name: 'manager-Reject'}
    ],
});

app.use(router);

app.mount('#app')
