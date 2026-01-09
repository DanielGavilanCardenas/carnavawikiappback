package org.carnavawiky.back.service;

import org.carnavawiky.back.model.Texto;
import org.carnavawiky.back.repository.TextoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    /**
     * Guarda o actualiza múltiples textos de forma masiva.
     */
    @Transactional
    public void saveAll(List<Texto> textos) {
        for (Texto textoEnviado : textos) {
            Optional<Texto> textoExistente = textoRepository.findByKey(textoEnviado.getKey());

            if (textoExistente.isPresent()) {
                Texto t = textoExistente.get();
                t.setValue(textoEnviado.getValue());
                textoRepository.save(t);
            } else {
                textoRepository.save(textoEnviado);
            }
        }
    }
}