package ru.fom.barcodeapplication

import androidx.activity.viewModels
import androidx.navigation.Navigation
import ru.fom.barcodeapplication.ui.base.BaseActivity
import ru.fom.barcodeapplication.viewmodels.MainViewModel

class RootActivity : BaseActivity<MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()
    override val layout: Int = R.layout.activity_main

    override fun onStart() {
        super.onStart()

        navController = Navigation.findNavController(this, R.id.nav_host)
    }




}