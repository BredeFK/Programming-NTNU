# Lab 4: Chat application

## Author: Brede Fritjof Klausen

## The idea

Users of the app can post text messages that are seen by everyone. The app is like a single chat room, that everyone having the app installed, participates in. When the app is in foreground, it works as real-time chat app, ie. messages appear as they happen, and the user can enter new messages. Self-messages should appear in the view, too. When the app is not in the foreground, the background service should periodically check for new messages (time to pull configured by preferences, similar to Lab 2). When new message is available, the Notification should be used, to communicate that to the user. The user can start the foreground activity from the Notification.

## Technologies

For this exercise, we will make use of:
* Firebase -- cloud-based storage, Real-time Database OR Cloud Firestore
* Service -- background fetching of the data updates
* Notifications -- communication with a user from within the background Service
* UI: tabs, ListView, Notifications


## Data storage

The app will use Firebase, with Real-time Database or Cloud Firestore, setup. The schema will consist of a single root element, and two subelements (or single database with two collections). One called "users" and called "messages", that will have an array of messages in a form
* d -- date, stored as timestamp, then converted to String, eg.

```java
    long date = Calendar.getInstance().getTimeInMillis();
    SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
    sfd.format(date);
```

* u -- user nickname, as string, limited to 35 UTF-8 characters.
* m -- message, as string, limited to 256 UTF-8 characters.

So, for example, the store will look as follows, after two messages are posted:

```json
{
 "messages": {
    "JGK-njfzvvdKz0V": {
        "d": 1466799937914
        "u": "bob"
        "m": "Hi, this is from Bob!"
     },
    "JGK-njfzvvdKabc": {
        "d": 1466799997914
        "u": "alice"
        "m": "Hi, Bob, nice to meet you."
     }
  }
}
```

## Nicknames

Users can pick their nickname upon first start after installing the app. The app can suggest the nickname for the user (a random nickname), but the user can overwrite it with what they want. For simplicity of the app, we assume user nicknames are Globally Unique. The user cannot change their nickname after they have picked one at the start of the app. The user nickname must be stored in the private app preferences, such that the user has no access to it, only the app has.

## Authentication

The app will make use of the Anonymous authentication in Firebase. This will allow users to use the app without providing any form of authentication, but, it will force the developer to make sure that only authenticated users can post the messages to the database. Therefore, using simple "curl-client" one would not be able to spam the database as non-authenticated user, and, new messages would have to be provided from the Android app.

Can one spawn multiple copies of the app through an emulator and spam the database this way?

## Showing the messages: tabs and sorting

The app should have two tabs: the main feed, and the users list. A simple ListView can be used in both cases. In the main feed tab, the feed should be sorted by time, in simple chronological order. All messages should be shown. In the other tab, the ListView should list all user nicknames, sorted alphabetically. Pressing on a given nickname, should provide a list view with all messages FROM that user only, in chronological order.

How would you handle paging of messages when the total number of messages is really large? Say, thousands?


# Checklist

* [X] The repo URL is correct. The project has a Readme file. `git clone http://prod3.imt.hig.no/BredeFK/Lab04`
* [X] The code is well structured, and well organised.
* [X] There are no secret credentials in the repository.
* [X] There are no Linter warnings, or, the warnings are documented and justified in the project Readme file.
* [X] The app starts and provides a user with a unique global nickname, that the user can edit/modify.
* [X] The user is not able to modify previously chosen Nickname, after accepting it.  The nickname is stored in private preferences of the app.
* [X] The main screen of the app has two tabs with nice icons: Messages Feed, and Friends List. (Note: Everyone is everyone's friend and everyone follows everyone - one global chat room).
* [X] The user can enter new message from the Feed tab. This can be achieved either inlined with the message list (the UI has the messages list and a TextEdit field with the button), or, through an Action Button that opens up a message editor/submit ability as separate from the List of messages. Discuss with the app author the choice they have made.
* [X] When new message is typed and submitted it automatically shows up in the message feed. (Note: self-messages are shown)
* [X] When new message is typed and submitted it automatically shows up in the messages for the user nickname in the Friends list.
* [X] When the second app is installed (phone or emulator), the two participants can see each other messages, and they are shown in Friends List view.
* [X] The Friends List tab lists nicknames ONCE only (no duplications).
* [ ] When the app is gone from foreground, and new message is posted by another user, within the predefined timeframe, the background service will post a Notification that shows up on the user phone. The notification can be used to open the app (or bring it back to the foreground).


## Questions

* How would you implement the ability for users to edit/delete their own posts, but not someone else's?

* In the current implementation, if two users choose the nickname "bob", they will appear as if there is only one user with that nickname. How would you implement globally unique user IDs, and how would you enforce it? Can users pick their own nicknames then?

* Is it possible with Firebase to implement the ability for users to login with their FB, Google, Github identities? Can you link it with the previously established anonymous identity?

* Current implementation requires background Service to monitor periodically for updates. This makes the app real-time ONLY when the foreground app is running. How would you possibly make it such that instead of PULL-mode, the PUSH mode is used, and notifications are done in real-time also? Hint: check Messaging, and PULL- vs. PUSH-models for data synchronisations.

## Sources
- https://stackoverflow.com/questions/7683448/in-java-how-to-get-substring-from-a-string-till-a-character-c
- https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
- https://www.youtube.com/watch?v=bNpWGI_hGGg
- https://stackoverflow.com/questions/49513356/error-running-app-unknown-error-in-android-studio-3-1
- https://stackoverflow.com/questions/2183962/how-to-read-value-from-string-xml-in-android
- https://stackoverflow.com/questions/9343241/passing-data-between-a-fragment-and-its-container-activity
- https://android--code.blogspot.no/2015/05/android-relativelayout-rounded-corners.html
- https://stackoverflow.com/questions/3606530/listview-scroll-to-the-end-of-the-list-after-updating-the-list
- https://stackoverflow.com/questions/41996802/how-to-set-the-align-parent-right-attribute-of-textviews-in-relative-layout-prog
- https://stackoverflow.com/questions/1914477/how-do-i-remove-lines-between-listviews-on-android
- https://firebase.google.com/docs/reference/js/firebase.database.Query
- https://firebase.google.com/docs/android/setup?authuser=0
- https://material.io/icons/#ic_message
- https://material.io/icons/#ic_language
- https://developer.android.com/training/appbar/setting-up.html
- https://stackoverflow.com/questions/2632272/android-notification-doesnt-disappear-after-clicking-the-notifcation
- https://github.com/JohanAanesen/imt3673-Lab04/blob/master/app/src/main/java/com/example/johanaanesen/imt3673_lab04/NotificationActivity.java
