package com.rizalfahlepi.moviecatalogue.ui.fragment.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.model.Movie
import com.rizalfahlepi.moviecatalogue.ui.activity.detailmovie.DetailMovieActivity
import com.rizalfahlepi.moviecatalogue.ui.adapter.MovieRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    private lateinit var listener: LoadDataListener

    companion object {
        private const val ARG_TYPE = "type"
        fun newInstance(type: Int): MovieFragment {
            val fragment = MovieFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MovieViewModel::class.java)

        if (arguments != null) {
            when (arguments?.getInt(ARG_TYPE)){
                0 -> {
                    movieViewModel.getMovie().observe(this as LifecycleOwner, Observer {
                        if (it != null) {
                            initData(arguments?.getInt(ARG_TYPE) ?: 0, it)
                            listener.setOnFinishLoadData(arguments?.getInt(ARG_TYPE) ?: 0)
                        }
                    })

                    movieViewModel.setMovies {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        listener.setOnFinishLoadData(arguments?.getInt(ARG_TYPE) ?: 0)
                    }
                }

                1 -> {
                    movieViewModel.getTVSeries().observe(this as LifecycleOwner, Observer {
                        if (it != null) {
                            initData(arguments?.getInt(ARG_TYPE) ?: 1, it)
                            listener.setOnFinishLoadData(arguments?.getInt(ARG_TYPE) ?: 1)
                        }
                    })

                    movieViewModel.setTVSeries {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        listener.setOnFinishLoadData(arguments?.getInt(ARG_TYPE) ?: 1)
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as LoadDataListener
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun initData(type: Int, data: ArrayList<Movie>){
        val movieAdapter = MovieRecyclerViewAdapter(data, type)
        with(recyclerViewMovies){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        movieAdapter.setOnItemClickCallback(object: MovieRecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(movieID: Int) {
                startActivity(Intent(context, DetailMovieActivity::class.java)
                    .putExtra(DetailMovieActivity.EXTRA_TYPE, type)
                    .putExtra(DetailMovieActivity.EXTRA_MOVIE_ID, movieID))
            }
        })
    }

    interface LoadDataListener{
        fun setOnFinishLoadData(position: Int)
    }
}