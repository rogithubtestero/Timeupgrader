package com.robinrosenstock.timeupgrader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity


import android.support.v4.view.GravityCompat

import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DividerItemDecoration
import android.view.*

import kotlinx.android.synthetic.main.task_list.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.task_list_recyclerview.*
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.robinrosenstock.timeupgrader.R.id.icon
import com.robinrosenstock.timeupgrader.R.mipmap.ic_launcher
import java.io.File
import com.robinrosenstock.timeupgrader.R.mipmap.ic_launcher




/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [TimeDetail] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class MainTaskList : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback  {


    private var twoPane: Boolean = false
    private var writepermission: Boolean = false
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101
    var firstuse = false
    var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        setSupportActionBar(toolbar)
        toolbar.title = title

        setupPermissions()


        val dividerItemDecoration = DividerItemDecoration(task_list.context,1 )
        task_list.addItemDecoration(dividerItemDecoration)

        fab.setOnClickListener {
//
//            val timedirectory = File(Environment.getExternalStorageDirectory(),"/time")
//            timedirectory.mkdirs()
//
//            val timefile = File(timedirectory, "time.txt")
//            timefile.createNewFile()

                addTaskDialog(this)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (item_detail_container != null) {
            twoPane = true
        }



        val mBuilder = NotificationCompat.Builder(this, "321")
                .setSmallIcon(getNotificationIcon())
                .setContentTitle("title")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(123, mBuilder.build())


        Log.e("myTag", "muuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuh")


    }


    private fun getNotificationIcon(): Int {
        val useWhiteIcon = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.drawable.notification_icon else R.mipmap.ic_launcher
    }



    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
        else {
            readFile("time.txt")
            task_list.adapter = RecyclerViewAdapterForTasks(this, TaskContent.TASKS, twoPane)
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE)
    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if ((grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    //permission denied
                } else {
                    //permission granted

                    readFile("time.txt")
                    task_list.adapter = RecyclerViewAdapterForTasks(this, TaskContent.TASKS, twoPane)

                    firstuse = true
                }
            }

        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        val intent1 = Intent(this, SettingsActivity::class.java)
        val intent2 = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

        when (item.itemId) {
            R.id.read_file -> startActivityForResult(Intent.createChooser(intent2, "Select a file"), 222)
            R.id.write_file -> {
                writeFile(TaskContent.TASKS)
            }
            R.id.action_settings -> startActivity(intent1)
            R.id.reload -> {
                TaskContent.TASKS.removeAll(TaskContent.TASKS)
                readFile("time.txt")
//                for now: this is working:
                task_list.adapter.notifyDataSetChanged()
//                if something is broken then setup from new with:
//                task_list.adapter = RecyclerViewAdapterForTime(this, TaskContent.TASKS, twoPane)
            }

            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


//        /////// open file //////////
        if (requestCode == 222 && resultCode == RESULT_OK) {
//            val selectedFile = data?.data //The uri with the location of the file
        }

//        /////// import file //////////
        if (requestCode == 111 && resultCode == RESULT_OK) {
//            val selectedFile = data?.data //The uri with the location of the file
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onPause() {
        super.onPause()


    }


    override fun onStop() {
        super.onStop()

        writeFile(TaskContent.TASKS)

    }

    override fun onDestroy() {
        super.onDestroy()

        TaskContent.TASKS.removeAll(TaskContent.TASKS)

    }




}






