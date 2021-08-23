package br.com.loadti.todolist.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.loadti.todolist.databinding.ActivityAddNoteBinding
import br.com.loadti.todolist.datasource.TaskDataSource
import br.com.loadti.todolist.model.Task
import br.com.loadti.todolist.utils.format
import br.com.loadti.todolist.utils.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val TAG: String = "AddTaskActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        getExtas()
        //
        initDataPicker()
        //
        initTimePicker()
        //
        cancelarInsert()
        //
        inserirTarefa()
    }

    private fun getExtas() {
        if (intent.hasExtra(TASK_ID)) {
            val taskId: Int = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.fildTitle.text = it.title
                binding.fildDate.text = it.date
                binding.fildHour.text = it.hour

            }

        }
    }

    private fun initDataPicker() {

        binding.fildDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.fildDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATA_PICKER_TASK_TAG")
        }
    }

    private fun initTimePicker() {

        binding.fildHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.fildHour.text = "${hour} : ${minute}"
            }

            timePicker.show(supportFragmentManager, "DATA_PICKER_TASK_HOUR")
        }
    }

    private fun cancelarInsert() {
        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun inserirTarefa() {

        binding.btnNewTask.setOnClickListener {

            val task = Task(
                title = binding.fildTitle.text,
                date = binding.fildDate.text,
                hour = binding.fildHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)

            setResult(RESULT_OK)
            finish()
            Log.i(TAG, "insertTask " + TaskDataSource.getList())

        }

    }

    companion object {
        const val TASK_ID: String = "task_id"
    }

}