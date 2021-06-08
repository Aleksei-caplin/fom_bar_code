package ru.fom.barcodeapplication.ui.create

import androidx.fragment.app.viewModels
import ru.fom.barcodeapplication.R
import ru.fom.barcodeapplication.ui.base.BaseFragment
import ru.fom.barcodeapplication.viewmodels.CreateCodeViewModel

class CreateCodeFragment : BaseFragment<CreateCodeViewModel>() {
    override val viewModel: CreateCodeViewModel by viewModels()
    override val layout: Int = R.layout.fragment_create_code

    override fun setupViews() {

    }

}