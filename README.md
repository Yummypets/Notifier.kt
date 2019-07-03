# Notifier.kt
 [![Language: Kotlin](https://img.shields.io/badge/language-Kotlin-7963FE.svg?style=flat)](https://kotlinlang.org)
 ![Platform: Android 8+](https://img.shields.io/badge/platform-Android-68b846.svg?style=flat)
 [![codebeat badge](https://codebeat.co/badges/1b9d3a18-c78e-4ca9-855c-92e02b457b48)](https://codebeat.co/projects/github-com-yummypets-notifier-master)
 [![License: MIT](http://img.shields.io/badge/license-MIT-lightgrey.svg?style=flat)](https://github.com/Yummypets/Notifier.kt/blob/master/LICENSE)

Publish/Subscribe mechanism in Kotlin. (Event-Bus like)

```kotlin
// Subscribe
subscribeFor<UserDidLoginEvent> { event ->
    // do something cool
}

// Publish
publishEvent(UserDidLoginEvent())
```

## About

- [x] Simple & type-safe api (generics FTW)
- [x] Lightweight: 1 file ~ 50 lines of pure kotlin
- [x] Does NOT rely on evil Annotations
- [x] Familiar to the well-known [EventBus](https://github.com/greenrobot/EventBus) Library

## Installation
Just copy and paste `Notifier.kt` file in your project.  
Make sure  you import the `kotlinx.coroutines` on which Notifier relies.

## Usage
#### 1 - Create your event
An event is a pure kotlin class. You can use fields to store whatever data you want to pass along with the event.
```kotlin
data class NewPostEvent(val newPost: Post)
```

#### 2 - Subscribe to the event

First you need to add the `NotifierSubscriber` interface.
This will force you to implement a `notifierToken` used for future cancellation & give you the ability to call `subscribeFor<Event>`.

```kotlin
class MySubscriberFragment: Fragment(), NotifierSubscriber {

    // Store a cancellation token
    override var notifierToken = NotifierToken()

    override fun onStart() {
        super.onStart()

        // Subscribe
        subscribeFor<NewPostEvent>{ event ->
            print(event.newPost)
        }
    }

    override fun onStop() {
        super.onStop()
        notifierToken.unsubscribe()
    }
}
```

In an android `Fragment`, you would typically subscribe in `onStart` and unsubscribe in `onStop`. This way, events won't be triggered after the fragment stopped.


#### 3 - Publish your event

Add the `NotifierPublisher` interface, this will give you the ability to call `publishEvent` as shown in the example below.

```kotlin
class MyPublisherFragment: Fragment(), NotifierPublisher {
    fun foo() {
        publishEvent(NewPostEvent(myNewPost)
    }
}
```

## Contributors
[S4cha](https://github.com/s4cha), [PAFagniez](https://github.com/PAFagniez)
