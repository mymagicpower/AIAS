import { computed } from 'vue'
import { enUS, jaJP, zhCN } from 'naive-ui'
import { useAppStore } from '../store'
import { setLocale } from '../locales'

export function useLanguage() {
  const appStore = useAppStore()

  const language = computed(() => {
    switch (appStore.language) {
      case 'zh-CN':
        setLocale('zh-CN')
        return zhCN
      case 'en-US':
        setLocale('en-US')
        return enUS
      case 'ja-JP':
        setLocale('ja-JP')
        return jaJP
      default:
        setLocale('zh-CN')
        return zhCN
    }
  })

  return { language }
}
