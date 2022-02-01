package conf;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/*" }; }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SecurityConfig.class };
    }


    // Dispatcher Servlet
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { DbConfig.class, HsqlDataSource.class }; }

}
