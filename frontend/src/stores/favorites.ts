import { defineStore } from 'pinia'
import { api } from '../services/api'
import { mockArticles, mockFavorites, mockRecipes } from '../data/mock'
import type { Article, Favorite, Recipe, RecommendationResult } from '../types'

function targetTypeFor(recipe: Recipe) {
  if (recipe.type === 'tea') return 'tea'
  if (recipe.type === 'soup') return 'soup'
  return 'recipe'
}

function sameTarget(item: Favorite, targetType: string, targetId: number) {
  return item.targetType === targetType && item.targetId === targetId
}

export const useFavoriteStore = defineStore('favorites', {
  state: () => ({
    items: [...mockFavorites] as Favorite[],
    loaded: false,
    loading: false,
  }),
  getters: {
    ids: (state) => new Set(state.items.map((item) => `${item.targetType}:${item.targetId}`)),
    grouped: (state) => ({
      recipe: state.items.filter((item) => item.targetType === 'recipe' || item.targetType === 'recommendation'),
      soup: state.items.filter((item) => item.targetType === 'soup'),
      tea: state.items.filter((item) => item.targetType === 'tea'),
      article: state.items.filter((item) => item.targetType === 'article'),
    }),
  },
  actions: {
    async load() {
      if (this.loading) return
      this.loading = true
      try {
        this.items = await api.favorites()
        this.loaded = true
      } finally {
        this.loading = false
      }
    },
    isRecipeFavorite(recipe: Recipe) {
      return this.ids.has(`${targetTypeFor(recipe)}:${recipe.id}`)
    },
    async toggleRecipe(recipe: Recipe) {
      const targetType = targetTypeFor(recipe)
      const existing = this.items.find((item) => sameTarget(item, targetType, recipe.id))
      if (existing) {
        await this.remove(existing.id)
        return
      }
      const favorite = await api.addFavorite({ targetType, targetId: recipe.id })
      this.items = [
        {
          ...favorite,
          title: favorite.title || recipe.name,
          summary: favorite.summary || recipe.summary,
        },
        ...this.items.filter((item) => !sameTarget(item, targetType, recipe.id)),
      ]
    },
    async toggleArticle(articleId: number) {
      const article = mockArticles.find((item) => item.id === articleId)
      if (!article) return
      const existing = this.items.find((item) => sameTarget(item, 'article', articleId))
      if (existing) {
        await this.remove(existing.id)
        return
      }
      const favorite = await api.addFavorite({ targetType: 'article', targetId: article.id })
      this.items = [
        {
          ...favorite,
          title: favorite.title || article.title,
          summary: favorite.summary || article.summary,
        },
        ...this.items.filter((item) => !sameTarget(item, 'article', article.id)),
      ]
    },
    async addRecommendation(result: RecommendationResult) {
      const existing = this.items.find((item) => sameTarget(item, 'recommendation', result.id))
      if (existing) return existing
      const favorite = await api.addFavorite({ targetType: 'recommendation', targetId: result.id })
      const enriched = {
        ...favorite,
        title: favorite.title || result.recipeName,
        summary: favorite.summary || result.reason,
      }
      this.items = [enriched, ...this.items.filter((item) => !sameTarget(item, 'recommendation', result.id))]
      return enriched
    },
    async remove(id: number) {
      this.items = this.items.filter((item) => item.id !== id)
      await api.removeFavorite(id).catch(() => undefined)
    },
    recipeForFavorite(item: Favorite) {
      return mockRecipes.find((recipe) => recipe.id === item.targetId)
    },
    articleForFavorite(item: Favorite): Article | undefined {
      return mockArticles.find((article) => article.id === item.targetId)
    },
  },
})
