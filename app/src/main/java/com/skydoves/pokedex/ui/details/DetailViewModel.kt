/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.pokedex.ui.details

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skydoves.bindables.bindingProperty
import com.skydoves.pokedex.base.LiveCoroutinesViewModel
import com.skydoves.pokedex.model.PokemonInfo
import com.skydoves.pokedex.repository.DetailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
  detailRepository: DetailRepository,
  @Assisted private val pokemonName: String
) : LiveCoroutinesViewModel() {

  val pokemonInfoLiveData: LiveData<PokemonInfo?>

  @get:Bindable
  var toastMessage: String? by bindingProperty(null)
    private set

  @get:Bindable
  var isLoading: Boolean by bindingProperty(true)
    private set

  init {
    Timber.d("init DetailViewModel")

    pokemonInfoLiveData = detailRepository.fetchPokemonInfo(
      name = pokemonName,
      onComplete = { isLoading = false },
      onError = { toastMessage = it }
    ).asLiveDataOnViewModelScope()
  }

  @dagger.assisted.AssistedFactory
  interface AssistedFactory {
    fun create(pokemonName: String): DetailViewModel
  }

  companion object {
    fun provideFactory(
      assistedFactory: AssistedFactory,
      pokemonName: String
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return assistedFactory.create(pokemonName) as T
      }
    }
  }
}
