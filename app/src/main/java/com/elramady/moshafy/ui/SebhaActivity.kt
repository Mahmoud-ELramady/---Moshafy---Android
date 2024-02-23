package com.elramady.moshafy.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivitySebhaBinding
import com.elramady.moshafy.databinding.DialogAddSebhaBinding
import com.elramady.moshafy.repo.SebhaRepository
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.SebhaViewModel
import com.elramady.moshafy.vo.sebha.SebhaData


class SebhaActivity : AppCompatActivity() {

    lateinit var binding:ActivitySebhaBinding


    private lateinit var dialogAddSebha: AlertDialog
    private var _bindingAddSebhaDialog: DialogAddSebhaBinding? = null

    private val bindingAddSebhaDialog get() = _bindingAddSebhaDialog!!

    lateinit var viewModel:SebhaViewModel
    lateinit var repo:SebhaRepository
    lateinit var db:DataBase

    val arraySebha= arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySebhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAddSebhaDialog()

        db= DataBase.getInstance(this)
        repo= SebhaRepository(db)
        viewModel=getSebhaViewModel()




        binding.imageCircleSebha.setOnClickListener {
            Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show()
        }

        binding.fabAddSebha.setOnClickListener {
            dialogAddSebha.show()
        }

        whenShowingDialogAddingSebha()
        getAllSebha()

    }

    private fun getAllSebha() {
      //  viewModel.getAllSebha()
        viewModel.getAllSebha().observe(this, Observer {

            Log.e("dataSebha",it.toString())
            arraySebha.clear()
            if (it!=null){
                for (i in it){
                    arraySebha.add(i.title.toString())
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this,
                    R.layout.item_spanner,
                    arraySebha
                ) // creates new ArrayAdapter with custom textviews for all elements including the first one (which is what we want)

                 adapter.setDropDownViewResource(R.layout.item_spanner2) // Modifies all the elements when you click on the dropdown
                binding.spinnerSebha.setAdapter(adapter)
            }

        })
    }

    private fun whenShowingDialogAddingSebha() {
        bindingAddSebhaDialog.btnAddSebha.setOnClickListener {
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