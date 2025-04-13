package com.example.reto2

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.reto2.dao.InvitadoDAO
import com.example.reto2.databinding.ActivityMainBinding
import com.example.reto2.model.Invitado

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //deshabilitar que la app cambie el nombre automáticamente
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //establecer el nombre que aparecerá an la barra
        binding.toolbar.title = "Reto 2. Yuliia Teterko Bobrytska"

        binding.fab.setOnClickListener(this)
        binding.btnAnadir.setOnClickListener(this)
        binding.btnMostrarTodo.setOnClickListener(this)
        binding.btnLimpiar.setOnClickListener(this)
        binding.btnEditar.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnAnadir.id -> {
                val invitadoDAO = InvitadoDAO(applicationContext)

                if (binding.insertName.text.isNotEmpty()) {
                    //comprobar si este nombre ya existe
                    if (invitadoDAO.nombreExiste(binding.insertName.text.toString())) {
                        Snackbar.make(binding.root, "El nombre ya existe. Añade una inicial para diferenciarlo de los demás.", Snackbar.LENGTH_SHORT).show()
                    } else {
                        invitadoDAO.insertInvitado(Invitado(binding.insertName.text.toString()))
                        binding.textoMostrar.text = invitadoDAO.getInvitados()
                    }
                }
                else {
                    Snackbar.make(binding.root, "Debes escribir un nombre.", Snackbar.LENGTH_SHORT).show()
                }
                binding.insertName.text.clear()
            }
            binding.btnMostrarTodo.id -> {
                val invitadoDAO = InvitadoDAO(applicationContext)
                if (invitadoDAO.getInvitados().isNotEmpty()) {
                    binding.textoMostrar.text = invitadoDAO.getInvitados().toString()
                } else {
                    Snackbar.make(binding.root, "La lista está vacía.", Snackbar.LENGTH_SHORT).show()
                }
            }
            binding.fab.id -> {
                binding.textoMostrar.text = ""
            }
            binding.btnLimpiar.id -> {
                binding.insertName.text.clear()
            }
        }
    }
}