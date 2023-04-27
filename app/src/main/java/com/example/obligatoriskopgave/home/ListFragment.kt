package com.example.obligatoriskopgave.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.obligatoriskopgave.R
import com.example.obligatoriskopgave.databinding.FragmentListBinding
import com.example.obligatoriskopgave.salesItems.ItemsRepository
import com.google.firebase.auth.FirebaseAuth

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var itemsRepository: ItemsRepository
    private lateinit var itemsAdapter: ItemsAdapter
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the repository and adapter
        itemsRepository = ItemsRepository()
        itemsAdapter = ItemsAdapter()

        // Set up the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemsAdapter

        // Call getItems() and observe the itemsLiveData
        itemsRepository.getItems()
        itemsRepository.itemsLiveData.observe(viewLifecycleOwner) { itemsList ->
            // Update your UI with the items list here
            Log.d("ITEMS_LIST", itemsList.toString())

            // Set the items list in the adapter
            itemsAdapter.setItemsList(itemsList)
        }

        // Set the initial items list in the adapter
        val initialItemsList = itemsRepository.itemsLiveData.value
        initialItemsList?.let { itemsAdapter.setItemsList(it) }

        binding.sortButton.setOnClickListener {
            Log.d("APPLE", "Sorting")

            when (binding.sortSpinner.selectedItemPosition) {
                0 -> itemsRepository.sortByTitleAscending()
                1 -> itemsRepository.sortByTitleDescending()
                2 -> itemsRepository.sortByPrice()
                3 -> itemsRepository.sortByPriceDescending()
            }

            // Get the sorted items list and set it in the adapter
            val sortedItemsList = itemsRepository.itemsLiveData.value
            sortedItemsList?.let { it1 -> itemsAdapter.setItemsList(it1) }
        }

        binding.filterButton.setOnClickListener {
            val filterOption = binding.filterSpinner.selectedItemPosition

            when (filterOption) {
                0 -> {
                    val description = binding.editTextFilter.text.toString().trim()
                    itemsRepository.filterByDescription(description)
                }
                1 -> {
                    val sellerEmail = binding.editTextFilter.text.toString().trim()
                    itemsRepository.filterBySeller(sellerEmail)
                }
                2 -> {
                    val priceString = binding.editTextFilter.text.toString().trim()
                    itemsRepository.filterByMaxPrice(priceString.toIntOrNull())
                }
                3 -> {
                    val priceString = binding.editTextFilter.text.toString().trim()
                    itemsRepository.filterByMinPrice(priceString.toIntOrNull())

                }
            }

            // Get the filtered items list and set it in the adapter
            val filteredItemsList = itemsRepository.itemsLiveData.value
            filteredItemsList?.let { it1 -> itemsAdapter.setItemsList(it1) }
        }


        setHasOptionsMenu(true)

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh the list of items here
            itemsRepository.getItems()
            swipeRefreshLayout.isRefreshing = false
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("APPLE", "logout try")
        when (item.itemId) {
            R.id.action_logout -> {
                // Log the user out
                Log.d("APPLE", "User is logging out")
                FirebaseAuth.getInstance().signOut()

                // Navigate back to the login screen
                findNavController().navigate(R.id.firstFragment)

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

