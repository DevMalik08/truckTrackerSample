package com.example.trucktracker.ui.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trucktracker.R
import com.example.trucktracker.data.Truck
import java.util.*

class TruckListAdapter(private var truckList: List<Truck>) : RecyclerView.Adapter<TruckListAdapter.MyViewHolder>() {

    companion object{
        const val SECOND = 59
        const val MIN_TO_SECOND = 3599
        const val HOURS_TO_SECOND = 86399
        const val NOT_RESPONDING_TIME = 14400
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val truckTitle: TextView = itemView.findViewById(R.id.truck_name)
        val speed: TextView = itemView.findViewById(R.id.speed)
        val startStopTime: TextView = itemView.findViewById(R.id.truck_update_date)
        val runningStatus: TextView = itemView.findViewById(R.id.truck_last_status)
        val cardView: ConstraintLayout = itemView.findViewById(R.id.card_view)
        val imageView: ImageView = itemView.findViewById(R.id.truck_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.truck_list_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val truckInfo = truckList[position]
        holder.truckTitle.text = truckInfo.truckNumber
        if(truckInfo.runningInfo.runningState == 1) {
            setCustomString(holder.speed,"${truckInfo.positionInfo.speed}"," km/h" ,holder.itemView.context.getColor(R.color.red))
            //holder.speed.text = "${truckInfo.positionInfo.speed} km/h"
        } else {
            holder.speed.text = ""
        }
        holder.startStopTime.text = getLastStatus(truckInfo)
        holder.runningStatus.text = getRunningOrStoppingTime(truckInfo)
        if(isNotResponding(truckInfo)){
            holder.cardView.setBackgroundColor(holder.itemView.context.getColor(R.color.red_shade))
        } else {
            holder.cardView.setBackgroundColor(holder.itemView.context.getColor(R.color.white))
        }

        if (isNotResponding(truckInfo)) {
            holder.imageView.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_baseline_battery_alert_24))
            //holder.imageView.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            holder.imageView.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.cargo_truck_32))
        }


    }

    override fun getItemCount(): Int {
        return truckList.size
    }

    fun updateList(trucks: ArrayList<Truck>) {
        truckList = trucks
    }

    private fun getLastStatus(truck: Truck): String {
        val currentTimestamp = System.currentTimeMillis()
        val diff = (currentTimestamp - truck.positionInfo.createTime)/1000
        return  "${getTimeDifference(diff)} ago"
    }

    private fun getRunningOrStoppingTime(truck: Truck): String{
        val currentTimestamp = System.currentTimeMillis()
        return if(truck.runningInfo.runningState == 0){
            "Stopped since last ${getTimeDifference((currentTimestamp - truck.runningInfo.stopStartTime)/1000)}"
        } else {
            "Running since last ${getTimeDifference((currentTimestamp - truck.runningInfo.stopStartTime)/1000)}"
        }
    }

    private fun getTimeDifference(diff: Long): String{
        return when( diff){
            in 0..SECOND ->{
                "$diff secs"
            }
            in (SECOND + 1)..MIN_TO_SECOND -> {
                "${diff/60} mins"
            }
            in (MIN_TO_SECOND+1)..HOURS_TO_SECOND -> {
                "${diff/3600} hours"
            }
            else -> {
                "${diff/(3600*24)} days"
            }
        }
    }

    private fun isNotResponding(truck: Truck): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime-truck.positionInfo.createTime)/1000 >= NOT_RESPONDING_TIME
    }

    fun setCustomString(view: TextView, start: String, end: String, color: Int){
        var spn = SpannableString(start+end) as Spannable
        spn = getCustomSpan(color, 0,
            start.length, spn)
        view.text = spn
    }

    private fun getCustomSpan(color: Int, start: Int, end: Int, spn: Spannable): Spannable{
        spn.setSpan(ForegroundColorSpan(color),start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spn
    }


    /*private Spannable getCustomSpann(int color, int start, int end, Spannable spn) {
        spn.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spn.setSpan(new RelativeSizeSpan(2f), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spn.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spn;
    }*/
}