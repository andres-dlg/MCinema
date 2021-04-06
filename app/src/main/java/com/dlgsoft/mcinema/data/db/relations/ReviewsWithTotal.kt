package com.dlgsoft.mcinema.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.dlgsoft.mcinema.data.db.models.MovieReviews
import com.dlgsoft.mcinema.data.db.models.Review

data class ReviewsWithTotal(
    @Embedded val movieReviews: MovieReviews,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieReviewId",
        entity = Review::class
    )
    val reviews: List<Review>,
)