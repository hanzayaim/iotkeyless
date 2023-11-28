package com.example.iotkeyless.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iotkeyless.R
import com.example.iotkeyless.model.FingerprintData

class FingerprintAdapter(private val context : Context, private val fingerprintList : List<FingerprintData>) : RecyclerView.Adapter<FingerprintViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FingerprintViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_fingerprint, parent, false)
        return FingerprintViewHolder(view)
    }

    override fun onBindViewHolder(holder: FingerprintViewHolder, position: Int) {
        holder.tvIdFingerprint.text = fingerprintList[position].fingerprintId.toString()
        holder.tvNameFingerprint.text = fingerprintList[position].fingerprintName
    }

    override fun getItemCount(): Int {
        return fingerprintList.size
    }

}

class FingerprintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var ivFingerprint: ImageView
    var tvIdFingerprint: TextView
    var tvNameFingerprint: TextView

    init {
        ivFingerprint = itemView.findViewById(R.id.ivFingerprint)
        tvIdFingerprint = itemView.findViewById(R.id.tvIdFingerprint)
        tvNameFingerprint = itemView.findViewById(R.id.tvNameFingerprint)
    }
}