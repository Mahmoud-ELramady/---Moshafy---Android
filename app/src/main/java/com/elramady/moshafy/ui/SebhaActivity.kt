package com.elramady.moshafy.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivitySebhaBinding
import com.elramady.moshafy.databinding.DialogAddSebhaBinding
import com.elramady.moshafy.databinding.DialogDeleteSumSebhaBinding
import com.elramady.moshafy.repo.SebhaRepository
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.SebhaViewModel
import com.elramady.moshafy.vo.sebha.SebhaData


class SebhaActivity : AppCompatActivity() {

    lateinit var binding:ActivitySebhaBinding


    private lateinit var dialogAddSebha: AlertDialog
    private var _bindingAddSebhaDialog: DialogAddSebhaBinding? = null

    private val bindingAddSebhaDialog get() = _bindingAddSebhaDialog!!


    private lateinit var dialogSure: AlertDialog

    private var _bindingDeleteSumSebhaDialog: DialogDeleteSumSebhaBinding? = null

    private val bindingDeleteSumSebhaDialog get() = _bindingDeleteSumSebhaDialog!!


    lateinit var viewModel:SebhaViewModel
    lateinit var repo:SebhaRepository
    lateinit var db:DataBase

    var arraySebhaSpinner= arrayListOf<String>()
    var allSebhaList= arrayListOf<SebhaData>()
    lateinit var adapter: ArrayAdapter<String>

     var itemSebhaData: SebhaData?=null
    var numberOfCountSebhaItem=0

    var isCount=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySebhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAddSebhaDialog()

        initSureDialog()

        db= DataBase.getInstance(this)
        repo= SebhaRepository(db)
        viewModel=getSebhaViewModel()






        binding.fabAddSebha.setOnClickListener {
            dialogAddSebha.show()
        }

        whenShowingDialogAddingSebha()
        initSpinnerAdapter()
        getAllSebha()
        getCountAllSebha()
        spinnerListnerOnSelectItem()

        onClickCountSebha()
        onClickDeleteSebha()
        onClickDeleteCountSebha()
        onClickDeleteSum()

    }



    private fun onClickDeleteSebha() {
        binding.fabDeleteSebha.setOnClickListener {


                if (itemSebhaData!=null){
                    Log.e("onClickkk","2")
                    dialogSure.show()
                    bindingDeleteSumSebhaDialog.dialogDeleteTitle.setText(resources.getString(R.string.are_you_sure_to_delete_sebha))

                    bindingDeleteSumSebhaDialog.dialogDeleteBtn2.setOnClickListener {
                        isCount=false //to update allSebhaList and arraySebhaSpinner
                        viewModel.deleteSebha(itemSebhaData?.id)
                        dialogSure.dismiss()
                        Toast.makeText(this,"تم مسح السبحة",Toast.LENGTH_SHORT).show()

                    }

                }



        }
    }

    private fun onClickDeleteCountSebha() {

        binding.fabDeleteSebhaCount.setOnClickListener {
            if (itemSebhaData!=null){
                Log.e("onClickkk","3")
                isCount=true   //to update allSebhaList only
                numberOfCountSebhaItem=0
                binding.textCountSebha.text=numberOfCountSebhaItem.toString()

                viewModel.deleteCountOfSebha(itemSebhaData?.id)
            }

        }
    }

    private fun onClickCountSebha() {
        binding.imageCircleSebha.setOnClickListener {
            if (itemSebhaData!=null){
                Log.e("onClickkk","1")

                isCount=true   //to update allSebhaList only
                numberOfCountSebhaItem++
                viewModel.updateSebha(numberOfCountSebhaItem,itemSebhaData?.id)
                binding.textCountSebha.text=numberOfCountSebhaItem.toString()

                if (numberOfCountSebhaItem==1){
                    binding.tvTitleSebha.text=itemSebhaData?.title.toString()
                }



            }


        }
    }

    private fun onClickDeleteSum() {
        binding.btnSetZero.setOnClickListener {
            if (allSebhaList.isNotEmpty()){
                Log.e("onClickkk","4")
                dialogSure.show()
                bindingDeleteSumSebhaDialog.dialogDeleteTitle.setText(resources.getString(R.string.are_you_sure_to_set_sum_zero))

                bindingDeleteSumSebhaDialog.dialogDeleteBtn2.setOnClickListener {
                    isCount=true   //to update allSebhaList only
                    viewModel.deleteAllCountOfSebha()
                    dialogSure.dismiss()
                    Toast.makeText(this,"تم تصفير كل العدادات",Toast.LENGTH_SHORT).show()

                }

            }

        }
    }

    private fun initSpinnerAdapter() {
         adapter = ArrayAdapter<String>(
            this,
            R.layout.item_spanner,
            arraySebhaSpinner
        )

        adapter.setDropDownViewResource(R.layout.item_spanner2)
    }



    private fun spinnerListnerOnSelectItem() {

        binding.spinnerSebha.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (allSebhaList.isNotEmpty()){
                    itemSebhaData=allSebhaList[position]
                    bindDataInUi()
                 //   Toast.makeText(this@SebhaActivity,itemSebhaData.toString(),Toast.LENGTH_SHORT).show()
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun bindDataInUi() {
        Log.e("bindUIIIII","this")
         itemSebhaData?.numberOfSebha?.let {
             numberOfCountSebhaItem=it
         }

        binding.textCountSebha.text=numberOfCountSebhaItem.toString()


        if (numberOfCountSebhaItem==0){
            binding.tvTitleSebha.text= getString(R.string.start_sebha)
        }else{
            binding.tvTitleSebha.text=itemSebhaData?.title.toString()

        }

    }


   private fun getCountAllSebha(){
       viewModel.getSumNumberOfAllSebha().observe(this, Observer {
           if (it!=null){
               binding.tvTotalCountSebha.text=it.toString()
               if (it==0){
                   binding.textCountSebha.text="0"
                   binding.tvTitleSebha.text= getString(R.string.start_sebha)
               }
           }else{
               binding.tvTotalCountSebha.text="0"

           }
       })
   }

    private fun getAllSebha() {
        viewModel.getAllSebha().observe(this, Observer {listSebhaData->

            Log.e("dataSebha",listSebhaData.toString())


            if (listSebhaData?.isNotEmpty() == true){
                Log.e("dataSebha","1")

                if (!isCount){
                    allSebhaList.clear()
                    arraySebhaSpinner.clear()
                    allSebhaList=listSebhaData as ArrayList<SebhaData>

                    for (i in listSebhaData){
                        arraySebhaSpinner.add(i.title.toString())
                    }
                    Log.e("dataSebha","2")


                    binding.spinnerSebha.adapter = adapter



                }else{
                    allSebhaList.clear()
                    allSebhaList=listSebhaData as ArrayList<SebhaData>
                }



            }else{
                arraySebhaSpinner.clear()
                allSebhaList.clear()
                itemSebhaData=null
                Log.e("dataSebha","3")
                arraySebhaSpinner.add("اختار جملة التسبيح")
                binding.spinnerSebha.adapter = adapter
                binding.textCountSebha.text="0"
                binding.tvTitleSebha.text= getString(R.string.add_sebha_to_start_sebha)


            }

        })
    }

    private fun whenShowingDialogAddingSebha() {
        bindingAddSebhaDialog.btnAddSebha.setOnClickListener {

         isCount=false

         val titleSebha= bindingAddSebhaDialog.dialogEdSebhaContent.text.toString()


            viewModel.insertSebha(SebhaData(title =titleSebha, numberOfSebha = 0))

            Toast.makeText(this,"تمت اضافة السبحة",Toast.LENGTH_SHORT).show()
            bindingAddSebhaDialog.dialogEdSebhaContent.setText("")
     //       bindingAddSebhaDialog.dialogEdSebhaContent.isFocusable=false
            dialogAddSebha.dismiss()

        }


    }

    private fun initAddSebhaDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        _bindingAddSebhaDialog = DialogAddSebhaBinding.inflate(inflater)
        builder.setView(bindingAddSebhaDialog.root)

        dialogAddSebha = builder.create()
        dialogAddSebha.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogAddSebha.requestWindowFeature(Window.FEATURE_NO_TITLE)

    }

    private fun initSureDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        _bindingDeleteSumSebhaDialog = DialogDeleteSumSebhaBinding.inflate(inflater)
        builder.setView(bindingDeleteSumSebhaDialog.root)


        dialogSure = builder.create()


         dialogSure.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          dialogSure.requestWindowFeature(Window.FEATURE_NO_TITLE)


        bindingDeleteSumSebhaDialog.dialogDeleteBtn1.setOnClickListener {
            dialogSure.dismiss()
        }


    }





    private fun getSebhaViewModel(): SebhaViewModel {
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SebhaViewModel::class.java)){
                    return SebhaViewModel(repo) as T
                }
                throw IllegalArgumentException("Unknown View Model  Class")

            }
        })[SebhaViewModel::class.java]


    }
}