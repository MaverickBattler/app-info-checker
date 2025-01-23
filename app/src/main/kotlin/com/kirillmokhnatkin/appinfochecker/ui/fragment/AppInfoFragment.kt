package com.kirillmokhnatkin.appinfochecker.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kirillmokhnatkin.appinfochecker.R
import com.kirillmokhnatkin.appinfochecker.databinding.FragmentAppInfoBinding
import com.kirillmokhnatkin.appinfochecker.flow_util.collectValues
import com.kirillmokhnatkin.appinfochecker.ui.state.AppInfoUiState
import com.kirillmokhnatkin.appinfochecker.ui.viewmodel.AppInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AppInfoFragment : Fragment() {

    private var _binding: FragmentAppInfoBinding? = null
    private val binding get() = _binding!!

    private val args: AppInfoFragmentArgs by navArgs()

    private val viewModel by viewModel<AppInfoViewModel> {
        parametersOf(args.packageName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.uiState.collectValues(lifecycleScope, lifecycle) { uiState ->
            when (uiState) {
                is AppInfoUiState.Content -> {
                    binding.apply {
                        errorTextview.isVisible = false
                        progressCover.fadeOut()
                        appIconImageview.setImageDrawable(uiState.appIcon)
                        appNameTextview.text = uiState.appName
                        appVersionTextview.text = uiState.versionName
                        appPackageNameValueTextview.text = uiState.packageName
                        appApkChecksumValueTextview.text = uiState.checkSum
                        openAppButton.isEnabled = uiState.buttonOpenAppEnabled
                    }
                }

                AppInfoUiState.Loading -> {
                    binding.apply {
                        errorTextview.isVisible = false
                        progressCover.setPossiblyDisappearingViewVisible()
                    }
                }

                AppInfoUiState.Error -> {
                    binding.apply {
                        progressCover.setPossiblyDisappearingViewVisible()
                        errorTextview.isVisible = true
                    }
                }
            }
        }
        binding.openAppButton.setOnClickListener {
            openApp(requireActivity(), args.packageName)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openApp(context: Context, packageName: String) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.app_opening_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private var fadingOutAnimation: ViewPropertyAnimator? = null
    private var animationRunning = false

    private fun View.fadeOut(duration: Long = DEFAULT_APPEARING_DURATION) {
        if (!animationRunning && isVisible) {
            animationRunning = true
            fadingOutAnimation = this.animate().alpha(0f).setDuration(duration).withEndAction {
                isVisible = false
                animationRunning = false
                fadingOutAnimation = null
            }
        }
    }

    private fun View.setPossiblyDisappearingViewVisible() {
        fadingOutAnimation?.cancel()
        isVisible = true
        alpha = 1f
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val DEFAULT_APPEARING_DURATION = 200L
    }
}