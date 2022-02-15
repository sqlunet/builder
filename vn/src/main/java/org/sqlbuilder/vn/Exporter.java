package org.sqlbuilder.vn;

import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.vn.joins.Class_Role;
import org.sqlbuilder.vn.objects.Role;
import org.sqlbuilder.vn.objects.RoleType;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Serialize;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;

public class Exporter
{
	private static final Comparator<Pair<String, String>> NONSERIALIZABLE_COMPARATOR = Comparator.comparing(Pair<String, String>::getFirst).thenComparing(Pair::getSecond);

	private static final Comparator<Pair<String, String>> COMPARATOR = (Comparator<Pair<String, String>> & Serializable) (p1, p2) -> NONSERIALIZABLE_COMPARATOR.compare(p1, p2);

	protected final Names names;

	protected File outDir;

	public Exporter(final Properties conf)
	{
		this.names = new Names("vn");
		this.outDir = new File(conf.getProperty("vn_serdir", "sers"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

	}

	public void export() throws IOException
	{
		System.err.printf("%s %d%n", "roletypes", RoleType.COLLECTOR.size());
		System.err.printf("%s %d%n", "roles", Role.COLLECTOR.size());
		System.err.printf("%s %d%n", "classes", VnClass.COLLECTOR.size());
		System.err.printf("%s %d%n", "classes_roles", Class_Role.SET.size());

		var m = makeMap();
		//m.forEach((strs, nids) -> System.out.printf("%s -> %s%n", strs, nids));
		Serialize.serialize(m, new File(outDir, names.serfile("classes_roles.resolve")));
	}

	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeMap() throws IOException
	{
		try ( //
		      @ProvidesIdTo(type = RoleType.class) var ignored1 = RoleType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Role.class) var ignored2 = Role.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnClass.class) var ignored3 = VnClass.COLLECTOR.open(); //
		)
		{
			return Class_Role.SET.stream() //
					.sorted(Class_Role.COMPARATOR) //
					.map(p -> new Pair<>( //
							new Pair<>(p.getClazz().getName(), p.getRole().getRoleType().getType()), //
							new Triplet<>(p.getClazz().getIntId(), p.getRole().getIntId(), p.getRole().getRoleType().getIntId()))) //
					.collect(toMap(Pair::getFirst, Pair::getSecond, (existing, replacement) -> {
						throw new RuntimeException(existing + " > " + replacement);
					}, () -> new TreeMap<>(COMPARATOR)));
		}
	}
}
