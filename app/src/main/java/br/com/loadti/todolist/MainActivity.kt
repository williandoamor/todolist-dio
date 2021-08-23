package br.com.loadti.todolist


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import br.com.loadti.todolist.adapter.TaskListAdapter
import br.com.loadti.todolist.databinding.ActivityMainBinding
import br.com.loadti.todolist.datasource.TaskDataSource
import br.com.loadti.todolist.view.AddTaskActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    private val register = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == RESULT_OK) {

            //binding.rvTasks.adapter = adapter
            //adapter.submitList(TaskDataSource.getList())
            updateList()

        }
        //

    }

    private fun updateList() {
        val list = TaskDataSource.getList()


        if (list.isEmpty()) {

            binding.includeEmpty.emptyState.visibility = View.VISIBLE
            binding.tvTitle.visibility = View.INVISIBLE

        } else {

            binding.includeEmpty.emptyState.visibility = View.INVISIBLE
            binding.tvTitle.visibility = View.VISIBLE
        }


        adapter.submitList(list)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()
        inserirTarefa()

    }

    private fun inserirTarefa() {
        binding.fabAddTask.setOnClickListener {
            Intent(this, AddTaskActivity::class.java).let {
                register.launch(it)
            }

        }

        adapter.listenerEdit = { task ->
            Intent(this, AddTaskActivity::class.java).putExtra(AddTaskActivity.TASK_ID, task.id)
                .let {
                    register.launch(it)
                }
        }

        adapter.listenerDelete = {

            TaskDataSource.deleteTask(it)
            updateList()
        }
    }
}