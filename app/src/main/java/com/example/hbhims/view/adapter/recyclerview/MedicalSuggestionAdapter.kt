package com.example.hbhims.view.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hbhims.R
import com.example.hbhims.model.entity.CustomMedicalSuggestion
import com.youth.xframe.utils.XFormatTimeUtils
import kotlinx.android.synthetic.main.rating_bar_with_time.view.*
import kotlinx.android.synthetic.main.recycler_view_medical_suggestion.view.*

class MedicalSuggestionAdapter(
    private val dataList: List<CustomMedicalSuggestion>,
    val context: Context,
    private val iMedicalSuggestion: IMedicalSuggestion
) :
    RecyclerView.Adapter<MedicalSuggestionAdapter.MedicalSuggestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalSuggestionViewHolder {
        return MedicalSuggestionViewHolder(
            (LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_medical_suggestion, parent, false))
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MedicalSuggestionViewHolder, position: Int) {
        val itemView = holder.itemView
        val customMedicalSuggestion = dataList[position]
        itemView.text_view_content.text = customMedicalSuggestion.content
        itemView.text_view_time.text =
            XFormatTimeUtils.getTimeSpanByNow1(customMedicalSuggestion.time)
        if (customMedicalSuggestion.evaluationScore != null) {
            itemView.text_view_no_rating_data.visibility = View.GONE
            itemView.rating_bar.visibility = View.VISIBLE
            itemView.rating_bar.rating = customMedicalSuggestion.evaluationScore.toFloat()
        } else {
            itemView.text_view_no_rating_data.visibility = View.VISIBLE
            itemView.rating_bar.visibility = View.GONE
        }
        itemView.material_card_view.setOnClickListener {
            iMedicalSuggestion.onClick(position, customMedicalSuggestion)
        }
    }

    inner class MedicalSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface IMedicalSuggestion {
        fun onClick(
            position: Int,
            medicalSuggestion: CustomMedicalSuggestion
        )
    }
}