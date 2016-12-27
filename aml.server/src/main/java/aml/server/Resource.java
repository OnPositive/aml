package aml.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.aml.apigatewayimporter.ApiImporter;
import org.aml.apimodel.Api;
import org.aml.apimodel.Library;
import org.aml.apimodel.impl.TopLevelModelImpl;
import org.aml.registry.operations.StoreRegistry;
import org.aml.swagger.writer.SwaggerWriter;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.TypeRegistryImpl;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.aml.typesystem.yamlwriter.RamlWriter;

import com.amazonaws.services.apigateway.model.ImportRestApiResult;
import com.google.common.io.Files;
import com.google.gson.JsonObject;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.ClientOpts;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.languages.DartClientCodegen;
//AWSAccessKeyId=AKIAJKZEPGFD2KSJQKNQ
//AWSSecretKey=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
@Path("home")
public class Resource {
	@POST
	@Path("aws")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld(

			String content) {
		JsonObject obj = new JsonObject();
		try{
		TopLevelRamlImpl build = (TopLevelRamlImpl) TopLevelRamlModelBuilder.build(content);
		
		obj.addProperty("correct", build.isOk());
		ImportRestApiResult doImport = new ApiImporter(null,"AKIAJKZEPGFD2KSJQKNQ","GwBbZPXOBpoXrZRgBF0bGYHj0rMS5ulLDwKUZeb5").doImport((Api) build);
		System.out.println("Successfully imported:"+doImport.getId()+"("+doImport.getName()+")");	
		obj.addProperty("resultUrl", "https://console.aws.amazon.com/apigateway/home?region=us-east-1#/apis");
	}catch (Throwable e){
		StringWriter out = new StringWriter();
		e.printStackTrace(new PrintWriter(out));
		obj.addProperty("result", out.toString());
	}
		return obj.toString();
	}
	
	static HashMap<String,String>convertedSpecifications=new HashMap<String,String>();
	static HashMap<String,File>files=new HashMap<String,File>();
	
	@GET
	@Path("tools")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTools() {
		return new StoreRegistry().apply(new LanguageList().buildRegitry());
	}
	
	@POST
	@Path("swagger")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSwagger(
			String content) {
		TopLevelRamlImpl build = (TopLevelRamlImpl) TopLevelRamlModelBuilder.build(content);
		JsonObject obj = new JsonObject();
		obj.addProperty("correct", build.isOk());
		try{
		SwaggerWriter wr=new SwaggerWriter();
		String result=wr.store((Api) build);
		String id=""+convertedSpecifications.size();
		convertedSpecifications.put(id, result);
		obj.addProperty("resultUrl", "/home/spec/"+id);
		}catch (Throwable e){
			StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			obj.addProperty("result", out.toString());
		}
		return obj.toString();
	}
	
	public static void zipDir(String zipFileName, String dir)  {
		try{
		File dirObj = new File(dir);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		System.out.println("Creating : " + zipFileName);
		addDir(new File(dir),dirObj, out);
		out.close();
		}catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private static void addDir(File root, File dirObj, ZipOutputStream out) throws IOException {
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(root,files[i], out);
				continue;
			}
			FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
			System.out.println(" Adding: " + files[i].getAbsolutePath());
			String substring = files[i].getAbsolutePath().substring(root.getAbsolutePath().length()+1);
			out.putNextEntry(new ZipEntry(substring));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}
	
	@POST
	@Path("gen/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getClient(
			@PathParam("id")
			String lid,
			String content) {
		
		TopLevelRamlImpl build = (TopLevelRamlImpl) TopLevelRamlModelBuilder.build(content);
		JsonObject obj = new JsonObject();
		obj.addProperty("correct", build.isOk());
		SwaggerWriter wr=new SwaggerWriter();
		String result=wr.store((Api) build);
		String id=""+convertedSpecifications.size();
		convertedSpecifications.put(id, result);
		//obj.addProperty("resultUrl", "/home/spec/"+id);
		io.swagger.codegen.DefaultGenerator gen=new DefaultGenerator();
		File createTempDir = Files.createTempDir();
		ClientOptInput opts = new ClientOptInput();
		ClientOpts opts2 = new ClientOpts();
		opts.setOpts(opts2);
		
		opts.setSwagger(wr.toSwaggerObject((Api) build));
		try {
			opts.setConfig((CodegenConfig) Class.forName(lid).newInstance());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashMap<String,String>options=new HashMap<>();
		build.annotations().forEach(x->{
			System.out.println(x.getName());
			if (x.getName().startsWith(opts.getConfig().getClass().getName().replace('.', '_'))){
				String nm=x.getName().substring(opts.getConfig().getClass().getName().length()+1);
				String val=""+x.value();
				options.put(nm, val);				
			}
			
		});
		opts2.setProperties(options);
		opts.getConfig().processOpts();
		opts.getConfig().setOutputDir(createTempDir.getAbsolutePath());
		gen.opts(opts);
		List<File> generate = gen.generate();
		try {
			File createTempFile = File.createTempFile("library", ".zip");
			String fid=""+files.size();
			files.put(fid, createTempFile);
			obj.addProperty("resultUrl", "/home/zipFile/"+fid);
			zipDir(createTempFile.getAbsolutePath(), createTempDir.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj.toString();
	}
	
	@GET
	@Path("zipFile/{id}")
	@Produces("application/zip")
	public Response getFile(@PathParam("id")
	String id) {
	    File f = files.get(id);

	    if (!f.exists()) {
	        throw new WebApplicationException(404);
	    }

	    return Response.ok(f)
	            .header("Content-Disposition",
	                    "attachment; filename=library.zip").build();
	}
	
	@GET
	@Path("spec/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String convertedSpec(
			@PathParam("id")
			String id) {
		return convertedSpecifications.get(id);
	}
	
	
	@GET
	@Path("lib/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String codeGenLib(
			@PathParam("id")
			String id) {
		try {
			CodegenConfig cfg=(CodegenConfig) Class.forName(id.replace('_', '.')).newInstance();
			
			TopLevelModelImpl impl=new org.aml.apimodel.impl.TopLevelModelImpl();
			List<CliOption> cliOptions = cfg.cliOptions();
			for (CliOption o:cliOptions){
				AbstractType base=BuiltIns.STRING;
				if (o.getType().equals("string")){
					base=BuiltIns.STRING;
				}
				if (o.getType().equals("boolean")){
					base=BuiltIns.BOOLEAN;
				}
				AbstractType derive = TypeOps.derive(o.getOpt(), base);
				derive.addMeta(new Description(o.getDescription()));
				TypeRegistryImpl im=(TypeRegistryImpl) impl.annotationTypes();
				im.registerType(derive);
			}
			RamlWriter wr=new RamlWriter();
			String store = wr.store(impl.types(), impl.annotationTypes());
			return store;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedSpecifications.get(id);
	}
	

	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorldGet() {
		return "Hello world";
	}
}