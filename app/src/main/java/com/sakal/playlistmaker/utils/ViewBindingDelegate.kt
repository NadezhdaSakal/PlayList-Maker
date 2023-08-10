package com.sakal.playlistmaker.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> Fragment.viewBinding(): ViewBindingDelegate<T> {
    val fragment: Fragment = this
    return ViewBindingDelegate(fragment = fragment, viewBindingClass = T::class.java)
}

class ViewBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val viewBindingClass: Class<T>,
) {
    private var binding: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val viewLifecycleOwner: LifecycleOwner = fragment.viewLifecycleOwner

        when {
            viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED -> {
                throw IllegalStateException(
                    "Called after onDestroyedView()"
                )
            }

            fragment.view != null -> return getOrCreateBinding(viewLifecycleOwner)
            else -> throw IllegalStateException("Called before onViewCreated()")
        }

    }

    @Suppress("UNCHECKED_CAST")
    private fun getOrCreateBinding(viewLifecycleOwner: LifecycleOwner): T {
        return this.binding ?: let {
            val method: Method = viewBindingClass.getMethod("bind", View::class.java)
            val binding: T = method.invoke(null, fragment.view) as T

            viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    this@ViewBindingDelegate.binding = null
                }
            })
            this.binding = binding
            binding
        }

    }
}