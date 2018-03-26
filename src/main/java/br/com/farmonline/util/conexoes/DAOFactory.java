package br.com.farmonline.util.conexoes;


import br.com.farmonline.usuario.UsuarioDAO;
import br.com.farmonline.usuario.UsuarioDAOHibernate;

public class DAOFactory {

	public static UsuarioDAO criarUsuarioDAO() {
		UsuarioDAOHibernate usuarioDAO = new UsuarioDAOHibernate();
		usuarioDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return usuarioDAO;
	}

	

}
