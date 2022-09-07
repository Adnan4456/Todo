package com.example.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.todoapp.Model.Todo
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.example.todoapp.viewModel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TodoActivity : ComponentActivity() {

    private val todoViewModel:TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ToDoAppTheme{
                Surface(color =  MaterialTheme.colors.background) {

                    AddToolbar()

                }
            }
        }
    }


    @Composable
    fun AddToolbar(){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Todo App")
                    }
                )
            },
            floatingActionButton = {

                val openDialog = remember {
                    mutableStateOf(false) //dialog box is not open right now
                }
                FloatingActionButton(onClick = {
                    //when click on FAB dialog box show
                    openDialog.value = true
                }) {
                    //Therefore call dialog function here
                    AddDialogBox(openDialog = openDialog)
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) {
            RecyclerView(todoViewModel)
        }
    }


    @Composable
    fun AddDialogBox(openDialog: MutableState<Boolean>){

        val year : Int
        val month: Int
        val day: Int

        val calender = Calendar.getInstance()
        year = calender.get(Calendar.YEAR)
        month = calender.get(Calendar.MONTH)
        day = calender.get(Calendar.DAY_OF_MONTH)
        val title = remember { mutableStateOf("")}
        val des = remember { mutableStateOf("")}
        val date = remember { mutableStateOf("")}

        //date picker build in dialog
        val datePickerDialog = DatePickerDialog(
            applicationContext,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                date.value = "$dayOfMonth/$month/$year"
            }, year, month, day
        )

        if (openDialog.value){

            AlertDialog(onDismissRequest = {

                openDialog.value = false

            },
                title = {
                    Text(text = "Add new Record " , modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth())
                },
                text = {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
//                            .fillMaxSize(),
                    ) {
                        Spacer(modifier = Modifier.size(16.dp))
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = {
                                title.value = it
                            },
                            placeholder = {
                                Text(text = "Enter Title")
                            },
                            label = {
                                Text(text = "Enter title")
                            } ,
                            modifier = Modifier.fillMaxWidth()
                        )
//                        Spacer(modifier = Modifier.size(16.dp))

                        OutlinedTextField(
                            value = des.value,
                            onValueChange = {
                                des.value = it
                            },
                            placeholder = {
                                Text(text = "Enter Description")
                            },
                            label = {
                                Text(text = "Enter description")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

//                        OutlinedTextField(
//                            value = date.value,
//                            onValueChange = {
//                                date.value = it
//                            },
//                            placeholder = {
//                                Text(text = "Select date")
//                            },
//                            label = {
//                                Text(text = "Select date")
//                            },
//                            modifier = Modifier.fillMaxWidth()
//                                .clickable {
//                                    datePickerDialog.show()
//                                }
//                        )

                    }
                },
                confirmButton = {
                    OutlinedButton(onClick = {
                        insert(title ,des)
                        openDialog.value = false
                    },
                        modifier = Modifier.padding(end = 26.dp)
                    ) {
                        Text(text = "Save" , modifier = Modifier.padding(1.dp))
                    }
                })
        }
    }

    @Composable
    fun EachRow(todo: Todo){
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = 5.dp,
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = todo.title , fontWeight = FontWeight.ExtraBold)
                Text(text = todo.description , fontStyle = FontStyle.Italic)
            }
        }
    }

    @Composable
    fun RecyclerView(todoViewModel: TodoViewModel){

        val todos = todoViewModel.response.collectAsLazyPagingItems()

        LazyColumn {
            items(todos){todo ->
                todo?.let {
                    EachRow(todo = todo)
                }
            }
            when(todos.loadState.append){

                is LoadState.NotLoading -> Unit
                LoadState.Loading -> {
                    item {
                        LoadingItem()
                    }
                }
                is LoadState.Error -> {
                    item {

                    }
                }
            }

            when (todos.loadState.refresh){

                is LoadState.NotLoading -> Unit
                LoadState.Loading -> {
                    item {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            contentAlignment = Alignment.Center
                        ){
                            LoadingItem()
                        }
                    }
                }
                is LoadState.Error -> {
                    item {

                        ErrorItem(message = "Network error occurred.")

                    }
                }
            }

//          items(todoViewModel.response.value) { todo ->
//              EachRow(todo = todo)
//          }
      }
    }

    @Composable
    fun LoadingItem(){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressAnimated()
        }
    }

    @Composable
    fun ErrorItem(message: String) {
        Card(
            elevation = 2.dp,
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(42.dp)
                        .height(42.dp),
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    color = Color.White,
                    text = message,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }

    @Composable
    fun CircularProgressAnimated(){
        val progressValue = 0.95f
        val infiniteTransition = rememberInfiniteTransition()

        val progressAnimation by infiniteTransition.animateFloat(
            initialValue = 0.0f,
            targetValue =progressValue ,
            animationSpec = infiniteRepeatable(animation = tween(400))
        )

        CircularProgressIndicator(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(8.dp),
            progress = progressAnimation,
            strokeWidth = 10.dp)
    }


    //calling this function when save button is clicked
    private fun insert(title: MutableState<String> , des : MutableState<String>){

        lifecycleScope.launchWhenCreated {

            if (!TextUtils.isEmpty(title.value) && !TextUtils.isEmpty(des.value))
            {
                todoViewModel.insert(
                    Todo(title.value , des.value)
                )
                Toast.makeText(this@TodoActivity , "Record inserted ",Toast.LENGTH_LONG).show()

            }
            else{
                Toast.makeText(this@TodoActivity , "Field is empty",Toast.LENGTH_LONG).show()
            }
        }
    }
}