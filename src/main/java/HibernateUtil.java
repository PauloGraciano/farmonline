

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			Configuration cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			StandardServiceRegistryBuilder registradorServico = new StandardServiceRegistryBuilder();
			registradorServico.applySettings(cfg.getProperties());
			StandardServiceRegistry servico = registradorServico.build();
			System.out.println("Conectou!");
			return cfg.buildSessionFactory(servico);
		}catch(Throwable erro) {
			System.out.println("Criação inicial do objeto SessionFactory fallhou. Erro: "+erro.getMessage());
			throw(new ExceptionInInitializerError(erro));
		}
	}

	public static SessionFactory getSessionfactory() {
		return sessionFactory;
	}
	
	
}
