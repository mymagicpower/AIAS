<script setup lang='ts'>
import { onMounted, ref } from 'vue'
import { NSpin } from 'naive-ui'
import { fetchChatConfig } from '../../../api'
import pkg from '../../../../package.json'
import { SvgIcon } from '../../../components/common'

interface ConfigState {
  timeoutMs?: number
  apiModel?: string
  socksProxy?: string
}

const loading = ref(false)

const config = ref<ConfigState>()

async function fetchConfig() {
  try {
    loading.value = true
    const { data } = await fetchChatConfig<ConfigState>()
    config.value = data
  }
  catch (e) {

  }
  finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchConfig()
})
</script>

<template>
  <NSpin :show="loading">
    <div class="p-4 space-y-4">
      <h2 class="text-xl font-bold">
        Local ChatGpt - {{ pkg.version }}
      </h2>
      <a
        href="https://github.com/WenJing95/chatgpt-web"
        target="_blank"
        class="text-[#4b9e5f] relative flex items-center"
      >
        View Source Code
        <SvgIcon class="text-lg text-[#4b9e5f] ml-1" icon="carbon:logo-github" />
      </a>
      <div class="p-2 space-y-2 rounded-md bg-neutral-100 dark:bg-neutral-700">
        <p v-text="$t(&quot;common.about_head&quot;)" />
        <p v-text="$t(&quot;common.about_body&quot;)" />
      </div>
    </div>
  </NSpin>
</template>
