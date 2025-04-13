package com.example.reto2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.reto2.dao.InvitadoDAO
import com.example.reto2.databinding.ActivitySecondBinding
import com.example.reto2.model.Invitado
import com.google.android.material.snackbar.Snackbar

class SecondActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //deshabilitar que la app cambie el nombre automáticamente
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //establecer el nombre que aparecerá an la barra
        binding.toolbar.title = "Reto 2. ADD CRUD - Yuliia TB"

        binding.btnEditarInvitado.setOnClickListener(this)
        binding.btnLimpiar.setOnClickListener(this)
        binding.btnEliminar.setOnClickListener(this)
        binding.btnMostrarTodo.setOnClickListener(this)
        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnMostrarTodo.id -> {
                val invitadoDAO = InvitadoDAO(applicationContext)
                if (invitadoDAO.getInvitados().isNotEmpty()) {
                    binding.textoMostrar.text = invitadoDAO.getInvitados().toString()
                } else {
                    Snackbar.make(binding.root, "La lista está vacía.", Snackbar.LENGTH_SHORT).show()
                }
            }
            binding.btnEditarInvitado.id -> {
                val invitadoDAO = InvitadoDAO(applicationContext)
                val nombreInsertado = binding.insertName.text.toString()

                if (binding.insertName.text.isNotEmpty()) {

                    //comprobar si este nombre existe
                    if (invitadoDAO.nombreExiste(nombreInsertado)) {
                        binding.invitadoSeleccionado.text = "Seleccionaste ${nombreInsertado}"
                        binding.insertName.text.clear()
                        binding.insertName.hint = "Modifica el nombre" //cambiar el hint para mejor UX

                        //el botón cambia el texto para guardar los cambios
                        binding.btnEditarInvitado.text = "Guadar"

                        binding.btnEditarInvitado.setOnClickListener {
                            val nuevoNombre = binding.insertName.text.toString()
                            if (nuevoNombre.isNotEmpty()) {
                                invitadoDAO.editarInvitados(nombreInsertado, nuevoNombre)
                                binding.textoMostrar.text = invitadoDAO.getInvitados()
                                Snackbar.make(binding.root, "El nombre se ha modificado correctamente.", Snackbar.LENGTH_SHORT).show()
                                binding.insertName.text.clear()
                                binding.btnEditarInvitado.text = "Editar"
                                binding.insertName.hint = "Escribe un nombre"
                                binding.invitadoSeleccionado.text = ""
                            } else {
                                Snackbar.make(binding.root, "Debes escribir un nombre.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        binding.textoMostrar.text = invitadoDAO.getInvitados()
                    } else {
                        Snackbar.make(binding.root, "El invitado ${binding.insertName.text} no existe.", Snackbar.LENGTH_SHORT).show()
                        binding.btnEditarInvitado.text = "Editar"
                        binding.insertName.text.clear()
                    }
                }

            }
            binding.btnEliminar.id -> {
                val invitadoDAO = InvitadoDAO(applicationContext)

                if (binding.insertName.text.isNotEmpty()){
                    if (invitadoDAO.nombreExiste(binding.insertName.text.toString())) {
                        invitadoDAO.eliminarInvitado(Invitado(binding.insertName.text.toString()))
                        binding.textoMostrar.text = invitadoDAO.getInvitados()
                        Snackbar.make(binding.root, "El invitado ${binding.insertName.text} ha sido eliminado.", Snackbar.LENGTH_SHORT).show()
                        binding.insertName.text.clear()
                    } else {
                        Snackbar.make(binding.root, "El invitado ${binding.insertName.text} no existe.", Snackbar.LENGTH_SHORT).show()
                        binding.insertName.text.clear()
                    }
                }
                else {
                    Snackbar.make(binding.root, "Debes escribir un nombre.", Snackbar.LENGTH_SHORT).show()
                }
            }
            binding.btnLimpiar.id -> {
                binding.insertName.text.clear()
            }
        }
    }

}