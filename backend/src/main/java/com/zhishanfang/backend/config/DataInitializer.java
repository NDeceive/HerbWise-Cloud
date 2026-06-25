package com.zhishanfang.backend.config;

import com.zhishanfang.backend.model.Article;
import com.zhishanfang.backend.model.Favorite;
import com.zhishanfang.backend.model.Recipe;
import com.zhishanfang.backend.model.User;
import com.zhishanfang.backend.repository.ArticleRepository;
import com.zhishanfang.backend.repository.FavoriteRepository;
import com.zhishanfang.backend.repository.RecipeRepository;
import com.zhishanfang.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer {
    @Bean
    @Transactional
    CommandLineRunner seedData(
            UserRepository userRepository,
            RecipeRepository recipeRepository,
            ArticleRepository articleRepository,
            FavoriteRepository favoriteRepository
    ) {
        return args -> {
            User demoUser = userRepository.findByPhone("13800000000").orElseGet(() -> {
                User user = new User();
                user.setPhone("13800000000");
                user.setNickname("张女士");
                user.setAvatar("");
                return userRepository.save(user);
            });

            if (recipeRepository.count() == 0) {
                recipeRepository.saveAll(seedRecipes());
            }

            if (articleRepository.count() == 0) {
                articleRepository.saveAll(seedArticles());
            }

            if (favoriteRepository.count() == 0) {
                List<Recipe> recipes = recipeRepository.findTop6ByOrderByPopularityDesc();
                if (!recipes.isEmpty()) {
                    Favorite recipeFavorite = favorite(demoUser.getId(), targetTypeFor(recipes.get(0)), recipes.get(0).getId());
                    favoriteRepository.save(recipeFavorite);
                }
                recipeRepository.findByTypeOrderByPopularityDesc("tea").stream().findFirst()
                        .map(recipe -> favorite(demoUser.getId(), "tea", recipe.getId()))
                        .ifPresent(favoriteRepository::save);
            }
        };
    }

    private List<Recipe> seedRecipes() {
        return List.of(
                recipe(
                        "当归黄芪乌鸡汤",
                        "soup",
                        "养生",
                        "/images/soup-chicken.jpg",
                        "以当归、黄芪、党参与乌鸡同炖，汤色清润，适合气血不足、疲劳乏力与日常温补调理。",
                        "温补、健脾、补气养血、安神",
                        "当归、黄芪、乌鸡、红枣、枸杞子、党参",
                        "乌鸡焯水后与当归、黄芪、党参同入砂锅，加清水慢炖，出锅前少许盐调味。",
                        "补气养血、健脾益气、温中扶正",
                        "气虚质、疲劳乏力、面色少华、产后调养人群",
                        "感冒发热、口舌生疮或实热明显者不宜多食；孕期与慢性病患者请咨询医师。",
                        "约 2.5 小时",
                        1280,
                        "四季"
                ),
                recipe(
                        "茯苓山药粥",
                        "medicated_food",
                        "祛湿",
                        "/images/porridge-green.jpg",
                        "茯苓健脾渗湿，山药补脾养胃，口感温和，适合湿热、脾胃虚弱与食欲欠佳人群。",
                        "健脾、祛湿、清润、助消化",
                        "茯苓、山药、薏米、粳米、红枣",
                        "茯苓、薏米提前浸泡，与山药、粳米同煮至软糯，可少量加盐或红枣调味。",
                        "健脾祛湿、调理脾胃、改善困倦沉重",
                        "痰湿质、湿热质、脾胃虚弱与湿气偏重人群",
                        "便溏严重或对谷物过敏者谨慎食用，糖尿病人群需控制摄入量。",
                        "约 50 分钟",
                        1120,
                        "夏季"
                ),
                recipe(
                        "莲子百合银耳羹",
                        "medicated_food",
                        "安神",
                        "/images/sweet-soup.jpg",
                        "银耳润燥，莲子养心，百合清心安神，适合失眠多梦、心烦口干的轻养生甜羹。",
                        "养心、安神、润肺、养颜",
                        "莲子、百合、银耳、枸杞子、冰糖",
                        "银耳泡发煮至出胶，再加入莲子、百合与少量冰糖，小火慢炖至软糯。",
                        "养心安神、润肺养颜、改善睡眠",
                        "阴虚质、睡眠欠佳、心烦口干与日常轻补人群",
                        "糖尿病人群减少或不加冰糖，腹泻明显者不宜过量。",
                        "约 45 分钟",
                        1180,
                        "夏季"
                ),
                recipe(
                        "冬瓜薏米汤",
                        "soup",
                        "减脂",
                        "/images/soup-entry.jpg",
                        "冬瓜清爽利水，薏米健脾祛湿，汤味清淡，适合减脂塑形与夏季轻负担饮食。",
                        "清热、利水、祛湿、轻盈",
                        "冬瓜、薏米、茯苓、瘦肉、生姜",
                        "薏米提前浸泡，瘦肉焯水后与冬瓜、茯苓同煮，保持清淡少油。",
                        "清热利水、健脾祛湿、辅助体重管理",
                        "湿气偏重、饮食油腻、希望轻盈调理的人群",
                        "脾胃虚寒、孕期或经期明显不适者谨慎食用。",
                        "约 60 分钟",
                        980,
                        "夏季"
                ),
                recipe(
                        "四物汤",
                        "soup",
                        "养生",
                        "/images/brown-herb-soup.jpg",
                        "当归、川芎、白芍、熟地黄组成的经典调养汤方，重在补血和血。",
                        "补血、调养、温润",
                        "当归、川芎、白芍、熟地黄、红枣",
                        "药食材洗净后加水煎煮，可与鸡肉或排骨同炖，保持温热饮用。",
                        "补血养血、调经和血、改善气色",
                        "血虚、面色萎黄、经后调养人群",
                        "感冒发热、湿热明显或经量过多者谨慎食用。",
                        "约 70 分钟",
                        920,
                        "秋季"
                ),
                recipe(
                        "桂枝汤",
                        "soup",
                        "养生",
                        "/images/side-four-soup.jpg",
                        "桂枝温通，搭配芍药、生姜、红枣，适合风寒后期与日常温养。",
                        "温通、调和、扶正",
                        "桂枝、芍药、生姜、红枣、甘草",
                        "药食材按比例加水煎煮，温热饮用，避免过量久服。",
                        "调和营卫、温通经脉、缓解畏寒",
                        "畏寒、风寒初起后调养与体虚易冷人群",
                        "发热明显、口渴咽痛或阴虚火旺者不宜自行服用。",
                        "约 45 分钟",
                        760,
                        "冬季"
                ),
                recipe(
                        "麻黄汤",
                        "soup",
                        "祛火",
                        "/images/soup-entry.jpg",
                        "经典汤方展示项，适合比赛页面内容陈列；实际使用需遵医嘱。",
                        "发散、宣肺、经典方",
                        "麻黄、桂枝、杏仁、甘草",
                        "本方为经典方展示，不建议自行煎服，请在专业医师指导下使用。",
                        "宣肺散寒、经典方学习展示",
                        "仅用于知识展示与专业指导场景",
                        "高血压、心脏病、孕期、儿童等人群严禁自行使用。",
                        "约 35 分钟",
                        620,
                        "冬季"
                ),
                recipe(
                        "茯苓山药排骨汤",
                        "soup",
                        "祛湿",
                        "/images/soup-entry.jpg",
                        "茯苓、山药与排骨同炖，汤味温和，适合春夏湿重与脾胃调理。",
                        "健脾、祛湿、增强免疫力",
                        "茯苓、山药、排骨、红枣、生姜",
                        "排骨焯水后与茯苓、山药、红枣同炖，出锅前调味。",
                        "健脾祛湿、补中益气、增强体力",
                        "湿气偏重、食欲一般、体力不足人群",
                        "痛风急性期或高尿酸人群需控制肉汤摄入。",
                        "约 90 分钟",
                        1060,
                        "春季"
                ),
                recipe(
                        "玫瑰红枣奶茶",
                        "tea",
                        "养生",
                        "/images/rose-tea.jpg",
                        "玫瑰理气，红枣补血，搭配温润奶香，适合作为日常轻养生茶饮。",
                        "理气、补血、美容养颜",
                        "玫瑰花、红枣、牛奶、红茶、枸杞子",
                        "红枣煮水出味后加入红茶，关火焖泡，再加入温牛奶与玫瑰花。",
                        "理气养血、舒缓情绪、温润养颜",
                        "气色不佳、压力较大、偏好温润茶饮人群",
                        "乳糖不耐受者可换植物奶；湿热上火明显时少饮。",
                        "约 15 分钟",
                        1150,
                        "四季"
                ),
                recipe(
                        "桂圆枸杞茶",
                        "tea",
                        "安神",
                        "/images/cassia-tea.jpg",
                        "桂圆补益心脾，枸杞滋养肝肾，适合熬夜后温养与睡前轻饮。",
                        "补益、养血、安神",
                        "桂圆、枸杞子、红枣、热水",
                        "桂圆、红枣略煮后加入枸杞焖泡，温热饮用。",
                        "补益心脾、养血安神、缓解疲劳",
                        "气血不足、睡眠欠佳、久坐用眼人群",
                        "湿热上火、口舌生疮或血糖控制不佳者少饮。",
                        "约 12 分钟",
                        1010,
                        "冬季"
                ),
                recipe(
                        "陈皮普洱茶",
                        "tea",
                        "祛湿",
                        "/images/chenpi-tea.jpg",
                        "陈皮理气健脾，普洱温和助消化，适合饭后轻饮。",
                        "理气、健脾、助消化",
                        "陈皮、普洱茶、热水",
                        "陈皮洗净与普洱一同冲泡，第一泡润茶后再饮用。",
                        "理气健脾、化湿助消化、饭后解腻",
                        "食积胀满、饭后困倦、湿气偏重人群",
                        "胃酸过多、空腹或睡前不宜浓饮。",
                        "约 10 分钟",
                        940,
                        "四季"
                ),
                recipe(
                        "茯苓安神奶茶",
                        "tea",
                        "安神",
                        "/images/tea-clear.jpg",
                        "茯苓健脾宁心，搭配淡奶香与少量红枣，口感柔和不厚重。",
                        "健脾、宁心、助眠",
                        "茯苓粉、红枣、牛奶、红茶、莲子",
                        "红枣与莲子煮出底汤，加入红茶焖泡，再以温牛奶调和茯苓粉。",
                        "健脾宁心、缓解疲劳、辅助睡眠",
                        "脾虚困倦、轻度睡眠不佳、偏好奶茶口感人群",
                        "乳糖不耐受者可换植物奶；过敏体质先少量尝试。",
                        "约 18 分钟",
                        870,
                        "四季"
                )
        );
    }

    private List<Article> seedArticles() {
        return List.of(
                article(
                        "夏季湿热体质如何清养",
                        "节气养生",
                        "/images/herb-basket.jpg",
                        "从饮食、作息和茶饮三个角度，梳理夏季湿热人群的温和调理方法。",
                        "夏季湿热常见表现包括口苦口黏、困倦沉重、食欲下降。日常可采用清淡饮食、规律作息、适度运动与健脾祛湿茶饮共同调理。",
                        1680,
                        LocalDateTime.now().minusDays(2)
                ),
                article(
                        "气虚人群的日常药膳搭配",
                        "药膳食疗",
                        "/images/recipe-entry.jpg",
                        "气虚不只靠大补，更要看脾胃承受力与日常执行度。",
                        "气虚调养宜循序渐进，可选择黄芪、党参、山药、红枣等温和食材，配合规律睡眠与轻运动。",
                        1328,
                        LocalDateTime.now().minusDays(4)
                ),
                article(
                        "睡眠不佳时，晚间饮食怎么安排",
                        "调理方法",
                        "/images/sweet-soup.jpg",
                        "晚间饮食宜清淡温和，避免浓茶、辛辣与过晚进食。",
                        "睡前两小时尽量停止大量进食，可选择莲子百合银耳羹、桂圆枸杞茶等轻量方案，但需结合个人体质。",
                        1096,
                        LocalDateTime.now().minusDays(7)
                ),
                article(
                        "药食同源入门：先看体质再看功效",
                        "养生指南",
                        "/images/hero-banquet.jpg",
                        "同一种食材并不适合所有人，体质、季节与饮食习惯都应纳入考虑。",
                        "药食同源强调辨体施养。选择药膳时，应综合体质类型、症状表现、季节变化、忌口过敏与慢性病史。",
                        2012,
                        LocalDateTime.now().minusDays(10)
                )
        );
    }

    private Recipe recipe(
            String name,
            String type,
            String category,
            String imageUrl,
            String summary,
            String tags,
            String ingredients,
            String steps,
            String effects,
            String suitablePeople,
            String contraindications,
            String cookingTime,
            Integer popularity,
            String season
    ) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setType(type);
        recipe.setCategory(category);
        recipe.setImageUrl(imageUrl);
        recipe.setSummary(summary);
        recipe.setTags(tags);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        recipe.setEffects(effects);
        recipe.setSuitablePeople(suitablePeople);
        recipe.setContraindications(contraindications);
        recipe.setCookingTime(cookingTime);
        recipe.setPopularity(popularity);
        recipe.setSeason(season);
        return recipe;
    }

    private Article article(
            String title,
            String category,
            String cover,
            String summary,
            String content,
            Integer views,
            LocalDateTime createdAt
    ) {
        Article article = new Article();
        article.setTitle(title);
        article.setCategory(category);
        article.setCover(cover);
        article.setSummary(summary);
        article.setContent(content);
        article.setViews(views);
        article.setCreatedAt(createdAt);
        return article;
    }

    private Favorite favorite(Long userId, String targetType, Long targetId) {
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setTargetType(targetType);
        favorite.setTargetId(targetId);
        return favorite;
    }

    private String targetTypeFor(Recipe recipe) {
        if ("soup".equals(recipe.getType())) {
            return "soup";
        }
        if ("tea".equals(recipe.getType())) {
            return "tea";
        }
        return "recipe";
    }
}
