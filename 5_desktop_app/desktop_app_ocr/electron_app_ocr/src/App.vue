@import '../assets/css/theme.css';
<template>
    <div id="app">
        <div v-if="app_state.is_start_screen">
            <transition name="slide_show">
                <SplashScreen v-if="app_state.show_splash_screen"></SplashScreen>
            </transition>
        </div>
        <ApplicationFrame ref="app_frame" v-else title="文字识别"

            @menu_item_click_about="show_about"
            @menu_item_click_help="open_url('http://aias.top/')"
            @menu_item_click_close="close_window"
        >

            <template v-slot:ocr_img>
              <OCRImage  :app_state="app_state" ref="ocr_img"></OCRImage>
            </template>

            <template v-slot:logs>
                <div class="animatable_content_box ">
                    <p>Logs : </p>
                    <p>
                        <span style="white-space: pre-line">{{app_state.logs}}</span>
                    </p>
                </div>

            </template>
        </ApplicationFrame>

    </div>
</template>
<script>

import {native_alert } from "./native_functions_vue_bridge.js"
import SplashScreen from './components_bare/SplashScreen.vue'
import ApplicationFrame from './components_bare/ApplicationFrame.vue'
import OCRImage from './components/OCRImage.vue'

import Vue from "vue"

native_alert;

export default

{
    name: 'App',
    components: {
        SplashScreen,
        ApplicationFrame,
        OCRImage
    },

    mounted() {

        if( require('../package.json').is_dev || require('../package.json').build_number.includes("dev") )
            alert("不检查更新")
        // else
        //     this.check_for_updates()

        let that = this;

        setTimeout( function(){

            that.app_state.is_start_screen = false;
        }  , 4000)

        let data = window.ipcRenderer.sendSync('load_data');
        if(!data.history){
            data.history = {}
        }
        if(!data.settings){
            data.settings = {}
        }

        if(!data.custom_models){
            data.custom_models = {}
        }
        if( data ){
            Vue.set(this.app_state , 'app_data' , data)
        }

    },

    watch: {
        'app_state.is_start_screen': {
            handler: function(new_value) {
                if (new_value == false) {
                    if(this.is_screen_frozen){
                        window.ipcRenderer.sendSync('unfreeze_win', '');
                        this.is_screen_frozen = false;
                    }
                }
                else{
                    if(!this.is_screen_frozen){
                        window.ipcRenderer.sendSync('freeze_win', '');
                        this.is_screen_frozen = true;
                    }
                }
            },
            deep: true
        } ,

        'app_state.app_data': {
            handler: function(new_value) {
                window.ipcRenderer.sendSync('save_data', new_value );
            },
            deep: true
        } ,


    },

    computed : {
    },

    methods: {
        show_about(){
            window.ipcRenderer.sendSync('show_about', '');
        },
        open_url(url){
            window.ipcRenderer.sendSync('open_url', url);
        } ,
        close_window(){
            window.ipcRenderer.sendSync('close_window', '');
        }
    },

    data() {
        let app_state = {
            is_start_screen: true,
            app_object : this ,
            should_show_dialog_on_quit : false ,
            show_dialog_on_quit_msg : "" ,
            show_splash_screen : true ,
            logs : "",
            global_loader_modal_msg : "",
            app_data: {history : {}},
        };

        return {
            is_mounted : false,
            app_state: app_state,
            is_screen_frozen : true ,
            is_dev : require('../package.json').is_dev ||  require('../package.json').build_number.includes("dev") ,
        }
    },



}
</script>
<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    /*margin-top: 60px;*/
}

body {
    margin: 0;
    padding: 0;
}
</style>
