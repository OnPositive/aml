package org.aml.registry.internal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.TypedResource;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.util.EncodingUtils;

public class PublishHelper {

	public static final String BRANCH_DEFAULT = "refs/heads/gh-pages";
	private GitHubClient cl;
	private RepositoryService rs;
	private DataService service;
	private String name;
	private String email;
	private String commitMessage;
	private RepositoryId id;
	
	public PublishHelper(CommitterInfo parameterObject,String commitMessage, RepositoryId id) {
		cl = new GitHubClient();
		cl.setCredentials(parameterObject.login, parameterObject.password);
		rs = new RepositoryService(cl);
		service = new DataService(cl);
		this.email=parameterObject.email;
		this.name=parameterObject.name;
		this.commitMessage=commitMessage;
		this.id=id;
	}
	
	public void commitFile(String prefixPath,String rootPath,String path) {		
		try {
			ArrayList<TreeEntry> tree2 = new ArrayList<TreeEntry>();
			tree2.add(PublishHelper.createTree(prefixPath, rootPath, path, service, id));
			commitTree(tree2);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void commitFileFromString(String prefixPath,String name,String contents) {		
		try {
			ArrayList<TreeEntry> tree2 = new ArrayList<TreeEntry>();
			tree2.add(PublishHelper.createTreeFromString(prefixPath, name, contents, service, id));
			commitTree(tree2);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void commitTree(ArrayList<TreeEntry> tree2) throws IOException, Exception {
		Repository repository = rs.getRepository(id);
		Reference ref = null;
		try {
			ref = service.getReference(repository, BRANCH_DEFAULT);
		} catch (RequestException e) {
			if (e.getStatus() != 404) {
				throw new IllegalStateException(e);
			}
		}			
		Tree currentTree = ref != null ? service.getCommit(repository, ref.getObject().getSha()).getTree() : null;
		String baseTree = null;
		if (currentTree != null)
			baseTree = currentTree.getSha();
		Tree tree = service.createTree(repository, tree2, baseTree);
		tree.setTree(tree2);
		Commit commit = createCommit();
		commit.setTree(tree);
		Commit created = service.createCommit(repository, commit);
		TypedResource object = new TypedResource();
		object.setType(TypedResource.TYPE_COMMIT).setSha(created.getSha());
		if (ref != null) {
			// Update existing reference
			ref.setObject(object);
			try {

				service.editReference(repository, ref, true);
			} catch (IOException e) {
				throw new Exception(e);
			}
		} else {
			// Create new reference
			ref = new Reference().setObject(object).setRef(BRANCH_DEFAULT);
			try {
				service.createReference(repository, ref);
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}

	private Commit createCommit() {
		Commit commit = new Commit();
		commit.setMessage(this.commitMessage);
		CommitUser author = createUser();
		commit.setAuthor(author);
		commit.setCommitter(author);
		return commit;
	}

	private CommitUser createUser() {
		CommitUser author = new CommitUser();
		author.setDate(new Date());
		author.setEmail(email);
		author.setName(name);
		return author;
	}

	protected static String createBlob(DataService service, RepositoryId repository, String prefix, String path)
			throws Exception {
		File file = new File(prefix, path);
		final long length = file.length();
		final int size = length > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) length;
		ByteArrayOutputStream output = new ByteArrayOutputStream(size);
		FileInputStream stream = new FileInputStream(file);
		try {

			final byte[] buffer = new byte[8192];
			int read;
			while ((read = stream.read(buffer)) != -1)
				output.write(buffer, 0, read);
			Blob blob = new Blob().setEncoding(Blob.ENCODING_BASE64);
			String encoded = EncodingUtils.toBase64(output.toByteArray());
			blob.setContent(encoded);
			return service.createBlob(repository, blob);
		} finally {
			stream.close();
		}
	}

	public static TreeEntry createTree(String prefix, String out, String path, DataService serv, RepositoryId id)
			throws Exception {
		TreeEntry entry = new TreeEntry();
		entry.setPath(prefix + path);
		entry.setType(TreeEntry.TYPE_BLOB);
		entry.setMode(TreeEntry.MODE_BLOB);
		entry.setSha(createBlob(serv, id, out, path));
		return entry;
	}
	public static TreeEntry createTreeFromString(String prefix, String name, String content, DataService serv, RepositoryId id)
			throws Exception {
		TreeEntry entry = new TreeEntry();
		entry.setPath(prefix + name);
		entry.setType(TreeEntry.TYPE_BLOB);
		entry.setMode(TreeEntry.MODE_BLOB);
		Blob blob = new Blob().setEncoding(Blob.ENCODING_UTF8);
		blob.setContent(content);
		entry.setSha(serv.createBlob(id, blob));
		return entry;
	}
}