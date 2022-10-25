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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
 import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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

    //Kotlin delegation using by keyword
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
        val scaffoldState = rememberScaffoldState()
       // val scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Text(text = "Todo App")
//                    }
//                )
//            },
            floatingActionButton = {

                val openDialog = remember {
                    mutableStateOf(false) //dialog box is not open right now
                }
                FloatingActionButton(onClick = {
                    //when click on FAB dialog box show
                    openDialog.value = true
                })
                {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    //Therefore call dialog function here
                    AddDialogBox(openDialog = openDialog)

                }
            }
        ) {
            SearchBar()
            RecyclerView(todoViewModel)
        }
    }


    @Composable
    fun SearchBar(
        modifier: Modifier =    Modifier
    ){
        TextField(value = "",
            onValueChange = {},
            leadingIcon = {
                Icon( Icons.Default.Search, contentDescription = null )
            },
            placeholder = {
                Text(stringResource(id = R.string.placeholder_search))
            },
            colors = TextFieldDefaults.textFieldColors(
                //getting color from material library set by default.
                backgroundColor = MaterialTheme.colors.surface
            ),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )
    }
    @Composable
    fun AddDialogBox(openDialog: MutableState<Boolean>){

        // Fetching the Local Context
        val mContext = LocalContext.current

        val year : Int
        val month: Int
        val day: Int

        val calender = Calendar.getInstance()
        year = calender.get(Calendar.YEAR)
        month = calender.get(Calendar.MONTH)
        day = calender.get(Calendar.DAY_OF_MONTH)
        val title = remember { mutableStateOf("")}
        val des = remember { mutableStateOf("")}
        val date = remember { mutableStateOf("Date")}
       // var  datePicked: String? = null

//        link for image picker view
//        https://sgkantamani.medium.com/how-to-show-date-picker-in-jetpack-compose-8bc77a3ce408

        //date picker build in dialog
        val datePickerDialog = DatePickerDialog(
            mContext,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                date.value = "$dayOfMonth/$month/$year"
              //  datePicked = "$dayOfMonth/$month/$year"
            }, year, month, day
        )

        if (openDialog.value){

            AlertDialog(onDismissRequest = {

                openDialog.value = false

            },
                title = {
                    Text(text = "Add task " , modifier = Modifier
//                        .padding(8.dp)
                        .fillMaxWidth(),
                        style = MaterialTheme.typography.h6
                    )
                },
                text = {
//                    Spacer(modifier = Modifier.size(16.dp))
                    Column(
                        modifier = Modifier
                            .padding(5.dp)

                    ) {

                        OutlinedTextField(
                            value = title.value,
                            onValueChange = {
                                title.value = it
                            },
                            placeholder = {
                               // Text(text = "Enter Title")
                            },
                            label = {
                                Text(text = "Enter Title")
                            } ,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = des.value,
                            onValueChange = {
                                des.value = it
                            },
                            placeholder = {
//                                Text(text = "Enter Description")
                            },
                            label = {
                                Text(text = "Enter Description")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        //start date picker code
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopStart)
                                .padding(top = 10.dp)
                                .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                                .clickable {
                                    datePickerDialog.show()
                                }
                        ){
                            Spacer(modifier = Modifier.size(16.dp))
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                val (lable, iconView) = createRefs()

                                Text(
                                    text= "${date.value}",
                                    color = MaterialTheme.colors.onSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .constrainAs(lable) {
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                            start.linkTo(parent.start)
                                            end.linkTo(iconView.start)
                                            width = Dimension.fillToConstraints
                                        }
                                )

                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp, 20.dp)
                                        .constrainAs(iconView) {
                                            end.linkTo(parent.end)
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                        },
                                    tint = MaterialTheme.colors.onSurface
                                )
                            }
                        }
                        //end

                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            insert(title, des, date)
                            openDialog.value = false
                        },
                        // Uses ButtonDefaults.ContentPadding by default
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            top = 12.dp,
                            end = 10.dp,
                            bottom = 12.dp
                        ),
                        // modifier = Modifier.padding(end = 26.dp)
                    ) {
                        // Inner content including an icon and a text label
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favorite",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Save")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        },
                        // Uses ButtonDefaults.ContentPadding by default
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            top = 12.dp,
                            end = 10.dp,
                            bottom = 12.dp
                        ),
                        //  modifier = Modifier.padding(end = 26.dp)
                    ) {

                        // Inner content including an icon and a text label
                        Icon(
                            Icons.Filled.Cancel,
                            contentDescription = "Cancel",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )

                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }

    @Composable
    fun EachRow(
        todo:Todo ,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        onClose: () -> Unit,
        modifier: Modifier = Modifier
    ){
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = 5.dp,
            shape = RoundedCornerShape(2.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ){

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = todo.title ,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = todo.description,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.body1)
                }

                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }
        }
    }

    @Composable
    fun EachRowStateless (todo: Todo){

        EachRow(
            todo = todo,
            checked = todo.status,
            onCheckedChange =  { newValue ->
                todo.status = newValue
                 //now update this value in database through viewmodel
                  todoViewModel.update(todo)},
            onClose = {
                //delete record from database
                todoViewModel.deleteTask(todo)
            })
    }
    @Composable
    fun RecyclerView(todoViewModel: TodoViewModel){

        Surface(
            color = MaterialTheme.colors.background
        ) {

            val todos = todoViewModel.response.collectAsLazyPagingItems()
            if(todos != null){
                LazyColumn {
                    items(todos){todo ->
                        todo?.let {
                            //EachRow(todo = todo)
                            EachRowStateless(todo = todo)
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
            else{
                Text(text = "No Record found",
                    style = MaterialTheme.typography.h6)
            }
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
    private fun insert(title: MutableState<String> , des : MutableState<String> , date: MutableState<String>){

        lifecycleScope.launchWhenCreated {

            if (!TextUtils.isEmpty(title.value) && !TextUtils.isEmpty(des.value) && !date.value.equals("Date"))
            {
                todoViewModel.insert(
                    Todo(title.value , des.value , false ,date.value)
                )
                Toast.makeText(this@TodoActivity , "Record inserted ",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this@TodoActivity , "Field is empty",Toast.LENGTH_LONG).show()
            }
        }
    }

    @Preview
    @Composable
    fun DialogePreview(){
        val openDialog = remember {
            mutableStateOf(true) //dialog box is not open right now
        }
        AddDialogBox(openDialog = openDialog)
    }

    @Preview
    @Composable
    fun EachRowPreview(){
        val todo = Todo("Title","Description" , false  , "date ")
        EachRow(todo = todo, checked = false, onCheckedChange = { Boolean -> Unit } , onClose = {  })
    }

    @Preview
    @Composable
    fun SearchBarPreview() {
        SearchBar(Modifier.padding(8.dp))
    }
    @Preview
    @Composable
    fun AddToolbarPreview(){
        AddToolbar()
    }
}