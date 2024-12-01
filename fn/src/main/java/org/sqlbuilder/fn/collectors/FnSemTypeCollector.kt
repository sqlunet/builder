package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.joins.SemType_SemTypeSuper;
import org.sqlbuilder.fn.objects.SemType;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.SemTypesDocument;

public class FnSemTypeCollector extends FnCollector1
{
	public FnSemTypeCollector(final String filename, final Properties props)
	{
		super(filename, props, "semtype");
	}

	@Override
	protected void processFrameNetFile(final String fileName)
	{
		final File xmlFile = new File(fileName);
		try
		{
			final SemTypesDocument _document = SemTypesDocument.Factory.parse(xmlFile);

			for (var _semtype : _document.getSemTypes().getSemTypeArray())
			{
				final SemType semtype = SemType.make(_semtype);
				for (var _supertype : _semtype.getSuperTypeArray())
				{
					SemType_SemTypeSuper.make(semtype, _supertype);
				}
			}
		}
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e);
		}
	}
}
