package com.codigo.apirest.controller;

import com.codigo.apirest.aggregates.request.PersonaRequest;
import com.codigo.apirest.aggregates.response.PersonaResponse;
import com.codigo.apirest.model.Persona;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


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

    /*Usando interfaces funcionales*/

    //Caso 1 -> Mayores de 25
    @GetMapping("/filter")
    public ResponseEntity<List<Persona>> getPersonas(){
        //Interface Funcional Stream
        return ResponseEntity.ok(getListPersonas().stream()
                //Operador Intermedio Filter
                .filter(persona -> persona.getEdad() > 25)
                //Pasar el resultado a una lista deseada.
                .toList());
    }
    @GetMapping("/sorted")
    public ResponseEntity<List<Persona>> getPersonasComparing(){
        //Interface Funcional Stream
        return ResponseEntity.ok(getListPersonas().stream()
                //Operador Intermedio Comparator
                .sorted(Comparator.comparingInt(Persona::getEdad).reversed())
                //Limitar la respuesta a los 3 primeros
                        //.limit(3)
                .toList());
    }
    @GetMapping("/tomap")
    public ResponseEntity<Map<String,Integer>> getPersonasMap(){
        //Interface Funcional Stream
        return ResponseEntity.ok(getListPersonas().stream()
                //collect -> Pertence a la interface Stream
                .collect(Collectors.toMap(Persona::getNombre, Persona::getEdad)));
    }

    @GetMapping("/grouping")
    public ResponseEntity<Map<Integer,Long>> getPersonasGroupin(){
        //Interface Funcional Stream
        return ResponseEntity.ok(getListPersonas().stream()
                //collect -> Pertence a la interface Stream
                .collect(Collectors.groupingBy(Persona::getEdad, Collectors.counting())));
    }

    @GetMapping("/map")
    public ResponseEntity<List<String>> getPersonasMapFunction(){
        //Predicado -> con una FUnción
        //Forma1
        //Function<Persona, String> mayusculas = persona -> persona.getNombre().toUpperCase();
        //Interface Funcional Stream
        return ResponseEntity.ok(getListPersonas().stream()
                //Operador Map
                //Forma1
                //.map(mayusculas)
                //Forma2
                .map(persona -> persona.getNombre().toUpperCase())
                //Pasar el resultado a una lista deseada.
                .toList());
    }

    @GetMapping("/supplier")
    public ResponseEntity<String> getPersonasSupplier(){
        //Supplier -> con una FUnción
        Supplier<String> porDefecto = () -> "No hay datos";
        //Lista Vacia
        List<Persona> perso = new ArrayList<>();
        return ResponseEntity.ok(perso.stream()
                .findFirst()
                .map(Persona::getNombre)
                .orElseGet(porDefecto));
    }

    private List<Persona> getListPersonas(){
        return Arrays.asList(
                new Persona("Paul","Rodriguez","codigo@tecsup.com",27),
                new Persona("Andres","Taboada","andres@tecsup.com",35),
                new Persona("Angel","Scaramutti","angel@tecsup.com",28),
                new Persona("Cristian","Blaz","cristian@tecsup.com",28),
                new Persona("Denis","Sabino","denis@tecsup.com",31),
                new Persona("Lincol","Dolores","lincol@tecsup.com",35)
                //new Persona("Lincol1","Dolores","lincol@tecsup.com",30)
        );
    }
}
