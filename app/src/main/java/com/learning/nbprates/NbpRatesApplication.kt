package com.learning.nbprates

import android.app.Application
import com.learning.nbprates.domain.Repository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NbpRatesApplication: Application() {

    @Inject
    lateinit var repository: Repository
}