package br.com.farmonline.usuario;

import java.util.List;

import br.com.farmonline.util.conexoes.DAOFactory;

public class UsuarioRN {
	
	private UsuarioDAO usuarioDAO;

	public UsuarioRN() {
		this.usuarioDAO = DAOFactory.criarUsuarioDAO();
	}

	public Usuario carregar(Integer codigo) {
		return this.usuarioDAO.carregar(codigo);
	}

	public Usuario buscarPorEmail(String email) {
		return this.usuarioDAO.buscarPorEmail(email);
	}
	public Usuario buscarPorCPF(String cpf){
		return this.usuarioDAO.buscarPorCPF(cpf);
	}

	public void salvar(Usuario usuario) {
		Integer codigo = usuario.getCodigo();
		if (codigo == null || codigo == 0) {
			usuario.getPermissao().add("ROLE_USUARIO");
			this.usuarioDAO.salvar(usuario);
		} else {
			this.usuarioDAO.atualizar(usuario);
		}
	}

	public void excluir(Usuario usuario) {
		this.usuarioDAO.excluir(usuario);
	}

	public List<Usuario> listar() {
		return this.usuarioDAO.listar();
	}

}
