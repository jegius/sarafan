<template>
    <v-app>
        <v-toolbar app>
            <v-toolbar-title>Sarafan</v-toolbar-title>
            <v-btn flat
                   v-if="profile"
                   :disabled="$route.path === '/'"
                   @click="showMessages"
            >
                Messages
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn flat
                   v-if="profile"
                   :disabled="$route.path === '/user'"
                   @click="showProfile"
            >{{profile.name}}
            </v-btn>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-toolbar>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import {addHandler} from "utils/ws";
    import {mapMutations, mapState} from "vuex";

    export default {
        name: "App",
        computed: mapState(['profile']),
        methods: {
            ...mapMutations([
                'addMethodMutations',
                'updateMethodMutations',
                'removeMethodMutations',
                'addCommentMutations'
            ]),
            showMessages() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/user')
            }
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMethodMutations(data.body)
                            break
                        case 'UPDATE':
                            this.updateMethodMutations(data.body)
                            break;
                        case 'REMOVE':
                            this.removeMethodMutations(data.body)
                            break;
                        default:
                            console.error(`Looks like the event type is unknown "${data.eventType}"`)
                    }
                } else if (data.objectType === 'COMMENT') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addCommentMutations(data.body)
                            break
                        default:
                            console.error(`Looks like the event type is unknown "${data.eventType}"`)
                    }
                } else {
                    console.error(`Looks like the event object is unknown "${data.objectType}"`)
                }
            })
        },
        beforeMount() {
            if (!this.profile) {
                this.$router.replace('/auth')
            }
        }
    }
</script>

<style scoped>

</style>
