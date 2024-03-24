package com.app.todolist.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.app.todolist.datastore.model.AppSettings
import com.app.todolist.presentation.models.Tasks
import kotlinx.collections.immutable.mutate
import javax.inject.Inject

/*
* this is the creation of the datastore.
* this extension functions uses the filename, and the serializer we have created to create the datastore
* which is then is used with the context
 */
val Context.dataStore: DataStore<AppSettings> by dataStore(
    fileName = "task_datastore.json", serializer = AppSettingsSerializer()
)

/*
* this class is used to handle the operation for saving all the data in the datastore.
* for reference how to save list perform the changes in the persistent list https://www.youtube.com/watch?v=yMGAbm84iIY&t=975s&ab_channel=PhilippLackner
*/
class DataStoreHandler @Inject constructor(private val context: Context) :
    DataStoreHandlerInterface {

    /*
    * this function is used to return the flow object of the data store.
    * when datastore is modified then this getAppSettings gets the updating value.
    */
    override fun getAppSettings() = context.dataStore.data

    /*
    * this function is used to save list of tasks.
    * this function is used once at the splash Screen, when the list of tasks is returned from the server*/
    override suspend fun addTasks(tasks: List<Tasks>) {
        /*
        * here the dataStore  updates it value by creating a copy of the original variable,
        * then by using mutate on persistent List, datastore allows list for modification  is allowed for modification and we add the list of tasks
        * as this is only called once, the recordCount using the total number of the tasks.
        * */
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                list.clear()
                list.addAll(tasks)
            }, recordCount = tasks.size)
        }
    }

    /* this function is used to update the tasks  */
    override suspend fun updateTask(task: Tasks) {
        /*
        * here it is updating the task by checking if the task already exist in the list by matching id of the tasks
        * then the task is updated.
        * this function can be used to for adding the task but but for the separation of function it is not used
        * */
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1) list[index] = task
            })
        }
    }

    /* this function is used to add task*/
    override suspend fun addTask(task: Tasks) {
        /*
        * here a new task is added to the list.
        * also at the time of adding a new value, the record count of the total task is also updated by 1
        * */
        context.dataStore.updateData { appSettings ->
            appSettings.copy(
                tasks = appSettings.tasks.mutate { list ->
                    list.add(task)
                }, recordCount = appSettings.recordCount + 1
            )
        }
    }

    /*this function is used to delete task*/
    override suspend fun deleteTasks(task: Tasks) {
        /*
        * here a task is removed from the list by matching the id of the tasks */
        context.dataStore.updateData { appSettings ->
            appSettings.copy(tasks = appSettings.tasks.mutate { list ->
                val index = list.indexOfFirst { item -> item.id == task.id }
                if (index != -1) list.removeAt(index)
            })
        }
    }

    /*
     * this function is used to save list of categories.
     * this function is used once at the splash Screen, when the list of tasks is returned from the server*/
    override suspend fun saveCategories(categories: List<String>) {
        /*
        * here we are using saving the list of categories which are  to list of categories from th it value by creating a copy of the original settings,
        * */
        context.dataStore.updateData { appSettings ->
            appSettings.copy(categories = appSettings.categories.mutate { list ->
                list.clear()
                list.addAll(categories)
            })
        }
    }
}