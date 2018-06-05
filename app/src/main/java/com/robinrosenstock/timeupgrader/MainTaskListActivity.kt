package com.robinrosenstock.timeupgrader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView



import android.support.v4.view.GravityCompat

import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.view.*
import android.widget.*

import com.robinrosenstock.timeupgrader.dummy.TaskContent
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.item_list.*
import android.support.v7.widget.PopupMenu
import kotlinx.android.synthetic.main.testlayout2.view.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.notificationManager
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        setSupportActionBar(toolbar)
        toolbar.title = title


        val dividerItemDecoration = DividerItemDecoration(item_list.context,1 )
        item_list.addItemDecoration(dividerItemDecoration)

//        /////////////Default snackbar://////////
//        fab.setOnClickListener { view ->
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//           .setAction("Action", null).show()
//        }

        fab.setOnClickListener {

            ////// start other display/class:
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

//          ////////  add a task through a custom dialog, learned from here: <https://www.youtube.com/watch?v=Z9LhAgBSlhU> /////
            addNewTaskDialog(it)
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)



        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }


//        if first start of app then make the time.txt file:


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not (yet) granted

//            Toast.makeText(baseContext, "Permission is not (yet) granted", Toast.LENGTH_LONG).show()

            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//            Toast.makeText(baseContext, "muffPutter", Toast.LENGTH_LONG).show()
//
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//            }
        } else {
            // Permission has already been granted

            val filedirectory = Environment.getExternalStoragePublicDirectory("/time")
            val file = File(filedirectory, "time.txt")


            if (file.canWrite()) {
                parseFile("time.txt")

            }
            else{
                filedirectory.mkdirs()
                file.createNewFile()
            }

            setupRecyclerView(findViewById(R.id.item_list))
        }

    }


    override fun onDestroy() {
        super.onDestroy()

        TaskContent.TASKS.removeAll(TaskContent.TASKS)
        TaskContent.TASK_MAP.clear()

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                // permission was granted, yay!

                val filedirectory = Environment.getExternalStoragePublicDirectory("/time")
                val file = File(filedirectory, "time.txt")


                if (file.canWrite()) {
                    parseFile("time.txt")

                }
                else{
                    filedirectory.mkdirs()
                    file.createNewFile()
                }

                setupRecyclerView(findViewById(R.id.item_list))


            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return
        }

    // Add other 'when' lines to check for other
    // permissions this app might request.
        else -> {
            // Ignore all other requests.
        }
    }
}




    private fun renameTaskDialog(taskToRename : TaskContent.TaskItem, rootview : View) {

        val dialogBuilder = AlertDialog.Builder(this@ItemListActivity)
        dialogBuilder.setTitle("Rename Task")
//        dialogBuilder.setMessage("I am a alert dialog!")
        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
        dialogBuilder.setView(view)

        view.alert_dialog_text.text = "What is the new name?"
        view.alert_dialog_button.text = "Rename!"

        val alertDialog = dialogBuilder.create()

        alertDialog.show()

        view.alert_dialog_button.setOnClickListener{

            val new_task_name = view.alert_dialog_text_input.text.toString()

            renameTask(taskToRename, new_task_name)
            reLoad()

            alertDialog.dismiss()


            Snackbar.make(rootview, "${taskToRename.title} RENAMED to: $new_task_name", 5000).show()

            setupRecyclerView(findViewById(R.id.item_list))
        }
    }



    private fun addNewTaskDialog(rootview : View) {

        val dialogBuilder = AlertDialog.Builder(this@ItemListActivity)
        dialogBuilder.setTitle("Add a new task")
//        dialogBuilder.setMessage("I am a alert dialog!")
        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
        dialogBuilder.setView(view)

        val alertDialog = dialogBuilder.create()

        alertDialog.show()

        view.alert_dialog_button.setOnClickListener{

            val lastpos = TaskContent.TASKS.last().pos
            val task_name = view.alert_dialog_text_input.text.toString()
            val task_entry = TaskContent.TaskItem(lastpos+1, task_name, ArrayList(), 5)
            TaskContent.TASKS.add(task_entry)
            TaskContent.TASK_MAP.put(task_entry.pos.toString(), task_entry)

//            append the new task to the end of the file:
//            val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
            File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt").appendText("\n\n# $task_name")

            reLoad()
            alertDialog.dismiss()

            Snackbar.make(rootview, "$task_name ADDED!", 4000).show()

            setupRecyclerView(findViewById(R.id.item_list))
        }
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.menu_main, menu)
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
            R.id.open_file -> startActivityForResult(Intent.createChooser(intent2, "Select a file"), 222)
            R.id.import_file -> startActivityForResult(Intent.createChooser(intent2, "Select a file"), 111)
            R.id.action_settings -> startActivity(intent1)
            R.id.reload -> {
                reLoad()
                setupRecyclerView(findViewById(R.id.item_list))
            }

            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


//        /////// open file //////////
        if (requestCode == 222 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file

            import_todo_txt(baseContext,selectedFile)
//            updateRecyclerView(item_list)
//            updateRecyclerView(findViewById(R.id.item_list))


        }


//        /////// import file //////////
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            import_todo_txt(baseContext,selectedFile)
//            updateRecyclerView(item_list)
//            updateRecyclerView(findViewById(R.id.item_list))

        }
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, TaskContent.TASKS, twoPane)
    }



    class SimpleItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                        private val values: List<TaskContent.TaskItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener
        private val onLongClickListener: View.OnLongClickListener


        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as TaskContent.TaskItem
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.pos.toString())
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.pos.toString())
                    }
                    v.context.startActivity(intent)
                }
            }



            onLongClickListener = View.OnLongClickListener {
                val popup = PopupMenu(this.parentActivity, it)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId){
                        R.id.rename_task -> {

                            val task = it.tag as TaskContent.TaskItem

                            parentActivity.renameTaskDialog(task, it)


                            true
                        }
                        R.id.delete_task -> {
                            val task = it.tag as TaskContent.TaskItem

                            removeWholeTask(task)
                            reLoad()

                            val snackbar = Snackbar.make(it, task.toString() + " DELETED!", 5000)
                            snackbar.setAction("undo", MyUndoListener())
                            snackbar.show()
                            parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.item_list))

                            true
                        }
                        else -> false
                    }
                }
                popup.inflate(R.menu.context_task_menu)
                popup.show()
                true

            }
        }




        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.testlayout2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.contentView.text = item.title


            item.interval_list.forEach {
                if (it.end_time == null) {
                    holder.buttonView.isChecked = true
                }
                else{
                    holder.buttonView.isChecked =false
                }
            }


            holder.buttonView.setOnCheckedChangeListener { buttonView, isChecked ->

                if(isChecked) {

                    val start_time = DateTime.now()

                    val eins : Int?
                    val zwei : Int?
                    val intervalItem: TaskContent.IntervalItem


                    if (item.interval_list.size > 0){

                        eins = item.interval_list.last().begin_time_number
                        zwei = item.interval_list.last().end_time_number
                        intervalItem = TaskContent.IntervalItem(start_time, null, eins!!.plus(3), eins!!.plus(4))
                        addIntervalItemToFile("time.txt", intervalItem, item.interval_list.last().end_time_number)

                    }else{

                        eins = item.line_number
                        intervalItem = TaskContent.IntervalItem(start_time, null, eins!!.plus(2), eins!!.plus(3))
                        addIntervalItemToFile("time.txt", intervalItem, item.line_number)

                    }

                    reLoad()
                    Snackbar.make(buttonView, item.toString() + " START!", 4000).show()
                    parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.item_list))

                }
                else{
                    val ongoingIntervalItem = searchOngoingIntervalItem(item)
                    ongoingIntervalItem!!.end_time = DateTime.now()
                    val linenumber = ongoingIntervalItem.end_time_number
                    val text = ongoingIntervalItem.end_time!!.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))

                    replaceLineInFile("time.txt", linenumber, text)
                    reLoad()
                    Snackbar.make(buttonView, item.toString() + " STOP!", 4000).show()
                    parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.item_list))
                }
            }

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
                setOnLongClickListener(onLongClickListener)

            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val idView: TextView = view.id_text
            val contentView: TextView = view.content
            val buttonView: ToggleButton = view.push_button
        }
    }


    class MyUndoListener : View.OnClickListener {

        override
        fun onClick(v: View) {

            Toast.makeText(v.context,"not yet implemented", Toast.LENGTH_LONG).show()
            // Code to undo the user's last action
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


}




