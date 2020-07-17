<template>
    <span></span>
</template>

<script>
    import {mapActions} from "vuex";

    export default {
        name: "LazyLoader",
        methods: mapActions([
            'loadPageAction'
        ]),
        mounted() {
            window.onscroll = () => {
                const element = document.documentElement
                const isBottomOfScreen = element.scrollTop + window.innerHeight === element.offsetHeight

                if (isBottomOfScreen) {
                    this.loadPageAction()
                }
            }
        },
        beforeDestroy() {
            window.onscroll = null
        }
    }
</script>

<style scoped>

</style>
