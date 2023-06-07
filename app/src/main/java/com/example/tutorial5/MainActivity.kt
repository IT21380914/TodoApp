package com.example.tutorial5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorial5.adapters.TodoAdapter
import com.example.tutorial5.database.TodoDatabase
import com.example.tutorial5.database.entities.Todo
import com.example.tutorial5.database.repositories.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = TodoRepository(TodoDatabase.getInstance(this))
        val recyclerView: RecyclerView = findViewById(R.id.rvTodoList)
        val ui = this

        val adapter = TodoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(ui)


        val btnAddTodo = findViewById<Button>(R.id.btnAddTodo)
        btnAddTodo.setOnClickListener {
            displayDialog(repository,adapter)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodos()
            adapter.setData(data, ui)
        }
    }

        fun displayDialog(repository: TodoRepository,adapter: TodoAdapter){
            val builder=AlertDialog.Builder(this)
            builder.setTitle("Enter New Todo Item")
            builder.setMessage("Enter the Todo item below:")

            //here we are using a dialog box to get the user input

            val input=EditText(this)
            input.inputType=InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            //setting OK button

            builder.setPositiveButton("OK"){
                dialog,which->
                val item =input.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insert(Todo(item))
                    val data=repository.getAllTodos()
                    runOnUiThread{
                        adapter.setData(data,this@MainActivity)
                    }
                }
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val alertDialog=builder.create()
            alertDialog.show()

        }
    }
