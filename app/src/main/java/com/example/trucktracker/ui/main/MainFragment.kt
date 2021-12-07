package com.example.trucktracker.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trucktracker.R
import com.example.trucktracker.data.Truck

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var truckListView: RecyclerView
    private var truckList: ArrayList<Truck>? = null
    private var adapter: TruckListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false).also {
            truckListView = it.findViewById(R.id.truck_list_view)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        // TODO: Use the ViewModel
        viewModel.getTruckList().observe(viewLifecycleOwner){ trucks ->

            if(trucks == null){
                return@observe
            }
            truckList = trucks as ArrayList
            if (adapter == null) {
                truckListView.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    adapter = TruckListAdapter(trucks)
                }
            } else {
                adapter!!.updateList(trucks)
                adapter!!.notifyDataSetChanged()
            }

        }
    }

}