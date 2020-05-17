package com.example.hbhims.view.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hbhims.R
import com.example.hbhims.model.entity.CustomSysUserProfessional
import kotlinx.android.synthetic.main.recycler_view_select_professional.view.*
import org.jetbrains.annotations.NotNull

class SelectProfessionalAdapter(
    private val dataList: List<CustomSysUserProfessional>,
    val context: Context,
    private val iSelectProfessional: ISelectProfessional
) :
    RecyclerView.Adapter<SelectProfessionalAdapter.MedicalSuggestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalSuggestionViewHolder {
        return MedicalSuggestionViewHolder(
            (LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_select_professional, parent, false))
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MedicalSuggestionViewHolder, position: Int) {
        val itemView = holder.itemView
        val customMedicalSuggestion = dataList[position]
        itemView.text_view_user_name.text = customMedicalSuggestion.username
        val totalSuggestionCount = customMedicalSuggestion.totalSuggestionCount
        itemView.text_view_total_suggestion_count.text = (totalSuggestionCount ?: 0).toString()
        val meanEvaluationScore = customMedicalSuggestion.meanEvaluationScore
        if (meanEvaluationScore != null) {
            itemView.rating_bar_medical_suggestion_score.rating = meanEvaluationScore.toFloat()
        } else {
            itemView.rating_bar_medical_suggestion_score.rating = 0F
        }
        itemView.material_card_view.setOnClickListener {
            iSelectProfessional.onSelect(position, customMedicalSuggestion)
        }
    }

    inner class MedicalSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @FunctionalInterface
    interface ISelectProfessional {
        fun onSelect(
            @NotNull position: Int, @NotNull sysUserProfessional: CustomSysUserProfessional
        )
    }
}