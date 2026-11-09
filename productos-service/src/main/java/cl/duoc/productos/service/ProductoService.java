package cl.duoc.productos.service;

import cl.duoc.productos.model.Producto;
import cl.duoc.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    public Producto getOne(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto update(Long id, Producto producto) {
        Producto productoActualizado = productoRepository.findById(id).orElse(null);

        if (productoActualizado == null) {
            return null;
        }

        return productoRepository.save(producto);
    }
}
