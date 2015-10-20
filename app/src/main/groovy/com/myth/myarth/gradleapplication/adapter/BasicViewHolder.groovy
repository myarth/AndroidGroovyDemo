package com.myth.myarth.gradleapplication.adapter

import android.content.Context
import android.view.View
import groovy.transform.CompileStatic

@CompileStatic
class BasicViewHolder {

    static View genViewHolder(Context context, int layoutId, View convertView, BasicViewHolder viewHolder, Closure closure = null) {
        if (!convertView) {
            convertView = View.inflate(context, layoutId, null)
            closure?.call(viewHolder)
            convertView.setTag(viewHolder)
        }
        convertView
    }

}
