package br.com.farmonline.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.farmonline.usuario.Usuario;
import br.com.farmonline.usuario.UsuarioRN;

@ManagedBean(name = "usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5378234700750836293L;
	private Usuario usuario = new Usuario();
	private String confirmarSenha;
	private List<Usuario> lista;
	private String destinoSalvar;
	private boolean logado;

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

	public String logar() {
		UsuarioRN usuarioRN = new UsuarioRN();
		FacesContext context = FacesContext.getCurrentInstance();
		Usuario usuarioLogin = usuarioRN.buscarPorEmail(this.getUsuario().getEmail());

		if (usuarioLogin.getCodigo() == null || usuarioLogin.getCodigo() == 0) {
			FacesMessage facesMessage = new FacesMessage("Usuário não localizado, tente novamente!");
			context.addMessage(null, facesMessage);
			this.setLogado(false);
			return null;

		} else if (!(this.usuario.getSenha().equals(usuarioLogin.getSenha()))) {
			this.usuario = null;
			FacesMessage facesMessage = new FacesMessage("Senha incorreta, verifique os dados e tente novamente!");
			context.addMessage(null, facesMessage);
			this.setLogado(false);
			return null;

		}

		else {
			this.setLogado(true);
			this.setUsuario(usuarioLogin);

			if (this.usuario.getPermissao().contains("ROLE_ADMINISTRADOR")) {
				this.destinoSalvar = "/admin/principal";
			}
			if (this.usuario.getPermissao().contains("ROLE_VIP")) {
				this.destinoSalvar = "/restrito/principal";
			}
			if ((!this.usuario.getPermissao().contains("ROLE_VIP"))
					&& (!this.usuario.getPermissao().contains("ROLE_ADMINISTRADOR"))) {
				this.destinoSalvar = "/usuariologadoteste";
			}

		}
		return this.destinoSalvar;

	}

	public String novo() {
		this.destinoSalvar = "/usuario";
		this.usuario = new Usuario();
		this.usuario.setAtivo(true);
		return this.destinoSalvar;
	}

	public String editar() {
		this.confirmarSenha = this.usuario.getSenha();
		return "/usuario";
	}

	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		UsuarioRN usuarioRN = new UsuarioRN();
		String email = this.usuario.getEmail();
		String senha = this.usuario.getSenha();
		String cpf = this.usuario.getCpf();
		Usuario usuarioBuscado = usuarioRN.buscarPorEmail(email);
		if (this.confirmarTermos == false) {
			FacesMessage facesMessage = new FacesMessage(
					this.usuario.getNome() + " Para prosseguir, aceite os termos " + this.confirmarTermos);
			context.addMessage(null, facesMessage);
			this.destinoSalvar = null;
		} else {
			context = FacesContext.getCurrentInstance();
			if (this.confirmarSenha.equalsIgnoreCase(this.usuario.getSenha())) {
				FacesMessage facesMessage = new FacesMessage("Senha confirmada incorretamente");
				context.addMessage(null, facesMessage);
				this.destinoSalvar = null;
			} else {

				if (this.usuario.getEmail().equals(usuarioRN.buscarPorEmail(this.usuario.getEmail()))
						&& (this.usuario.getSenha().equals(usuarioRN.buscarPorEmail(this.usuario.getSenha())))) {

					this.usuario = usuarioRN.buscarPorEmail(email);
					this.destinoSalvar = "/login";
				}
			}

			this.destinoSalvar = "/login";

			usuarioRN.salvar(this.usuario);
		}
		return this.destinoSalvar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((confirmarSenha == null) ? 0 : confirmarSenha.hashCode());
		result = prime * result + (confirmarTermos ? 1231 : 1237);
		result = prime * result + ((destinoSalvar == null) ? 0 : destinoSalvar.hashCode());
		result = prime * result + ((lista == null) ? 0 : lista.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioBean other = (UsuarioBean) obj;
		if (confirmarSenha == null) {
			if (other.confirmarSenha != null)
				return false;
		} else if (!confirmarSenha.equals(other.confirmarSenha))
			return false;
		if (confirmarTermos != other.confirmarTermos)
			return false;
		if (destinoSalvar == null) {
			if (other.destinoSalvar != null)
				return false;
		} else if (!destinoSalvar.equals(other.destinoSalvar))
			return false;
		if (lista == null) {
			if (other.lista != null)
				return false;
		} else if (!lista.equals(other.lista))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String excluir() {
		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.excluir(this.usuario);
		this.lista = null;
		return null;
	}

	public boolean confirmarTermos;

	public boolean confirmar() {
		if (this.confirmarTermos == true) {
			this.confirmarTermos = false;
		} else {
			this.confirmarTermos = true;
		}

		return this.confirmarTermos;

	}

	public boolean isConfirmarTermos() {
		return confirmarTermos;
	}

	public void setConfirmarTermos(boolean confirmarTermos) {
		this.confirmarTermos = confirmarTermos;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
	}

	public String ativar() {
		if (this.usuario.isAtivo())
			this.usuario.setAtivo(false);
		else
			this.usuario.setAtivo(true);

		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.salvar(this.usuario);
		return null;
	}

	public List<Usuario> getLista() {
		if (this.lista == null) {
			UsuarioRN usuarioRN = new UsuarioRN();
			this.lista = usuarioRN.listar();
		}
		return this.lista;
	}

	public String atribuiPermissao(Usuario usuario, String permissao) {
		this.usuario = usuario;
		java.util.Set<String> permissoes = this.usuario.getPermissao();
		if (permissoes.contains(permissao)) {
			permissoes.remove(permissao);
		} else {
			permissoes.add(permissao);
		}
		return null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

}
