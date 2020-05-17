package com.example.hbhims.view.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.entity.CustomMedicalSuggestionRequest
import com.youth.xframe.utils.XFormatTimeUtils
import kotlinx.android.synthetic.main.recycler_view_medical_suggestion_request.view.*
import org.jetbrains.annotations.NotNull

class MedicalSuggestionRequestAdapter(
    private val dataList: List<CustomMedicalSuggestionRequest>,
    val context: Context,
    private val iMedicalSuggestionRequest: IMedicalSuggestionRequest
) :
    RecyclerView.Adapter<MedicalSuggestionRequestAdapter.MedicalSuggestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalSuggestionViewHolder {
        return MedicalSuggestionViewHolder(
            (LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_medical_suggestion_request, parent, false))
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MedicalSuggestionViewHolder, position: Int) {
        val itemView = holder.itemView
        val request = dataList[position]
        if (request.userId == App.user.id) {
            //发送的请求
            itemView.linear_layout_professional.visibility = View.VISIBLE
            itemView.linear_layout_user.visibility = View.GONE
            itemView.text_view_request_professional_username.text = request.professionalUsername
            itemView.image_view_undo.visibility = View.VISIBLE
            itemView.material_card_view.isClickable = false
            itemView.image_view_undo.setOnClickListener {
                iMedicalSuggestionRequest.onUndo(position, request)
            }
        } else {
            //收到的请求
            itemView.linear_layout_user.visibility = View.VISIBLE
            itemView.linear_layout_professional.visibility = View.GONE
            itemView.text_view_request_user_username.text = request.userUsername
            itemView.image_view_undo.visibility = View.GONE
            itemView.material_card_view.setOnClickListener {
                iMedicalSuggestionRequest.onSelect(position, request)
            }
        }
        itemView.text_view_request_time.text = XFormatTimeUtils.getTimeSpanByNow1(request.time)
    }

    inner class MedicalSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface IMedicalSuggestionRequest {
        fun onSelect(
            @NotNull position: Int,
            @NotNull customMedicalSuggestionRequest: CustomMedicalSuggestionRequest
        )

        fun onUndo(
            @NotNull position: Int,
            @NotNull customMedicalSuggestionRequest: CustomMedicalSuggestionRequest
        )
    }
}