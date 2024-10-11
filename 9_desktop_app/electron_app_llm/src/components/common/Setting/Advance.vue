<script lang="ts" setup>
import { computed, ref } from 'vue'
import { NRadioButton, NRadioGroup, NSlider, useMessage } from 'naive-ui'
import { useUserStore } from '../../../store'
import type { UserInfo } from '../../../store/modules/user/helper'
import { t } from '../../../locales'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

const chatgpt_top_p = ref(userInfo.value.chatgpt_top_p ?? 100)
const chatgpt_memory = ref(userInfo.value.chatgpt_memory ?? 100)

const ms = useMessage()

function updateChatgptParams(options: Partial<UserInfo>) {
  userStore.updateUserInfo(options)
  ms.success(t('common.success'))
}
</script>

<template>
  <div class="p-4 space-y-5 min-h-[200px]">
    <div class="space-y-6">
      <div class="flex flex-wrap items-center gap-4">
        <span class="flex-shrink-0 w-[100px]">{{ $t('setting.chatgpt_memory_title') }}</span>
        <div class="w-[300px]">
          <NSlider
            v-model:value="chatgpt_memory"
            :marks="{
              1: $t('setting.chatgpt_memory_choice_1'),
              50: $t('setting.chatgpt_memory_choice_2'),
              100: $t('setting.chatgpt_memory_choice_3'),
            }"
            step="mark"
            @update:value="updateChatgptParams({ chatgpt_memory })"
          />
        </div>
      </div>
      <div class="flex flex-wrap items-center gap-4">
        <span class="flex-shrink-0 w-[100px]" />
        <div class="w-[300px]  text-gray-500">
          {{ $t('setting.chatgpt_memory_memo') }}
        </div>
      </div>
      <div class="flex flex-wrap items-center gap-4">
        <span class="flex-shrink-0 w-[100px]">{{ $t('setting.chatgpt_top_p_title') }}</span>
        <div class="w-[400px] text-gray-500">
          <NRadioGroup
            v-model:value="chatgpt_top_p"
            name="radiobuttongroup2"
            size="medium"
            @update:value="updateChatgptParams({ chatgpt_top_p })"
          >
            <NRadioButton
              :key="0"
              :value="0"
            >
              {{ $t('setting.chatgpt_top_p_choice_1') }}
            </NRadioButton>
            <NRadioButton
              :key="50"
              :value="50"
            >
              {{ $t('setting.chatgpt_top_p_choice_2') }}
            </NRadioButton>
            <NRadioButton
              :key="100"
              :value="100"
            >
              {{ $t('setting.chatgpt_top_p_choice_3') }}
            </NRadioButton>
          </NRadioGroup>
        </div>
      </div>
      <div class="flex flex-wrap items-center gap-4">
        <span class="flex-shrink-0 w-[100px]" />
        <div class="w-[400px] text-gray-500">
          <span v-if="0 === chatgpt_top_p">
            {{ $t('setting.chatgpt_top_p_1_memo') }}
          </span>
          <span v-else-if="50 === chatgpt_top_p">
            {{ $t('setting.chatgpt_top_p_2_memo') }}
          </span>
          <span v-else>
            {{ $t('setting.chatgpt_top_p_3_memo') }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>
