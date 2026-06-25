export const contentImages = {
  hero: '/images/real/hero-herbal-soup.jpg',
  login: '/images/real/login-herbal-table.jpg',
  herbBasket: '/images/real/herb-basket.jpg',

  entries: {
    soup: '/images/real/entry-soup.jpg',
    recipe: '/images/real/entry-recipe.jpg',
    tea: '/images/real/entry-tea.jpg',
  },

  banners: {
    recipes: '/images/real/banner-recipe.jpg',
    soups: '/images/real/banner-soup.jpg',
    teas: '/images/real/banner-tea.jpg',
    ai: '/images/real/banner-ai-herbs.jpg',
    articles: '/images/real/banner-articles-tea.jpg',
  },

  recipes: {
    当归黄芪乌鸡汤: '/images/real/soup-chicken.jpg',
    黄芪党参乌鸡汤: '/images/real/soup-chicken.jpg',
    莲子百合银耳羹: '/images/real/sweet-soup.jpg',
    玫瑰红枣奶茶: '/images/real/rose-milk-tea.jpg',
    茯苓山药排骨汤: '/images/real/yam-soup.jpg',
    茯苓山药粥: '/images/real/yam-porridge.jpg',
    四物汤: '/images/real/dark-herbal-soup.jpg',
    桂枝汤: '/images/real/light-herbal-tea.jpg',
    麻黄汤: '/images/real/light-herbal-tea.jpg',
    冬瓜薏米汤: '/images/real/winter-melon-soup.jpg',
    陈皮普洱茶: '/images/real/chenpi-tea.jpg',
    桂圆枸杞茶: '/images/real/goji-tea.jpg',
    茯苓安神奶茶: '/images/real/herbal-milk-tea.jpg',
  } as Record<string, string>,

  articles: {
    default: '/images/real/article-herbal-tea.jpg',
    season: '/images/real/article-season.jpg',
    diet: '/images/real/article-diet.jpg',
    soup: '/images/real/article-soup.jpg',
  },
}

const unsafeImageMarkers = [
  'assets/reference-ui',
  'reference-ui',
  'cropped',
  'screenshot',
  '屏幕截图',
  '智膳坊首页界面设计',
  '智膳坊健康管理登录界面',
  '个性化药膳推荐',
  'ai药膳健康问卷',
  'AI药膳个人信息填写流程图',
  '/images/brown-herb-soup.jpg',
  '/images/cassia-tea.jpg',
  '/images/chenpi-tea.jpg',
  '/images/herb-basket.jpg',
  '/images/login-soup-table.jpg',
  '/images/porridge-green.jpg',
  '/images/recipe-entry.jpg',
  '/images/soup-entry.jpg',
  '/images/tea-entry.jpg',
]

export function isUnsafeContentImage(url?: string) {
  if (!url) return true
  return unsafeImageMarkers.some((marker) => url.includes(marker))
}

export function safeContentImage(url: string | undefined, fallback: string) {
  if (!url || isUnsafeContentImage(url)) return fallback
  return url
}
