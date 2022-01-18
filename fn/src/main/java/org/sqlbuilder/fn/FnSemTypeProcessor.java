package org.sqlbuilder.fn;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.SemTypeType;
import edu.berkeley.icsi.framenet.SemTypeType.SuperType;
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
		final int count = 0;
		final File xmlFile = new File(fileName);
		// System.out.printf("file=<%s>\n", xmlFile);
		try
		{
			final SemTypesDocument document = SemTypesDocument.Factory.parse(xmlFile);

			final SemTypeType[] semtypes = document.getSemTypes().getSemTypeArray();
			for (final SemTypeType semtype : semtypes)
			{
				final FnSemType fnSemType = new FnSemType(semtype);
				FnSemType.SET.add(fnSemType);

				final SuperType[] supertypes = semtype.getSuperTypeArray();
				for (final SuperType supertype : supertypes)
				{
					//final FnSemType fnSemType2 = new FnSemType(supertype);
					final FnSemTypeSuper fnSemSuperType = new FnSemTypeSuper(fnSemType, supertype);
					FnSemTypeSuper.SET.add(fnSemSuperType);
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
