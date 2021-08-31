package com.sriyank.globotour.city

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sriyank.globotour.R
import kotlin.math.log

class CityAdapter(val context: Context, var cityList: ArrayList<City>) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    //create primary constructor matching    //superclass
    //inner keyword so that view holder class can use the properties of city adapter class


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {

        Log.i("CityAdapter", "onCreateViewHolder: ViewHolder created")
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item_city, parent, false)
        return CityViewHolder(itemView)
    }

    override fun onBindViewHolder(cityViewHolder: CityViewHolder, position: Int) {

        //for each item on the list the bind view holder method is called

        Log.i("CityAdapter", "oBindViewHolder: position: $position")
        val city = cityList[position]
        cityViewHolder.setData(city, position)
        cityViewHolder.setListeners()
    }

    override fun getItemCount(): Int = cityList.size

    //view holder class
    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var currentPosition: Int = -1
        private var currentCity: City? = null

        private val txvCityName = itemView.findViewById<TextView>(R.id.txv_city_name)
        private val imvCityImage = itemView.findViewById<ImageView>(R.id.imv_city)
        private val imvDelete = itemView.findViewById<ImageView>(R.id.imv_delete)
        private val imvFavourite = itemView.findViewById<ImageView>(R.id.imv_favorite)

        private val icFavoriteFilledImage = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.ic_favorite_filled, null
        )
        private val icFavouriteBorderedImage = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.ic_favorite_bordered, null
        )

        fun setData(city: City, position: Int) {

            txvCityName.text = city.name
            imvCityImage.setImageResource(city.imageId)

            if (city.isFavorite)
                imvFavourite.setImageDrawable(icFavoriteFilledImage)
            else
                imvFavourite.setImageDrawable(icFavouriteBorderedImage)
            this.currentPosition = position
            this.currentCity = city

        }

        fun setListeners() {
            //add listener to image views
            imvDelete.setOnClickListener(this@CityViewHolder)
            imvFavourite.setOnClickListener(this@CityViewHolder)
        }

        override fun onClick(v: View?) {

            //determine which method to execute
            when(v!!.id ) {
                R.id.imv_delete -> deleteItem()
                R.id.imv_favorite -> addToFavorite()
            }
        }

        fun deleteItem() {
            cityList.removeAt(currentPosition)
            notifyItemRemoved(currentPosition)
            notifyItemRangeChanged(currentPosition, cityList.size)

            //if it is chosen as favorite delete also from the favorite list
            VacationSpots.favoriteCityList.remove(currentCity!!)

        }

        fun addToFavorite() {
            currentCity?.isFavorite = !(currentCity?.isFavorite!!) //Toggle the "isFavorite" boolean value

            if (currentCity?.isFavorite!!) {   //if it is favorite - update icon and the city object to favorite list
                imvFavourite.setImageDrawable(icFavoriteFilledImage)
                VacationSpots.favoriteCityList.add(currentCity!!)
            } else {  //else it is not favorite - update icon and remove the city object from favorite list
                imvFavourite.setImageDrawable(icFavouriteBorderedImage)
                VacationSpots.favoriteCityList.remove(currentCity!!)
            }
        }

    }
}