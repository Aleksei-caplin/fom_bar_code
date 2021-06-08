package ru.fom.barcodeapplication.ui.start

import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_start.*
import ru.fom.barcodeapplication.R
import ru.fom.barcodeapplication.ui.base.BaseFragment
import ru.fom.barcodeapplication.viewmodels.StartFragmentViewModel


class StartFragment : BaseFragment<StartFragmentViewModel>() {

    override val viewModel: StartFragmentViewModel by viewModels()
    override val layout: Int = R.layout.fragment_start



    override fun setupViews() {

        read_fragment.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToReadBarCodeFragment2()
            main.navController.navigate(action.actionId)
        }

        create_fragment.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToCreateCodeFragment2()
            main.navController.navigate(action.actionId)
        }

        results_fragment.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToResultCodeFragment2()
            main.navController.navigate(action.actionId)
        }
    }



}

