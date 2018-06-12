package com.robinrosenstock.timeupgrader

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.NavigationView
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

import kotlinx.android.synthetic.main.task_list.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.task_list_recyclerview.*
import android.support.v7.widget.PopupMenu
import kotlinx.android.synthetic.main.task_fragment.view.*
import org.joda.time.DateTime
import java.io.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [TimeDetail] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class MainTaskList : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE : Int = 0
    var endlinenumber = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        setSupportActionBar(toolbar)
        toolbar.title = title


        val dividerItemDecoration = DividerItemDecoration(task_list.context,1 )
        task_list.addItemDecoration(dividerItemDecoration)

//        /////////////Default snackbar://////////
//        fab.setOnClickListener { view ->
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//           .setAction("Action", null).show()
//        }

        fab.setOnClickListener {

            ////// start other display/class:
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

            addNewTaskDialog()
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
                endlinenumber = parseFile("time.txt")

            }
            else{
                filedirectory.mkdirs()
                file.createNewFile()
            }

            setupRecyclerView(findViewById(R.id.task_list))

        }

    }


    override fun onDestroy() {
        super.onDestroy()

        TaskContent.TASKS.removeAll(TaskContent.TASKS)

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
                        endlinenumber = parseFile("time.txt")

                    }
                    else{
                        filedirectory.mkdirs()
                        file.createNewFile()
                    }

                    setupRecyclerView(findViewById(R.id.task_list))


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
//            R.id.import_file -> startActivityForResult(Intent.createChooser(intent2, "Select a file"), 111)
            R.id.import_file -> {
                writeFile(TaskContent.TASKS)
            }
            R.id.action_settings -> startActivity(intent1)
            R.id.reload -> {
                reLoad()
//                for now: this is working:
                notifyRecyclerView(findViewById(R.id.task_list))
//                if something is broken then setup from new with:
//                setupRecyclerView(findViewById(R.id.task_list))
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
//            updateRecyclerView(task_list_recyclerview)
//            updateRecyclerView(findViewById(R.id.task_list_recyclerview))


        }


//        /////// import file //////////
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            import_todo_txt(baseContext,selectedFile)
//            updateRecyclerView(task_list_recyclerview)
//            updateRecyclerView(findViewById(R.id.task_list_recyclerview))

        }
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, TaskContent.TASKS, twoPane)
    }


    private fun notifyRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter.notifyDataSetChanged()
    }


    class SimpleItemRecyclerViewAdapter(private val parentActivity: MainTaskList,
                                        private val values: List<TaskContent.TaskItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener
        private val onLongClickListener: View.OnLongClickListener


        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as TaskContent.TaskItem
                if (twoPane) {
                    val fragment = TimeDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(TimeDetailFragment.ITEM_POS, item.pos.toString())
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, TimeDetail::class.java).apply {
                        putExtra(TimeDetailFragment.ITEM_POS, item.pos.toString())
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
                            val index = TaskContent.TASKS.indexOf(task)

                            parentActivity.renameTask(task, it.context)
                            true

                        }
                        R.id.delete_task -> {
                            val task = it.tag as TaskContent.TaskItem

//                            removeTask(task)

                            val index = TaskContent.TASKS.indexOf(task)
                            TaskContent.TASKS.removeAt(index)
                            parentActivity.notifyRecyclerView(parentActivity.findViewById(R.id.task_list))

//                            reLoad()
//                            val snackbar = Snackbar.make(it, task.toString() + " DELETED!", 5000)
//                            snackbar.setAction("undo", MyUndoListener())
//                            snackbar.show()
//                            parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.task_list))

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
                    .inflate(R.layout.task_fragment, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val task = values[position]
            holder.contentView.text = task.title


            if(task.ongoing){
                holder.buttonView.isChecked = true
            }else{
                holder.buttonView.isChecked = false
            }

//            item.interval_list.forEach {
//                if (it.end_time == null) {
//                    holder.buttonView.isChecked = true
//                }
////                else{
////                    holder.buttonView.isChecked =false
////                }
//            }

//            holder.buttonView.setOnClickListener {
//
//
//                if(holder.buttonView.isChecked) {
//
//                    val intervall_item =  TaskContent.IntervalItem(123, DateTime.now(), null, 123, 321)
//                        task.interval_list.add(intervall_item)
//
//                    val start_time = DateTime.now()
//
//                    val eins : Int?
//                    val zwei : Int?
//                    val intervalItem: TaskContent.IntervalItem
//
//
//                    if (task.interval_list.size > 0){
//
//                        eins = task.interval_list.last().begin_time_number
//                        zwei = task.interval_list.last().end_time_number
//                        intervalItem = TaskContent.IntervalItem(task.pos, start_time, null, eins!!.plus(3), eins!!.plus(4))
////                        addIntervalItemToFile("time.txt", intervalItem, item.interval_list.last().end_time_number)
//
//                    }else{
//
//                        eins = task.line_number
//                        intervalItem = TaskContent.IntervalItem(task.pos, start_time, null, eins!!.plus(2), eins!!.plus(3))
////                        addIntervalItemToFile("time.txt", intervalItem, item.line_number)
//
//                    }
//
//                    reLoad()
////                    Snackbar.make(buttonView, item.toString() + " START!", 4000).show()
////                    parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.task_list))
//
//
////                    TaskContent.TASKS[task.pos].title = new_name
//                    parentActivity.task_list.adapter.notifyItemChanged(task.pos)
//
//                }
//                else{
//
//
////                    val ongoingIntervalItem = searchOngoingIntervalItem(item)
////                    ongoingIntervalItem!!.end_time = DateTime.now()
////                    val linenumber = ongoingIntervalItem.end_time_number
////                    val text = ongoingIntervalItem.end_time!!.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
////
////                    replaceLineInFile("time.txt", linenumber, text)
////                    reLoad()
////                    Snackbar.make(buttonView, item.toString() + " STOP!", 4000).show()
////                    parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.task_list))
//                }
//            }

            with(holder.itemView) {
                tag = task
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




    private fun addNewTaskDialog() {

        val dialogBuilder = AlertDialog.Builder(this@MainTaskList)

        var name : String = ""
        val edittext = EditText(this@MainTaskList)

        dialogBuilder.setView(edittext)
        dialogBuilder.setTitle("Add a new task")
        dialogBuilder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int ->
        })
        dialogBuilder.setPositiveButton("Add!", { dialogInterface: DialogInterface, i: Int ->
            //                                new_name = dialogView.alert_dialog_text_input.text.toString()

            name = edittext.text.toString()
            val lasttask = TaskContent.TASKS.last()

            val last_time_pos = lasttask.interval_list.last().end_time_number


            val new_task = TaskContent.TaskItem(lasttask.pos+1, name, ArrayList(), last_time_pos!!)
            TaskContent.TASKS.add(lasttask.pos+1,new_task)
            task_list.adapter.notifyItemInserted(lasttask.pos+1)


            addTaskToFile(last_time_pos, new_task.title)


//            //// for that working first all whitespace at the end must be removed (at the parsing process or somewhen inbetween)
//            File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt").appendText("\n# $name")

        })
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }



    private fun renameTask(task : TaskContent.TaskItem, context: Context) {

        val dialogBuilder = AlertDialog.Builder(context)
        var new_name : String = ""
        val edittext = EditText(context)

        dialogBuilder.setView(edittext)
        edittext.setText(task.title)
        dialogBuilder.setTitle("Rename Task")
        dialogBuilder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int ->

        })
        dialogBuilder.setPositiveButton("Rename!", { dialogInterface: DialogInterface, i: Int ->
            //                                new_name = dialogView.alert_dialog_text_input.text.toString()

            new_name = edittext.text.toString()

            TaskContent.TASKS[task.pos].title = new_name
            task_list.adapter.notifyItemChanged(task.pos)


//                            rename task on the file
//            renameTaskonFile(task, new_name)
//                            reLoad()

        })

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

    }








}




