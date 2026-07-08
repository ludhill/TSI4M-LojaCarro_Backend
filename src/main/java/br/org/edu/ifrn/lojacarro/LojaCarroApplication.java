package br.org.edu.ifrn.lojacarro;

import br.org.edu.ifrn.lojacarro.model.Usuario;
import br.org.edu.ifrn.lojacarro.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LojaCarroApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaCarroApplication.class, args);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			if (usuarioRepository.findByUsername("root").isEmpty()) {
				Usuario root = new Usuario();
				root.setUsername("root");
				root.setPassword(passwordEncoder.encode("root"));
				root.setRole("ROLE_GERENTE");
				usuarioRepository.save(root);
			}


			if (usuarioRepository.findByUsername("vendedor1").isEmpty()) {
				Usuario vendedor = new Usuario();
				vendedor.setUsername("vendedor1");
				vendedor.setPassword(passwordEncoder.encode("123456"));
				vendedor.setRole("ROLE_VENDEDOR");
				usuarioRepository.save(vendedor);
			}
		};
	}
}