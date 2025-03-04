package com.example.localscurestorage.DI

import com.example.localscurestorage.viewModle.FileDataViewModle
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val databaseModule = module {
    viewModel { FileDataViewModle() }
}
