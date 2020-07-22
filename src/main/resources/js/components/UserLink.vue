<template>
    <router-link class="py-3 px-3" :to="`/user/${user.id}`">
        <v-avatar
                v-if="user && user.userpic"
                :size="`${size || 40}px`">
            <img :src="user.userpic" :alt="user.name">
        </v-avatar>

        <v-avatar
                v-else
                color="indigo"
                :size="`${size || 40}px`">
            <v-icon dark>account_circle</v-icon>
        </v-avatar>
        <span class="pl-3">{{userName}}</span>
    </router-link>
</template>

<script>
    export default {
        name: "UserLink",
        props: ['user', 'size'],
        computed: {
            userName() {
                return this.user ?
                    this.user.name :
                    "unknown"
            }
        }
    }
</script>

<style scoped>

</style>
