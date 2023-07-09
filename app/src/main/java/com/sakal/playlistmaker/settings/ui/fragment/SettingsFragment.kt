package com.sakal.playlistmaker.settings.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.FragmentSettingsBinding
import com.sakal.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initSwitch()

        initButtonSharing()

        initButtonSupport()

        initButtonUserAgreement()
    }

    private fun initSwitch() {
        binding.themeSwitcher
            .apply {
                isChecked = viewModel.isDarkTheme()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.switchTheme(isChecked)
                }
            }
    }

    fun initButtonSharing() {
        binding.buttonSharing.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

    private fun initButtonSupport() {
        binding.buttonSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                startActivity(Intent.createChooser(this, null))
            }
        }
    }


    private fun initButtonUserAgreement() {
        binding.buttonUserAgreement.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.support_user_agreement))
                startActivity(Intent.createChooser(this, null))
            }
        }
    }
}