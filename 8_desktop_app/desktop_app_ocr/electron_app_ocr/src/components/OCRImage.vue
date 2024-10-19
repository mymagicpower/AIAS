@import '../assets/css/theme.css';

<template>
    <div  class="animatable_content_box ">
      <div class="left_half">
        <div v-if="in_img" @drop.prevent="onDragFile" @dragover.prevent class="image_area" :class="{ crosshair_cur  : is_inpaint }"  style="height: calc(100% - 200px);  border-radius: 16px; padding:5px;">
          <ImageCanvas ref="inp_img_canvas" :is_inpaint="is_inpaint" :image_source="in_img" canvas_id="textImg" canvas_d_id="textImgd" ></ImageCanvas>
        </div>
        <div v-else @drop.prevent="onDragFile" @dragover.prevent @click="open_input_image" class="image_area" :class="{ pointer_cursor  : true }" style="height: calc(100% - 200px);  border-radius: 16px; padding:5px;">
          <center>
            <p style="margin-top: calc( 50vh - 180px); opacity: 70%;" >点击添加图片</p>
          </center>
        </div>

        <div v-if="in_img" class="l_button" @click="open_input_image" >更换图片</div>
        <div v-if="in_img" class="l_button" @click="in_img =''">清空</div>
        <div class="content_toolbox" style="margin-top:10px; margin-bottom:-10px;">
          <div class="l_button button_medium button_colored" style="float:right ; " @click="do_recognize" >运行</div>
        </div>
        <br>
      </div>


      <div  class="right_half">
        <div style="color:darkblue ; margin-top:50px;">
          <div class="center loader_box">
            <textarea
                id="scroll_text"
                v-model="generation_state_msg"
                placeholder=""
                style="border-radius: 12px 12px 12px 12px; width: calc(100%); resize: none;font-size: 13px; "
                class="form-control"
                :rows="20"></textarea>
          </div>
        </div>

        <div v-if="error" style="color:red ; margin-top:50px;">
          <div class="center loader_box">
            <p>{{error}}</p>
          </div>
        </div>
      </div>

    </div>
</template>
<script>


import ImageCanvas from "../components_bare/ImageCanvas";
import Vue from "vue";


export default {
    name: 'OCRImage',
    props: {
        app_state : Object ,
        stable_diffusion : Object,
    },
    components: {ImageCanvas},
    mounted() {
      window.bind_ipc_renderer_on(this.on_msg_receive)
    },
    computed:{
      this_object(){
        return this;
      }
    },
    watch: {
      'in_img': {
        handler: function() {
          this.is_inpaint = false;
        },
        deep: true
      } ,
    },
    data() {
        return {
            in_img : '',
            output_image : "",
            output_result : "",
            error : "",
            is_inpaint : false,
            is_inprocess : false,
            input_path : '',
            output_path : '',
            generation_state_msg : ''
        };
    },
    methods: {
        on_msg_receive(msg) {
            if (msg.substring(0, 4) == "AIAS"){
              console.log('AIAS: '+ msg);
              this.generation_state_msg = this.generation_state_msg +  msg.substring(5)
              this.$nextTick(() => {
                setTimeout(() => {
                  const textarea = document.getElementById('scroll_text');
                  textarea.scrollTop = textarea.scrollHeight;
                }, 13)
              })
            }
          },

        do_recognize(){
            if(!this.in_img){
              Vue.$toast.default('请添加图片')
              return;
            }

            let that = this;
            that.output_image = "";
            // this.error = "";
            that.is_inprocess = true;
            that.generation_state_msg = "";

            let out_path = "C:\\Windows\\Temp\\";

            window.ipcRenderer.invoke('run_backend_process', this.in_img.split("?")[0] , out_path).then((result) => {
                that.is_inprocess = false;
                if(that.generation_state_msg){
                  that.in_img = out_path + "ocr_result.png";
                }
                console.log(result)
            })
        },
        open_input_image(){
          let img_path = window.ipcRenderer.sendSync('file_dialog',  'img_file' );
          if(img_path && img_path != 'NULL'){
            this.in_img = img_path;
            console.log(this.in_img);
          }
        },
        onDragFile(e){
          if(!e.dataTransfer.files[0].type.startsWith('image/'))
            return;
          let img_path = e.dataTransfer.files[0].path;
          if(img_path && img_path != 'NULL'){
            this.in_img = img_path;
          }
        },
    },
}
</script>
<style>
.left_half{
  position: absolute;
  top :2px;
  bottom: 3px;
  left : 2px ;
  padding: 20px;
  width: calc(100vw / 2 - 10px);
  border-right: 1px solid ;
  border-color: rgba(0,0,0,0.1);
}

@media (prefers-color-scheme: dark) {
  .left_half{
    border-color: #606060;
  }
}

.right_half{
  position: absolute;
  top :2px;
  bottom: 3px;
  right : 3px ;
  padding: 20px;
  width: calc(100vw / 2 - 10px);
  overflow-y: auto;
}

.pointer_cursor{
  cursor: pointer;
}
</style>
<style scoped>
.crosshair_cur{
  cursor: crosshair;
}
</style>
