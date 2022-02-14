package com.springboot.form.app.services;

import com.springboot.form.app.models.Role;
import java.util.List;

public interface RoleService {

  List<Role> listar();
  Role obtenerPorId(Integer id);

}
