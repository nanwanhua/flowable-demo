// createApp(App).mount('#app')
// main.ts
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8080';

const imageAxios = axios.create();

const app = createApp(App)

app.config.globalProperties.$axios = axios;
app.config.globalProperties.$imageAxios = imageAxios;




app.use(ElementPlus)
app.mount('#app')
