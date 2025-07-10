<script lang="ts" setup>
import { computed, ref } from 'vue'
import { NButton, NImage, NInput, useMessage } from 'naive-ui'
import { useAppStore, useUserStore } from '../../../store'
import type { UserInfo } from '../../../store/modules/user/helper'
import { t } from '../../../locales'
import { onMounted } from 'vue'

interface Emit {
	(event: 'update'): void
}

const emit = defineEmits<Emit>()
const appStore = useAppStore()
const userStore = useUserStore()
const ms = useMessage()
const userInfo = computed(() => userStore.userInfo)
const name = ref(userInfo.value.name ?? '')
let model_path = ref(userInfo.value.model_path ?? '')

function updateUserInfo(options: Partial<UserInfo>) {
	userStore.updateUserInfo(options)
	ms.success(t('common.success'))
}

function openInputModel(){
	let model_file = window.ipcRenderer.sendSync('file_dialog',  'model_file' );
	if(model_file && model_file != 'NULL'){
		model_path = ref(model_file)
		updateUserInfo({ model_path })
		emit('update')
	}
}

function startModel(){
	window.ipcRenderer.invoke('run_llm', model_path.value, '/Users/calvin/Downloads/' ).then((result) => {
		console.log(result)
	})
}

function stopModel(){
		window.ipcRenderer.invoke('close_proc').then((result) => {
			console.log(result)
		})
}

// onMounted(() => {
// 	window.bind_ipc_renderer_on(on_msg_receive)
// })
//
// function on_msg_receive(msg) { // on new msg from backend
// 	alert(msg)
// }

</script>

<template>
	<div class="p-4 space-y-5 min-h-[200px]">
		<div class="space-y-6">
			<div class="flex items-center space-x-4">
				<NButton size="small" text type="primary" @click="openInputModel()">
					{{ $t('setting.select') }}
				</NButton>
			</div>
			<div class="flex items-center space-x-4">
				<span class="flex-shrink-0 w-[60px]">{{ $t('setting.model_title') }}</span>
				<!--          <input :value="model_path" style="width: 400px;" placeholder="请选择模型" />-->
				<NInput v-model:value="model_path" placeholder="请选择模型" />
				<NButton size="small" text type="primary" @click="startModel()">
					{{ $t('setting.start') }}
				</NButton>
			</div>
			<div class="flex items-center space-x-4">
				<NButton size="small" text type="primary" @click="stopModel()">
					{{ $t('setting.stop') }}
				</NButton>
			</div>
		</div>
	</div>
</template>
