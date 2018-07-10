package com.app.venyoo.base

import android.os.Bundle
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BasePresenter<V : BaseView> {

    protected val disposables = CompositeDisposable()
    private var view: WeakReference<V>? = null

    private var loaded: Boolean = false

    fun takeView(view: V?) {
        if (view == null) throw NullPointerException("new itemView must not be null")

        if (this.view != null) dropView(this.view!!.get())

        this.view = WeakReference(view)
        if (!loaded) {
            loaded = true
            onLoad()
        }
    }

    fun dropView(view: V?) {
        if (view == null) throw NullPointerException("dropped itemView must not be null")
        loaded = false
        this.view = null
        onDestroy()
    }

    protected fun getView(): V {
        if (view == null)
            throw NullPointerException("getItemView called when itemView is null. Ensure takeView(View itemView) is called first.")
        return view!!.get()!!
    }

    open fun onLoad() {}

    fun onRestore(@NonNull savedInstanceState: Bundle) {}

    fun onSave(@NonNull outState: Bundle) {}

    fun onDestroy() {
        disposables.dispose()
    }
}