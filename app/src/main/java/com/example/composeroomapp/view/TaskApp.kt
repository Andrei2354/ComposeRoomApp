package com.example.composeroomapp.view


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
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import com.example.composeroomapp.data.Task
import com.example.composeroomapp.data.AppDatabase

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
        OutlinedTextField(
            value = newTaskName,
            onValueChange = { newTaskName = it },
            label = { Text("New Task") },
            modifier = Modifier.fillMaxWidth()
        )

        // Botón para agregar tarea
        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    val newTask = Task(name = newTaskName)
                    taskDao.insert(newTask)
                    tasks = taskDao.getAllTasks() // Actualizar la lista
                    newTaskName = "" // Limpiar el campo
                }
            }
        ) {
            Text(text = "Add Task")
        }

        // Mostrar lista de tareas
        tasks.forEach { task ->
            Text(text = task.name)
        }
    }
}