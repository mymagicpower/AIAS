<template>
    <div style="height:100%" v-bind:style="style_obj">
        <img @click="open_image_popup( path )"  class="gal_img" :src="'file://' + path " style="max-height: 100% ; max-width: 100%;" >
        <br>
        <div v-if="!hide_extra_save_button" @click="save_image(path)" class="l_button">保存图片</div>
    </div>
</template>
<script>

import {open_popup} from "../utils"

export default {
    name: 'ImageItem',
    props: {
        path : String,
        style_obj:Object,
        app_state:Object,
        hide_extra_save_button : Boolean,
        hide_dropdown : Boolean,
    },
    components: {},
    mounted() {

    },
    data() {
        return {};
    },
    methods: {
        open_image_popup(img){
            open_popup("file://"+img , undefined);
        },

        save_image(generated_image, prompt, seed){
            if(!generated_image)
                return;
            generated_image = generated_image.split("?")[0];
            let out_path = window.ipcRenderer.sendSync('save_dialog', prompt, seed);
            if(!out_path)
                return
            let org_path = generated_image.replaceAll("file://" , "")
            window.ipcRenderer.sendSync('save_file', org_path+"||" +out_path);
        },
    },
}
</script>
<style>
</style>
<style scoped>
</style>
