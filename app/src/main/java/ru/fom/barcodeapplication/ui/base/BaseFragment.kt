package ru.fom.barcodeapplication.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import ru.fom.barcodeapplication.RootActivity
import ru.fom.barcodeapplication.viewmodels.BaseViewModel

abstract class BaseFragment<T: BaseViewModel>: Fragment() {

    val main: RootActivity
        get() = activity as RootActivity

    protected abstract val viewModel: T
    protected abstract val layout: Int
    lateinit var navController: NavController

    abstract fun setupViews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }
}