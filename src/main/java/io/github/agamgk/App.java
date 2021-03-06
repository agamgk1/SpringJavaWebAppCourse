package io.github.agamgk;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.*;

public class App {
    public static void main(String[] args) throws Exception {
        //inicjalizacja servera jetty + webapp
        var webapp = new WebAppContext();
        webapp.setResourceBase("src/main/webapp");

        // dodatkowa, niezbedna konfiguracja
        webapp.setConfigurations(new Configuration[]
                {
                        new AnnotationConfiguration(),
                        new WebInfConfiguration(),
                        new WebXmlConfiguration(),
                        new MetaInfConfiguration(),
                        new FragmentConfiguration(),
                        new EnvConfiguration(),
                        new PlusConfiguration(),
                        new JettyWebXmlConfiguration()
                });
        webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/classes/.*");
    //    webapp.addServlet(HelloServlet.class, "/app/*");
        var server = new Server(8080);
        server.setHandler(webapp);
        //nasłuchiwacz na konkretne zdarzenie serwera Jetty
        server.addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {
            @Override
            public void lifeCycleStopped(LifeCycle event) {
                // w momencie kiedy Jetty zatrzymuje sie to wołamy
                HibernateUtil.close();
            }
        });
        server.start();
        server.join();
    }
}
