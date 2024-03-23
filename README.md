# Todo List


**Screens**

<p float="left">
<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Screen_1.png" data-canonical-src="https://github.com/raheelmushtaq/AssessmentTest/Todo-Task/main/screens/Screen_1.png" width="200" height="400" />

<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Screen_2.png" data-canonical-src="https://github.com/raheelmushtaq/AssessmentTest/Todo-Task/main/screens/Screen_2.png" width="200" height="400" />

<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Screen_3.png" data-canonical-src="https://github.com/raheelmushtaq/AssessmentTest/Todo-Task/main/screens/Screen_3.png" width="200" height="400" />
</p>

<p float="left">
<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Screen_4.png" data-canonical-src="https://github.com/raheelmushtaq/AssessmentTest/Todo-Task/main/screens/Screen_4.png" width="200" height="400" />

<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Screen_5.png" data-canonical-src="https://github.com/raheelmushtaq/AssessmentTest/Todo-Task/main/screens/Screen_5.png" width="200" height="400" />

<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Screen_3.png" data-canonical-src="https://github.com/raheelmushtaq/AssessmentTest/Todo-Task/main/screens/Screen_6.png" width="200" height="400" />
</p>

**This Project is built using.**
* Kotlin
* MVVM Clean Architecture
* Compose UI
* Retrofit
* Hilt DI
* Unit Testing
* Kotlin Flow
* Compose State
* DataStore
* WorkManager
* <a href="https://developer.android.com/build/migrate-to-catalogs">Gradle version catalogs</a>

**Folder Structure**

<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Folder_Structure_1.png" data-canonical-src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Folder_Structure_1.png" width="200" height="400" />

* **datastore**: This package contains all the handling related to the data source
* **di**: This package contains the Hilt DI
* **network**: This package contains the handling for retrofit
* **notification**: This package contains the handling for showing the notifications
* **presentation**: this package contains all the handling related to the screes
  * **component**: This sub-package contains all the custom components used in the application
  * **models**: This package has the data class used in the screen I.e Tasks
  * **screens**: This package contains all the screens and their functionality
      * **components**:  This package will hold the sub-components that are used only for this screen
      * **state_event**: This package holds the classes that are used for communication between the ViewModel and the Compose Screens.
      * **viewmodel**: this package contains the ViewModel
  * **utils**: This package contains util class related to these screens
* **utils**:  This package holds the utils classes related to the application.


**Unit test**
<p> 
<img src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Folder_Structure_2.png" data-canonical-src="https://github.com/raheelmushtaq/Todo-Task/blob/main/screens/Folder_Structure_2.png" width="220" height="200" />
</p>

* screens
   * add_edit_task
      * ViewModel
        * AddEditTodoViewModelTest
      * FakeNotificationScheduler
   * task_list
     * viewmodel
        * TaskListViewModelTest
     * DataProvider
     * FakeDataSourceHandler



**Reference Material**
* https://developer.android.com/topic/libraries/architecture/datastore#prefs-vs-proto
* https://developer.android.com/codelabs/android-preferences-datastore#1
* https://medium.com/androiddevelopers/datastore-and-kotlin-serialization-8b25bf0be66c
* https://www.youtube.com/watch?v=yMGAbm84iIY&t=975s&ab_channel=PhilippLackner
* https://programmerofpersia.medium.com/how-to-serialize-deserialize-a-persistentlist-persistentmap-with-kotlinx-serialization-72a11a226e56
  

Developer.
Raheel Mushtaq raheelmushtaq41@gmail.com
