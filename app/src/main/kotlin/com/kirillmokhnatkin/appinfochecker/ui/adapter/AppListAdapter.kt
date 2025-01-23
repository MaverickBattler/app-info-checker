package com.kirillmokhnatkin.appinfochecker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kirillmokhnatkin.appinfochecker.R
import com.kirillmokhnatkin.appinfochecker.databinding.AppInfoListItemBinding
import com.kirillmokhnatkin.appinfochecker.ui.model.AppInfoModel

class AppListAdapter(
    private val onItemClickListener: (String) -> Unit,
) : ListAdapter<AppInfoModel, AppListAdapter.AppListViewHolder>(AppListItemDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        return AppListViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClickListener)
    }

    class AppListViewHolder(
        private val binding: AppInfoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): AppListViewHolder {
                return AppListViewHolder(
                    AppInfoListItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

        fun bind(
            item: AppInfoModel,
            onItemClickListener: (String) -> Unit,
        ) {
            val context = binding.root.context
            binding.apply {
                listItem.setOnClickListener {
                    onItemClickListener(item.packageName)
                }
                appNameTextview.text = item.appName
                appPackageNameTextview.text = item.packageName
                appVersionTextview.text =
                    context.getString(R.string.version_is_value, item.versionName)
            }
        }
    }
}