package com.dlgsoft.mcinema.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.dlgsoft.mcinema.data.db.models.Genre
import com.dlgsoft.mcinema.data.db.models.Movie

data class MovieWithGenres(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId",
        entity = Genre::class
    )
    val genres: List<Genre>?,
)