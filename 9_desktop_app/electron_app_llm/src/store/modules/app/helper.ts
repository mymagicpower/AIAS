import { ss } from '../../../utils/storage'

const LOCAL_NAME = 'appSetting'
const ZH_CN = 'zh-CN'
const EN_US = 'en-US'

export type Theme = 'light' | 'dark' | 'auto'

export type Language = typeof ZH_CN | typeof EN_US

export type focusTextarea = true

export interface AppState {
  siderCollapsed: boolean
  theme: Theme
  language: Language
  focusTextarea: focusTextarea
}

export function defaultSetting(): AppState {
  const browserLanguage = window.navigator.language || ZH_CN
  let defaultLanguage: string
  if (browserLanguage.includes('zh'))
    defaultLanguage = ZH_CN

  else if (browserLanguage.includes('en'))
    defaultLanguage = EN_US

  else
    defaultLanguage = ZH_CN

  return { siderCollapsed: false, theme: 'light', language: defaultLanguage as Language, focusTextarea: true }
}

export function getLocalSetting(): AppState {
  const localSetting: AppState | undefined = ss.get(LOCAL_NAME)
  return { ...defaultSetting(), ...localSetting }
}

export function setLocalSetting(setting: AppState): void {
  ss.set(LOCAL_NAME, setting)
}
