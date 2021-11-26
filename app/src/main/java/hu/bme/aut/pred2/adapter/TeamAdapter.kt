package hu.bme.aut.pred2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.pred2.R
import hu.bme.aut.pred2.data.Team
import hu.bme.aut.pred2.databinding.TeamListBinding

class TeamAdapter(private val listener: TeamClickListener) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private val items = mutableListOf<Team>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TeamViewHolder(
        TeamListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = items[position]

        holder.binding.tvName.text = team.teamname
        holder.binding.tvValue.text = team.clubWorth.toString()
    }

    fun addItem(item: Team) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(teamItems: List<Team>) {
        items.clear()
        items.addAll(teamItems)
        notifyDataSetChanged()
    }

    //fun deletebyname(name: String) {
    //    items.clear()
    //    items.delete(name)
    //    notifyDataSetChanged()
    //}

    override fun getItemCount(): Int = items.size

    interface TeamClickListener {
        fun onItemChanged(item: Team)
    }

    inner class TeamViewHolder(val binding: TeamListBinding) : RecyclerView.ViewHolder(binding.root)
}