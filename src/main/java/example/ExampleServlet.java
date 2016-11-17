package example;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ExampleServlet extends HttpServlet {

	private static final String DEFAULT_NAME = "World";

	private ProducerTemplate producer;

	@Override
	public void init() throws ServletException {
		producer = getSpringContext().getBean("exampleProducerTemplate", ProducerTemplate.class);
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String endpoint = "direct:inbox";
		String input = getName(request);
		String output = producer.requestBody(endpoint, input, String.class);

		response.getOutputStream().println(output);
	}

	private String getName(HttpServletRequest request) {
		String name = request.getParameter("name");
		if (name != null) {
			return name;
		}
		return DEFAULT_NAME;
	}

	private WebApplicationContext getSpringContext() {
		ServletContext servletContext = getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		return context;
	}
}