package com.app.todolist.presentation.components.bottomsheets.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.app.todolist.presentation.utils.filters.OrderBy
import com.app.todolist.presentation.utils.filters.TaskPriority
import com.app.todolist.presentation.utils.filters.TaskFilters
import com.app.todolist.presentation.components.bottomsheets.BottomSheetDialog
import com.app.todolist.presentation.components.button.AppButton
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.utils.filters.SortBy

@Composable
fun FilterBottomSheet(
    heading: String,
    dialogState: Boolean,
    selectedTaskFilters: TaskFilters,
    applyFilter: (TaskFilters) -> Unit,
    onDismiss: () -> Unit,
    categories: List<String> = arrayListOf()
) {


    BottomSheetDialog(
        dialogState = dialogState,
        modifier = Modifier.height(530.dp),
        heading,
        onDismiss = onDismiss
    ) {
        FilterView(selectedTaskFilters = selectedTaskFilters, categories, applyFilter)
    }
}


@Composable
fun FilterView(
    selectedTaskFilters: TaskFilters,
    categories: List<String>,
    applyFilter: (TaskFilters) -> Unit,
) {


    val selectedPriority = remember { mutableStateOf(selectedTaskFilters.taskPriority) }

    val selectedSortBy = remember { mutableStateOf(selectedTaskFilters.sortBy) }

    val selectedOrderBy = remember { mutableStateOf(selectedTaskFilters.orderBy) }

    val selectedCategory = remember { mutableStateOf(selectedTaskFilters.category) }

    fun onApplyFilter() {
        applyFilter.invoke(
            TaskFilters(
                taskPriority = selectedPriority.value,
                orderBy = selectedOrderBy.value,
                sortBy = selectedSortBy.value,
                category = selectedCategory.value
            )
        )
    }

    LaunchedEffect(key1 = selectedTaskFilters) {
        selectedPriority.value = selectedTaskFilters.taskPriority
        selectedOrderBy.value = selectedTaskFilters.orderBy
        selectedSortBy.value = selectedTaskFilters.sortBy
        selectedCategory.value = selectedTaskFilters.category
    }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp)
            .background(Color.White)
    ) {

        PriorityView(defaultPriority = selectedPriority.value) {
            selectedPriority.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        SortByView(defaultValue = selectedSortBy.value) {
            selectedSortBy.value = it

        }
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
            AppButton(isSecondary = true,
                buttonText = R.string.clear,
                modifier = Modifier.weight(1f),
                isDisabled = false,
                onClick = {
                    selectedCategory.value = null
                    selectedOrderBy.value = OrderBy.Date
                    selectedPriority.value = null
                    onApplyFilter()
                })
            Spacer(modifier = Modifier.width(10.dp))
            AppButton(buttonText = R.string.apply,
                modifier = Modifier.weight(1f),
                isDisabled = false,
                onClick = {
                    onApplyFilter()
                })
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityView(
    defaultPriority: TaskPriority?,
    showOnlySelected: Boolean = false,
    onSelect: (TaskPriority?) -> Unit
) {
    val selectedPriority = remember { mutableStateOf(defaultPriority) }

    val taskPriorityArrayLists =
        arrayListOf(TaskPriority.Low, TaskPriority.Medium, TaskPriority.High)
    LaunchedEffect(key1 = defaultPriority) {
        selectedPriority.value = defaultPriority
    }
    MediumText(text = stringResource(id = R.string.priority), fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(5.dp))
    if (showOnlySelected) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .border(width = 1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            MediumText(text = stringResource(id = selectedPriority.value?.resId ?: R.string.low))
        }
    } else {

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            taskPriorityArrayLists.forEachIndexed { index, priority ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        baseShape = RoundedCornerShape(30),
                        count = taskPriorityArrayLists.size,
                    ), colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = Color.White,
                        inactiveContainerColor = Color.White,
                    ), onClick = {
                        if (selectedPriority.value != priority) {
                            selectedPriority.value = priority
                            onSelect(selectedPriority.value)
                        }
                    }, selected = selectedPriority.value == priority
                ) {
                    MediumText(text = stringResource(id = priority.resId))
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortByView(defaultValue: SortBy, onSelect: (SortBy) -> Unit) {
    val selectedSortBy = remember { mutableStateOf(defaultValue) }
    val sortByList = arrayListOf(SortBy.Ascending, SortBy.Descending)

    LaunchedEffect(key1 = defaultValue) {
        selectedSortBy.value = defaultValue
    }

    MediumText(text = stringResource(id = R.string.sort_by), fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(5.dp))

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        sortByList.forEachIndexed { index, sortBy ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = sortByList.size,
                ), colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color.White,
                    inactiveContainerColor = Color.White,
                ), onClick = {
                    if (selectedSortBy.value != sortBy) {
                        selectedSortBy.value = sortBy
                        onSelect(sortBy)
                    }
                }, selected = selectedSortBy.value == sortBy
            ) {
                MediumText(text = stringResource(id = sortBy.resId))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderByView(defaultValue: OrderBy?, onSelect: (OrderBy) -> Unit) {

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
                ), colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color.White,
                    inactiveContainerColor = Color.White,
                ), onClick = {
                    if (selectedOrderBy.value != ordrBy) {
                        selectedOrderBy.value = ordrBy
                        onSelect(ordrBy)
                    }

                }, selected = selectedOrderBy.value == ordrBy
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
        text = stringResource(id = R.string.categories), fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            Row(modifier = Modifier
                .background(Color.White)
                .border(width = 1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                .clickable {
                    if (!readOnly) {
                        if (!selectedCategory.value.equals(category)) {
                            selectedCategory.value = category
                            onSelect(category)
                        }
                    }
                }
                .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically) {
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