package com.jacknic.persistence

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

/**
 * 列表展示页面
 *
 * @author Jacknic
 */
class StarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        val activities = packageInfo.activities.filter { javaClass.name != it.name }
        val activityNameList = activities.map { it.loadLabel(packageManager) }
        ListView(this).let {
            it.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, activityNameList)
            it.setOnItemClickListener { _, _, position, _ ->
                val activityInfo = activities[position]
                val intent = Intent(this, Class.forName(activityInfo.name))
                startActivity(intent)
            }
            setContentView(it)
        }
    }
}