# Task App (Solo Project)

<img src="https://github.com/Gregbgarman/TaskApp/blob/master/TAgoodimage1.PNG" width=250> &nbsp; &nbsp; &nbsp; &nbsp; <img src="https://github.com/Gregbgarman/TaskApp/blob/master/TAgoodimage2.PNG" width=250><br>

<img src="https://github.com/Gregbgarman/TaskApp/blob/master/TAgoodimage3.PNG" width=250> &nbsp; &nbsp; &nbsp; &nbsp; <img src="https://github.com/Gregbgarman/TaskApp/blob/master/TAgoodimage4.PNG" width=250><br>


## OverView
This is a classic To do list app but it incorporates many additional features including Google Maps and notifications

## Motivation for App creation
As a student, I saw this application as an achievable solo project that would help grow my Android development skills. Though
the concept of a to do list app is very simple, I saw the opportunity to enjoy making this app and expanding upon it with
more advanced features.



## Features
- User can create a Task and include additional information such as a note, destination, time and date, and schedule a notification
- Tasks are saved locally using SQLite as a database
- Tasks can be added to Google Calendar
- User can add a destination to each task using Google Maps
- Live search results appear as user searches for location on Google Maps 
- User can receive a notification that when clicked, launches Google maps app to get directions (Work Manager used for scheduling)
- User can edit any task and save it


## Walkthrough videos of features

### Creating Task 
<img src="https://github.com/Gregbgarman/TaskApp/blob/master/taskwalkthru.gif" width=250><br>

### Adding Task to Google Calendar
<img src="https://github.com/Gregbgarman/TaskApp/blob/master/cal.gif" width=250><br>

### Receiving notification and launching Google Maps app
<img src="https://github.com/Gregbgarman/TaskApp/blob/master/mapp.gif" width=250><br>


## Models
**MyTask (stored using SQLite)**
| Property | Type         | Description                    |
| -------- | ------------ | ------------------------------ |
| Task | String       | Name of the task itself    |
| Note| String        | Optional Note that goes with task |
| PlaceName  | String        | Name of destination selected on Google Maps           |
| PlaceAddress | String        | Address of destination selected on Google Maps           |
| DateString | String       | Date and time of task, stored as string        |
| Notificationid | String      | ID of each scheduled notification, needed for cancelling if user chooses        |
| year     | int         | year when task takes place |
| month     | int         | month when task takes place |
| day | int        | day of week when task takes place  |
| hour | int       | hour when task takes place       |
| min | int      | minute when task takes place        |
| alerthour    | int         | hour of scheduled notification |
| alertmin     | int         | minute of scheduled notification |
| lat | double        | latitude coordinate of destination           |
| lng | double       | longitued coordinate of destination      |
| TotalTimeinMillis | long      | time in milliseconds of when task is scheduled        |
(below not stored on SQLite, but set after data retrived from SQLite database)
| AlertSet    | boolean         | true when notification scheduled |
| DestinationSet     | boolean         | true when destination set |
| TaskSet    | boolean         | true when task itself is entered |
| NoteSet     | boolean         | true when a note is entered |
| TimeSet    | boolean         | true when time and date are set for a task |


## Challenges Faced During development
- This was my first time using SQLite in an app. SQLite could be challenging to work with 
because it is very particular with spacing when creating the SQL table to store data. Much time spent debugging
and uninstalling the app was needed to implement proper storing of data with SQLite database.



## Future features that could enhance app
- Being able to sort tasks by what day of the week or month they are set



## Other Information
- API keys needed for this app are:
      Google Maps API
      


