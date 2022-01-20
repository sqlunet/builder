package org.sqlbuilder.fn;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.SemTypesDocument;

public class FnSemTypeProcessor extends FnProcessor1
{
	public FnSemTypeProcessor(final String filename, final Properties props)
	{
		super(filename, props, "semtype");
	}

	@Override
	protected int processFrameNetFile(final String fileName, final String name)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (semtype)", name);
		}
		final File xmlFile = new File(fileName);
		try
		{
			final SemTypesDocument _document = SemTypesDocument.Factory.parse(xmlFile);

			for (var _semtype : _document.getSemTypes().getSemTypeArray())
			{
				final FnSemType semtype = new FnSemType(_semtype);
				FnSemType.SET.add(semtype);

				for (var _supertype : _semtype.getSuperTypeArray())
				{
					final FnSemTypeSuper semsupertype = new FnSemTypeSuper(semtype, _supertype);
					FnSemTypeSuper.SET.add(semsupertype);
				}
			}
		}
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, this.tag, "xml-document", fileName, -1, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (semtype)", name);
		}
		return 1;
	}
}
