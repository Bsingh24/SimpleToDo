package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listofTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. Remove item from the list
                listofTasks.removeAt(position)
                //2. Notify adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1. Detect when user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Caren","User clicked a button")
//        }

        loadItems()

        //Look up recyclerview in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listofTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Set up a button and input field, so that the user can enter a task and add it to the list
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. grab text user has inputted @addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. add string to our list of tasks: listofTasks
            listofTasks.add(userInputtedTask)

            //Notify adapter that our data has been updated
            adapter.notifyItemInserted(listofTasks.size - 1)

            //3. reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data user has inputted by writing and reading from a file

    //Get the file we need
    fun getDataFile() : File {

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir,"data.txt")
    }
    //load the items by reading every line in the data file
    fun loadItems() {
        try {
            listofTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    //save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listofTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}