package com.sibela.datastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.sibela.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        saveButton.setOnClickListener { onSaveClicked() }
        readButton.setOnClickListener { onReadClicked() }
        deleteItemButton.setOnClickListener { onDeleteClicked() }
        deleteAllButton.setOnClickListener { onDeleteAllClicked() }
    }

    private fun onSaveClicked() = lifecycleScope.launch {
        val key = stringPreferencesKey(binding.keyInput.text.toString())
        dataStore.edit { settings ->
            settings[key] = binding.valueInput.text.toString()
        }
    }

    private fun onReadClicked() = lifecycleScope.launch {
        val key = stringPreferencesKey(binding.keyInput.text.toString())
        binding.textView.text = dataStore.data.first()[key]
    }

    private fun onDeleteClicked() = lifecycleScope.launch {
        val key = stringPreferencesKey(binding.keyInput.text.toString())
        dataStore.edit { settings ->
            settings.remove(key)
        }
    }

    private fun onDeleteAllClicked() = lifecycleScope.launch {
        dataStore.edit { settings ->
            settings.clear()
        }
    }
}