package cl.duoc.usuarios.service;

import cl.duoc.usuarios.model.Usuario;
import cl.duoc.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Usuario getOne(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario usuarioActualizado = usuarioRepository.findById(id).orElse(null);

        if (usuarioActualizado == null) {
            return null;
        }

        usuario.setId(usuarioActualizado.getId());

        if (usuario.getClave() != null && !usuario.getClave().isBlank()) {
            usuario.setClave(passwordEncoder.encode(usuario.getClave()));

        } else {
            usuario.setClave(usuarioActualizado.getClave()
            );
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String correo, String clave) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario != null && passwordEncoder.matches(clave, usuario.getClave())) {
            return usuario;
        }

        return null;
    }
}
