import Vue from 'vue'

const messages = Vue.resource('/message{/id}');

export default {
    add(message) {
        return messages.save({}, message)
    },
    update(message) {
        return messages.update({id: message.id}, message)
    },
    remove({id}) {
        return messages.remove({id})
    }
}
