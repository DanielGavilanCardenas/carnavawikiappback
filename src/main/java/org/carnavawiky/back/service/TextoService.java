package org.carnavawiky.back.service;

import org.carnavawiky.back.model.Texto;
import org.carnavawiky.back.repository.TextoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TextoService {

    @Autowired
    private TextoRepository textoRepository;

    public List<Texto> findAll() {
        return textoRepository.findAll();
    }

    public Texto findByKey(String key) {
        return textoRepository.findByKey(key)
                .orElseThrow(() -> new RuntimeException("Key no encontrada: " + key));
    }

    public Texto save(Texto texto) {
        return textoRepository.save(texto);
    }

    @Transactional
    public Texto update(String key, String newValue) {
        Texto texto = findByKey(key);
        texto.setValue(newValue);
        return textoRepository.save(texto);
    }

    @Transactional
    public void delete(String key) {
        textoRepository.deleteByKey(key);
    }
}