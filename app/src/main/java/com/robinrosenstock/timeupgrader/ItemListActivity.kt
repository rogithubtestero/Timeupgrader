package com.robinrosenstock.timeupgrader

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView



import android.support.v4.view.GravityCompat

import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.*

import com.robinrosenstock.timeupgrader.dummy.TaskContent
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.item_list.*
import android.support.v7.widget.PopupMenu
import com.fondesa.recyclerviewdivider.RecyclerViewDivider
import kotlinx.android.synthetic.main.testlayout2.view.*


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        setSupportActionBar(toolbar)
        toolbar.title = title


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
            addNewTaskDialog()
          ////// update the view and write file:
            updateRecyclerView(item_list)
            writeFile("time.txt")

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

        setupRecyclerView(item_list)
    }

    private fun addNewTaskDialog() {

        val dialogBuilder = AlertDialog.Builder(this@ItemListActivity)
        dialogBuilder.setTitle("Add a new task")
//        dialogBuilder.setMessage("I am a alert dialog!")
        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()


        view.alert_dialog_button.setOnClickListener{
            val name = view.alert_dialog_text_input.text.toString()
//              add the task to the list:
            val newtask = TaskContent.TaskItem("0", name, ArrayList())
            TaskContent.TASKS.add(newtask)
            TaskContent.TASK_MAP.put(newtask.id, newtask)


            alertDialog.dismiss()
            Snackbar.make(it.rootView, name + " added", 5000).show()


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
                TaskContent.TASKS.removeAll(TaskContent.TASKS)
                readFile("time.txt")
                updateRecyclerView(item_list)}

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
            updateRecyclerView(item_list)
        }


//        /////// import file //////////
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            import_todo_txt(baseContext,selectedFile)
            updateRecyclerView(item_list)
        }
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, TaskContent.TASKS, twoPane)
        RecyclerViewDivider.with(this).build().addTo(recyclerView)

    }


    private fun updateRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter.notifyDataSetChanged()
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
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }



            onLongClickListener = View.OnLongClickListener {
                val popup = PopupMenu(this.parentActivity, it)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId){
                        R.id.rename_task -> {

//                            val item2 = it.tag as TaskContent.TaskItem
//                            TaskContent.ITEMS.remove(item2)
                            Snackbar.make(it, "not yet implemented", 5000).show()

                            true
                        }
                        R.id.delete_task -> {
                            val item2 = it.tag as TaskContent.TaskItem
                            val Snackbar = Snackbar.make(it, item2.toString() + " deleted", 5000)
                            Snackbar.setAction("undo", MyUndoListener());
                            Snackbar.show()
                            TaskContent.TASKS.remove(item2)
                            parentActivity.updateRecyclerView(parentActivity.item_list)
                            writeFile("time.txt")

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
//            holder.idView.text = item.id
            holder.contentView.text = item.title
            holder.buttonView.setOnClickListener {
                Snackbar.make(it, "timer started for: " + item.toString(), 5000).show()


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
            val buttonView: Button = view.push_button
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




