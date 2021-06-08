package ru.fom.barcodeapplication.ui.results

import androidx.fragment.app.viewModels
import ru.fom.barcodeapplication.R
import ru.fom.barcodeapplication.ui.base.BaseFragment
import ru.fom.barcodeapplication.viewmodels.ResultCodeViewModel


class ResultCodeFragment : BaseFragment<ResultCodeViewModel>() {
    
    override val viewModel: ResultCodeViewModel by viewModels()
    override val layout: Int = R.layout.fragment_result_code

    override fun setupViews() {

    }

}