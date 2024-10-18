<script lang="ts" setup>
import { computed, ref } from 'vue'
import { NButton, NImage, NInput, useMessage } from 'naive-ui'
import type { Language, Theme } from '../../../store/modules/app/helper'
import { HoverButton, SvgIcon } from '../../../components/common'
import { useAppStore, useUserStore } from '../../../store'
import type { UserInfo } from '../../../store/modules/user/helper'
import { t } from '../../../locales'
import Vue from "vue";

interface Emit {
  (event: 'update'): void
}

const emit = defineEmits<Emit>()

const appStore = useAppStore()
const userStore = useUserStore()

const ms = useMessage()

const theme = computed(() => appStore.theme)

const userInfo = computed(() => userStore.userInfo)

const avatar = ref(userInfo.value.avatar ?? '')

const name = ref(userInfo.value.name ?? '')

const description = ref(userInfo.value.description ?? '')

const language = computed({
  get() {
    return appStore.language
  },
  set(value: Language) {
    appStore.setLanguage(value)
  },
})

const themeOptions: { label: string; key: Theme; icon: string }[] = [
  {
    label: 'Auto',
    key: 'auto',
    icon: 'ri:contrast-line',
  },
  {
    label: 'Light',
    key: 'light',
    icon: 'ri:sun-foggy-line',
  },
  {
    label: 'Dark',
    key: 'dark',
    icon: 'ri:moon-foggy-line',
  },
]

const languageOptions: { label: string; key: Language; value: Language }[] = [
  { label: '中文', key: 'zh-CN', value: 'zh-CN' },
  { label: 'English', key: 'en-US', value: 'en-US' },
]

function updateUserInfo(options: Partial<UserInfo>) {
  userStore.updateUserInfo(options)
  ms.success(t('common.success'))
}

function randomAvatar() {
  const avatar = `https://api.multiavatar.com/${Math.random()}.svg`
  userStore.updateUserInfo({ avatar })
  ms.success(t('common.success'))
}

function handleReset() {
  userStore.resetUserInfo()
  ms.success(t('common.success'))
  emit('update')
}
</script>

<template>
  <div class="p-4 space-y-5 min-h-[200px]">
    <div class="space-y-6">
      <div class="flex items-center space-x-4">
        <span class="flex-shrink-0 w-[60px]">{{ $t('setting.avatarLink') }}</span>
        <div class="w-[200px]">
          <NInput v-model:value="avatar" placeholder="" />
        </div>
        <NButton size="small" text type="primary" @click="updateUserInfo({ avatar })">
          {{ $t('common.save') }}
        </NButton>
        <HoverButton :tooltip="$t('setting.randomAvatar')" @click="randomAvatar()">
          <span class="text-xl text-[#4f555e] dark:text-white">
            <SvgIcon class="text-lg" icon="mdi:dice-5-outline" />
          </span>
        </HoverButton>
      </div>
      <div class="flex items-center space-x-4">
        <span class="flex-shrink-0 w-[60px]" />
        <div class="w-[200px]">
          <NImage
            v-model:src="userInfo.avatar"
            width="100"
            class="rounded-full"
          />
        </div>
      </div>

      <div class="flex items-center space-x-4">
        <span class="flex-shrink-0 w-[60px]">{{ $t('setting.name') }}</span>
        <div class="w-[200px]">
          <NInput v-model:value="name" placeholder="" />
        </div>
        <NButton size="small" text type="primary" @click="updateUserInfo({ name })">
          {{ $t('common.save') }}
        </NButton>
      </div>
      <div class="flex items-center space-x-4">
        <span class="flex-shrink-0 w-[60px]">{{ $t('setting.description') }}</span>
        <div class="flex-1">
          <NInput v-model:value="description" placeholder="" />
        </div>
        <NButton size="small" text type="primary" @click="updateUserInfo({ description })">
          {{ $t('common.save') }}
        </NButton>
      </div>
      <div class="flex items-center space-x-4">
        <span class="flex-shrink-0 w-[60px]">{{ $t('setting.theme') }}</span>
        <div class="flex flex-wrap items-center gap-4">
          <template v-for="item of themeOptions" :key="item.key">
            <NButton
              size="small"
              :type="item.key === theme ? 'primary' : undefined"
              @click="appStore.setTheme(item.key)"
            >
              <template #icon>
                <SvgIcon :icon="item.icon" />
              </template>
            </NButton>
          </template>
        </div>
      </div>
      <div class="flex items-center space-x-4">
        <span class="flex-shrink-0 w-[60px]">{{ $t('setting.language') }}</span>
        <div class="flex flex-wrap items-center gap-4">
          <template v-for="item of languageOptions" :key="item.key">
            <NButton
              size="small"
              :type="item.key === language ? 'primary' : undefined"
              @click="appStore.setLanguage(item.key)"
            >
              {{ item.label }}
            </NButton>
          </template>
        </div>
      </div>
      <div class="flex items-center space-x-4">
        <span class="flex-shrink-0 w-[60px]">{{ $t('setting.resetUserInfo') }}</span>
        <NButton size="small" text type="primary" @click="handleReset">
          {{ $t('common.reset') }}
        </NButton>
      </div>
    </div>
  </div>
</template>
