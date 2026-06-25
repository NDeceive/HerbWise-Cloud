export type RecipeType = 'medicated_food' | 'soup' | 'tea'

export interface UserProfile {
  id: number
  phone: string
  nickname: string
  avatar?: string
}

export interface Recipe {
  id: number
  name: string
  type: RecipeType
  category: string
  imageUrl: string
  summary: string
  tags: string[]
  ingredients: string[]
  steps: string
  effects: string
  suitablePeople: string
  contraindications: string
  cookingTime: string
  popularity: number
  season: string
}

export interface Article {
  id: number
  title: string
  category: string
  cover: string
  summary: string
  views: number
  createdAt: string
}

export interface Favorite {
  id: number
  targetType: string
  targetId: number
  title: string
  summary: string
  createdAt: string
}

export interface HomeData {
  recommended: Recipe[]
  seasonal: Recipe[]
  articles: Article[]
  favoriteSummary: Record<string, number>
}

export interface HealthProfilePayload {
  name: string
  gender: string
  birthDate: string
  age: number | null
  height: number | null
  weight: number | null
  constitutionTypes: string[]
  symptoms: string[]
  diseases: string[]
  tastePreference: string
  dietRegularity: string
  allergies: string[]
  sleepTime: string
  wakeTime: string
  waterIntake: string
  goals: string[]
  extraNote: string
}

export interface RecommendationResult {
  id: number
  recipeName: string
  reason: string
  tags: string[]
  ingredients: string[]
  steps: string[]
  warnings: string[]
  bodyAnalysis: string
  relatedRecipes: Recipe[]
  teaPairings: Recipe[]
  createdAt: string
}
