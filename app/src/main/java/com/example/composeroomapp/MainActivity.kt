package com.example.composeroomapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos
        database = AppDatabase.getDatabase(this)

        setContent {
            TaskApp(database)
        }
    }
}

@Composable
fun TaskApp(database: AppDatabase) {
    val taskDao = database.taskDao()
    val scope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTaskName by remember { mutableStateOf("") }

    // Cargar tareas al iniciar
    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de texto para agregar una nueva tarea
        androidx.compose.material.OutlinedTextField(
            value = newTaskName,
            onValueChange = { newTaskName = it },
            label = { androidx.compose.material.Text("New Task") },
            modifier = Modifier.fillMaxWidth()
        )

        // Botón para agregar tarea
        androidx.compose.material.Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    val newTask = Task(name = newTaskName)
                    taskDao.insert(newTask)
                    tasks = taskDao.getAllTasks() // Actualizar la lista
                    newTaskName = "" // Limpiar el campo
                }
            }
        ) {
            androidx.compose.material.Text("Add Task")
        }

        // Mostrar lista de tareas
        tasks.forEach { task ->
            androidx.compose.material.Text(text = task.name)
        }
    }
}
