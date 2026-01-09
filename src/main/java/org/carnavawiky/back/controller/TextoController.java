package org.carnavawiky.back.controller;

import org.carnavawiky.back.model.Texto;
import org.carnavawiky.back.service.TextoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/textos")
@CrossOrigin(origins = "*")
public class TextoController {

    @Autowired
    private TextoService textoService;

    @GetMapping
    public List<Texto> getAll() {
        return textoService.findAll();
    }

    @GetMapping("/{key}")
    public ResponseEntity<Texto> getByKey(@PathVariable String key) {
        return ResponseEntity.ok(textoService.findByKey(key));
    }

    @PostMapping
    public Texto create(@RequestBody Texto texto) {
        return textoService.save(texto);
    }

    /**
     * Endpoint para guardado masivo de textos.
     */
    @PostMapping("/bulk")
    public ResponseEntity<Void> updateBulk(@RequestBody List<Texto> textos) {
        textoService.saveAll(textos);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{key}")
    public Texto update(@PathVariable String key, @RequestBody String value) {
        return textoService.update(key, value);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        textoService.delete(key);
        return ResponseEntity.noContent().build();
    }
}