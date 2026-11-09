package cl.duoc.ventas.service;

import cl.duoc.ventas.model.Venta;
import cl.duoc.ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    public List<Venta> getAll() {
        return ventaRepository.findAll();
    }

    public Venta getOne(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    public Venta update(Long id, Venta venta) {
        Venta ventaActualizada = ventaRepository.findById(id).orElse(null);

        if (ventaActualizada == null) {
            return null;
        }

        return ventaRepository.save(venta);
    }
}
