<template>
    <v-container>
        <v-layout justify-space-around>
            <v-flex :xs6="!$vuetify.breakpoint.xsOnly">
                <h1 class="title mb-3">User profile</h1>
                <v-layout row justify-space-between>
                    <v-flex class="px-1">
                        <v-img :src="profile.userpic"></v-img>
                    </v-flex>
                    <v-flex class="px-1">
                        <v-layout column>
                            <v-flex><b>Name:</b> {{profile.name}}</v-flex>
                            <v-flex><b>Locale:</b> {{profile.locale}}</v-flex>
                            <v-flex><b>Gender:</b> {{profile.gender}}</v-flex>
                            <v-flex><b>LastVisit:</b> {{profile.lastVisit}}</v-flex>
                            <v-flex><b>subscriptions:</b> {{profile.subscriptions && profile.subscriptions.length}}
                            </v-flex>
                            <v-flex><b>subscribers:</b> {{profile.subscribers && profile.subscribers.length}}</v-flex>
                        </v-layout>
                    </v-flex>
                </v-layout>
                <v-btn
                        v-if="!isMyProfile"
                        @click="changeSubscription"
                >
                    {{isISubscribe ? 'Unsubscribe' : 'Subscribe'}}
                </v-btn>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import profileApi from "api/profile";

    export default {
        name: "Profile",
        data() {
            return {
                profile: {}
            }
        },
        computed: {
            isMyProfile() {
                return !this.$route.params.id || this.$route.params.id === this.$store.state.profile.id;
            },
            isISubscribe() {
                return this.profile.subscribers &&
                    this
                        .profile
                        .subscribers
                        .find(subscription => subscription.subscriber === this.$store.state.profile.id)
            }
        },
        watch: {
            '$route'() {
                this.updateProfile()
            }
        },
        methods: {
            async changeSubscription() {
                const data = await profileApi.changeSubscription(this.profile.id)
                this.profile = await data.json()

            },
            async updateProfile() {
                const id = this.$route.params.id || this.$store.state.profile.id;
                const data = await profileApi.get(id)
                this.profile = await data.json();

                this.$forceUpdate();
            }
        },
        beforeMount() {
            this.updateProfile()
        }
    }
</script>

<style scoped>

</style>
