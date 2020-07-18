<template>
    <v-layout>
        <v-text-field
                label="New Message"
                placeholder="Write something"
                v-model="text"
                @keyup.enter="save"
        />
        <v-btn @click="save">
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    import {mapActions} from "vuex";

    export default {
        name: "MessageForm",
        props: ['currentMessage'],
        data() {
            return {
                text: '',
                id: null,
            }
        },
        watch: {
            currentMessage(newValue, oldValue) {
                this.text = newValue.text;
                this.id = newValue.id;
            }
        },
        methods: {
            ...mapActions([
                'addMethodAction',
                'updateMethodAction',
            ]),
            save() {
                const message = {
                    id: this.id,
                    text: this.text
                }

                this.id ?
                    this.updateMethodAction(message) :
                    this.addMethodAction(message)

                this.text = ''
                this.id = null
            }
        }
    }
</script>

<style scoped>

</style>
