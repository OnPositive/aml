package org.aml.registry.operations;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.aml.registry.internal.HTTPUtil;
import org.aml.registry.internal.LocalRegistry;
import org.aml.registry.model.ApiDescription;
import org.aml.registry.model.ItemDescription;
import org.aml.registry.model.LibraryDescription;
import org.aml.registry.model.Registry;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class RegistryMaterialize implements Function<Registry, Registry> {

	private LocalRegistry localRegistry;
	private boolean clean;
	protected HashSet<String> requested = new HashSet<>();

	protected ArrayList<ParseInfo> infos = new ArrayList<>();

	class ParseInfo {
		public ParseInfo(ItemDescription d2, Future<String> readStringAsync) {
			this.item = d2;
			this.location = readStringAsync;
		}

		ItemDescription item;
		Future<String> location;

		public void materialize(LocalRegistry localRegistry) throws IOException {
			try {
				String str = location.get();
				File localFileFor = localRegistry.getLocalFileFor(item.getLocation());
				localFileFor.getParentFile().mkdirs();

				boolean inUses = false;
				boolean waitUses = false;
				boolean readKey = true;
				HashSet<String> includes = new HashSet<>();
				if (item.getLocation().endsWith(".raml") || item.getLocation().endsWith(".yml")||item.getLocation().endsWith(".yaml")) {
					boolean loadDependencies = loadDependencies(str, inUses, waitUses, readKey, includes);
					if (!loadDependencies) {
						System.err.println(item.getLocation());
						;
					}
				}
				Files.write(localFileFor.toPath(), Collections.singletonList(str));
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} catch (ExecutionException e) {
				throw new IllegalStateException(e);
			}
		}

		public boolean loadDependencies(String str, boolean inUses, boolean waitUses, boolean readKey,
				HashSet<String> includes) throws IOException {
			Iterable<Event> parse = new Yaml().parse(new StringReader(str));
			try {
				for (Event e : parse) {
					if (e instanceof MappingStartEvent && waitUses) {
						waitUses = false;
						inUses = true;
					}
					if (inUses && e instanceof MappingEndEvent) {
						inUses = false;
					}
					if (e instanceof ScalarEvent) {
						if (inUses) {
							if (readKey) {
								readKey = false;
								continue;
							} else {
								includes.add(((ScalarEvent) e).getValue());
								readKey = true;
							}
						}
						ScalarEvent sc = (ScalarEvent) e;
						if (sc.getTag() != null && sc.getTag().equals("!include")) {
							includes.add(sc.getValue());
						}
						if (sc.getValue() != null && sc.getValue().equals("uses")) {
							waitUses = true;
						}
					}
				}
				if (!includes.isEmpty()) {
					for (String s : includes) {
						String fullUrl = null;
						if (s.startsWith("http://") || s.startsWith("https://")) {
							fullUrl = s;
						} else {
							int li = item.getLocation().lastIndexOf('/');
							if (!s.startsWith("/")) {
								s = "/" + s;
							}
							fullUrl = item.getLocation().substring(0, li) + s;
						}
						synchronized (RegistryMaterialize.class) {
							if (requested.add(fullUrl)) {
								Future<String> readStringAsync = HTTPUtil.readStringAsync(fullUrl);
								ItemDescription d2 = new ItemDescription();
								d2.setLocation(fullUrl);
								ParseInfo info = new ParseInfo(d2, readStringAsync);
								infos.add(info);
							}

						}
					}
					parseDeps();

				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	public RegistryMaterialize(String file, boolean clean) {
		localRegistry = new LocalRegistry(file);
		this.clean = clean;
	}

	public RegistryMaterialize(LocalRegistry reg) {
		localRegistry = reg;
	}

	public Registry apply(Registry t) {
		long t0 = System.currentTimeMillis();
		ArrayList<ParseInfo> pi = new ArrayList<RegistryMaterialize.ParseInfo>();
		for (ApiDescription d : t.getApis()) {
			process(pi, d);
		}
		for (LibraryDescription d : t.getLibraries()) {
			process(pi, d);
		}
		for (ParseInfo p : pi) {
			try {
				p.materialize(localRegistry);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		long t1 = System.currentTimeMillis();
		System.out.println("Items resolved:" + (t.getApis().size() + t.getLibraries().size()));
		System.out.println("Total time:" + (t1 - t0) + "ms");
		Registry rs = new Registry();
		for (ApiDescription d : t.getApis()) {
			ApiDescription clone = (ApiDescription) d.clone();
			clone.setLocation(localRegistry.getLocalFileFor(clone.getLocation()).getAbsolutePath());
			rs.getApis().add(clone);
		}
		parseDeps();
		for (LibraryDescription d : t.getLibraries()) {
			LibraryDescription clone = (LibraryDescription) d.clone();
			clone.setLocation(localRegistry.getLocalFileFor(clone.getLocation()).getAbsolutePath());
			rs.getLibraries().add(clone);
		}

		return t;
	}

	public void parseDeps() {
		while (true) {
			ArrayList<ParseInfo> copy = new ArrayList<>(infos);
			synchronized (RegistryMaterialize.class) {
				if (infos.isEmpty()) {
					break;
				} else {
					infos.clear();
				}
			}
			for (ParseInfo p : copy) {
				try {
					System.out.println(p.item.getLocation());
					p.materialize(localRegistry);
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}

	public void process(ArrayList<ParseInfo> pi, ItemDescription d) {
		String locaiton = d.getLocation();
		try {
			File localFileFor = localRegistry.getLocalFileFor(locaiton);
			if (!localFileFor.exists() || clean) {
				requested.add(locaiton);
				Future<String> readStringAsync = HTTPUtil.readStringAsync(locaiton);
				ParseInfo info = new ParseInfo(d, readStringAsync);
				pi.add(info);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public LocalRegistry getLocalRegistry() {
		return localRegistry;
	}
}