package com.springboot.form.app.controllers;

import com.springboot.form.app.editors.NombreMayusculaEditor;
import com.springboot.form.app.editors.PaisPropertyEditor;
import com.springboot.form.app.editors.RolesEditor;
import com.springboot.form.app.models.Pais;
import com.springboot.form.app.models.Role;
import com.springboot.form.app.models.Usuario;
import com.springboot.form.app.services.PaisService;
import com.springboot.form.app.services.RoleService;
import com.springboot.form.app.validation.UsuarioValidation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("usuario")
public class FormController {

  @Autowired
  private UsuarioValidation validator;

  @Autowired
  private PaisService paisService;

  @Autowired
  private PaisPropertyEditor paisPropertyEditor;

  @Autowired
  private RoleService roleService;

  @Autowired
  private RolesEditor rolesEditor;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, "fechaNacimiento",
        new CustomDateEditor(dateFormat, false));

    binder.registerCustomEditor(String.class, "nombre", new NombreMayusculaEditor());
    binder.registerCustomEditor(String.class, "apellido", new NombreMayusculaEditor());

    binder.registerCustomEditor(Pais.class, "pais", paisPropertyEditor);
    binder.registerCustomEditor(Role.class, "roles", rolesEditor);
  }

  @GetMapping("/form")
  public String form(Model model) {
    Usuario usuario = new Usuario();
    usuario.setNombre("John");
    usuario.setApellido("Doe");
    usuario.setIdentificador("12.456.789-K");
    usuario.setHabilitar(true);
    usuario.setValorSecreto("Algún valor secreto...");
    usuario.setPais(new Pais(1, "ES", "España"));
    usuario.setRoles(Arrays.asList(new Role(1, "Administrador", "ROLE_ADMIN")));
    model.addAttribute("titulo", "Formulario usuarios");
    model.addAttribute("usuario", usuario);
    return "form";
  }

  @PostMapping("/form")
  public String procesar(@Valid Usuario usuario, BindingResult result, Model model) {

    //validator.validate(usuario, result);
    if (result.hasErrors()) {
      model.addAttribute("titulo", "Resultado del formulario");
      return "form";
    }

    return "redirect:/ver";
  }

  @GetMapping("/ver")
  public String ver(@SessionAttribute(name="usuario", required = false) Usuario usuario, Model model, SessionStatus status) {

    if (usuario == null) {
      return "redirect:/form";
    }

    model.addAttribute("titulo", "Resultado del formulario");
    model.addAttribute("usuario", usuario);

    status.setComplete();
    return "resultado";
  }

  @ModelAttribute("listaPaises")
  public List<Pais> listaPaises() {
    return paisService.listar();
  }

  @ModelAttribute("paises")
  public List<String> paises() {
    return Arrays.asList("España", "México", "Chile", "Argentina", "Perú", "Colombia", "Venezuela");
  }

  @ModelAttribute("paisesMap")
  public Map<String, String> paisesMap() {
    Map<String, String> paises = new HashMap<>();
    paises.put("ES", "España");
    paises.put("MX", "México");
    paises.put("CL", "Chile");
    paises.put("AR", "Argentina");
    paises.put("PE", "Perú");
    paises.put("CO", "Colombia");
    paises.put("VE", "Venezuela");

    return paises;
  }

  @ModelAttribute("listaRolesString")
  public List<String> listaRolesString() {
    List<String> roles = new ArrayList<>();
    roles.add("ROLE_ADMIN");
    roles.add("ROLE_USER");
    roles.add("ROLE_MODERATOR");
    return roles;
  }

  @ModelAttribute("listaRolesMap")
  public Map<String, String> listaRolesMap() {
    Map<String, String> roles = new HashMap<>();
    roles.put("ROLE_ADMIN", "Administrador");
    roles.put("ROLE_USER", "Usuario");
    roles.put("ROLE_MODERATOR", "Moderador");

    return roles;
  }

  @ModelAttribute("listaRoles")
  public List<Role> listaRoles() {
    return this.roleService.listar();
  }

  @ModelAttribute("genero")
  public List<String> genero() {
    return Arrays.asList("Hombre", "Mujer");
  }

}
