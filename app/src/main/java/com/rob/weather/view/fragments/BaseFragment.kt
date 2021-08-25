package com.rob.weather.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//abstract class BaseFragment<B : ViewBinding> : Fragment() {
//    val binding: B by lazy { inflate(layoutInflater) }
//
//    abstract fun inflate(inflater: LayoutInflater): B
//
//    final override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View = binding.root
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }
//}
//
//private var _binding: ResultProfileBinding? = null
//// This property is only valid between onCreateView and
//// onDestroyView.
//private val binding get() = _binding!!
//
//override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//): View? {
//    _binding = ResultProfileBinding.inflate(inflater, container, false)
//    val view = binding.root
//    return view
//}
//
//override fun onDestroyView() {
//    super.onDestroyView()
//    _binding = null
//}