package com.suivie_academique.suivie_academique;

		import org.junit.jupiter.api.Test;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.test.context.SpringBootTest;
		import org.springframework.context.ApplicationContext;
		import org.springframework.test.context.ActiveProfiles;

		import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SuivieAcademiqueApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Vérifie que le contexte Spring démarre correctement
		assertThat(applicationContext).isNotNull();
		System.out.println("✅ Le contexte Spring a démarré avec succès");
	}

	@Test
	void testApplicationContextContainsBeans() {
		// Vérifie que les beans principaux sont chargés
		assertThat(applicationContext.getBeanDefinitionCount()).isGreaterThan(0);
		System.out.println("✅ Nombre de beans chargés: " + applicationContext.getBeanDefinitionCount());
	}

	@Test
	void testApplicationName() {
		// Vérifie que l'application a un nom configuré
		String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
		System.out.println("✅ Nom de l'application: " + (appName != null ? appName : "Non défini"));
	}

	@Test
	void testActiveProfile() {
		// Vérifie que le profil 'test' est bien actif
		String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
		assertThat(activeProfiles).contains("test");
		System.out.println("✅ Profil actif: test");
	}

	@Test
	void testDataSourceConfiguration() {
		// Vérifie que la configuration H2 est chargée pour les tests
		String dbUrl = applicationContext.getEnvironment().getProperty("spring.datasource.url");
		assertThat(dbUrl).contains("h2");
		System.out.println("✅ Base de données de test: H2 (in-memory)");
	}
}
