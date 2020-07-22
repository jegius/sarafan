import Vue from 'vue'
import 'api/resource'
import App from "pages/App.vue"
import {connect} from "./utils/ws"
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'vuetify/dist/vuetify.min.css'
import store from 'store/store'
import router from 'router/router'
import * as Sentry from '@sentry/browser'
import { Vue as VueIntegration } from '@sentry/integrations'

Sentry.init({
    dsn: 'https://c6ccc331a21c4472a5409d80303c2bc6@o422273.ingest.sentry.io/5346950',
    integrations: [new VueIntegration({Vue, attachProps: true})],
});

Sentry.configureScope(scope => scope.setUser({
    id: profile && profile.id,
    username: profile && profile.name
}))

if (profile) {
    connect()
}

Vue.use(Vuetify)

new Vue({
    el: '#app',
    store,
    router,
    render: render => render(App)
})

