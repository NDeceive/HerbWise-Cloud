import axios from 'axios'
import {
  decorateArticle,
  decorateRecipe,
  mockArticles,
  mockFavorites,
  mockHome,
  mockRecommendation,
  mockRecipes,
  mockUser,
} from '../data/mock'
import type {
  Article,
  Favorite,
  HealthProfilePayload,
  HomeData,
  Recipe,
  RecipeType,
  RecommendationResult,
  UserProfile,
} from '../types'

const http = axios.create({
  baseURL: '/api',
  timeout: 8000,
})

export const api = {
  login(payload: { phone: string; code: string }) {
    return http
      .post<UserProfile>('/auth/login', payload)
      .then((res) => res.data)
      .catch(() => ({ ...mockUser, phone: payload.phone || mockUser.phone }))
  },
  register(payload: { phone: string; code: string }) {
    return http
      .post<UserProfile>('/auth/register', payload)
      .then((res) => res.data)
      .catch(() => ({ ...mockUser, id: 2, phone: payload.phone || mockUser.phone, nickname: '新用户' }))
  },
  guest() {
    return http
      .post<UserProfile>('/auth/guest')
      .then((res) => res.data)
      .catch(() => ({ id: 0, phone: '', nickname: '游客', avatar: '' }))
  },
  home() {
    return http
      .get<HomeData>('/home')
      .then((res) => ({
        ...res.data,
        recommended: res.data.recommended.map(decorateRecipe),
        seasonal: res.data.seasonal.map(decorateRecipe),
        articles: res.data.articles.map((article, index) =>
          decorateArticle(
            {
              ...article,
              cover: article.cover || mockArticles[index % mockArticles.length].cover,
            },
            index,
          ),
        ),
      }))
      .catch(() => mockHome)
  },
  recipes(type?: RecipeType, category?: string) {
    return http
      .get<Recipe[]>('/recipes', { params: { type, category } })
      .then((res) => res.data.map(decorateRecipe))
      .catch(() =>
        mockRecipes
          .filter((recipe) => !type || recipe.type === type)
          .filter((recipe) => !category || recipe.category === category)
          .map(decorateRecipe),
      )
  },
  articles() {
    return http
      .get<Article[]>('/articles')
      .then((res) => res.data.map((article, index) => decorateArticle(article, index)))
      .catch(() => mockArticles)
  },
  favorites() {
    return http.get<Favorite[]>('/favorites').then((res) => res.data).catch(() => mockFavorites)
  },
  addFavorite(payload: { targetType: string; targetId: number }) {
    return http
      .post<Favorite>('/favorites', payload)
      .then((res) => res.data)
      .catch(() => ({
        id: Date.now(),
        targetType: payload.targetType,
        targetId: payload.targetId,
        title: '新的收藏',
        summary: '已加入本地收藏',
        createdAt: new Date().toISOString(),
      }))
  },
  removeFavorite(id: number) {
    return http.delete<void>(`/favorites/${id}`)
  },
  saveHealthProfile(payload: HealthProfilePayload) {
    return http
      .post<{ profileId: number }>('/ai/profile', payload)
      .then((res) => res.data)
      .catch(() => ({ profileId: Date.now() }))
  },
  recommend(payload: HealthProfilePayload) {
    return http
      .post<RecommendationResult>('/ai/recommend', payload)
      .then((res) => ({
        ...res.data,
        relatedRecipes: res.data.relatedRecipes.map(decorateRecipe),
        teaPairings: res.data.teaPairings.map(decorateRecipe),
      }))
      .catch(() => ({ ...mockRecommendation, id: Date.now() }))
  },
  recommendation(id: string | number) {
    return http
      .get<RecommendationResult>(`/ai/result/${id}`)
      .then((res) => ({
        ...res.data,
        relatedRecipes: res.data.relatedRecipes.map(decorateRecipe),
        teaPairings: res.data.teaPairings.map(decorateRecipe),
      }))
      .catch(() => ({ ...mockRecommendation, id: Number(id) || mockRecommendation.id }))
  },
}
