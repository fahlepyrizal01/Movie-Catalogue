package com.rizalfahlepi.myfavoritemovies.ui.fragment

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
import com.rizalfahlepi.myfavoritemovies.ui.adapter.MovieRecyclerViewAdapter
import com.rizalfahlepi.myfavoritemovies.R
import com.rizalfahlepi.myfavoritemovies.model.Movie
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var listener: LoadDataListener

    companion object {
        private const val REQUEST_CODE_DETAIL = 100
        private const val ARG_TYPE = "type"
        fun newInstance(type: Int): FavoriteFragment {
            val fragment = FavoriteFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FavoriteViewModel::class.java)

        if (arguments != null) {
            when (arguments?.getInt(ARG_TYPE)){
                0 -> {
                    movieViewModel.getMovie().observe(this as LifecycleOwner, Observer {
                        if (it != null) {

                            initData(arguments?.getInt(ARG_TYPE) ?: 0, it)
                            listener.setOnFinishLoadData(arguments?.getInt(ARG_TYPE) ?: 0)
                        }
                    })

                    movieViewModel.setMovie(context) {
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

                    movieViewModel.setTVSeries(context) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        listener.setOnFinishLoadData(arguments?.getInt(ARG_TYPE) ?: 1)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_DETAIL){
            listener.setOnDataChange()
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
        if (data.isNotEmpty()){
            textViewEmpty.visibility = View.GONE
        }else{
            textViewEmpty.visibility = View.VISIBLE
        }

        val movieAdapter = MovieRecyclerViewAdapter(data, type)
        with(recyclerViewFavorites){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    interface LoadDataListener{
        fun setOnFinishLoadData(position: Int)
        fun setOnDataChange()
    }
}