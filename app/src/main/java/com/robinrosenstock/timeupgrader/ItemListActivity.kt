package com.robinrosenstock.timeupgrader

import android.app.Dialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView

import android.support.v4.view.GravityCompat

import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.*

import com.robinrosenstock.timeupgrader.dummy.DummyContent
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            Log.d("tag", selectedFile.toString())


//            read the file in and add each line as a DummyItem it for the recycler view:
            try {

                val fIn = getContentResolver().openInputStream(selectedFile)
                val file = InputStreamReader (fIn)
                val br = BufferedReader (file)
                var line = br.readLine ()
                val all = StringBuilder ()
                var item_id = 0

                while (line!= null) {
                    all.append (line + "\n")
                    line = br.readLine ()

                    if (line!=null) {
                        val neger = DummyContent.DummyItem(item_id.toString(), line, "details will be filled later")
                        item_id = item_id.inc()
                        DummyContent.ITEMS.add(neger)
                    }
                }
                br.close ()
                file.close ()

            } catch (e: IOException) {
                Toast.makeText (this, "Could not read", Toast.LENGTH_SHORT) .show ()
                Log.d("ioioioioioioioioio", e.toString())
            }

            updateRecyclerView(item_list)
        }
    }


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
//            val intent = Intent(this, Fileactor::class.java)
//            startActivity(intent)


//          ////////  add a task through a custom dialog, learned from here: <https://www.youtube.com/watch?v=Z9LhAgBSlhU> /////
            val dialogBuilder = AlertDialog.Builder(this@ItemListActivity)

            dialogBuilder.setTitle("title")
            dialogBuilder.setMessage("I am a alert dialog!")

            val view = layoutInflater.inflate(R.layout.alert_dialog, null)

            dialogBuilder.setView(view)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            view.alert_dialog_button.setOnClickListener{
                val name = view.alert_dialog_text_input.text.toString()
                Toast.makeText(this@ItemListActivity, name, Toast.LENGTH_LONG).show()

                alertDialog.dismiss()
            }


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
            R.id.action_settings -> startActivity(intent1)
            R.id.import_file -> startActivityForResult(Intent.createChooser(intent2, "Select a file"), 111)
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    }


    private fun updateRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter.notifyDataSetChanged()
    }


    class SimpleItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                        private val values: List<DummyContent.DummyItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
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
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            holder.buttonView.setOnClickListener {
                Toast.makeText(this.parentActivity, "timer start for: " + item.toString(), Toast.LENGTH_LONG).show()

            }

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
            val buttonView: Button = view.push_button
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
