package com.app.todolist.presentation.components.bottomsheets.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.app.todolist.presentation.components.bottomsheets.BottomSheetDialog
import com.app.todolist.presentation.components.button.AppButton
import com.app.todolist.presentation.components.category.CategoryView
import com.app.todolist.presentation.components.textfields.MediumText
import com.app.todolist.presentation.utils.filters.OrderBy
import com.app.todolist.presentation.utils.filters.SortBy
import com.app.todolist.presentation.utils.filters.TaskFilters
import com.app.todolist.presentation.utils.filters.TaskPriority

/*
* FilterBottomSheet is used to show filter Dialog for the user.
* it takes following parameters
* heading to show on the header
* dialog-state for visibility of the dialog
* selectedTasksFilter for showing if filter was already applied, then showing these values when filter is visible to user
* applyFilter a callback function when user presses the apply button. it takes the TaskFilters object as a parameter
* onDismiss function is called when user either drags the bottom sheet down, or user presses outside of the dialog
* categories to show to the user.
*  */
@Composable
fun FilterBottomSheet(
    heading: String,
    dialogState: Boolean,
    selectedTaskFilters: TaskFilters,
    applyFilter: (TaskFilters) -> Unit,
    onDismiss: () -> Unit,
    categories: List<String> = arrayListOf()
) {


    //base bottom-sheet dialog
    BottomSheetDialog(
        dialogState = dialogState,
        modifier = Modifier.height(530.dp),
        heading,
        onDismiss = onDismiss
    ) {
        // here we have add a filter which takes selectedFilter, categories, and applyFilter callBack
        FilterView(
            selectedTaskFilters = selectedTaskFilters,
            categories = categories,
            applyFilter = applyFilter
        )
    }
}


/*
* FilterView is to  show filter view inside the bottom sheet dialog.
* it take
* default/previous selected filter
* categories
* and apply button
* */

@Composable
fun FilterView(
    selectedTaskFilters: TaskFilters,
    categories: List<String>,
    applyFilter: (TaskFilters) -> Unit,
) {


    // saving the state of the selected priority
    val selectedPriority = remember { mutableStateOf(selectedTaskFilters.taskPriority) }

    // saving the state of the selected SortBy option
    val selectedSortBy = remember { mutableStateOf(selectedTaskFilters.sortBy) }

    // saving the state of the selected OrderBy option
    val selectedOrderBy = remember { mutableStateOf(selectedTaskFilters.orderBy) }

    // saving the state of the selected category
    val selectedCategory = remember { mutableStateOf(selectedTaskFilters.category) }
    val showDelete = remember { mutableStateOf(selectedTaskFilters.showDeleted) }

    // function to be called when user presses the clear or Apply button
    fun onApplyFilter() {
        applyFilter.invoke(
            TaskFilters(
                taskPriority = selectedPriority.value,
                orderBy = selectedOrderBy.value,
                sortBy = selectedSortBy.value,
                category = selectedCategory.value,
                showDeleted = showDelete.value
            )
        )
    }

    // this function will be called every time when the state of the selectedTaskFilter changes
    //this function will set the selected Filer properties to its respected data states
    LaunchedEffect(key1 = selectedTaskFilters) {
        selectedPriority.value = selectedTaskFilters.taskPriority
        selectedOrderBy.value = selectedTaskFilters.orderBy
        selectedSortBy.value = selectedTaskFilters.sortBy
        selectedCategory.value = selectedTaskFilters.category
        showDelete.value = selectedTaskFilters.showDeleted
    }

    // Compose to show the view vertically
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp)
            .background(Color.White)
    ) {

        // Priority view to be shown to user.
        // it will return the selected state when user changes Priority like Selecting High or Selected Low
        PriorityView(defaultPriority = selectedPriority.value) {
            selectedPriority.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Sort by view with ascending and Descending option
        // it will return the selected state when user changes the SortBy
        SortByView(defaultValue = selectedSortBy.value) {
            selectedSortBy.value = it

        }
        Spacer(modifier = Modifier.height(10.dp))

        // Order by view with ascending and Descending option
        // it will return the selected state when user changes the OrderBy

        OrderByView(defaultValue = selectedOrderBy.value) {
            selectedOrderBy.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        //
        // Category view to show list of categories
        // it will return the selected state when user changes the SortBy
        CategoriesFilterView(defaultValue = selectedCategory.value, categories) {
            selectedCategory.value = it
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showDelete.value, onCheckedChange = {
                showDelete.value = it
            })

            // show the heading
            MediumText(
                text = stringResource(id = R.string.show_delete), fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        //show button horizontally
        Row(modifier = Modifier.fillMaxWidth()) {
            //clear button
            AppButton(isSecondary = true,
                buttonText = R.string.clear,
                modifier = Modifier.weight(1f),
                isDisabled = false,
                onClick = {
                    // when user presses clear button, then remove all teh filter,
                    // and set the default value for order by, and sortBy,
                    // and call the function to close the dialog and show All the Tasks
                    selectedCategory.value = null
                    selectedOrderBy.value = OrderBy.Date
                    selectedSortBy.value = SortBy.Descending
                    selectedPriority.value = null
                    showDelete.value = false
                    onApplyFilter()
                })
            Spacer(modifier = Modifier.width(10.dp))
            //apply button
            AppButton(buttonText = R.string.apply,
                modifier = Modifier.weight(1f),
                isDisabled = false,
                onClick = {
                    // apply button to apply the filter/
                    onApplyFilter()
                })
        }

    }
}


/*
* Priority view is used to show section button with 3 different options from the TaskPriority class.
* low, medium, and high
* the parameters for this composable are
* defaultPriority is the selected Priority, by default it is null, but if user has already applied the filter
* then this will show the selected value
* showOnlySelected is used to in case of the completed Tasks, i,e, if the task is already completed  then we do not need to show user these option to perform selection
* onSelect callback with Task priority will pass the selected value to save for applying filter*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityView(
    defaultPriority: TaskPriority?,
    showOnlySelected: Boolean = false,
    onSelect: (TaskPriority?) -> Unit
) {
    // default selected priority
    val selectedPriority = remember { mutableStateOf(defaultPriority) }

    // list of priorities from TaskPriority
    val taskPriorityArrayLists =
        arrayListOf(TaskPriority.Low, TaskPriority.Medium, TaskPriority.High)

    // this will be called every time if the default state is changed
    LaunchedEffect(key1 = defaultPriority) {
        selectedPriority.value = defaultPriority
    }
    // showing the heading of Priority
    MediumText(text = stringResource(id = R.string.priority), fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(5.dp))
    // if user is in the completed screen, then showOnlySelected will be true and in this case, we have a default view created for Categories, we are showing this here.
    if (showOnlySelected) {
        // category view
        CategoryView(
            text = stringResource(id = selectedPriority.value?.resId ?: R.string.low),
            showIcon = true,
            isClickable = false
        )
    } else {
        //if not completed then show the segmented button with single selectable option
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            // loop through all the list of Priority and add the segmented button
            taskPriorityArrayLists.forEachIndexed { index, priority ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        baseShape = RoundedCornerShape(30),
                        count = taskPriorityArrayLists.size,
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = Color.White,
                        inactiveContainerColor = Color.White,
                    ),
                    onClick = {
                        // when user person the segment button, check if the selected value is not same then select the value
                        if (selectedPriority.value != priority) {
                            selectedPriority.value = priority
                            onSelect(selectedPriority.value)
                        }
                    },
                    selected = selectedPriority.value == priority // show tick icon for the selected value
                ) {
                    // title of the priority
                    MediumText(text = stringResource(id = priority.resId))
                }
            }
        }
    }

}

/*Sort by view is used to show two types of options, Ascending or Descending
* this composable take 2 parameter,
* selected Sort by, which is the default value
* onSelect a callback function it is called when user changes te sort by*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortByView(defaultValue: SortBy, onSelect: (SortBy) -> Unit) {
    // default selected sort bv value
    val selectedSortBy = remember { mutableStateOf(defaultValue) }
    // created a list to show segmented button
    val sortByList = arrayListOf(SortBy.Ascending, SortBy.Descending)

    // this will be called every time if the default state is changed
    LaunchedEffect(key1 = defaultValue) {
        selectedSortBy.value = defaultValue
    }

    // show the heading
    MediumText(text = stringResource(id = R.string.sort_by), fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(5.dp))

    //show the segmented button with single selectable option

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        sortByList.forEachIndexed { index, sortBy ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = sortByList.size,
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color.White,
                    inactiveContainerColor = Color.White,
                ),
                onClick = {
                    // when user person the segment button, check if the selected value is not same then select the value
                    if (selectedSortBy.value != sortBy) {
                        selectedSortBy.value = sortBy
                        onSelect(sortBy)
                    }
                },
                selected = selectedSortBy.value == sortBy // show tick icon for the selected value
            ) {
                // title of the sort by
                MediumText(text = stringResource(id = sortBy.resId))
            }
        }
    }

}
/*OrderBy view is used to show 3 different options from OrderBy class
* this composable take 2 parameter,
* defaultValue, which is the or selected value fo the filter
* onSelect a callback function it is called when user changes te OrderBy by*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderByView(defaultValue: OrderBy?, onSelect: (OrderBy) -> Unit) {
    // default selected order bv value

    val selectedOrderBy = remember { mutableStateOf(defaultValue) }
    // created a list to show segmented button
    val orderByList = arrayListOf(OrderBy.Date, OrderBy.Title, OrderBy.Completed)

    // this will be called every time if the default state is changed
    LaunchedEffect(key1 = defaultValue) {
        selectedOrderBy.value = defaultValue
    }
    // show the heading
    MediumText(text = stringResource(id = R.string.order_by), fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(5.dp))

    //show the segmented button with single selectable option
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        orderByList.forEachIndexed { index, orderBy ->
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
                    // when user person the segment button, check if the selected value is not same then select the value
                    if (selectedOrderBy.value != orderBy) {
                        selectedOrderBy.value = orderBy
                        onSelect(orderBy)
                    }

                },
                selected = selectedOrderBy.value == orderBy// show tick icon for the selected value
            ) {
                // title of the OrderBy
                MediumText(text = stringResource(id = orderBy.keyRes))
            }
        }
    }

}

/*Categories filter view is used to show list of filters
* this composable take 4 parameter,
* defaultValue, which is the or selected value fo the category
* categories which is the list of categories which we have saved
* readOnly is used for the cases if we want to only who value and it is not clickable
* onSelect a callback function it is called when user changes te category by*/
@Composable
fun CategoriesFilterView(
    defaultValue: String?,
    categories: List<String>,
    readOnly: Boolean = false,
    onSelect: (String?) -> Unit
) {
    // default selected order bv value
    val selectedCategory = remember { mutableStateOf(defaultValue) }
    // this will be called every time if the default state is changed
    LaunchedEffect(key1 = defaultValue) {
        selectedCategory.value = defaultValue
    }
    // show the heading
    MediumText(
        text = stringResource(id = R.string.categories), fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(10.dp))


    //show the list of Category view button with single selectable option
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            //using the component to show the  category view.
            CategoryView(
                text = category,
                isClickable = !readOnly,
                showIcon = selectedCategory.value.equals(category)
            ) {
                // when user a category, then this callback function is called which saved the default value
                selectedCategory.value = category
                onSelect(category)
            }
        }
    }

}