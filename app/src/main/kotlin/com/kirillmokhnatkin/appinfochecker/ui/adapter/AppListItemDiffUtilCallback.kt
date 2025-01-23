package com.kirillmokhnatkin.appinfochecker.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kirillmokhnatkin.appinfochecker.ui.model.AppInfoModel

class AppListItemDiffUtilCallback : DiffUtil.ItemCallback<AppInfoModel>() {
    override fun areItemsTheSame(oldItem: AppInfoModel, newItem: AppInfoModel): Boolean =
        (oldItem.packageName == newItem.packageName)

    override fun areContentsTheSame(oldItem: AppInfoModel, newItem: AppInfoModel): Boolean =
        (oldItem == newItem)
}