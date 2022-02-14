package com.springboot.form.app.services;

import com.springboot.form.app.models.Pais;
import java.util.List;

public interface PaisService {
  
  List<Pais> listar();
  Pais obtenerPorId(Integer id);

}
