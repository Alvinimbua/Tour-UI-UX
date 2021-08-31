package com.sriyank.globotour.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sriyank.globotour.R
import com.sriyank.globotour.city.City
import com.sriyank.globotour.city.VacationSpots
import java.util.*
import kotlin.collections.ArrayList


class FavoriteFragment : Fragment() {

    private lateinit var  favoriteCityList : ArrayList<City>
    private  lateinit var  favoriteAdapter : FavoriteAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        setupRecyclerView(view)

        return view
    }

    private fun setupRecyclerView(view: View) {

        val context = requireContext()

         favoriteCityList = VacationSpots.favoriteCityList as ArrayList<City>
        //initialising adapter and passing required parameter
         favoriteAdapter = FavoriteAdapter(context, favoriteCityList)

        //link recycler view with adapter
         recyclerView = view.findViewById<RecyclerView>(R.id.favorite_recycler_view)
        recyclerView.adapter = favoriteAdapter
        recyclerView.setHasFixedSize(true)

        //define layout manager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager

        //attach itemTouchHelper instance,the property to our recycler view
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
        //method executed when you drag an item into the recycler view
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            targetViewHolder: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = targetViewHolder.adapterPosition

            Collections.swap(favoriteCityList,fromPosition, toPosition)

            //notifyItemMove method will notify the adapter that this item was moved from this pos to this pos
            recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)

            return true
        }

        //executed when you swipe an item
        //first param instance of item which is getting swiped (viewholder), second param direction in which the item is being swiped
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //using viewholder object we can get position of that item by using adapter position property
            val position = viewHolder.adapterPosition
            //[]is an index operator and can be used instead of the .get(position)
            //retrieving that deleted city object
            val deletedCity : City = favoriteCityList[position]

            deletedItem(position) //calling this function to delete item form the recycler
            //view
            //after deleting we update the city list
            updateCityList(deletedCity, isFavorite = false)

            Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                        undoDelete(position, deletedCity)
                    updateCityList(deletedCity,true)

                    }
                .show()
        }

    })

    private fun deletedItem(position:Int) {
        //remove item from arraylist
        favoriteCityList.removeAt(position)
        //notify the adapter about the change
        favoriteAdapter.notifyItemRemoved(position)
        favoriteAdapter.notifyItemRangeChanged(position, favoriteCityList.size)
    }

    private fun updateCityList(deletedCity : City, isFavorite: Boolean) {
        //get the city list
        val cityList = VacationSpots.cityList!!
        //get the position in that original list
        val position = cityList.indexOf(deletedCity)
        //update favorite property to false
        cityList[position].isFavorite = isFavorite
    }

    private fun undoDelete(position: Int, deletedCity: City) {
        //adding deleted object back to favorite list
        favoriteCityList.add(position, deletedCity)
        //notify the adapter about that change
        favoriteAdapter.notifyItemInserted(position)
        favoriteAdapter.notifyItemRangeChanged(position, favoriteCityList.size)
    }
}
