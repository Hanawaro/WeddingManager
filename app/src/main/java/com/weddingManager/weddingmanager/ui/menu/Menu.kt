package com.weddingManager.weddingmanager.ui.menu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weddingManager.database.models.WeddingModel
import com.weddingManager.repository.Repository
import com.weddingManager.weddingmanager.R
import com.weddingManager.weddingmanager.ui.menu.components.calendarBottomSheet.CalendarBottomSheet
import com.weddingManager.weddingmanager.util.TimeRange
import com.weddingManager.weddingmanager.ui.menu.components.weddingsRecycler.WeddingRecycler
import com.weddingManager.weddingmanager.util.onQueryTextChanged
import kotlinx.android.synthetic.main.fragment_menu.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.abs

class Menu : Fragment(R.layout.fragment_menu) {

    lateinit var viewModel: MenuViewModel

    private lateinit var calendarBottomSheet: CalendarBottomSheet
    private lateinit var weddingRecycler: WeddingRecycler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        activity?.title = "Свадьбы"

        initModelView()
        viewModel.searchQuery.value = MenuViewModel.SearchUnit("", TimeRange())

        calendarBottomSheet = CalendarBottomSheet(requireContext(), viewModel.searchQuery)
        weddingRecycler = WeddingRecycler(requireContext(), parentFragmentManager, recycler_view_weddings)

        viewModel.weddings.observe(viewLifecycleOwner, Observer<List<WeddingModel>> { list ->
            weddingRecycler.submitList(list)
        })

        viewModel.allWeddings.observe(viewLifecycleOwner, Observer<List<WeddingModel>> { list ->
            calendarBottomSheet.filter(list)
            calendarBottomSheet.invalidate()
        })

        fab_add_wedding.setOnClickListener {
            val action = MenuDirections.actionMenuToWeddingEditor(null)
            findNavController().navigate(action)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)

        val search = menu.findItem(R.id.action_search).actionView as SearchView

        search.onQueryTextChanged {
            viewModel.searchQuery.value = MenuViewModel.SearchUnit(it, viewModel.searchQuery.value.time)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_search -> {
                true
            }
            R.id.action_sort -> {
                calendarBottomSheet.show()
                true
            }
            R.id.action_components -> {
                val current: Calendar = Calendar.getInstance().apply {
                    time = Date(System.currentTimeMillis())
                    set(Calendar.HOUR, 3)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                current.set(Calendar.DAY_OF_MONTH, abs(Random().nextInt()) % 28 + 1)

                val image = if (current.get(Calendar.DAY_OF_MONTH) % 2 == 0) {
                    val bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.profile);
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                    stream.toByteArray()
                } else {
                    ByteArray(0)
                }

                Repository.Wedding?.insert(requireContext(), WeddingModel(
                    "Имя мужа_${abs(Random().nextInt()) % 10}",
                    "Имя жены_${abs(Random().nextInt()) % 10}",
                    image,
                    current.timeInMillis / 1000L,
                    0, 0
                ))
                true
            }
            R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun initModelView() {
        viewModel = ViewModelProvider(this, object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MenuViewModel(Repository.getDatabase(requireContext()).getWeddingDAO()) as T
            }
        }).get(MenuViewModel::class.java)
    }

}