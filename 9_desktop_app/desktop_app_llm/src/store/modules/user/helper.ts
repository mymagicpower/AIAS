import { ss } from '../../../utils/storage'

const LOCAL_NAME = 'userStorage'

export interface UserInfo {
  avatar: string
  name: string
  description: string
	model_path: string
  chatgpt_top_p: number
  chatgpt_memory: number
}

export interface UserState {
  userInfo: UserInfo
}

export function defaultSetting(): UserState {
  return {
    userInfo: {
      avatar: 'https://api.multiavatar.com/0.8481955987976837.svg',
      name: 'Local ChatGPT',
			model_path: '',
      description: '',
      chatgpt_top_p: 100,
      chatgpt_memory: 50,
    },
  }
}

export function getLocalState(): UserState {
  const localSetting: UserState | undefined = ss.get(LOCAL_NAME)
  return { ...defaultSetting(), ...localSetting }
}

export function setLocalState(setting: UserState): void {
  ss.set(LOCAL_NAME, setting)
}
