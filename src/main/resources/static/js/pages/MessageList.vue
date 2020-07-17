<template>
    <v-container>
        <v-layout align-space-around justify-start column>
            <message-form :currentMessage="message"/>
            <message-row v-for="message in sortedMessages"
                         :message="message"
                         :key="message.id"
                         :editMessage="editMessage"
            ></message-row>
            <lazy-loader></lazy-loader>
        </v-layout>
    </v-container>
</template>

<script>
    import MessageForm from "components/messages/MessageForm.vue";
    import MessageRow from "components/messages/MessageRow.vue";
    import {mapGetters} from "vuex";
    import LazyLoader from "components/LazyLoader.vue";

    export default {
        components: {
            LazyLoader,
            MessageForm,
            MessageRow
        },
        name: "MessageList",
        data() {
            return {
                message: null
            }
        },
        computed: mapGetters(['sortedMessages']),
        methods: {
            editMessage(message) {
                this.message = message;
            },
        }
    }
</script>

<style scoped>

</style>
