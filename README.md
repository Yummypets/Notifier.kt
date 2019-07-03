# Notifier
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

Notifier
- [x] Simple & type-safe api (generics FTW)
- [x] Lightweight: 1 file ~ 50 lines of pure kotlin
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
