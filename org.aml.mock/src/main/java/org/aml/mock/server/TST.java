package org.aml.mock.server;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TST {

	public static void main(String[] args) {
		try {
			Template template = new Template("A",new StringReader("${s!\"false\"}"));
			StringWriter out = new StringWriter();
			template.process(new HashMap<>(), out);
			System.out.println(out.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
