package com.ubayadev.studentproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubayadev.studentproject.R
import com.ubayadev.studentproject.databinding.FragmentStudentListBinding
import com.ubayadev.studentproject.viewmodel.ListViewModel


class StudentListFragment : Fragment() {
    private lateinit var binding: FragmentStudentListBinding
    private lateinit var viewmodel: ListViewModel
    private val studentListAdapter = StudentListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init the vm
        viewmodel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewmodel.refresh() // load/fetch data

        // testing file
        //viewmodel.testSaveFile()

        // setup recycle view
        binding.recViewStudent.layoutManager = LinearLayoutManager(context)
        binding.recViewStudent.adapter = studentListAdapter

        // swipe refresh
        binding.refreshLayout.setOnRefreshListener {
            viewmodel.refresh()
            binding.refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel() {
        // observe - live data - arraylist student
        viewmodel.studentsLD.observe(viewLifecycleOwner, Observer {
            studentListAdapter.updateStudentList(it)
        })

        // observe - live data - loadingLD
        viewmodel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                // still loading
                binding.progressLoad.visibility = View.VISIBLE
                binding.recViewStudent.visibility = View.INVISIBLE
            } else {
                // sudah ga loading
                binding.progressLoad.visibility = View.INVISIBLE
                binding.recViewStudent.visibility = View.VISIBLE
            }
        })

        // observe - live data - errorLD
        viewmodel.errorLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                // ada error
                binding.txtError.text = "Something wrong when loading student data"
                binding.txtError.visibility = View.VISIBLE
                binding.recViewStudent.visibility = View.INVISIBLE
            } else {
                // tidak ada error
                binding.txtError.visibility = View.INVISIBLE
                binding.recViewStudent.visibility = View.VISIBLE
            }
        })
    }
}