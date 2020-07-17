<template>
    <v-card class="my-2">
        <div class="py-3 px-3">
            <v-avatar
                    v-if="message.author && message.author.userpic"
                    size="50px">
                <img :src="message.author.userpic" :alt="message.author.name">
            </v-avatar>

            <v-avatar
                   v-else
                    color="indigo"
                    size="50px">
                <v-icon dark>account_circle</v-icon>
            </v-avatar>
            <span class="pl-3">{{authorName}}</span>
        </div>
        <div class="pt-3">
            <v-card-text primary-title>
                {{message.text}}
            </v-card-text>
        </div>
        <media v-if="message.link" :message="message"></media>
        <v-card-actions>
            <v-btn @click="edit" small flat round>
                Edit
            </v-btn>
            <v-btn icon @click="del" small>
                <v-icon>delete</v-icon>
            </v-btn>
        </v-card-actions>
        <comment-list
                :comments="message.comments"
                :message-id="message.id"
        ></comment-list>
    </v-card>
</template>

<script>
    import {mapActions} from "vuex";
    import Media from "components/media/Media.vue";
    import CommentList from "../comment/CommentList.vue";

    export default {
        name: "MessageRow",
        props: ['message', 'editMessage'],
        components: {
            CommentList,
            Media
        },
        computed: {
            authorName() {
                return this.message.author ?
                    this.message.author.name :
                    "unknown"
            }
        },
        methods: {
            ...mapActions([
                'removeMethodAction'
            ]),
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMethodAction(this.message)
            }
        }
    }
</script>

<style scoped>

</style>
