
import { defineStore } from 'pinia'
import type { UserState } from './model/userModel'
import type { UserInfo } from '@/api/user/types'
import { RESEETSTORE } from '@/utils/reset'

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    token: '',
    userInfo: {} as UserInfo,
  }),
  actions: {
    // setToken
    setToken(token: string) {
      this.token = token
    },
    // setUserInfo
    setUserInfo(userInfo: UserInfo) {
      this.userInfo = userInfo
    },
    async Logout() {
      // await logout()
      RESEETSTORE()
    },
  },
  // 设置为true，缓存state
  persist: true,
})
