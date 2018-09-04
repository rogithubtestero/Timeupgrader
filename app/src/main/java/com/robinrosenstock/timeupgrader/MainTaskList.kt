package com.robinrosenstock.timeupgrader

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.task_list.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.task_list_recyclerview.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [TimeDetail] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class MainTaskList : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback  {


    var memberFieldString: String? = null
    private var twoPane: Boolean = false
    private val RECORD_REQUEST_CODE = 101
    var firstuse = false
    public var myadapter = RecyclerViewAdapterForTasks(this, TaskContent.TASKS, twoPane)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        setSupportActionBar(toolbar)
        toolbar.title = title


        if (hasWritePermission()){
            // effectively reloading data:
            TaskContent.TASKS.removeAll(TaskContent.TASKS)
            readFile("time.txt")
            task_list.adapter = myadapter
        }
        else{
            requestWritePermission()
        }


        val dividerItemDecoration = DividerItemDecoration(task_list.context,1 )
        task_list.addItemDecoration(dividerItemDecoration)

        fab.setOnClickListener {
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


    }



    fun reload(){


    }



    private fun hasWritePermission():Boolean {

        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return (permission == PackageManager.PERMISSION_GRANTED)
        }



    private fun requestWritePermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE)
    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if ((grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    // permission denied
                    // use the killDialog
                    killDialog(this)

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
                task_list.adapter.notifyDataSetChanged()
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

        Log.e("YourActivity", "onPause")

    }


    override fun onStop() {
        super.onStop()

        if (TaskContent.TASKS.isNotEmpty()){
            writeFile(TaskContent.TASKS)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }


    override fun onNewIntent(intent: Intent) {
        Log.e("YourActivity", "onNewIntent is called!")

        memberFieldString = intent.getStringExtra("KEY")

        super.onNewIntent(intent)

    } // End of onNewIntent(Intent intent)

    override fun onResume() {


//        TaskContent.TASKS.removeAll(TaskContent.TASKS)
//        readFile("time.txt")
//        task_list.adapter.notifyDataSetChanged()

        Log.e("YourActivity", "onResume")


        super.onResume()

    } // End of onResume()


}






