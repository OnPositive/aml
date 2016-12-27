package aml.server;

import org.aml.registry.model.Registry;
import org.aml.registry.model.ToolDescription;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.languages.AkkaScalaClientCodegen;
import io.swagger.codegen.languages.AndroidClientCodegen;
import io.swagger.codegen.languages.AspNet5ServerCodegen;
import io.swagger.codegen.languages.AsyncScalaClientCodegen;
import io.swagger.codegen.languages.CSharpClientCodegen;
import io.swagger.codegen.languages.ClojureClientCodegen;
import io.swagger.codegen.languages.CppRestClientCodegen;
import io.swagger.codegen.languages.CsharpDotNet2ClientCodegen;
import io.swagger.codegen.languages.DartClientCodegen;
import io.swagger.codegen.languages.FlashClientCodegen;
import io.swagger.codegen.languages.FlaskConnexionCodegen;
import io.swagger.codegen.languages.GoClientCodegen;
import io.swagger.codegen.languages.GoServerCodegen;
import io.swagger.codegen.languages.GroovyClientCodegen;
import io.swagger.codegen.languages.HaskellServantCodegen;
import io.swagger.codegen.languages.JavaCXFServerCodegen;
import io.swagger.codegen.languages.JavaClientCodegen;
import io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen;
import io.swagger.codegen.languages.JavaJerseyServerCodegen;
import io.swagger.codegen.languages.JavaResteasyServerCodegen;
import io.swagger.codegen.languages.NancyFXServerCodegen;
import io.swagger.codegen.languages.PerlClientCodegen;
import io.swagger.codegen.languages.PythonClientCodegen;
import io.swagger.codegen.languages.ScalaClientCodegen;

public class LanguageList {

	
	static class Language{
		String name;
		String icon;
		Class<?>clazz;
		public Language(String name, String icon, Class<?> clazz) {
			super();
			this.name = name;
			this.icon = icon;
			this.clazz = clazz;
		}
	}
	
	Language[]langs=new Language[]{
		new Language("Acca Scala Client", "http://akka.io/", AkkaScalaClientCodegen.class),
		new Language("Android client", "https://www.android.com/", AndroidClientCodegen.class),
		new Language("ASP Net5 Server Stubs", "https://www.asp.net/", AspNet5ServerCodegen.class),
		new Language("Async Scala Client", "http://www.scala-lang.org/", AsyncScalaClientCodegen.class),
		new Language("Scala Client", "http://www.scala-lang.org/", ScalaClientCodegen.class),
		new Language("Clojure client", "http://clojure.org/", ClojureClientCodegen.class),
		//new Language("Confluence Wiki", "atlassian.com", ConfluenceWikiGenerator.class),
		new Language("C++ Rest Client", "http://cppstudio.com", CppRestClientCodegen.class),
		new Language("C# Client", "http://mycsharp.ru/", CSharpClientCodegen.class),
		new Language("C# .Net 2.0 Client", "http://mycsharp.ru/", CsharpDotNet2ClientCodegen.class),
		new Language("Dart Client", "http://dartlang.org", DartClientCodegen.class),
		new Language("Flash Client", "adobe.com", FlashClientCodegen.class),
		new Language("Flask Connexion Client", "adobe.com", FlaskConnexionCodegen.class),
		new Language("Go Client", "https://golang.org/", GoClientCodegen.class),
		new Language("Go Server stubs", "https://golang.org/", GoServerCodegen.class),
		new Language("Groovy Client", "http://www.groovy-lang.org/", GroovyClientCodegen.class),
		new Language("Haskell Servant", "https://www.haskell.org/", HaskellServantCodegen.class),
		new Language("Java Client", "https://java.com/", JavaClientCodegen.class),
		new Language("Java CXF Server Stubs", "http://cxf.apache.org/", JavaCXFServerCodegen.class),
		new Language("Java JAXRS Server Stubs", "https://jax-rs-spec.java.net/", JavaJAXRSSpecServerCodegen.class),
		new Language("Java Jersey Server Stubs", "https://jersey.java.net/", JavaJerseyServerCodegen.class),
		new Language("Java REST Easy Server Stubs", "http://resteasy.jboss.org/", JavaResteasyServerCodegen.class),
		new Language("Lumen Server", "https://lumen.laravel.com/", io.swagger.codegen.languages.LumenServerCodegen.class),
		new Language("Nancy FX Server Codegen", "http://nancyfx.org/", NancyFXServerCodegen.class),
		new Language("Php Client Codegen", "http://php.net/", io.swagger.codegen.languages.PhpClientCodegen.class),
		new Language("Python Client Codegen", "http://python.org/", PythonClientCodegen.class),
		new Language("Perl Client Codegen", "https://www.perl.org/", PerlClientCodegen.class),
		new Language("Ruby Client Codegen", "https://www.ruby-lang.org/", io.swagger.codegen.languages.RubyClientCodegen.class),
		new Language("Objective C Client", "https://developer.apple.com/", io.swagger.codegen.languages.ObjcClientCodegen.class),
		new Language("Tizen Client", "https://www.tizen.org/", io.swagger.codegen.languages.TizenClientCodegen.class),
		new Language("Swift Client", "developer.apple.com", io.swagger.codegen.languages.SwiftCodegen.class),
		new Language("Typescript Fetch Client", "http://www.typescriptlang.org/", io.swagger.codegen.languages.TypeScriptFetchClientCodegen.class),
		new Language("Typescript Node Client", "http://www.typescriptlang.org/", io.swagger.codegen.languages.TypeScriptFetchClientCodegen.class),
		new Language("Angular 2 JS Client", "https://angularjs.org/", io.swagger.codegen.languages.TypeScriptAngular2ClientCodegen.class),
		new Language("Angular JS Client", "https://angularjs.org/", io.swagger.codegen.languages.TypeScriptAngularClientCodegen.class),
		new Language("Spring Stubs", "http://spring.io/", io.swagger.codegen.languages.SpringCodegen.class),
		new Language("Slim Framework Server Stubs", "https://www.slimframework.com/", io.swagger.codegen.languages.SlimFrameworkServerCodegen.class),
		new Language("Node JS Server Stubs", "https://nodejs.org/", io.swagger.codegen.languages.NodeJSServerCodegen.class),
		new Language("Javascript Client", "http://www.enable-javascript.com/", io.swagger.codegen.languages.JavascriptClientCodegen.class),
		new Language("Rails 5 Server Stubs", "http://rubyonrails.org/", io.swagger.codegen.languages.Rails5ServerCodegen.class),
		new Language("Sinatra 5 Server Stubs", "http://www.sinatrarb.com/", io.swagger.codegen.languages.SinatraServerCodegen.class),
		new Language("Scalatra  Server Stubs", "http://www.scalatra.org/", io.swagger.codegen.languages.ScalatraServerCodegen.class),
		new Language("Silex Server", "http://silex.sensiolabs.org/", io.swagger.codegen.languages.SilexServerCodegen.class),
	};//https://lumen.laravel.com/
	
	//https://nodejs.org/en/
	//https://angularjs.org/
	//http://nancyfx.org/
	//JavaInflectorServerCodegen.class
	//JavascriptClosureAngularClientCodegen.class
	//JMeterCodegen.class
	//Qt5CPPGenerator.class
	//StaticDocCodegen.class
	//StaticHtml2Generator.class
	//StaticHtmlGenerator.class
	
	Registry buildRegitry(){
		Registry result=new Registry();
		for (Language l:langs){
			ToolDescription e = new ToolDescription();
			e.setName(l.name);
			
			if (l.icon.startsWith("http://")){
				l.icon=l.icon.substring("http://".length());
			}
			if (l.icon.startsWith("https://")){
				l.icon=l.icon.substring("https://".length());
			}
			String category="Generate Client";
			if (l.name.contains("Server")){
				category="Generate Server";
			}
			if (l.name.contains("Stubs")){
				category="Generate Server";
			}
			try {
				CodegenConfig cfg=(CodegenConfig) l.clazz.newInstance();
				cfg.cliOptions();
			} catch (InstantiationException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.setNeedsConfig(true);
			e.setLibUrl("/lib/"+l.clazz.getName().replace('.', '_'));
			e.setCategory(category);
			e.setIcon("http://favicon.yandex.net/favicon/"+l.icon);
			e.setLocation("/gen/"+l.clazz.getName());
			result.getTools().add(e);
			
		}
		return result;
	}
	
}