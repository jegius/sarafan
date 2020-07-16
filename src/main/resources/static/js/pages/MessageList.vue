<template>
    <v-container>
        <v-layout align-space-around justify-start column>
            <message-form :currentMessage="message"/>
            <message-row v-for="message in sortedMessages"
                         :message="message"
                         :key="message.id"
                         :editMessage="editMessage"
            ></message-row>
        </v-layout>
    </v-container>
</template>

<script>
    import MessageForm from "components/MessageForm.vue";
    import MessageRow from "components/MessageRow.vue";
    import {mapGetters} from "vuex";

    export default {
        components: {
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
