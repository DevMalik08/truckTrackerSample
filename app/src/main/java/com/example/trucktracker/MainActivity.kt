package com.example.trucktracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.trucktracker.ui.main.MainFragment
import com.example.trucktracker.ui.main.MainViewModel
import com.example.trucktracker.ui.main.MapsFragment


class MainActivity : AppCompatActivity() {

    enum class ViewState{
        LIST_VIEW,
        MAP_VIEW
    }

    private var viewState: ViewState = ViewState.LIST_VIEW
    private lateinit var viewModel: MainViewModel
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        if(viewState == ViewState.LIST_VIEW){
            menu!!.findItem(R.id.list_view).isVisible = false
            menu.findItem(R.id.map_view).isVisible = true
            menu.findItem(R.id.search).isVisible = true
        } else {
            menu!!.findItem(R.id.list_view).isVisible = true
            menu.findItem(R.id.map_view).isVisible = false
            menu.findItem(R.id.search).isVisible = false
        }


        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        // search queryTextChange Listener
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchTruckList(it) }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let { viewModel.searchTruckList(it) }
                return true
            }
        })

        //Expand Collapse listener
        menu?.findItem(R.id.search).setOnActionExpandListener(object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                viewModel.clearSearch()
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }
        })

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if(viewState == ViewState.LIST_VIEW) {
            searchView?.let {
                if (!it.isIconified) {
                    it.onActionViewCollapsed()
                    return
                }
            }
        }

        if(supportFragmentManager.backStackEntryCount == 1){
            viewState = ViewState.LIST_VIEW
            invalidateOptionsMenu()
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_view -> {
                searchView?.let {
                    if (!it.isIconified) {
                        val im = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        try {
                            im.hideSoftInputFromWindow(
                                it.windowToken,
                                0
                            ) // make keyboard hide
                        } catch (e: NullPointerException) {
                        }
                    }
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragment.newInstance()).addToBackStack("map_fragment")
                    .commit()
                viewState = ViewState.MAP_VIEW
                invalidateOptionsMenu()
            }
            R.id.refresh -> {
                searchView?.let {
                    if (!it.isIconified) {
                        it.onActionViewCollapsed()
                    }
                }
                viewModel.updateTruckList()
            }
            R.id.list_view -> {
                /*supportFragmentManager.beginTransaction()
                    .remove(supportFragmentManager.fragments[1]).commitNow()*/
                supportFragmentManager.popBackStack()
                viewState = ViewState.LIST_VIEW
                invalidateOptionsMenu()
            }
            R.id.search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }


}