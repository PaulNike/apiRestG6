package com.codigo.apirest.controller;

import com.codigo.apirest.aggregates.request.PersonaRequest;
import com.codigo.apirest.aggregates.response.PersonaResponse;
import com.codigo.apirest.model.Persona;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/apirest/v1")
public class ApiController {

    /*Inyecciones de Beans*/
    @Value("${apirest.variable.email}")
    private String email;

    @Value("${apirest.variable.edad}")
    private int edad;
    /*EndPoint*/
    @GetMapping("/saludoNormal")
    public ResponseEntity<String> getSaludo(){
     return ResponseEntity.ok("Hola!!!");
    }

    @GetMapping("/saludoPerso/{nombre}")
    public ResponseEntity<String> getSaludoPersonalizado(@PathVariable("nombre") String nombre){
        return ResponseEntity.ok("Hola "+nombre);
    }
    @GetMapping("/saludoPersoQuery")
    public ResponseEntity<String> getSaludoPersonalizadoQuery(@RequestParam("nombre") String nombre){
        return ResponseEntity.ok((String) "Hola "+nombre);
    }
    @PostMapping("/persona")
    public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest personaRequest){
        PersonaResponse personaResponse = new PersonaResponse();
        System.out.println("Persona Request: " + personaRequest.toString());
        personaResponse.setNombre(personaRequest.getNombre());
        personaResponse.setApellidos(personaRequest.getApellidos());
        return ResponseEntity.status(HttpStatus.CREATED).body(personaResponse);
    }

    @GetMapping("/inyecprop")
    public ResponseEntity<Persona> getInyectProp(){
        Persona persona = new Persona();
        persona.setNombre("Paul");
        persona.setApellidos("Rodriguez");
        persona.setEmail(email);
        persona.setEdad(edad);
        return ResponseEntity.ok(persona);
    }

}
