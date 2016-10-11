package org.aml.raml2java;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.codemodel.JResourceFile;

public final class StringResourceFile extends JResourceFile {
	private String content;

	public StringResourceFile(String name,String content) {
		super(name);
		this.content=content;
	}

	@Override
	protected void build(OutputStream os) throws IOException {
		os.write(content.getBytes("UTF-8"));
	}
}