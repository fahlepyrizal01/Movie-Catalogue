package com.rizalfahlepi.myfavoritemovies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rizalfahlepi.myfavoritemovies.R
import com.rizalfahlepi.myfavoritemovies.api.TheMovieDBApi
import com.rizalfahlepi.myfavoritemovies.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MovieRecyclerViewAdapter(private val movies: ArrayList<Movie>, private val type: Int) : RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>() {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val simpleDateFormatView = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                Glide.with(context).load(TheMovieDBApi.getPosterMovie(movie.posterPath ?: "")).into(imageViewPosterMovie)
                when(type){
                    0 -> {
                        textViewTitleMovie.text = movie.title
                        textViewDateMovie.text = try {
                            simpleDateFormatView.format(simpleDateFormat.parse(movie.releaseDate ?: "") ?: Date())
                        }catch (e: Exception){
                            e.printStackTrace()
                            movie.releaseDate
                        }
                    }

                    1 -> {
                        textViewTitleMovie.text = movie.name
                        textViewDateMovie.text = try {
                            simpleDateFormatView.format(simpleDateFormat.parse(movie.firstAirDate ?: "") ?: Date())
                        }catch (e: Exception){
                            e.printStackTrace()
                            movie.firstAirDate
                        }
                    }
                }
                textViewScoreMovie.text = movie.voteAverage.toString()
                textViewOverviewMovie.text = movie.overview
            }
        }
    }
}