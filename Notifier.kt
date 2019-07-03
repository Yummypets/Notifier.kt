package com.octopepper.yummypets.common.util
import kotlinx.coroutines.*


typealias Callback<T> = (T) -> Unit

class CallbackWrapper<T>(val callback:Callback<T>, val eventName: String, val receiverHash: Int, val dispatcher: CoroutineDispatcher)

object Notifier: CoroutineScope {

    private val supervisorJob = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + supervisorJob

    var callbacks = mutableListOf<CallbackWrapper<Any>>()

    fun post(event: Any, sender: Any) {
        callbacks.filter {
            it.eventName == event::class.simpleName && it.receiverHash != sender.hashCode()
        }.forEach {
            launch(it.dispatcher) {
                it.callback.invoke(event)
            }
        }
    }

    inline fun <reified T> register(receiver: Any, token: NotifierToken = NotifierToken(), on: CoroutineDispatcher = Dispatchers.Main, noinline callback: Callback<T>) {
        val wrapper = CallbackWrapper(callback, T::class.simpleName ?: "noname", receiver.hashCode(), on) as CallbackWrapper<Any>
        callbacks.add(wrapper)
        token.unregistrationCodes.add { callbacks.remove(wrapper) }
    }
}

class NotifierToken {

    var unregistrationCodes = mutableListOf<(() -> Unit)>()

    fun unsubscribe() {
        unregistrationCodes.forEach { it.invoke() }
    }
}

interface NotifierSubscriber {
    var notifierToken: NotifierToken
}

inline fun <reified T>NotifierSubscriber.subscribeFor(dispatcher: CoroutineDispatcher = Dispatchers.Main, noinline callback: Callback<T>) {
    Notifier.register(this, notifierToken, dispatcher, callback)
}

interface NotifierPublisher {
    fun publishEvent(event: Any, from: Any = this) {
        Notifier.post(event, from)
    }
}
