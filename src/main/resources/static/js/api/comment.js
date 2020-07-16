import Vue from 'vue'

const comments = Vue.resource('/comment{/id}');

export default {
    add(message) {
        return comments.save({}, message)
    }
}
