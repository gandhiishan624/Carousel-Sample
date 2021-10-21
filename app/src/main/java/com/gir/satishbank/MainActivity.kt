package com.gir.satishbank

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.LinearLayoutManager
import com.gir.satishbank.adapter.DataListAdapter
import com.gir.satishbank.adapter.DotIndicatorAdapter
import com.gir.satishbank.databinding.ActivityMainBinding
import com.gir.satishbank.model.DataList
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    private lateinit var dataList: MutableList<DataList>
    private lateinit var binding: ActivityMainBinding
    private val dataAdapter by lazy {
            DataListAdapter(this)
    }

    private val dotIndicatorAdapter by lazy {
        DotIndicatorAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readJsonData()
        initViewData()

    }

    private fun initViewData() {

        binding.amEtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                dataAdapter.filter.filter(text)

            }
        })



        initDotIndicatorData()
        initImageBanner()
        initListData()

    }

    private fun initDotIndicatorData() {

        binding.amRvIndicator.apply {
            layoutManager=LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter=dotIndicatorAdapter
        }

    }

    private fun initListData() {

        binding.amRvList.apply {
            layoutManager=LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter=dataAdapter
        }

    }

    private fun readJsonData()
    {
        val fileInString: String =
            applicationContext.assets.open("data.json").bufferedReader().use { it.readText() }

        val gson=Gson()
        val listType: Type = object : TypeToken<ArrayList<DataList?>?>() {}.type
        dataList=gson.fromJson(fileInString, listType)
    }

    private fun initImageBanner() {

        binding.amCrView.setAdapter(object : Carousel.Adapter {
            /**
             * Number of items you want to display in the Carousel
             * @return number of items
             */
            override fun count(): Int {
                dotIndicatorAdapter.bannerCount=dataList.size
                return dataList.size
            }

            /**
             * Callback to populate the view for the given index
             *
             * @param view
             * @param index
             */
            override fun populate(view: View?, index: Int) {
                if (dataList.isNullOrEmpty())
                    return

                dotIndicatorAdapter.notifyItemChanged(index)
                if (view is ImageView) {
                    val dataValue = dataList[index]
                    Picasso.get().load(dataValue.banner).into(view)
                    // view.setImageResource(images.get(index))
                }


            }

            /**
             * Callback when we reach a new index
             * @param index
             */
            override fun onNewItem(index: Int) {
                dotIndicatorAdapter.indexSelected=index
                dotIndicatorAdapter.notifyItemChanged(index)
                refreshProductData(index)
                binding.amEtSearch.setText("")


            }

        })

        refreshProductData(0)
        binding.amEtSearch.setText("")
    }

    private fun refreshProductData(index: Int) {
        val productList=dataList[index].list
//        dataAdapter.
        dataAdapter.productList=productList
    }


}