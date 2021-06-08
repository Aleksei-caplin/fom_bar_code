package ru.fom.barcodeapplication.ui.read

import androidx.fragment.app.viewModels
import ru.fom.barcodeapplication.R
import ru.fom.barcodeapplication.ui.base.BaseFragment
import ru.fom.barcodeapplication.viewmodels.ReadCodeViewModel


class ReadBarCodeFragment : BaseFragment<ReadCodeViewModel>() {
    override val viewModel: ReadCodeViewModel by viewModels()
    override val layout: Int = R.layout.fragment_read_bar_code

    override fun setupViews() {

    }

}