package com.myth.myarth.gradleapplication.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import groovy.transform.CompileStatic


@CompileStatic
class SimpleAdapter extends BaseAdapter {

    Context context
    int layoutId
    List data = []

    SimpleAdapter(Context context, int layoutId) {
        this.context = context
        this.layoutId = layoutId
    }

    @Override
    int getCount() {
        return data.size()
    }

    @Override
    Object getItem(int position) {
        return data[position]
    }

    @Override
    long getItemId(int position) {
        return position
    }

    @Override
    View getView(int position, View convertView, ViewGroup parent) {
        BasicViewHolder.genViewHolder(context, layoutId, convertView, new ViewHolder()) { ViewHolder holder ->
            // 可以在这里做数据绑定
            holder.position = position
        }
        // 也可以在这里做数据绑定
        def holder = convertView.tag as ViewHolder
        holder.data = data
        convertView
    }

    class ViewHolder extends BasicViewHolder {
        List data
        int position
    }

}
