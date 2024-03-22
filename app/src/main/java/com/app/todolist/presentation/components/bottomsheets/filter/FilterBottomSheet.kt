package com.app.todolist.presentation.components.bottomsheets.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.todolist.R
import com.app.todolist.data.models.OrderBy
import com.app.todolist.data.models.TasksPriority
import com.app.todolist.data.models.TodoFilters
import com.app.todolist.presentation.components.bottomsheets.BottomSheetDialog
import com.app.todolist.presentation.components.button.AppButton
import com.app.todolist.presentation.components.textfields.MediumText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    heading: String,
    dialogState: Boolean,
    selectedTodoFilters: TodoFilters,
    applyFilter: (TodoFilters) -> Unit,
    onDismiss: () -> Unit,
    categories: List<String> = arrayListOf()
) {


    BottomSheetDialog(
        dialogState = dialogState,
        modifier = Modifier.height(450.dp),
        heading,
        onDismiss = onDismiss
    ) {
        FilterView(selectedTodoFilters = selectedTodoFilters, categories, applyFilter)
    }
}


@Composable
fun FilterView(
    selectedTodoFilters: TodoFilters,
    categories: List<String>,
    applyFilter: (TodoFilters) -> Unit,
) {


    val selectedPriority = remember { mutableStateOf(selectedTodoFilters.tasksPriority) }

    val selectedOrderBy = remember { mutableStateOf(selectedTodoFilters.orderBy) }

    val selectedCategory = remember { mutableStateOf(selectedTodoFilters.category) }

    fun onApplyFilter() {
        applyFilter.invoke(
            TodoFilters(
                tasksPriority = selectedPriority.value,
                orderBy = selectedOrderBy.value,
                category = selectedCategory.value
            )
        )
    }

    LaunchedEffect(key1 = selectedTodoFilters) {
        selectedPriority.value = selectedTodoFilters.tasksPriority
        selectedOrderBy.value = selectedTodoFilters.orderBy
        selectedCategory.value = selectedTodoFilters.category
    }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp)
            .background(Color.White)
    ) {

        PriorityView(defaultPrority = selectedPriority.value) {
            selectedPriority.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        Spacer(modifier = Modifier.height(10.dp))
        OrderByView(defaultValue = selectedOrderBy.value) {
            selectedOrderBy.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        CategoriesFilterView(defaultValue = selectedCategory.value, categories) {
            selectedCategory.value = it

        }

        Spacer(modifier = Modifier.height(20.dp))


        Row(modifier = Modifier.fillMaxWidth()) {
            AppButton(
                buttonText = R.string.clear,
                modifier = Modifier.weight(1f),
                isDisabled = false,
                onClick = {
                    selectedCategory.value = null
                    selectedOrderBy.value = null
                    selectedPriority.value = null
                    onApplyFilter()
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            AppButton(
                buttonText = R.string.apply,
                modifier = Modifier.weight(1f),
                isDisabled = false,
                onClick = {
                    onApplyFilter()
                }
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityView(
    defaultPrority: TasksPriority?,
    readOnly: Boolean = false,
    onSelect: (TasksPriority?) -> Unit
) {
    val selectedPriority = remember { mutableStateOf(defaultPrority) }

    val tasksPriorityArrayLists =
        arrayListOf(TasksPriority.Low, TasksPriority.Medium, TasksPriority.High)
    LaunchedEffect(key1 = defaultPrority) {
        selectedPriority.value = defaultPrority
    }
    MediumText(text = stringResource(id = R.string.priority), fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(5.dp))

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        tasksPriorityArrayLists.forEachIndexed { index, priority ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    baseShape = RoundedCornerShape(30),
                    count = tasksPriorityArrayLists.size,
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color.White,
                    inactiveContainerColor = Color.White,
                ),
                onClick = {
                    if (!readOnly) {
                        if (selectedPriority.value != priority) {
                            selectedPriority.value = priority
                        }
                        onSelect(selectedPriority.value)
                    }
                },
                selected = selectedPriority.value == priority
            ) {
                MediumText(text = stringResource(id = priority.resId))
            }
        }
    }

}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SortByView(defaultValue: SortBy?, onSelect: (SortBy?) -> Unit) {
//    val selectedSortBy = remember { mutableStateOf(defaultValue) }
//    val sortByList = arrayListOf(SortBy.Ascending, SortBy.Descending)
//
//    LaunchedEffect(key1 = defaultValue) {
//        selectedSortBy.value = defaultValue
//    }
//
//    MediumText(text = stringResource(id = R.string.sort_by), fontWeight = FontWeight.Bold)
//    Spacer(modifier = Modifier.height(5.dp))
//
//    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
//        sortByList.forEachIndexed { index, sortBy ->
//            SegmentedButton(
//                shape = SegmentedButtonDefaults.itemShape(
//                    index = index,
//                    count = sortByList.size,
//                ),
//                colors = SegmentedButtonDefaults.colors(
//                    activeContainerColor = Color.White,
//                    inactiveContainerColor = Color.White,
//                ),
//                onClick = {
//                    if (selectedSortBy.value != sortBy) {
//                        selectedSortBy.value = sortBy
//                    }
//                    onSelect(selectedSortBy.value)
//                },
//                selected = selectedSortBy.value == sortBy
//            ) {
//                MediumText(text = sortBy.toString().replace("_", ""))
//            }
//        }
//    }
//
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderByView(defaultValue: OrderBy?, onSelect: (OrderBy?) -> Unit) {

    val selectedOrderBy = remember { mutableStateOf(defaultValue) }
    val orderByList = arrayListOf(OrderBy.Date, OrderBy.Title, OrderBy.Completed)
    LaunchedEffect(key1 = defaultValue) {
        selectedOrderBy.value = defaultValue
    }
    MediumText(text = stringResource(id = R.string.order_by), fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(5.dp))

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        orderByList.forEachIndexed { index, ordrBy ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = orderByList.size,
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color.White,
                    inactiveContainerColor = Color.White,
                ),
                onClick = {
                    if (selectedOrderBy.value != ordrBy) {
                        selectedOrderBy.value = ordrBy
                    }
                    onSelect(selectedOrderBy.value)

                },
                selected = selectedOrderBy.value == ordrBy
            ) {
                MediumText(text = stringResource(id = ordrBy.keyRes))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesFilterView(
    defaultValue: String?,
    categories: List<String>,
    readOnly: Boolean = false,
    onSelect: (String?) -> Unit
) {
    val selectedCategory = remember { mutableStateOf(defaultValue) }
    LaunchedEffect(key1 = defaultValue) {
        selectedCategory.value = defaultValue
    }
    MediumText(
        text = stringResource(id = R.string.categories),
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .border(width = 1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                    .clickable {
                        if (!readOnly) {
                            if (!selectedCategory.value.equals(category)) {
                                selectedCategory.value = category
                            }
                            onSelect(category)
                        }
                    }
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCategory.value.equals(category)) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                }
                MediumText(text = category)
            }
        }
    }

}


/*
*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    heading: String,
    dialogState: Boolean,
    selectedFilter: FilterClass?,
    categories: ArrayList<String> = arrayListOf("Office", "Shopping", "Home", "Market"),
    clearFilter: () -> Unit,
    applyFilter: (FilterClass?) -> Unit?,
    onDismiss: () -> Unit
) {

    val filterState = remember {
        mutableStateOf(selectedFilter)
    }

    val selectedPriority = remember { mutableStateOf(filterState.value?.priority) }
    val priorityArrayList = arrayListOf(Priority.Low, Priority.Medium, Priority.High)

    val selectedSortBy = remember { mutableStateOf(filterState.value?.sortBy) }
    val sortByList = arrayListOf(SortBy.Ascending, SortBy.Descending)

    val selectedOrderBy = remember { mutableStateOf(filterState.value?.orderBy) }
    val orderByList = arrayListOf(OrderBy.Due_Date, OrderBy.Name)

    val selectedCategory = remember { mutableStateOf(filterState.value?.category) }

    LaunchedEffect(key1 = selectedFilter) {
        filterState.value = selectedFilter
    }

    BottomSheetDialog(
        dialogState = dialogState,
        modifier = Modifier.fillMaxSize(),
        heading,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp)
                .background(Color.White)
        ) {


            MediumText(text = stringResource(id = R.string.priority), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                priorityArrayList.forEachIndexed { index, priority ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = priorityArrayList.size,
                        ),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color.White,
                            inactiveContainerColor = Color.White,
                        ),
                        onClick = {
                            if (selectedPriority.value != priority) {
                                selectedPriority.value = priority
                            }
                        },
                        selected = selectedPriority.value == priority
                    ) {
                        MediumText(text = priority.toString())
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            MediumText(text = stringResource(id = R.string.sort_by), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                sortByList.forEachIndexed { index, sortBy ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = orderByList.size,
                        ),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color.White,
                            inactiveContainerColor = Color.White,
                        ),
                        onClick = {
                            if (selectedSortBy.value != sortBy) {
                                selectedSortBy.value = sortBy
                            }
                        },
                        selected = selectedSortBy.value == sortBy
                    ) {
                        MediumText(text = sortBy.toString().replace("_", ""))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            MediumText(text = stringResource(id = R.string.order_by), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                orderByList.forEachIndexed { index, ordrBy ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = orderByList.size,
                        ),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color.White,
                            inactiveContainerColor = Color.White,
                        ),
                        onClick = {
                            if (selectedOrderBy.value != ordrBy) {
                                selectedOrderBy.value = ordrBy
                            }
                        },
                        selected = selectedOrderBy.value == ordrBy
                    ) {
                        MediumText(text = ordrBy.toString().replace("_", " "))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            MediumText(
                text = stringResource(id = R.string.categories),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))

            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    Row(
                        modifier = Modifier
                            .background(Color.White)
                            .border(
                                width = 1.dp, color = Color.Black, RoundedCornerShape(
                                    5.dp
                                )
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clickable {
                                if (!selectedCategory.value.equals(category)) {
                                    selectedCategory.value = category
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedCategory.value.equals(category)) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                        }
                        MediumText(text = category)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            Row(modifier = Modifier.fillMaxWidth()) {
                AppButton(
                    buttonText = R.string.clear,
                    modifier = Modifier.weight(1f),
                    isDisabled = false,
                    onClick = {
                        filterState.value = null
                        clearFilter()
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                AppButton(
                    buttonText = R.string.apply,
                    modifier = Modifier.weight(1f),
                    isDisabled = false,
                    onClick = {
                        applyFilter(FilterClass(priority = selectedPriority.value))
                    }
                )
            }

        }
    }
}

*/