# XKCD Comic App

---
### Table of Contents

- [Description](#description)
- [Project Structure](#project-structure)
- [Project Architecture](#project-architecture)
- [Functionality](#functionality)
- [Author Info](#author-info)

---

## Description

This is the code for the Android app developed as part of the Shortcut's coding challenge. The app was tested on Google Pixel 3a and OnePlus 5 before submission.

---

## Project Structure

The code is located in **com.sarim.xkcd**. Inside, there are several sub-directories to separate relevant groups of files. 
- The **comic** directory, contains the database components for initializing a Room database for storing comics and performing CRUD operations on them.
- **di** contains java files for Dagger2 dependency injection.
- **retrofit** contains the Retrofit client and the interface that is used to communicate with the endpoints for retrieving comics.
- **ui** contains subdirectories for databinding and registering and responding to UI events.
- **usecases** separates out the logic, for performing different functions inside the app, from the Activity files.
- a separate directory where all the tests are stored

## Project Architecture

I use the MVVM architecture with a clean component where I am able to separate the logic for performing different tasks from the activities. Dagger2 is used for dependency injection and Room for storing favorite comics in a persistent database or simply caching comics. 

The comics are associated to a LiveData object that follows the Observer design pattern. This allows me to register a listener that responds to any changes made to the underlying repository.

SharedPreferences is used for keeping track of the latest comic on the remote database. This is queried against the remote database every time the user opens the app to check whether a new comic was published or not.

Databinding is used to reduce boiler plate code associated with updating views whenever data changes. Finally, unit and integration tests were written to ensure the code is stable.

## Functionality

- The app downloads the first 5 comics in the beginning and caches them inside the database using Room.
- When the app is installed for the first time and then opened, the **https://xkcd.com/info.0.json** is pinged to get the latest comic and this is shown as a notification to the user. The user can click this notification to be taken straight to the comic.
- Two separate tabs are used for displaying comics. The **All Comics** tab provides all the comics and the **Favorites** tab only shows the comics marked as favorite by the user. Comics marked as favorite are available online as well.
- 5 comics at a time are displayed on a single page and the user has the option to move between pages or hop several pages forward or backwards. This is possible for both tabs.
- User can mark comics as favorite while looking at them casually in the ScrollView or when they are looking at the full details of an individual comic. To mark a comic as favorite, simply tap the star and it will turn red to indicate that.
- Use the search bar at the bottom of the screen to search comics by text (a webview opens in that case which shows the search results on **https://relevant-xkcd.github.io/**) or by comic number (the comic is retrieved from the remote database if it exists).
- While viewing a comic's detail, user can tap the **Explanation** button to open a webview with the entire explanation of the comic presented.
- Comics can be sent to another person via an email app. Simply select the EditText dialog and type in the email address for the person you wish to send the comic too.

## Author Info

- LinkedIn - [@sarimmehdi](https://www.linkedin.com/in/sarimmehdi550/)
- Website - [Sarim Mehdi](https://sarimmehdi.github.io/individual.html?latest)
