package com.mbmc.fiinfo.ui.dashboard

import android.os.Bundle

import androidx.fragment.app.Fragment
import com.mbmc.fiinfo.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.helper.PreferenceManager
import com.mbmc.fiinfo.ui.codes.CodesFragment
import com.mbmc.fiinfo.ui.welcome.WelcomeFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    @Inject lateinit var preferenceManager: PreferenceManager

    private val viewModel: DashboardViewModel by viewModels()
    private val adapter = EventAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                findNavController().navigate(DashboardFragmentDirections.actionDashboardToSettings())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bindUi() {
        binding.events.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest {
                    binding.progress.visibility = View.GONE
                    adapter.submitData(it)
                }
            }
        }

        binding.switcher.setOnClickListener {
            showCodes()
        }

        showWelcome()
    }

    private fun showWelcome() {
        if (!preferenceManager.hasSeenWelcome()) {
            WelcomeFragment().show(parentFragmentManager, WelcomeFragment.TAG)
        }
    }

    private fun showCodes() {
        CodesFragment().show(parentFragmentManager, CodesFragment.TAG)
    }
}
