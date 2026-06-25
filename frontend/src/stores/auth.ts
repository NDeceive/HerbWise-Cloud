import { defineStore } from 'pinia'
import { api } from '../services/api'
import type { UserProfile } from '../types'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as UserProfile | null,
  }),
  actions: {
    async loginAsGuest() {
      this.user = await api.guest()
    },
    logout() {
      this.user = null
    },
  },
})
