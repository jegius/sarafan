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
                            <v-flex><b>Email:</b> {{profile.email}}</v-flex>
                            <v-flex><b>LastVisit:</b> {{profile.lastVisit}}</v-flex>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: "Profile",
        computed: mapState(['profile'])
    }
</script>

<style scoped>

</style>
