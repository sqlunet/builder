package org.sqlbuilder;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.annotations.ProvidesIdTo2;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;

import java.io.FileNotFoundException;

@ProvidesIdTo(type=Test.class)
@ProvidesIdTo2("Type")
public class Test
{
	@ProvidesIdTo2("StaticField")
	static SetCollector<@ProvidesIdTo2("TypeUse") String> COLLECTOR = new SetCollector<>(String::compareToIgnoreCase);

	@ProvidesIdTo2("Field") int myfields = 0;

	@ProvidesIdTo2("Constructor")
	public Test()
	{
	}

	@RequiresIdFrom(type=Test.class)
	@ProvidesIdTo2("Method")
	public void mymethod(@ProvidesIdTo2("Parameter") String myparam) throws FileNotFoundException
	{
		reload();
		@ProvidesIdTo2("LocalVar") SetCollector<String> mylocalvar = COLLECTOR.open();
		mylocalvar.close();

		reload();
		try (@ProvidesIdTo2("LocalVar") SetCollector<String> mylocalvar1 = COLLECTOR.open())
		{
			System.out.println(mylocalvar1.get("test"));
		}

		reload();
		try (@ProvidesIdTo2("LocalVar") var mylocalvar2 = COLLECTOR.open())
		{
			@ProvidesIdTo2("LocalVar") var mylocalvar3 = COLLECTOR.open();
			System.out.println(mylocalvar2.get("test"));
		}
	}

	public void reload()
	{
		COLLECTOR.add("test");
		COLLECTOR.add("test1");
		COLLECTOR.add("test2");
		COLLECTOR.add("test3");
	}
}
