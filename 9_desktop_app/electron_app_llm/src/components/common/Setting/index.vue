<script setup lang='ts'>
import { computed, ref } from 'vue'
import { NModal, NTabPane, NTabs } from 'naive-ui'
import General from './General.vue'
import Advance from './Advance.vue'
import Model from './Model.vue'
import About from './About.vue'
import { SvgIcon } from '../../../components/common'

const props = defineProps<Props>()

const emit = defineEmits<Emit>()

interface Props {
  visible: boolean
}

interface Emit {
  (e: 'update:visible', visible: boolean): void
}

const active = ref('Model')

const reload = ref(false)

const show = computed({
  get() {
    return props.visible
  },
  set(visible: boolean) {
    emit('update:visible', visible)
  },
})

function handleReload() {
  reload.value = true
  setTimeout(() => {
    reload.value = false
  }, 0)
}
</script>

<template>
  <NModal v-model:show="show" :auto-focus="false" preset="card" style="width: 95%; max-width: 640px">
    <div>
      <NTabs v-model:value="active" type="line" animated>
				<NTabPane name="Model" tab="Model">
					<template #tab>
						<SvgIcon class="text-lg" icon="ri:list-settings-line" />
						<span class="ml-2">{{ $t('setting.model') }}</span>
					</template>
					<Model v-if="!reload" @update="handleReload"/>
				</NTabPane>

        <NTabPane name="Advance" tab="Advance">
          <template #tab>
            <SvgIcon class="text-lg" icon="ri:list-settings-line" />
            <span class="ml-2">{{ $t('setting.advance') }}</span>
          </template>
          <Advance />
        </NTabPane>
        <NTabPane name="General" tab="General">
          <template #tab>
            <SvgIcon class="text-lg" icon="ri:file-user-line" />
            <span class="ml-2">{{ $t('setting.general') }}</span>
          </template>
          <div class="min-h-[100px]">
            <General v-if="!reload" @update="handleReload" />
          </div>
        </NTabPane>
        <NTabPane name="Config" tab="Config">
          <template #tab>
            <SvgIcon class="text-lg" icon="mdi:about-circle-outline" />
            <span class="ml-2">{{ $t('setting.about') }}</span>
          </template>
          <About />
        </NTabPane>
      </NTabs>
    </div>
  </NModal>
</template>
