package ru.ktsstudio.common.domain.filter

import com.badoo.mvicore.element.NewsPublisher

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterNewsPublisher: NewsPublisher<FilterFeature.Wish, FilterFeature.Effect, FilterFeature.State, FilterFeature.News> {
    override fun invoke(
        action: FilterFeature.Wish,
        effect: FilterFeature.Effect,
        state: FilterFeature.State
    ): FilterFeature.News? {
        return when(effect) {
            FilterFeature.Effect.FilterApplied -> FilterFeature.News.FilterApplied
            else -> null
        }
    }
}