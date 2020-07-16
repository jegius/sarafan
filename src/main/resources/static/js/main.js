import Vue from 'vue'
import 'api/resource'
import App from "pages/App.vue"
import {connect} from "./utils/ws"
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'vuetify/dist/vuetify.min.css'
import store from 'store/store'
import router from 'router/router'

if (frontendData.profile) {
    connect()
}

Vue.use(Vuetify)

new Vue({
    el: '#app',
    store,
    router,
    render: render => render(App)
})
