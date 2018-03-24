

import java.sql.SQLException;

import org.hibernate.Session;

public class ConectaHibernateMySQL {
	public static void main(String[] args) throws SQLException {
		Session sessao = null;
		sessao = HibernateUtil.getSessionfactory().openSession();
		
		sessao.close();
		System.out.println("desconectou");
	}
}
