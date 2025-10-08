package com.learning.nbprates

import android.app.Application
import com.learning.nbprates.domain.Repository

class NbpRatesApplication: Application() {
    val repository: Repository by lazy { Repository() }
}