package com.example.yeshasprabhakar.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.yeshasprabhakar.todo.R
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yeshasprabhakar.todo.SharedPref
import com.example.yeshasprabhakar.todo.MainActivity
import com.example.yeshasprabhakar.todo.TodoItem
import com.example.yeshasprabhakar.todo.TodoViewModel
import com.example.yeshasprabhakar.todo.ui.theme.TodoTheme
// Assuming Typography and Shapes will be in the same theme package
import com.example.yeshasprabhakar.todo.ui.theme.Typography
import com.example.yeshasprabhakar.todo.ui.theme.Shapes
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TodoAppBar(onThemeToggle: () -> Unit, isDarkTheme: Boolean) {
    TopAppBar(
        title = { Text("ToDo App") },
        actions = {
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Filled.Brightness7 else Icons.Filled.Brightness4,
                    contentDescription = "Toggle Theme"
                )
            }
        }
    )
}

@Composable
fun TodoApp(todoViewModel: TodoViewModel = viewModel()) {
    val context = LocalContext.current
    val sharedPreferences = SharedPref(context)
    val isDarkTheme = sharedPreferences.loadNightModeState()
    val todoItems by todoViewModel.allTodos.observeAsState(initial = emptyList())

    TodoTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TodoAppBar(
                    onThemeToggle = {
                        sharedPreferences.setNightModeState(!isDarkTheme)
                        (context as? MainActivity)?.restartApp()
                    },
                    isDarkTheme = isDarkTheme
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Apply innerPadding
                    .padding(horizontal = 16.dp), // Add horizontal padding for items
                verticalArrangement = Arrangement.spacedBy(4.dp) // Spacing between items
            ) {
                items(todoItems ?: emptyList()) { item -> // Handle null case for todoItems
                    TodoItemRow(
                        todoItem = item,
                        onDelete = {
                            todoViewModel.deleteByNameDateTime(item.name, item.date, item.time)
                            // Optionally, show a toast or snackbar here using context from LocalContext.current
                        }
                    )
                }
            } else {
                EmptyState()
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
        LottieAnimation(
            composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.height(200.dp) // Adjust size as needed
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.listEmptyText))
    }
}

@Composable
fun TodoItemRow(todoItem: TodoItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = todoItem.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Date: ${todoItem.date}", fontSize = 14.sp)
                Text(text = "Time: ${todoItem.time}", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Item")
            }
        }
    }
    Divider()
}
