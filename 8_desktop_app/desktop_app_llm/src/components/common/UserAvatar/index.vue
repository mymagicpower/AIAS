<script setup lang='ts'>
import { computed } from 'vue'
import { NAvatar } from 'naive-ui'
import { useUserStore } from '../../../store'
import { isString } from '../../../utils/is'
import { SvgIcon } from '../../../components/common'

const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)
</script>

<template>
  <div class="flex items-center">
    <div class="w-10 h-10 overflow-hidden rounded-full">
      <template v-if="isString(userInfo.avatar) && userInfo.avatar.length > 0">
        <NAvatar
          size="large"
          round
          :src="userInfo.avatar"
        />
      </template>
      <template v-else>
        <NAvatar size="large" round />
      </template>
    </div>
    <div class="ml-2">
      <h2 class="font-bold text-md flex ">
        大模型单机版
      </h2>
      <p class="text-xs text-gray-500">
        <span
          v-if="isString(userInfo.description) && userInfo.description !== ''"
          v-text="userInfo.description"
        />
      </p>
    </div>
  </div>
</template>
