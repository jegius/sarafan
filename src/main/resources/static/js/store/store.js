import Vue from "vue";
import Vuex from "vuex";
import messagesApi from "../api/messages";
import commentApi from "../api/comment"

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        messages,
        profile,
        ...frontendData
    },
    getters: {
        sortedMessages(state = []) {
            return state
                .messages
                .sort((first, second) => -(first.id - second.id))
        }
    },
    mutations: {
        addMethodMutations(state, message) {
            state.messages = [
                ...state.messages,
                message
            ]
        },
        updateMethodMutations(state, message) {
            const updateIndex = state.messages.findIndex(item => item.id === message.id);

            state.messages = [
                ...state.messages.slice(0, updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ]
        },
        addCommentMutations(state, comment) {
            const updateIndex = state.messages.findIndex(item => item.id === comment.message.id);
            const message = state.messages[updateIndex];
            message.comments = message.comments || [];

            if (!message.comments.find(it => it.id === comment.id)) {
                state.messages = [
                    ...state.messages.slice(0, updateIndex),
                    {
                        ...message,
                        comments: [
                            ...message.comments,
                            comment
                        ]
                    },
                    ...state.messages.slice(updateIndex + 1)
                ]
            }
        },
        removeMethodMutations(state, message) {
            state.messages = state.messages.filter(item => item.id !== message.id)
        },
        addMessagePageMutations(state, messages) {
           const targetMessages = state
                .messages
                .concat(messages)
                .reduce((res, value) => {
                    res[value.id] = value;
                    return res
                }, {})

            state.messages = Object.values(targetMessages)
        },
        updateTotalPagesMutations(state, totalPages) {
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            state.currentPage = currentPage
        }
    },
    actions: {
        async addMethodAction({commit, state}, message) {
            const result = await messagesApi.add(message)
            const data = await result.json()
            const index = state.messages.findIndex(item => item.id === data.id);

            index > -1 ?
                commit('updateMethodMutations', data) :
                commit('addMethodMutations', data)

        },
        async updateMethodAction({commit}, message) {
            const result = await messagesApi.update(message)
            const data = await result.json()

            commit('updateMethodMutations', data)
        },
        async removeMethodAction({commit}, message) {
            const result = await messagesApi.remove(message)
            if (result.ok) {
                commit('removeMethodMutations', message)
            }
        },
        async addCommentAction({commit, state}, comment) {
            const response = await commentApi.add(comment)
            const data = await response.json()
            commit('addCommentMutations', data)
        },
        async loadPageAction({commit, state}) {
            const response = await messagesApi.page(state.currentPage + 1)
            const date = await response.json()

            commit('addMessagePageMutations', date.messages)
            commit('updateTotalPagesMutations', date.totalPages)
            commit('updateCurrentPageMutation', Math.min(date.currentPage,  date.totalPages - 1))
        }
    }
})
