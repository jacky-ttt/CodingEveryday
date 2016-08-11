# Day016
day16 of #CodingEveryday

I wrote an article on Medium.  
  
EventBus is a very good way to communicate between thread. In any decent app, it must have some part of code that runs on thread other than the main thread (UI thread). Handler, intent filter (listening incoming text messages), AsyncTask, and Loader are some of the ways I have tried to accomplish that kind of background task. All of them has their own way to communicate with the main thread, which I found it is confusing and troublesome. EventBus establish a standard pattern for posting data across thread. The way they implement it is straight forward and it can be easily adopted to existing code base.