ActivityRecognition
====  
ActivityRecognition is an android application which can recognize uesr's activity. The user of our application could be able to:
ActivityRecognition是一个可以识别uesr活动的android应用程序。我们的应用程序的用户可以：
* Our application starts with a greeting to the user with the current date and time. Then immediately recognize the user’s activity (walking, running, in vehicle or still).
我们的应用程序首先向用户发出当前日期和时间的问候语。然后立即识别用户的活动（步行，跑步，车辆或静止）。
* Whenever an activity is recognized, an appropriate picture (your choice) is displayed and text describing the activity is displayed as well.
每当识别出活动时，都会显示适当的图片和文本描述活动也会显示。
* Whenver the user is walking or running music from an MP3 is played.
当用户走路或跑步时播放MP3音乐。
* When the user is walking, display a map of your current location at the top of the screen.
当用户走路时，在屏幕顶部显示当前位置的地图。
* Create a local SQLite database to continuously store the start time of each activity along with the activity type. E.g. (12:24, walking).
创建一个本地SQLite数据库，以连续存储每个活动的开始时间活动类型。
* Whenever a user switches to a new activity, a toast pops up displaying how long the last activity lasted. For instance, if the user was walking and became still, a toast may pop up announcing "You have just walked for 1 min, 36 seconds".
每当用户切换到新活动时，会弹出一个显示一个Toast显示最近一个活动持续了多久。例如，如果用户正在行走并且变得静止，则可能会弹出一个Toast宣布“你刚刚走了1分36秒”。
* when the user is walking or in vehicle show his/her continuous movement on the map.
当用户走路或在车辆中显示他/她在地图上的连续移动状态。

Environment
-------
Created by Android Studio 3.1.1.
minSdkVersion 23(Android6.0).
Author
Carl and Ailwyn
