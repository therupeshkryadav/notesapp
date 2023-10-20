package notes.mynotes.mynotes.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import notes.mynotes.mynotes.R
import notes.mynotes.mynotes.adapter.NotesAdapter
import notes.mynotes.mynotes.databinding.FragmentHomeBinding
import notes.mynotes.mynotes.viewmodel.NotesViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val viewModel: NotesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        //toolbar setup
        toolBarSetUp()

        val gridLayout = GridLayoutManager(requireContext(), 2)
        binding.rcvAllNotes.layoutManager = gridLayout

        viewModel.getAllNotes().observe(viewLifecycleOwner) { notesList ->
            binding.rcvAllNotes.adapter = NotesAdapter(requireContext(), notesList)
        }

        binding.btnAddNotes.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_homeFragment_to_createNotesFragment)
        }

        binding.ivGetHighNotes.setOnClickListener {
            viewModel.getHighNotes().observe(viewLifecycleOwner) { notesList ->
                binding.rcvAllNotes.adapter = NotesAdapter(requireContext(), notesList)
            }
        }

        binding.ivGetMediumNotes.setOnClickListener {
            viewModel.getMediumNotes().observe(viewLifecycleOwner) { notesList ->
                binding.rcvAllNotes.adapter = NotesAdapter(requireContext(), notesList)
            }
        }
        binding.ivGetLowNotes.setOnClickListener {
            viewModel.getLowNotes().observe(viewLifecycleOwner) { notesList ->
                binding.rcvAllNotes.adapter = NotesAdapter(requireContext(), notesList)
            }
        }

        binding.ivAllNotes.setOnClickListener {
            viewModel.getAllNotes().observe(viewLifecycleOwner) { notesList ->
                binding.rcvAllNotes.adapter = NotesAdapter(requireContext(), notesList)
            }
        }



        return binding.root
    }

    private fun toolBarSetUp() {
        binding.toolbarHome.title = ""
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarHome)
        if ((activity as AppCompatActivity).supportActionBar != null) {
        }
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_share -> {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    var shareMessage = "\nShare with you loved ones\n\n"
                    shareMessage = """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${notes.mynotes.mynotes.BuildConfig.APPLICATION_ID}""".trimIndent()
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch (e: Exception) {
                }
                true
            }
            R.id.mi_rate_us -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + requireActivity().packageName)
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + requireActivity().packageName)
                        )
                    )
                }
                true
            }
            R.id.mi_privacy -> {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_homeFragment_to_privacyPolicyFragment)
                true
            }
            R.id.mi_feedback -> {
                val intent = Intent(Intent.ACTION_SEND)
                val recipients = arrayOf("rishabh1112131415@gmail.com")
                intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent.putExtra(Intent.EXTRA_SUBJECT, "")
                intent.putExtra(Intent.EXTRA_TEXT, "")
                intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com")
                intent.type = "text/html"
                intent.setPackage("com.google.android.gm")
                startActivity(Intent.createChooser(intent, "Send mail"))
                true
            }
        }

        return true
    }

}