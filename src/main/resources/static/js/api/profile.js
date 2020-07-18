import Vue from 'vue'

const profile = Vue.resource('/profile{/id}');

export default {
    get(id) {
        return profile.get({id})
    },
    changeSubscription(chanelId) {
        return Vue.http.post(`/profile/change-subscription/${chanelId}`)
    }
}
