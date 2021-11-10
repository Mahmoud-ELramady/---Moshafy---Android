package com.elramady.moshafy.ui.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.elramady.moshafy.Adapters.AzkarAdapter
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.FragmentAzkarWritingBinding


class AzkarWritingFragment : Fragment() {

    lateinit var binding:FragmentAzkarWritingBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_azkar_writing, container, false)

        val array= arrayListOf<String>(" (سورة الاخلاص) 3 مرات\n (سورة الناس) 3 مرات \n (سورة الفلق) 3 مرات "
        ,"أية الكرسى (البقره 255 ) مره واحده","بسم الله الذى لا يضر مع اسمه شئ فى الارض ولا فى السماء وهو السميع العليم (3 مرات ) "
        ,"اللهم بك اصبحنا (أمسينا) وبك أمسينا(أصبحنا) وبك نحيا ونموت واليك النشور(المصير)... (مره واحده )"
        ,"أصبحنا وصبح (أمسينا وأمسى) الملك لله والحمد لله لا اله الا الله وحده لا شريك له وله الحمد وهو على كل شئ قدير رب أسألك خير ما فى هذا اليوم وشر ما بعده رب اعوذ بك من الكسل وسوء الكبر رب اعوذ بك من عذاب فى النار وعذاب فى القبر"
        ,"أصبحنا (أمسينا) على فطرة الإسلام وكلمة الإخلاص وعلى دين نبينا محمد (ص) وعلى ملة أبينا إبراهيم حنيفا مسلما وما كان من المشركين"
        ,"أصبحنا وأصبح (أمسينا وأمسى) الملك لله رب العالمين اللهم انى  أسألك خير فى هذا اليوم فتحه ونصره ونوره وأعوذ بك من شر ما فيه وشر ما بعده"
        ,"اللهم إنى أصبحت (أمسيت) أشهدك وأشهد حملة عرشك وملائكتك وجميع خلقك أنك أنت الله لا إله إلا أنت وحدك لا شريك لك وأن محمد عبدك ورسولك (4 مرات)"
        ,"رضيت بالله ربا وبالإسلام دينا وبمحمد (ص) نبيا ورسولا (3 مرات)"
        ,"اللهم إنى أسألك العافيه فى الدنيا والأخرة اللهم إنى أسألك العفو والعافيه فى دينى ودنياى وأهلى ومالى اللهم استر عوراتى وأمن روعاتى اللهم احفظنى من بين يدى ومن خلفى وعن يمينى وعن شمالى ومن فوقى وأعوذ بعظمتك أن أغتال من تحتى"
        ,"اللهم عالم الغيب والشهادة فاطر السماوات والأرض رب كل شئ ومليكه أشهد أن لا إله إلا أنت أعوذ بك من شر نفسى ومن شر الشيطان وشركه وأن أقترف على نفسى سوءا أو أجره إلى مسلم"
        ,"سبحان الله وبحمده عدد خلقه ورضا نفسه وزنة عرشه ومداد كلماته (3 مرات)"
        ,"اللهم أنت ربى لا إله إالا أنت خلقتنى وأنا عبدك وأنا على عهدك ووعدك ما أستطعت أعوذ بك من شر ما صنعت وأبوء لك بنعمتك على وأبوء بذنبى فاغفر لى فإنه لا يغفر الذنوب الا أنت"
        ,"أعوذ بكلمات الله التامات من شر ما خلق (3 مرات)"
        ,"اللهم عافنى فى بدنى اللهم عافنى فى سمعى اللهم عافنى فى بصرى لا إله إلا أنت إنى أعوذ بك من الكفر والفقر وأعوذ بك من عذاب القبر لا إله إلا أنت ( 3 مرات)"
        ,"يا حى يا قيوم برحمتك أستغيذ أصلح لى شأنى كله ولا تكلنى إلى نفسى طرفة عين ( 3 مرات)"
        ,"لا إلا الله وحده لا شريك له له الملك وله الحمد وهو على كل شئ قدير (مية مره)"
        ,"سبحان الله وبحمد (مية مره)"
        ,"اللهم صلى وسلم على نبينا محمد (عشر مرات)"
        )

        val adpter= AzkarAdapter(context!!)
        binding.azkarListWriting.layoutManager= LinearLayoutManager(context)
        binding.azkarListWriting.adapter=adpter

        adpter.setList(array)




        return binding.root
    }


}