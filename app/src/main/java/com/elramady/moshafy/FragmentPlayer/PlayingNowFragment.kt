package com.elramady.moshafy.FragmentPlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.FragmentPlayingNowBinding
import com.elramady.moshafy.ui.ReciationsActivity.Companion.RECITER_TO_FRAG
import com.elramady.moshafy.ui.ReciationsActivity.Companion.SHOW_MINI_PLAYER
import com.elramady.moshafy.ui.ReciationsActivity.Companion.SURAH_TO_FRAG


class PlayingNowFragment : Fragment() {

lateinit var bindinfFragment:FragmentPlayingNowBinding
lateinit var actionPlaying: ActionPlaying

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        bindinfFragment=DataBindingUtil.inflate(inflater,R.layout.fragment_playing_now, container, false)

        bindinfFragment.bottomPlayPause.setOnClickListener {
//actionPlaying.playPauseBtnClick()
        }
        bindinfFragment.bottomImageNext.setOnClickListener {
//            actionPlaying.nextBtnClick()
        }

        return bindinfFragment.root
    }

    override fun onResume() {
        super.onResume()
        if (SHOW_MINI_PLAYER){

                bindinfFragment.bottomReciaterName.text= RECITER_TO_FRAG
                bindinfFragment.bottomSurahName.text= SURAH_TO_FRAG

        }
    }

   fun setActionFrag(actionPlaying: ActionPlaying){
       this.actionPlaying=actionPlaying
   }

}