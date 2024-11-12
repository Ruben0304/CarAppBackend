package com.example.services.dao

import com.example.interfaces.DAOService
import com.example.interfaces.Searchable
import com.example.models.Carro


interface CarroService: DAOService<Carro> , Searchable<Carro>